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
package groups;

import java.util.Arrays;
import basic_operations.Arithmetic;
import basic_operations.ReducedMatrixOperations;
import api.Group;

/**
 * A class that models the group of non-singular 
 * square matrices over a <code>short</code> integer prime field, implemented in 
 * the framework of the Group interface.
 * 
 * <p>
 * Elements of the group are represented internally by the <em>uniquely</em> defined 
 * integer matrix representative mod q whose entries are non-negative and less 
 * than q.  The reduced matrix entries are computed at the time of 
 * construction.  This has the subsequent benefit of rapid equality testing
 * between two GLn_PrimeField elements.  In particular, the <code>equals</code> 
 * method is overridden in this class to test for equivalence of group elements 
 * by checking for equality between corresponding matrix entries.
 * </p>
 * 
 * <p>
 * Remark: The matrix computations mod q in this class are delegated to the 
 * class basic_operations.ReducedMatrixOperations of the Arithmetic module.
 * </p>
 * 
 * @author pdokos
 */
public class GLn_PrimeField implements Group<GLn_PrimeField> {

    private short q; //The order of the field
    private short[][] rep;
    

    /**
     * Constructor for a GLn_PrimeField object, invoked by specifying a square two-dimensional array for 
     * the matrix entries, and a prime number q for the order of base field.
     * 
     * <p>
     * It is the 
     * responsibility of the client to ensure that the parameter q is prime, and
     * that the given matrix is non-singular mod q.
     * </p>
     * 
     * @param sqMtx a two-dimensional <code>int</code> array which, as a matrix, is non-singular mod q.
     * @param q any <code>short</code> prime
     */
    public GLn_PrimeField(int[][] sqMtx, short q) {
        this.q = q;
        rep = ReducedMatrixOperations.reduce(sqMtx, q);
    }
    
    /**
     * Constructor for a GLn_PrimeField object, invoked by specifying a square two-dimensional array for 
     * the matrix entries, and a prime number q for the order of base field.
     * 
     * <p>
     * It is the 
     * responsibility of the client to ensure that the parameter q is prime, and
     * that the given matrix is non-singular mod q.
     * </p>
     * 
     * @param sqMtx a two-dimensional <code>short</code> array which, as a matrix, is non-singular mod q.
     * @param q any <code>short</code> prime
     */
    public GLn_PrimeField(short[][] sqMtx, short q) {
        this.q = q;
        rep = ReducedMatrixOperations.reduce(sqMtx, q);
    }

    /**
     * Constructor for the identity element of GLn_PrimeField for a space of dimension n over the field of 
     * q elements.  
     * 
     * <p>
     * It is the responsibility of the client to ensure that the 
     * parameter q is prime and that n is positive.
     * </p>
     * 
     * @param n a positive <code>int</code> value.
     * @param q a <code>short</code> integer prime.
     */
    public GLn_PrimeField(int n, short q) {
        this.q = q;
        rep = ReducedMatrixOperations.constructIdentityMtx(n);
    }

    
    /**
     * A static helper for producing the identity element of GLn_PrimeField 
     * for a space of dimension n
     * over the field of q elements, where q is prime.
     * 
     * @param n a positive <code>int</code> value.
     * @param q a <code>short</code> integer prime.
     * 
     * @return The identity element of GLn_PrimeField.
     */
    public static GLn_PrimeField constructIdentity(int n, short q) {
        return new GLn_PrimeField(n, q);
    }
    
    /**
     * A static helper for producing elementary matrices, i.e.&#160those which
     * have ones along the diagonal and at the (h,k)-entry, and have zeros everywhere 
     * else.
     * 
     * @param n Any positive <code>int</code> value.
     * @param q Any <code>short</code> integer prime.
     * @param h A positive <code>int</code> value, less than or equal to n.
     * @param k A positive <code>int</code> value, less than or equal to n, and not equal to k.
     * @return The elementary matrix associated with (h,k).
     */
    public static GLn_PrimeField constructElementaryMtx(int n, short q, int h, int k) {
        return new GLn_PrimeField(ReducedMatrixOperations.constructElementaryMtx(n, h, k), q);
    }

    /**
     * A static helper for producing a random element of GLn_PrimeField 
     * over the field of q elements, where q is prime.
     * 
     * @param n Any positive <code>int</code> value.
     * @param q A <code>short</code> integer prime.
     * @return A random element of GLn_PrimeField.
     */
    public static GLn_PrimeField constructRandomElement(int n, short q) {
        return new GLn_PrimeField(ReducedMatrixOperations.constructRandomInvertibleElement(n, q), q);
    }    

