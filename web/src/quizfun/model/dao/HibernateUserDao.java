/*
 * QuizFun - A quiz game
 * Copyright (C) 2008,2009
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package quizfun.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import quizfun.model.dto.CourseSCDO;
import quizfun.model.dto.UserSCDO;
import quizfun.model.entity.User;
import quizfun.model.exception.DuplicateUserException;
import quizfun.model.exception.UserNotFoundException;

/**
 * @author M. Isuru Tharanga Chrishantha Perera
 */
public class HibernateUserDao extends HibernateDaoSupport implements UserDao {

	final Logger logger = LoggerFactory.getLogger(HibernateUserDao.class);
	
	@Override
	public User findUser(String username) throws UserNotFoundException {
		if (logger.isDebugEnabled()) {
			logger.info("Finding User by username: {}", username);
		}
		Session session = getSession(false);
		try {
			User user = (User) session.get(User.class, username);
			if (user == null) {
				throw new UserNotFoundException(username);
			}
			return user;
		} catch (HibernateException ex) {
			throw convertHibernateAccessException(ex);
		}
	}

	@Override
	public void saveUser(User user) throws DuplicateUserException {
		if (logger.isDebugEnabled()) {
			logger.info("Saving User: {}", user);
		}
		Session session = getSession(false);
		try {
			User existingUser = (User) session.get(User.class, user.getUsername());
			if (existingUser != null) {
				throw new DuplicateUserException(user.toString());
			}
			session.save(user);
		} catch (HibernateException ex) {
			throw convertHibernateAccessException(ex);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> findUser(UserSCDO userSCDO) {
		if (logger.isDebugEnabled()) {
			logger.debug("Finding User(s): {}", userSCDO);
		}
		List<User> list = null;
		Session session = getSession(false);
		try {
			
			String username = userSCDO.getUsername();
			Boolean accountNonExpired = userSCDO.getAccountNonExpired();
			Boolean accountNonLocked = userSCDO.getAccountNonLocked();
			Boolean credentialsNonExpired = userSCDO.getCredentialsNonExpired();
			Boolean enabled = userSCDO.getEnabled();
			
			CourseSCDO courseSCDO = userSCDO.getCourseSCDO();
			
			String courseCode = courseSCDO.getCode();
			String courseName = courseSCDO.getName();

			Criteria criteria = session.createCriteria(User.class);
			if (username != null && !username.equals("")) {
				criteria.add(Restrictions.like("username", username, MatchMode.ANYWHERE));
			}
			if (accountNonExpired != null) {
				criteria.add(Restrictions.eq("accountNonExpired", accountNonExpired));
			}
			if (accountNonLocked != null) {
				criteria.add(Restrictions.eq("accountNonLocked", accountNonLocked));
			}
			if (credentialsNonExpired != null) {
				criteria.add(Restrictions.eq("credentialsNonExpired", credentialsNonExpired));
			}
			if (enabled != null) {
				criteria.add(Restrictions.eq("enabled", enabled));
			}
			
			criteria.addOrder(Order.asc("username"));
			
			if ((courseCode != null && !courseCode.isEmpty()) || (courseName != null && !courseName.isEmpty())) {
				criteria = criteria.createCriteria("course");
			}
	
			if (courseCode != null && !courseCode.isEmpty()) {
				criteria.add(Restrictions.like("code", courseCode, MatchMode.ANYWHERE));
			}
			if (courseName != null && !courseName.isEmpty()) {
				criteria.add(Restrictions.like("name", courseName, MatchMode.ANYWHERE));
			}
			
			list = criteria.list();
		} catch (HibernateException ex) {
			throw convertHibernateAccessException(ex);
		}
		return list;
	}

	@Override
	public User updateUser(User user) {
		if (logger.isDebugEnabled()) {
			logger.debug("Updating User: {}", user);
		}
		Session session = getSession(false);
		try {
			return (User) session.merge(user);
		} catch (HibernateException ex) {
			throw convertHibernateAccessException(ex);
		}
	}

	@Override
	public void deleteUser(User user) {
		if (logger.isDebugEnabled()) {
			logger.debug("Deleting User: {}", user);
		}
		Session session = getSession(false);
		try {
			session.delete(user);
		} catch (HibernateException ex) {
			throw convertHibernateAccessException(ex);
		}
	}

}
