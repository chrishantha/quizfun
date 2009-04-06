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

import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quizfun.model.entity.Answer;
import quizfun.model.entity.Question;
import quizfun.model.exception.DuplicateQuestionException;
import quizfun.view.util.ICEfacesUtils;
import quizfun.view.util.JSFUtils;

/**
 * @author Hiranya Mudunkotuwa
 */
public class CreateQuestionManagedBean extends QuestionManagedBean{

	final Logger logger = LoggerFactory.getLogger(CreateQuestionManagedBean.class);
	
	@javax.annotation.PostConstruct
	public void init() {
		question = new Question();
		answerList = new ArrayList<Answer>();
		initializeModuleSelectInput();		
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
			question.setModule(module);
			for (Answer answer : answerList) {
				answer.setQuestion(question);
			}
			for (Answer answer : answerList) {
				question.getAnswers().add(answer);
			}
			serviceLocator.getQuestionService().saveQuestion(question);
			super.clearValues();
			clearValues();
			// Don't focus. There is an issue with ICEfaces.
			ICEfacesUtils.setFocus(quesInputTextArea);
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
	
	public void clearActionListener(ActionEvent event) {
		super.clearActionListener(event);
		clearValues();
		answerInputTextArea.resetValue();
		tblAnswers.getChildren().clear();
		tblPanelGroup.getChildren().clear();
	}
	
	protected void clearValues() {
		answerList.clear();
		answerInput = null;
	}
}
