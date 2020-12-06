package WDI.fusionmodel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

public class AuthorXMLFormatter extends XMLFormatter<Author> {

	@Override
	public Element createRootElement(Document doc) {
		return doc.createElement("authors");
	}

	@Override
	public Element createElementFromRecord(Author record, Document doc) {
		Element author = doc.createElement("author");

		author.appendChild(createTextElement("name", record.getName(), doc));

		return author;
	}

}

