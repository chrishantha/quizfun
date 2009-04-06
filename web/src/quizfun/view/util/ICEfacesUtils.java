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

package quizfun.view.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.icesoft.faces.context.effects.JavascriptContext;

/**
 * @author M. Isuru Tharanga Chrishantha Perera
 */
public class ICEfacesUtils {

	public static void setFocus(UIComponent component) {
		setFocus(component.getClientId(FacesContext.getCurrentInstance()));
	}

	public static void setFocus(String clientId) {
		StringBuilder script = new StringBuilder();
		script.append("setFocusToComponent('").append(clientId).append("');");

		JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), script.toString());
		JavascriptContext.applicationFocus(FacesContext.getCurrentInstance(), clientId);
	}
}
