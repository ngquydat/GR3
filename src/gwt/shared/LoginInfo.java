package gwt.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LoginInfo implements Serializable {

	public boolean loggedIn;
	public String loginUrl;
	public String logoutUrl;
	public String email;
	public String nickname;
	public String id;

	public LoginInfo(){
		this.loggedIn = false;
	}
}