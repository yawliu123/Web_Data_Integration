package WDI.Comparators;

import WDI.model.Book;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.date.YearSimilarity;

/**
 * {@link Comparator} for {@link Movie}s based on the {@link Book#getDate()}
 * value, with a maximal difference of 2 years.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class BookDateComparator2Years implements Comparator<Book, Attribute> {

	private static final long serialVersionUID = 1L;
	private YearSimilarity sim = new YearSimilarity(2);

	private ComparatorLogger comparisonLog;

	@Override
	public double compare(Book record1, Book record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {
		double similarity = sim.calculate(record1.getPublished_date(), record2.getPublished_date());
		if (this.comparisonLog != null) {
			this.comparisonLog.setComparatorName(getClass().getName());

			if (record1.getPublished_date() == null)
				this.comparisonLog.setRecord1Value("");
			else
				this.comparisonLog.setRecord1Value(record1.getPublished_date().toString());
			
			if(record2.getPublished_date() == null)
				this.comparisonLog.setRecord2Value("");
			else
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