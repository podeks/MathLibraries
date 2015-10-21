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

import java.util.Random;
import basic_operations.Arithmetic;
import api.Group;

/**
 * A class that models the group of non-singular projective
 * 2x2 matrices over a <code>short</code> integer prime field, implemented in 
 * the framework of the Group interface.  
 * 
 * <p>
 * Elements of this group are represented
 * internally by the <em>uniquely</em> defined matrix representative, modulo nonzero scalar 
 * multiplication, which has an entry of 1 as the first nonzero entry of the
 * first column, and non-negative integer entries less than q throughout.  The reduced matrix representative is computed at the time of 
 * construction.  This has the subsequent benefit of rapid equality testing
 * between two PGL2_PrimeField elements.  In particular, the <code>equals</code> method is
 * overridden in this class to test for equivalence of group elements by checking for equality between their
 * reduced matrix representatives.
 * </p>
 * 
 * <p>
 * Remark: The arithmetic operations mod q in this class are delegated to the 
 * class basic_operations.Arithmetic of the Arithmetic module.
 * </p>
 * 
 * @author pdokos
 */
public class PGL2_PrimeField implements Group<PGL2_PrimeField> {

    private short q;
    private short a;
    private short b;
    private short c;
    private short d;

    /**
     * Constructor for a PGL2_PrimeField object, invoked by specifying four integer representatives for 
     * the matrix entries, together with a prime number q for the order of base field.  The 
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
     * @param q any <code>short</code>
     */
    public PGL2_PrimeField(int a, int b, int c, int d, short q) {

        short[] ents = {Arithmetic.reduce(a, q), Arithmetic.reduce(b, q), Arithmetic.reduce(c, q), Arithmetic.reduce(d, q)};

        this.q = q;

        short prop;
        if (ents[0] != 0) {
            prop = Arithmetic.findInverse(ents[0], q);
            this.a = 1;
            this.c = Arithmetic.reducedProduct(ents[2], prop, q);
        } else {
            prop = Arithmetic.findInverse(ents[2], q);
            this.a = 0;
            this.c = 1;
        }

        this.b = Arithmetic.reducedProduct(ents[1], prop, q);
        this.d = Arithmetic.reducedProduct(ents[3], prop, q);

    }
    
    /**
     * Constructor for a PGL2_PrimeField object, invoked by specifying a 2x2 
     * <code>int</code> array, together with a prime number q for the order of 
     * base field.
     * 
     * <p>
     * It is the 
     * responsibility of the client to ensure that the parameter q is prime, and
     * that the matrix is non-singular mod q.
     * </p>
     * 
     * @param mtx A 2x2 <code>int</code> array.
     * @param q a <code>short</code> integer prime.
     */
    public PGL2_PrimeField(int[][] mtx, short q) {
        this(mtx[0][0], mtx[0][1], mtx[1][0], mtx[1][1], q);
    }

    /**
     * Constructor for the identity element of PGL2_PrimeField over the field of 
     * q elements.  
     * 
     * <p>
     * It is the responsibility of the client to ensure that the 
     * parameter q is prime.
     * </p>
     * 
     * @param q any <code>short</code>
     */
    public PGL2_PrimeField(short q) {

        this.q = q;

        a = 1;
        b = 0;
        c = 0;
        d = 1;
    }
    
    /**
     * Constructor for an element of PGL2_PrimeField, defined by a specified element of
     * GL2_PrimeField.
     * 
     * @param g An element of GL2_PrimeField.
     */
    public PGL2_PrimeField(GL2_PrimeField g) {
        this(g.getA(), g.getB(), g.getC(), g.getD(), g.getFieldOrder());
    }
    
    /**
     * A static utility for producing the identity element of PGL2_PrimeField 
     * over the field of q elements, where q is prime.
     * 
     * @param q a <code>short</code> integer prime.
     * @return The identity element of PGL2_PrimeField.
     */
    public static PGL2_PrimeField constructIdentity(short q) {
        return new PGL2_PrimeField(q);
    }
    
    /**
     * A static utility for producing a random element of PGL2_PrimeField 
     * over the field of q elements, where q is prime.
     * 
     * @param q a <code>short</code> integer prime.
     * @return A random element of PGL2_PrimeField.
     */
    public static PGL2_PrimeField constructRandomElement(short q) {
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
        return new PGL2_PrimeField(a, b, c, d, q);
    }
    
