package gwt.server;

import gwt.client.services.LoginService;
import gwt.shared.LoginInfo;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

	public LoginInfo login(String requestUri) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		LoginInfo loginInfo = new LoginInfo();

		if (user != null) {
			loginInfo.loggedIn = true;
			loginInfo.email = user.getEmail();
			loginInfo.nickname = user.getNickname();
			loginInfo.logoutUrl = userService.createLogoutURL(requestUri);
			loginInfo.id = user.getUserId();
		} else {
			loginInfo.loggedIn = false;
			loginInfo.loginUrl = userService.createLoginURL(requestUri);
		}
		return loginInfo;
	}

}