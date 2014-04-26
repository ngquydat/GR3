package gwt.server;

import gwt.client.services.EbookService;
import gwt.shared.Ebook;
import gwt.shared.LoginInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

@SuppressWarnings({ "serial", "deprecation" })
public class EbookServiceImpl extends RemoteServiceServlet implements EbookService {
	List<String> listChapters = new ArrayList<String>();
    List<String> listChapterContents = new ArrayList<String>();
    
    static {
		ObjectifyService.register(Ebook.class);
	}
    private Objectify ofy;
    
    
    
	public Ebook createEbook(LoginInfo loginInfo, String Title, String WebURL) {
		listChapters.clear();
		listChapterContents.clear();

		CountWebPage(WebURL);
		FilterContent(WebURL);
		System.out.println(listChapterContents);
		return createEbook(loginInfo, Title, listChapters, listChapterContents);
		
	}

	public Ebook createEbook(LoginInfo loginInfo, String Title,List<String> listChapters, List<String> listChapterContents) {

		FileService fileService = FileServiceFactory.getFileService();
		AppEngineFile file = null;
		try {
			file = fileService.createNewBlobFile("application/epub+zip", Title+ ".epub");
			boolean lock = true;
			FileWriteChannel writeChannel = fileService.openWriteChannel(file,lock);

			OutputStream out = Channels.newOutputStream(writeChannel);
			
			new Epub(Title, listChapters, listChapterContents, out);
			
			out.close();

			// Now finalize
			writeChannel.closeFinally();
			// Now read from the file using the Blobstore API
			BlobKey blobKey = fileService.getBlobKey(file);
			BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
			

			Ebook ebook = new Ebook();
			ebook.name = blobInfo.getFilename().substring(0, blobInfo.getFilename().length()-5);
			System.out.println(ebook.name);
			ebook.blobkey = blobInfo.getBlobKey().getKeyString();
			ebook.listChapters = listChapters;
			System.out.println(listChapters);
			ebook.listChapterContents = listChapterContents;
			ebook.owner = loginInfo.email;
			ofy = ObjectifyService.begin();
			ofy.put(ebook);

			return ebook;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	public boolean checkExistsChapter(String chaptername){
		for(int i=0;i<listChapters.size();i++){
			if(chaptername.indexOf(listChapters.get(i)) != -1){
				return true;
			}
		}
		return false;
	}
	
	public List<Ebook> getAll(String username) {
		ofy = ObjectifyService.begin();
		List<Ebook> listEbooks = ofy.query(Ebook.class).filter("owner", username).order("created").list();
		return listEbooks;
	}
	
	public void delete(Long ebookid) {
		ofy = ObjectifyService.begin();
		Ebook ebook = ofy.query(Ebook.class).filter("id", ebookid).get();
		if(ebook != null){
			ofy.delete(ebook);
			BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
			blobstoreService.delete(new BlobKey(ebook.blobkey));
		}
			
			
	}
	public void CountWebPage(String WebURL){
    	URL tangthuvien;
		int pages= 0;
		try {
			tangthuvien = new URL(WebURL);
			URLConnection ttv = tangthuvien.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(ttv.getInputStream(), "UTF8"));
			String inputLine;
			
			while ((inputLine = in.readLine()) != null) {
				
				if(inputLine.toLowerCase().contains("t=70735&amp;page=".toLowerCase()) && inputLine.toLowerCase().contains("title=\"Trang Cuối".toLowerCase())) {
					pages = Integer.parseInt(inputLine.substring(inputLine.indexOf("t=70735&amp;page=")+17, inputLine.indexOf("title=\"Trang Cuối")-2));
				}
				
			}
			System.out.println(pages);
			in.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
	public void FilterContent(String WebURL){
		System.out.println(WebURL);
    	URL tangthuvien;
    	try {
			String url ="";
			for(int j=1;j<=3;j++){
				if(j==1)
					url = WebURL;
				else 
					url = WebURL+"&page="+j;
				tangthuvien = new URL(url);
				System.out.println(url);
				URLConnection ttv = tangthuvien.openConnection();
				ttv.setConnectTimeout(600000);
				ttv.setReadTimeout(600000);
		        BufferedReader in = new BufferedReader(new InputStreamReader(ttv.getInputStream(),"UTF8"));
		       
		        String tmp = "";
		        String inputLine;
				while ((inputLine = in.readLine()) != null) {
					//start filter list Chapters
					inputLine = inputLine.replaceAll("CHƯƠNG", "Chương");
					if (inputLine.indexOf("Chương ") != -1 ){
//						System.out.println(inputLine);
						int StartIndex = inputLine.indexOf("Chương");
						String substring = inputLine.substring(inputLine.indexOf("Chương")+7);
						substring = substring.replaceAll(":", " ").replaceAll("\"", " ").replaceAll("\'", " ");
						String chap = substring.substring(0, substring.indexOf(" "));
						Pattern pattern = Pattern.compile(".*\\D.*");
						if (!chap.equalsIgnoreCase("") && !pattern.matcher(chap).matches()) {
							if (substring.indexOf("<") != -1) {
								int EndIndex = StartIndex+ substring.indexOf("<") + 7;
								String chaptername = inputLine.substring(StartIndex, EndIndex).trim();
								if(!checkExistsChapter("Chương "+chap)){
									listChapters.add(chaptername);
									System.out.println(chaptername);
								}
								
							}
						}
					}
					//end filter list chapters
					//start filter chapter content
					if(inputLine.toLowerCase().contains("class=\"hiddentext\"".toLowerCase())) {
						tmp = "";
					}
					if(inputLine.toLowerCase().contains("Tra đấu Quan Vũ - chiến đấu thôi".toLowerCase())) {//ket thuc 1 chapter
						if (listChapters.size() > listChapterContents.size()){
							tmp = tmp.replaceAll("&quot;", "\"");
							listChapterContents.add(tmp);
						}
					}
					else tmp = tmp + inputLine;
				}
				in.close();
			}
			System.out.println(listChapterContents);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
}
