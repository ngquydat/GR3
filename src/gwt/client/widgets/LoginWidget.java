package gwt.client.widgets;

import gwt.client.Main;
import gwt.client.services.LoginService;
import gwt.client.services.LoginServiceAsync;
import gwt.shared.LoginInfo;
import gwt.shared.Person;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoginWidget extends Composite {

	private static LoginWidgetUiBinder uiBinder = GWT.create(LoginWidgetUiBinder.class);
	private final LoginServiceAsync loginService = GWT.create(LoginService.class); 
	@UiField Button loginButton;
	@UiField Button registerButton;
	@UiField TextBox username;
	@UiField Label statusLabel;
	@UiField PasswordTextBox password;
	MypageWidget mypageWidget;
	private LoginInfo loginInfo = null;

	private VerticalPanel loginPanel = new VerticalPanel();
	private Anchor signInLink = new Anchor("Sign In by Google Account");
	
	interface LoginWidgetUiBinder extends UiBinder<Widget, LoginWidget> {}

	public LoginWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		loginService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<LoginInfo>() {
					@Override
					public void onSuccess(LoginInfo result) {
						loginInfo = result;
						if (loginInfo.loggedIn) {
							mypageWidget = new MypageWidget(loginInfo, LoginWidget.this);
							RootPanel.get("abc").clear();
							RootPanel.get("abc").add(mypageWidget);
						} else {
							loadLogin();
						}
					}
					@Override
					public void onFailure(Throwable caught) {
					}
				});
		
		loginButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Person user = new Person();
				user.userName = username.getText();
				user.passWord = password.getText();
				loginService.login(user, new AsyncCallback<Person>() {
					
					@Override
					public void onSuccess(Person result) {
						if(result.id != null){
							LoginInfo loginInfo = new LoginInfo();
							loginInfo.email = result.email;
							loginInfo.nickname = result.nickName;
							loginInfo.username = result.userName;
							loginInfo.password = result.passWord;
							loginInfo.userid = result.id;
							RootPanel.get("abc").clear();
							mypageWidget = new MypageWidget(loginInfo, LoginWidget.this);
							RootPanel.get("abc").clear();
							RootPanel.get("abc").add(mypageWidget);
						}else {
							statusLabel.setText("Failed");
						}
						
					}
					
					@Override
					public void onFailure(Throwable caught) {
						
					}
				});
			}
		});
		
		registerButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				loadRegister();
			}
		});
	}
	
	private void loadLogin() {
		// Assemble login panel.
		signInLink.setStyleName("btn btn-primary");
		signInLink.setHref(loginInfo.loginUrl);
		
		loginPanel.add(signInLink);
		RootPanel.get("abc").add(loginPanel);
	}
	public void loadRegister(){
		RegisterWidget register = new RegisterWidget();
		RootPanel.get("abc").clear();
		RootPanel.get("abc").add(register);
	}

}
