package gwt.client.widgets;

import gwt.client.Main;
import gwt.client.services.LoginService;
import gwt.client.services.LoginServiceAsync;
import gwt.shared.Person;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PasswordTextBox;

public class RegisterWidget extends Composite {

	private static RegisterWidgetUiBinder uiBinder = GWT.create(RegisterWidgetUiBinder.class);
	private final LoginServiceAsync loginService = GWT.create(LoginService.class);
	@UiField TextBox nickname;
	@UiField TextBox email;
	@UiField TextBox username;
	@UiField Label statusLabel;
	@UiField Button registerButton;
	@UiField Button backButton;
	@UiField PasswordTextBox password;

	interface RegisterWidgetUiBinder extends UiBinder<Widget, RegisterWidget> {}

	public RegisterWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		
		registerButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Person user = new Person();
				user.userName = username.getText();
				user.passWord = password.getText();
				user.email = email.getText();
				user.nickName = nickname.getText();
				loginService.register(user, new AsyncCallback<Boolean>() {
					
					@Override
					public void onSuccess(Boolean result) {
						// TODO Auto-generated method stub
						if(result) statusLabel.setText("Successfully!");
						else statusLabel.setText("Failed");
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
		
		backButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				loadLogin();
			}
		});
	}
	
	public void loadLogin(){
		LoginWidget login = new LoginWidget();
		RootPanel.get("abc").remove(RegisterWidget.this);
		RootPanel.get("abc").add(login);
	}
	

}
