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

package quizfun.view.bean;

import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quizfun.model.entity.Course;
import quizfun.model.exception.DuplicateCourseException;
import quizfun.view.util.JSFUtils;

/**
 * @author M. Isuru Tharanga Chrishantha Perera
 */
public class CreateCourseManagedBean extends CourseManagedBean {

	final Logger logger = LoggerFactory.getLogger(CreateCourseManagedBean.class);

	@javax.annotation.PostConstruct
	public void init() {
		course = new Course();
	}

	public void saveActionListener(ActionEvent event) {
		try {
			serviceLocator.getCourseService().saveCourse(course);
			clearValues();
			JSFUtils.addFacesInfoMessage("course.save.successful");
			codeInputText.requestFocus();
		} catch (DuplicateCourseException e) {
			JSFUtils.addFacesErrorMessage("course.save.duplicate", new Object[] { course.getCode() });
			return;
		} catch (Throwable e) {
			logger.error("Exception when saving course: " + course, e);
			JSFUtils.addApplicationErrorMessage();
			return;
		}
	}
}
