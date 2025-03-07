package com.gisele.RoadandutilityInsepectionmanagementsystem.service;


import com.gisele.RoadandutilityInsepectionmanagementsystem.Domain.Role;
import com.gisele.RoadandutilityInsepectionmanagementsystem.Domain.User;
import com.gisele.RoadandutilityInsepectionmanagementsystem.dao.RoleDao;
import com.gisele.RoadandutilityInsepectionmanagementsystem.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

	// need to inject user dao
	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	private Logger logger = Logger.getLogger(getClass().getName());
	@Override
	@Transactional
	public User findByUserName(String userName) {
		// check the database if the user already exists
		return userDao.findByUserName(userName);
	}

	@Override
	@Transactional
	public User findById(Long id) {
		// check the database if the user already exists
		return userDao.findById(id);
	}

	@Override
	@Transactional
	public User findByEmail(String email) {
		// check the database if the user already exists
		return userDao.findByEmail(email);
	}

	@Override
	@Transactional
	public User findByEmailAndTelNumber(String email, String tel){
		return userDao.findByEmailAndTelNumber(email, tel);
	}

	@Override
	@Transactional
	public void save(User us, List<GrantedAuthority> authorities) {
		//User user = new User();
		// assign user details to the user object

		//user.setPassword(passwordEncoder.encode(om.getPassword()));
		us.setPassword(passwordEncoder.encode(us.getPassword()));

		User user = new User(us.getFirstName(), us.getLastName(), us.getUserName(),
		us.getEmail(), us.getTelephone(), us.getPassword());

		List<Role> roles = new ArrayList<>();
		for(GrantedAuthority auth:authorities){
			Role role = roleDao.findRoleByName(auth.toString());
			roles.add(role);
			//user.setRoles(Arrays.asList(roleDao.findRoleByName(auth.toString())));
		}
		us.setRoles(roles);
		us.setMatchingPassword(us.getPassword());
		// save user in the database
		userDao.save(us);
	}

	@Override
	@Transactional
	public void saveUpdate(User user){
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		System.out.println(user.toString());
		userDao.saveUpdate(user);
	}

//	@Override
//	@Transactional
//	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//		User user = userDao.findByUserName(userName);
//		if (user == null) {
//			throw new UsernameNotFoundException("Invalid username or password.");
//		}
//		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
//				mapRolesToAuthorities(user.getRoles()));
//	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userDao.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}
}
