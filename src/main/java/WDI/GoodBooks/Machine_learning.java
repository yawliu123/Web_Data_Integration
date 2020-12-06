package WDI.GoodBooks;

import java.io.File;

import org.slf4j.Logger;

import WDI.Blocking.BookBlockingKeyByPagesGenerator;
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
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;

public class Machine_learning {

	private static final Logger logger = WinterLogManager.activateLogger("default");

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
		 * load gold standard
		 */
		System.out.println("*\n*\tLoading gold standard\n*");
		MatchingGoldStandard gsTraining_BX_books = new MatchingGoldStandard();
		gsTraining_BX_books.loadFromCSVFile(new File(
		"data/goldstandard/goodread_crossing_GS_train.csv"));

		/**
		 * BX-Books.xml and books-dataset.xml
		 */
       
		//###################
		/**
         * LMT
         */
//		String options[] = new String[] {"-B"};
//		String modelType = "LMT"; // use a logistic regression
		
		//###################
		//SimpleLogistic
//		String options[] = new String[] {"-S"};
//		String modelType = "SimpleLogistic";
		
		//###################
//		String options[] = new String[] {"-S"};
//		String modelType = "J48";
		
		//###################
//		String options[] = new String[] {"-M"};
//		String modelType = "SMO";
		
		//###################
		String options[] = new String[] {"-E"};
		String modelType = "KStar";
		
		WekaMatchingRule<Book, Attribute> matchingRule_BX_books = new WekaMatchingRule<>(0.9, modelType, options);
		matchingRule_BX_books.activateDebugReport("data/output/debugResultsMatchingRule_BX_books.csv", 1000,
				gsTraining_BX_books);

		matchingRule_BX_books.addComparator(new BookDateComparator10Years());
		matchingRule_BX_books.addComparator(new BookDateComparator2Years());
		matchingRule_BX_books.addComparator(new BookDateComparatorWeightedDateSimilarity());
		matchingRule_BX_books.addComparator(new BookPagesComparatorAbsoluteDifferenceSimilarity());
		matchingRule_BX_books.addComparator(new BookPagesComparatorDeviationSimilarity());
		matchingRule_BX_books.addComparator(new BookPagesComparatorUnadjustedDeviationSimilarity());
		matchingRule_BX_books.addComparator(new BookPublisherComparatorJaccard());
		matchingRule_BX_books.addComparator(new BookPublisherComparatorJaccardOnNGram());
		matchingRule_BX_books.addComparator(new BookPublisherComparatorLevenshtein());
		matchingRule_BX_books.addComparator(new BookTitleComparatorEqual());
		matchingRule_BX_books.addComparator(new BookTitleComparatorJaccard());
		matchingRule_BX_books.addComparator(new BookTitleComparatorLevenshtein());
		matchingRule_BX_books.addComparator(new BookTitleComparatorJaccardOnNGram());
		matchingRule_BX_books.addComparator(new BookAuthorComparatorMaximumOfContainment());
		matchingRule_BX_books.addComparator(new BookAuthorComparatorMaxSimilarity());
		matchingRule_BX_books.addComparator(new BookAuthorComparatorOverlapSimilarity());

		// train the matching rule's model
		System.out.println("*\n*\tLearning matching rule\n*");
		RuleLearner<Book, Attribute> learner_BX_books = new RuleLearner<>();
		learner_BX_books.learnMatchingRule(data_BX_Books, data_books_dataset, null, matchingRule_BX_books, gsTraining_BX_books);
		System.out.println(
				String.format("Matching rule of BX_books is:\n%s", matchingRule_BX_books.getModelDescription()));

