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


package quizfun.model.exception;

/**
 * @author Hiranya Mudunkotuwa
 */
public class ModuleNotFoundException  extends Exception {

	private static final long serialVersionUID = 8096354142771380643L;

	public ModuleNotFoundException() {
	}

	public ModuleNotFoundException(String message) {
		super(message);
	}

	public ModuleNotFoundException(Throwable cause) {
		super(cause);
	}

	public ModuleNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
