package model.similarity;

import model.util.FuzzySetUtils;
import org.apache.jena.rdf.model.RDFNode;

import java.util.HashSet;
import java.util.Set;

public class KulchinskiyModelsSimilarity extends ModelsSimilarity {

    public double calculateSimilarityCoefficient(Set<RDFNode> a, Set<RDFNode> b) {
        Set<RDFNode> union = new HashSet<RDFNode>();
        union.addAll(a);
        union.addAll(b);

        //Set<RDFNode> intersection = new HashSet<RDFNode>(a);
        //intersection.retainAll(b);

        Set<RDFNode> intersection = FuzzySetUtils.intersection(a, b);

        return (((double) intersection.size()) / 2) * ((1 / ((double) a.size())) + (1 / ((double) b.size())));
    }
}
