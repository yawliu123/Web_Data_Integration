package WDI.model;

import org.w3c.dom.Node;

//import WDI.model.Genre;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

public class GenreXMLReader extends XMLMatchableReader<Genre, Attribute> {

	@Override
	public Genre createModelFromElement(Node node, String provenanceInfo) {
		String id = getValueFromChildElement(node, "id");

		// create the object with id and provenance information
		Genre genre = new Genre(id, provenanceInfo);

		// fill the attributes
		genre.setGenre(getValueFromChildElement(node, "genre"));

		return genre;
	}

}