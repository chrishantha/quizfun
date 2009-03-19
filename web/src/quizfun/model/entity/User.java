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

package quizfun.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

/**
 * @author M. Isuru Tharanga Chrishantha Perera
 */
public class User implements UserDetails, Serializable, Auditable {

	private static final long serialVersionUID = -1425503060155190198L;

	private String password;
	private String username;
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;

	private Course course;

	private Set<String> grantedAuthorities = new HashSet<String>();

	private GrantedAuthority[] authorities;

	private Long version;
	
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;

	public User() {
	}

	public User(String username, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) throws IllegalArgumentException {
	    if (username == null || username.isEmpty()) {
	        throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
	    }
		this.username = username;
		this.accountNonExpired = accountNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
	}



	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	@Override
	public GrantedAuthority[] getAuthorities() {
		return authorities;
	}

	public void setAuthorities(GrantedAuthority[] authorities) {
		this.authorities = authorities;
	}

	public Set<String> getGrantedAuthorities() {
		return grantedAuthorities;
	}

	public void setGrantedAuthorities(Set<String> grantedAuthorities) {
		this.grantedAuthorities = grantedAuthorities;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		if (getUsername() == null) {
			if (other.getUsername() != null)
				return false;
		} else if (!getUsername().equals(other.getUsername()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		final String separator = ", ";

		StringBuilder builder = new StringBuilder();
		builder.append("User {");
		builder.append("username = ").append('\"').append(getUsername()).append('\"');
		builder.append(separator).append("password = \"******\"");
		builder.append(separator).append("accountNonExpired = ").append(isAccountNonExpired());
		builder.append(separator).append("accountNonLocked = ").append(isAccountNonLocked());
		builder.append(separator).append("credentialsNonExpired = ").append(isCredentialsNonExpired());
		builder.append(separator).append("enabled = ").append(isEnabled());
		builder.append(separator);
		if (getGrantedAuthorities() != null) {
			builder.append("[Granted Authorities: ");
			int i = 0;
			for (String authority : getGrantedAuthorities()) {
				if (i > 0) {
					builder.append(separator);
				}
				i++;
				builder.append(authority);
			}
			builder.append(']');
		} else {
			builder.append("Not granted any authorities");
		}
		builder.append(separator).append("version = ").append(getVersion());
		builder.append(separator).append("createdBy = ").append('\"').append(getCreatedBy()).append('\"');
		builder.append(separator).append("createdDate = ").append(getCreatedDate());
		builder.append(separator).append("modifiedBy = ").append('\"').append(getModifiedBy()).append('\"');
		builder.append(separator).append("modifiedDate = ").append(getModifiedDate());
		builder.append(separator).append("course = ").append(getCourse());
		builder.append("}");
		return builder.toString();
	}
}
