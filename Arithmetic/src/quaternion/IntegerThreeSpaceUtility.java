package quaternion;

import java.util.ArrayList;
import java.util.List;
import basic_operations.Arithmetic;

/**
 * A utility class for determining the solution sets to the equation x<sup>2</sup> + y<sup>2</sup> + z<sup>2</sup> = n,
 * and exploiting the symmetry of these sets under the finite reflection group
 * generated by the reflections along the three coordinate planes  x=0, y=0, z=0, 
 * together with the six planes x=&plusmn;y, y=&plusmn;z, z=&plusmn;x. 
 * This group is also known as the <em>Coxeter group of type BC3</em>, and the rotational
 * subgroup (which is of index 2) is also known as the <em>octahedral group</em>.
 * 
 * <p>
 * Points in integer 3-space are modeled simply by <code>int</code> arrays of length 3.
 * A <em>fundamental region</em> for the group action is taken to be the set 
 * 0 &le; x &le; y &le; z, and an ordering of the points in this region is 
 * defined by the <em>lexicographic ordering</em>.
 * </p>
 * <p>
 * The set of integer points can be classified by their orbit type under the
 * Coxeter group as follows.  The type of a given point is represented as a pair
 * of integers (t, s) with t ranging among the values {1, 2, 3, 4},
 * and s ranging among {1, 2, 3} for t=2 or t=3, while s=1 when t=1 or t=4 (no subtype).
 * Let F1, F2, F3 be the faces of the fundamental region contained in the planes
 * x=0, y=x, and z=y respectively, and let L1 = F1 &cap; F2, L2 = F1 &cap; F3,
 * L3 = F2 &cap; F3.</br>
 * For an integer triplet p with fundamental representative p', we say that p is of type (t, s) with:</br>
 * &#160&#160 t=4 : if p' is the origin </br>
 * &#160&#160 t=3 : if p' is on one and only one of the rays L1, L2, L3</br>
 * &#160&#160 t=2 : if p' is on one and only one of the faces F1, F2, F3</br>
 * &#160&#160 t=1 : if p' is an interior point of the fundamental region (i.e in the region 0 &lt x &lt y &lt z)
 * </br>
 * For t=2 and t=3, the subtype s is defined by the index on F and L respectively.
 * </p>
 * <p>
 * The method getCoxeterOrbitClass(p) determines the type (t, s) of the point p
 * and returns it as an int[] of length 2.
 * </p>
 * 
 * 
 * 
 * @author pdokos
 */
public class IntegerThreeSpaceUtility {

    private IntegerThreeSpaceUtility() {}
    
    /**
     * Returns the number of integer solutions to the equation x<sup>2</sup> + y<sup>2</sup> + z<sup>2</sup> = n.
     * 
     * @param n an <code>int</code> value >= 0.
     * @return The number of integer point on the sphere of radius sqrt(n).
     */
    public static int getNumPoints(int n) {

        if (n == 0) {
            return 1;
        }

        List<int[]> domain = getFundamentalIntegerPoints(n);
        int numPts = 0;

        for (int[] point : domain) {
            numPts += getCoxeterOrbitSize(point);
        }

        return numPts;
    }

    /**
     * Returns a List of the integer solutions to the equation 
     * x<sup>2</sup> + y<sup>2</sup> + z<sup>2</sup> = n lying in the fundamental region 0 &le; x &le; y &le; z.
     * The elements of the list are ordered lexicographically.
     *
     * @param n an <code>int</code> value > 0.
     * @return A fundamental domain for the set of integer points on the sphere
     * of radius sqrt(n) under the action of BC3 finite reflection group.
     */
    public static List<int[]> getFundamentalIntegerPoints(int n) {

        List<int[]> domain;

        int resMod4 = Arithmetic.reduce(n, 4);

        if (resMod4 == 0) {
            domain = getFundamentalIntegerPoints(n / 4);
            scalePoints(2, domain);//domain.scale(2);
            return domain;
        }

        domain = new ArrayList<int[]>();
        int x;
        int delta;
        int deltaSqA;
        int deltaSqB;

        if (resMod4 == 3) {
            x = 1;
            delta = 2;
            deltaSqA = 4;
            deltaSqB = 4;
        } else {
            x = 0;
            delta = 1;
            deltaSqA = 2;
            deltaSqB = 1;
        }

        int xSq = x * x;
        int N1 = n / 3;
        while (xSq <= N1) {
            int y = x;
            int ySq = xSq;
            int diff1 = n - xSq;
            int N2 = (diff1) / 2;
            while (ySq <= N2) {
                int zVal = Arithmetic.perfSqrt(diff1 - ySq);
                if (zVal != -1) {
                    domain.add(new int[]{x, y, zVal});//domain.addPoint(x, y, zVal);
                }
                ySq += deltaSqA * y + deltaSqB;
                y += delta;
            }
            xSq += deltaSqA * x + deltaSqB;
            x += delta;
        }
        return domain;
    }

