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

import quizfun.model.dao.ModuleDao;
import quizfun.model.dto.ModuleSCDO;
import quizfun.model.entity.Module;
import quizfun.model.exception.DuplicateModuleException;
import quizfun.model.exception.ModuleNotFoundException;

@Transactional(readOnly = true)
public class ModuleServiceImpl implements ModuleService {

	private ModuleDao moduleDao;

	public void setModuleDao(ModuleDao moduleDao) {
		this.moduleDao = moduleDao;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = { DuplicateModuleException.class })
	public void saveModule(Module module) throws DuplicateModuleException {
		moduleDao.saveModule(module);
	}
	
	@Override
	public List<Module> findModule(ModuleSCDO moduleSCDO) {
		return moduleDao.findModule(moduleSCDO);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Module updateModule(Module module) {
		return moduleDao.updateModule(module);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteModule(Module module) {
		moduleDao.deleteModule(module);
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public List<Module> findAllModules(){
		return moduleDao.findAllModules();
	}	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public Module findModuleByCode(String code) throws ModuleNotFoundException{
		Module module = new Module();
		module = moduleDao.findModuleByCode(code);
		if(module == null) {
			throw new ModuleNotFoundException();
		}
		
		return module;
	}
}
