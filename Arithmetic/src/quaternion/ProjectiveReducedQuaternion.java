package quaternion;

import java.util.ArrayList;
import java.util.List;
import basic_operations.Arithmetic;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A class which models the set (or family of sets) of Projective Reduced
 * Quaternions over a prime field F_q modulo non-zero scalar multiplication.
 * This is the set of Reduced Quaternions over F_q modulo non-zero scalar
 * multiplication (i.e. projective 3-space over F_q). Elements of this class are
 * represented internally by their uniquely defined ReducedQuaternion
 * representative, modulo non-zero scalar multiplication, whose first nonzero
 * entry is 1. This is referred to as the <em>canonical</em> representative. The
 * <code>equals</code> method of this class is overridden so as to return true
 * if and only if the ReducedQuaternion representatives of the elements under
 * comparison are equal.
 *
 * <p>
 * Note that multiplication is well defined on the projective space since the
 * scalars commute with the whole quaternion space, however the operation of
 * addition is <em>not</em> well defined. The projected elements of nonzero norm
 * mod q form a <em>group</em> under multiplication, the Projective Unit
 * Quaternions, and this group is isomorphic to the 2x2 projective general
 * linear group over F_q.
 * </p>
 *
 * <p>
 * The action of the BC3 reflection group on the imaginary coordinates of the
 * Reduced Quaternion space defines an action on the Projective Reduced
 * Quaternion space. In terms of our canonical representation of projective
 * space, a fundamental region for the BC3 group action is given by the union of
 * the set:</br>
 *
 * &#160&#160{1 + x<sub>1</sub>i + x<sub>2</sub>j + x<sub>3</sub>k &#160|&#160 0
 * &le; x<sub>1</sub>&le; x<sub>2</sub>&le; x<sub>3</sub>&le; (q-1)/2},</br>
 *
 * together with a set of fundamental representatives for the "points at
 * infinity" (i.e. the projections of points with real part 0):</br>
 *
 * &#160&#160{i + x<sub>2</sub>j + x<sub>3</sub>k &#160|&#160 1 &lt;
 * x<sub>2</sub>&lt; x<sub>3</sub>&le; (q-1)/2, and
 * x<sub>3</sub>&le;min{max{|x<sub>2</sub><sup>-1</sup>|,
 * |x<sub>2</sub><sup>-1</sup>x<sub>3</sub>|}, max{|x<sub>3</sub><sup>-1</sup>|,
 * |x<sub>3</sub><sup>-1</sup>x<sub>2</sub>|}}}</br>
 *
 * &#160&#160&#160&#160&#160{i + j + x<sub>3</sub>k &#160|&#160 1 &le;
 * x<sub>3</sub>&le; (q-1)/2}</br>
 *
 * &#160&#160&#160&#160&#160&#160&#160&#160&#160&#160{j + x<sub>3</sub>k
 * &#160|&#160 1 &le; x<sub>3</sub>&le; (q-1)/2, and
 * x<sub>3</sub>&le;|x<sub>3</sub><sup>-1</sup>|}</br>
 *
 * &#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160{</sub>k}</br>
 *
 * Here, by the absolute value of an element of F_q, we mean the absolute value
 * of the <em>integer</em> representative lying in within the interval -(q-1)/2
 * &le; n &le; (q-1)/2.
 * </p>
 *
 * <p>
 * As described in the documentation for ReducedQuaternion, the octahedral
 * action is natural in the sense that it arises from the conjugation action
 * x-->gxg<sup>-1</sup> by a certain subgroup of the Projective Unit
 * Quaternions. In particular, the restricted action of the octahedral group to
 * the Projective Unit Quaternions, is one that arises as a group of
 * <em>inner-automorphisms</em>.
 * </p>
 *
 * @author pdokos
 */
public class ProjectiveReducedQuaternion implements Comparable<ProjectiveReducedQuaternion> {

    private ReducedQuaternion rep;

