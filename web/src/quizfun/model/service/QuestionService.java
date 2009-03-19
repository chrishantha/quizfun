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

import java.util.List;

import quizfun.model.dto.QuestionSCDO;
import quizfun.model.entity.Question;
import quizfun.model.exception.DuplicateQuestionException;
import quizfun.model.exception.ModuleNotFoundException;
import quizfun.model.exception.QuestionNotFoundException;

/**
 * @author Hiranya Mudunkotuwa
 */
public interface QuestionService {

	void saveQuestion(Question question) throws DuplicateQuestionException, ModuleNotFoundException;

	List<Question> findQuestion(QuestionSCDO questionSCDO);

	void deleteQuestion(Question question);

	Question findQuestionById(Long id) throws QuestionNotFoundException;

	Question updateQuestion(Question question);
	
	List<Question> findRandomQuestionByLevel(String moduleCode, int level, int limit);

}