    private static List<int[]> expandFundamentalRegion(List<int[]> region) {
        List<int[]> expandedRegion = new ArrayList<int[]>();
        for (int[] pt : region) {
            List<int[]> orbit = getCoxeterOrbit(pt);
            for (int[] image : orbit)
            expandedRegion.add(image);
        }
        return expandedRegion;
    }

    /**
     * Returns a full List of the integer solutions to the equation x<sup>2</sup> + y<sup>2</sup> + z<sup>2</sup> = n.
     * 
     * @param n an <code>int</code> value >= 0.
     * @return A List of the integer points on the sphere of radius sqrt(n).
     */
    public static List<int[]> getIntegerPoints(int n) {
        return expandFundamentalRegion(getFundamentalIntegerPoints(n));
    }

    /**
     * Returns a List of the points contained in the <em>octahedral</em> orbit of the
     * <em>fundamental representative</em> of the given point.  
     * Note that the returned set either contains the given point or its antipodal
     * point.
     * For a point which lies on any of the planes of reflection, the octahedral 
     * orbit is necessarily closed under the antipodal map, 
     * and in this case, a List of representatives modulo the 
     * antipodal map is returned.  The elements of the List are ordered in a consistent way
     * according to the orbit type.
     * </br></br>
     * Note that the full Coxeter orbit
     * of the given point consists of the union of this set with the 
     * antipodal map applied to it.
     * 
     * @param pt any <code>int</code>[] of length 3.
     * @return A List of the points contained in the octahedral orbit of the
     * <em>fundamental representative</em> of the given point.  For points which lie
     * on any of the planes of reflection, a List of representatives modulo the 
     * antipodal map is returned.
     */
    public static List<int[]> getPositiveCoxeterOrbit(int[] pt) {
        int[] fundRep = getFundamentalRepresentative(pt);
        int[] orbitType = getCoxeterOrbitClass(fundRep);
        List<int[]> orbit = new ArrayList<int[]>();

        if (orbitType[0] == 4) { //0 0 0
            orbit.add(fundRep); //{0, 0, 0}
            
        } else if (orbitType[0] == 3) {
            
            if (orbitType[1] == 1) { // 0 0 a 
                for (int i = 0; i < 3; i++) {  // {0, 0, a} {a, 0, 0} {0, a, 0}
                    orbit.add(tau(i, fundRep));
                }
                
            } else if (orbitType[1] == 2) { // 0 a a 
                for (int i = 0; i < 3; i++) {  //{0, a, a} {a, 0, a} {a, a, 0}
                    orbit.add(tau(i, fundRep));
                }
                int[] rep=axialHalfRotation(2, fundRep);
                for (int i = 0; i < 3; i++) { //{0, -a, a} {a, 0, -a} {-a, a, 0})
                    orbit.add(tau(i, rep));
                }

            } else { //a a a 
                orbit.add(fundRep); //{a, a, a}
                int[] rep = axialQuarterRotation(2, fundRep);//{-a, a, a}
                for (int i = 0; i < 3; i++) { //{-a, a, a} {a, -a, a} {a, a, -a})
                    orbit.add(tau(i, rep));
                }
            }

        } else if (orbitType[0] == 2) { int b = fundRep[2];
            
            if (orbitType[1] == 1) { //0<a<b //int a = fundRep[1];
                for (int i = 0; i < 3; i++) {  //{0, a, b} {a, 0, b} {b, 0, a} {b, a, 0} {a, b, 0} {0, b, a}
                    int[] rep = tau(i, fundRep);
                    orbit.add(rep);
                    orbit.add(transposition((i+2)%3, rep));
                }
                int[] rotatedFundRep = axialHalfRotation(2, fundRep);
                for (int i = 0; i < 3; i++) {  //{0, -a, b} {-a, 0, b} {b, 0, -a} {b, -a, 0} {-a, b, 0} {0, b, -a}
                    int[] rep = tau(i, rotatedFundRep);
                    orbit.add(rep);
                    orbit.add(transposition((i+2)%3, rep));
                }

            } else { //if (orbitType[1] == 2 || 3) // a a b //int a = fundRep[1]; //==fundRep[0];
                for (int i = 0; i < 3; i++) {  //{a, a, b} {b, a, a} {a, b, a}
                    orbit.add(tau(i, fundRep));
                }
                for (int j = 0; j < 3; j++) {  //{-a, a, b} {-a, b, a} {-b, a, a} //{a, -b, a} {b, -a, a} {a, -a, b} //{b, a, -a} {a, a, -b} {a, b, -a}
                    for (int i = 0; i < 3; i++) {
                        orbit.add(axialQuarterRotation((j+2)%3, orbit.get(i)));
                    }
                }// || // a b b //{a, b, b} {b, a, b} {b, b, a} //{-b, a, b} {-a, b, b} {-b, b, a} //{a, -b, b} {b, -b, a} {b, -a, b} //{b, b, -a} {b, a, -b} {a, b, -b}
            } 
                
        } else { // get octahedral orbit (24).

            for (int i = 0; i < 3; i++) {
                orbit.add(tau(i, fundRep));
            }
            for (int j = 0; j < 3; j++) {
                for (int i = 0; i < 3; i++) {
                    orbit.add(axialQuarterRotation(j, orbit.get(i)));
                }
            }
            for (int j = 0; j < 3; j++) {
                for (int i = 0; i < 3; i++) {
                    orbit.add(axialHalfRotation(j, orbit.get(i)));
                }
            }
            for (int i = 0; i < 3; i++) {
                orbit.add(psi(orbit.get(i)));
            }
        }
        return orbit;
    }

    
    /**
     * Returns the full orbit of the given point under the BC3 reflection group.  
     * The elements of the List are arranged so that the positive 
     * Coxeter orbit of the fundamental representative of the given point occurs
     * in the first half of the list, followed by the respective quaternion 
     * conjugates of the positive representatives in the second half.  The elements
     * in the first half of the list are ordered in a consistent way depending on
     * the orbit type.
     * 
     * @param pt any <code>int</code>[] of length 3.
     * @return The full orbit of the given point under the BC3 reflection group.
     */
    public static List<int[]> getCoxeterOrbit(int[] pt) {
        List<int[]> orbit = getPositiveCoxeterOrbit(pt);
        int size = orbit.size();
        if (size != 1) {
            for (int i=0; i<size; i++) {
                orbit.add(negative(orbit.get(i)));
            }
        }
        return orbit;
    }

