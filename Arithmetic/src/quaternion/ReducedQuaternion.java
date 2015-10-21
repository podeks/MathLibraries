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
package quaternion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class which models the algebra (over an odd prime field) of reduced Lipschitz quaternions modulo 
 * an odd prime q.  This is the algebra of formal linear combinations
 * x<sub>0</sub> + x<sub>1</sub>&sdot;i + x<sub>2</sub>&sdot;j + x<sub>3</sub>&sdot;k
 * over the field F_q, with the same multiplicative relations on the basis elements
 * 1, i, j, k as with the Lipschitz quaternions.
 * Elements of the algebra are represented internally as int arrays of length 4
 * whose entries all have absolute value less than or equal to (q-1)/2.
 * This representation is unique, and the <code>equals</code> method of this class 
 * is overridden so as to return true if and only if the corresponding reduced 
 * entries of the elements being compared are equal.
 * <p>
 * Note that this algebra is isomorphic to the 2x2 matrix 
 * algebra over F_q.
 * </p>
 * 
 * <p>
 * Like the Lipschitz quaternions class, this class incorporates several methods 
 * (namely the getFundamentalRepresentative method and the CoxeterOrbit methods) 
 * pertaining to a certain action on the Reduced Quaternions by the BC3 
 * reflection group and the octahedral subgroup.  The groups act in the usual 
 * way on the "imaginary" part xi+yj+zk (as described in the documentation for 
 * IntegerThreeSpaceUtility), while fixing the "real" part.  In this setting, a 
 * fundamental region for the action of the BC3 reflection group is given by the set</br>
 * 0 &le; x<sub>1</sub>&le; x<sub>2</sub>&le; x<sub>3</sub>&le; (q-1)/2, with no
 * restrictions on x<sub>0</sub>.</br>
 * </p>
 * <p>
 * These actions are natural with regard to the quaternion arithmetic, in the sense 
 * that there is a subgroup of the projective unit quaternions over F_q which is 
 * isomorphic to the octahedral group, and the conjugation action x --> gxg<sup>-1</sup> by 
 * this group, is the one described.  
 * In particular the octahedral action commutes with quaternion multiplication 
 * (which means that applying a transformation to a product of two quaternions is the same 
 * as multiplying the transformations of the two quaternions).
 * The action of the full BC3 reflection group described is obtained by adjoining 
 * the antipodal map, which arises from the quaternion conjugation (an anti-commutative operation!), to the 
 * octahedral action. 
 * </p>
 * 
 * @author pdokos
 */
public class ReducedQuaternion implements Comparable<ReducedQuaternion>{

    private int[] entries;
    private int q; //an odd prime

    /**
     * Constructor for a ReducedQuaternion, invoked by directly specifying each of its 
     * entries, and the modulus.
     * 
     * @param a Any <code>int</code>.
     * @param b Any <code>int</code>.
     * @param c Any <code>int</code>.
     * @param d Any <code>int</code>. 
     * @param q An odd <code>int</code> prime.
     */
    public ReducedQuaternion(int a, int b, int c, int d, int q) {
        entries = new int[]{reduce(a, q), reduce(b, q), reduce(c, q), reduce(d, q)};
        this.q = q;
    }

    /**
     * Constructor for a ReducedQuaternion, invoked by passing an <code>int[]</code> of 
     * length 4 containing the entries, together with the modulus.
     * <p>
     * Remark: The <code>int[]</code> passed to the constructor is <em>copied</em> into
     * the internal model, and not wrapped within the object.
     * </p>
     * 
     * @param entries An <code>int[]</code> of length 4.
     * @param q An odd <code>int</code> prime.
     */
    public ReducedQuaternion(int[] entries, int q) {
        this(entries[0], entries[1], entries[2], entries[3], q);
    }

    /**
     * Constructor for a ReducedQuaternion, invoked by passing a 
     * LipschitzQuaternion, together with the modulus.
     * 
     * @param quat A LipschitzQuaternion.
     * @param q An odd <code>int</code> prime.
     */
    public ReducedQuaternion(LipschitzQuaternion quat, int q) {
        this(quat.getEntry(0), quat.getEntry(1), quat.getEntry(2), quat.getEntry(3), q);
    }
    
