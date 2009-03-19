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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import quizfun.model.dao.GameDao;
import quizfun.model.dto.GameSCDO;
import quizfun.model.entity.Game;
import quizfun.model.exception.DuplicateQuestionException;

public class GameServiceImpl implements GameService {
	final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

	private GameDao gameDao;
	private ModuleService moduleService;

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = { DuplicateQuestionException.class })
	public void saveGame(Game game) {
		gameDao.saveGame(game);
		return;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public List<Game> findGame(GameSCDO gameSCDO) {
		return gameDao.findGame(gameSCDO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Game findGameById(Long id) {
		Game game = gameDao.findGameById(id);
		return game;
	}

	public ModuleService getModuleService() {
		return moduleService;
	}

	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}

	public void setGameDao(GameDao gameDao) {
		this.gameDao = gameDao;
	}
}
