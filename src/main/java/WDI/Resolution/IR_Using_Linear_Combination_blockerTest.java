package WDI.Resolution;

import java.io.File;

import org.slf4j.Logger;

import WDI.Blocking.BookBlockingKeyBy20Generator;
import WDI.Blocking.BookBlockingKeyByDecadeGenerator;
import WDI.Blocking.BookBlockingKeyByTitleGenerator;
import WDI.Blocking.BookBlockingKeyByYearGenerator;
import WDI.Comparators.BookAuthorComparatorMaxSimilarity;
import WDI.Comparators.BookDateComparator10Years;
import WDI.Comparators.BookDateComparator2Years;
import WDI.Comparators.BookPagesComparatorUnadjustedDeviationSimilarity;
import WDI.Comparators.BookTitleComparatorJaccard;
import WDI.model.Book;
import WDI.model.BookXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.Blocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.matching.rules.MatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;

public class IR_Using_Linear_Combination_blockerTest {
	private static final Logger logger = WinterLogManager.activateLogger("trace");

	public static void main(String[] args) throws Exception {
		// loading data
		System.out.println("*\n*\tLoading datasets\n*");
		HashedDataSet<Book, Attribute> data_books_dataset = new HashedDataSet<>();
		new BookXMLReader().loadFromXML(new File("data/input/books-dataset.xml"), "/catalog/book", data_books_dataset);

		HashedDataSet<Book, Attribute> data_BX_Books = new HashedDataSet<>();
		new BookXMLReader().loadFromXML(new File("data/input/BX-Books.xml"), "/catalog/book", data_BX_Books);

		HashedDataSet<Book, Attribute> data_goodread_books = new HashedDataSet<>();
		new BookXMLReader().loadFromXML(new File("data/input/goodread_books.xml"), "/catalog/book",
				data_goodread_books);
		/**
		 * BX and Books
		 */

		System.out.println("*\n*\tLoading gold standard for BX and books\n*");
		MatchingGoldStandard gsTest_BX_books = new MatchingGoldStandard();
		gsTest_BX_books.loadFromCSVFile(new File("data/goldstandard/goodread_crossing_GS_test.csv"));

		// create a matching rule
		
		// matchingRule_BX_books.activateDebugReport("data/output/debugResultsMatchingRule_BX_books"
		// + ".csv", 1000,
		// gsTest_BX_books);
		// add comparators

		for (double weight1 = 0.1; weight1 < 1.0; weight1 += 0.1) {
			LinearCombinationMatchingRule<Book, Attribute> matchingRule_BX_books = new LinearCombinationMatchingRule<>(0.7);
			double weight2 = 1.0 - weight1;
			matchingRule_BX_books.addComparator(new BookPagesComparatorUnadjustedDeviationSimilarity(), weight1);
			matchingRule_BX_books.addComparator(new BookDateComparator10Years(), weight2);
			// create a blocker
			Blocker<Book, Attribute, Book, Attribute> blocker = new StandardRecordBlocker<>(
					new BookBlockingKeyByTitleGenerator());
			System.out.println("*\n*\tStandard Blocker: by title\n*");
			blocker = new StandardRecordBlocker<>(new BookBlockingKeyByTitleGenerator());
			testBlocker(blocker, data_BX_Books, data_books_dataset, matchingRule_BX_books, gsTest_BX_books);


			System.out.println("weight1: "+weight1+"   "+"weight2: "+weight2+" BX_goodreadbooks(books-datase.xml and BX-Books.xml)");						
		}
		


	}

	protected static void testBlocker(Blocker<Book, Attribute, Book, Attribute> blocker, DataSet<Book, Attribute> ds1,
			DataSet<Book, Attribute> ds2, MatchingRule<Book, Attribute> rule, MatchingGoldStandard gsTest) {
		// Initialize Matching Engine
		MatchingEngine<Book, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		Processable<Correspondence<Book, Attribute>> correspondences = engine.runIdentityResolution(ds1, ds2, null,
				rule, blocker);

		// evaluate your result
		System.out.println("*\n*\tEvaluating result\n*");
		MatchingEvaluator<Book, Attribute> evaluator = new MatchingEvaluator<Book, Attribute>();
		Performance perfTest = evaluator.evaluateMatching(correspondences, gsTest);

		// print the evaluation result
		System.out.println("Academy Awards <-> Actors");
		System.out.println(String.format("Precision: %.4f", perfTest.getPrecision()));
		System.out.println(String.format("Recall: %.4f", perfTest.getRecall()));
		System.out.println(String.format("F1: %.4f", perfTest.getF1()));
	}
}
