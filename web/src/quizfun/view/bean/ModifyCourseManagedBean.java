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
import quizfun.view.util.JSFUtils;

/**
 * @author M. Isuru Tharanga Chrishantha Perera
 */
public class ModifyCourseManagedBean extends CourseManagedBean {

	final Logger logger = LoggerFactory.getLogger(ModifyCourseManagedBean.class);
	
	private Course modifyingCourse;

	@javax.annotation.PostConstruct
	public void init() {
		modifyingCourse = (Course) JSFUtils.removeFromSessionMap("course");
		if (logger.isDebugEnabled()) {
			logger.debug("Object retrieved from session: {}", modifyingCourse);
		}
		course = new Course();
		course.setCode(modifyingCourse.getCode());
		course.setName(modifyingCourse.getName());
	}

	public void saveActionListener(ActionEvent event) {
		try {
			modifyingCourse.setCode(course.getCode());
			modifyingCourse.setName(course.getName());
			modifyingCourse = serviceLocator.getCourseService().updateCourse(modifyingCourse);
			if (logger.isDebugEnabled()) {
				logger.debug("Course updated: {}", modifyingCourse);
			}
			JSFUtils.storeOnRequestMap("course", modifyingCourse);
			JSFUtils.addFacesInfoMessage("course.save.successful");
			codeInputText.requestFocus();
		} catch (Throwable e) {
			logger.error("Exception when saving course: " + course, e);
			JSFUtils.addApplicationErrorMessage();
			return;
		}
	}
	
	public void resetActionListener(ActionEvent event) {
		course.setCode(modifyingCourse.getCode());
		course.setName(modifyingCourse.getName());

		codeInputText.resetValue();
		nameInputText.resetValue();
		codeInputText.requestFocus();
	}
}
