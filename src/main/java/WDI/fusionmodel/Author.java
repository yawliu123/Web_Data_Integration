package WDI.fusionmodel;

import java.io.Serializable;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class Author extends AbstractRecord<Attribute> implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;
	
	public Author(String identifier, String provenance) {
		super(identifier,provenance);
	}
	
	public static final Attribute NAME = new Attribute("Name");
	
	@Override
	public boolean hasValue(Attribute attribute) {
		// TODO Auto-generated method stub
		if(attribute==NAME) {
			return name!=null;
		}else {
			return false;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Author other = (Author) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = 31 + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	
	public String getName() {
		return name;
	}
    

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("[Author: %s]", getName());
	}

}