    // 90 degrees counterclockwise around the specified axis.
    private static int[] axialQuarterRotation(int axis, int[] pt) {
        if (axis == 0) {
            return new int[]{pt[0], -pt[2], pt[1]};
        } else if (axis == 1) {
            return new int[]{pt[2], pt[1], -pt[0]};
        } else if (axis == 2) {
            return new int[]{-pt[1], pt[0], pt[2]};
        }
        return null;
    }

    // 180 degrees around the specified axis. 
    private static int[] axialHalfRotation(int axis, int[] pt) {
        if (axis == 0) {
            return new int[]{pt[0], -pt[1], -pt[2]};
        } else if (axis == 1) {
            return new int[]{-pt[0], pt[1], -pt[2]};
        } else if (axis == 2) {
            return new int[]{-pt[0], -pt[1], pt[2]};
        }
        return null;
    }

    private static int[] transposition(int fixedCoord, int[] pt) {

        if (fixedCoord == 0) {
            return new int[]{pt[0], pt[2], pt[1]};
        } else if (fixedCoord == 1) {
            return new int[]{pt[2], pt[1], pt[0]};
        } else if (fixedCoord == 2) {
            return new int[]{pt[1], pt[0], pt[2]};
        }
        return null;
    }

    private static int[] tau(int exp, int[] pt) {
        int reducedExp = Arithmetic.reduce(exp, 3);
        if (reducedExp == 1) {
            return new int[]{pt[2], pt[0], pt[1]};
        }
        if (reducedExp == 2) {
            return new int[]{pt[1], pt[2], pt[0]};
        }
        return new int[]{pt[0], pt[1], pt[2]};
    }

    private static int[] psi(int[] pt) {
        return new int[]{-pt[1], -pt[0], -pt[2]};
    }
    
    private static int[] negative(int[] pt) {
        return new int[]{-pt[0], -pt[1], -pt[2]};
    }

    /**
     * Return the size of the orbit of the given point under the BC3 reflection group.
     * The size of the orbit is determined by the orbit class, and it is given as follows:</br>
     * Type:</br>
     * &#160&#160 (4, 1) : orbit size 1 </br>
     * &#160&#160 (3, 3) : orbit size 8 </br>
     * &#160&#160 (3, 2) : orbit size 12 </br>
     * &#160&#160 (3, 1) : orbit size 6 </br>
     * &#160&#160 (2, k) : orbit size 24, for k=1, 2, 3 </br>
     * &#160&#160 (1, 1) : orbit size 48 </br>
     * 
     * 
     * @param point any <code>int</code>[] of length 3.
     * @return The size of the orbit of the given point under the BC3 reflection group.
     */
    public static int getCoxeterOrbitSize(int[] point) {
        int[] type = getCoxeterOrbitClass(point);
        if (type[0] == 4) {
            return 1;
        } else if (type[0] == 3) {
            if (type[1] == 1) {
                return 6;
            } else if (type[1] == 2) {
                return 12;
            } else {
                return 8;
            }
        } else if (type[0] == 2) {
            return 24;
        } else {
            return 48;
        }

    }

