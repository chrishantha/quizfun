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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;

import quizfun.model.entity.User;
import quizfun.model.exception.UserNotFoundException;
import quizfun.view.servicelocator.ServiceLocator;
import quizfun.view.util.JSFUtils;

/**
 * @author M. Isuru Tharanga Chrishantha Perera
 */
public class CommonManagedBean {

	final Logger logger = LoggerFactory.getLogger(CommonManagedBean.class);

	private String username;
	
	private User user;

	private ServiceLocator serviceLocator;

	public void setServiceLocator(ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	@javax.annotation.PostConstruct
	public void init() {
		username = null;
		user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			Object obj = authentication.getPrincipal();
			// Can't obtain UserDetails from the OpenIDAuthenticationToken. 
			// It will only return the identity URL.
			// See http://jira.springframework.org/browse/SEC-931
			if (obj instanceof String) {
				username = (String) obj;
			} else if (obj instanceof UserDetails) {
				username = ((UserDetails) obj).getUsername();
			} else if (obj instanceof User) {
				user = (User) obj;
				username = user.getUsername();
			}
		}
		
		if (username != null && "guest".equalsIgnoreCase(username)) {
			username = null;
			if (logger.isDebugEnabled()) {
				logger.debug("Anonymous User... No need to load data");
			}
		}
		
		if (username != null) {
			if (user == null) {
				// User is not loaded. Finding user..
				try {
					user = serviceLocator.getUserService().findUser(username);
				} catch (UserNotFoundException e) {
					// This is OK. OpenID users can create a new user.
					if (logger.isWarnEnabled()) {
						logger.warn("Could not find user: {}", username);
					}
				} catch (Throwable e) {
					logger.error("Exception when finding user: " + username, e);
					JSFUtils.addApplicationErrorMessage();
					return;
				}
			}
		}
	}

	public String getUsername() {
		return username;
	}

	public User getUser() {
		return user;
	}
	
	public String editAction() {
		if (user != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Going to edit: {}", user);
			}
			JSFUtils.storeOnSessionMap("user", user);
			return "settings";
		}
		return null;
	}
}
