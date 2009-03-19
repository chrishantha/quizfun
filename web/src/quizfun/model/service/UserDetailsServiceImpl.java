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

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

import quizfun.model.entity.User;
import quizfun.model.exception.UserNotFoundException;

/**
 * @author M. Isuru Tharanga Chrishantha Perera
 */

public class UserDetailsServiceImpl implements UserDetailsService {

	final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		if (logger.isTraceEnabled()) {
			logger.trace("Load user by username: {}", username);
		}
		User user = null;
		try {
			user = userService.findUser(username);
		} catch (UserNotFoundException e) {
			if (logger.isTraceEnabled()) {
				logger.trace("Could not find user: {}", username);
			}
		}

		SortedSet<GrantedAuthority> authorities = new TreeSet<GrantedAuthority>();

		if (user == null) {
			// Do not throw the exception. Create a new user 
			// throw new UsernameNotFoundException("Could not find user: " + username, username);
			user = new User(username, true, true, true, true);
		} else {
			Set<String> grantedAuthorities = user.getGrantedAuthorities();

			if (grantedAuthorities != null && !grantedAuthorities.isEmpty()) {
				for (String authority : grantedAuthorities) {
					authorities.add(new GrantedAuthorityImpl(authority));
				}
			}
		}

		// Add Default Authority
		authorities.add(new GrantedAuthorityImpl("ROLE_USER")); 

		user.setAuthorities(authorities.toArray(new GrantedAuthority[authorities.size()]));

		return user;
	}

}
