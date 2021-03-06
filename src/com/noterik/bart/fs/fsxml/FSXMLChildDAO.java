/* 
* FSXMLChildDAO.java
* 
* Copyright (c) 2012 Noterik B.V.
* 
* This file is part of smithers, related to the Noterik Springfield project.
*
* Smithers is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Smithers is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Smithers.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.noterik.bart.fs.fsxml;

import java.util.List;

import com.noterik.bart.fs.fscommand.dao.GenericDAO;

/**
 * FSXMLChild DAO interface.
 * 
 * @author Derk Crezee <d.crezee@noterik.nl>
 * @copyright Copyright: Noterik B.V. 2009
 * @package com.noterik.bart.fs.fsxml
 * @access private
 *
 */
public interface FSXMLChildDAO extends GenericDAO<FSXMLChild, FSXMLChildKey> {
	/**
	 * Returns a list of all children of the given parent uri
	 * 
	 * @param uri	parent uri
	 * @return 		a list of all children of the given parent uri
	 */
	public List<FSXMLChild> getChildren(String uri);
	
	/**
	 * Returns a list of all children of the given parent uri, and of given child type
	 * 
	 * @param uri	parent uri
	 * @param type	child type
	 * @return 		a list of all children of the given parent uri, and of given child type
	 */
	public List<FSXMLChild> getChildrenByType(String uri, String type);
	
	/**
	 * Returns the amount children of the given parent uri, and of given child type
	 * 
	 * @param uri	parent uri
	 * @param type	child type
	 * @return 		the amount children of the given parent uri, and of given child type
	 */
	public int getChildrenByTypeCount(String uri, String type);
}
