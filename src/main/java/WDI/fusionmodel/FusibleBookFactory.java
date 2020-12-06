package WDI.fusionmodel;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.uni_mannheim.informatik.dws.winter.model.FusibleFactory;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class FusibleBookFactory implements FusibleFactory<Book, Attribute> {

	@Override
	public Book createInstanceForFusion(RecordGroup<Book, Attribute> cluster) {

		List<String> ids = new LinkedList<>();

		for (Book b : cluster.getRecords()) {
			ids.add(b.getIdentifier());
		}

		Collections.sort(ids);

		String mergedId = StringUtils.join(ids, '+');

		return new Book(mergedId, "fused");
	}
	
}
