package WDI.Blocking;

import WDI.model.Book;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class BookBlockingKeyBy20Generator extends
RecordBlockingKeyGenerator<Book, Attribute>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void generateBlockingKeys(Book record, Processable<Correspondence<Attribute, Matchable>> correspondences,
			DataIterator<Pair<String, Book>> resultCollector) {
		// TODO Auto-generated method stub
		resultCollector.next(new Pair<>(Double.toString(record.getPublished_date().getYear() / 0.01), record));
		
	}

}
