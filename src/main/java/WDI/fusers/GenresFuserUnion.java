package WDI.fusers;

import java.util.List;


import WDI.fusionmodel.Book;
import WDI.fusionmodel.Genre;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.list.Union;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class GenresFuserUnion extends AttributeValueFuser<List<Genre>, Book, Attribute> {
	
	public GenresFuserUnion() {
		super(new Union<Genre, Book, Attribute>());
	}
	
	@Override
	public boolean hasValue(Book record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Book.GENRES);
	}
	
	@Override
	public List<Genre> getValue(Book record, Correspondence<Attribute, Matchable> correspondence) {
		return record.getGenres();
	}

	@Override
	public void fuse(RecordGroup<Book, Attribute> group, Book fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<List<Genre>, Book, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setGenres(fused.getValue());
		fusedRecord.setAttributeProvenance(Book.GENRES, fused.getOriginalIds());
	}
}
