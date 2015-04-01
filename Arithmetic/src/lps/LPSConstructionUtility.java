package lps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import basic_operations.Arithmetic;
import quaternion.IntegerThreeSpaceUtility;
import quaternion.LipschitzQuaternion;
import quaternion_private.RamificationIndexedReduction;
import quaternion.ProjectiveReducedQuaternion;
import quaternion.QuaternionToMatrixProjector;

/**
 * This class contains several static methods for producing LPS generating sets 
 * for the finite group PGL2(F_q) (or PSL2(F_q)) in various forms.  These 
 * generating sets, first defined in the paper [LPS], are extraordinary 
 * for the reason that the resulting Cayley graphs are optimal expander graphs, 
 * i.e. they are Ramanujan graphs.  Methods for 
 * producing the set of Gerritzen-Van der Put generators (from which the LPS 
 * generating sets are derived) for the free subgroup (denoted by &Lambda;(2) in
 * [GVdP] and [LPS]) of the p-integral projective quaternions are also included.
 * 
 * <p>
 * These generating sets possess symmetry under the natural action by the 
 * cube/octahedral symmetry group acting on the imaginary component of the 
 * quaternions.  Thus, the generating sets can be organized in a compressed form
 * by listing only the canonical representatives lying in a fundamental region, 
 * as is done by the methods getFundamentalGVdPGenerators and 
 * getFundamentalLPSQuaternionGenerators.
 * The representatives in the returned collections are ordered by the 
 * lexicographic ordering on the quaternion entries. 
 * </p>
 * 
 * <p>
 * The methods getGVdPGenerators and getLPSQuaternionGenerators return the <em>full</em>
 * respective generating sets.  The elements of the returned collections are listed by 
 * concatenating the orbits (whose elements are also ordered in a consistent way) in the same
 * order as the fundamental representatives.  The method 
 * getOrbitPartitionedLPSGenerators returns the full LPS generating set as a List
 * of individual orbits.
 * </p>
 * 
 * <p>
 * Note that the LPS methods of this library return Integer-valued Maps on the 
 * quaternion elements rather than Lists.  This is because, strictly speaking, for 
 * arbitrary pairs p and q, the LPS graphs are <em>weighted graphs</em>, where 
 * the weight of a generator is defined as the number of GVdP generators lying above it (weights
 * bigger than 1 occur when p&gtq<sup>2</sup>/4).  
 * Despite not being a List, a traversal through the entries of the returned map
 * will be in the order described (which is accomplished by utilizing a 
 * LinkedHashMap in the implementation). 
 * </p>
 * 
 * <p>
 * The primary reason for incorporating the symmetry aspects into this library (though 
 * reducing the construction and organizing the elements of the generating sets 
 * are good enough reasons alone), is the relevance to the <em>graph 
 * symmetry</em> of the resulting Cayley graphs.  In particular, the octahedral 
 * action on imaginary component of defines fixed-point graph symmetry 
 * of the Cayley graphs.  This is 
 * due, in short, to the fact that this action arises from the conjugation action, 
 * x-->gxg^(-1), by a certain finite subgroup of the projective quaternion unit group
 * which is isomorphic to the octahedral group (see [D]).
 * </p>
 * 
 * <p>
 * References:
 * </p>
 * </p>
 * <p>
 * &#160 &#160 [D] P. Dokos, "Automorphisms of LPS Graphs," 2013. 
 * </p>
 * <p>
 * &#160 &#160 [DSV] G. Davidoff, P. Sarnak, A. Valette, "Elementary Number Theory, Group Theory, and Ramanujan Graphs," 
 * Cambridge, LMSST 55, 2003.
 * </p>
 * <p>
 * &#160 &#160 [GVdP] L. Gerritzen, M. Van der Put, "Schottky Groups and Mumford Curves," Springer LNM 817, 1980". 
 * </p>
 * <p>
 * &#160 &#160 [LPS] A. Lubotzky, R. Phillips, P Sarnak, "Ramanujan Graphs," Combinatorica, 1986.
 * </p>
 *
 * 
 * @author pdokos
 */
