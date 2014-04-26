package gwt.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

@SuppressWarnings("serial")
@Entity
public class Ebook implements Serializable {
	
	@Id public Long id; 
	public String name;
	public String blobkey;
	public List<String> listChapters;
	public List<String> listChapterContents;
	public String owner;
	public Date created;
	public Date updated;
	
	public Ebook(){
		this.created = new Date();
	}
}