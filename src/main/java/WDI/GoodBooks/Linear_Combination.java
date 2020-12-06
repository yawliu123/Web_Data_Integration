package WDI.GoodBooks;

import java.io.File;

import org.slf4j.Logger;

import WDI.Blocking.BookBlockingKeyByTitleGenerator;
import WDI.Comparators.BookDateComparator10Years;
import WDI.Comparators.BookDateComparator2Years;
import WDI.Comparators.BookPagesComparatorDeviationSimilarity;
import WDI.Comparators.BookTitleComparatorJaccard;
import WDI.model.Book;
import WDI.model.BookXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;

public class Linear_Combination {
	private static final Logger logger = WinterLogManager.activateLogger("default");
	
	public static void main(String[] args) throws Exception{
		// loading data                                                           
		System.out.println("*\n*\tLoading datasets\n*");                          
		HashedDataSet<Book, Attribute> data_books_dataset = new HashedDataSet<>();
		new BookXMLReader().loadFromXML(new File("data/input/books-dataset.xml"), "/catalog/book",data_books_dataset);
		
		HashedDataSet<Book,Attribute> data_BX_Books = new HashedDataSet<>();
		new BookXMLReader().loadFromXML(new File("data/input/BX-Books.xml"),"/catalog/book", data_BX_Books);
		
		HashedDataSet<Book, Attribute> data_goodread_books = new HashedDataSet<>();
		new BookXMLReader().loadFromXML(new File("data/input/goodread_books.xml"), "/catalog/book",data_goodread_books);
		/**
		 * BX and books
		 */
		System.out.println("*\n*\tLoading gold standard for BX and books\n*");
		MatchingGoldStandard gsTest_BX_books = new MatchingGoldStandard();
		gsTest_BX_books.loadFromCSVFile(new File("data/goldstandard/goodread_crossing_GS_test.csv"));
		//create a matching rule
		LinearCombinationMatchingRule<Book, Attribute> matchingRule_BX_books = new LinearCombinationMatchingRule<>(     
		1.0); 
		matchingRule_BX_books.activateDebugReport("data/output/debugResultsMatchingRule_BX_books" + ".csv", 1000, gsTest_BX_books);
		// add comparators
		matchingRule_BX_books.addComparator(new BookDateComparator2Years(), 0.5);
		matchingRule_BX_books.addComparator(new BookTitleComparatorJaccard(), 0.5);
		//create a blocker
		//NoBlocker<Book, Attribute> blocker_BX_books = new NoBlocker<>();
		StandardRecordBlocker<Book, Attribute> blocker_BX_books = new StandardRecordBlocker<Book, Attribute>(new BookBlockingKeyByTitleGenerator());
		blocker_BX_books.setMeasureBlockSizes(true);
		blocker_BX_books.collectBlockSizeData("data/output/debugResultsBlocking_BX_books.csv", 100);
		//Initialize matching Engine
		MatchingEngine<Book, Attribute> engine_BX_books = new MatchingEngine<>();
		//Execute the matching
		System.out.println("*\n*\tRunning identity resolution for BX and books\n*");
		
		Processable<Correspondence<Book, Attribute>> correspondences_BX_books = engine_BX_books.runIdentityResolution(
				data_BX_Books, data_books_dataset, null, matchingRule_BX_books,
				blocker_BX_books);
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/BX_books_correspondences.csv"), correspondences_BX_books);
		System.out.println("*\n*\tEvaluating result for BX and books\n*");
		MatchingEvaluator<Book, Attribute> evaluator_BX_books = new MatchingEvaluator<Book, Attribute>();
		Performance perfTest_BX_books = evaluator_BX_books.evaluateMatching(correspondences_BX_books,
				gsTest_BX_books);

		// print the evaluation result
		System.out.println("Books <-> Author");
		System.out.println(String.format(
				"Precision: %.4f",perfTest_BX_books.getPrecision()));
		System.out.println(String.format(
				"Recall: %.4f",	perfTest_BX_books.getRecall()));
		System.out.println(String.format(
				"F1: %.4f",perfTest_BX_books.getF1()));
		
		System.out.println("##################################################");
		/**
		 * good and books, some date is null
		 */
		System.out.println("*\n*\tLoading gold standard for good and books\n*");
		MatchingGoldStandard gsTest_good_books = new MatchingGoldStandard();
		gsTest_good_books.loadFromCSVFile(new File("data/goldstandard/goodread_bestbook_GS_test.csv"));
		//create a matching rule
		LinearCombinationMatchingRule<Book, Attribute> matchingRule_good_books = new LinearCombinationMatchingRule<>(     
		0.8); 
		matchingRule_good_books.activateDebugReport("data/output/debugResultsMatchingRule_good_books" + ".csv", 1000, gsTest_good_books);
		// add comparators
		//matchingRule_good_books.addComparator(new BookPagesComparatorDeviationSimilarity(), 0.1);
		matchingRule_good_books.addComparator(new BookDateComparator2Years(), 0.8);
		matchingRule_good_books.addComparator(new BookTitleComparatorJaccard(), 0.2);
		//create a blocker
		//NoBlocker<Book, Attribute> blocker_good_books = new NoBlocker<>();
		StandardRecordBlocker<Book, Attribute> blocker_good_books = new StandardRecordBlocker<Book, Attribute>(new BookBlockingKeyByTitleGenerator());
		blocker_good_books.setMeasureBlockSizes(true);
		blocker_good_books.collectBlockSizeData("data/output/debugResultsBlocking_good_books.csv", 100);
		//Initialize matching Engine
		MatchingEngine<Book, Attribute> engine_good_books = new MatchingEngine<>();
		//Execute the matching
		System.out.println("*\n*\tRunning identity resolution for good and books\n*");
		
		Processable<Correspondence<Book, Attribute>> correspondences_good_books = engine_good_books.runIdentityResolution(
				data_goodread_books, data_books_dataset, null, matchingRule_good_books,
				blocker_good_books);
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/good_books_correspondences.csv"), correspondences_good_books);
		System.out.println("*\n*\tEvaluating result for BX and books\n*");
		MatchingEvaluator<Book, Attribute> evaluator_good_books = new MatchingEvaluator<Book, Attribute>();
		Performance perfTest_good_books = evaluator_good_books.evaluateMatching(correspondences_good_books,
				gsTest_good_books);

		// print the evaluation result
		System.out.println("Books <-> Author");
		System.out.println(String.format(
				"Precision: %.4f",perfTest_good_books.getPrecision()));
		System.out.println(String.format(
				"Recall: %.4f",	perfTest_good_books.getRecall()));
		System.out.println(String.format(
				"F1: %.4f",perfTest_good_books.getF1()));
		System.out.println("##################################");
		/**
		 * good and BX
		 */
		System.out.println("*\n*\tLoading gold standard for good and books\n*");
		MatchingGoldStandard gsTest_BX_good = new MatchingGoldStandard();
		gsTest_BX_good.loadFromCSVFile(new File("data/goldstandard/crossing_bestbook_GS_test.csv"));
		//create a matching rule
		LinearCombinationMatchingRule<Book, Attribute> matchingRule_BX_good = new LinearCombinationMatchingRule<>(     
		0.7); 
		matchingRule_BX_good.activateDebugReport("data/output/debugResultsMatchingRule_BX_good" + ".csv", 1000, gsTest_BX_good);
		// add comparators
		//matchingRule_BX_good.addComparator(new BookPagesComparatorDeviationSimilarity(), 0.1);
		matchingRule_BX_good.addComparator(new BookDateComparator10Years(), 0.8);
		matchingRule_BX_good.addComparator(new BookTitleComparatorJaccard(), 0.2);
		//create a blocker
		//NoBlocker<Book, Attribute> blocker_good_books = new NoBlocker<>();
		StandardRecordBlocker<Book, Attribute> blocker_BX_good = new StandardRecordBlocker<Book, Attribute>(new BookBlockingKeyByTitleGenerator());
		blocker_BX_good.setMeasureBlockSizes(true);
		blocker_BX_good.collectBlockSizeData("data/output/debugResultsBlocking_BX_good.csv", 100);
		//Initialize matching Engine
		MatchingEngine<Book, Attribute> engine_BX_good = new MatchingEngine<>();
		//Execute the matching
		System.out.println("*\n*\tRunning identity resolution for good and books\n*");
		
		Processable<Correspondence<Book, Attribute>> correspondences_BX_good = engine_BX_good.runIdentityResolution(
				data_goodread_books, data_BX_Books, null, matchingRule_BX_good,
				blocker_BX_good);
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/BX_good_correspondences.csv"), correspondences_BX_good);
		System.out.println("*\n*\tEvaluating result for BX and books\n*");
		MatchingEvaluator<Book, Attribute> evaluator_BX_good = new MatchingEvaluator<Book, Attribute>();
		Performance perfTest_BX_good = evaluator_BX_good.evaluateMatching(correspondences_BX_good,
				gsTest_BX_good);

		// print the evaluation result
		System.out.println("Books <-> Author");
		System.out.println(String.format(
				"Precision: %.4f",perfTest_BX_good.getPrecision()));
		System.out.println(String.format(
				"Recall: %.4f",	perfTest_BX_good.getRecall()));
		System.out.println(String.format(
				"F1: %.4f",perfTest_BX_good.getF1()));
		
	}

}
