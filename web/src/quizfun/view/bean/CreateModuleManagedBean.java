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


import quizfun.model.entity.Module;
import quizfun.model.exception.DuplicateModuleException;
import quizfun.view.util.ICEfacesUtils;
import quizfun.view.util.JSFUtils;

/**
 * @author Nevindaree Premarathne
 */
public class CreateModuleManagedBean extends ModuleManagedBean {

	final Logger logger = LoggerFactory.getLogger(CreateModuleManagedBean.class);

	@javax.annotation.PostConstruct
	public void init() {
		module = new Module();
		initializeCourseSelectInput();
	}

	public void saveActionListener(ActionEvent event) {
		if (course == null) {
			JSFUtils.addFacesErrorMessage("selectcourse.course.required.message");
			ICEfacesUtils.setFocus(courseSelectInputText);
			return;
		}
		
		try {
			module.setCourse(course);
			serviceLocator.getModuleService().saveModule(module);
			clearValues();
			JSFUtils.addFacesInfoMessage("module.save.successful");
			ICEfacesUtils.setFocus(codeInputText);
		} catch (DuplicateModuleException e) {
			JSFUtils.addFacesErrorMessage("module.save.duplicate", new Object[] { module.getCode() });
			return;
		} catch (Throwable e) {
			logger.error("Exception when saving module: " + module, e);
			JSFUtils.addApplicationErrorMessage();
			return;
		}
	}
}
