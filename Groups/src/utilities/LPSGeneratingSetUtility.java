package utilities;

import java.util.ArrayList;
import java.util.List;
import lps.LPSConstructionUtility;
import groups.PGL2_PrimeField;

/**
 * This class consists of a single static method, getLPSGenerators(short p, short q), which 
 * creates a List&ltPGL2_PrimeField&gt of size p+1 whose elements constitute a
 * very special generating subset of PGL2(F_q) (or the subgroup
 * PSL2(F_q), depending on whether or not p is a square mod q).  Namely, they are the generating
 * sets that define
 * the so-called "Ramanujan graphs," which were introduced in the paper [LPS] as
 * Cayley graphs with respect to these generating sets. 
 * This method appeals to the static method:
 * <p>
 * &#160 &#160 <code>java.util.List&ltint[][]&gt lps.LPSGenSetRepsBuilder.getGeneratorReps(int p, int q)</code>,
 * </p>
 * for the construction of the generating set as a list of 2x2 int arrays, and 
 * then simply wraps the arrays as PGL2_PrimeField objects.
 * 
 * <p>
 * Note that the returned List will typically contain repeated elements when p
 * &ge; q<sup>2</sup>/4.
 * </p>
 * 
 * <p>
 * References:
 * </p>
 * <p>
 * &#160 &#160 [DSV] G. Davidoff, P. Sarnak, A. Valette, " Elementary Number Theory, Group Theory, and Ramanujan Graphs," 
 * Cambridge, LMSST 55, 2003.
 * </p>
 * <p>
 * &#160 &#160 [LPS] A. Lubotzky, R. Phillips, P Sarnak, "Ramanujan Graphs," Combinatorica, 1986.
 * </p>
 * 
 * @author pdokos
 */
public class LPSGeneratingSetUtility {
    
    private LPSGeneratingSetUtility() {}
    
    /**
     * This method produces a <code>List&ltPGL2_PrimeField&gt</code> of size p+1
     * containing the LPS generators for the group PGL2(F_q) (or the subgroup
     * PSL2(F_q), depending on whether or not p is a square mod q).
     * 
     * <p>
     * Note that the returned List will typically contain repeated 
     * elements when p &ge; q<sup>2</sup>/4.
     * </p>
     * 
     * @param p An <code>int</code> prime.
     * @param q A <code>short</code> integer prime different from p.
     * 
     * @return A <code>List&ltPGL2_PrimeField&gt</code> of size p+1
     * containing the LPS generators for the group PGL2(F_q) (or the subgroup
     * PSL2(F_q), depending on whether or not p is a square mod q).
     */
    public static List<PGL2_PrimeField> getLPSGenerators(int p, short q) {
        List<int[][]> generatorReps = LPSConstructionUtility.getLPSMatrixGeneratorReps(p, q); 
        List<PGL2_PrimeField> generatingSet = new ArrayList<PGL2_PrimeField>(generatorReps.size());
        for (int[][] mtxGenerator : generatorReps) {
            generatingSet.add(new PGL2_PrimeField(mtxGenerator, q));
        }
        return generatingSet;
    }
 
}