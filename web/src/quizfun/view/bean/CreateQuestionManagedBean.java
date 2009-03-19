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

import java.util.ArrayList;
import java.util.HashSet;

import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quizfun.model.entity.Answer;
import quizfun.model.entity.Module;
import quizfun.model.entity.Question;
import quizfun.model.exception.DuplicateQuestionException;
import quizfun.view.util.JSFUtils;

/**
 * @author Hiranya Mudunkotuwa
 */
public class CreateQuestionManagedBean extends QuestionManagedBean{

	final Logger logger = LoggerFactory.getLogger(CreateQuestionManagedBean.class);
	
	@javax.annotation.PostConstruct
	public void init() {
		question = new Question();
		question.setAnswers(null);
		question.setModule(new Module());
		answerList = new ArrayList<Answer>(0);
//		ans = new HashSet<Answer>();
		initializeModuleSelectInput();		
	}


	public void saveActionListener(ActionEvent event) {
		if (module == null) {
			JSFUtils.addFacesErrorMessage("question.module.required.message");
			moduleSelectInputText.requestFocus();
			return;
		}
		if (question.getAnswers() == null || question.getAnswers().isEmpty()) {
			JSFUtils.addFacesErrorMessage("question.answers.required.message");
			return;
		}
		
		Boolean correctAns = false;
		for (Answer a :answerList) {
			if(a.isCorrect()) {
				correctAns = true;
			}
		}
		if (!correctAns) {
			JSFUtils.addFacesErrorMessage("question.correctans.required.message");
			return;			
		}
		try {
			question.setModule(module);
			serviceLocator.getQuestionService().saveQuestion(question);
			super.clearValues();
			clearValues();
			quesInputTextArea.requestFocus();
			JSFUtils.addFacesInfoMessage("question.save.successful");
		} catch (DuplicateQuestionException e) {
			JSFUtils.addFacesErrorMessage("question.save.duplicate", new Object[] { question.getId() });
			return;
		} catch (Throwable e) {
			logger.error("Exception when saving question: " + question, e);
			JSFUtils.addApplicationErrorMessage();
			return;
		}
	}
	
	/**
	 * Add an Answer
	 */
	public void addAnswers() {
		logger.debug("CreateQuestionManagedBean - addAnswer");
		
		String answer = (String) answrInputTextArea.getValue();
		Answer newAnswer = new Answer();
		newAnswer.setAnswer(answer);
		answerList.add(newAnswer);		
//		ans.add(newAnswer);
		question.setAnswers(new HashSet<Answer>(answerList));
		answrInputTextArea.resetValue();
	}
	
	
	public void correctValueChangeEvent(ValueChangeEvent event) {
		Answer correctAns = (Answer) tblAnswers.getRowData();
		HtmlSelectBooleanCheckbox selectBooleanCheckbox = (HtmlSelectBooleanCheckbox) event.getComponent();
		Boolean correct = false;
		if (selectBooleanCheckbox.getValue() != null) {
			correct = (Boolean) selectBooleanCheckbox.getValue();
		}
		correctAns.setCorrect(correct);
	}	
	
	public void clearActionListener(ActionEvent event) {
		super.clearActionListener(event);
		 clearValues();
		 answrInputTextArea.resetValue();
		 correctSelectBooleanCheckbox.resetValue();
		 tblAnswers.getChildren().clear();
		 tblPanelGroup.getChildren().clear();		 
	}
	
	public void clearValues() {
		question.setAnswers(new HashSet<Answer>());
		setAnswerList(new ArrayList<Answer>());
//		setAns(new HashSet<Answer>());
	}
	
	public void editAnswerAction() {
		this.answer = (Answer) tblAnswers.getRowData();
		answrInputTextArea.setValue(this.answer.getAnswer());
		updateAnswer = true;
	}
	
	public void editAnswer() {
		this.answer.setAnswer((String) answrInputTextArea.getValue());
		answrInputTextArea.resetValue();
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
	//	this.ans.remove(this.answer);
		removeAnswerConfirmVisible = false;

	}
	
	public void closeRemoveAnswerActionListener(ActionEvent event) {
		removeAnswerConfirmVisible = false;
	}	
}
