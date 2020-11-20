package WDI.model;

import org.w3c.dom.Node;

//import WDI.model.Author;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

public class AuthorXMLReader extends XMLMatchableReader<Author, Attribute> {

	@Override
	public Author createModelFromElement(Node node, String provenanceInfo) {
		String id = getValueFromChildElement(node, "id");

		// create the object with id and provenance information
		Author author = new Author(id, provenanceInfo);

		// fill the attributes
		author.setName(getValueFromChildElement(node, "name"));

		return author;
	}

}