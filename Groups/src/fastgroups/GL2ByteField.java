package fastgroups;


import api.Group;
import finitefields.ByteField;
import java.util.HashSet;
import java.util.Set;


/**
 * An optimized implementation of the Group of 2x2 matrices over a finite
 * field of order &le 256.  This implementation is built on the ByteField class. 
 * The matrix entries are stored as individual byte values, which act as keys
 * for the field elements through the ByteField object.
 * The field-arithmetic operations are performed by the ByteField object, which
 * retrieves the values from table lookups that are computed upon initial construction.
 * 
 * @author pdokos
 */
public class GL2ByteField implements Group<GL2ByteField>{
    
    private byte a;
    private byte b;
    private byte c;
    private byte d;
    
    private short q;
    private ByteField f;

    /**
     * Constructor for a GL2ByteField object, invoked by specifying four byte keys for 
     * the matrix entries, and a ByteField f for the underlying field.  The 
     * parameters a and b specify the first row of the matrix, and the  
     * parameters c and d specify the second row.
     * 
     * <p>
     * It is the responsibility of the client to ensure that the matrix is 
     * non-singular over f.
     * </p>
     * 
     * @param a any <code>byte</code>
     * @param b any <code>byte</code>
     * @param c any <code>byte</code>
     * @param d any <code>byte</code>
     * @param f any <code>ByteField</code>
     */
    public GL2ByteField(byte a, byte b, byte c, byte d, ByteField f) {
        this.f = f;
        q= (short) f.getOrder();
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        
    }
    
    /**
     * Constructor for a GL2ByteField object, invoked by specifying four ByteField.Elements
     * for the matrix entries, and a ByteField f for the underlying field.  The 
     * parameters a and b specify the first row of the matrix, and the  
     * parameters c and d specify the second row.
     * 
     * <p>
     * It is the responsibility of the client to ensure that the matrix is 
     * non-singular over f.
     * </p>
     * 
     * @param a any <code>ByteField.Element</code>
     * @param b any <code>ByteField.Element</code>
     * @param c any <code>ByteField.Element</code>
     * @param d any <code>ByteField.Element</code>
     * @param f any <code>ByteField</code>
     */
    public GL2ByteField(ByteField.Element a, ByteField.Element b, ByteField.Element c, ByteField.Element d, ByteField f) {
        this.f = f;
        q= (short) f.getOrder();
        this.a = a.getIndex();
        this.b = b.getIndex();
        this.c = c.getIndex();
        this.d = d.getIndex();
        
    }

    /**
     * Constructor for the identity element of GL2ByteField over the field f.  
     * 
     * @param f any <code>ByteField</code>.
     */
    public GL2ByteField(ByteField f) {

        this((byte) 1,(byte) 0,(byte) 0,(byte) 1, f);

    }
    
    /**
     * A static helper for producing the identity element of GL2ByteField
     * over the field f.
     * 
     * @param f a <code>ByteField</code>.
     * @return The identity element of GL2ByteField.
     */
    public static GL2ByteField constructIdentity(ByteField f) {
        return new GL2ByteField(f);
    }
    
    public static GL2ByteField convert(GLnByteField g) {
        return new GL2ByteField(g.getEntry(0, 0), g.getEntry(0, 1), g.getEntry(1, 0), g.getEntry(1, 1), g.getField() );
    }
    
    public static Set<GL2ByteField> convert(Set<GLnByteField> set) {
        Set<GL2ByteField> newSet = new HashSet<GL2ByteField>(set.size()+1, 1.0f);
        for (GLnByteField g : set) {
            newSet.add(convert(g));
        }
        return newSet;
    }


    /**
     * An override of the <code>equals</code> method so as to return
     * true if: (i) h is an instance of GL2ByteField over an equal ByteField f, 
     * and (ii) the corresponding entries of the two matrices are equal.
     * 
     * @param h Any object.
     * 
     * @return true if: (i) h is an instance of GL2ByteField over an equal 
     * ByteField f, and (ii) the corresponding entries of the two matrices are equal.
     */
    @Override
    public boolean equals(Object h) {
        if (h instanceof GL2ByteField) {
            return (a==((GL2ByteField) h).a &&
                    b==((GL2ByteField) h).b &&
                    c==((GL2ByteField) h).c &&
                    d==((GL2ByteField) h).d &&
                    q==((GL2ByteField) h).q);
        }
        return false;
    }