		// create a blocker (blocking strategy)
		// StandardRecordBlocker<Book, Attribute> blocker = new
		// StandardRecordBlocker<Book, Attribute>(new
		// BookBlockingKeyByTitleGenerator());
		// SortedNeighbourhoodBlocker<Book, Attribute, Attribute> blocker = new
		// SortedNeighbourhoodBlocker<>(new BookBlockingKeyByDecadeGenerator(), 1);
		// blocker.collectBlockSizeData("data/output/debugResultsBlocking_BX_Books.csv", 100);
		//NoBlocker<Book, Attribute> blocker_BX_books = new NoBlocker<>();
		StandardRecordBlocker<Book, Attribute> blocker_BX_books = new StandardRecordBlocker<Book, Attribute>(new BookBlockingKeyByTitleGenerator());
		blocker_BX_books.setMeasureBlockSizes(true);
		blocker_BX_books.collectBlockSizeData("data/output/debugResultsBlocking_BX_books.csv", 100);

		// Initialize Matching Engine
		MatchingEngine<Book, Attribute> engine_BX_books = new MatchingEngine<>();

		// Execute the matching
		System.out.println("*\n*\tRunning identity resolution\n*");
		Processable<Correspondence<Book, Attribute>> correspondences_BX_books = engine_BX_books.runIdentityResolution(
				data_BX_Books, data_books_dataset, null, matchingRule_BX_books, blocker_BX_books);

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/BX_books.csv"), correspondences_BX_books);

		// load the gold standard (test set)
		System.out.println("*\n*\tLoading gold standard\n*");
		MatchingGoldStandard gsTest_BX_books = new MatchingGoldStandard();
		//here need to edit later, because there is no train dataset
		gsTest_BX_books.loadFromCSVFile(new File("data/goldstandard/goodread_crossing_GS_test.csv"));

		// evaluate your result
		System.out.println("*\n*\tEvaluating result\n*");
		MatchingEvaluator<Book, Attribute> evaluator_BX_books = new MatchingEvaluator<Book, Attribute>();
		Performance perfTest_BX_books = evaluator_BX_books.evaluateMatching(correspondences_BX_books, gsTest_BX_books);

		// print the evaluation result
		System.out.println("Book <-> Author");
		System.out.println(String.format("Precision: %.4f", perfTest_BX_books.getPrecision()));
		System.out.println(String.format("Recall: %.4f", perfTest_BX_books.getRecall()));
		System.out.println(String.format("F1: %.4f", perfTest_BX_books.getF1()));

		/**
		 * 
		 */
		/**
		 * BX-Books.xml and goodread_books.xml
		 */
		//load train
		MatchingGoldStandard gsTraining_BX_good = new MatchingGoldStandard();
		gsTraining_BX_good.loadFromCSVFile(new File(
				"data/goldstandard/crossing_bestbook_GS_train.csv"));
		
		WekaMatchingRule<Book, Attribute> matchingRule_BX_good = new WekaMatchingRule<>(0.9, modelType, options);
		matchingRule_BX_good.activateDebugReport("data/output/debugResultsMatchingRule_BX_good.csv", 1000,
				gsTraining_BX_good);

		matchingRule_BX_good.addComparator(new BookDateComparator10Years());
		matchingRule_BX_good.addComparator(new BookDateComparator2Years());
		matchingRule_BX_good.addComparator(new BookDateComparatorWeightedDateSimilarity());
		matchingRule_BX_good.addComparator(new BookPagesComparatorAbsoluteDifferenceSimilarity());
		matchingRule_BX_good.addComparator(new BookPagesComparatorDeviationSimilarity());
		matchingRule_BX_good.addComparator(new BookPagesComparatorUnadjustedDeviationSimilarity());
		matchingRule_BX_good.addComparator(new BookPublisherComparatorJaccard());
		matchingRule_BX_good.addComparator(new BookPublisherComparatorLevenshtein());
		matchingRule_BX_good.addComparator(new BookPublisherComparatorJaccardOnNGram());
		matchingRule_BX_good.addComparator(new BookTitleComparatorEqual());
		matchingRule_BX_good.addComparator(new BookTitleComparatorJaccard());
		matchingRule_BX_good.addComparator(new BookTitleComparatorLevenshtein());
		matchingRule_BX_good.addComparator(new BookTitleComparatorJaccardOnNGram());
		matchingRule_BX_good.addComparator(new BookAuthorComparatorMaximumOfContainment());
		matchingRule_BX_good.addComparator(new BookAuthorComparatorMaxSimilarity());
		matchingRule_BX_good.addComparator(new BookAuthorComparatorOverlapSimilarity());

