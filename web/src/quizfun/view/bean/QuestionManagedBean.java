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

import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quizfun.model.entity.Answer;
import quizfun.model.entity.Question;

import com.icesoft.faces.component.ext.HtmlInputTextarea;
import com.icesoft.faces.component.ext.HtmlPanelGroup;
import com.icesoft.faces.component.ext.HtmlSelectBooleanCheckbox;

/**
 * @author Hiranya Mudunkotuwa
 */
public abstract class QuestionManagedBean extends ModuleSelectManagedBean{
	final Logger logger = LoggerFactory.getLogger(QuestionManagedBean.class);

	//protected ServiceLocator serviceLocator;
	protected Question question;

	protected HtmlDataTable tblAnswers;

	protected HtmlPanelGroup tblPanelGroup;
	protected HtmlInputTextarea quesInputTextArea;
	protected HtmlInputTextarea hintInputTextArea;
	protected HtmlInputTextarea refInputTextArea;
	protected HtmlInputTextarea answrInputTextArea;


	protected HtmlSelectOneMenu typeSelectOneMenu;
	protected HtmlSelectOneMenu levelSelectOneMenu;
	protected HtmlSelectBooleanCheckbox correctSelectBooleanCheckbox;


	protected boolean loadModuleDetails = false;	
	protected boolean updateAnswer = false;



	protected List<Answer> answerList;
//	protected Set<Answer> ans;
	protected Answer answer;

	protected boolean removeAnswerConfirmVisible;

	public void clearActionListener(ActionEvent event) {
		clearValues();
		
		 quesInputTextArea.resetValue();
		 hintInputTextArea.resetValue();
		 refInputTextArea.resetValue();	
		 typeSelectOneMenu.resetValue();
		 levelSelectOneMenu.resetValue();
		 quesInputTextArea.requestFocus();
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	protected void clearValues() {
		question.setQuestion(null);
		question.setType(new Integer(0));
		question.setLevel(new Integer(0));
		question.setHint(null);
		question.setReference(null);

	}	
	
	public HtmlDataTable getTblAnswers() {
		return tblAnswers;
	}

	public void setTblAnswers(HtmlDataTable tblAnswers) {
		this.tblAnswers = tblAnswers;
	}

	public HtmlPanelGroup getTblPanelGroup() {
		return tblPanelGroup;
	}

	public void setTblPanelGroup(HtmlPanelGroup tblPanelGroup) {
		this.tblPanelGroup = tblPanelGroup;
	}

	public HtmlInputTextarea getQuesInputTextArea() {
		return quesInputTextArea;
	}

	public void setQuesInputTextArea(HtmlInputTextarea quesInputTextArea) {
		this.quesInputTextArea = quesInputTextArea;
	}

	public HtmlInputTextarea getHintInputTextArea() {
		return hintInputTextArea;
	}

	public void setHintInputTextArea(HtmlInputTextarea hintInputTextArea) {
		this.hintInputTextArea = hintInputTextArea;
	}

	public HtmlInputTextarea getRefInputTextArea() {
		return refInputTextArea;
	}

	public void setRefInputTextArea(HtmlInputTextarea refInputTextArea) {
		this.refInputTextArea = refInputTextArea;
	}

	public HtmlInputTextarea getAnswrInputTextArea() {
		return answrInputTextArea;
	}

	public void setAnswrInputTextArea(HtmlInputTextarea answrInputTextArea) {
		this.answrInputTextArea = answrInputTextArea;
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

	public HtmlSelectBooleanCheckbox getCorrectSelectBooleanCheckbox() {
		return correctSelectBooleanCheckbox;
	}

	public void setCorrectSelectBooleanCheckbox(
			HtmlSelectBooleanCheckbox correctSelectBooleanCheckbox) {
		this.correctSelectBooleanCheckbox = correctSelectBooleanCheckbox;
	}

	public List<Answer> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<Answer> answerList) {
		this.answerList = answerList;
	}	
	
	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}
	
	public boolean isUpdateAnswer() {
		return updateAnswer;
	}

	public void setUpdateAnswer(boolean updateAnswer) {
		this.updateAnswer = updateAnswer;
	}
	
	public boolean isRemoveAnswerConfirmVisible() {
		return removeAnswerConfirmVisible;
	}

	public void setRemoveAnswerConfirmVisible(boolean removeAnswerConfirmVisible) {
		this.removeAnswerConfirmVisible = removeAnswerConfirmVisible;
	}
}
