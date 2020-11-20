package WDI.Comparators;

import WDI.model.Book;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.numeric.AbsoluteDifferenceSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;

/**
 * {@link Comparator} for {@link Movie}s based on the {@link Movie#getTitle()}
 * value and their {@link LevenshteinSimilarity} value.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class BookPagesComparatorAbsoluteDifferenceSimilarity implements Comparator<Book, Attribute> {

	private static final long serialVersionUID = 1L;
	private AbsoluteDifferenceSimilarity sim = new AbsoluteDifferenceSimilarity(50);

	private ComparatorLogger comparisonLog;

	@Override
	public double compare(Book record1, Book record2, Correspondence<Attribute, Matchable> schemaCorrespondences) {

		double n1 = record1.getPages();
		double n2 = record2.getPages();

		double similarity = sim.calculate(n1, n2);

		if (this.comparisonLog != null) {
			this.comparisonLog.setComparatorName(getClass().getName());

			this.comparisonLog.setRecord1Value(record1.getPublished_date().toString());
			this.comparisonLog.setRecord2Value(record2.getPublished_date().toString());

			this.comparisonLog.setSimilarity(Double.toString(similarity));
		}
		return similarity;
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