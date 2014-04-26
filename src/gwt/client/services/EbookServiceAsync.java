package gwt.client.services;

import gwt.shared.Ebook;
import gwt.shared.LoginInfo;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EbookServiceAsync {

	void createEbook(LoginInfo loginInfo, String Title, String WebURL, AsyncCallback<Ebook> callback);
	void createEbook(LoginInfo loginInfo, String Title,  List<String> listChapters, List<String> listChapterContents, AsyncCallback<Ebook> callback);

	void getAll(String username, AsyncCallback<List<Ebook>> callback);

	void delete(Long ebookid, AsyncCallback<Void> callback);

	

}
