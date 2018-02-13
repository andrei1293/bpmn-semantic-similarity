package app;

import model.load.ModelLoader;
import model.similarity.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.ResourceFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class App {
    private static String[] files = {
            "Process1.nt",
            "Process2.nt",
            "Process3.nt",
            "Process4.nt",
            "Process5.nt"
    };

    private static ModelsSimilarity[] coefficients = {
            new JaccardModelsSimilarity(),
            new DiceModelsSimilarity(),
            new KulchinskiyModelsSimilarity(),
            new SimpsonModelsSimilarity(),
            new BrownModelsSimilarity(),
            new OtiaiModelsSimilarity()
    };

    public static void main(String[] args) {
        List<Model> models = ModelLoader.loadListOfModelsFromFiles(files);
        int coefficientNumber = 1;

        System.out.println("Test similarity:");

        Set<RDFNode> firstSubjects = new HashSet<RDFNode>();
        Set<RDFNode> secondSubjects = new HashSet<RDFNode>();

        for (ResIterator i = models.get(3).listSubjects(); i.hasNext();)
            firstSubjects.add(i.nextResource());

        for (ResIterator i = models.get(4).listSubjects(); i.hasNext();)
            secondSubjects.add(i.nextResource());

        System.out.printf("Sim(A, B): %f\n\n", coefficients[0].calculateSimilarityCoefficient(firstSubjects, secondSubjects));

        for (ModelsSimilarity coefficient : coefficients) {
            System.out.printf("%d:\n", coefficientNumber);

            coefficientNumber++;

            System.out.printf("RDFSim(G%d, G%d) = %.2f\n", 1, 2,
                    coefficient.defineModelsSimilarity(models.get(0), models.get(1)));
            System.out.printf("RDFSim(G%d, G%d) = %.2f\n", 1, 3,
                    coefficient.defineModelsSimilarity(models.get(0), models.get(2)));
            System.out.printf("RDFSim(G%d, G%d) = %.2f\n", 2, 3,
                    coefficient.defineModelsSimilarity(models.get(1), models.get(2)));

            System.out.println("\nSemantic similarity matrix: ");

            for (Model model : models) {
                List<Model> tmpModels = new ArrayList<Model>(models);
                tmpModels.remove(model);

                double[] row = coefficient.defineSemanticSimilarity(tmpModels, model);

                for (Double value : row)
                    System.out.printf("%.2f ", value);

                System.out.println();
            }

            System.out.println();
        }
    }
}