    //n must be odd.
    private static int reduce(int m, int n) {
        
        int n0 = Math.abs(n);
        int modulus = m%n0;
        int mdpt = (n0-1)/2;
        int reduction=modulus;
        
        if (modulus > mdpt) {
            reduction -= n;
        } else if (modulus < -mdpt) {
            reduction += n;
        }
        return reduction;
    }
    
    private static int reduce(long m, int n) {
        
        int n0 = Math.abs(n);
        int modulus = (int) m%n0;
        int mdpt = (n0-1)/2;
        int reduction=modulus;
        
        if (modulus > mdpt) {
            reduction -= n;
        } else if (modulus < -mdpt) {
            reduction += n;
        }
        return reduction;
    }
    
    private static int reducedSum(int a, int b, int n) { 
        return reduce((((long) a) + ((long) b)), n);
    }
    
    private static int reducedProduct(int a, int b, int n) {
        return reduce((((long) a) * ((long) b)), n);
    }

    /**
     * Override of the <code>equals</code> method so as to return true
     * if the passed object is a ReducedQuaternion for the same modulus, with 
     * corresponding reduced entries that are equal to those of the calling object.
     * @param obj Any Object.
     * @return <code>true</code> if obj is an instanceof  ReducedQuaternion 
     * with the same modulus, and with corresponding reduced entries that are equal to those of the calling object.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ReducedQuaternion) {
            if (((ReducedQuaternion) obj).q == q) {
                return Arrays.equals(entries, ((ReducedQuaternion) obj).entries);
            }
        }
        return false;
    }

    /**
     * Override of the hashCode method in accordance with that of the equals method.
     * 
     * @return The hashCode of the ReducedQuaternion.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Arrays.hashCode(this.entries);
        hash = 31 * hash + this.q;
        return hash;
    }

    /**
     * Returns the reduced entry, between -(q-1)/2 and (q-1)/2 inclusive, of this element for the specified index i = 0, 1, 2, or 3.
     * @param i One of the index values 0, 1, 2, or 3.
     * @return The reduced <code>int</code> entry, between -(q-1)/2 and (q-1)/2 inclusive, for the specified index.
     */
    public int getEntry(int i) {
        return entries[i];
    }

    /**
     * Returns the modulus associated with this element.
     * @return The modulus associated with this element.
     */
    public int getModulus() {
        return q;
    }

    /**
     * Returns the quaternion conjugate x<sub>0</sub> - x<sub>1</sub>&sdot;i - x<sub>2</sub>&sdot;j - x<sub>3</sub>&sdot;k
     * of this element mod q.
     * @return The Quaternion conjugate of this element mod q.
     */
    public ReducedQuaternion conjugate() {
        return new ReducedQuaternion(entries[0], -entries[1], -entries[2], -entries[3], q);
    }

    /**
     * Returns the negative of this element mod q.
     * @return The negative of this element mod q.
     */
    public ReducedQuaternion negative() {
        return new ReducedQuaternion(-entries[0], -entries[1], -entries[2], -entries[3], q);
    }

    /**
     * Returns the norm x<sub>0</sub><sup>2</sup> + x<sub>1</sub><sup>2</sup> + 
     * x<sub>2</sub><sup>2</sup> + x<sub>3</sub><sup>2</sup> of this element 
     * modulo q.  The returned value is the unique representative lying in the 
     * interval between -(q-1)/2 and (q-1)/2 inclusive.
     * 
     * @return The representative in the 
     * interval between -(q-1)/2 and (q-1)/2 inclusive, of the norm of this element modulo q.
     */
    public int norm() {
        int norm = 0;
        for (int i = 0; i < 4; i++) {
            norm = reducedSum(norm, reducedProduct(entries[i], entries[i], q), q);
        }
        return norm;
    }

    /**
     * Returns the ReducedQuaternion whose entries are equal to the reduced product 
     * mod q of <code>a</code> with corresponding entries of this element.
     * 
     * @param a Any <code>int</code>.
     * @return A ReducedQuaternion whose entries are equal to the reduced product, 
     * mod q, of <code>a</code> with corresponding entries of this element.
     */
    public ReducedQuaternion scalarMultiply(int a) {
        int[] product = new int[4];
        for (int i=0; i<4; i++)
            product[i] = reducedProduct(a, entries[i], q);
        return new ReducedQuaternion(product, q);
    }
    
