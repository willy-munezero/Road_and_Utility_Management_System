package com.gisele.RoadandutilityInsepectionmanagementsystem.service;


import com.gisele.RoadandutilityInsepectionmanagementsystem.Domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

	public User findByUserName(String userName);

	public User findById(Long id);

	public User findByEmail(String email);

	public User findByEmailAndTelNumber(String email, String tel);

	public void save(User user, List<GrantedAuthority> authorities);

	public void saveUpdate(User user);

}
