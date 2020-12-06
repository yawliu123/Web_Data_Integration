package WDI.fusers;

import WDI.fusionmodel.Book;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.numeric.Average;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class RatingCountFuserMedian extends AttributeValueFuser<Double, Book, Attribute> {

	public RatingCountFuserMedian() {
		super(new Average<Book, Attribute>());
	}
	
	@Override
	public boolean hasValue(Book record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Book.RATING_COUNT);
	}
	
	@Override
	public Double getValue(Book record, Correspondence<Attribute, Matchable> correspondence) {
		return Double.valueOf(record.getRating_count());
	}

	@Override
	public void fuse(RecordGroup<Book, Attribute> group, Book fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<Double, Book, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		int count = 0;
		if(fused.getValue() == null) {
			count = Integer.valueOf(0);
		}else {
			count = fused.getValue().intValue();
		}
		fusedRecord.setRating_count(count);
		fusedRecord.setAttributeProvenance(Book.RATING_COUNT, fused.getOriginalIds());
	}

}