		// train the matching rule's model
		System.out.println("*\n*\tLearning matching rule\n*");
		RuleLearner<Book, Attribute> learner_BX_good = new RuleLearner<>();
		learner_BX_good.learnMatchingRule(data_BX_Books, data_goodread_books, null, matchingRule_BX_good, gsTraining_BX_good);
		System.out.println(
				String.format("Matching rule of BX_good is:\n%s", matchingRule_BX_good.getModelDescription()));

		// create a blocker (blocking strategy)
		 StandardRecordBlocker<Book, Attribute> blocker_BX_good = new
		 StandardRecordBlocker<Book, Attribute>(new
		 BookBlockingKeyByTitleGenerator());
		// SortedNeighbourhoodBlocker<Book, Attribute, Attribute> blocker = new
		// SortedNeighbourhoodBlocker<>(new BookBlockingKeyByDecadeGenerator(), 1);
		// blocker.collectBlockSizeData("data/output/debugResultsBlocking_BX_good.csv", 100);
		//NoBlocker<Book, Attribute> blocker_BX_good = new NoBlocker<>();
		blocker_BX_good.setMeasureBlockSizes(true);
		blocker_BX_good.collectBlockSizeData("data/output/debugResultsBlocking_BX_good.csv", 100);

		// Initialize Matching Engine
		MatchingEngine<Book, Attribute> engine_BX_good = new MatchingEngine<>();

		// Execute the matching
		System.out.println("*\n*\tRunning identity resolution\n*");
		Processable<Correspondence<Book, Attribute>> correspondences_BX_good = engine_BX_good.runIdentityResolution(
				data_BX_Books, data_goodread_books, null, matchingRule_BX_good, blocker_BX_good);

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/BX_good.csv"), correspondences_BX_good);

		// load the gold standard (test set)
		System.out.println("*\n*\tLoading gold standard\n*");
		MatchingGoldStandard gsTest_BX_good = new MatchingGoldStandard();
		gsTest_BX_good.loadFromCSVFile(new File("data/goldstandard/crossing_bestbook_GS_test.csv"));

		// evaluate your result
		System.out.println("*\n*\tEvaluating result\n*");
		MatchingEvaluator<Book, Attribute> evaluator_BX_good = new MatchingEvaluator<Book, Attribute>();
		Performance perfTest_BX_good = evaluator_BX_good.evaluateMatching(correspondences_BX_good, gsTest_BX_good);

		// print the evaluation result
		System.out.println("Book <-> Author");
		System.out.println(String.format("Precision: %.4f", perfTest_BX_good.getPrecision()));
		System.out.println(String.format("Recall: %.4f", perfTest_BX_good.getRecall()));
		System.out.println(String.format("F1: %.4f", perfTest_BX_good.getF1()));
		
		/**
		 * books.dataset.xml and goodread_books.xml
		 * 
		 */		

		MatchingGoldStandard gsTraining_books_good = new MatchingGoldStandard();
		gsTraining_books_good.loadFromCSVFile(new File(
				"data/goldstandard/goodread_bestbook_GS_train.csv"));
		WekaMatchingRule<Book, Attribute> matchingRule_books_good = new WekaMatchingRule<>(0.9, modelType, options);
		matchingRule_books_good.activateDebugReport("data/output/debugResultsMatchingRule_books_good.csv", 1000,
				gsTraining_books_good);

