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

package quizfun.model.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import quizfun.model.dao.UserDao;
import quizfun.model.dto.UserSCDO;
import quizfun.model.entity.User;
import quizfun.model.exception.DuplicateUserException;
import quizfun.model.exception.UserNotFoundException;

/**
 * @author M. Isuru Tharanga Chrishantha Perera
 */
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = { UserNotFoundException.class })
	public User findUser(String username) throws UserNotFoundException {
		return userDao.findUser(username);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = { DuplicateUserException.class })
	public void saveUser(User user) throws DuplicateUserException {
		userDao.saveUser(user);
	}
	
	@Override
	public List<User> findUser(UserSCDO userSCDO) {
		return userDao.findUser(userSCDO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public User updateUser(User user) {
		return userDao.updateUser(user);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteUser(User user) {
		userDao.deleteUser(user);
	}

}
