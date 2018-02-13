package model.similarity;

import model.util.FuzzySetUtils;
import org.apache.jena.rdf.model.RDFNode;

import java.util.HashSet;
import java.util.Set;

public class SimpsonModelsSimilarity extends ModelsSimilarity {

    public double calculateSimilarityCoefficient(Set<RDFNode> a, Set<RDFNode> b) {
        Set<RDFNode> union = new HashSet<RDFNode>();
        union.addAll(a);
        union.addAll(b);

        //Set<RDFNode> intersection = new HashSet<RDFNode>(a);
        //intersection.retainAll(b);

        Set<RDFNode> intersection = FuzzySetUtils.intersection(a, b);

        return ((double) intersection.size()) / (double) Math.min(a.size(), b.size());
    }
}
