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
import java.util.Set;

import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quizfun.model.entity.Answer;
import quizfun.model.entity.Question;
import quizfun.model.util.MergeCallback;
import quizfun.model.util.Utils;
import quizfun.view.util.ICEfacesUtils;
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
		answerList = new ArrayList<Answer>();
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
		
		answerInput = null;
		
		Set<Answer> answers = modifyingQuestion.getAnswers();
		answerList.clear();
		for (Answer answer : answers) {
			// Only answer and correct should be displayed.
			Answer ansObj = new Answer();
			ansObj.setAnswer(answer.getAnswer());
			ansObj.setCorrect(answer.isCorrect());
			answerList.add(ansObj);
		}
		
		module = modifyingQuestion.getModule();
		selectedModule = JSFUtils.getStringFromBundle("question.selectedmodule.display.pattern", new Object[] { module.getCode(),
				module.getName() });
	}
	
	private MergeCallback<Answer> answerMergeCallback;
	
	public MergeCallback<Answer> getAnswerMergeCallback() {
		if (answerMergeCallback == null) {
			answerMergeCallback = new MergeCallback<Answer>() {

				@Override
				public void updateEntity(Answer detachedObject, Answer newObject) {
					// No need to set the answer as equals is implemented based on the answer.
					// This is for clarity
					detachedObject.setAnswer(newObject.getAnswer());
					detachedObject.setCorrect(newObject.isCorrect());
				}
				
			};
		}
		return answerMergeCallback;
	}
	
	public void saveActionListener(ActionEvent event) {
		if (module == null) {
			JSFUtils.addFacesErrorMessage("question.module.required.message");
			ICEfacesUtils.setFocus(moduleSelectInputText);
			return;
		}
		
		if (!validateAnswers(answerList)) {
			return;
		}
		
		try {
			if (!modifyingQuestion.getModule().equals(module)) {
				modifyingQuestion.setModule(module);
			}
			modifyingQuestion.setId(question.getId());
			modifyingQuestion.setQuestion(question.getQuestion());
			modifyingQuestion.setHint(question.getHint());
			modifyingQuestion.setLevel(question.getLevel());
			modifyingQuestion.setType(question.getType());
			modifyingQuestion.setReference(question.getReference());
			// Set question for each answer. There might be new answers added.
			for (Answer answer : answerList) {
				answer.setQuestion(modifyingQuestion);
			}
			Utils.updateDetachedList(modifyingQuestion.getAnswers(), answerList,  getAnswerMergeCallback());
			modifyingQuestion = serviceLocator.getQuestionService().updateQuestion(modifyingQuestion);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Question updated: {}", modifyingQuestion);
			}
			JSFUtils.addFacesInfoMessage("question.save.successful");
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
		 answerInputTextArea.resetValue();
		 tblAnswers.getChildren().clear();
		 tblPanelGroup.getChildren().clear();
		 selectedModuleInputText.resetValue();
	}	
	
	public Question getModifyingQuestion() {
		return modifyingQuestion;
	}

	public void setModifyingQuestion(Question modifyingQuestion) {
		this.modifyingQuestion = modifyingQuestion;
	}
}
