package WDI.fusers;

import java.util.List;

import WDI.fusionmodel.Author;
import WDI.fusionmodel.Book;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.list.IntersectionKSources;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class AuthorsFuserIntersectionKSources extends AttributeValueFuser<List<Author>, Book, Attribute> {

/**
* 
* @param k specifies the number of sources 
*/
public AuthorsFuserIntersectionKSources(int k) {
super(new IntersectionKSources<Author, Book, Attribute>(k));
}

	@Override
	public boolean hasValue(Book record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Book.AUTHORS);
	}

	@Override
	public List<Author> getValue(Book record, Correspondence<Attribute, Matchable> correspondence) {
		return record.getAuthors();
	}

	@Override
	public void fuse(RecordGroup<Book, Attribute> group, Book fusedRecord,
			Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<List<Author>, Book, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setAuthors(fused.getValue());
		fusedRecord.setAttributeProvenance(Book.AUTHORS, fused.getOriginalIds());
	}
}
