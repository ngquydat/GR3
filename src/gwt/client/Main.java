package gwt.client;

import gwt.client.widgets.LoginWidget;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class Main implements EntryPoint {
	public void onModuleLoad() {
		LoginWidget login = new LoginWidget();
		RootPanel.get("abc").add(login);
	}
}
