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
import quizfun.view.util.ICEfacesUtils;
import quizfun.view.util.JSFUtils;

/**
 * @author Nevindaree Premarathne
 */
public class ModifyModuleManagedBean extends ModuleManagedBean {

	final Logger logger = LoggerFactory.getLogger(ModifyModuleManagedBean.class);
	
	private Module modifyingModule;

	@javax.annotation.PostConstruct
	public void init() {
		modifyingModule = (Module) JSFUtils.removeFromSessionMap("module");
		if (logger.isDebugEnabled()) {
			logger.debug("Object retrieved from session: {}", modifyingModule);
		}
		module = new Module();
		resetValues();
		initializeCourseSelectInput();
	}
	
	private void resetValues() {
		module.setCode(modifyingModule.getCode());
		module.setName(modifyingModule.getName());
		course = modifyingModule.getCourse();
		selectedCourse = JSFUtils.getStringFromBundle("selectcourse.selectedcourse.display.pattern", new Object[] { course.getCode(),
				course.getName() });
	}

	public void saveActionListener(ActionEvent event) {
		if (course == null) {
			JSFUtils.addFacesErrorMessage("selectcourse.course.required.message");
			ICEfacesUtils.setFocus(courseSelectInputText);
			return;
		}
		
		try {
			if (!modifyingModule.getCourse().equals(course)) {
				modifyingModule.setCourse(course);
			}
			modifyingModule.setCode(module.getCode());
			modifyingModule.setName(module.getName());
			modifyingModule = serviceLocator.getModuleService().updateModule(modifyingModule);
			if (logger.isDebugEnabled()) {
				logger.debug("Module updated: {}", modifyingModule);
			}
			JSFUtils.storeOnRequestMap("module", modifyingModule);
			JSFUtils.addFacesInfoMessage("module.save.successful");
			ICEfacesUtils.setFocus(codeInputText);
		} catch (Throwable e) {
			logger.error("Exception when saving module: " + module, e);
			JSFUtils.addApplicationErrorMessage();
			return;
		}
	}
	
	public void resetActionListener(ActionEvent event) {
		resetValues();

		codeInputText.resetValue();
		nameInputText.resetValue();
		ICEfacesUtils.setFocus(codeInputText);
		
		selectedCourseInputText.resetValue();
	}
}
