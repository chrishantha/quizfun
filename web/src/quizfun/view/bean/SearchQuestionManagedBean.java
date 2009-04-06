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
import org.springframework.dao.DataIntegrityViolationException;

import quizfun.model.dto.ModuleSCDO;
import quizfun.model.dto.QuestionSCDO;
import quizfun.model.entity.Module;
import quizfun.model.entity.Question;
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
public class SearchQuestionManagedBean extends QuestionManagedBean{
	final Logger logger = LoggerFactory.getLogger(SearchQuestionManagedBean.class);

	private List<Question> questions;

	private List<Question> filterList;

	private TextFilterator<Question> questionFilterator;

	private TextMatcherEditor<Question> questionMatcherEditor;

	private String filterText;

	private HtmlInputText filterInputText;

	private HtmlDataTable dataTable;

	private boolean removeConfirmVisible;

	private Question removingQuestion;
	
	private HtmlInputText moduleCodeInputText;
	private HtmlInputText moduleNameInputText;
	
	private int selectedIndex = -1;
		
	@javax.annotation.PostConstruct
	public void init() {
		question = new Question();
		module = new Module();
		questionFilterator = new TextFilterator<Question>() {

			@Override
			public void getFilterStrings(List<String> baseList, Question question) {
				baseList.add(String.valueOf(question.getId()));
				baseList.add(question.getQuestion());
				baseList.add(question.getModule().getCode());
				baseList.add(question.getModule().getName());
			}

		};
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public List<Question> getFilterList() {
		return filterList;
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

	public boolean isRemoveConfirmVisible() {
		return removeConfirmVisible;
	}

	public Question getRemovingQuestion() {
		return removingQuestion;
	}

	public void searchActionListener(ActionEvent event) {
		ModuleSCDO moduleSCDO = new ModuleSCDO();
		QuestionSCDO questionSCDO = new QuestionSCDO();

		try {
			moduleSCDO.setCode(module.getCode());
			moduleSCDO.setName(module.getName());
			questionSCDO.setModuleSCDO(moduleSCDO);
			questionSCDO.setId(question.getId());
			questionSCDO.setQuestion(question.getQuestion());
			questionSCDO.setLevel(question.getLevel());
			questionSCDO.setType(question.getType());
			questionSCDO.setHint(question.getHint());
			questionSCDO.setReference(question.getReference());
			
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

	public void filterActionListener(ActionEvent event) {
		filterList();
		if (filterList == null || filterList.isEmpty()) {
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
		questionMatcherEditor.setMode(TextMatcherEditor.CONTAINS);
		questionMatcherEditor.setFilterText((filterText == null) ? new String[] {} : new String[] { filterText });
	}

	public String editAction() {
		selectedIndex = dataTable.getRowIndex();
		Object object = dataTable.getRowData();
		if (object != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Going to edit: {}", object);
			}
			JSFUtils.storeOnSessionMap("question", object);
			return "modifyQuestion";
		}
		return null;
	}

	public void removeConfirmActionListener(ActionEvent event) {
		removingQuestion = (Question) dataTable.getRowData();
		removeConfirmVisible = removingQuestion != null;
		if (removeConfirmVisible) {
			if (logger.isDebugEnabled()) {
				logger.debug("Going to remove: {}", removingQuestion);
			}
		}
	}

	public void removeActionListener(ActionEvent event) {
		if (removingQuestion == null) {
			return;
		}

		try {
			serviceLocator.getQuestionService().deleteQuestion(removingQuestion);
			questions.remove(removingQuestion);
			questionMatcherEditor = new TextMatcherEditor<Question>(questionFilterator);
			filterList = new FilterList<Question>(GlazedLists.eventList(questions), questionMatcherEditor);
			filterList();
			JSFUtils.addFacesInfoMessage("question.remove.successful");
		} catch (DataIntegrityViolationException e) {
			logger.error("Exception when deleting question: " + removingQuestion, e);
			JSFUtils.addFacesErrorMessage("course.remove.integrityviolation.error", new Object[] {removingQuestion.getId()});
			return;
		} catch (Throwable e) {
			logger.error("Exception when deleting question: " + removingQuestion, e);
			JSFUtils.addApplicationErrorMessage();
			return;
		} finally {
			removeConfirmVisible = false;
		}
	}
	
	public void clearActionListener(ActionEvent event) {
		super.clearActionListener(event);
		
		module.setCode(null);
		module.setName(null);
		
		moduleCodeInputText.resetValue();
		moduleNameInputText.resetValue();
	}	

	public String modifyBackAction() {
		Question question = (Question) JSFUtils.removeFromRequestMap("question");
		if (question != null && selectedIndex >= 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("Going back to search page. Setting modified object to index {}. Object: {}", selectedIndex, question);
			}
			filterList.set(selectedIndex, question);
		}
		// Remove the modify managed bean
		JSFUtils.removeFromRequestMap("modifyQuestion");
		return "back";
	}	
	
	public void closeRemoveActionListener(ActionEvent event) {
		removeConfirmVisible = false;
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
}
