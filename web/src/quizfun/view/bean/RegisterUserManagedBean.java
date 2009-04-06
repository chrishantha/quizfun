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

import quizfun.model.entity.User;
import quizfun.model.exception.DuplicateUserException;
import quizfun.model.util.SpringSecurityUtil;
import quizfun.view.util.ICEfacesUtils;
import quizfun.view.util.JSFUtils;

/**
 * @author M. Isuru Tharanga Chrishantha Perera
 */
public class RegisterUserManagedBean extends UserManagedBean {

	final Logger logger = LoggerFactory.getLogger(RegisterUserManagedBean.class);
	
	private boolean saved;

	public boolean isSaved() {
		return saved;
	}

	@javax.annotation.PostConstruct
	public void init() {
		user = new User();
		user.setUsername(SpringSecurityUtil.getUsername());
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setEnabled(true);
		initializeCourseSelectInput();
	}

	public void saveActionListener(ActionEvent event) {
		if (!validatePassword()) {
			return;
		}
		
		if (!validateCourse()) {
			return;
		}

		try {
			user.setCourse(course);
			user.getGrantedAuthorities().add("ROLE_USER");
			user.setPassword(encodePassword(user.getPassword(),	user.getUsername()));
			serviceLocator.getUserService().saveUser(user);
			clearValues();
			saved = true;
		} catch (DuplicateUserException e) {
			JSFUtils.addFacesErrorMessage("user.save.duplicate", new Object[] { user.getUsername() });
			return;
		} catch (Throwable e) {
			logger.error("Exception when saving user: " + user, e);
			JSFUtils.addApplicationErrorMessage();
			return;
		}
	}
	
	protected void clearValues() {
		confirmPassword = null;
		user.setPassword(null);
	}
	
	protected void resetComponents() {
		passwordInputSecret.resetValue();
		confirmPasswordInputSecret.resetValue();
	}

	public void clearActionListener(ActionEvent event) {
		clearValues();
		resetComponents();
		ICEfacesUtils.setFocus(passwordInputSecret);
	}
}