    /**
     * Constructor for a ProjectiveReducedQuaternion, invoked by directly
     * specifying each of its entries, and the modulus.
     *
     * @param a Any <code>int</code>.
     * @param b Any <code>int</code>.
     * @param c Any <code>int</code>.
     * @param d Any <code>int</code>.
     * @param q An odd <code>int</code> prime.
     */
    public ProjectiveReducedQuaternion(int a, int b, int c, int d, int q) {
        this(new LipschitzQuaternion(a, b, c, d), q);
    }

    /**
     * Constructor for a ProjectiveReducedQuaternion, invoked by passing an
     * <code>int[]</code> of length 4 containing the entries, together with the
     * modulus.
     * <p>
     * Remark: The
     * <code>int[]</code> passed to the constructor is <em>copied</em> into the
     * internal model, and not wrapped within the object.
     * </p>
     *
     * @param entries An <code>int[]</code> of length 4.
     * @param q An odd <code>int</code> prime.
     */
    public ProjectiveReducedQuaternion(int[] entries, int q) {
        this(new LipschitzQuaternion(entries), q);
    }

    /**
     * Constructor for a ProjectiveReducedQuaternion, invoked by passing a
     * LipschitzQuaternion, together with the modulus.
     *
     * @param quat A LipschitzQuaternion.
     * @param q An odd <code>int</code> prime.
     */
    public ProjectiveReducedQuaternion(LipschitzQuaternion quat, int q) {
        this(new ReducedQuaternion(quat, q));
    }

    /**
     * Constructor for a ProjectiveReducedQuaternion, invoked by passing a
     * ReducedQuaternion.
     *
     * @param quat A ReducedQuaternion.
     */
    public ProjectiveReducedQuaternion(ReducedQuaternion quat) {
        int firstNonZeroIndex = 0;
        while (quat.getEntry(firstNonZeroIndex) == 0 && firstNonZeroIndex < 4) {
            firstNonZeroIndex++;
        }
        int factor = 1;
        if (firstNonZeroIndex != 4) {
            factor = Arithmetic.findInverse(quat.getEntry(firstNonZeroIndex), quat.getModulus());
        }
        rep = quat.scalarMultiply(factor);
    }

    /**
     * Override of the
     * <code>equals</code> method so as to return true if the passed object is a
     * ProjectiveReducedQuaternion whose canonical representative is equal, as a
     * ReducedQuaternion, to that of this object.
     *
     * @param obj Any Object.
     * @return <code>true</code> if obj is an instanceof
     * ProjectiveReducedQuaternion with canonical representative equal to that
     * of the calling object.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProjectiveReducedQuaternion) {
            return rep.equals(((ProjectiveReducedQuaternion) obj).rep);
        }
        return false;
    }

    /**
     * Override of the hashCode method in accordance with that of the equals
     * method.
     *
     * @return The hashCode of the ProjectiveReducedQuaternion.
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.rep != null ? this.rep.hashCode() : 0);
        return hash;
    }

    /**
     * Returns the reduced entry, between -(q-1)/2 and (q-1)/2 inclusive, of the
     * canonical representative of this element for the specified index i = 0,
     * 1, 2, or 3.
     *
     * @param i One of the index values 0, 1, 2, or 3.
     * @return The reduced <code>int</code> entry, between -(q-1)/2 and (q-1)/2
     * inclusive, of the canonical representative of this element, for the
     * specified index.
     */
    public int getEntry(int i) {
        return rep.getEntry(i);
    }

    /**
     * Returns the modulus associated with this element.
     *
     * @return The modulus associated with this element.
     */
    public int getModulus() {
        return rep.getModulus();
    }

    /**
     * Returns the quaternion conjugate x<sub>0</sub> - x<sub>1</sub>&sdot;i -
     * x<sub>2</sub>&sdot;j - x<sub>3</sub>&sdot;k of this element mod q.
     *
     * @return The Quaternion conjugate of this element mod q.
     */
    public ProjectiveReducedQuaternion conjugate() {
        return new ProjectiveReducedQuaternion(rep.conjugate());
    }

