/*
 * Copyright (C) 2015 Pericles Dokos
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package utilities;

import fastgroups.PGL2ByteField;
import finitefields.ByteField;
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
    public static List<PGL2ByteField> getPGL2ByteFieldGenerators(int p, ByteField f_q) {
        List<int[][]> generatorReps = LPSConstructionUtility.getLPSMatrixGeneratorReps(p, f_q.getOrder()); 
        List<PGL2ByteField> generatingSet = new ArrayList<PGL2ByteField>(generatorReps.size());
        for (int[][] mtxGenerator : generatorReps) {
            generatingSet.add(new PGL2ByteField((byte) mtxGenerator[0][0], (byte) mtxGenerator[0][1], (byte) mtxGenerator[1][0], (byte) mtxGenerator[1][1], f_q));
        }
        return generatingSet;
    }
 
}
