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
 * @author Hiranya Mudunkotuwa
 */
public class GameSCDO implements Serializable {

	private static final long serialVersionUID = 5499599626747192553L;

	private ModuleSCDO moduleSCDO;
	private Long id;
	
	@Override
	public String toString() {
		final String separator = ", ";

		StringBuilder builder = new StringBuilder();
		builder.append("GameSCDO {");
		builder.append("id = ").append('\"').append(getId()).append('\"');
		builder.append(separator).append("moduleSCDO = ").append(getModuleSCDO());
		builder.append("}");
		return builder.toString();
	}

	public ModuleSCDO getModuleSCDO() {
		return moduleSCDO;
	}

	public void setModuleSCDO(ModuleSCDO moduleSCDO) {
		this.moduleSCDO = moduleSCDO;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