    /**
     * Returns the norm mod q of this element, which is only defined modulo the
     * squares in F_q.
     *
     * @return The norm of this element.
     */
    public int norm() {
        return rep.norm();
    }

    /**
     * Returns the projected quaternion product of this element with
     * <code>b</code> mod q (with this element on the left, and
     * <code>b</code> on the right).
     *
     * @param b Any ProjectiveReducedQuaternion.
     * @return The projected quaternion product of this element
     * with <code>b</code> mod q (with this element on the left,
     * and <code>b</code> on the right).
     */
    public ProjectiveReducedQuaternion multiply(ProjectiveReducedQuaternion b) {
        return new ProjectiveReducedQuaternion(rep.multiply(b.rep));
    }

    /**
     * Returns the uniquely defined ProjectiveReducedQuaternion that lies in a
     * fixed fundamental region for action of the BC3 reflection group acting on
     * the imaginary coordinates.
     * <p>
     * The fundamental region consists of the set:</br>
     *
     * &#160&#160{1 + x<sub>1</sub>i + x<sub>2</sub>j + x<sub>3</sub>k
     * &#160|&#160 0 &le; x<sub>1</sub>&le; x<sub>2</sub>&le; x<sub>3</sub>&le;
     * (q-1)/2},</br>
     *
     * together with a set of fundamental representatives for the "points at
     * infinity" (i.e. the projections of points with real part 0):</br>
     *
     * &#160&#160{i + x<sub>2</sub>j + x<sub>3</sub>k &#160|&#160 1 &lt;
     * x<sub>2</sub>&lt; x<sub>3</sub>&le; (q-1)/2, and
     * x<sub>3</sub>&le;min{max{|x<sub>2</sub><sup>-1</sup>|,
     * |x<sub>2</sub><sup>-1</sup>x<sub>3</sub>|},
     * max{|x<sub>3</sub><sup>-1</sup>|,
     * |x<sub>3</sub><sup>-1</sup>x<sub>2</sub>|}}}</br>
     *
     * &#160&#160&#160&#160&#160{i + j + x<sub>3</sub>k &#160|&#160 1 &le;
     * x<sub>3</sub>&le; (q-1)/2}</br>
     *
     * &#160&#160&#160&#160&#160&#160&#160&#160&#160&#160{j + x<sub>3</sub>k
     * &#160|&#160 1 &le; x<sub>3</sub>&le; (q-1)/2, and
     * x<sub>3</sub>&le;|x<sub>3</sub><sup>-1</sup>|}</br>
     *
     * &#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160{</sub>k}</br>
     *
     * Here, by the absolute value of an element of F_q, we mean the absolute
     * value of the <em>integer</em> representative lying in within the interval
     * -(q-1)/2 &le; n &le; (q-1)/2.
     * </p>
     *
     * @return The ProjectiveReducedQuaternion equivalent to this element under
     * the action of the BC3 reflection group acting on the imaginary
     * coordinates, whose imaginary part lies in a fixed fundamental region.
     */
    public ProjectiveReducedQuaternion getFundamentalRepresentative() {
        ReducedQuaternion fundRep = rep.getFundamentalRepresentative();

        if (fundRep.getEntry(0) == 0) {
            int q = fundRep.getModulus();

            if (fundRep.getEntry(3) == 1) {
                return new ProjectiveReducedQuaternion(fundRep);
            } else if (fundRep.getEntry(1) == 0) {
                int b = fundRep.getEntry(3);
                int bInvAbs = Math.abs(Arithmetic.centeredInverse(b, q));
                if (bInvAbs > b) {
                    return new ProjectiveReducedQuaternion(fundRep);
                }
                return new ProjectiveReducedQuaternion(0, 0, 1, bInvAbs, q);
            } else if (fundRep.getEntry(2) == 1) {
                
                int b = fundRep.getEntry(3);
                int bInvAbs = Math.abs(Arithmetic.centeredInverse(b, q));
                if (b < bInvAbs) {
                    return new ProjectiveReducedQuaternion(fundRep);
                } else {
                    return new ProjectiveReducedQuaternion(0, 1, bInvAbs, bInvAbs, q);
                }
            } else if (fundRep.getEntry(2) == fundRep.getEntry(3)) {
                int b = fundRep.getEntry(3);
                int bInvAbs = Math.abs(Arithmetic.centeredInverse(b, q));
                if (b <= bInvAbs) {
                      return new ProjectiveReducedQuaternion(fundRep);
                } else {
                    return new ProjectiveReducedQuaternion(0, 1, 1, bInvAbs, q);
                }
            }
            
            // 0 1 a b; 0 1 aInv absAInvB; 0 1 bInv absABInv
            int a = fundRep.getEntry(2); //The previous conditions imply a != 0
            int b = fundRep.getEntry(3);
            int aInv = Arithmetic.centeredInverse(a, q);
            int bInv = Arithmetic.centeredInverse(b, q);
            int absAInv = Math.abs(aInv);
            int absBInv = Math.abs(bInv);
            int absAInvB = Math.abs(Arithmetic.centeredProduct(aInv, b, q));
            int absABInv = Math.abs(Arithmetic.centeredProduct(a, bInv, q));

            int max1 = Math.max(absAInv, absAInvB);
            int max2 = Math.max(absBInv, absABInv);
            int min1 = Math.min(absAInv, absAInvB);
            int min2 = Math.min(absBInv, absABInv);

            if (max2 > b && max1 > b) {
                return new ProjectiveReducedQuaternion(fundRep);
            } else if (max2 > max1) {
                if (b == max1 && a < min1) {
                    return new ProjectiveReducedQuaternion(fundRep);
                }
                return new ProjectiveReducedQuaternion(0, 1, min1, max1, q);
            } else if (max1 == max2) {
                if (b == max2) {
                    return new ProjectiveReducedQuaternion(0, 1, Math.min(a, Math.min(min1, min2)), b, q);
                } else if (min1 < min2) {
                    return new ProjectiveReducedQuaternion(0, 1, min1, max1, q);
                }
                return new ProjectiveReducedQuaternion(0, 1, min2, max2, q);
            } else if (b == max2 && a < min2) {
                return new ProjectiveReducedQuaternion(fundRep);
            }
            return new ProjectiveReducedQuaternion(0, 1, min2, max2, q);
        }
        return new ProjectiveReducedQuaternion(fundRep); //when fundRep.getEntry(0) != 0
    }


