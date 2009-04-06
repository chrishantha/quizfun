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

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import quizfun.model.dto.ModuleSCDO;
import quizfun.model.dto.QuestionSCDO;
import quizfun.model.entity.Answer;
import quizfun.model.entity.Question;

/**
 * @author Hiranya Mudunkotuwa
 */
public class HibernateQuestionDao extends HibernateDaoSupport implements QuestionDao {

	final Logger logger = LoggerFactory.getLogger(HibernateQuestionDao.class);
	@Override
	public Long saveQuestion(Question question) {
		if (logger.isDebugEnabled()) {
			logger.info("Saving Question: " + question);
		}
		Long id = null;
		Session session = getSession(false);
		try {
			id = (Long) session.save(question);
			
		} catch (HibernateException ex) {
			throw convertHibernateAccessException(ex);
		}
		
		return id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Question> findQuestion(QuestionSCDO questionSCDO) {
		if (logger.isDebugEnabled()) {
			logger.debug("Finding Question(s): {}", questionSCDO);
		}
		List<Question> list = null;
		Session session = getSession(false);
		try {
			@SuppressWarnings("unused")
			Long id = questionSCDO.getId();
			String ques = questionSCDO.getQuestion();
			int type = questionSCDO.getType();
			int level = questionSCDO.getLevel();
			String hint = questionSCDO.getHint();
			String reference = questionSCDO.getReference();
			ModuleSCDO moduleSCDO = questionSCDO.getModuleSCDO();

			String moduleCode = moduleSCDO.getCode();
			String moduleName = moduleSCDO.getName();

			Criteria criteria = session.createCriteria(Question.class);
			if (ques != null && !ques.equals("")) {
				criteria.add(Restrictions
						.like("question", ques, MatchMode.ANYWHERE));
			}
			if (type != 0) {
				if (String.valueOf(type) != null && !String.valueOf(type).equals("")) {
					criteria.add(Restrictions
							.like("type", type));
				}				
			}

			if (level != 0) {
				if (String.valueOf(level) != null && !String.valueOf(level).equals("")) {
					criteria.add(Restrictions
							.like("level", level));
				}				
			}
			
			if (hint != null && !hint.equals("")) {
				criteria.add(Restrictions
						.like("hint", hint, MatchMode.ANYWHERE));
			}
			if (reference != null && !reference.equals("")) {
				criteria.add(Restrictions
						.like("reference", reference, MatchMode.ANYWHERE));
			}				

			criteria.addOrder(Order.asc("id"));

			if ((moduleCode != null && !moduleCode.isEmpty())
					|| (moduleName != null && !moduleName.isEmpty())) {
				criteria = criteria.createCriteria("module");
			}

			if (moduleCode != null && !moduleCode.isEmpty()) {
				criteria.add(Restrictions.like("code", moduleCode,
						MatchMode.ANYWHERE));
			}
			if (moduleName != null && !moduleName.isEmpty()) {
				criteria.add(Restrictions.like("name", moduleName,
						MatchMode.ANYWHERE));
			}

			list = criteria.list();
		} catch (HibernateException ex) {
			throw convertHibernateAccessException(ex);
		}
		return list;
	}
	
	@Override	
	public Question findQuestionById (Long id){
		if(logger.isDebugEnabled()){
			logger.debug("Going to find Question");
		}
		Session session = getSession(false);

		Question question = null;
		try {
			question = (Question) session.get(Question.class, id);
	        Hibernate.initialize(question.getAnswers());
		} catch (HibernateException e) {
			throw convertHibernateAccessException(e);
		}
		
		if(question != null){
			if(logger.isDebugEnabled()){
				logger.debug("Returning " + question);
			}
			return question;	
		}
		return null;

	}	
	
	@Override
	public Question updateQuestion(Question question) {
		if (logger.isDebugEnabled()) {
			logger.info("Updating Question: " + question);
		}
		Session session = getSession(false);
		try {
			return (Question)session.merge(question);
			
		} catch (HibernateException ex) {
			throw convertHibernateAccessException(ex);
		}
	}
	

	@Override
	public void deleteQuestion(Question question) {
		if (logger.isDebugEnabled()) {
			logger.debug("Deleting Question: {}", question);
		}
		Session session = getSession(false);
		try {
			session.delete(question);
		} catch (HibernateException ex) {
			throw convertHibernateAccessException(ex);
		}
	}
	
	@Override
	public void deleteAnswer(Answer answer) {
		if (logger.isDebugEnabled()) {
			logger.debug("Deleting Answer: {}", answer);
		}
		Session session = getSession(false);
		try {
			session.delete(answer);
		} catch (HibernateException ex) {
			throw convertHibernateAccessException(ex);
		}
	}	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Question> findRandomQuestionByLevel(String moduleCode, int level, int limit) {
		if (logger.isDebugEnabled()) {
			logger.debug("Finding {} Random Questions by Level: {}", limit, level);
		}
		Session session = getSession(false);
		List<Question> list = null;
		try {
			list = session.createQuery("from Question o where o.module = :module and o.level = :level order by rand()").setString(
					"module", moduleCode).setInteger("level", level).setMaxResults(limit).list();
		} catch (HibernateException ex) {
			throw convertHibernateAccessException(ex);
		}
		return list;
	}
}