public class LPSConstructionUtility {

    private LPSConstructionUtility() {}
    
    /**
     * 
     * Returns a list of representatives for the GVdP 
     * generators that lie in a <em>fundamental region</em> under the action of the  
     * BC3 reflection group.  The elements of the list are LipschitzQuaternions of
     * norm p with distinguished real part (see [GVdP] for details) and imaginary 
     * part lying in the distinguished chamber x3 &ge; x2 &ge; x1 &ge; 0, where x1, x2, x3
     * are the coefficients of i, j, k respectively.  The elements of the list
     * are ordered lexicographically by their entries.
     * 
     * @param p an odd <code>int</code> prime
     * @return A list of representatives for the GVdP 
     * generators that lie in a fundamental region under the action of the  
     * BC3 reflection group
     */
    public static List<LipschitzQuaternion> getFundamentalGVdPGenerators(int p) {
        if (Arithmetic.isPrime(p) && p > 2) {
            double pSqrt = Math.sqrt(p);
            List<LipschitzQuaternion> reps = new ArrayList<LipschitzQuaternion>();
            for (int a = ((p - 3) / 2) % 2; a <= pSqrt; a += 2) {
                List<int[]> domain = IntegerThreeSpaceUtility.getFundamentalIntegerPoints(p - (a * a));
                for (int[] point : domain) { //System.out.println(a + " " + point[0] + " " + point[1] + " " + point[2]);
                    reps.add(new LipschitzQuaternion(new int[]{a, point[0], point[1], point[2]}));
                }
            }
            return reps;
        }
        return null;
    }
    
    /**
     * Returns a Map whose key-set consists of the LPS quaternion generators that
     * lie in a <em>fundamental region</em> for the action of the BC3 reflection
     * group acting on the Projective unit quaternions modulo the prime q.  The
     * Integer values associated with the generators are their 
     * weights, defined as the sum of the ramification indices of all orbits of 
     * the GVdP generating set that cover the posOrbit of the given generator mod q.
     * The entries of the Map are <em>linked</em> so that an iteration
     * over the entries (or the key-set) of the Map through its Iterator 
     * is a lexicographically ordered traversal.
     * 
     * @param p any odd <code>int</code> prime
     * @param q any odd <code>int</code> prime different from q
     * @return A Map whose key-set consist of the LPS quaternion generators 
     * lying in a fundamental region for the action of the BC3 finite reflection
     * group acting on the Projective unit quaternions modulo the prime q.
     */
    public static Map<ProjectiveReducedQuaternion, Integer> getFundamentalLPSQuaternionGenerators(int p, int q) {
        if (Arithmetic.isPrime(p) && Arithmetic.isPrime(q) && p > 2 && q > 2 && p != q) {//&& (q % 4 == 1) && (p % 4 == 1) ) { //&& (q > 2 * pSqrt)
            Map<ProjectiveReducedQuaternion, Integer> reps = new HashMap<ProjectiveReducedQuaternion, Integer>();
            List<LipschitzQuaternion> fundGVdPGens = getFundamentalGVdPGenerators(p);
            for (LipschitzQuaternion lipQuat : fundGVdPGens) {
                RamificationIndexedReduction ramifFundRep = new RamificationIndexedReduction(lipQuat, q);
                ProjectiveReducedQuaternion fundRep = (new ProjectiveReducedQuaternion(ramifFundRep.getReduction())).getFundamentalRepresentative();
                Integer ramIndex = ramifFundRep.getRamificationIndex();
                if (reps.containsKey(fundRep))
                    reps.put(fundRep, reps.get(fundRep)+ramIndex);
                else
                    reps.put(fundRep, ramIndex);
            }
            return createSortedMap(reps);
        }
        return null;
    }
    
