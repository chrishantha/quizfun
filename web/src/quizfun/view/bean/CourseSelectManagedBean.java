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
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quizfun.model.entity.Course;
import quizfun.view.servicelocator.ServiceLocator;
import quizfun.view.util.JSFUtils;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.matchers.TextMatcherEditor;

import com.icesoft.faces.component.ext.HtmlInputText;
import com.icesoft.faces.component.selectinputtext.SelectInputText;

/**
 * @author M. Isuru Tharanga Chrishantha Perera
 */
public abstract class CourseSelectManagedBean {
	
	final Logger logger = LoggerFactory.getLogger(CourseSelectManagedBean.class);

	protected ServiceLocator serviceLocator;
	
	protected Course course;
	
	protected List<SelectItem> courseSelectItemList;
	
	protected SelectInputText courseSelectInputText;
	
	protected String courseCode;
	
	protected TextMatcherEditor<SelectItem> courseMatcherEditor;
	
	protected String selectedCourse;
	
	protected HtmlInputText selectedCourseInputText;

	public void setServiceLocator(ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	public SelectInputText getCourseSelectInputText() {
		return courseSelectInputText;
	}

	public void setCourseSelectInputText(SelectInputText courseSelectInputText) {
		this.courseSelectInputText = courseSelectInputText;
	}

	public Course getCourse() {
		return course;
	}

	public List<SelectItem> getCourseSelectItemList() {
		return courseSelectItemList;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getSelectedCourse() {
		return selectedCourse;
	}

	public void setSelectedCourse(String selectedCourse) {
		this.selectedCourse = selectedCourse;
	}

	public HtmlInputText getSelectedCourseInputText() {
		return selectedCourseInputText;
	}

	public void setSelectedCourseInputText(HtmlInputText selectedCourseInputText) {
		this.selectedCourseInputText = selectedCourseInputText;
	}

	protected void initializeCourseSelectInput() {
		TextFilterator<SelectItem> selectItemFilterator = new TextFilterator<SelectItem>() {
			@Override
			public void getFilterStrings(List<String> baseList,
					SelectItem element) {
				baseList.add(element.getLabel());
			}
		};
		
		List<Course> list = serviceLocator.getCourseService().findAll();
		
		if (list != null && !list.isEmpty()) {
			courseSelectItemList = new ArrayList<SelectItem>(list.size());
			
			for (Course course : list) {
				courseSelectItemList.add(new SelectItem(course, course.getCode()));
			}
			
			courseMatcherEditor = new TextMatcherEditor<SelectItem>(selectItemFilterator);
			courseSelectItemList = new FilterList<SelectItem>(GlazedLists.eventList(courseSelectItemList), courseMatcherEditor);
		}
	}
	
	public void courseValueChangeListener(ValueChangeEvent event) {
		SelectInputText selectInputText = (SelectInputText) event.getComponent();
		String value = (String) event.getNewValue();

		if (logger.isDebugEnabled()) {
			logger.debug("Course value change listener. Value: " + value);
		}

		courseMatcherEditor.setMode(TextMatcherEditor.STARTS_WITH);
		courseMatcherEditor.setFilterText((value == null) ? new String[] {} : new String[] { value });

		SelectItem selectedItem = selectInputText.getSelectedItem();
		if (selectedItem != null) {
			Course course = (Course) selectedItem.getValue();
			this.course = course;			
			selectedCourse = JSFUtils.getStringFromBundle("selectcourse.selectedcourse.display.pattern", new Object[] {
					course.getCode(), course.getName() });
		} else {
			course = null;
			selectedCourse = null;
		}
		selectedCourseInputText.resetValue();
	}
	
	public void clearCourseActionListener(ActionEvent event) {
		course = null;
		courseCode = null;
		selectedCourse = null;
		
		courseSelectInputText.resetValue();
		selectedCourseInputText.resetValue();
		
		courseMatcherEditor.setFilterText(new String[] {});
		
		courseSelectInputText.requestFocus();
	}
}
