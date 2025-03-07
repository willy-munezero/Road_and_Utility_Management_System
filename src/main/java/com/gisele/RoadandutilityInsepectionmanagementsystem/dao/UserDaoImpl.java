package com.gisele.RoadandutilityInsepectionmanagementsystem.dao;


import com.gisele.RoadandutilityInsepectionmanagementsystem.Domain.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private EntityManager entityManager;

	@Override
	public User findByUserName(String theUserName) {
		// get the current hibernate session
		Session currentSession = entityManager.unwrap(Session.class);

		// now retrieve/read from database using username
		Query<User> theQuery = currentSession.createQuery("from User where userName=:uName", User.class);
		theQuery.setParameter("uName", theUserName);
		User theUser = null;
		try {
			theUser = theQuery.getSingleResult();
		} catch (Exception e) {
			theUser = null;
		}

		return theUser;
	}

	public User findByEmailAndTelNumber(String email, String tel){
		// get the current hibernate session
		Session currentSession = entityManager.unwrap(Session.class);
		Query<User> theQuery = currentSession.createQuery("from User where email=:uemail and telephone=:umobile", User.class);
		theQuery.setParameter("uemail", email);
		theQuery.setParameter("umobile", tel);

		User theUser = null;
		try{
			theUser = theQuery.getSingleResult();
		}catch(Exception e){
			theUser = null;
		}
		return theUser;
	}

	@Override
	public User findByEmail(String email) {
		// get the current hibernate session
		Session currentSession = entityManager.unwrap(Session.class);

		// now retrieve/read from database using username
		Query<User> theQuery = currentSession.createQuery("from User where email=:uemail", User.class);
		theQuery.setParameter("uemail", email);
		User theEmail = null;
		try{
			theEmail = theQuery.getSingleResult();
		}catch(Exception e){
			theEmail = null;
		}
		return theEmail;
	}

	@Override
	public User findById(Long id) {
		// get the current hibernate session
		Session currentSession = entityManager.unwrap(Session.class);

		// now retrieve/read from database using username
		Query<User> theQuery = currentSession.createQuery("from User where id=:uid", User.class);
		theQuery.setParameter("uid", id);
		User theUser = null;
		try{
			theUser = theQuery.getSingleResult();
		}catch(Exception e){
			theUser = null;
		}
		return theUser;
	}

	@Override
	public void save(User theUser) {
		// get current hibernate session
		Session currentSession = entityManager.unwrap(Session.class);

		// create the user ... finally LOL
		currentSession.saveOrUpdate(theUser);
	}

	@Override
	public void saveUpdate(User theUser) {
//		// get current hibernate session
		Session currentSession = entityManager.unwrap(Session.class);
//
//		// create the user ... finally LOL
//		currentSession.saveOrUpdate(theUser);


		Query<User> query = currentSession.createQuery("update User set password = :newPassword where id = :userId");
		query.setParameter("newPassword", theUser.getPassword());
		query.setParameter("userId", theUser.getId());
		int rowCount = query.executeUpdate();
		System.out.println("Updated rows" + rowCount);
	}

	@Override
	public List<User> findUsers(int page, int size) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query query = currentSession.createQuery("SELECT u FROM User u", User.class);
		query.setFirstResult(page * size);
		query.setMaxResults(size);
		return query.getResultList();
	}
	@Override
	public long countUsers() {
		Session currentSession = entityManager.unwrap(Session.class);
		Query query = currentSession.createQuery("SELECT COUNT(u) FROM User u");
		return (long) query.getSingleResult();
	}

	@Override
	public List<User> findUsers(int page, int size, String searchTerm) {
		Session currentSession = entityManager.unwrap(Session.class);
		CriteriaBuilder criteriaBuilder = currentSession.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
		Root<User> root = criteriaQuery.from(User.class);

		if (searchTerm != null && !searchTerm.isEmpty()) {
			String pattern = "%" + searchTerm + "%";
			Predicate firstNamePredicate = criteriaBuilder.like(root.get("firstName"), pattern);
			Predicate lastNamePredicate = criteriaBuilder.like(root.get("lastName"), pattern);
			Predicate userNamePredicate = criteriaBuilder.like(root.get("userName"), pattern);
			Predicate emailPredicate = criteriaBuilder.like(root.get("email"), pattern);
			Predicate telephonePredicate = criteriaBuilder.like(root.get("telephone"), pattern);
			criteriaQuery.where(criteriaBuilder.or(firstNamePredicate, lastNamePredicate, userNamePredicate, emailPredicate, telephonePredicate));
		}

		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("lastName")), criteriaBuilder.asc(root.get("firstName")));
		TypedQuery<User> query = currentSession.createQuery(criteriaQuery);
		query.setFirstResult(page * size);
		query.setMaxResults(size);
		return query.getResultList();
	}


}
