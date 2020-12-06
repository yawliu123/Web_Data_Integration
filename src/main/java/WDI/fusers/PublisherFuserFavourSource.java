package WDI.fusers;

import WDI.fusionmodel.Book;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.meta.FavourSources;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class PublisherFuserFavourSource extends AttributeValueFuser<String, Book, Attribute> {

	public PublisherFuserFavourSource() {
		super(new FavourSources<String,Book, Attribute>());
	}

	@Override
	public boolean hasValue(Book record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Book.PUBLISHER);
	}

	@Override
	public String getValue(Book record, Correspondence<Attribute, Matchable> correspondence) {
		return record.getPublisher();
	}

	@Override
	public void fuse(RecordGroup<Book, Attribute> group, Book fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<String, Book, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setPublisher(fused.getValue());
		fusedRecord.setAttributeProvenance(Book.PUBLISHER,
				fused.getOriginalIds());
	}
}
