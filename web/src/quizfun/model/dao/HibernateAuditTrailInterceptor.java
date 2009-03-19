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

package quizfun.model.dao;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import quizfun.model.entity.Auditable;
import quizfun.model.util.SpringSecurityUtil;

/**
 * @author M. Isuru Tharanga Chrishantha Perera
 */
public class HibernateAuditTrailInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 7715286660000990617L;

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		boolean modified = false;
		if (entity instanceof Auditable) {
			for (int i = 0; i < propertyNames.length; i++) {
				if ("modifiedDate".equals(propertyNames[i])) {
					currentState[i] = new Date();
					modified = true;
				} else if ("modifiedBy".equals(propertyNames[i])) {
					currentState[i] = SpringSecurityUtil.getUsername();
					modified = true;
				}
			}
		}
		return modified;
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		boolean modified = false;
		if (entity instanceof Auditable) {
			for (int i = 0; i < propertyNames.length; i++) {
				if ("createdDate".equals(propertyNames[i])) {
					state[i] = new Date();
					modified = true;
				} else if ("createdBy".equals(propertyNames[i])) {
					state[i] = SpringSecurityUtil.getUsername();
					modified = true;
				}
			}
		}
		return modified;
	}
}