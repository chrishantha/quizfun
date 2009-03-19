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

import java.util.List;

/**
 * @author Hiranya Mudunkotuwa
 */
public class UserGameDTO {

	private String userName;
	private Long marks;
	private Long gameId;
	private List<QuesAnswDTO> QuesAnswDTOs;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getMarks() {
		return marks;
	}
	public void setMarks(Long marks) {
		this.marks = marks;
	}
	public Long getGameId() {
		return gameId;
	}
	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	public List<QuesAnswDTO> getQuesAnswDTOs() {
		return QuesAnswDTOs;
	}
	public void setQuesAnswDTOs(List<QuesAnswDTO> quesAnswDTOs) {
		QuesAnswDTOs = quesAnswDTOs;
	}
}
