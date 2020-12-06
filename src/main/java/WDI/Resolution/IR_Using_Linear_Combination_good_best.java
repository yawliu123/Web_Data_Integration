package WDI.Resolution;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import WDI.Blocking.BookBlockingKeyByTitleGenerator;
import WDI.Comparators.BookAuthorComparatorMaxSimilarity;
import WDI.Comparators.BookAuthorComparatorMaximumOfContainment;
import WDI.Comparators.BookAuthorComparatorOverlapSimilarity;
import WDI.Comparators.BookDateComparator10Years;
import WDI.Comparators.BookDateComparator2Years;
import WDI.Comparators.BookDateComparatorWeightedDateSimilarity;
import WDI.Comparators.BookPagesComparatorAbsoluteDifferenceSimilarity;
import WDI.Comparators.BookPagesComparatorDeviationSimilarity;
import WDI.Comparators.BookPagesComparatorUnadjustedDeviationSimilarity;
import WDI.Comparators.BookPublisherComparatorJaccard;
import WDI.Comparators.BookPublisherComparatorJaccardOnNGram;
import WDI.Comparators.BookPublisherComparatorLevenshtein;
import WDI.Comparators.BookTitleComparatorEqual;
import WDI.Comparators.BookTitleComparatorJaccard;
import WDI.Comparators.BookTitleComparatorJaccardOnNGram;
import WDI.Comparators.BookTitleComparatorLevenshtein;
import WDI.model.Book;
import WDI.model.BookXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.Blocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.matching.rules.MatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;

public class IR_Using_Linear_Combination_good_best {
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

		ArrayList<Comparator> comparatorList = new ArrayList<>();
		BookAuthorComparatorMaximumOfContainment acmoc = new BookAuthorComparatorMaximumOfContainment();
		comparatorList.add(acmoc);

		BookAuthorComparatorMaxSimilarity acms = new BookAuthorComparatorMaxSimilarity();
		comparatorList.add(acms);

		BookAuthorComparatorOverlapSimilarity acos = new BookAuthorComparatorOverlapSimilarity();
		comparatorList.add(acos);

		BookDateComparator2Years dc2 = new BookDateComparator2Years();
		comparatorList.add(dc2);

		BookDateComparator10Years dc10 = new BookDateComparator10Years();
		comparatorList.add(dc10);

		BookDateComparatorWeightedDateSimilarity dcwd = new BookDateComparatorWeightedDateSimilarity();
		comparatorList.add(dcwd);

		BookPagesComparatorUnadjustedDeviationSimilarity pcujds = new BookPagesComparatorUnadjustedDeviationSimilarity();
		comparatorList.add(pcujds);

		BookPagesComparatorAbsoluteDifferenceSimilarity pcads = new BookPagesComparatorAbsoluteDifferenceSimilarity();
		comparatorList.add(pcads);

		BookPagesComparatorDeviationSimilarity pcds = new BookPagesComparatorDeviationSimilarity();
		comparatorList.add(pcds);

		BookPublisherComparatorJaccardOnNGram pcjon = new BookPublisherComparatorJaccardOnNGram();
		comparatorList.add(pcjon);

		BookPublisherComparatorJaccard pcj = new BookPublisherComparatorJaccard();
		comparatorList.add(pcj);

		BookPublisherComparatorLevenshtein pcl = new BookPublisherComparatorLevenshtein();
		comparatorList.add(pcl);

		BookTitleComparatorJaccard tcj = new BookTitleComparatorJaccard();
		comparatorList.add(tcj);

		BookTitleComparatorJaccardOnNGram tcjon = new BookTitleComparatorJaccardOnNGram();
		comparatorList.add(tcjon);

		BookTitleComparatorEqual tce = new BookTitleComparatorEqual();
		comparatorList.add(tce);

		BookTitleComparatorLevenshtein tcl = new BookTitleComparatorLevenshtein();
		comparatorList.add(tcl);

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
		ArrayList<Erg> results_BX_books = new ArrayList<Erg>();

		for (int i = 0; i < comparatorList.size() - 1; i++) {
			for (int k = i + 1; k < comparatorList.size(); k++) {

				Comparator c1 = comparatorList.get(i);
				Comparator c2 = comparatorList.get(k);

				for (double weight1 = 0.1; weight1 < 1.0; weight1 += 0.1) {
					LinearCombinationMatchingRule<Book, Attribute> matchingRule_BX_books = new LinearCombinationMatchingRule<>(
							0.7);
					double weight2 = 1.0 - weight1;
					matchingRule_BX_books.addComparator(c1, weight1);
					matchingRule_BX_books.addComparator(c2, weight2);
					// create a blocker
					Blocker<Book, Attribute, Book, Attribute> blocker = new StandardRecordBlocker<>(
							new BookBlockingKeyByTitleGenerator());
					System.out.println("*\n*\tStandard Blocker: by title\n*");
					blocker = new StandardRecordBlocker<>(new BookBlockingKeyByTitleGenerator());
					Erg erg = testBlocker(blocker, data_BX_Books, data_books_dataset, matchingRule_BX_books,
							gsTest_BX_books, weight1, weight2, c1, c2);
					results_BX_books.add(erg);
					// System.out.println("weight1: "+weight1+" "+"weight2: "+weight2+"
					// BX_goodreadbooks(books-datase.xml and BX-Books.xml)");
				}
			}
		}
		/**
		 * good and books, some date is null
		 */
		MatchingGoldStandard gsTest_good_books = new MatchingGoldStandard();
		gsTest_good_books.loadFromCSVFile(new File("data/goldstandard/goodread_bestbook_GS_test.csv"));