		matchingRule_books_good.addComparator(new BookDateComparator10Years());
		matchingRule_books_good.addComparator(new BookDateComparator2Years());
		matchingRule_books_good.addComparator(new BookDateComparatorWeightedDateSimilarity());
		matchingRule_books_good.addComparator(new BookPagesComparatorAbsoluteDifferenceSimilarity());
		matchingRule_books_good.addComparator(new BookPagesComparatorDeviationSimilarity());
		matchingRule_books_good.addComparator(new BookPagesComparatorUnadjustedDeviationSimilarity());
		matchingRule_books_good.addComparator(new BookPublisherComparatorJaccard());
		matchingRule_books_good.addComparator(new BookPublisherComparatorLevenshtein());
		matchingRule_books_good.addComparator(new BookPublisherComparatorJaccardOnNGram());
		matchingRule_books_good.addComparator(new BookTitleComparatorEqual());
		matchingRule_books_good.addComparator(new BookTitleComparatorJaccard());
		matchingRule_books_good.addComparator(new BookTitleComparatorLevenshtein());
		matchingRule_books_good.addComparator(new BookTitleComparatorJaccardOnNGram());
		matchingRule_books_good.addComparator(new BookAuthorComparatorMaximumOfContainment());
		matchingRule_books_good.addComparator(new BookAuthorComparatorMaxSimilarity());
		matchingRule_books_good.addComparator(new BookAuthorComparatorOverlapSimilarity());


		// train the matching rule's model
		System.out.println("*\n*\tLearning matching rule\n*");
		RuleLearner<Book, Attribute> learner_books_good = new RuleLearner<>();
		learner_books_good.learnMatchingRule(data_books_dataset, data_goodread_books, null, matchingRule_books_good, gsTraining_books_good);
		System.out.println(
				String.format("Matching rule of books_good is:\n%s", matchingRule_books_good.getModelDescription()));

		// create a blocker (blocking strategy)
		 StandardRecordBlocker<Book, Attribute> blocker_books_good = new
		 StandardRecordBlocker<Book, Attribute>(new
		 BookBlockingKeyByPagesGenerator());
		// SortedNeighbourhoodBlocker<Book, Attribute, Attribute> blocker = new
		// SortedNeighbourhoodBlocker<>(new BookBlockingKeyByDecadeGenerator(), 1);
		// blocker.collectBlockSizeData("data/output/debugResultsBlocking_books_good.csv", 100);
		//NoBlocker<Book, Attribute> blocker_books_good = new NoBlocker<>();
		blocker_books_good.setMeasureBlockSizes(true);
		blocker_books_good.collectBlockSizeData("data/output/debugResultsBlocking_books_good.csv", 100);

		// Initialize Matching Engine
		MatchingEngine<Book, Attribute> engine_books_good = new MatchingEngine<>();

		// Execute the matching
		System.out.println("*\n*\tRunning identity resolution\n*");
		Processable<Correspondence<Book, Attribute>> correspondences_books_good = engine_books_good.runIdentityResolution(
				data_books_dataset, data_goodread_books, null, matchingRule_books_good, blocker_books_good);

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/books_good.csv"), correspondences_books_good);

		// load the gold standard (test set)
		System.out.println("*\n*\tLoading gold standard\n*");
		MatchingGoldStandard gsTest_books_good = new MatchingGoldStandard();
		gsTest_books_good.loadFromCSVFile(new File("data/goldstandard/goodread_bestbook_GS_test.csv"));

		// evaluate your result
		System.out.println("*\n*\tEvaluating result\n*");
		MatchingEvaluator<Book, Attribute> evaluator_books_good = new MatchingEvaluator<Book, Attribute>();
		Performance perfTest_books_good = evaluator_books_good.evaluateMatching(correspondences_books_good, gsTest_books_good);

		// print the evaluation result
		System.out.println("Book <-> Author");
		System.out.println(String.format("Precision: %.4f", perfTest_books_good.getPrecision()));
		System.out.println(String.format("Recall: %.4f", perfTest_books_good.getRecall()));
		System.out.println(String.format("F1: %.4f", perfTest_books_good.getF1()));
		
	}

}
