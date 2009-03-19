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


/**
 * @author Hiranya Mudunkotuwa
 */
public class Game implements Serializable, Auditable {

	private static final long serialVersionUID = -799324222232866635L;

	private Long id;
	private Module module;
	private Set<Question> questions = new HashSet<Question>();
	private String status;

	private Long version;
	
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
		result = prime * result + ((getModule() == null) ? 0 : getModule().hashCode());
		result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Game))
			return false;
		Game other = (Game) obj;
		if (getModule() == null) {
			if (other.getModule() != null)
				return false;
		} else if (!getModule().equals(other.getModule()))
			return false;
		if (getStatus() == null) {
			if (other.getStatus() != null)
				return false;
		} else if (!getStatus().equals(other.getStatus()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		final String separator = ", ";

		StringBuilder builder = new StringBuilder();
		builder.append("Game {");
		builder.append("id = ").append(getId());
		builder.append(separator).append("module = ").append('\"').append(getModule()).append('\"');
		builder.append(separator).append("status = ").append(getStatus());
		builder.append(separator).append("createdBy = ").append('\"').append(getCreatedBy()).append('\"');
		builder.append(separator).append("createdDate = ").append(getCreatedDate());
		builder.append(separator).append("modifiedBy = ").append('\"').append(getModifiedBy()).append('\"');
		builder.append(separator).append("modifiedDate = ").append(getModifiedDate());
		builder.append("}");
		return builder.toString();
	}

	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}

	public Set<Question> getQuestions() {
		return questions;
	}

}
