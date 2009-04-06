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

import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quizfun.model.entity.Module;
import quizfun.view.util.ICEfacesUtils;

import com.icesoft.faces.component.ext.HtmlInputText;

/**
 * @author Nevindaree Premarathne
 */
public abstract class ModuleManagedBean extends CourseSelectManagedBean {

	final Logger logger = LoggerFactory.getLogger(ModuleManagedBean.class);

	protected Module module;

	protected HtmlInputText codeInputText;
	protected HtmlInputText nameInputText;

	public HtmlInputText getCodeInputText() {
		return codeInputText;
	}

	public void setCodeInputText(HtmlInputText codeInputText) {
		this.codeInputText = codeInputText;
	}

	public HtmlInputText getNameInputText() {
		return nameInputText;
	}

	public void setNameInputText(HtmlInputText nameInputText) {
		this.nameInputText = nameInputText;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	protected void clearValues() {
		module.setCode(null);
		module.setName(null);
	}

	public void clearActionListener(ActionEvent event) {
		clearValues();

		codeInputText.resetValue();
		nameInputText.resetValue();
		ICEfacesUtils.setFocus(codeInputText);
	}
}
