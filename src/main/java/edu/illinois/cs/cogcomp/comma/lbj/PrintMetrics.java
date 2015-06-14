package edu.illinois.cs.cogcomp.comma.lbj;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import edu.illinois.cs.cogcomp.comma.Comma;
import edu.illinois.cs.cogcomp.comma.utils.EvaluateDiscrete;
import edu.illinois.cs.cogcomp.lbjava.classify.Classifier;
import edu.illinois.cs.cogcomp.lbjava.classify.TestDiscrete;
import edu.illinois.cs.cogcomp.lbjava.learn.TestingMetric;
import edu.illinois.cs.cogcomp.lbjava.parse.Parser;

public class PrintMetrics extends EvaluateDiscrete implements TestingMetric {
	int iteration = 0;
	int total_iterations;
	String outputFile;


    public PrintMetrics(int total_iterations){
        super();
        this.total_iterations = total_iterations;
    }

	public PrintMetrics(int total_iterations, String outputFile){
		super();
		this.total_iterations = total_iterations;
		this.outputFile = outputFile;
	}
	
	public String getName() {
		return "Accuracy. In addition to reporting accuracy, this class will also print the result of lbjava.classify.TestDiscrete";
	}
	
	@Override
	public double test(Classifier classifier, Classifier oracle, Parser parser) {
		Comma.setGold(false);
//		TestDiscrete evaluator = TestDiscrete.testDiscrete(classifier, oracle, parser);
		
		iteration++;
		EvaluateDiscrete evaluator = EvaluateDiscrete.evaluateDiscrete(classifier, oracle, parser);
		reportAll(evaluator);
		if(iteration == total_iterations){
			printPerformance(System.out);
			printConfusion(System.out);
            if (outputFile != null) {
                try {
                    PrintStream experimentResults = new PrintStream(
                            new FileOutputStream(outputFile, true));
                    printPerformance(experimentResults);
                    experimentResults.println();
                    experimentResults.println();
                    printConfusion(experimentResults);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
		}

		Comma.setGold(true);
		return evaluator.getOverallStats()[0];
	}
}
