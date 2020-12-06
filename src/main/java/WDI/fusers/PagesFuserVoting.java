package WDI.fusers;

import WDI.fusionmodel.Book;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.Voting;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class PagesFuserVoting extends AttributeValueFuser<Integer, Book, Attribute> {

	public PagesFuserVoting() {
		super(new Voting<Integer, Book, Attribute>());
	}
	
	@Override
	public boolean hasValue(Book record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Book.PAGES);
	}
	
	@Override
	public Integer getValue(Book record, Correspondence<Attribute, Matchable> correspondence) {
		return record.getPages();
	}

	@Override
	public void fuse(RecordGroup<Book, Attribute> group, Book fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<Integer, Book, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		int v = 0;
		if(fused.getValue() == null) {
			v = 0;
		}else {
			v = fused.getValue();
		}
		fusedRecord.setPages(v);
		fusedRecord.setAttributeProvenance(Book.PAGES, fused.getOriginalIds());
	}

}
