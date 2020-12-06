package WDI.fusionmodel;

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

		book.appendChild(createTextElementWithProvenance("title",
				record.getTitle(),
				record.getMergedAttributeProvenance(Book.TITLE), doc));
		book.appendChild(createTextElementWithProvenance("publisher",
				record.getPublisher(),
				record.getMergedAttributeProvenance(Book.PUBLISHER), doc));
		book.appendChild(createTextElementWithProvenance("published_date", record
				.getPublished_date().toString(), record
				.getMergedAttributeProvenance(Book.PUBLISHED_DATE), doc));
		book.appendChild(createTextElementWithProvenance("pages", Integer.toString(record
				.getPages()), record.getMergedAttributeProvenance(Book.PAGES), doc));
		book.appendChild(createTextElementWithProvenance("rating", Double.toString(record
				.getRating()), record.getMergedAttributeProvenance(Book.RATING), doc));
		book.appendChild(createTextElementWithProvenance("rating_count", Integer.toString(record
				.getRating_count()), record.getMergedAttributeProvenance(Book.RATING_COUNT), doc));
		book.appendChild(createAuthorsElement(record, doc));
		if(record.getGenres().size() != 0) book.appendChild(createGenresElement(record, doc));

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
		authorRoot.setAttribute("provenance",
				record.getMergedAttributeProvenance(Book.AUTHORS));

		for (Author a : record.getAuthors()) {
			authorRoot.appendChild(authorFormatter
					.createElementFromRecord(a, doc));
		}

		return authorRoot;
	}
	protected Element createGenresElement(Book record, Document doc) {
		Element genreRoot = genreFormatter.createRootElement(doc);
		genreRoot.setAttribute("provenance",
				record.getMergedAttributeProvenance(Book.GENRES));

		for (Genre g : record.getGenres()) {
			genreRoot.appendChild(genreFormatter
					.createElementFromRecord(g, doc));
		}

		return genreRoot;
	}

}
