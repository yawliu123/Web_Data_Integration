package WDI.model;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import de.uni_mannheim.informatik.dws.winter.model.Matchable;

public class Book implements Matchable{
	
	protected String id;
	protected String provenance;
	private String title;
	private List<Author> authors;
	private List<Genre> genres;
	private double rating;
	private int rating_count;
	private LocalDateTime published_date;
	private String publisher;
	private int pages;
	
	public Book(String identifier, String provenance) {
		id = identifier;
		this.provenance = provenance;
		authors = new LinkedList<>();
		genres = new LinkedList<>();
	}
	
	

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public String getProvenance() {
		// TODO Auto-generated method stub
		return provenance;
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
    @Override
    public String toString() {
		// TODO Auto-generated method stub
		if(getPublished_date() != null)
			return String.format("[Book %s: %s / %s / %s / %s]", getIdentifier(),getTitle(),String.valueOf(getRating()),getPublished_date().toString(),getPublisher());
		else return String.format("[Book %s: %s / %s / %s / %s]", getIdentifier(),getTitle(),String.valueOf(getRating()),"",getPublisher());
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
