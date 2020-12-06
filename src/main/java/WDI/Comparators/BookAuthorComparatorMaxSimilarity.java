package WDI.Comparators;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.list.MaxSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.string.JaccardOnNGramsSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import WDI.model.Author;
import WDI.model.Book;

/**
 * {@link Comparator} for {@link Movie}s based on the
 * {@link Movie#getDirector()} values, and their
 * {@link TokenizingJaccardSimilarity} similarity, with a lower casing
 * beforehand.
 * 
 * @author Robert Meusel (robert@dwslab.de)
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class BookAuthorComparatorMaxSimilarity implements Comparator<Book, Attribute> {

	private static final long serialVersionUID = 1L;
	private MaxSimilarity<String> sim = new MaxSimilarity<String>();
	
	private ComparatorLogger comparisonLog;

	@Override
	public double compare(Book record1, Book record2, Correspondence<Attribute, Matchable> schemaCorrespondences) {

		// preprocessing
		List<Author> list1 = record1.getAuthors();
		List<Author> list2 = record2.getAuthors();

		List<String> l1 = new LinkedList<String>();
		List<String> l2 = new LinkedList<String>();

		String print1 = list1.stream().map(Author::getName).collect(Collectors.joining(", "));
		String print2 = list1.stream().map(Author::getName).collect(Collectors.joining(", "));


		for(int i=0; i<list1.size(); i++){
			if(preprocesString(list1.get(i).getName()) != null)
				l1.add(preprocesString(list1.get(i).getName()));
			else l1.add("");
		}

		for(int i=0; i<list2.size(); i++){
			if(preprocesString(list2.get(i).getName()) != null)
				l2.add(preprocesString(list2.get(i).getName()));
			else l2.add("");
		}

		//TokenizingJaccardSimilarity jaccard = new TokenizingJaccardSimilarity();
		JaccardOnNGramsSimilarity jaccard = new JaccardOnNGramsSimilarity(1);
		//LevenshteinSimilarity levenshtein = new LevenshteinSimilarity();

		sim.setInnerSimilarity(jaccard);
		sim.setInnerSimilarityThreshold(0.5);

		double similarity = sim.calculate(l1,l2);

		if (this.comparisonLog != null) {
			this.comparisonLog.setComparatorName(getClass().getName());

			if (record1.getPublished_date() == null)
				this.comparisonLog.setRecord1Value("");
			else
				this.comparisonLog.setRecord1Value(print1);
			
			if(record2.getPublished_date() == null)
				this.comparisonLog.setRecord2Value("");
			else
				this.comparisonLog.setRecord2Value(print2);
			
			this.comparisonLog.setSimilarity(Double.toString(similarity));
		}

		return similarity;
	}

	public String preprocesString(String s) {
		// Normalize Spelling: lowercase, remove punctuation and remove non-ASCII characters
		s = s.toLowerCase();
		s = s.replaceAll("&amp;amp", "");
		s = s.replaceAll("\\p{Punct}", "");
		s = s.replaceAll("[^\\p{ASCII}]", "");
		return s;
	}

	@Override
	public ComparatorLogger getComparisonLog() {
		return this.comparisonLog;
	}

	@Override
	public void setComparisonLog(ComparatorLogger comparatorLog) {
		this.comparisonLog = comparatorLog;
	}

}