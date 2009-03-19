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
import quizfun.model.entity.Course;
import quizfun.model.exception.DuplicateCourseException;

/**
 * @author M. Isuru Tharanga Chrishantha Perera
 */
public class HibernateCourseDao extends HibernateDaoSupport implements CourseDao {

	final Logger logger = LoggerFactory.getLogger(HibernateCourseDao.class);

	@Override
	public void saveCourse(Course course) throws DuplicateCourseException {
		if (logger.isDebugEnabled()) {
			logger.debug("Saving Course: {}", course);
		}
		Session session = getSession(false);
		try {
			Course existingCourse = (Course) session.get(Course.class, course.getCode());
			if (existingCourse != null) {
				throw new DuplicateCourseException(course.toString());
			}
			session.save(course);
		} catch (HibernateException ex) {
			throw convertHibernateAccessException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Course> findCourse(CourseSCDO courseSCDO) {
		if (logger.isDebugEnabled()) {
			logger.debug("Finding Course(s): {}", courseSCDO);
		}
		List<Course> list = null;
		Session session = getSession(false);
		try {
			String code = courseSCDO.getCode();
			String name = courseSCDO.getName();

			Criteria criteria = session.createCriteria(Course.class);
			if (code != null && !code.isEmpty()) {
				criteria.add(Restrictions.like("code", code, MatchMode.ANYWHERE));
			}
			if (name != null && !name.isEmpty()) {
				criteria.add(Restrictions.like("name", name, MatchMode.ANYWHERE));
			}

			criteria.addOrder(Order.asc("code"));
			list = criteria.list();
		} catch (HibernateException ex) {
			throw convertHibernateAccessException(ex);
		}
		return list;
	}

	@Override
	public Course updateCourse(Course course) {
		if (logger.isDebugEnabled()) {
			logger.debug("Updating Course: {}", course);
		}
		Session session = getSession(false);
		try {
			return (Course) session.merge(course);
		} catch (HibernateException ex) {
			throw convertHibernateAccessException(ex);
		}
	}

	@Override
	public void deleteCourse(Course course) {
		if (logger.isDebugEnabled()) {
			logger.debug("Deleting Course: {}", course);
		}
		Session session = getSession(false);
		try {
			session.delete(course);
		} catch (HibernateException ex) {
			throw convertHibernateAccessException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Course> findAll() {
		if (logger.isDebugEnabled()) {
			logger.debug("Finding all Course(s)");
		}
		List<Course> list = null;
		Session session = getSession(false);
		try {
			Criteria criteria = session.createCriteria(Course.class);
			criteria.addOrder(Order.asc("code"));
			list = criteria.list();
		} catch (HibernateException ex) {
			throw convertHibernateAccessException(ex);
		}
		return list;
	}
}
