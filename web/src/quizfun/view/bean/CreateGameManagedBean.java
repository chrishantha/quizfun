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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quizfun.model.dto.ModuleSCDO;
import quizfun.model.dto.QuestionSCDO;
import quizfun.model.entity.Game;
import quizfun.model.entity.Question;
import quizfun.view.util.ICEfacesUtils;
import quizfun.view.util.JSFUtils;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.matchers.TextMatcherEditor;

import com.icesoft.faces.component.ext.HtmlSelectOneRadio;
import com.icesoft.faces.component.panelpositioned.PanelPositionedEvent;


/**
 * @author Nevindaree Premarathne & Hiranya Mudunkotuwa
 */
public class CreateGameManagedBean extends GameManagedBean{

	final Logger logger = LoggerFactory.getLogger(CreateGameManagedBean.class);
	private boolean random;
	private boolean showQuesType;
	private boolean showViewQues;	
	private boolean showModule;
	private boolean showSelectQues;
	private boolean selected;
	// default panel
	private String selectedGroup = "groupOne";


	
	@javax.annotation.PostConstruct
	public void init() {	
		game = new Game();
		selectedQuestions = new ArrayList<Question>(0);
		easyQues = new ArrayList<Question>(); 
		mediumQues = new ArrayList<Question>(); 
		hardQues = new ArrayList<Question>(); 
		//game.setQuestions(null);
		initializeModuleSelectInput();		
		random = true;
		showQuesType = true;
		showViewQues = false;
		showSelectQues = false;
		showModule = true;
		selected = false;
		questionFilterator = new TextFilterator<Question>() {

			@Override
			public void getFilterStrings(List<String> baseList, Question question) {
				baseList.add(String.valueOf(question.getId()));
				baseList.add(question.getQuestion());;
				baseList.add(question.getModule().getCode());
				baseList.add(question.getModule().getName());
			}

		};
	}

	public void randomValueChangeEvent(ValueChangeEvent event) {
		HtmlSelectOneRadio htmlSelectOneRadio = (HtmlSelectOneRadio) event.getComponent();
		String ran = null;
		if (htmlSelectOneRadio.getValue() != null) {
			ran = (String) htmlSelectOneRadio.getValue();				
		}
		
		if (ran != null && !ran.equals("")) {
			if(ran.equals("random")) {
				random = true;
			}		
			else if (ran.equals("other")){				
				random = false;				
			}			
		} 

	}	
	
	public void nextToQuesTypeActionListener(ActionEvent event) {
		if (random) {
			generateRandomQuestions();
			showViewQues = true;
			showModule = false;	
			showQuesType = false;
			showSelectQues = false;
		}
		else if (!random){
			searchQuestions();

			showModule = false;
			showQuesType = false;
			showSelectQues = true;
		}
		else {
			showQuesType = true;
		}
	}
	
	public void searchQuestions() {
		ModuleSCDO moduleSCDO = new ModuleSCDO();
		QuestionSCDO questionSCDO = new QuestionSCDO();
		if(module == null) {
			JSFUtils.addFacesErrorMessage("game.module.required.message");
			showQuesType = true;
			return;
		}

		try {
			moduleSCDO.setCode(module.getCode());
			moduleSCDO.setName(module.getName());
			questionSCDO.setModuleSCDO(moduleSCDO);
			
			questions = serviceLocator.getQuestionService().findQuestion(questionSCDO);
			if (questions == null || questions.isEmpty()) {
				filterList = null;
				JSFUtils.addFacesInfoMessage("common.search.empty");
			} else {
				questionMatcherEditor = new TextMatcherEditor<Question>(questionFilterator);
				filterList = new FilterList<Question>(GlazedLists.eventList(questions), questionMatcherEditor);
				ICEfacesUtils.setFocus(filterInputText);
			}
		} catch (Throwable e) {
			logger.error("Exception when finding question: " + questionSCDO, e);
			JSFUtils.addApplicationErrorMessage();
			return;
		}
	}
	
	
	public void selectedQuesValueChangeEvent(ValueChangeEvent event) {
		Question quesSelected = (Question) dataTable.getRowData();
		HtmlSelectBooleanCheckbox selectBooleanCheckbox = (HtmlSelectBooleanCheckbox) event.getComponent();
		Boolean select = false;
		if (selectBooleanCheckbox.getValue() != null) {
			select = (Boolean) selectBooleanCheckbox.getValue();
		}
		
		if (select) {
			if (!(selectedQuestions.contains(quesSelected))) {
				quesSelected.setSelected(true);
				selectedQuestions.add(quesSelected);
			}
			
		}
		else {
			if (!(selectedQuestions.contains(quesSelected))) {
				quesSelected.setSelected(false);
				selectedQuestions.remove(quesSelected);

			}		
		}

			
	}	
	
