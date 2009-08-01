package com.mark.twiggle.server;

import twitter4j.*;
import com.mark.twiggle.client.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	public String greetServer(String username, String password) {
		String followers = "";
		int count = 0;
		Twitter twitter = new Twitter(username, password);
		try {
			for (User u : twitter.getFollowers()) 
			{				
				if (count == twitter.getFollowers().size()-1)
				{
					followers+=u.getName(); 
					break;
				}
				followers+=u.getName()+", ";
				count++;
			}
			return "-----------------------" +
			"</br></br>Welcome, <b>"+twitter.verifyCredentials().getName()+"</b>" +
			"<br/><br/>"+
			"<b>Status:</b> <i>"+twitter.verifyCredentials().getStatusText()+"</i>" +
			"<br/><br/>" +
			"<b>Location:</b> <i>"+twitter.verifyCredentials().getLocation()+"</i>" +
			"<br/><br/>" +
			"<b>Followers:</b> <i>"+followers+"</i>" +
			"<br/><br/><h6>much more coming soon</h6>";
		} catch (TwitterException e) {
			return "<br/><b>Oops:</b><br/> "+e;
		}
			
	}
	
}