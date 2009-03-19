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

package quizfun.model.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import quizfun.model.dao.QuestionDao;
import quizfun.model.dto.QuestionSCDO;
import quizfun.model.entity.Answer;
import quizfun.model.entity.Question;
import quizfun.model.exception.DuplicateQuestionException;
import quizfun.model.exception.QuestionNotFoundException;

/**
 * @author Hiranya Mudunkotuwa
 */
@Transactional(readOnly = true)
public class QuestionServiceImpl implements QuestionService {
	final Logger logger = LoggerFactory.getLogger(QuestionService.class);

	private QuestionDao questionDao;
	private ModuleService moduleService;

	public void setQuestionDao(QuestionDao questionDao) {
		this.questionDao = questionDao;
	}

	public ModuleService getModuleService() {
		return moduleService;
	}

	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = { DuplicateQuestionException.class })
	public void saveQuestion(Question question) {

		Set<Answer> answers = question.getAnswers();
		question.setAnswers(null);
		Long id = questionDao.saveQuestion(question);
		Question ques = questionDao.findQuestionById(id);
		Set<Answer> answerList = new HashSet<Answer>();

		for (Answer ans :answers){
			ans.setQuestion(ques);
			answerList.add(ans);
		}    
		ques.setAnswers(answerList);
		questionDao.updateQuestion(ques);
		return;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public List<Question> findQuestion(QuestionSCDO questionSCDO) {
		return questionDao.findQuestion(questionSCDO);
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteQuestion(Question question) {
		questionDao.deleteQuestion(question);
	}	
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Question findQuestionById(Long id) throws QuestionNotFoundException {
		Question question = questionDao.findQuestionById(id);
		if(question == null || question.equals("")) {
			throw new QuestionNotFoundException();
		}
		return question;
		
	}	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = { DuplicateQuestionException.class })
	public Question updateQuestion(Question question) {
		Question ques = questionDao.findQuestionById(question.getId());
		Set<Answer> answerList = new HashSet<Answer>();

		for (Answer ans :question.getAnswers()){
			ans.setQuestion(ques);
			answerList.add(ans);
		}   
		for (Answer ans :ques.getAnswers()) {
			if (question.getAnswers().contains(ans)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Contain answer");
				}
			}
			else {
				questionDao.deleteAnswer(ans);
			}
		}
		ques.setAnswers(answerList);
		ques.setId(question.getId());
		ques.setModule(question.getModule());
		ques.setHint(question.getHint());
		ques.setLevel(question.getLevel());
		ques.setQuestion(question.getQuestion());
		ques.setReference(question.getReference());
		ques.setType(question.getType());
		
		return questionDao.updateQuestion(ques);
	}

	@Override
	public List<Question> findRandomQuestionByLevel(String moduleCode, int level, int limit) {
		return questionDao.findRandomQuestionByLevel(moduleCode, level, limit);
	}	
	
}
