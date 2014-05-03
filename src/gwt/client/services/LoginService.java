package gwt.client.services;

import gwt.shared.LoginInfo;
import gwt.shared.Person;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	public LoginInfo login(String requestUri);

	Boolean register(Person user);

	Person login(Person User);

	Boolean logout(long userid);

	Person getUser(long userid);

	Boolean delUser(long userid);

	List<Person> getAll();
}