	public void backActionListener(ActionEvent event) {
		showQuesType = true;
		showModule = true;
		showSelectQues = false;
		questions = new ArrayList<Question>(); 
		selectedQuestions = new ArrayList<Question>(); 
		easyQues = new ArrayList<Question>(); 
		mediumQues = new ArrayList<Question>(); 
		hardQues = new ArrayList<Question>(); 
		quesHtmlSelectOneRadio.resetValue();
		//quesHtmlSelectOneRadio.r
	}

	
	public void nextActionListener(ActionEvent event) {

		// order the questions and show.
		if (selectedQuestions != null || !selectedQuestions.isEmpty()) {
			for (Question ques : selectedQuestions) {
				if (ques.getLevel() == Question.LEVEL_EASY) {
					easyQues.add(ques);					
				} else if (ques.getLevel() == Question.LEVEL_MEDIUM) {
					mediumQues.add(ques);
				} else if (ques.getLevel() == Question.LEVEL_HARD) {
					hardQues.add(ques);
				}
			}	
			
			if((easyQues.size() > 5) || (mediumQues.size() > 5) || (hardQues.size() > 5)) {
				JSFUtils.addFacesErrorMessage("game.ques.limit.message");
				return;
			}
			showViewQues = true;
			showQuesType = false;
			showSelectQues = false;
			
		}
		
		else {
			JSFUtils.addFacesErrorMessage("game.ques.required.message");
			showQuesType = false;
			showViewQues = false;
			showSelectQues = true;
		}

		
	}	
	
	
	public void generateRandomQuestions() {
		searchQuestions();

		List<Long> indexes = new ArrayList<Long>();
		for (Question ques : questions) {
			indexes.add(ques.getId());
			logger.debug("Question Id : " + ques.getId());
		}
		Random random = new Random();
		int listSize = 0;
		do {
			// Long id = showRandomNumber(new Long(1), new Long(20), random);
			int id = random.nextInt(50);
			logger.debug("Generated : " + id);
			for (Question ques : questions) {
				if (ques.getId().equals(Long.valueOf(id))) {
					if ((ques.getLevel() == Question.LEVEL_EASY) && (!easyQues.contains(ques)) && (easyQues.size() < 5)) {
						easyQues.add(ques);
						listSize++;
						logger.debug("Easy Question Id : " + ques.getId());
					} 
					else if ((ques.getLevel() == Question.LEVEL_MEDIUM) && (!mediumQues.contains(ques)) && (mediumQues.size() < 5)) {
						mediumQues.add(ques);
						listSize++;
						logger.debug("Medium Question Id : " + ques.getId());
					} 
					else if ((ques.getLevel() == Question.LEVEL_HARD) && (!hardQues.contains(ques)) && (hardQues.size() < 5)) {
						hardQues.add(ques);
						listSize++;
						logger.debug("Hard Question Id : " + ques.getId());
					}
				}
			}
		} while (listSize < 15);

	}

	private void resetEasyId() {
		for (int i = 0; i < easyQues.size(); i++) {
			((Question) easyQues.get(i)).setId(Long.valueOf(i) + 1);
		}
	}

	public void changedEasy(PanelPositionedEvent evt) {
		resetEasyId();
	}

