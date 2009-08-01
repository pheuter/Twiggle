package com.mark.twiggle.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Twiggle implements EntryPoint {

	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Button sendButton = new Button("get profile");
		final TextBox usernameField = new TextBox();
		final PasswordTextBox passwordField = new PasswordTextBox();
		usernameField.setTitle("username");
		passwordField.setTitle("password");

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(usernameField);
		RootPanel.get("nameFieldContainer").add(passwordField);
		RootPanel.get("sendButtonContainer").add(sendButton);

		// Focus the cursor on the name field when the app loads
		usernameField.setFocus(true);
		usernameField.selectAll();

		// Create the popup dialog box
		final DialogBox profileBox = new DialogBox();
		profileBox.setText("profile");
		profileBox.setAnimationEnabled(true);
		final Button closeButton = new Button("close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		profileBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				profileBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				loginServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					loginServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void loginServer() {
				sendButton.setEnabled(false);
				String username = usernameField.getText();
				String password = passwordField.getText();
				textToServerLabel.setText(username);
				serverResponseLabel.setText("");
				greetingService.greetServer(username, password,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								profileBox
										.setText("profile fetch failed");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								profileBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(String result) {
								profileBox.setText("profile");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								profileBox.center();
								closeButton.setFocus(true);
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		usernameField.addKeyUpHandler(handler);
		passwordField.addKeyUpHandler(handler);
	}
}