    /**
     * An override of the <code>equals</code> method which returns
     * true if both: (i) h is an instance of GLn_PrimeField over the same prime
     * and of the same dimension as the element making the call, and (ii) the 
     * corresponding entries of the reduced matrix representatives are equal.
     * 
     * @param h Any object.
     * 
     * @return <code>true</code> if both: (i) h is an instance of GLn_PrimeField over the same prime field
     * and of the same dimension as the element making the call, and (ii) the 
     * corresponding entries of the reduced matrix representatives are equal.
     */
    @Override
    public boolean equals(Object h) {
        if (h instanceof GLn_PrimeField) {
            if (((GLn_PrimeField) h).getDimension() == getDimension() && ((GLn_PrimeField) h).getFieldOrder() == q) {
                return Arrays.deepEquals(rep, ((GLn_PrimeField) h).getRep());
            }
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
        int hash = 3;
        hash = 29 * hash + this.getDimension();
        hash = 29 * hash + this.q;
        hash = 29 * hash + Arrays.deepHashCode(this.rep);
        return hash;
    }

    /**
     * Returns the dimension of the vector space on which the element acts.
     * 
     * @return The dimension of the vector space on which the element acts.
     */
    public int getDimension() {
        return rep.length;
    }

    /**
     * Returns the order of the base field.
     * 
     * @return The order of the base field.
     */
    public short getFieldOrder() {
        return q;
    }

    /**
     * Returns the entry in i-th row and j-th column of the reduced matrix 
     * representative.
     * 
     * @param i A positive <code>int</code> value less than or equal to n.
     * @param j A positive <code>int</code> value less than or equal to n.
     * 
     * @return The entry in i-th row and j-th column of the reduced matrix 
     * representative.
     */
    public short getEntry(int i, int j) {
        return rep[i][j];
    }

    private short[][] getRep() {
        return rep;
    }

    /**
     * Computes the determinant of this element mod q, where q is the order of the base field.
     * 
     * @return The integer representative r, with 0&lt=r&ltq, for the determinant mod q of the matrix representative of this element.
     */
    public short determinant() {
        return ReducedMatrixOperations.determinantModQ(rep, q);
    }
    
    /**
     * Returns the transposed element of GLn_PrimeField.
     * 
     * @return The transposed element of GLn_PrimeField.
     */
    public GLn_PrimeField transpose() {
        return new GLn_PrimeField(ReducedMatrixOperations.transpose(rep), q);
    }

    /**
     * Returns the product of this element with the scalar matrix having the 
     * value a mod q along the diagonal.
     * 
     * @param a An <code>int</code> value not divisible by q.
     * @return The product of this element with the scalar matrix having the 
     * value a mod q along the diagonal.
     */
    public GLn_PrimeField scalarMultiplyBy(int a) {
        short aReduced = Arithmetic.reduce(a, q);
        if (!(aReduced==0)) {
            return new GLn_PrimeField(ReducedMatrixOperations.scalarMultiply(aReduced, rep, q), q);
        }
        throw new ArithmeticException("Illegal Operation.");
    }
    
    @Override
    public GLn_PrimeField getInverse() {
        return new GLn_PrimeField(ReducedMatrixOperations.inverseModQ(rep, q), q);
    }    

    /**
     * @param h Any element of GLn_PrimeField that is operational with the element making the call. 
     */
    @Override
    public GLn_PrimeField leftProductBy(GLn_PrimeField h) {
        if (h.getDimension() == getDimension()) {
            return new GLn_PrimeField(ReducedMatrixOperations.reducedProduct(h.getRep(), rep, q), q);
        }
        return null;
    }

    /**
     * @param h Any element of GLn_PrimeField that is operational with the element making the call. 
     */
    @Override
    public GLn_PrimeField rightProductBy(GLn_PrimeField h) {
        if (h.getDimension() == getDimension()) {
            return new GLn_PrimeField(ReducedMatrixOperations.reducedProduct(rep, h.getRep(), q), q);
        }
        return null;
    }

    @Override
    public GLn_PrimeField getIdentity() {
        return new GLn_PrimeField(getDimension(), q);
    }

    /**
     * Returns true if the vector space associated with h is over the same field
     * and of the same dimension as that of the element making the call.
     * 
     * @param h Any element of GLn_PrimeField.
     * @return true if the vector space associated with h is over the same field
     * and of the same dimension as that of the element making the call.
     */
    @Override
    public boolean isOperationalWith(GLn_PrimeField h) {
        return q == h.getFieldOrder() && getDimension() == h.getDimension();
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
        StringBuilder sb = new StringBuilder();
        int dimension = getDimension();
        sb.append("[");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                sb.append(rep[i][j]);
                if (j != dimension - 1) {
                    sb.append(", ");
                }
            }
            if (i != dimension - 1) {
                sb.append("; ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * Returns a representation of the element making the call as a String
     * consisting of n^2 + 1 integers separated by single spaces. The first n^2
     * integers of the list are the entries of the reduced matrix representative, 
     * listed by concatenating the rows of the matrix from top to bottom.  
     * The last entry is the order of the base field.
     * 
     * @return A String representation of the element making the call
     * consisting of n^2 + 1 integers separated by single spaces.
     */
    public String toUnpunctuatedString() {
        StringBuilder sb = new StringBuilder();
        int dimension = getDimension();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                sb.append(rep[i][j]).append(' ');
                //if (!((j == dimension-1) && (i == dimension-1))) sb.append(" ")
            }
        }
        sb.append(q);
        return sb.toString();
    }

}
