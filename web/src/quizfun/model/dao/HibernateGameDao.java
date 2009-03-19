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

import quizfun.model.dto.GameSCDO;
import quizfun.model.dto.ModuleSCDO;
import quizfun.model.entity.Game;

/**
 * @author Hiranya Mudunkotuwa
 */
public class HibernateGameDao extends HibernateDaoSupport implements GameDao {

	final Logger logger = LoggerFactory.getLogger(HibernateGameDao.class);
	
	@Override
	public Long saveGame(Game game) {
		if (logger.isDebugEnabled()) {
			logger.info("Saving Game: " + game);
		}
		Long id = null;
		Session session = getSession(false);
		try {
			id = (Long) session.save(game);
			
		} catch (HibernateException ex) {
			throw convertHibernateAccessException(ex);
		}
		
		return id;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Game> findGame(GameSCDO gameSCDO) {
		if (logger.isDebugEnabled()) {
			logger.debug("Finding Game(s): {}", gameSCDO);
		}
		List<Game> list = null;
		Session session = getSession(false);
		try {
			Long id = gameSCDO.getId();
			ModuleSCDO moduleSCDO = gameSCDO.getModuleSCDO();

			String moduleCode = moduleSCDO.getCode();
			String moduleName = moduleSCDO.getName();

			Criteria criteria = session.createCriteria(Game.class);
			if (id != null && !id.equals("")) {
				criteria.add(Restrictions
						.like("id", String.valueOf(id), MatchMode.ANYWHERE));
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
	public Game findGameById (Long id){
		if(logger.isDebugEnabled()){
			logger.debug("Going to find Game");
		}
		Session session = getSession(false);

		Game game = new Game();
		try {
			game = (Game) session.get(Game.class, id);
			session.refresh(game);
	        Hibernate.initialize(game.getQuestions());
		} catch (HibernateException e) {
			throw convertHibernateAccessException(e);
		}
		
		if(game != null){
			if(logger.isDebugEnabled()){
				logger.debug("Returning " + game.getId() + " Question");
			}
			return game;	
		}
		return null;
	}		

}