    /**
     * Returns the orbit of this element under the action of the BC3 reflection
     * group acting on the imaginary coordinates.
     *
     * <p>
     * For elements which have non-zero real part, the elements of the List are arranged so that the <em>positive</em> Coxeter 
     * orbit (see the documentation for IntegerThreeSpaceUtility.getPositiveCoxeterRepresentative) of the fundamental representative of the given point occurs in the first
     * half of the list, followed by the respective quaternion conjugates of the
     * positive representatives in the second half. The elements in the first
     * half of the list are ordered in a consistent way depending on the orbit
     * type.
     * </p>
     * <p>
     * For elements which are represented by purely imaginary quaternions
     * (the "points at infinity" of projective 3-space), the operation of quaternion
     * conjugation is the identity, and so the elements of the positive orbit represent the full Coxeter orbit.
     * </p>
     * 
     * @return The orbit of this element under the action of the BC3 reflection
     * group acting on the imaginary coordinates.
     */
    public List<ProjectiveReducedQuaternion> getCoxeterOrbit() {
        ProjectiveReducedQuaternion fundRep = getFundamentalRepresentative();
        List<ProjectiveReducedQuaternion> orbit = new ArrayList<ProjectiveReducedQuaternion>();

        List<int[]> imaginaryCoxeterOrbit;
        if (getEntry(0) == 0) {
            imaginaryCoxeterOrbit = IntegerThreeSpaceUtility.getPositiveCoxeterOrbit(new int[]{fundRep.getEntry(1), fundRep.getEntry(2), fundRep.getEntry(3)});
        } else {
            imaginaryCoxeterOrbit = IntegerThreeSpaceUtility.getCoxeterOrbit(new int[]{fundRep.getEntry(1), fundRep.getEntry(2), fundRep.getEntry(3)});
        }
        for (int[] image : imaginaryCoxeterOrbit) {
            orbit.add(new ProjectiveReducedQuaternion(getEntry(0), image[0], image[1], image[2], getModulus()));
        }
        return orbit;
    }
    
