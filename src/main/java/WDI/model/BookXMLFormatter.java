package WDI.model;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

public class BookXMLFormatter extends XMLFormatter<Book> {

	AuthorXMLFormatter authorFormatter = new AuthorXMLFormatter();
	GenreXMLFormatter genreFormatter = new GenreXMLFormatter();

	@Override
	public Element createRootElement(Document doc) {
		return doc.createElement("books");
	}

	@Override
	public Element createElementFromRecord(Book record, Document doc) {
		Element book = doc.createElement("book");

		book.appendChild(createTextElement("id", record.getIdentifier(), doc));

		book.appendChild(createTextElement("title",
				record.getTitle(),
				doc));
		book.appendChild(createTextElement("publisher",
				record.getPublisher(),
				doc));
		book.appendChild(createTextElement("published_date", record
				.getPublished_date().toString(), doc));
		
		book.appendChild(createTextElement("pages", Integer.toString(record
				.getPages()), doc));
		book.appendChild(createTextElement("rating", Double.toString(record
				.getRating()), doc));
		book.appendChild(createTextElement("rating_count", Integer.toString(record
				.getRating_count()), doc));
		book.appendChild(createAuthorsElement(record, doc));
		book.appendChild(createGenresElement(record, doc));

		return book;
	}

	protected Element createTextElementWithProvenance(String name,
			String value, String provenance, Document doc) {
		Element elem = createTextElement(name, value, doc);
		elem.setAttribute("provenance", provenance);
		return elem;
	}

	protected Element createAuthorsElement(Book record, Document doc) {
		Element authorRoot = authorFormatter.createRootElement(doc);

		for (Author a : record.getAuthors()) {
			authorRoot.appendChild(authorFormatter
					.createElementFromRecord(a, doc));
		}

		return authorRoot;
	}
	
	protected Element createGenresElement(Book record, Document doc) {
		Element genreRoot = genreFormatter.createRootElement(doc);

		for (Genre g : record.getGenres()) {
			genreRoot.appendChild(genreFormatter
					.createElementFromRecord(g, doc));
		}

		return genreRoot;
	}

}