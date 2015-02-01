package groups;

import java.util.Random;
import basic_operations.Arithmetic;
import api.Group;

/**
 * A class that models the group of non-singular 
 * 2x2 matrices over a <code>short</code> integer prime field, implemented in 
 * the framework of the Group interface.
 * 
 * <p>
 * Elements of the group are represented internally by the <em>uniquely</em> defined 
 * integer matrix representative mod q whose entries are non-negative and less 
 * than q.  The reduced matrix entries are computed at the time of 
 * construction.  This has the subsequent benefit of rapid equality testing
 * between two GL2_PrimeField elements.  In particular, the <code>equals</code> 
 * method is overridden in this class to test for equivalence of group elements 
 * by checking for equality between corresponding matrix entries.
 * </p>
 * 
 * <p>
 * Remark: The arithmetic operations mod q in this class are delegated to the 
 * class basic_operations.Arithmetic of the Arithmetic module.
 * </p>
 * 
 * @author pdokos
 */
public class GL2_PrimeField implements Group<GL2_PrimeField> {

    private short a;
    private short b;
    private short c;
    private short d;
    private short q;

    /**
     * Constructor for a GL2_PrimeField object, invoked by specifying four integer representatives for 
     * the matrix entries, and a prime number q for the order of base field.  The 
     * parameters a and b specify the first row of the matrix, and the  
     * parameters c and d specify the second row.
     * 
     * <p>
     * It is the 
     * responsibility of the client to ensure that the parameter q is prime, and
     * that the matrix is non-singular mod q.
     * </p>
     * 
     * @param a any <code>int</code>
     * @param b any <code>int</code>
     * @param c any <code>int</code>
     * @param d any <code>int</code>
     * @param q any <code>short</code> integer prime
     */
    public GL2_PrimeField(int a, int b, int c, int d, short q) {
        this.a = Arithmetic.reduce(a, q);
        this.b = Arithmetic.reduce(b, q);
        this.c = Arithmetic.reduce(c, q);
        this.d = Arithmetic.reduce(d, q);
        this.q = q;
    }

    /**
     * Constructor for the identity element of GL2_PrimeField over the field of 
     * q elements.  
     * 
     * <p>
     * It is the responsibility of the client to ensure that the 
     * parameter q is prime.
     * </p>
     * 
     * @param q any <code>short</code> integer prime.
     */
    public GL2_PrimeField(short q) {

        this(1, 0, 0, 1, q);

    }
    
    /**
     * A static helper for producing the identity element of GL2_PrimeField 
     * over the field of q elements, where q is prime.
     * 
     * @param q a <code>short</code> integer prime.
     * @return The identity element of GL2_PrimeField.
     */
    public static GL2_PrimeField constructIdentity(short q) {
        return new GL2_PrimeField(q);
    }
    
    /**
     * A static helper for producing a random element of GL2_PrimeField 
     * over the field of q elements, where q is prime.
     * 
     * @param q a <code>short</code> integer prime.
     * @return A random element of GL2_PrimeField.
     */
    public static GL2_PrimeField constructRandomElement(short q) {
        Random r = new Random();
        int det=0;
        int a=0;
        int b=0;
        int c=0;
        int d=0;
        
        while (det == 0) {
            a = r.nextInt(q);
            b = r.nextInt(q);
            c = r.nextInt(q);
            d = r.nextInt(q);
            det = Arithmetic.reduce(a*d-b*c, q);
        }
        return new GL2_PrimeField(a, b, c, d, q);
    }

