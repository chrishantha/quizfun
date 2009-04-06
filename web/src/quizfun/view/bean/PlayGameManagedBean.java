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

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quizfun.model.dto.GameDTO;
import quizfun.model.dto.QuesAnswDTO;
import quizfun.model.dto.UserGameDTO;
import quizfun.model.entity.Answer;
import quizfun.model.entity.Game;
import quizfun.model.entity.Question;
import quizfun.view.servicelocator.ServiceLocator;
import quizfun.view.util.JSFUtils;

import com.icesoft.faces.component.ext.HtmlOutputText;

/**
 * @author Hiranya Mudunkotuwa
 */
public class PlayGameManagedBean {
	
	final Logger logger = LoggerFactory.getLogger(PlayGameManagedBean.class);
	
	private ServiceLocator serviceLocator;
	private Game selectedGame; 
	private Game game;
	private List<Question> quesList;
	private List<Answer> ansList;
	private List<GameDTO> gameDTOs;
	private List<UserGameDTO> userGameDTOs;

	HtmlOutputText idOutputText;

	private ArrayList<Color> colors;
	
	@SuppressWarnings("unchecked")
	@javax.annotation.PostConstruct
	public void init() {
		game = new Game();
		quesList = new ArrayList<Question>();
		ansList = new ArrayList<Answer>();
		gameDTOs =  new ArrayList<GameDTO>();
		userGameDTOs = new ArrayList<UserGameDTO>();
		selectedGame = (Game) JSFUtils.removeFromSessionMap("game");
		if (logger.isDebugEnabled()) {
			logger.debug("Object retrieved from session: {}", selectedGame);
		}
		resetValues();

        // build colors
        colors = new ArrayList(Arrays.asList(
                new Color[]{new Color(222, 29, 42),
                new Color(255, 217, 0),
                new Color(90, 103, 39),
                new Color(148, 179, 203)}));
	}
	
	@SuppressWarnings("unchecked")
	private void resetValues() {

		game.setId(selectedGame.getId());
		Game sgame = new Game();

		sgame = serviceLocator.getGameService().findGameById(
				selectedGame.getId());

		game.setQuestions(sgame.getQuestions());
		quesList = new ArrayList<Question>(game.getQuestions());

		//Should get UserGameDTOs
		for(Question qq :quesList) {

			
			GameDTO gameDTO = new GameDTO();
			gameDTO.setGameId(selectedGame.getId());
			gameDTO.setQuestion(qq);	
			//Setting Answers
			 ansList = new ArrayList<Answer>(qq.getAnswers());
			gameDTO.setLabels(new ArrayList<String>());
			for (Answer a :ansList) {
				gameDTO.getLabels().add(a.getAnswer());
			}
			Random random = new Random();
			Double ans1 = 10 + Math.ceil(30 * random.nextDouble());
			Double ans2 = 10 + Math.ceil(30 * random.nextDouble());
			Double ans3 = 10 + Math.ceil(30 * random.nextDouble());
			Double ans4 = 10 + Math.ceil(30 * random.nextDouble());
			
			ArrayList chartData = new ArrayList(Arrays.asList(
	                new Double[]{ans1, ans2,ans3,ans4}));
			gameDTO.setChartData(chartData);
			gameDTOs.add(gameDTO);
		}
	}
	
	@SuppressWarnings("unchecked")
	public GameDTO generatingChart(Question ques, GameDTO gameDTO) {
		//Setting Answers
		gameDTO.setLabels(new ArrayList<String>());
		Double ans1 = new Double(0);
		Double ans2 = new Double(0);
		Double ans3 = new Double(0);
		Double ans4 = new Double(0);
		Answer a1 = new Answer();
		Answer a2 = new Answer();
		Answer a3 = new Answer();
		Answer a4 = new Answer();
		int i = 1;
		for (Answer a :ansList) {
			gameDTO.getLabels().add(a.getAnswer());
			
			if(i == 1) {
				a1 = a;
			} else if (i == 2) {
				a2 = a;
			} else if (i == 3) {
				a3 = a;
			} else if (i == 4) {
				a4 = a;
			}
			i++;
		}
		
		
		for(UserGameDTO ugDTO :userGameDTOs) {
			List<QuesAnswDTO> qaDTOs = ugDTO.getQuesAnswDTOs();
			
			for(QuesAnswDTO aq :qaDTOs) {
				if(aq.getQuestionId().equals(ques.getId())) {
					if(aq.getGivenAnsId().equals(a1.getId())) {
						ans1++;
					}
					if(aq.getGivenAnsId().equals(a2.getId())) {
						ans2++;
					}
					if(aq.getGivenAnsId().equals(a3.getId())) {
						ans3++;
					}
					if(aq.getGivenAnsId().equals(a4.getId())) {
						ans4++;
					}
				}
			}
		}
		//Temp
		ArrayList chartData = new ArrayList(Arrays.asList(
                new Double[]{ans1, ans2,ans3,ans4}));
		gameDTO.setChartData(chartData); // This should change accordingly to total hits.
		return gameDTO;
	}
	public void setServiceLocator(ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	public Game getSelectedGame() {
		return selectedGame;
	}

	public void setSelectedGame(Game selectedGame) {
		this.selectedGame = selectedGame;
	}
	
	
	public HtmlOutputText getIdOutputText() {
		return idOutputText;
	}

	public void setIdOutputText(HtmlOutputText idOutputText) {
		this.idOutputText = idOutputText;
	}
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	
	public List<Question> getQuesList() {
		return quesList;
	}
	public void setQuesList(List<Question> quesList) {
		this.quesList = quesList;
	}
	public ArrayList<Color> getColors() {
		return colors;
	}
	public void setColors(ArrayList<Color> colors) {
		this.colors = colors;
	}
	public List<Answer> getAnsList() {
		return ansList;
	}
	public void setAnsList(List<Answer> ansList) {
		this.ansList = ansList;
	}

	public List<GameDTO> getGameDTOs() {
		return gameDTOs;
	}

	public void setGameDTOs(List<GameDTO> gameDTOs) {
		this.gameDTOs = gameDTOs;
	}

	public List<UserGameDTO> getUserGameDTOs() {
		return userGameDTOs;
	}

	public void setUserGameDTOs(List<UserGameDTO> userGameDTOs) {
		this.userGameDTOs = userGameDTOs;
	}
}
