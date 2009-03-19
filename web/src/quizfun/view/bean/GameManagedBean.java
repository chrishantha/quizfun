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

import java.util.List;

import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quizfun.model.entity.Game;
import quizfun.model.entity.Question;
import quizfun.view.util.JSFUtils;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.matchers.TextMatcherEditor;

import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.ext.HtmlInputText;
import com.icesoft.faces.component.ext.HtmlInputTextarea;
import com.icesoft.faces.component.ext.HtmlPanelGroup;
import com.icesoft.faces.component.ext.HtmlSelectBooleanCheckbox;
import com.icesoft.faces.component.ext.HtmlSelectOneRadio;

/**
 * @author Hiranya Mudunkotuwa
 */
public abstract class GameManagedBean extends ModuleSelectManagedBean {
	final Logger logger = LoggerFactory.getLogger(QuestionManagedBean.class);

	protected HtmlInputTextarea quesInputTextArea;
	protected HtmlSelectOneMenu typeSelectOneMenu;
	protected HtmlSelectOneMenu levelSelectOneMenu;
	protected HtmlSelectBooleanCheckbox quesSelectBooleanCheckbox;
	protected HtmlSelectOneRadio quesHtmlSelectOneRadio;
	protected HtmlPanelGroup ranPanelGroup;
	protected Game game;
	
	//Search Question Components
	protected List<Question> quesList;
	
	protected List<Question> questions;

	protected List<Question> filterList;

	protected TextFilterator<Question> questionFilterator;

	protected TextMatcherEditor<Question> questionMatcherEditor;

	protected String filterText;

	protected HtmlInputText filterInputText;

	protected HtmlDataTable dataTable;	
	
	protected List<Question> selectedQuestions;
	protected List<Question> easyQues; 
	protected List<Question> mediumQues; 
	protected List<Question> hardQues; 

	public void filterActionListener(ActionEvent event) {
		filterList();
		if (filterList == null || filterList.isEmpty()) {
			JSFUtils.addFacesInfoMessage(filterInputText, "common.filter.empty");
		}
		filterInputText.requestFocus();
	}

	public void filterClearActionListener(ActionEvent event) {
		filterText = null;
		filterList();
		filterInputText.resetValue();
		filterInputText.requestFocus();
	}

	private void filterList() {
		questionMatcherEditor.setMode(TextMatcherEditor.CONTAINS);
		questionMatcherEditor.setFilterText((filterText == null) ? new String[] {} : new String[] { filterText });
	}
	
	public HtmlInputTextarea getQuesInputTextArea() {
		return quesInputTextArea;
	}
	public void setQuesInputTextArea(HtmlInputTextarea quesInputTextArea) {
		this.quesInputTextArea = quesInputTextArea;
	}
	public HtmlSelectOneMenu getTypeSelectOneMenu() {
		return typeSelectOneMenu;
	}
	public void setTypeSelectOneMenu(HtmlSelectOneMenu typeSelectOneMenu) {
		this.typeSelectOneMenu = typeSelectOneMenu;
	}
	public HtmlSelectOneMenu getLevelSelectOneMenu() {
		return levelSelectOneMenu;
	}
	public void setLevelSelectOneMenu(HtmlSelectOneMenu levelSelectOneMenu) {
		this.levelSelectOneMenu = levelSelectOneMenu;
	}

	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	public List<Question> getQuesList() {
		return quesList;
	}
	public void setQuesList(List<Question> quesList) {
		this.quesList = quesList;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public List<Question> getFilterList() {
		return filterList;
	}

	public void setFilterList(List<Question> filterList) {
		this.filterList = filterList;
	}

	public TextFilterator<Question> getQuestionFilterator() {
		return questionFilterator;
	}

	public void setQuestionFilterator(TextFilterator<Question> questionFilterator) {
		this.questionFilterator = questionFilterator;
	}

	public TextMatcherEditor<Question> getQuestionMatcherEditor() {
		return questionMatcherEditor;
	}

	public void setQuestionMatcherEditor(
			TextMatcherEditor<Question> questionMatcherEditor) {
		this.questionMatcherEditor = questionMatcherEditor;
	}

	public String getFilterText() {
		return filterText;
	}

	public void setFilterText(String filterText) {
		this.filterText = filterText;
	}

	public HtmlInputText getFilterInputText() {
		return filterInputText;
	}

	public void setFilterInputText(HtmlInputText filterInputText) {
		this.filterInputText = filterInputText;
	}

	public HtmlDataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public HtmlSelectOneRadio getQuesHtmlSelectOneRadio() {
		return quesHtmlSelectOneRadio;
	}

	public void setQuesHtmlSelectOneRadio(HtmlSelectOneRadio quesHtmlSelectOneRadio) {
		this.quesHtmlSelectOneRadio = quesHtmlSelectOneRadio;
	}

	public List<Question> getSelectedQuestions() {
		return selectedQuestions;
	}

	public void setSelectedQuestions(List<Question> selectedQuestions) {
		this.selectedQuestions = selectedQuestions;
	}

	public HtmlPanelGroup getRanPanelGroup() {
		return ranPanelGroup;
	}

	public void setRanPanelGroup(HtmlPanelGroup ranPanelGroup) {
		this.ranPanelGroup = ranPanelGroup;
	}

	public List<Question> getEasyQues() {
		return easyQues;
	}

	public void setEasyQues(List<Question> easyQues) {
		this.easyQues = easyQues;
	}

	public List<Question> getMediumQues() {
		return mediumQues;
	}

	public void setMediumQues(List<Question> mediumQues) {
		this.mediumQues = mediumQues;
	}

	public List<Question> getHardQues() {
		return hardQues;
	}

	public void setHardQues(List<Question> hardQues) {
		this.hardQues = hardQues;
	}

	public HtmlSelectBooleanCheckbox getQuesSelectBooleanCheckbox() {
		return quesSelectBooleanCheckbox;
	}

	public void setQuesSelectBooleanCheckbox(
			HtmlSelectBooleanCheckbox quesSelectBooleanCheckbox) {
		this.quesSelectBooleanCheckbox = quesSelectBooleanCheckbox;
	}
	
	
}
