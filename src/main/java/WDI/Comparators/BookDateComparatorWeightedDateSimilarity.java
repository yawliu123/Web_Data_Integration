package WDI.Comparators;
import WDI.model.Book;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.date.WeightedDateSimilarity;

/**
 * {@link Comparator} for {@link Movie}s based on the {@link Movie#getDate()}
 * value. With a maximal difference of 10 years.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class BookDateComparatorWeightedDateSimilarity implements Comparator<Book, Attribute> {

	private static final long serialVersionUID = 1L;
	private WeightedDateSimilarity sim = new WeightedDateSimilarity(0.2,0.2,0.6);
	
	private ComparatorLogger comparisonLog;

	@Override
	public double compare(
			Book record1,
			Book record2,
			Correspondence<Attribute, Matchable> schemaCorrespondences) {
    	
    	double similarity = sim.calculate(record1.getPublished_date(), record2.getPublished_date());
    	
		if(this.comparisonLog != null){
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