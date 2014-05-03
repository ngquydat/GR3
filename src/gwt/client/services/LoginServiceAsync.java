package gwt.client.services;

import gwt.shared.LoginInfo;
import gwt.shared.Person;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
  public void login(String requestUri, AsyncCallback<LoginInfo> async);
  void register(Person user, AsyncCallback<Boolean> callback);
	void login(Person user, AsyncCallback<Person> callback);
	void logout(long userid, AsyncCallback<Boolean> callback);
	void getUser(long userid, AsyncCallback<Person> callback);
	void delUser(long userid, AsyncCallback<Boolean> callback);
	void getAll(AsyncCallback<List<Person>> callback);
}