    /**
     * Returns the type of the given point in regard to action of the BC3 
     * reflection group.  
     * The type of a given point is represented as a pair of integers (t, s), in
     * the form of a length 2 int array.
     * The value t ranges among the values {1, 2, 3, 4}, with s ranging among {1, 2,
     * 3} for t=2 or t=3, while s=1 when t=1 or t=4 (no subtype). The meaning of
     * these values is as follows.  Let F1, F2, F3
     * be the faces of the fundamental region contained in the planes x=0, y=x,
     * and z=y respectively, and let L1 = F1 &cap; F2, L2 = F1 &cap; F3, L3 = F2
     * &cap; F3.</br>
     * For an integer triplet p with fundamental representative p', we say that
     * p is of type (t, s) with:</br>
     * &#160&#160 t=4 : if p' is the origin </br>
     * &#160&#160 t=3 : if p' is on one and only one of the rays L1, L2, L3</br>
     * &#160&#160 t=2 : if p' is on one and only one of the faces F1, F2,
     * F3</br>
     * &#160&#160 t=1 : if p' is an interior point of the fundamental region
     * (i.e in the region 0 &lt x &lt y &lt z)
     * </br>
     * For t=2 and t=3, the subtype s is defined by the index on F and L
     * respectively.
     * </p> 
     * 
     * @param point any <code>int</code>[] of length 3.
     * @return An <code>int[]</code> of length 2 representing the type of the given point in regard to action of the BC3 
     * reflection group.
     */
    public static int[] getCoxeterOrbitClass(int[] point) {
        int[] fundRep = getFundamentalRepresentative(point);

        int[] type = new int[2];

        if (fundRep[0] == 0) {
            if (fundRep[1] == 0) {
                if (fundRep[2] == 0) {
                    type[0] = 4;
                    type[1] = 1;
                } else {
                    type[0] = 3; //cornerPts[0].add(pt);
                    type[1] = 1;
                }
            } else if (fundRep[1] == fundRep[2]) {
                type[0] = 3; //cornerPts[1].add(pt);
                type[1] = 2;
            } else {
                type[0] = 2; //edgePts[0].add(pt);
                type[1] = 1;
            }
        } else if (fundRep[0] == fundRep[1]) {
            if (fundRep[1] == fundRep[2]) {
                type[0] = 3; //cornerPts[2].add(pt);
                type[1] = 3;
            } else {
                type[0] = 2; //edgePts[1].add(pt);
                type[1] = 2;
            }
        } else if (fundRep[1] == fundRep[2]) {
            type[0] = 2; //edgePts[2].add(pt);
            type[1] = 3;
        } else {
            type[0] = 1; //interiorPts.add(pt);
            type[1] = 1;
        }

        return type;
    }

    /**
     * Returns the uniquely defined representative for the Coxeter orbit of the given point which
     * lies in the fundamental region 0 &le; x &le; y &le; z.
     * @param point any <code>int</code>[] of length 3.
     * @return An <code>int</code>[] of length 3 representing the uniquely defined point in 
     * the Coxeter orbit of the given point which lies in the fundamental region 0 &le; x &le; y &le; z.
     */
    public static int[] getFundamentalRepresentative(int[] point) {
        int[] rep = new int[]{Math.abs(point[0]), Math.abs(point[1]), Math.abs(point[2])};
        sortEntries(rep);
        return rep;
    }

    //Just for fun.
    private static void sortEntries(int[] rep) {
        if (rep[0] <= rep[1]) {
            if (rep[2] < rep[1]) {
                int rep2old = rep[2];
                rep[2] = rep[1];
                if (rep2old < rep[0]) {   //(3 1 2)
                    rep[1] = rep[0];
                    rep[0] = rep2old;
                } else {                 //(1 3 2)
                    rep[1] = rep2old;
                }
            }                            //else {} //leave alone
        } else {
            if (rep[2] >= rep[0]) {      //switch 1 and 2 : (2 1 3)
                int rep1old = rep[1];
                rep[1] = rep[0];
                rep[0] = rep1old;
            } else {                     // (2 3 1) or (3 2 1)
                int rep2old = rep[2];
                rep[2] = rep[0];
                if (rep2old <= rep[1]) {  // switch 1 and 3 (3 2 1)
                    rep[0] = rep2old;
                } else {                 //(2 3 1)
                    rep[0] = rep[1];
                    rep[1] = rep2old;
                }
            }
        }
    }

    private static void scalePoints(int a, List<int[]> points) {
        for (int[] point : points) {
            for (int i = 0; i < 3; i++) {
                point[i] = a * point[i];
            }
        }
    }
}
