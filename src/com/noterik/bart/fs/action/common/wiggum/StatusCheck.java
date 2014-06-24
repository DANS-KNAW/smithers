package com.noterik.bart.fs.action.common.wiggum;

import javax.xml.transform.dom.DOMSource;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.noterik.bart.fs.action.ActionAdapter;
import com.noterik.bart.fs.action.common.ingest.IngestAudioAction;
import com.noterik.bart.fs.fsxml.FSXMLRequestHandler;
import com.noterik.springfield.tools.fs.URIParser;

public class StatusCheck extends ActionAdapter{
	
	private static Logger logger = Logger.getLogger(IngestAudioAction.class);
	
	@Override
	public String run() {
		logger.debug("New failed upload detected");
		String requestBody = event.getRequestData();
		logger.debug("XML: " + requestBody);
		String uri = event.getUri();
		logger.debug("URI" + uri);
		Document doc = null;
		Document doc1 = null;
		String response = null;
		String newUri = "/domain/springfieldwebtv/tmp/wiggum";
		String[] fields = uri.split("/");
		String date = fields[fields.length-1];
		System.out.println("GOT REQUEST\n"+ requestBody+"\n"+uri);
		try {
			if (requestBody != null) {
				doc = DocumentHelper.parseText(requestBody);
				Node status = doc.selectSingleNode("//properties/status");
				
				
				if (status != null && status.getText().equals("done")) {
					
						
						doc1 = DocumentHelper.createDocument();
						Element fsxml = DocumentHelper.createElement("fsxml");
						Element properties = DocumentHelper.createElement("properties");
						fsxml.add(properties);
						Element failure = DocumentHelper.createElement("failure");
						fsxml.add(failure);
						Element failproperties = DocumentHelper.createElement("properties");
						failure.add(failproperties);
						Element datestat = DocumentHelper.createElement("date");
						datestat.setText(date);
						failproperties.add(datestat);
						
						String msg = doc.selectSingleNode("//properties/message").getText();
						Element newmessage = DocumentHelper.createElement("message");
						newmessage.setText(msg);
						failproperties.add(newmessage);
						
						Element tmpfilename = DocumentHelper.createElement("tmpfilename");
						String temp = doc.selectSingleNode("//properties/tmpfilename").getText();
						tmpfilename.setText(temp);
						failproperties.add(tmpfilename);
						
						doc1.add(fsxml);
						
						String xml = doc1.asXML().split("\\?>")[1];	
						
						System.out.println("\n"+xml+"\n");
	
						
						System.out.println(FSXMLRequestHandler.instance().handlePUT(newUri+"/properties",xml));
					
				}
			}
			
		} catch (DocumentException e) {
			logger.error("",e);
		}
		return response;
	}
	
	

}
