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

import java.util.List;

import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quizfun.model.dto.GameSCDO;
import quizfun.model.dto.ModuleSCDO;
import quizfun.model.entity.Game;
import quizfun.model.entity.Module;
import quizfun.view.util.ICEfacesUtils;
import quizfun.view.util.JSFUtils;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.matchers.TextMatcherEditor;

import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.ext.HtmlInputText;

/**
 * @author Hiranya Mudunkotuwa
 */
public class SearchGameManagedBean extends GameManagedBean{ 

	final Logger logger = LoggerFactory.getLogger(SearchGameManagedBean.class);

	private List<Game> games;

	private List<Game> gameFilterList;

	private TextFilterator<Game> gameFilterator;

	private TextMatcherEditor<Game> gameMatcherEditor;

	private String filterText;

	private HtmlInputText filterInputText;

	private HtmlDataTable dataTable;	
	
	private HtmlInputText moduleCodeInputText;
	private HtmlInputText moduleNameInputText;
	
	@javax.annotation.PostConstruct
	public void init() {
		game = new Game();
		module = new Module();
		gameFilterator = new TextFilterator<Game>() {

			@Override
			public void getFilterStrings(List<String> baseList, Game game) {
				baseList.add(String.valueOf(game.getId()));
				baseList.add(game.getModule().getCode());
				baseList.add(game.getModule().getName());
			}

		};
	}

	public void searchActionListener(ActionEvent event) {
		ModuleSCDO moduleSCDO = new ModuleSCDO();
		GameSCDO gameSCDO = new GameSCDO();

		try {
			moduleSCDO.setCode(module.getCode());
			moduleSCDO.setName(module.getName());
			gameSCDO.setModuleSCDO(moduleSCDO);
			gameSCDO.setId(game.getId());
			
			games = serviceLocator.getGameService().findGame(gameSCDO);
			if (games == null || games.isEmpty()) {
				gameFilterList = null;
				JSFUtils.addFacesInfoMessage("common.search.empty");
			} else {
				gameMatcherEditor = new TextMatcherEditor<Game>(gameFilterator);
				gameFilterList = new FilterList<Game>(GlazedLists.eventList(games), gameMatcherEditor);
				ICEfacesUtils.setFocus(filterInputText);
			}
		} catch (Throwable e) {
			logger.error("Exception when finding game: " + gameSCDO, e);
			JSFUtils.addApplicationErrorMessage();
			return;
		}
	}

	public void filterActionListener(ActionEvent event) {
		filterList();
		if (gameFilterList == null || gameFilterList.isEmpty()) {
			JSFUtils.addFacesInfoMessage(filterInputText, "common.filter.empty");
		}
		ICEfacesUtils.setFocus(filterInputText);
	}

	public void filterClearActionListener(ActionEvent event) {
		filterText = null;
		filterList();
		filterInputText.resetValue();
		ICEfacesUtils.setFocus(filterInputText);
	}

	private void filterList() {
		gameMatcherEditor.setMode(TextMatcherEditor.CONTAINS);
		gameMatcherEditor.setFilterText((filterText == null) ? new String[] {} : new String[] { filterText });
	}
	
	public String selectAction() {
		
	//	selectedIndex = dataTable.getRowIndex();
		Game gameSelected = (Game) dataTable.getRowData();
		if (gameSelected != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Going to play: {}", gameSelected);
			}
			JSFUtils.storeOnSessionMap("game", gameSelected);
			return "playGame";
		}
		return null;
	}
	
	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}

	public TextFilterator<Game> getGameFilterator() {
		return gameFilterator;
	}

	public void setGameFilterator(TextFilterator<Game> gameFilterator) {
		this.gameFilterator = gameFilterator;
	}

	public TextMatcherEditor<Game> getGameMatcherEditor() {
		return gameMatcherEditor;
	}

	public void setGameMatcherEditor(TextMatcherEditor<Game> gameMatcherEditor) {
		this.gameMatcherEditor = gameMatcherEditor;
	}

	public String getFilterText() {
		return filterText;
	}

	public void setFilterText(String filterText) {
		this.filterText = filterText;
	}

	public HtmlInputText getFilterInputText() {
		return filterInputText;
	}

	public void setFilterInputText(HtmlInputText filterInputText) {
		this.filterInputText = filterInputText;
	}

	public HtmlDataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public HtmlInputText getModuleCodeInputText() {
		return moduleCodeInputText;
	}

	public void setModuleCodeInputText(HtmlInputText moduleCodeInputText) {
		this.moduleCodeInputText = moduleCodeInputText;
	}

	public HtmlInputText getModuleNameInputText() {
		return moduleNameInputText;
	}

	public void setModuleNameInputText(HtmlInputText moduleNameInputText) {
		this.moduleNameInputText = moduleNameInputText;
	}

	public List<Game> getGameFilterList() {
		return gameFilterList;
	}

	public void setGameFilterList(List<Game> gameFilterList) {
		this.gameFilterList = gameFilterList;
	}
	
}
