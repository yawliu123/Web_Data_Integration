package WDI.fusionmodel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleFactory;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;


public class BookXMLReader extends XMLMatchableReader<Book, Attribute> implements
FusibleFactory<Book, Attribute> {

	@Override
	protected void initialiseDataset(DataSet<Book, Attribute> dataset) {
		super.initialiseDataset(dataset);

		// the schema is defined in the Movie class and not interpreted from the file, so we have to set the attributes manually
		dataset.addAttribute(Book.TITLE);
		dataset.addAttribute(Book.AUTHORS);
		dataset.addAttribute(Book.PUBLISHER);
		dataset.addAttribute(Book.PUBLISHED_DATE);
		dataset.addAttribute(Book.GENRES);
		dataset.addAttribute(Book.PAGES);
		dataset.addAttribute(Book.RATING);
		dataset.addAttribute(Book.RATING_COUNT);

	}

	@Override
	public Book createModelFromElement(Node node, String provenanceInfo) {
		//String id = getValueFromChildElement(node, "id");
		String id = node.getAttributes().getNamedItem("id").getNodeValue();

		// create the object with id and provenance information
		Book book = new Book(id, provenanceInfo);

		// fill the attributes
		book.setTitle(getValueFromChildElement(node, "title"));
		book.setPublisher(getValueFromChildElement(node, "publisher"));

		///convert string rating to double
		try{
			String rating = getValueFromChildElement(node, "rating");
			double rat = Double.parseDouble(rating);
			book.setRating(rat);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// convert string rating_count to int
		try{
			String rating_count = getValueFromChildElement(node, "rating_count");
			int r = Integer.parseInt(rating_count.trim());
			book.setRating_count(r);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// convert string pages to int
		try{
			String pages = getValueFromChildElement(node, "pages");
			int p = Integer.parseInt(pages.trim());
			book.setPages(p);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// convert the date string into a DateTime object
		try {
			String date = getValueFromChildElement(node, "published_date");
			if (date != null && !date.isEmpty()) {
				DateTimeFormatter formatter = new DateTimeFormatterBuilder()
						.appendPattern("yyyy-MM-dd")
						.parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
						.parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
						.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
						.toFormatter(Locale.ENGLISH);
				LocalDateTime dt = LocalDateTime.parse(date, formatter);
				book.setPublished_date(dt);
			}else {
				System.out.println("&&&&&&&&&&&&&&&& the date is null");
				String str = "2020-11-20";
				DateTimeFormatter formatter = new DateTimeFormatterBuilder()
						.appendPattern("yyyy-MM-dd")
						.parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
						.parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
						.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
						.toFormatter(Locale.ENGLISH);
				LocalDateTime dt = LocalDateTime.parse(str, formatter);
				book.setPublished_date(dt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// load the list of actors
		List<Author> authors = getObjectListFromChildElement(node, "authors",
				"author", new AuthorXMLReader(), provenanceInfo);
		book.setAuthors(authors);

		// load the list of genres
		List<Genre> genres = getObjectListFromChildElement(node, "genres",
				"genres", new GenreXMLReader(), provenanceInfo);
		book.setGenres(genres);

		return book;
	}


	@Override
	public Book createInstanceForFusion(RecordGroup<Book, Attribute> cluster) {

		List<String> ids = new LinkedList<>();

		for (Book b : cluster.getRecords()) {
			ids.add(b.getIdentifier());
		}

		Collections.sort(ids);

		String mergedId = StringUtils.join(ids, '+');

		return new Book(mergedId, "fused");
	}

}
