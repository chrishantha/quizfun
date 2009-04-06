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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.openid4java.discovery.Discovery;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.providers.encoding.ShaPasswordEncoder;

import quizfun.model.entity.User;
import quizfun.view.util.ICEfacesUtils;
import quizfun.view.util.JSFUtils;

import com.icesoft.faces.component.ext.HtmlInputSecret;
import com.icesoft.faces.component.ext.HtmlInputText;
import com.icesoft.faces.component.ext.HtmlSelectBooleanCheckbox;
import com.icesoft.faces.component.ext.HtmlSelectManyMenu;

/**
 * @author M. Isuru Tharanga Chrishantha Perera
 */
public abstract class UserManagedBean extends CourseSelectManagedBean {

	final Logger logger = LoggerFactory.getLogger(UserManagedBean.class);

	protected User user;

	protected String confirmPassword;
	protected String[] selectedRoles;
	protected List<SelectItem> roleList;

	protected HtmlInputText userNameInputText;
	protected HtmlInputSecret passwordInputSecret;
	protected HtmlInputSecret confirmPasswordInputSecret;
	protected HtmlSelectBooleanCheckbox enabledSelectBooleanCheckbox;
	protected HtmlSelectManyMenu roleSelectManyMenu;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String[] getSelectedRoles() {
		return selectedRoles;
	}

	public void setSelectedRoles(String[] selectedRoles) {
		this.selectedRoles = selectedRoles;
	}

	public List<SelectItem> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<SelectItem> roleList) {
		this.roleList = roleList;
	}

	public HtmlInputText getUserNameInputText() {
		return userNameInputText;
	}

	public void setUserNameInputText(HtmlInputText userNameInputText) {
		this.userNameInputText = userNameInputText;
	}

	public HtmlInputSecret getPasswordInputSecret() {
		return passwordInputSecret;
	}

	public void setPasswordInputSecret(HtmlInputSecret passwordInputSecret) {
		this.passwordInputSecret = passwordInputSecret;
	}

	public HtmlInputSecret getConfirmPasswordInputSecret() {
		return confirmPasswordInputSecret;
	}

	public void setConfirmPasswordInputSecret(HtmlInputSecret confirmPasswordInputSecret) {
		this.confirmPasswordInputSecret = confirmPasswordInputSecret;
	}

	public HtmlSelectBooleanCheckbox getEnabledSelectBooleanCheckbox() {
		return enabledSelectBooleanCheckbox;
	}

	public void setEnabledSelectBooleanCheckbox(HtmlSelectBooleanCheckbox enabledSelectBooleanCheckbox) {
		this.enabledSelectBooleanCheckbox = enabledSelectBooleanCheckbox;
	}

	public HtmlSelectManyMenu getRoleSelectManyMenu() {
		return roleSelectManyMenu;
	}

	public void setRoleSelectManyMenu(HtmlSelectManyMenu roleSelectManyMenu) {
		this.roleSelectManyMenu = roleSelectManyMenu;
	}
	
	public void openIDChangeListener(ValueChangeEvent event) {
		String username = (String) event.getNewValue();

		if (username == null) {
			return;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Validating OpenID: {}", username);
		}

		try {
			Identifier identifier = Discovery.parseIdentifier(username);
			user.setUsername(identifier.getIdentifier());
			userNameInputText.resetValue();
		} catch (DiscoveryException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Error parsing identifier", e);
			}
			JSFUtils.addFacesErrorMessage(userNameInputText, "openid.parse.error");
		}
	}
	
	protected boolean validatePassword() {
		if (!user.getPassword().equals(confirmPassword)) {
			JSFUtils.addFacesErrorMessage(confirmPasswordInputSecret, "password.mismatch.error");
			ICEfacesUtils.setFocus(confirmPasswordInputSecret);
			return false;
		}
		return true;
	}
	
	protected boolean validateCourse() {
		boolean validateCourse = true;
		if (selectedRoles != null && selectedRoles.length == 1) {
			// Course is not required for administrators
			validateCourse = !selectedRoles[0].equals("ROLE_ADMIN");
		}
		if (validateCourse && (course == null)) {
			JSFUtils.addFacesErrorMessage("selectcourse.course.required.message");
			ICEfacesUtils.setFocus(courseSelectInputText);
			return false;
		}
		return true;
	}
	
	protected void setGrantedAuthorities(User user) {
		if (logger.isTraceEnabled()) {
			logger.trace("Setting Granted Authorities. ");
		}

		if (selectedRoles != null) {
			if (logger.isTraceEnabled()) {
				logger.trace("Before setting authorities: {}", user);
			}
			
			List<String> selectedAuthorities = new ArrayList<String>();
		    // build the new selected list
			for (int i = 0; i < selectedRoles.length; i++) {
				String role = selectedRoles[i];
				selectedAuthorities.add(role);
			}
			
			Set<String> existingAuthorities = user.getGrantedAuthorities();
			List<String> authorities = new ArrayList<String>(selectedAuthorities); // To use iterator.remove()
			
			Iterator<String> iterator = authorities.iterator();
			while (iterator.hasNext()) {
				String authority = iterator.next();
				if (existingAuthorities.contains(authority)) {
					iterator.remove();
				} else {
					existingAuthorities.add(authority);
				}
			}
			
			List<String> list = new ArrayList<String>(existingAuthorities);
			list.retainAll(selectedAuthorities);
			
			List<String> removeList = new ArrayList<String>(existingAuthorities);
			removeList.removeAll(list);
			
			for (String authority : removeList) {
				existingAuthorities.remove(authority); // Remove from the set
			}
			
			if (logger.isTraceEnabled()) {
				logger.trace("After setting authorities: {}", user);
			}
		}
	}

	protected void initializeRoleSelection() {
		roleList = new ArrayList<SelectItem>(3);
		roleList.add(new SelectItem("ROLE_USER"));
		roleList.add(new SelectItem("ROLE_LECTURER"));
		roleList.add(new SelectItem("ROLE_ADMIN"));
	}

	protected void clearValues() {
		confirmPassword = null;
		user.setUsername(null);
		user.setPassword(null);
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setEnabled(true);
		selectedRoles = null;
	}
	
	protected void resetComponents() {
		userNameInputText.resetValue();
		passwordInputSecret.resetValue();
		confirmPasswordInputSecret.resetValue();
		enabledSelectBooleanCheckbox.resetValue();
		roleSelectManyMenu.resetValue();
	}

	public void clearActionListener(ActionEvent event) {
		clearValues();
		resetComponents();
		ICEfacesUtils.setFocus(userNameInputText);
	}
	
	protected String encodePassword(String password, String username) {
		ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder();
		return passwordEncoder.encodePassword(password, username);
	}
}
