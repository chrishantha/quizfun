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

package quizfun.model.util;

import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;

import quizfun.model.entity.User;

/**
 * @author M. Isuru Tharanga Chrishantha Perera
 */
public class SpringSecurityUtil {
	
	public static String getUsername() {
		String username = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			Object obj = authentication.getPrincipal();
			// Can't obtain UserDetails from the OpenIDAuthenticationToken. 
			// It will only return the identity URL.
			// See http://jira.springframework.org/browse/SEC-931
			if (obj instanceof String) {
				username = (String) obj;
			} else if (obj instanceof UserDetails) {
				UserDetails userDetails = (UserDetails) obj;
				username = userDetails.getUsername();
			} else if (obj instanceof User) {
				User user = (User) obj;
				username = user.getUsername();
			}
		}
		return username;
	}
}