    private static <S extends Comparable<S>, T> Map<S, T> createSortedMap(Map<S, T> map) {
        List<S> sortedKeys = new LinkedList<S>(map.keySet());
        Collections.sort(sortedKeys);
        Map<S, T> sortedMap = new LinkedHashMap<S, T>();
        for (S generator : sortedKeys) {
            sortedMap.put(generator, map.get(generator));
        }
        return sortedMap;
    }
    
    /**
     * Returns a full list of the Geritzen-Van der Put generators for the 
     * free subgroup, denoted by &Lambda;(2) in [GVdP] and [LPS], of the group of p-integral
     * projective quaternions.  The generators are 
     * represented as Lipschitz quaternions and listed by consecutive orbits 
     * under the action of the octahedral (rotation) group. 
     * 
     * @param p an odd <code>int</code> prime
     * @return A full list of the Geritzen-Van der Put generators for the 
     * free-group of p-integral projective quaternions.
     */
    public static List<LipschitzQuaternion> getGVdPGenerators(int p) {
        List<LipschitzQuaternion> fundGens = getFundamentalGVdPGenerators(p);
        List<LipschitzQuaternion> genSet = new ArrayList<LipschitzQuaternion>();
        for (LipschitzQuaternion fundGen : fundGens) {
            genSet.addAll(fundGen.getProjectiveCoxeterOrbit());
        }
        return genSet;
    }
    
    /**
     * Returns a Map whose key-set consists of the full set of LPS 
     * <em>quaternion</em> generators for the group of Projective Quaternion 
     * Units mod q (or the subgroup of norm 1 units).  The
     * Integer value associated with a generator is its 
     * weight, defined as the number of GVdP generators that lie above it.
     * The entries of the Map are <em>linked</em> so that an iteration
     * over the entries (or the key-set) of the Map through its Iterator 
     * traverses the orbits consecutively in the lexicographical order on the
     * fundamental representatives.  The ordering on the individual orbits is
     * defined by the method ProjectiveReducedQuaternion.getCoxeterOrbit. 
     * 
     * @param p an odd <code>int</code> prime
     * @param q an odd <code>int</code> prime different from p.
     * @return Returns a full list of the LPS generators for the group of Projective
     * Quaternion Units mod q (or the subgroup of norm 1 units). 
     */
    public static Map<ProjectiveReducedQuaternion, Integer> getLPSQuaternionGenerators(int p, int q) {
        Map<ProjectiveReducedQuaternion, Integer> fundamentalLPSQuaternionGenerators = getFundamentalLPSQuaternionGenerators(p, q);
        if (fundamentalLPSQuaternionGenerators != null) {
            Map<ProjectiveReducedQuaternion, Integer> generators = new LinkedHashMap<ProjectiveReducedQuaternion, Integer>();
            for (Map.Entry<ProjectiveReducedQuaternion, Integer> entry : fundamentalLPSQuaternionGenerators.entrySet()) {
                for (ProjectiveReducedQuaternion image : entry.getKey().getCoxeterOrbit()) {
                    generators.put(image, entry.getValue());
                }
            }
            return generators;
        }
        return null;
    }
    
