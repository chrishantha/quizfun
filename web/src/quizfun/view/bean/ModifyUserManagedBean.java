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

import java.util.Set;

import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quizfun.model.entity.User;
import quizfun.view.util.ICEfacesUtils;
import quizfun.view.util.JSFUtils;

/**
 * @author M. Isuru Tharanga Chrishantha Perera
 */
public class ModifyUserManagedBean extends UserManagedBean {

	final Logger logger = LoggerFactory.getLogger(ModifyUserManagedBean.class);
	
	private User modifyingUser;

	@javax.annotation.PostConstruct
	public void init() {
		modifyingUser = (User) JSFUtils.removeFromSessionMap("user");
		if (logger.isDebugEnabled()) {
			logger.debug("Object retrieved from session: {}", modifyingUser);
		}
		user = new User();
		resetValues();
		initializeCourseSelectInput();
		initializeRoleSelection();
	}
	
	private void resetValues() {
		user.setUsername(modifyingUser.getUsername());
		user.setAccountNonExpired(modifyingUser.isAccountNonExpired());
		user.setAccountNonLocked(modifyingUser.isAccountNonLocked());
		user.setCredentialsNonExpired(modifyingUser.isCredentialsNonExpired());
		user.setEnabled(modifyingUser.isEnabled());
		user.setPassword(null); // No need to set the password.
		confirmPassword = null;
		
		Set<String> authorities = modifyingUser.getGrantedAuthorities();
		if (authorities != null) {
			selectedRoles = authorities.toArray(new String[authorities.size()]);
		} else {
			selectedRoles = null;
		}
		
		
		course = modifyingUser.getCourse();
		if (course != null) {
			selectedCourse = JSFUtils.getStringFromBundle("selectcourse.selectedcourse.display.pattern", new Object[] { course.getCode(),
					course.getName() });
		}
	}

	public void saveActionListener(ActionEvent event) {
		String password = user.getPassword();
		if (password != null && !password.isEmpty()) {
			if (!validatePassword()) {
				return;
			}
		}
		
		if (!validateCourse()) {
			return;
		}
		
		try {
			if (course != null && !course.equals(modifyingUser.getCourse())) {
				modifyingUser.setCourse(course);
			}
			if (password != null && !password.isEmpty()) {
				modifyingUser.setPassword(encodePassword(password,	modifyingUser.getUsername()));
			}
			setGrantedAuthorities(modifyingUser);
			modifyingUser.setUsername(user.getUsername());
			modifyingUser.setAccountNonExpired(user.isAccountNonExpired());
			modifyingUser.setAccountNonLocked(user.isAccountNonLocked());
			modifyingUser.setCredentialsNonExpired(user.isCredentialsNonExpired());
			modifyingUser.setEnabled(user.isEnabled());
			
			modifyingUser = serviceLocator.getUserService().updateUser(modifyingUser);
			if (logger.isDebugEnabled()) {
				logger.debug("User updated: {}", modifyingUser);
			}
			JSFUtils.storeOnRequestMap("user", modifyingUser);
			JSFUtils.addFacesInfoMessage("user.save.successful");
			ICEfacesUtils.setFocus(userNameInputText);
		} catch (Throwable e) {
			logger.error("Exception when saving user: " + user, e);
			JSFUtils.addApplicationErrorMessage();
			return;
		}
	}
	
	public void resetActionListener(ActionEvent event) {
		resetValues();

		resetComponents();
		
		selectedCourseInputText.resetValue();
	}
}
