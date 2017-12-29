package model.load;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.util.ArrayList;
import java.util.List;

public class ModelLoader {

    public static Model loadModelFromFile(String fileName) {
        Model model = ModelFactory.createDefaultModel();
        model.read("triplestore\\" + fileName, "N-TRIPLES");

        return model;
    }

    public static List<Model> loadListOfModelsFromFiles(String[] fileNames) {
        List<Model> models = new ArrayList<Model>();

        for (String fileName : fileNames)
            models.add(loadModelFromFile(fileName));

        return models;
    }
}
