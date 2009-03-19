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
import quizfun.model.entity.Question;
import quizfun.model.exception.QuestionNotFoundException;
import quizfun.view.util.JSFUtils;

/**
 * @author Hiranya Mudunkotuwa
 */
public class ModifyQuestionManagedBean extends QuestionManagedBean {
	
	final Logger logger = LoggerFactory.getLogger(ModifyQuestionManagedBean.class);

	private Question modifyingQuestion;
	
	@javax.annotation.PostConstruct
	public void init() {
		modifyingQuestion = (Question) JSFUtils.removeFromSessionMap("question");
		if (logger.isDebugEnabled()) {
			logger.debug("Object retrieved from session: {}", modifyingQuestion);
		}
		answerList = new ArrayList<Answer>(0);
		question = new Question();
		resetValues();
		initializeModuleSelectInput();
	}
	
	private void resetValues() {

		question.setId(modifyingQuestion.getId());
		question.setQuestion(modifyingQuestion.getQuestion());
		question.setHint(modifyingQuestion.getHint());
		question.setLevel(modifyingQuestion.getLevel());
		question.setType(modifyingQuestion.getType());
		question.setReference(modifyingQuestion.getReference());
		Question ques = new Question();
		try {
			ques = serviceLocator.getQuestionService().findQuestionById(modifyingQuestion.getId());
		} catch (QuestionNotFoundException e) {
			return;
		}
	//	ans = ques.getAnswers();
		question.setAnswers(ques.getAnswers());
		answerList = new ArrayList<Answer>(ques.getAnswers());
		
		module = modifyingQuestion.getModule();
		selectedModule = JSFUtils.getStringFromBundle("question.selectedmodule.display.pattern", new Object[] { module.getCode(),
				module.getName() });
	}
	
	public void saveActionListener(ActionEvent event) {
		if (module == null) {
			JSFUtils.addFacesErrorMessage("question.module.required.message");
			moduleSelectInputText.requestFocus();
			return;
		}
		
		try {
			if (!modifyingQuestion.getModule().equals(module)) {
				modifyingQuestion.setModule(module);
			}
//			ans = new HashSet<Answer>(answerList);
			question.setAnswers(new HashSet<Answer>(answerList));
			modifyingQuestion.setId(question.getId());
			modifyingQuestion.setQuestion(question.getQuestion());
			modifyingQuestion.setHint(question.getHint());
			modifyingQuestion.setLevel(question.getLevel());
			modifyingQuestion.setType(question.getType());
			modifyingQuestion.setReference(question.getReference());
			modifyingQuestion.setAnswers(question.getAnswers());
			modifyingQuestion = serviceLocator.getQuestionService().updateQuestion(modifyingQuestion);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Question updated: {}", modifyingQuestion);
			}
			JSFUtils.addFacesInfoMessage("question.save.successful");
			answrInputTextArea.resetValue();
		} catch (Throwable e) {
			logger.error("Exception when saving question: " + question, e);
			JSFUtils.addApplicationErrorMessage();
			return;
		}
	}	
	
	public void resetActionListener(ActionEvent event) {
		 resetValues();

		 quesInputTextArea.resetValue();
		 hintInputTextArea.resetValue();
		 refInputTextArea.resetValue();	
		 typeSelectOneMenu.resetValue();
		 levelSelectOneMenu.resetValue();
		 answrInputTextArea.resetValue();
		 correctSelectBooleanCheckbox.resetValue();
		 tblAnswers.getChildren().clear();
		 tblPanelGroup.getChildren().clear();	
		 selectedModuleInputText.resetValue();
	}
	
	/**
	 * Add an Answer
	 */
	public void addAnswers() {
		logger.debug("CreateQuestionManagedBean - addAnswer");
		
		String answer = (String) answrInputTextArea.getValue();
		Answer newAnswer = new Answer();
		newAnswer.setAnswer(answer);
		newAnswer.setQuestion(question);
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
	
	
	public void editAnswerAction() {
		this.answer = (Answer) tblAnswers.getRowData();
		answrInputTextArea.setValue(this.answer.getAnswer());
		updateAnswer = true;
	}
	
	public void editAnswer() {
		this.answer.setAnswer((String) answrInputTextArea.getValue());
		for(Answer answr :this.answerList) {
			if(answr.getAnswer().equals(this.answer.getAnswer())) {
				answr.setAnswer((String) answrInputTextArea.getValue());
				answrInputTextArea.resetValue();
				break;
			}
			
		}
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
//		this.ans.remove(this.answer);
		removeAnswerConfirmVisible = false;

	}
	
	public void closeRemoveAnswerActionListener(ActionEvent event) {
		removeAnswerConfirmVisible = false;
	}		
	
	public Question getModifyingQuestion() {
		return modifyingQuestion;
	}

	public void setModifyingQuestion(Question modifyingQuestion) {
		this.modifyingQuestion = modifyingQuestion;
	}
}
