package WDI.DataFusion;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

import org.apache.logging.log4j.Logger;
import WDI.fusionmodel.Book;
import WDI.fusionmodel.Author;
import WDI.fusionmodel.BookXMLReader;
import WDI.fusionmodel.BookXMLFormatter;
import WDI.fusionmodel.FusibleBookFactory;
import WDI.Evaluation.AuthorsEvaluationRule;
import WDI.Evaluation.DateEvaluationRule;
import WDI.Evaluation.GenresEvaluationRule;
import WDI.Evaluation.PagesEvaluationRule;
import WDI.Evaluation.PublisherEvaluationRule;
import WDI.Evaluation.RatingCountEvaluationRule;
import WDI.Evaluation.RatingEvaluationRule;
import WDI.Evaluation.TitleEvaluationRule;
import WDI.fusers.AuthorsFuserFavourSource;
import WDI.fusers.AuthorsFuserIntersection;
import WDI.fusers.AuthorsFuserUnion;
import WDI.fusers.DateFuserFavourSource;
import WDI.fusers.GenresFuserUnion;
import WDI.fusers.PagesFuserAverage;
import WDI.fusers.PublisherFuserLongestString;
import WDI.fusers.RatingCountFuserAverage;
import WDI.fusers.RatingFuserAverage;
import WDI.fusers.TitleFuserShortestString;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeFusionLogger;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.CorrespondenceSet;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEngine;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEvaluator;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionStrategy;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleDataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleHashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroupFactory;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;

public class DataFusion 
{
	/*
	 * Logging Options:
	 * 		default: 	level INFO	- console
	 * 		trace:		level TRACE     - console
	 * 		infoFile:	level INFO	- console/file
	 * 		traceFile:	level TRACE	- console/file
	 *  
	 * To set the log level to trace and write the log to winter.log and console, 
	 * activate the "traceFile" logger as follows:
	 
	 *
	 */
    private static final org.slf4j.Logger logger = WinterLogManager.activateLogger("traceFile");
	//private static final Logger logger = WinterLogManager.activateLogger("traceFile");
	
    public static void main( String[] args ) throws Exception
    {
		// Load the Data into FusibleDataSet
		System.out.println("*\n*\tLoading datasets\n*");
		
		 FusibleDataSet<Book, Attribute> BX_dataset = new FusibleHashedDataSet<>();
		 new BookXMLReader().loadFromXML(new File("data/input/BX-books.xml"), "/catalog/book", BX_dataset);
		 BX_dataset.printDataSetDensityReport();

		FusibleDataSet<Book, Attribute> books_dataset = new FusibleHashedDataSet<>();
		new BookXMLReader().loadFromXML(new File("data/input/books-dataset.xml"), "/catalog/book", books_dataset);
		books_dataset.printDataSetDensityReport();

		FusibleDataSet<Book, Attribute> goodread_dataset = new FusibleHashedDataSet<>();
		new BookXMLReader().loadFromXML(new File("data/input/goodread_books.xml"), "/catalog/book", goodread_dataset);
		goodread_dataset.printDataSetDensityReport();

		// Maintain Provenance
		// Scores (e.g. from rating)
		BX_dataset.setScore(3.0);
		books_dataset.setScore(1.0);
		goodread_dataset.setScore(2.0);
		/*
		// Date (e.g. last update)
		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
		        .appendPattern("yyyy-MM-dd")
		        .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
		        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
		        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
		        .toFormatter(Locale.ENGLISH);
		
		BX_dataset.setDate(LocalDateTime.parse("2012-01-01", formatter));
		books_dataset.setDate(LocalDateTime.parse("2010-01-01", formatter));
		goodread_dataset.setDate(LocalDateTime.parse("2008-01-01", formatter));
		*/
		// load correspondences
		System.out.println("*\n*\tLoading correspondences\n*");
		CorrespondenceSet<Book, Attribute> correspondences = new CorrespondenceSet<>();
		
		//correspondences.loadCorrespondences(new File("data/correspondences/BX_good.csv"), BX_dataset, goodread_dataset);
		correspondences.loadCorrespondences(new File("data/correspondences/books_good.csv"), books_dataset, goodread_dataset);
		correspondences.loadCorrespondences(new File("data/correspondences/BX_books.csv"), BX_dataset, books_dataset);

		// write group size distribution
		correspondences.printGroupSizeDistribution();
		
		//load the gold standard 
		DataSet<Book, Attribute> gs = new FusibleHashedDataSet<>();
		new BookXMLReader().loadFromXML(new File("data/goldstandard/gs_fusion.xml"), "/catalog/book", gs);
		
		// define the fusion strategy
		DataFusionStrategy<Book, Attribute> strategy = new DataFusionStrategy<>(new BookXMLReader());
		
		//write debug results to file
		strategy.activateDebugReport("data/output/debugResultsDatafusion.csv", 1000, gs);
		
		//add attribute fusers
		strategy.addAttributeFuser(Book.RATING_COUNT, new RatingCountFuserAverage(), new RatingCountEvaluationRule());
		strategy.addAttributeFuser(Book.RATING, new RatingFuserAverage(), new RatingEvaluationRule());
		strategy.addAttributeFuser(Book.GENRES, new GenresFuserUnion(), new GenresEvaluationRule());
		strategy.addAttributeFuser(Book.PAGES, new PagesFuserAverage(), new PagesEvaluationRule());
		strategy.addAttributeFuser(Book.TITLE, new TitleFuserShortestString(),new TitleEvaluationRule());
		strategy.addAttributeFuser(Book.PUBLISHER,new PublisherFuserLongestString(), new PublisherEvaluationRule());
		strategy.addAttributeFuser(Book.PUBLISHED_DATE, new DateFuserFavourSource(),new DateEvaluationRule());
		strategy.addAttributeFuser(Book.AUTHORS, new AuthorsFuserUnion(),new AuthorsEvaluationRule());
		
		// create the fusion engine
		DataFusionEngine<Book, Attribute> engine = new DataFusionEngine<Book, Attribute>(strategy);

		// print consistency report
		engine.printClusterConsistencyReport(correspondences, null);

		// print record groups sorted by consistency
		engine.writeRecordGroupsByConsistency(new File("data/output/recordGroupConsistencies.csv"), correspondences, null);
		
		// run the fusion
		System.out.println("*\n*\tRunning data fusion\n*");
		FusibleDataSet<Book, Attribute> fusedDataSet = engine.run(correspondences, null);
		fusedDataSet.printDataSetDensityReport();
		// write the result
		new BookXMLFormatter().writeXML(new File("data/output/fused.xml"), fusedDataSet);

		// evaluate
		
		System.out.println("*\n*\tEvaluating results\n*");		
		DataFusionEvaluator<Book, Attribute> evaluator = new DataFusionEvaluator<>(
				strategy, new RecordGroupFactory<Book, Attribute>());
		double accuracy = evaluator.evaluate(fusedDataSet, gs, null);

		logger.info(String.format("Accuracy: %.2f", accuracy));
    }
    
    
}
