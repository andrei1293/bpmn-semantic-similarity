package model.similarity;

import model.util.FuzzySetUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.StmtIterator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ModelsSimilarity {
    private static final double Q_COEFF = 1.5;

    public double[] defineSemanticSimilarity(List<Model> models, Model model) {
        double[] semanticSimilarity = new double[models.size()];
        double semanticSimilaritySum = 0;

        for (int i = 0; i < models.size(); i++) {
            double distance = 1.0 - defineModelsSimilarity(models.get(i), model);

            if (distance < 10E-3)
                distance = 10E-3;

            semanticSimilarity[i] = 1.0 / Math.pow(distance, 1.0 / (Q_COEFF - 1.0));
            semanticSimilaritySum += semanticSimilarity[i];
        }

        for (int i = 0; i < models.size(); i++)
            semanticSimilarity[i] /= semanticSimilaritySum;

        return semanticSimilarity;
    }

    public double defineModelsSimilarity(Model first, Model second) {
        Set<RDFNode> firstSubjects = new HashSet<RDFNode>();
        Set<RDFNode> secondSubjects = new HashSet<RDFNode>();

        for (ResIterator i = first.listSubjects(); i.hasNext();)
            firstSubjects.add(i.nextResource());

        for (ResIterator i = second.listSubjects(); i.hasNext();)
            secondSubjects.add(i.nextResource());

        //Set<RDFNode> intersection = new HashSet<RDFNode>(firstSubjects);
        //intersection.retainAll(secondSubjects);

        Set<RDFNode> intersection = FuzzySetUtils.intersection(firstSubjects, secondSubjects);

        //System.out.println(firstSubjects + " union " + secondSubjects + " = " + intersection);

        double result = 0;

        for (RDFNode subject : intersection) {
            Set<RDFNode> firstProperties = new HashSet<RDFNode>();
            Set<RDFNode> secondProperties = new HashSet<RDFNode>();

            for (StmtIterator i = first.listStatements(subject.asResource(), null,
                    (RDFNode) null); i.hasNext();)
                firstProperties.add(i.nextStatement().getPredicate());

            for (StmtIterator i = second.listStatements(subject.asResource(), null,
                    (RDFNode) null); i.hasNext();)
                secondProperties.add(i.nextStatement().getPredicate());

            Set<RDFNode> firstObjects = new HashSet<RDFNode>();
            Set<RDFNode> secondObjects = new HashSet<RDFNode>();

            for (StmtIterator i = first.listStatements(subject.asResource(), null,
                    (RDFNode) null); i.hasNext();)
                firstObjects.add(i.nextStatement().getObject());

            for (StmtIterator i = second.listStatements(subject.asResource(), null,
                    (RDFNode) null); i.hasNext();)
                secondObjects.add(i.nextStatement().getObject());

            //System.out.println("property(" + subject + ") = " + firstProperties);
            //System.out.println("property(" + subject + ") = " + secondProperties);
            //System.out.println("object(" + subject + ") = " + firstObjects);
            //System.out.println("object(" + subject + ") = " + secondObjects);

            double propertiesSim;
            double objectsSim;

            if (firstProperties.isEmpty() || secondProperties.isEmpty())
                propertiesSim = 0;
            else
                propertiesSim = calculateSimilarityCoefficient(firstProperties, secondProperties);

            if (firstObjects.isEmpty() || secondObjects.isEmpty())
                objectsSim = 0;
            else
                objectsSim = calculateSimilarityCoefficient(firstObjects, secondObjects);


            result += propertiesSim + objectsSim;
        }

        return result == 0 ? 0 : result / (2.0 * intersection.size());
    }

    public abstract double calculateSimilarityCoefficient(Set<RDFNode> a, Set<RDFNode> b);
}
