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
public class QuestionSCDO implements Serializable {

	private static final long serialVersionUID = -94519190602260416L;
	private Long id;
	private String question;
	private int type;
	private int level;
	private String hint;
	private String reference;
	
	private ModuleSCDO moduleSCDO;
	
	@Override
	public String toString() {
		final String separator = ", ";

		StringBuilder builder = new StringBuilder();
		builder.append("QuestionSCDO {");
		builder.append("id = ").append('\"').append(getId()).append('\"');
		builder.append(separator).append("question = ").append('\"').append(getQuestion()).append('\"');
		builder.append(separator).append("type = ").append(getType());
		builder.append(separator).append("level = ").append(getLevel());
		builder.append(separator).append("moduleSCDO = ").append(getModuleSCDO());
		builder.append("}");
		return builder.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public ModuleSCDO getModuleSCDO() {
		return moduleSCDO;
	}

	public void setModuleSCDO(ModuleSCDO moduleSCDO) {
		this.moduleSCDO = moduleSCDO;
	}	
}