    /**
     * An override of the <code>equals</code> method so as to return
     * true if: (i) h is an instance of GL2_PrimeField over the same prime field of order 
     * q, and (ii) the corresponding entries of the reduced matrix representatives
     * are equal.
     * 
     * @param h Any object.
     * 
     * @return true if: (i) h is an instance of GL2_PrimeField over the same prime field of order 
     * q, and (ii) the corresponding entries of the reduced matrix representatives
     * are equal.
     */
    @Override
    public boolean equals(Object h) {
        if (h instanceof GL2_PrimeField) {
            return (a==((GL2_PrimeField) h).getA() &&
                    b==((GL2_PrimeField) h).getB() &&
                    c==((GL2_PrimeField) h).getC() &&
                    d==((GL2_PrimeField) h).getD() &&
                    q==((GL2_PrimeField) h).getFieldOrder());
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
    
    /**
     * Returns the order of the base field.
     * 
     * @return The order of the base field.
     */
    public short getFieldOrder(){
        return q;
    }

    /**
     * The first entry of the first row of the reduced matrix representative.
     * 
     * @return The first entry of the first row of the matrix.
     */
    public short getA(){
        return a;
    }

    /**
     * The second entry of the first row of the reduced matrix representative.
     * @return The second entry of the first row of the matrix.
     */
    public short getB(){
        return b;
    }

    /**
     * The first entry of the second row of the reduced matrix representative.
     * 
     * @return The first entry of the second row of the matrix.
     */
    public short getC(){
        return c;
    }

    /**
     * The second entry of the second row of the reduced matrix representative.
     * 
     * @return The second entry of the second row of the matrix.
     */
    public short getD(){
        return d;
    }

    /**
     * Computes the determinant of this element mod q, where q is the order of the base field.
     * 
     * @return The integer representative r, with 0&lt=r&ltq, for the determinant mod q of the matrix representative of this element.
     */
    public short determinant() {
        return Arithmetic.reduce(a*d-b*c, q);
    }
    
    /**
     * Returns the transposed element of GL2_PrimeField.
     * 
     * @return The transposed element of GL2_PrimeField.
     */
    public GL2_PrimeField transpose() {
        return new GL2_PrimeField(a, c, b, d, q);
    }

    /**
     * Returns the product of this element with the scalar matrix having the 
     * value a mod q along the diagonal.
     * 
     * @param a An <code>int</code> value not divisible by q.
     * @return The product of this element with the scalar matrix having the 
     * value a mod q along the diagonal.
     */
    public GL2_PrimeField scalarMultiplyBy(int a) {
        return null;
    }
    
    /**
     * @param h Any element of GL2_PrimeField that is operational with the element making the call. 
     */
    @Override
    public GL2_PrimeField leftProductBy(GL2_PrimeField h) {
        if (h.getFieldOrder()== q) {
        return new GL2_PrimeField(Arithmetic.reducedSum(Arithmetic.reducedProduct(h.getA(), a, q), Arithmetic.reducedProduct(h.getB(), c, q), q), 
                                  Arithmetic.reducedSum(Arithmetic.reducedProduct(h.getA(), b, q), Arithmetic.reducedProduct(h.getB(), d, q), q),
                                  Arithmetic.reducedSum(Arithmetic.reducedProduct(h.getC(), a, q), Arithmetic.reducedProduct(h.getD(), c, q), q), 
                                  Arithmetic.reducedSum(Arithmetic.reducedProduct(h.getC(), b, q), Arithmetic.reducedProduct(h.getD(), d, q), q), q);
        }
        return null;
    }

    /**
     * @param h Any element of GL2_PrimeField that is operational with the element making the call. 
     */
    @Override
    public GL2_PrimeField rightProductBy(GL2_PrimeField h) {
        if (h.getFieldOrder()== q) {
        return new GL2_PrimeField(Arithmetic.reducedSum(Arithmetic.reducedProduct(a, h.getA(), q), Arithmetic.reducedProduct(b, h.getC(), q), q), 
                                  Arithmetic.reducedSum(Arithmetic.reducedProduct(a, h.getB(), q), Arithmetic.reducedProduct(b, h.getD(), q), q),
                                  Arithmetic.reducedSum(Arithmetic.reducedProduct(c, h.getA(), q), Arithmetic.reducedProduct(d, h.getC(), q), q), 
                                  Arithmetic.reducedSum(Arithmetic.reducedProduct(c, h.getB(), q), Arithmetic.reducedProduct(d, h.getD(), q), q),q);
        }
        return null;
    }

    @Override
    public GL2_PrimeField getInverse() {
        short detInv= Arithmetic.findInverse(determinant(), q);
        return new GL2_PrimeField(detInv*d, -detInv*b, -detInv*c, detInv*a, q);
    }

    @Override
    public GL2_PrimeField getIdentity() {
        return new GL2_PrimeField(q);
    }

    /**
     * Returns true if the base field of h is the same as that of the element 
     * making the call.
     * 
     * @param h Any element of GL2_PrimeField.
     * @return true if the base field of h is the same as that of the element 
     * making the call.
     */
    @Override
    public boolean isOperationalWith(GL2_PrimeField h) {
        return q==h.getFieldOrder();
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
        return "[" + Integer.toString(a) + ", " + Integer.toString(b) + "; " + Integer.toString(c) + ", " + Integer.toString(d) + "]";
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
        return Short.toString(a)+" "+Short.toString(b)+" "+Short.toString(c)+" "+Short.toString(d)+" "+Short.toString(q);
    }

}
