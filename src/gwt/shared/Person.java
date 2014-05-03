package gwt.shared;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Person implements Serializable{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	public Long id;

	@Persistent public boolean loggedIn = false;
	@Persistent public String loginUrl;
	@Persistent public String logoutUrl;
	@Persistent public String userName;
	@Persistent public String passWord;
	@Persistent public String email;
	@Persistent public String nickName;

	public Person(Person user) {
		this.loggedIn = user.loggedIn;
		this.loginUrl = user.loginUrl;
		this.logoutUrl = user.logoutUrl;
		this.userName = user.userName;
		this.passWord = user.passWord;
		this.email = user.email;
		this.nickName = user.nickName;
		this.id = user.id;
	}
	
	public Person() {}
	
}
