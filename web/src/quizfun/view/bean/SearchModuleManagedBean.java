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

import quizfun.model.dto.CourseSCDO;
import quizfun.model.dto.ModuleSCDO;
import quizfun.model.entity.Course;
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
 * @author Nevindaree Premarathne
 */
public class SearchModuleManagedBean extends ModuleManagedBean {

	final Logger logger = LoggerFactory.getLogger(SearchModuleManagedBean.class);

	private List<Module> modules;

	private List<Module> filterList;

	private TextFilterator<Module> moduleFilterator;

	private TextMatcherEditor<Module> moduleMatcherEditor;

	private String filterText;

	private HtmlInputText filterInputText;

	private HtmlDataTable dataTable;

	private boolean removeConfirmVisible;

	private Module removingModule;
	
	private HtmlInputText courseCodeInputText;
	private HtmlInputText courseNameInputText;
	
	private int selectedIndex = -1;

	@javax.annotation.PostConstruct
	public void init() {
		module = new Module();
		course = new Course();

		moduleFilterator = new TextFilterator<Module>() {

			@Override
			public void getFilterStrings(List<String> baseList, Module module) {
				baseList.add(module.getCode());
				baseList.add(module.getName());
				baseList.add(module.getCourse().getCode());
				baseList.add(module.getCourse().getName());
			}

		};
	}

	public List<Module> getModules() {
		return modules;
	}

	public List<Module> getFilterList() {
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

	public Module getRemovingModule() {
		return removingModule;
	}

	public HtmlInputText getCourseCodeInputText() {
		return courseCodeInputText;
	}

	public void setCourseCodeInputText(HtmlInputText courseCodeInputText) {
		this.courseCodeInputText = courseCodeInputText;
	}

	public HtmlInputText getCourseNameInputText() {
		return courseNameInputText;
	}

	public void setCourseNameInputText(HtmlInputText courseNameInputText) {
		this.courseNameInputText = courseNameInputText;
	}

	public void searchActionListener(ActionEvent event) {
		ModuleSCDO moduleSCDO = new ModuleSCDO();
		CourseSCDO courseSCDO = new CourseSCDO();

		try {
			courseSCDO.setCode(course.getCode());
			courseSCDO.setName(course.getName());
			moduleSCDO.setCourseSCDO(courseSCDO);
			moduleSCDO.setCode(module.getCode());
			moduleSCDO.setName(module.getName());
			modules = serviceLocator.getModuleService().findModule(moduleSCDO);
			if (modules == null || modules.isEmpty()) {
				filterList = null;
				JSFUtils.addFacesInfoMessage("common.search.empty");
			} else {
				moduleMatcherEditor = new TextMatcherEditor<Module>(moduleFilterator);
				filterList = new FilterList<Module>(GlazedLists.eventList(modules), moduleMatcherEditor);
				ICEfacesUtils.setFocus(filterInputText);
			}
		} catch (Throwable e) {
			logger.error("Exception when finding module: " + moduleSCDO, e);
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
		moduleMatcherEditor.setMode(TextMatcherEditor.CONTAINS);
		moduleMatcherEditor.setFilterText((filterText == null) ? new String[] {} : new String[] { filterText });
	}

	public String editAction() {
		selectedIndex = dataTable.getRowIndex();
		Object object = dataTable.getRowData();
		if (object != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Going to edit: {}", object);
			}
			JSFUtils.storeOnSessionMap("module", object);
			return "modifyModule";
		}
		return null;
	}

	public void removeConfirmActionListener(ActionEvent event) {
		removingModule = (Module) dataTable.getRowData();
		removeConfirmVisible = removingModule != null;
		if (removeConfirmVisible) {
			if (logger.isDebugEnabled()) {
				logger.debug("Going to remove: {}", removingModule);
			}
		}
	}

	public void removeActionListener(ActionEvent event) {
		if (removingModule == null) {
			return;
		}

		try {
			serviceLocator.getModuleService().deleteModule(removingModule);
			modules.remove(removingModule);
			moduleMatcherEditor = new TextMatcherEditor<Module>(moduleFilterator);
			filterList = new FilterList<Module>(GlazedLists.eventList(modules), moduleMatcherEditor);
			filterList();
			JSFUtils.addFacesInfoMessage("module.remove.successful");
		} catch (DataIntegrityViolationException e) {
			logger.error("Exception when deleting module: " + removingModule, e);
			JSFUtils.addFacesErrorMessage("module.remove.integrityviolation.error", new Object[] {removingModule.getCode()});
			return;
		} catch (Throwable e) {
			logger.error("Exception when deleting module: " + removingModule, e);
			JSFUtils.addApplicationErrorMessage();
			return;
		} finally {
			removeConfirmVisible = false;
		}
	}

	public void closeRemoveActionListener(ActionEvent event) {
		removeConfirmVisible = false;
	}
	
	public void clearActionListener(ActionEvent event) {
		super.clearActionListener(event);
		
		course.setCode(null);
		course.setName(null);
		
		courseCodeInputText.resetValue();
		courseNameInputText.resetValue();
	}
	
	public String modifyBackAction() {
		Module module = (Module) JSFUtils.removeFromRequestMap("module");
		if (module != null && selectedIndex >= 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("Going back to search page. Setting modified object to index {}. Object: {}", selectedIndex, module);
			}
			filterList.set(selectedIndex, module);
		}
		// Remove the modify managed bean
		JSFUtils.removeFromRequestMap("modifyModule");
		return "back";
	}
}
