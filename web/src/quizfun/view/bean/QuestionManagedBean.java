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

import java.util.Collection;
import java.util.List;

import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quizfun.model.entity.Answer;
import quizfun.model.entity.Question;
import quizfun.view.util.ICEfacesUtils;
import quizfun.view.util.JSFUtils;

import com.icesoft.faces.component.ext.HtmlInputTextarea;
import com.icesoft.faces.component.ext.HtmlPanelGroup;

/**
 * @author Hiranya Mudunkotuwa
 */
public abstract class QuestionManagedBean extends ModuleSelectManagedBean{
	final Logger logger = LoggerFactory.getLogger(QuestionManagedBean.class);
	
	private static final int MAX_ANSWERS = 5;

	protected Question question;

	protected HtmlDataTable tblAnswers;

	protected HtmlPanelGroup tblPanelGroup;
	protected HtmlInputTextarea quesInputTextArea;
	protected HtmlInputTextarea hintInputTextArea;
	protected HtmlInputTextarea refInputTextArea;
	protected HtmlInputTextarea answerInputTextArea;


	protected HtmlSelectOneMenu typeSelectOneMenu;
	protected HtmlSelectOneMenu levelSelectOneMenu;

	protected boolean loadModuleDetails = false;	
	protected boolean updateAnswer = false;
	
	//Value bound to answerInputTextArea
	protected String answerInput;

	protected List<Answer> answerList;
	protected Answer answer;

	protected boolean removeAnswerConfirmVisible;
	
	protected boolean validateAnswers(Collection<Answer> answers) {
		if (answers == null || answers.isEmpty()) {
			JSFUtils.addFacesErrorMessage("question.answers.required.message");
			return false;
		}
		if (answerList.size() > MAX_ANSWERS) {
			JSFUtils.addFacesErrorMessage("question.answers.limit.message", new Object[] {MAX_ANSWERS});
			return false;
		}
		boolean correctAns = false;
		for (Answer a : answers) {
			if (correctAns && a.isCorrect()) {
				// TODO: For now only allow one answer to be correct.
				JSFUtils.addFacesErrorMessage("question.correctans.selectone.message");
				return false;
			}
			if(a.isCorrect()) {
				correctAns = true;
			}
		}
		if (!correctAns) {
			JSFUtils.addFacesErrorMessage("question.correctans.required.message");
			return false;			
		}
		return true;
	}

	public void clearActionListener(ActionEvent event) {
		clearValues();

		quesInputTextArea.resetValue();
		hintInputTextArea.resetValue();
		refInputTextArea.resetValue();
		typeSelectOneMenu.resetValue();
		levelSelectOneMenu.resetValue();
		ICEfacesUtils.setFocus(quesInputTextArea);
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	protected void clearValues() {
		question = new Question();
		question.setQuestion(null);
		question.setType(Question.TYPE_MCQ);
		question.setLevel(Question.LEVEL_EASY);
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

	public HtmlInputTextarea getAnswerInputTextArea() {
		return answerInputTextArea;
	}

	public void setAnswerInputTextArea(HtmlInputTextarea answerInputTextArea) {
		this.answerInputTextArea = answerInputTextArea;
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

	public List<Answer> getAnswerList() {
		return answerList;
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
	
	private boolean validateAnswerInput() {
		if (logger.isDebugEnabled()) {
			logger.debug("QuestionManagedBean - validateAnswerInput: {}", answerInput);
		}
		if (answerInput == null || answerInput.trim().length() == 0) {
			JSFUtils.addFacesErrorMessage(answerInputTextArea, UIInput.REQUIRED_MESSAGE_ID);
			ICEfacesUtils.setFocus(answerInputTextArea);
			return false;
		}
		for (Answer answer : answerList) {
			if (answerInput.equals(answer.getAnswer())) {
				JSFUtils.addFacesErrorMessage(answerInputTextArea, "question.answers.duplicate.message");
				ICEfacesUtils.setFocus(answerInputTextArea);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Add an Answer
	 */
	public void addAnswer(ActionEvent event) {
		if (!validateAnswerInput()) {
			return;
		}
		Answer newAnswer = new Answer();
		newAnswer.setAnswer(answerInput);
		answerList.add(newAnswer);
		answerInput = null;
		//ICEfacesUtils.setFocus(answerInputTextArea);
		answerInputTextArea.resetValue();
	}
	
	public void editAnswerAction() {
		answer = (Answer) tblAnswers.getRowData();
		answerInput = answer.getAnswer();
		answerInputTextArea.resetValue();
		updateAnswer = true;
	}
	
	public void editAnswer(ActionEvent event) {
		if (!validateAnswerInput()) {
			return;
		}
		answer.setAnswer(answerInput);
		answerInput = null;
		answerInputTextArea.resetValue();
		ICEfacesUtils.setFocus(answerInputTextArea);
		updateAnswer = false;
	}
	
	public void cancelAnswerInput(ActionEvent event) {
		answerInput = null;
		answerInputTextArea.resetValue();
		ICEfacesUtils.setFocus(answerInputTextArea);
		updateAnswer = false;
	}

	public void removeAnswerConfirmActionListener(ActionEvent event) {
		this.answer = (Answer) tblAnswers.getRowData();
		removeAnswerConfirmVisible = this.answer != null;
		if (removeAnswerConfirmVisible) {
			if (logger.isDebugEnabled()) {
				logger.debug("Going to remove: {}", this.answer);
			}
		}
	}
	public void removeAnswerActionListener(ActionEvent event) {
		if (this.answer == null) {
			return;
		}
		this.answerList.remove(this.answer);
		removeAnswerConfirmVisible = false;
	}
	
	public void closeRemoveAnswerActionListener(ActionEvent event) {
		removeAnswerConfirmVisible = false;
	}

	public String getAnswerInput() {
		return answerInput;
	}

	public void setAnswerInput(String answerInput) {
		this.answerInput = answerInput;
	}
}