    /**
     * Returns a partitioned map, in the form of a List of Maps, whose key-sets 
     * together constitute the full set of LPS quaternion generators 
     * for the group of Projective Quaternion 
     * Units mod q (or the subgroup of norm 1 units), with partitioning of the
     * generating set by orbits under the <em>octahedral</em> action.  The
     * Integer value associated with a given generator is its 
     * weight, defined as the number of GVdP generators that lie above it.
     * The entries (orbits) of the List are ordered by the lexicographical order on the
     * fundamental representatives, with conjugate orbits listed successively.  
     * The Entries of each Map are <em>linked</em> so that an iteration
     * over the Entries of the Map via its Iterator 
     * traverses the orbit in the same order
     * defined by the method ProjectiveReducedQuaternion.getCoxeterOrbit. 
     * 
     * @param p an odd <code>int</code> prime
     * @param q an odd <code>int</code> prime different from p.
     * @return A partitioned map, in the form of a List of Maps, whose key-sets 
     * together consist of the full set of LPS quaternion generators 
     * for the group of Projective Quaternion 
     * Units mod q (or the subgroup of norm 1 units).
     */
    public static List<Map<ProjectiveReducedQuaternion, Integer>> getOrbitPartitionedLPSGenerators(int p, int q) {
        List<Map<ProjectiveReducedQuaternion, Integer>> genSet = new ArrayList<Map<ProjectiveReducedQuaternion, Integer>>();
        Map<ProjectiveReducedQuaternion, Integer> fundamentalLPSQuaternionGenerators = getFundamentalLPSQuaternionGenerators(p, q);
        if (fundamentalLPSQuaternionGenerators != null) {
            for (Map.Entry<ProjectiveReducedQuaternion, Integer> entry : fundamentalLPSQuaternionGenerators.entrySet()) {
                int[] orbClass = entry.getKey().getCoxeterOrbitClass();
                List<ProjectiveReducedQuaternion> coxeterOrbit = entry.getKey().getCoxeterOrbit();
                Integer weight = entry.getValue();
                
                if (orbClass[0]==1 && orbClass[1]==1) {
                    Map<ProjectiveReducedQuaternion, Integer> octOrbit = new LinkedHashMap<ProjectiveReducedQuaternion, Integer>();
                    Map<ProjectiveReducedQuaternion, Integer> conjOrbit = new LinkedHashMap<ProjectiveReducedQuaternion, Integer>();
                    for (int i=0; i<coxeterOrbit.size(); i++) {
                        if (i<coxeterOrbit.size()/2) {
                            octOrbit.put(coxeterOrbit.get(i), weight);
                        } else {
                            conjOrbit.put(coxeterOrbit.get(i), weight);
                        }
                    }
                    genSet.add(octOrbit);
                    genSet.add(conjOrbit);
                } else {
                    Map<ProjectiveReducedQuaternion, Integer> orbit = new LinkedHashMap<ProjectiveReducedQuaternion, Integer>();
                    for (ProjectiveReducedQuaternion image : coxeterOrbit) {
                        orbit.put(image, weight);
                    }
                    genSet.add(orbit);
                }
            }
            return genSet;
        }
        return null;
    }
    
    /**
     * This method constructs a List of p+1 2x2 <code>int</code> arrays with entries in 0, 1,
     * ..., q-1, where the parameters p and q are any two distinct odd primes.
     * Regarded as elements of the group PGL2(F_q), they constitute the set of
     * generators for the group (or the subgroup PSL2(F_q), depending on whether
     * or not p is a square mod q), that define the so-called "Ramanujan graphs"
     * which were introduced in the paper [LPS] as Cayley graphs with respect to
     * this remarkable generating set. The implementation is based on the
     * presentation given in [DSV].
     * 
     * <p>
     * Note that the returned List will typically contain repeated elements when
     * p &ge; q<sup>2</sup>/4.
     * </p>
     *
     * @param p an odd integer prime
     * @param q an odd integer prime different form p
     * @return A list of the LPS matrix generators.
     */
    public static List<int[][]> getLPSMatrixGeneratorReps(int p, int q) {
        Map<ProjectiveReducedQuaternion, Integer> lpsQuaternionGenerators = getLPSQuaternionGenerators(p, q);
        List<int[][]> lpsMatrixGenerators = new ArrayList<int[][]>();
        QuaternionToMatrixProjector projector = new QuaternionToMatrixProjector(q);
        for (Map.Entry<ProjectiveReducedQuaternion, Integer> entry : lpsQuaternionGenerators.entrySet()) {
            //int[][] nextElement = projector.pi(entry.getKey());
            for (int i=1; i<=entry.getValue(); i++) {
                lpsMatrixGenerators.add(projector.pi(entry.getKey()));
            }
        }
        return lpsMatrixGenerators;
    }
        
}