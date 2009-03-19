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

package quizfun.model.dto;

import java.io.Serializable;

/**
 * <p>
 * Search Criteria Data Object for User
 * </p>
 * 
 * @author M. Isuru Tharanga Chrishantha Perera
 */
public class UserSCDO implements Serializable {

	private static final long serialVersionUID = -2891595610742662833L;

	private String username;
	private Boolean accountNonExpired;
	private Boolean accountNonLocked;
	private Boolean credentialsNonExpired;
	private Boolean enabled;
	
	private CourseSCDO courseSCDO;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(Boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public Boolean getAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(Boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public Boolean getCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public CourseSCDO getCourseSCDO() {
		return courseSCDO;
	}

	public void setCourseSCDO(CourseSCDO courseSCDO) {
		this.courseSCDO = courseSCDO;
	}

	@Override
	public String toString() {
		final String separator = ", ";

		StringBuilder builder = new StringBuilder();
		builder.append("UserSCDO {");
		builder.append("username = ").append('\"').append(getUsername()).append('\"');
		builder.append(separator).append("accountNonExpired = ").append(getAccountNonExpired());
		builder.append(separator).append("accountNonLocked = ").append(getAccountNonLocked());
		builder.append(separator).append("credentialsNonExpired = ").append(getCredentialsNonExpired());
		builder.append(separator).append("enabled = ").append(getEnabled());
		builder.append(separator).append("courseSCDO = ").append(getCourseSCDO());
		builder.append("}");
		return builder.toString();
	}
}
