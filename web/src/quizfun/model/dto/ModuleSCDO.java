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
 * Search Criteria Data Object for Module
 * </p>
 * 
 * @author Nevindaree Premarathne
 */
public class ModuleSCDO implements Serializable {

	private static final long serialVersionUID = -8455060934805960528L;

	private String code;
	private String name;
	
	private CourseSCDO courseSCDO;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		builder.append("ModuleSCDO {");
		builder.append("code = ").append('\"').append(getCode()).append('\"');
		builder.append(separator).append("name = ").append('\"').append(getName()).append('\"');
		builder.append(separator).append("courseSCDO = ").append(getCourseSCDO());
		builder.append("}");
		return builder.toString();
	}
}
