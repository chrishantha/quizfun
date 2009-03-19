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

package quizfun.model.dao;

import java.util.List;

import quizfun.model.dto.QuestionSCDO;
import quizfun.model.entity.Answer;
import quizfun.model.entity.Question;

/**
 * @author Hiranya Mudunkotuwa
 */
public interface QuestionDao {

	Long saveQuestion(Question question);
	
	List<Question> findQuestion(QuestionSCDO questionSCDO);

	Question findQuestionById(Long id);

	Question updateQuestion(Question question);

	void deleteQuestion(Question question);

	void deleteAnswer(Answer answer);

	List<Question> findRandomQuestionByLevel(String moduleCode, int level, int limit);

}