		ArrayList<Erg> results_good_best = new ArrayList<Erg>();

		for (int i = 0; i < comparatorList.size() - 1; i++) {
			for (int k = i + 1; k < comparatorList.size(); k++) {
				Comparator c1 = comparatorList.get(i);
				Comparator c2 = comparatorList.get(k);

				for (double weight1 = 0.1; weight1 < 1.0; weight1 += 0.1) {
					LinearCombinationMatchingRule<Book, Attribute> matchingRule_good_books = new LinearCombinationMatchingRule<>(
							0.7);
					double weight2 = 1.0 - weight1;
					matchingRule_good_books.addComparator(new BookPagesComparatorUnadjustedDeviationSimilarity(),
							weight1);
					matchingRule_good_books.addComparator(new BookDateComparator10Years(), weight2);
					// create a blocker
					Blocker<Book, Attribute, Book, Attribute> blocker = new StandardRecordBlocker<>(
							new BookBlockingKeyByTitleGenerator());
					System.out.println("*\n*\tStandard Blocker: by title\n*");
					blocker = new StandardRecordBlocker<>(new BookBlockingKeyByTitleGenerator());
					Erg erg = testBlocker(blocker, data_goodread_books, data_books_dataset, matchingRule_good_books,
							gsTest_good_books, weight1, weight2, c1, c2);
					results_good_best.add(erg);
					// System.out.println("weight1: "+weight1+" "+"weight2: "+weight2+"
					// Best_goodreadbooks(books-datase.xml and goodread-Books.xml)");
				}
			}
		}
		/**
		 * good and BX
		 */
		System.out.println("*\n*\tLoading gold standard for good and books\n*");
		MatchingGoldStandard gsTest_BX_good = new MatchingGoldStandard();
		gsTest_BX_good.loadFromCSVFile(new File("data/goldstandard/crossing_bestbook_GS_test.csv"));

		ArrayList<Erg> results_good_BX = new ArrayList<Erg>();
		for (int i = 0; i < comparatorList.size() - 1; i++) {
			for (int k = i + 1; k < comparatorList.size(); k++) {
				Comparator c1 = comparatorList.get(i);
				Comparator c2 = comparatorList.get(k);
				for (double weight1 = 0.1; weight1 < 1.0; weight1 += 0.1) {
					LinearCombinationMatchingRule<Book, Attribute> matchingRule_good_BX = new LinearCombinationMatchingRule<>(
							0.7);
					double weight2 = 1.0 - weight1;
					matchingRule_good_BX.addComparator(new BookTitleComparatorJaccard(), weight1);
					matchingRule_good_BX.addComparator(new BookAuthorComparatorMaximumOfContainment(), weight2);
					// create a blocker
					Blocker<Book, Attribute, Book, Attribute> blocker = new StandardRecordBlocker<>(
							new BookBlockingKeyByTitleGenerator());
					System.out.println("*\n*\tStandard Blocker: by title\n*");
					blocker = new StandardRecordBlocker<>(new BookBlockingKeyByTitleGenerator());
					Erg erg = testBlocker(blocker, data_goodread_books, data_BX_Books, matchingRule_good_BX,
							gsTest_BX_good, weight1, weight2, c1, c2);
					results_good_BX.add(erg);
					// System.out.println("weight1: "+weight1+" "+"weight2: "+weight2+"
					// Best_goodreadbooks(books-datase.xml and goodread-Books.xml)");
				}
			}
		}
		System.out.println("********************************");

		System.out.println("-------Result of BX and Good");
		for (Erg r : results_BX_books) {
			System.out.println(r.toString());
			System.out.println("---------------------------");
		}

		System.out.println("********************************");

		System.out.println("-------Result of Best and Good");
		for (Erg r : results_good_best) {
			System.out.println(r.toString());
			System.out.println("---------------------------");
		}

		System.out.println("********************************");
		System.out.println("Results of BX and Best ");
		for (Erg r : results_good_BX) {
			System.out.println(r.toString());
			System.out.println("---------------------------");
		}

	}

	protected static Erg testBlocker(Blocker<Book, Attribute, Book, Attribute> blocker, DataSet<Book, Attribute> ds1,
			DataSet<Book, Attribute> ds2, MatchingRule<Book, Attribute> rule, MatchingGoldStandard gsTest,
			double weight1, double weight2, Comparator c1, Comparator c2) {
		// Initialize Matching Engine
		MatchingEngine<Book, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		Processable<Correspondence<Book, Attribute>> correspondences = engine.runIdentityResolution(ds1, ds2, null,
				rule, blocker);

		// evaluate your result
		System.out.println("*\n*\tEvaluating result\n*");
		MatchingEvaluator<Book, Attribute> evaluator = new MatchingEvaluator<Book, Attribute>();
		Performance perfTest = evaluator.evaluateMatching(correspondences, gsTest);

		Erg erg = new Erg(perfTest.getPrecision(), perfTest.getRecall(), perfTest.getF1(), weight1, weight2, c1, c2);

		return erg;

	}
}
