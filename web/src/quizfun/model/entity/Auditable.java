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

package quizfun.model.entity;

import java.util.Date;

/**
 * <p>
 * Use this interface to mark an entity as Auditable
 * </p>
 * <p>
 * The DAO implementation will set the values during the life cycle of the
 * entity
 * </p>
 * 
 * @author Hiranya Mudunkotuwa
 */
public interface Auditable {

	String getCreatedBy();

	void setCreatedBy(String createdBy);

	Date getCreatedDate();

	void setCreatedDate(Date createdDate);

	String getModifiedBy();

	void setModifiedBy(String modifiedBy);

	Date getModifiedDate();

	void setModifiedDate(Date modifiedDate);
}
