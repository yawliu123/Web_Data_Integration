package WDI.fusionmodel;

import java.io.Serializable;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class Genre extends AbstractRecord<Attribute> implements Serializable {

	private static final long serialVersionUID = 1L;
	private String genre;
	
	
	public Genre(String identifier, String provenance) {
		super(identifier,provenance);
	}

	public static final Attribute GENRE = new Attribute("genre");
	

	@Override
	public boolean hasValue(Attribute attribute) {
		// TODO Auto-generated method stub
		if (attribute == GENRE) {
			return genre != null;
		} else {
			return false;
		}
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Genre other = (Genre) obj;
		if (genre == null) {
			if (other.genre != null)
				return false;
		} else if (!genre.equals(other.genre))
			return false;
		return true;
	}
	
	/**
	 * warning: change the basic value to 32
	 */
	public int hashCode() {
		int result = 32 + ((genre == null) ? 0 : genre.hashCode());
		return result;
	}
	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

}
