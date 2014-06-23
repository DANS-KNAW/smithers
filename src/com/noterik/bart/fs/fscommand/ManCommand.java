package com.noterik.bart.fs.fscommand;

import java.util.Properties;

import com.noterik.springfield.tools.PropertiesHelper;
import com.noterik.springfield.tools.fs.FSXMLBuilder;

/**
 * Display the manual pages
 * 
 * SYNTAX
 * 
 * man [options ... ] -name NAME
 * 
 * OPTIONS
 * 
 * -name NAME
 * 		Displays the manual pages, where NAME specifies the name of the command.
 * 
 * @author Derk Crezee <d.crezee@noterik.nl>
 * @copyright Copyright: Noterik B.V. 2010
 * @package com.noterik.bart.fs.fscommand
 * @access private
 *
 */
public class ManCommand extends CommandAdapter {

	public String execute(String uri, String xml) {
		// get input parameters
		Properties params = getInputParameters(xml);
		String name = PropertiesHelper.getString(params, "name", null);
		
		// parameter check
		if(name == null) {
			return FSXMLBuilder.getErrorMessage("400", "Bad Request", "Parameters missing", "");
		}
		
		// get description
		ManualEntry entry = null;
		Command cmd = CommandHandler.instance().getCommand(name);
		if(cmd != null) {
			entry = cmd.man();
		}
		
		// check if there is a manual entry
		if(entry == null) {
			return FSXMLBuilder.getErrorMessage("404", "Not Found", "No manual entry exists for command "+name, "");
		}
		
		// convert entry to description 	
		String description = entry2str(entry);
		return FSXMLBuilder.getStatusMessage("OK", description, "");
	}
	
	/**
	 * Converts a manual entry to xml
	 * 
	 * @param entry
	 * @return
	 */
	private String entry2str(ManualEntry entry) {
		StringBuffer buff = new StringBuffer();
		buff.append(entry.getDescription()).append("\n");
		buff.append("\n");
		buff.append("SYNTAX").append("\n");
		buff.append(entry.getSyntax()).append("\n");
		buff.append("\n");
		for(ManualEntry.Option option : entry.getOptions()) {
			buff.append("-").append(option.getId());
			if(option.getAlt()!=null) {
				buff.append(", --").append(option.getAlt());
			}
			buff.append("\n");
			buff.append(option.getDescription()).append("\n");
			buff.append("\n");
		}
		return buff.toString();
	}

	/**
	 * Returns the manual entry for this command
	 */
	public ManualEntry man() {
		ManualEntry entry = new ManualEntry();
		entry.setDescription("Display the manual pages");
		entry.setSyntax("man [options ... ] -name NAME");
		entry.addOption("name", "Displays the manual pages, where NAME specifies the name of the command.");
		return entry;
	}
}
