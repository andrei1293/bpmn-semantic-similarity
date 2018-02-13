package model.similarity;

import model.util.FuzzySetUtils;
import org.apache.jena.rdf.model.RDFNode;

import java.util.HashSet;
import java.util.Set;

public class DiceModelsSimilarity extends ModelsSimilarity {

    public double calculateSimilarityCoefficient(Set<RDFNode> a, Set<RDFNode> b) {
        Set<RDFNode> union = new HashSet<RDFNode>();
        union.addAll(a);
        union.addAll(b);

        //Set<RDFNode> intersection = new HashSet<RDFNode>(a);
        //intersection.retainAll(b);

        Set<RDFNode> intersection = FuzzySetUtils.intersection(a, b);

        return (2 * ((double) intersection.size())) / (double) (a.size() + b.size());
    }
}