	private void resetMidId() {
		for (int i = 0; i < mediumQues.size(); i++) {
			((Question) mediumQues.get(i)).setId(Long.valueOf(i) + 1);
		}
	}

	public void changedMedium(PanelPositionedEvent evt) {
		resetMidId();
	}

	private void resetHardId() {
		for (int i = 0; i < hardQues.size(); i++) {
			((Question) hardQues.get(i)).setId(Long.valueOf(i) + 1);
		}
	}

	public void changedHard(PanelPositionedEvent evt) {
		resetHardId();
	}
	    
	public void saveActionListener(ActionEvent event) {
		if (module == null) {
			JSFUtils.addFacesErrorMessage("game.module.required.message");
			ICEfacesUtils.setFocus(moduleSelectInputText);
			return;
		}		
		
		if((easyQues.size() < 5) || (mediumQues.size() < 5) || (hardQues.size() < 5)) {
			JSFUtils.addFacesErrorMessage("game.queslevel.limit.message");
			return;
		}
		
		try {
			List<Question> sq = new ArrayList<Question>();
			sq.addAll(easyQues);
			sq.addAll(mediumQues);
			sq.addAll(hardQues);

			game.setQuestions(new HashSet<Question>(sq));
			game.setModule(module);
			game.setStatus("N");
			serviceLocator.getGameService().saveGame(game);
			super.clearModuleActionListener(event);
			//clearValues();
			ICEfacesUtils.setFocus(moduleSelectInputText);
			JSFUtils.addFacesInfoMessage("game.save.successful");
		
		} catch (Throwable e) {
			logger.error("Exception when saving game: " + game, e);
			JSFUtils.addApplicationErrorMessage();
			return;
		}
	}
	
	public void backFromViewActionListener(ActionEvent event) {
	//	super.clearModuleActionListener(event);  
		if (random) {
			questions = new ArrayList<Question>(); 
			selectedQuestions = new ArrayList<Question>(); 
			easyQues = new ArrayList<Question>(); 
			mediumQues = new ArrayList<Question>(); 
			hardQues = new ArrayList<Question>(); 
			showQuesType = true;
			showModule = true;
			showViewQues = false;
			random = false;
			showSelectQues = false;
		}
		else{
/*			questions = new ArrayList<Question>(); 
			selectedQuestions = new ArrayList<Question>(); 
			easyQues = new ArrayList<Question>(); 
			mediumQues = new ArrayList<Question>(); 
			hardQues = new ArrayList<Question>(); */			
			showQuesType = false;
			showModule = false;
			showViewQues = false;	
			showSelectQues = true;
		}

		//quesHtmlSelectOneRadio.r
	}
	
	/**
	 * Gets the selected panel group.
	 * 
	 * @return the panel group id
	 */
	public String getSelectedGroup() {
		return selectedGroup;
	}

	/**
	 * Sets the selected panel group.
	 * 
	 * @param selectedGroup the new panel group id
	 */
	public void setSelectedGroup(String selectedGroup) {
		this.selectedGroup = selectedGroup;
	}

	public boolean isRandom() {
		return random;
	}

	public void setRandom(boolean random) {
		this.random = random;
	}

	public boolean isShowQuesType() {
		return showQuesType;
	}

	public void setShowQuesType(boolean showQuesType) {
		this.showQuesType = showQuesType;
	}

	public boolean isShowViewQues() {
		return showViewQues;
	}

	public void setShowViewQues(boolean showViewQues) {
		this.showViewQues = showViewQues;
	}


	public boolean isShowModule() {
		return showModule;
	}

	public void setShowModule(boolean showModule) {
		this.showModule = showModule;
	}


	public boolean isSelected() {
		return selected;
	}


	public void setSelected(boolean selected) {
		this.selected = selected;
	}


	public boolean isShowSelectQues() {
		return showSelectQues;
	}


	public void setShowSelectQues(boolean showSelectQues) {
		this.showSelectQues = showSelectQues;
	}	
}