    /**
     * An override of the <code>hashCode</code> method in accordance with that 
     * of the equals method.
     * 
     * @return The hashcode of the element making the call. 
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.a;
        hash = 59 * hash + this.b;
        hash = 59 * hash + this.c;
        hash = 59 * hash + this.d;
        hash = 59 * hash + this.q;
        return hash;
    }
    
    public ByteField getField() {
        return f;
    }
        
    public void project() {
        byte factor;
        if (a==f.zero().getIndex()) {
            factor = f.inverse(c);
        } else {
            factor = f.inverse(a);
        }
        scale(factor);
    }
    
    private void scale(byte factor) {
        a=f.mult(factor, a);
        b=f.mult(factor, b);
        c=f.mult(factor, c);
        d=f.mult(factor, d);
    }
    
    /**
     * Returns the order of the base field.
     * 
     * @return The order of the base field.
     */
    public int getFieldOrder(){
        return f.getOrder();
    }

    /**
     * The first entry of the first row of the reduced matrix representative.
     * 
     * @return The first entry of the first row of the matrix.
     */
    public byte getA(){
        return a;
    }

    /**
     * The second entry of the first row of the reduced matrix representative.
     * @return The second entry of the first row of the matrix.
     */
    public byte getB(){
        return b;
    }

    /**
     * The first entry of the second row of the reduced matrix representative.
     * 
     * @return The first entry of the second row of the matrix.
     */
    public byte getC(){
        return c;
    }

    /**
     * The second entry of the second row of the reduced matrix representative.
     * 
     * @return The second entry of the second row of the matrix.
     */
    public byte getD(){
        return d;
    }

    /**
     * Computes the determinant of this element.
     * 
     * @return The byte-key r (under the field f) for the determinant of this element.
     */
    public byte determinant() {
        return f.add(f.mult(a, d), f.negative(f.mult(b, c)));
    }
    
    /**
     * Returns the transposed element of GL2ByteField.
     * 
     * @return The transposed element of GL2ByteField.
     */
    public GL2ByteField transpose() {
        return new GL2ByteField(a, c, b, d, f);
    }
    
    /**
     * @param h Any element of GL2ByteField that is operational with the element making the call. 
     */
    @Override
    public GL2ByteField leftProductBy(GL2ByteField h) {
        if (h.q== q) {
        return new GL2ByteField(f.add(f.mult(h.getA(), a), f.mult(h.getB(), c)), 
                                  f.add(f.mult(h.getA(), b), f.mult(h.getB(), d)),
                                  f.add(f.mult(h.getC(), a), f.mult(h.getD(), c)), 
                                  f.add(f.mult(h.getC(), b), f.mult(h.getD(), d)), f);
        }
        return null;
    }

    /**
     * @param h Any element of GL2ByteField that is operational with the element making the call. 
     */
    @Override
    public GL2ByteField rightProductBy(GL2ByteField h) {
        if (h.q== q) {
        return new GL2ByteField(f.add(f.mult(a, h.getA()), f.mult(b, h.getC())), 
                                  f.add(f.mult(a, h.getB()), f.mult(b, h.getD())),
                                  f.add(f.mult(c, h.getA()), f.mult(d, h.getC())), 
                                  f.add(f.mult(c, h.getB()), f.mult(d, h.getD())),f);
        }
        return null;
    }

    @Override
    public GL2ByteField getInverse() {
        byte detInv= f.inverse(determinant());
        return new GL2ByteField(f.mult(detInv,d), f.negative(f.mult(detInv,b)), f.negative(f.mult(detInv,c)), f.mult(detInv,a), f);
    }

    @Override
    public GL2ByteField getIdentity() {
        return new GL2ByteField(f);
    }

    /**
     * Returns true if the base field of h is the same as that of the element 
     * making the call.
     * 
     * @param h Any element of GL2ByteField.
     * @return true if the base field of h is the same as that of the element 
     * making the call.
     */
    @Override
    public boolean isOperationalWith(GL2ByteField h) {
        return q==h.q;
    }

    /**
     * Overrides the <code>toString</code> method so as to return a String 
     * representation of the matrix of the element making the call, in the 
     * format of a MATLAB matrix.
     * 
     * <p>
     * Note that the base field order is lost in this representation.
     * </p>
     * 
     * @return A String representation of the matrix of the element making the 
     * call, in the format of a MATLAB matrix.
     */
    @Override
    public String toString() {
        return "[" + f.getElement(a).toString() + ", " + f.getElement(b).toString() + "; " + f.getElement(c).toString() + ", " + f.getElement(d).toString() + "]";
    }
    
    /**
     * Returns a representation of the element making the call as a String
     * consisting of the four entries of the matrix, followed by the field order,
     * all separated by single spaces.
     * 
     * @return A String representation of the element making the call
     * consisting of the four entries of the matrix, followed by the field order,
     * all separated by single spaces.
     */
    public String toUnpunctuatedString() {
        return a + " "+b+" "+c+" "+d+" "+q;
    }

}