    /**
     * Returns the sum of this element with <code>b</code> mod q.
     * @param b Any ReducedQuaternion.
     * @return The sum of this element with <code>b</code> mod q.
     */
    public ReducedQuaternion add(ReducedQuaternion b) {
        int[] sum = new int[4];
        for (int i = 0; i < 4; i++) {
            sum[i] = reducedSum(entries[i], b.entries[i], q);
        }
        return new ReducedQuaternion(sum, q);
    }

    /**
     * Returns the quaternion product of this element with <code>b</code> mod q (with this 
     * element on the left, and <code>b</code> on the right).
     * @param b Any ReducedQuaternion.
     * @return The quaternion product of this element with <code>b</code> mod q (with this 
     * element on the left, and <code>b</code> on the right).
     */
    public ReducedQuaternion multiply(ReducedQuaternion b) {
        int[] product = new int[4];
        product[0] = reducedSum(reducedSum(reducedProduct(entries[0], b.entries[0], q), -reducedProduct(entries[1], b.entries[1], q), q), reducedSum(-reducedProduct(entries[2], b.entries[2], q), -reducedProduct(entries[3], b.entries[3], q), q), q);
        product[1] = reducedSum(reducedSum(reducedProduct(entries[0], b.entries[1], q), reducedProduct(entries[1], b.entries[0], q), q), reducedSum(reducedProduct(entries[2], b.entries[3], q), -reducedProduct(entries[3], b.entries[2], q), q), q);
        product[2] = reducedSum(reducedSum(reducedProduct(entries[0], b.entries[2], q), reducedProduct(entries[2], b.entries[0], q), q), reducedSum(reducedProduct(entries[3], b.entries[1], q), -reducedProduct(entries[1], b.entries[3], q), q), q);
        product[3] = reducedSum(reducedSum(reducedProduct(entries[0], b.entries[3], q), reducedProduct(entries[3], b.entries[0], q), q), reducedSum(reducedProduct(entries[1], b.entries[2], q), -reducedProduct(entries[2], b.entries[1], q), q), q);
        return new ReducedQuaternion(product, q);
    }
    
    /**
     * Returns the uniquely defined ReducedQuaternion whose
     * imaginary part lies in the region 0&le;x<sub>1</sub>&le;x<sub>2</sub>&le;x<sub>3</sub>&le;(q-1)/2, which is equivalent to this element under the 
     * action of the BC3 reflection group acting on the imaginary coordinates.
     * The real part of the fundamental representative is the same as that of 
     * this element.
     * 
     * @return The ReducedQuaternion equivalent to this element under the 
     * action of the BC3 reflection group acting on the imaginary coordinates, whose
     * imaginary part lies in the fundamental region 
     * 0&le;x<sub>1</sub>&le;x<sub>2</sub>&le;x<sub>3</sub>&le;(q-1)/2.
     */
    public ReducedQuaternion getFundamentalRepresentative() {
        int[] imPart = new int[]{entries[1], entries[2], entries[3]};
        int[] fundRep = IntegerThreeSpaceUtility.getFundamentalRepresentative(imPart);
        return new ReducedQuaternion(entries[0], fundRep[0], fundRep[1], fundRep[2], q);
    }
    
    /**
     * Returns the orbit of this element under the 
     * action of the BC3 reflection group acting on the imaginary coordinates.
     * 
     * The elements of the List are arranged so that the positive 
     * Coxeter orbit of the fundamental representative of the given point occurs
     * in the first half of the list, followed by the respective quaternion 
     * conjugates of the positive representatives in the second half.  The elements
     * in the first half of the list are ordered in a consistent way depending on
     * the orbit type.
     * 
     * @return The orbit of this element under the 
     * action of the BC3 reflection group acting on the imaginary coordinates.
     */
    public List<ReducedQuaternion> getCoxeterOrbit() {
        ReducedQuaternion fundRep = getFundamentalRepresentative();
        List<ReducedQuaternion> orbit = new ArrayList<ReducedQuaternion>();
        List<int[]> imaginaryCoxeterOrbit = IntegerThreeSpaceUtility.getCoxeterOrbit(new int[]{fundRep.entries[1], fundRep.entries[2], fundRep.entries[3]});
        for (int[] image : imaginaryCoxeterOrbit) {
            orbit.add(new ReducedQuaternion(fundRep.entries[0], image[0], image[1], image[2], q));
        }
        return orbit;
    }
    
