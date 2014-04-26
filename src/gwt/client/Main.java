package gwt.client;

import gwt.client.services.LoginService;
import gwt.client.services.LoginServiceAsync;
import gwt.client.widgets.MypageWidget;
import gwt.shared.LoginInfo;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Main implements EntryPoint {
	LoginServiceAsync loginService = GWT.create(LoginService.class);

	private LoginInfo loginInfo = null;

	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Sign in to create ebook!");
	private Anchor signInLink = new Anchor("Sign In");
	
	MypageWidget mypageWidget;
	
	public void onModuleLoad() {
		loginService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<LoginInfo>() {
					@Override
					public void onSuccess(LoginInfo result) {
						loginInfo = result;
						if (loginInfo.loggedIn) {
							mypageWidget = new MypageWidget(loginInfo, Main.this);
							RootPanel.get("abc").add(mypageWidget);
						} else {
							loadLogin();
						}
					}
					@Override
					public void onFailure(Throwable caught) {
					}
				});
	}
	private void loadLogin() {
		// Assemble login panel.
		signInLink.setStyleName("btn btn-primary");
		signInLink.setHref(loginInfo.loginUrl);
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		RootPanel.get("abc").add(loginPanel);
	}
}