    /**
     * An override of the <code>equals</code> method so as to return
     * true if: (i) h is an instance of PGL2_PrimeField over the same prime field of order 
     * q, and (ii) the corresponding entries of the reduced matrix representatives
     * are equal.
     * 
     * @param h Any object.
     * 
     * @return true if: (i) h is an instance of PGL2_PrimeField over the same prime field of order 
     * q, and (ii) the corresponding entries of the reduced matrix representatives
     * are equal.
     */
    @Override
    public boolean equals(Object h) {
        if (h instanceof PGL2_PrimeField) {
            return ((PGL2_PrimeField) h).getA() == this.a
                    && ((PGL2_PrimeField) h).getB() == this.b
                    && ((PGL2_PrimeField) h).getC() == this.c
                    && ((PGL2_PrimeField) h).getD() == this.d
                    && ((PGL2_PrimeField) h).getFieldOrder() == this.q;
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
        hash = 7 * hash + this.a;
        hash = 11 * hash + this.b;
        hash = 13 * hash + this.c;
        hash = 17 * hash + this.d;
        hash = 19 * hash + this.q;
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
     * @return The first entry of the first row of the reduced matrix representative.
     */
    public short getA(){
        return a;
    }

    /**
     * The second entry of the first row of the reduced matrix representative.
     * @return The second entry of the first row of the reduced matrix representative.
     */
    public short getB(){
        return b;
    }

    /**
     * The first entry of the second row of the reduced matrix representative.
     * 
     * @return The first entry of the second row of the reduced matrix representative.
     */
    public short getC(){
        return c;
    }

    /**
     * The second entry of the second row of the reduced matrix representative.
     * 
     * @return The second entry of the second row of the reduced matrix representative.
     */
    public short getD(){
        return d;
    }
    
    /**
     * Computes the determinant of this element (which is only defined up to 
     * multiplication by non-zero <em>squares</em> in the field Z/qZ). 
     * 
     * @return The integer representative r, with 0&lt=r&ltq, for
     * the determinant of the reduced matrix representative mod q.
     */
    public short determinant() {
        return Arithmetic.reduce(a*d-b*c, q);
    }
    
    /**
     * Returns the transposed element of PGL2_PrimeField.
     * @return The transposed element of PGL2_PrimeField.
     */
    public PGL2_PrimeField transpose() {
        return new PGL2_PrimeField(a, c, b, d, q);
    }

    /**
     * @param h Any element of PGL2_PrimeField that is operational with the element making the call. 
     */
    @Override
    public PGL2_PrimeField leftProductBy(PGL2_PrimeField h) {
        if (h.getFieldOrder()== q) {
        return new PGL2_PrimeField(Arithmetic.reducedSum(Arithmetic.reducedProduct(h.getA(), a, q), Arithmetic.reducedProduct(h.getB(), c, q), q), 
                                   Arithmetic.reducedSum(Arithmetic.reducedProduct(h.getA(), b, q), Arithmetic.reducedProduct(h.getB(), d, q), q),
                                   Arithmetic.reducedSum(Arithmetic.reducedProduct(h.getC(), a, q), Arithmetic.reducedProduct(h.getD(), c, q), q), 
                                   Arithmetic.reducedSum(Arithmetic.reducedProduct(h.getC(), b, q), Arithmetic.reducedProduct(h.getD(), d, q), q), q);
        }
        return null;
    }

    /**
     * @param h Any element of PGL2_PrimeField that is operational with the element making the call. 
     */
    @Override
    public PGL2_PrimeField rightProductBy(PGL2_PrimeField h) {
        if (h.getFieldOrder()== q) {
        return new PGL2_PrimeField(Arithmetic.reducedSum(Arithmetic.reducedProduct(a, h.getA(), q), Arithmetic.reducedProduct(b, h.getC(), q), q), 
                                   Arithmetic.reducedSum(Arithmetic.reducedProduct(a, h.getB(), q), Arithmetic.reducedProduct(b, h.getD(), q), q),
                                   Arithmetic.reducedSum(Arithmetic.reducedProduct(c, h.getA(), q), Arithmetic.reducedProduct(d, h.getC(), q), q), 
                                   Arithmetic.reducedSum(Arithmetic.reducedProduct(c, h.getB(), q), Arithmetic.reducedProduct(d, h.getD(), q), q),q);
        }
        return null;
    }

    @Override
    public PGL2_PrimeField getInverse() {
        return new PGL2_PrimeField(d, -b, -c, a, q);
    }

    @Override
    public PGL2_PrimeField getIdentity() {
        return new PGL2_PrimeField(q);
    }
    
    /**
     * Returns true if the base field of h is the same as that of the element 
     * making the call.
     * 
     * @param h Any element of PGL2_PrimeField.
     * @return true if the base field of h is the same as that of the element 
     * making the call.
     */
    @Override
    public boolean isOperationalWith(PGL2_PrimeField h) {
        return q==h.getFieldOrder();
    }
    
    public SymmetricGroup getPermutationRepr() {
        int[] rep = new int[q+1];
        for (int i=0; i<q; i++) {
            
            int num = Arithmetic.reducedSum(a*i, b, q);
            int den = Arithmetic.reducedSum(c*i, d, q);
            if (den != 0) {
                rep[i] = Arithmetic.reduce(num * Arithmetic.findInverse(den, q), q); //safe because terms are short size
            } else 
                rep[i] = q;
            
            if (c !=0 )
                rep[q] = Arithmetic.reduce(a * Arithmetic.findInverse(c, q), q);
            else 
                rep[q]=q;
        }
        SymmetricGroup permRep = new SymmetricGroup(rep);
        return permRep;
    }
    
    /**
     * Overrides the <code>toString</code> method so as to return a string 
     * representation of the reduced matrix representative of the element 
     * making the call, in the format of a MATLAB matrix (e.g.&#160"[1, 1; 0, 1]").
     * 
     * <p>
     * Note that the base field order is lost in this representation.
     * </p>
     * 
     * @return A String representation of the reduced matrix representative of 
     * the element making the call, in the format of a MATLAB matrix.
     */
    @Override
    public String toString() {
        return "[" + Integer.toString(a) + ", " + Integer.toString(b) + "; " + Integer.toString(c) + ", " + Integer.toString(d) + "]";
    }
    
    /**
     * Returns a representation of the element making the call as a String
     * consisting of the four entries of the reduced matrix representative, 
     * followed by the field order, all separated by single spaces.
     * 
     * @return A String representation of the element making the call
     * consisting of the four entries of the reduced matrix representative, 
     * followed by the field order, all separated by single spaces.
     */
    public String toUnpunctuatedString() {
        return Integer.toString(a)+" "+Integer.toString(b)+" "+Integer.toString(c)+" "+Integer.toString(d)+" "+Integer.toString(q);
    }

}


/*
    public static Set<PGL2_PrimeField> generateCompletePGL2Set(short q){
        Set<PGL2_PrimeField> set = new HashSet<PGL2_PrimeField>();
        for (int i=0; i<q; i++) {
            for (int j=1; j<q; j++) {
                set.add(new PGL2_PrimeField(0, j, 1, i, q));
            }
        }
        for (int i=0; i<q; i++) {
            for (int j=0; j<q; j++) {
                int prod = (i*j)%q;
                for (int k=0; k<q; k++) {                    
                    if (k != prod) {
                        set.add(new PGL2_PrimeField(1, i, j, k, q));
                    }
                }
            }
        }
        return set;
    }
    
        public static Set<PGL2_PrimeField> generatePSL2Subset(short q){
        Set<PGL2_PrimeField> set = new HashSet<PGL2_PrimeField>();
        Set<Integer> squares = new HashSet<Integer>();

        for (int i=0; i<q; i++) {
            for (int j=1; j<=(q/2); j++) {
                int sq = (j*j)%q;
                set.add(new PGL2_PrimeField(0, -sq, 1, i, q));
                squares.add(sq);
            }
        }
        for (int i=0; i<q; i++) {
            for (int j=0; j<q; j++) {
                int prod = (i*j)%q;
                for (int k=0; k<q; k++) {                    
                    if (squares.contains((int) Arithmetic.reduce(k - prod, q))) {
                        set.add(new PGL2_PrimeField(1, i, j, k, q));
                    }
                }
            }
        }
        return set;
    }
    */