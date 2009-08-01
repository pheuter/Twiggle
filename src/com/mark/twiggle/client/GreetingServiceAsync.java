package com.mark.twiggle.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync{
	void greetServer(String username, String password, AsyncCallback<String> callback);
}
