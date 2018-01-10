package app;

import model.load.ModelLoader;
import model.similarity.*;
import org.apache.jena.rdf.model.Model;

import java.util.ArrayList;
import java.util.List;

public class App {
    private static String[] files = {
            "Process1.nt",
            "Process2.nt",
            "Process3.nt"
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
