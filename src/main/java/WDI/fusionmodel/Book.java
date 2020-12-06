package WDI.fusionmodel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class Book extends AbstractRecord<Attribute> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	protected String id;
	private String title;
	private List<Author> authors;
	private List<Genre> genres;
	private double rating;
	private int rating_count;
	private LocalDateTime published_date;
	private String publisher;
	private int pages;
	
	public Book(String identifier, String provenance) {
		super(identifier, provenance);
		authors = new LinkedList<>();
		genres = new LinkedList<>();
	}
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public int getRating_count() {
		return rating_count;
	}

	public void setRating_count(int rating_count) {
		this.rating_count = rating_count;
	}

	public LocalDateTime getPublished_date() {
		return published_date;
	}

	public void setPublished_date(LocalDateTime published_date) {
		this.published_date = published_date;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}
	
	private Map<Attribute, Collection<String>> provenance = new HashMap<>();
	private Collection<String> recordProvenance;

	public void setRecordProvenance(Collection<String> provenance) {
		recordProvenance = provenance;
	}

	public Collection<String> getRecordProvenance() {
		return recordProvenance;
	}

	public void setAttributeProvenance(Attribute attribute,
			Collection<String> provenance) {
		this.provenance.put(attribute, provenance);
	}

	public Collection<String> getAttributeProvenance(String attribute) {
		return provenance.get(attribute);
	}

	public String getMergedAttributeProvenance(Attribute attribute) {
		Collection<String> prov = provenance.get(attribute);

		if (prov != null) {
			return StringUtils.join(prov, "+");
		} else {
			return "";
		}
	}

	public static final Attribute TITLE = new Attribute("Title");
	public static final Attribute AUTHORS = new Attribute("Authors");
	public static final Attribute GENRES = new Attribute("Genres");
	public static final Attribute PUBLISHER = new Attribute("Publisher");
	public static final Attribute PUBLISHED_DATE = new Attribute("Published_date");
	public static final Attribute PAGES = new Attribute("Pages");
	public static final Attribute RATING = new Attribute("Rating");
	public static final Attribute RATING_COUNT = new Attribute("Rating_count");
	
	
	@Override
	public boolean hasValue(Attribute attribute) {
		if(attribute==TITLE)
			return getTitle() != null && !getTitle().isEmpty();
		else if(attribute==AUTHORS)
			return getAuthors() != null && getAuthors().size() > 0;
		else if(attribute==GENRES)
			return getGenres() != null && getGenres().size() > 0;
		else if(attribute==PUBLISHER)
			return getPublisher() != null && !getPublisher().isEmpty();
		else if(attribute==PUBLISHED_DATE)
			return getPublished_date() != null;
		else if(attribute==PAGES)
			return getPages() !=0;
		else if(attribute==RATING)
			return getRating() != 0;
		else if(attribute==RATING_COUNT)
			return getRating_count() != 0;	
		else
			return false;
	}
	
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return String.format("[Book %s: %s / %s / %s / %s]", getIdentifier(),getTitle(),String.valueOf(getRating()),getPublished_date().toString(),getPublisher());
    }
    
    @Override
    public int hashCode() {
    	// TODO Auto-generated method stub
//    	if(this.id == null) {
//    		System.out.println("it is null????????????????????????????????");
//    		this.id = "0";
//    	}
    	return getIdentifier().hashCode();
    }
    
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Book){
			return this.getIdentifier().equals(((Book) obj).getIdentifier());
		}else
			return false;
	}
}
