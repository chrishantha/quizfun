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
import quizfun.model.entity.Course;
import quizfun.view.util.JSFUtils;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.matchers.TextMatcherEditor;

import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.ext.HtmlInputText;

/**
 * @author M. Isuru Tharanga Chrishantha Perera
 */
public class SearchCourseManagedBean extends CourseManagedBean {

	final Logger logger = LoggerFactory.getLogger(SearchCourseManagedBean.class);

	private List<Course> courses;

	private List<Course> filterList;

	private TextFilterator<Course> courseFilterator;

	private TextMatcherEditor<Course> courseMatcherEditor;

	private String filterText;

	private HtmlInputText filterInputText;

	private HtmlDataTable dataTable;

	private boolean removeConfirmVisible;

	private Course removingCourse;
	
	private int selectedIndex = -1;

	@javax.annotation.PostConstruct
	public void init() {
		course = new Course();

		courseFilterator = new TextFilterator<Course>() {

			@Override
			public void getFilterStrings(List<String> baseList, Course course) {
				baseList.add(course.getCode());
				baseList.add(course.getName());
			}

		};
	}

	public List<Course> getCourses() {
		return courses;
	}

	public List<Course> getFilterList() {
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

	public Course getRemovingCourse() {
		return removingCourse;
	}

	public void searchActionListener(ActionEvent event) {
		CourseSCDO courseSCDO = new CourseSCDO();

		try {
			courseSCDO.setCode(course.getCode());
			courseSCDO.setName(course.getName());
			courses = serviceLocator.getCourseService().findCourse(courseSCDO);
			if (courses == null || courses.isEmpty()) {
				filterList = null;
				JSFUtils.addFacesInfoMessage("common.search.empty");
			} else {
				courseMatcherEditor = new TextMatcherEditor<Course>(courseFilterator);
				filterList = new FilterList<Course>(GlazedLists.eventList(courses), courseMatcherEditor);
				filterInputText.requestFocus();
			}
		} catch (Throwable e) {
			logger.error("Exception when finding course: " + courseSCDO, e);
			JSFUtils.addApplicationErrorMessage();
			return;
		}
	}

	public void filterActionListener(ActionEvent event) {
		filterList();
		if (filterList == null || filterList.isEmpty()) {
			JSFUtils.addFacesInfoMessage(filterInputText, "common.filter.empty");
		}
		filterInputText.requestFocus();
	}

	public void filterClearActionListener(ActionEvent event) {
		filterText = null;
		filterList();
		filterInputText.resetValue();
		filterInputText.requestFocus();
	}

	private void filterList() {
		courseMatcherEditor.setMode(TextMatcherEditor.CONTAINS);
		courseMatcherEditor.setFilterText((filterText == null) ? new String[] {} : new String[] { filterText });
	}

	public String editAction() {
		selectedIndex = dataTable.getRowIndex();
		Object object = dataTable.getRowData();
		if (object != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Going to edit: {}", object);
			}
			JSFUtils.storeOnSessionMap("course", object);
			return "modifyCourse";
		}
		return null;
	}

	public void removeConfirmActionListener(ActionEvent event) {
		removingCourse = (Course) dataTable.getRowData();
		removeConfirmVisible = removingCourse != null;
		if (removeConfirmVisible) {
			if (logger.isDebugEnabled()) {
				logger.debug("Going to remove: {}", removingCourse);
			}
		}
	}

	public void removeActionListener(ActionEvent event) {
		if (removingCourse == null) {
			return;
		}

		try {
			serviceLocator.getCourseService().deleteCourse(removingCourse);
			courses.remove(removingCourse);
			courseMatcherEditor = new TextMatcherEditor<Course>(courseFilterator);
			filterList = new FilterList<Course>(GlazedLists.eventList(courses), courseMatcherEditor);
			filterList();
			JSFUtils.addFacesInfoMessage("course.remove.successful");
		} catch (DataIntegrityViolationException e) {
			logger.error("Exception when deleting course: " + removingCourse, e);
			JSFUtils.addFacesErrorMessage("course.remove.integrityviolation.error", new Object[] {removingCourse.getCode()});
			return;
		} catch (Throwable e) {
			logger.error("Exception when deleting course: " + removingCourse, e);
			JSFUtils.addApplicationErrorMessage();
			return;
		} finally {
			removeConfirmVisible = false;
		}
	}

	public void closeRemoveActionListener(ActionEvent event) {
		removeConfirmVisible = false;
	}
	
	public String modifyBackAction() {
		Course course = (Course) JSFUtils.removeFromRequestMap("course");
		if (course != null && selectedIndex >= 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("Going back to search page. Setting modified object to index {}. Object: {}", selectedIndex, course);
			}
			filterList.set(selectedIndex, course);
		}
		// Remove the modify managed bean
		JSFUtils.removeFromRequestMap("modifyCourse");
		return "back";
	}
}
