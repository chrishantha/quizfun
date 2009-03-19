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

package quizfun.model.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import quizfun.model.dao.CourseDao;
import quizfun.model.dto.CourseSCDO;
import quizfun.model.entity.Course;
import quizfun.model.exception.DuplicateCourseException;

/**
 * @author M. Isuru Tharanga Chrishantha Perera
 */
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {

	private CourseDao courseDao;

	public void setCourseDao(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = { DuplicateCourseException.class })
	public void saveCourse(Course course) throws DuplicateCourseException {
		courseDao.saveCourse(course);
	}

	@Override
	public List<Course> findCourse(CourseSCDO courseSCDO) {
		return courseDao.findCourse(courseSCDO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Course updateCourse(Course course) {
		return courseDao.updateCourse(course);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteCourse(Course course) {
		courseDao.deleteCourse(course);
	}

	@Override
	public List<Course> findAll() {
		return courseDao.findAll();
	}
}
