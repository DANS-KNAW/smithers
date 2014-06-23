package com.noterik.bart.fs.fscommand.dynamic.playlist.generators;

import java.util.*;
import org.apache.log4j.Logger;
import org.dom4j.Element;

import com.noterik.bart.fs.fscommand.dynamic.config.flash;
import com.noterik.bart.fs.fscommand.dynamic.playlist.GeneratorInterface;
import org.dom4j.*;

/**
 * 
 * @author Daniel Ockeloen
 * 
 * Create a videoplaylist based on the selected layer, this allows people to
 * select part of the video by just placing selected blocks on the timeline
 * all events are shifted based on that (not implemented yet).
 *
 */
public class SelectedLayer implements GeneratorInterface {

	/** Logger */
	private static Logger logger = Logger.getLogger(flash.class);
	
	/*
	 * the incoming generate call from either quickpresentation or direct restlet
	 * 
	 * @see com.noterik.bart.fs.fscommand.dynamic.playlist.GeneratorInterface#generate(org.dom4j.Node, java.lang.String)
	 */
	public Element generate(Element pr,String wantedplaylist,Element params,Element domainvpconfig,Element fsxml) {
	//	System.out.println("selected called");
		
		// select the original playlist (now hardcoded to playlist with id 1).
		Element videonode = (Element) pr.selectSingleNode("videoplaylist/video[@id='1']");
		
		if (videonode!=null) {
			// if its a videonode than detach it from the document (delete it) since we are
			// doing a rewrite on the document not generating a new one
			videonode.detach(); // remove it from the presentation
			
			// we need the referid, so we can set this in all the new video's, This is
			// now limited to one video as imput and needs a rewrite to support more input 
			// video's in a playlist.
			String referid = videonode.attributeValue("referid");
			
			// select the selected layer.
			List<Node> vpnodes = pr.selectNodes("videoplaylist/selected");		

			// ok lets loop all the blocks and create video nodes based on them
			int idcounter = 1; // we need to number the new video nodes
			for (Iterator<Node> i = vpnodes.iterator();i.hasNext();) {
				Node block = i.next();
				
				// get the starttime and duration of the block as a base for the new
				// video's start/duration
				String starttime = block.selectSingleNode("properties/starttime").getText();
				String duration = block.selectSingleNode("properties/duration").getText();
		
				// create the new video node
				Element newvideonode = DocumentHelper.createElement("video");
				
				// set the id and aim it to our original video
				newvideonode.addAttribute("id", ""+idcounter++);
				newvideonode.addAttribute("referid", referid);
				
				// create the properties and set them (this can be done easer?)
				Element p = DocumentHelper.createElement("properties");
				Element st = DocumentHelper.createElement("starttime");
				Element du = DocumentHelper.createElement("duration");
				st.setText(starttime);
				du.setText(duration);
				p.add(st);
				p.add(du);
				
				// add the properties to the video node so it plays just that part.
				newvideonode.add(p);
				
				// we deleted (detached) the old video nodes so lets now add the
				// new video's we created to the original document.
				((Element)pr.selectSingleNode("videoplaylist")).add(newvideonode);
			}
			return pr; // return it, not really needed its changed in place but..
		} else {
			// if no video node is found something is wrong so error.
			logger.error("No video 1 node found in playlist");
		}
		
		// always return the presentation even if no hits found (empty video list now)
		return pr;
	}
	
}
