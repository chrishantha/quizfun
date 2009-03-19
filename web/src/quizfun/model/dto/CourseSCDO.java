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
 * Search Criteria Data Object for Course
 * </p>
 * 
 * @author M. Isuru Tharanga Chrishantha Perera
 */
public class CourseSCDO implements Serializable {

	private static final long serialVersionUID = 7988152009548490081L;

	private String code;
	private String name;

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

	@Override
	public String toString() {
		final String separator = ", ";

		StringBuilder builder = new StringBuilder();
		builder.append("CourseSCDO {");
		builder.append("code = ").append('\"').append(getCode()).append('\"');
		builder.append(separator).append("name = ").append('\"').append(getName()).append('\"');
		builder.append("}");
		return builder.toString();
	}
}
