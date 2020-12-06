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

public class PagesFuserAverage extends AttributeValueFuser<Double,Book, Attribute> {

	public PagesFuserAverage() {
		super(new Average<Book, Attribute>());
	}
	
	@Override
	public boolean hasValue(Book record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Book.PAGES);
	}
	
	@Override
	public Double getValue(Book record, Correspondence<Attribute, Matchable> correspondence) {
		return Double.valueOf(record.getPages());
	}

	@Override
	public void fuse(RecordGroup<Book, Attribute> group, Book fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<Double, Book, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		int page = 0;
		if(fused.getValue() == null) {
			page = Integer.valueOf(0);
		}else {
			page = fused.getValue().intValue();
		}
		fusedRecord.setPages(page);
		fusedRecord.setAttributeProvenance(Book.PAGES, fused.getOriginalIds());
	}

}