    /**
     * Returns the size of the orbit of this element under the 
     * action of the BC3 reflection group acting on the imaginary coordinates.
     * The size of the orbit depends on the orbit class, and a table of values
     * can be found in the documentation for the method that goes by the same 
     * name in the utility class IntegerThreeSpaceUtility.
     * 
     * @return The size of the orbit of this element under the 
     * action of the BC3 reflection group acting on the imaginary coordinates.
     */
    public int getCoxeterOrbitSize() {
        ReducedQuaternion fundRep = getFundamentalRepresentative();
        int[] imPart = new int[]{fundRep.entries[1], fundRep.entries[2], fundRep.entries[3]};
        return IntegerThreeSpaceUtility.getCoxeterOrbitSize(imPart);
    }
    
    /**
     * Returns the type of this element in regard to action of the BC3 
     * reflection group acting on the imaginary coordinates.  The type is 
     * represented as a pair of integers (t, s), in the form of a length 2 int 
     * array.  A description of this classification can be found in the 
     * documentation for the method that goes by the same 
     * name in the utility class IntegerThreeSpaceUtility.
     * 
     * @return An <code>int[]</code> of length 2 representing the type of this element in regard to action of the BC3 
     * reflection group acting on the imaginary coordinates.
     */
    public int[] getCoxeterOrbitClass() {
        int[] imaginaryPart = new int[]{entries[1], entries[2], entries[3]};
        return IntegerThreeSpaceUtility.getCoxeterOrbitClass(imaginaryPart);
    }
    
    /**
     * Returns -1, 0, or 1 based on whether the fundamental representative of 
     * this element is, respectively, less than, equal to, or greater than the
     * fundamental representative of b under the lexicographic ordering.
     *
     * 
     * @param b Any ReducedQuaternion.
     * @return -1, 0, or 1 based on whether the fundamental representative of 
     * this element is, respectively, less than, equal to, or greater than the
     * fundamental representative of b under the lexicographic ordering.
     */
    @Override
    public int compareTo(ReducedQuaternion b) {
        ReducedQuaternion fundRep = getFundamentalRepresentative();
        ReducedQuaternion bFundRep = b.getFundamentalRepresentative();
        if (fundRep.getEntry(0) < bFundRep.getEntry(0)) {
            return -1;
        } else if (fundRep.getEntry(0) == bFundRep.getEntry(0)) {
            if (fundRep.getEntry(1) < bFundRep.getEntry(1)) {
                return -1;
            } else if (fundRep.getEntry(1) == bFundRep.getEntry(1)) {
                if (fundRep.getEntry(2) < bFundRep.getEntry(2)) {
                    return -1;
                } else if (fundRep.getEntry(2) == bFundRep.getEntry(2)) {
                    if (fundRep.getEntry(3) < bFundRep.getEntry(3)) {
                        return -1;
                    } else if (fundRep.getEntry(3) == bFundRep.getEntry(3)) {
                        return 0;
                    }
                    return 1;
                }
                return 1;
            }
            return 1;
        }
        return 1;
    }
    
    /**
     * Returns a String representation of this element, in the form 
     * "x<sub>0</sub> + x<sub>1</sub>i + x<sub>2</sub>j + x<sub>3</sub>k".
     * @return A String representation of this element, in the form 
     * "x<sub>0</sub> + x<sub>1</sub>i + x<sub>2</sub>j + x<sub>3</sub>k".
     */
    @Override
    public String toString() {
        return entries[0] + " + " + entries[1] + "i + " + entries[2] + "j + " + entries[3] + "k" + " mod " + q;
    }
            
}
