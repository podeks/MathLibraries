package quaternion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class which models the algebra (over the integers) of Lipschitz Integer Quaternions.  This is
 * the set of formal linear combinations x<sub>0</sub> + x<sub>1</sub>&sdot;i + x<sub>2</sub>&sdot;j + x<sub>3</sub>&sdot;k with integer entries 
 * x<sub>i</sub>, and with a (non-commutative) multiplication rule defined by the relations i<sup>2</sup> = j<sup>2</sup> =  k<sup>2</sup> = -1 and ij = -ji = k, jk=-kj=i, ki=-ik=j.
 * 
 * <p>
 * Objects of this class are represented internally as <code>int</code> arrays
 * of length 4.  The <code>equals</code> method is overridden so as to return 
 * <code>true</code> if the passed object is a LipschitzQuaternion with 
 * corresponding entries that are equal to those of the calling element.
 * </p>
 * <p>
 * This class incorporates several methods (namely the getFundamentalRepresentative method and the CoxeterOrbit methods) 
 * pertaining to a certain action on the Lipschitz 
 * Quaternions by the BC3 reflection group and the octahedral subgroup.  The
 * groups act in the usual way on the "imaginary" part xi+yj+zk (as described
 * in the documentation for IntegerThreeSpaceUtility), while fixing the "real" part.
 * </p>
 * <p>
 * These actions are natural with regard to the quaternion arithmetic, in the sense that there is a subgroup of the 
 * projective unit quaternions over the real numbers (or any extension of Q(sqrt(2))
 * which is isomorphic to the octahedral group, and the conjugation action x --> gxg<sup>-1</sup> by 
 * this group, restricted to the Lipschitz integers, is the one described.  
 * In particular the octahedral action commutes with quaternion multiplication, 
 * (which means that applying one of the transformations to a product of two quaternions is the same 
 * as multiplying the transformations of the two quaternions).
 * The action of the full BC3 reflection group described is obtained by adjoining 
 * the antipodal map, which arises from the quaternion conjugation (an anti-commutative operation!), to the 
 * octahedral action. 
 * </p>
 * 
 * @author pdokos
 */
public class LipschitzQuaternion {
    
    private int[] entries;
    
    /**
     * Constructor for a LipschitzQuaternion, invoked by directly specifying each of its 
     * entries.
     * 
     * @param a Any <code>int</code>.
     * @param b Any <code>int</code>.
     * @param c Any <code>int</code>.
     * @param d Any <code>int</code>. 
     */
    public LipschitzQuaternion(int a, int b, int c, int d) {
        entries = new int[]{a, b, c, d};
    }
    
    /**
     * Constructor for a LipschitzQuaternion, invoked by passing an <code>int[]</code> of 
     * length 4 containing the entries.
     * <p>
     * Remark: The <code>int[]</code> passed to the constructor is <em>copied</em> into
     * the internal model, and not wrapped within the object.
     * </p>
     * 
     * @param entries An <code>int[]</code> of length 4.
     */
    public LipschitzQuaternion(int[] entries) {
        this(entries[0], entries[1], entries[2], entries[3]);
    }
    
    /**
     * Override of the <code>equals</code> method so as to return true
     * if the passed object is a LipschitzQuaternion with 
     * corresponding entries that are equal to those of the calling object.
     * @param obj Any Object.
     * @return <code>true</code> if obj is an instanceof  LipschitzQuaternion 
     * with corresponding entries that are equal to those of the calling object.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LipschitzQuaternion) {
            return Arrays.equals(entries, ((LipschitzQuaternion) obj).entries);
        }
        return false;
    }

    /**
     * Override of the hashCode method in accordance with that of the equals method.
     * 
     * @return The hashCode of the LipschitzQuaternion.
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Arrays.hashCode(this.entries);
        return hash;
    }
    
    /**
     * Returns the entry of this element for the specified index i = 0, 1, 2, or 3.
     * @param i One of the index values 0, 1, 2, or 3.
     * @return The <code>int</code> entry for the specified index.
     */
    public int getEntry(int i) {
        return entries[i];
    }
    
    /**
     * Returns the quaternion conjugate x<sub>0</sub> - x<sub>1</sub>&sdot;i - x<sub>2</sub>&sdot;j - x<sub>3</sub>&sdot;k
     * of this element.
     * @return The Quaternion conjugate of this element.
     */
    public LipschitzQuaternion conjugate() {
        return new LipschitzQuaternion(entries[0], -entries[1], -entries[2], -entries[3]);
    }
    
    /**
     * Returns the negative of this element.
     * @return The negative of this element.
     */
    public LipschitzQuaternion negative() {
        return new LipschitzQuaternion(-entries[0], -entries[1], -entries[2], -entries[3]);
    }
    
    /**
     * Returns the norm x<sub>0</sub><sup>2</sup> + x<sub>1</sub><sup>2</sup> + 
     * x<sub>2</sub><sup>2</sup> + x<sub>3</sub><sup>2</sup> of this element.
     * @return The norm of this element.
     */
    public int norm() {
        int norm = 0;
        for (int i=0; i< 4; i++) {
            norm += entries[i]*entries[i];
        } 
        return norm;
    }
    
    /**
     * Returns a LipschitzQuaternion whose entries are equal to the product of 
     * <code>a</code> with corresponding entries of this element.
     * 
     * @param a Any <code>int</code>.
     * @return A LipschitzQuaternion whose entries are equal to the product of 
     * <code>a</code> with corresponding entries of this element.
     */
    public LipschitzQuaternion scalarMultiply(int a) {
        int[] product = new int[4];
        for (int i=0; i<4; i++)
            product[i] = a*entries[i];
        return new LipschitzQuaternion(product);
    }
    
    /**
     * Returns the sum of this element with <code>b</code>.
     * @param b Any LipschitzQuaternion.
     * @return The sum of this element with <code>b</code>.
     */
    public LipschitzQuaternion add(LipschitzQuaternion b) {
        int[] sum = new int[4];
        for (int i=0; i<4; i++)
            sum[i] = entries[i]+ b.entries[i];
        return new LipschitzQuaternion(sum);
    }
    
    /**
     * Returns the quaternion product of this element with <code>b</code> (with this 
     * element on the left, and <code>b</code> on the right).
     * @param b Any LipschitzQuaternion.
     * @return The quaternion product of this element with <code>b</code> (with this 
     * element on the left, and <code>b</code> on the right).
     */
    public LipschitzQuaternion multiply(LipschitzQuaternion b) {
        int[] product = new int[4];
        product[0] = entries[0]*b.entries[0] - entries[1]*b.entries[1] - entries[2]*b.entries[2] - entries[3]*b.entries[3];
        product[1] = entries[0]*b.entries[1] + entries[1]*b.entries[0] + entries[2]*b.entries[3] - entries[3]*b.entries[2];
        product[2] = entries[0]*b.entries[2] + entries[2]*b.entries[0] + entries[3]*b.entries[1] - entries[1]*b.entries[3];
        product[3] = entries[0]*b.entries[3] + entries[3]*b.entries[0] + entries[1]*b.entries[2] - entries[2]*b.entries[1];
        return new LipschitzQuaternion(product);
    }
    
    /**
     * Returns the LipschitzQuaternion whose
     * imaginary part lies in the region 0&le;x<sub>1</sub>&le;x<sub>2</sub>&le;x<sub>3</sub>, 
     * which is equivalent to this element under the 
     * action of the BC3 reflection group acting on the imaginary coordinates.
     * The real part of the fundamental representative is the same as that of 
     * this element.
     * 
     * @return The LipschitzQuaternion equivalent to this element under the 
     * action of the BC3 reflection group acting on the imaginary coordinates, whose
     * imaginary part lies in the fundamental region 
     * 0&le;x<sub>1</sub>&le;x<sub>2</sub>&le;x<sub>3</sub>.
     */
    public LipschitzQuaternion getFundamentalRepresentative() {
        int[] imaginaryPart = IntegerThreeSpaceUtility.getFundamentalRepresentative(new int[]{entries[1], entries[2], entries[3]});
        return new LipschitzQuaternion(entries[0], imaginaryPart[0], imaginaryPart[1], imaginaryPart[2]);
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
    public List<LipschitzQuaternion> getCoxeterOrbit() {
        LipschitzQuaternion fundRep = getFundamentalRepresentative();
        List<LipschitzQuaternion> orbit = new ArrayList<LipschitzQuaternion>();
        List<int[]> imaginaryCoxeterOrbit = IntegerThreeSpaceUtility.getCoxeterOrbit(new int[]{fundRep.getEntry(1), fundRep.getEntry(2), fundRep.getEntry(3)});
        int a = fundRep.getEntry(0);
        for (int[] pt : imaginaryCoxeterOrbit) {
            orbit.add(new LipschitzQuaternion(a, pt[0], pt[1], pt[2]));
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
        LipschitzQuaternion fundRep = getFundamentalRepresentative();
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
     * This method is included for scenarios where one uses LipschitzQuaternions
     * to represent elements of a group contained in the projective unit 
     * quaternions over the rational numbers (e.g.&#160the group of p-integral
     * projective unit quaternions).  The returned list differs from that of the
     * method getCoxeterOrbit only when the real part of this element is 0, in which case a set 
     * of representatives modulo the antipodal map is returned.  The choice
     * of representatives is defined by the method getPositiveCoxeterOrbit of the 
     * class IntegerThreeSpaceUtility.
     * 
     * @return The same List as getCoxeterOrbit of this class, except when the 
     * real part of this element is 0, in which case a List of representatives 
     * modulo the antipodal map is returned.
     */
    public List<LipschitzQuaternion> getProjectiveCoxeterOrbit() {
        LipschitzQuaternion fundRep = getFundamentalRepresentative();
        List<LipschitzQuaternion> orbit = new ArrayList<LipschitzQuaternion>();
        List<int[]> imaginaryCoxeterOrbit;

        int a = fundRep.getEntry(0);
        if (a==0) {
            imaginaryCoxeterOrbit = IntegerThreeSpaceUtility.getPositiveCoxeterOrbit(new int[]{fundRep.getEntry(1), fundRep.getEntry(2), fundRep.getEntry(3)});
        } else {
            imaginaryCoxeterOrbit = IntegerThreeSpaceUtility.getCoxeterOrbit(new int[]{fundRep.getEntry(1), fundRep.getEntry(2), fundRep.getEntry(3)});
        }
        
        for (int[] pt : imaginaryCoxeterOrbit) {
            orbit.add(new LipschitzQuaternion(a, pt[0], pt[1], pt[2]));
        }
        
        return orbit;
    }
    
    /**
     * Returns a String representation of this element, in the form 
     * "x<sub>0</sub> + x<sub>1</sub>i + x<sub>2</sub>j + x<sub>3</sub>k".
     * @return A String representation of this element, in the form 
     * "x<sub>0</sub> + x<sub>1</sub>i + x<sub>2</sub>j + x<sub>3</sub>k".
     */
    @Override
    public String toString() {
        return entries[0] + " + " + entries[1] + "i + " + entries[2] + "j + " + entries[3] + "k";
    }
}