    public Set<ProjectiveReducedQuaternion> getOctahedralOrbit() {
        Set<ProjectiveReducedQuaternion> orbit = new LinkedHashSet<ProjectiveReducedQuaternion>();

        List<int[]> imaginaryOctahedralOrbit = IntegerThreeSpaceUtility.getOctahedralOrbit(new int[]{getEntry(1), getEntry(2), getEntry(3)});
        
        for (int[] image : imaginaryOctahedralOrbit) {
            orbit.add(new ProjectiveReducedQuaternion(getEntry(0), image[0], image[1], image[2], getModulus()));
        }
        return orbit;
    }

    /**
     * Returns the size of the orbit of this element under the action of the BC3
     * reflection group acting on the imaginary coordinates. The size of the
     * orbit depends on the orbit class (r, t, s) as defined in the method getCoxeterOrbitClass.
     * For elements represented by quaternions with nonzero real part (r=1), the orbit size
     * is as usual for a point of type (t,s), while for elements having real part 0 (r=0), the orbit 
     * size is <em>half</em> that.
     * 
     * A table of values for the sizes in terms of the type (t, s) can be found in
     * the documentation for the method that goes by the same name in the
     * utility class IntegerThreeSpaceUtility.
     *
     * @return The size of the orbit of this element under the action of the BC3
     * reflection group acting on the imaginary coordinates.
     */
    public int getCoxeterOrbitSize() {
        int coxeterOrbSize = rep.getCoxeterOrbitSize();
        int collapsedOrbSize = coxeterOrbSize;

        if (getEntry(0) == 0) {
            collapsedOrbSize = coxeterOrbSize / 2;
        }
        return collapsedOrbSize;
    }

    /**
     * Returns the type of this element in regard to action of the BC3
     * reflection group acting on the imaginary coordinates. The type is
     * represented as a triplet of integers (r, t, s), in the form of a length 3 int
     * array. 
     * 
     * 
     * The value of <code>r</code> is the real part of the canonical representative, which can be either 0 or 1.
     * The values <code>t</code> and <code>s</code> are those defined by the same classification scheme, applied to the fundamental representative,
     * as that with reduced quaternions.
     * A description of this classification can be found in the
     * documentation for the method that goes by the same name in the utility
     * class IntegerThreeSpaceUtility.
     *
     * @return An <code>int[]</code> of length 3 representing the type of this
     * element in regard to action of the BC3 reflection group acting on the
     * imaginary coordinates.
     */
    public int[] getCoxeterOrbitClass() {
        int[] rType = getFundamentalRepresentative().rep.getCoxeterOrbitClass();
        return new int[]{rep.getEntry(0), rType[0], rType[1]};
    }

    /**
     * Returns -1, 0, or 1 based on whether the fundamental representative of 
     * this element is, respectively, less than, equal to, or greater than the
     * fundamental representative of b under the lexicographic ordering.
     *
     * 
     * @param b Any ProjectiveReducedQuaternion.
     * @return -1, 0, or 1 based on whether the fundamental representative of 
     * this element is, respectively, less than, equal to, or greater than the
     * fundamental representative of b under the lexicographic ordering.
     */
    @Override
    public int compareTo(ProjectiveReducedQuaternion b) {
        return rep.compareTo(b.rep);
    }

    /**
     * Returns a String representation of this element, in the form
     * "x<sub>0</sub> + x<sub>1</sub>i + x<sub>2</sub>j + x<sub>3</sub>k".
     *
     * @return A String representation of this element, in the form
     * "x<sub>0</sub> + x<sub>1</sub>i + x<sub>2</sub>j + x<sub>3</sub>k".
     */
    @Override
    public String toString() {
        return rep.toString();
    }
}