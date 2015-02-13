package gwt.server;

import java.io.OutputStream;
import java.util.List;

import com.adobe.dp.epub.io.OCFContainerWriter;
import com.adobe.dp.epub.ncx.TOCEntry;
import com.adobe.dp.epub.opf.NCXResource;
import com.adobe.dp.epub.opf.OPSResource;
import com.adobe.dp.epub.opf.Publication;
import com.adobe.dp.epub.ops.Element;
import com.adobe.dp.epub.ops.OPSDocument;
//created by nguyen quy dat
public class Epub {
	public Publication epub;

	public Epub(String Title, List<String> listChapters,
			List<String> listChapterContents, OutputStream out) {
		try {
			// create new EPUB document
			epub = new Publication();

			// set up title, author and language
			epub.addDCMetadata("title", Title);
			epub.addDCMetadata("creator", System.getProperty("desune"));
			epub.addDCMetadata("language", "en");

			// prepare table of contents
			NCXResource toc = epub.getTOC();
			TOCEntry rootTOCEntry = toc.getRootTOCEntry();
			
			for (int i = 0; i < listChapters.size(); i++) {
				// create first chapter resource
				OPSResource chapter = epub.createOPSResource("OPS/chapter"
						+ (i + 1) + ".html");
				epub.addToSpine(chapter);

				// get chapter document
				OPSDocument chapterDoc = chapter.getDocument();

				// add chapter to the table of contents
				TOCEntry chapterTOCEntry = toc.createTOCEntry(
						listChapters.get(i), chapterDoc.getRootXRef());
				rootTOCEntry.add(chapterTOCEntry);

				// chapter XHTML body element
				Element body = chapterDoc.getBody();

				// add a paragraph
				Element paragraph = chapterDoc.createElement("p");
				paragraph.add(listChapterContents.get(i));
				body.add(paragraph);
			}
			OCFContainerWriter writer = new OCFContainerWriter(out);
			epub.serialize(writer);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
