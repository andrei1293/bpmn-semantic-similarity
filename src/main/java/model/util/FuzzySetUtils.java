package model.util;

import org.apache.jena.rdf.model.RDFNode;

import java.util.HashSet;
import java.util.Set;

public class FuzzySetUtils {
    public static double ALPHA_CUT = 0.8;

    public static Set<RDFNode> intersection(Set<RDFNode> a, Set<RDFNode> b) {
        Set<RDFNode> result = new HashSet<RDFNode>();

        if (ALPHA_CUT <= 0) {
            result.addAll(a);
            result.addAll(b);
        } else if (ALPHA_CUT >= 1) {
            result.addAll(a);
            result.retainAll(b);
        } else {
            for (RDFNode aValue : a) {
                for (RDFNode bValue : b) {
                    String aString = aValue.toString();
                    String bString = bValue.toString();

                    if (sim(aString, bString) >= ALPHA_CUT) {
                        result.add(aValue);
                        result.add(bValue);
                    }
                }
            }
        }

        System.out.println(result);

        return result;
    }

    private static double sim(String a, String b) {
        int aLength = a.length();
        int bLength = b.length();

        return 1.0 - dist(a, b) / Math.max(aLength, bLength);
    }

    private static double dist(String S1, String S2) {
        int m = S1.length(), n = S2.length();
        int[] D1;
        int[] D2 = new int[n + 1];

        for(int i = 0; i <= n; i ++)
            D2[i] = i;

        for(int i = 1; i <= m; i ++) {
            D1 = D2;
            D2 = new int[n + 1];
            for(int j = 0; j <= n; j ++) {
                if(j == 0) D2[j] = i;
                else {
                    int cost = (S1.charAt(i - 1) != S2.charAt(j - 1)) ? 1 : 0;
                    if(D2[j - 1] < D1[j] && D2[j - 1] < D1[j - 1] + cost)
                        D2[j] = D2[j - 1] + 1;
                    else if(D1[j] < D1[j - 1] + cost)
                        D2[j] = D1[j] + 1;
                    else
                        D2[j] = D1[j - 1] + cost;
                }
            }
        }
        return D2[n];
    }
}
