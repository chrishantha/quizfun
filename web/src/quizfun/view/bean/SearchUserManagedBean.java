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
import quizfun.model.dto.UserSCDO;
import quizfun.model.entity.Course;
import quizfun.model.entity.User;
import quizfun.view.util.ICEfacesUtils;
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
public class SearchUserManagedBean extends UserManagedBean {

	final Logger logger = LoggerFactory.getLogger(SearchUserManagedBean.class);

	private List<User> users;

	private List<User> filterList;

	private TextFilterator<User> userFilterator;

	private TextMatcherEditor<User> userMatcherEditor;

	private String filterText;

	private HtmlInputText filterInputText;

	private HtmlDataTable dataTable;

	private boolean removeConfirmVisible;

	private User removingUser;
	
	private HtmlInputText courseCodeInputText;
	private HtmlInputText courseNameInputText;
	
	private int selectedIndex = -1;

	@javax.annotation.PostConstruct
	public void init() {
		user = new User();
		course = new Course();

		userFilterator = new TextFilterator<User>() {

			@Override
			public void getFilterStrings(List<String> baseList, User user) {
				baseList.add(user.getUsername());
				Course course = user.getCourse();
				if (course != null) {
					baseList.add(user.getCourse().getCode());
					baseList.add(user.getCourse().getName());
				}
			}

		};
	}

	public List<User> getUsers() {
		return users;
	}

	public List<User> getFilterList() {
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

	public User getRemovingUser() {
		return removingUser;
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
		UserSCDO userSCDO = new UserSCDO();
		CourseSCDO courseSCDO = new CourseSCDO();

		try {
			courseSCDO.setCode(course.getCode());
			courseSCDO.setName(course.getName());
			userSCDO.setCourseSCDO(courseSCDO);
			userSCDO.setUsername(user.getUsername());
			users = serviceLocator.getUserService().findUser(userSCDO);
			if (users == null || users.isEmpty()) {
				filterList = null;
				JSFUtils.addFacesInfoMessage("common.search.empty");
			} else {
				userMatcherEditor = new TextMatcherEditor<User>(userFilterator);
				filterList = new FilterList<User>(GlazedLists.eventList(users), userMatcherEditor);
				ICEfacesUtils.setFocus(filterInputText);
			}
		} catch (Throwable e) {
			logger.error("Exception when finding user: " + userSCDO, e);
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
		userMatcherEditor.setMode(TextMatcherEditor.CONTAINS);
		userMatcherEditor.setFilterText((filterText == null) ? new String[] {} : new String[] { filterText });
	}

	public String editAction() {
		selectedIndex = dataTable.getRowIndex();
		Object object = dataTable.getRowData();
		if (object != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Going to edit: {}", object);
			}
			JSFUtils.storeOnSessionMap("user", object);
			return "modifyUser";
		}
		return null;
	}

	public void removeConfirmActionListener(ActionEvent event) {
		removingUser = (User) dataTable.getRowData();
		removeConfirmVisible = removingUser != null;
		if (removeConfirmVisible) {
			if (logger.isDebugEnabled()) {
				logger.debug("Going to remove: {}", removingUser);
			}
		}
	}

	public void removeActionListener(ActionEvent event) {
		if (removingUser == null) {
			return;
		}

		try {
			serviceLocator.getUserService().deleteUser(removingUser);
			users.remove(removingUser);
			userMatcherEditor = new TextMatcherEditor<User>(userFilterator);
			filterList = new FilterList<User>(GlazedLists.eventList(users), userMatcherEditor);
			filterList();
			JSFUtils.addFacesInfoMessage("user.remove.successful");
		} catch (DataIntegrityViolationException e) {
			logger.error("Exception when deleting user: " + removingUser, e);
			JSFUtils.addFacesErrorMessage("user.remove.integrityviolation.error", new Object[] {removingUser.getUsername()});
			return;
		} catch (Throwable e) {
			logger.error("Exception when deleting user: " + removingUser, e);
			JSFUtils.addApplicationErrorMessage();
			return;
		} finally {
			removeConfirmVisible = false;
		}
	}

	public void closeRemoveActionListener(ActionEvent event) {
		removeConfirmVisible = false;
	}
	
	protected void clearValues() {
		user.setUsername(null);
		course.setCode(null);
		course.setName(null);
	}
	
	protected void resetComponents() {
		userNameInputText.resetValue();
		courseCodeInputText.resetValue();
		courseNameInputText.resetValue();
	}
	
	public String modifyBackAction() {
		User user = (User) JSFUtils.removeFromRequestMap("user");
		if (user != null && selectedIndex >= 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("Going back to search page. Setting modified object to index {}. Object: {}", selectedIndex, user);
			}
			filterList.set(selectedIndex, user);
		}
		// Remove the modify managed bean
		JSFUtils.removeFromRequestMap("modifyUser");
		return "back";
	}
}
