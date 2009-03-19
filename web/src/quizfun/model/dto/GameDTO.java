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

package quizfun.model.dto;

import java.util.ArrayList;

import quizfun.model.entity.Question;

/**
 * @author Hiranya Mudunkotuwa
 */
public class GameDTO {

	private Long gameId;
	private Question question;
	@SuppressWarnings("unchecked")
	private ArrayList chartData;
	private ArrayList<String> labels;
	
	public Long getGameId() {
		return gameId;
	}
	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	@SuppressWarnings("unchecked")
	public ArrayList getChartData() {
		return chartData;
	}
	@SuppressWarnings("unchecked")
	public void setChartData(ArrayList chartData) {
		this.chartData = chartData;
	}
	public ArrayList<String> getLabels() {
		return labels;
	}
	public void setLabels(ArrayList<String> labels) {
		this.labels = labels;
	}
}
