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
package fastgroups;

import api.Group;
import finitefields.ByteField;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author pdokos
 */
public class GL3ByteField implements Group<GL3ByteField>{
    
    private byte a11, a12, a13;
    private byte a21, a22, a23;
    private byte a31, a32, a33;

    
    private short q;
    private ByteField f;

    public GL3ByteField(byte a11,byte a12,byte a13,
            byte a21,byte a22,byte a23,
            byte a31,byte a32,byte a33, ByteField f) {
        this.f = f;
        q= (short) f.getOrder();
        this.a11 = a11;
        this.a12 = a12;
        this.a13 = a13;
        this.a21 = a21;
        this.a22 = a22;
        this.a23 = a23;
        this.a31 = a31;
        this.a32 = a32;
        this.a33 = a33;

     }
    
    public GL3ByteField(ByteField.Element a11, ByteField.Element a12, ByteField.Element a13,
            ByteField.Element a21, ByteField.Element a22, ByteField.Element a23,
            ByteField.Element a31, ByteField.Element a32, ByteField.Element a33, ByteField f) {

        this(a11.getIndex(), a12.getIndex(), a13.getIndex(), 
                a21.getIndex(), a22.getIndex(), a23.getIndex(),
                a31.getIndex(), a32.getIndex(), a33.getIndex(), f);
    }

     /**
     * Constructor for the identity element of GL3ByteField over the field f.  
     * 
     * @param f any <code>ByteField</code>.
     */
    public GL3ByteField(ByteField f) {

        this((byte) 1,(byte) 0,(byte) 0,
                (byte) 0,(byte) 1,(byte) 0,
                (byte) 0,(byte) 0,(byte) 1, f);

    }
    
    /**
     * A static helper for producing the identity element of GL3ByteField
     * over the field f.
     * 
     * @param f a <code>ByteField</code>.
     * @return The identity element of GL3ByteField.
     */
    public static GL3ByteField constructIdentity(ByteField f) {
        return new GL3ByteField(f);
    }
    
    public static GL3ByteField convert(GLnByteField g) {
        return new GL3ByteField(g.getEntry(0, 0), g.getEntry(0, 1), g.getEntry(0, 2), 
                g.getEntry(1, 0), g.getEntry(1, 1), g.getEntry(1, 2),
                g.getEntry(2, 0), g.getEntry(2, 1), g.getEntry(2, 2),
                g.getField() );
    }
    
    public static Set<GL3ByteField> convert(Set<GLnByteField> set) {
        Set<GL3ByteField> newSet = new HashSet<GL3ByteField>(set.size()+1, 1.0f);
        for (GLnByteField g : set) {
            newSet.add(convert(g));
        }
        return newSet;
    }

    /**
     * An override of the <code>equals</code> method so as to return
     * true if: (i) h is an instance of GL3ByteField over an equal ByteField f, 
     * and (ii) the corresponding entries of the two matrices are equal.
     * 
     * @param h Any object.
     * 
     * @return true if: (i) h is an instance of GL3ByteField over an equal 
     * ByteField f, and (ii) the corresponding entries of the two matrices are equal.
     */
    @Override
    public boolean equals(Object h) {
        if (h instanceof GL3ByteField) {
            return (a11==((GL3ByteField) h).a11 &&
                    a12==((GL3ByteField) h).a12 &&
                    a13==((GL3ByteField) h).a13 &&
                    a21==((GL3ByteField) h).a21 &&
                    a22==((GL3ByteField) h).a22 &&
                    a23==((GL3ByteField) h).a23 &&
                    a31==((GL3ByteField) h).a31 &&
                    a32==((GL3ByteField) h).a32 &&
                    a33==((GL3ByteField) h).a33 &&
                    q==((GL3ByteField) h).q);
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
        hash = q * hash + this.a11;
        hash = q * hash + this.a12;
        hash = q * hash + this.a13;
        hash = q * hash + this.a21;
        hash = q * hash + this.a22;
        hash = q * hash + this.a23;
        hash = q * hash + this.a31;
        hash = q * hash + this.a32;
        hash = q * hash + this.a33;
        return hash;
    }
    
    public void project() {
        byte factor;
        if (a11!=f.zero().getIndex()) {
            factor = f.inverse(a11);
        } else if (a21!=f.zero().getIndex()) {
            factor = f.inverse(a21);
        } else {
            factor = f.inverse(a31);
        }
        scale(factor);
    }
    
    private void scale(byte factor) {
        a11=f.mult(factor, a11);
        a12=f.mult(factor, a12);
        a13=f.mult(factor, a13);
        a21=f.mult(factor, a21);
        a22=f.mult(factor, a22);
        a23=f.mult(factor, a23);
        a31=f.mult(factor, a31);
        a32=f.mult(factor, a32);
        a33=f.mult(factor, a33);
    }
    
    
    public ByteField getField() {
        return f;
    }
    
    public int getFieldOrder(){
        return f.getOrder();
    }


    /**
     * Computes the determinant of this element mod q, where q is the order of the base field.
     * 
     * @return The integer representative r, with 0&lt=r&ltq, for the determinant mod q of the matrix representative of this element.
     */
//    public byte determinant() {
//        return f.add(f.mult(a, d), f.negative(f.mult(b, c)));
//    }
    
    /**
     * Returns the transposed element of GL2_PrimeField.
     * 
     * @return The transposed element of GL2_PrimeField.
     */
//    public GL3ByteField transpose() {
//        return new GL3ByteField(a, c, b, d, f);
//    }
    
    /**
     * @param h Any element of GL3ByteField that is operational with the element making the call. 
     */
    @Override
    public GL3ByteField leftProductBy(GL3ByteField h) {
        return h.rightProductBy(this);
    }

    /**
     * @param h Any element of GL3ByteField that is operational with the element making the call. 
     */
    @Override
    public GL3ByteField rightProductBy(GL3ByteField h) {
        if (h.q == q) {
            return new GL3ByteField(f.add(f.add(f.mult(a11, h.a11), f.mult(a12, h.a21)), f.mult(a13, h.a31)),
                    f.add(f.add(f.mult(a11, h.a12), f.mult(a12, h.a22)), f.mult(a13, h.a32)),
                    f.add(f.add(f.mult(a11, h.a13), f.mult(a12, h.a23)), f.mult(a13, h.a33)),
                    f.add(f.add(f.mult(a21, h.a11), f.mult(a22, h.a21)), f.mult(a23, h.a31)),
                    f.add(f.add(f.mult(a21, h.a12), f.mult(a22, h.a22)), f.mult(a23, h.a32)),
                    f.add(f.add(f.mult(a21, h.a13), f.mult(a22, h.a23)), f.mult(a23, h.a33)),
                    f.add(f.add(f.mult(a31, h.a11), f.mult(a32, h.a21)), f.mult(a33, h.a31)),
                    f.add(f.add(f.mult(a31, h.a12), f.mult(a32, h.a22)), f.mult(a33, h.a32)),
                    f.add(f.add(f.mult(a31, h.a13), f.mult(a32, h.a23)), f.mult(a33, h.a33)),
                    f);
        }
        return null;
    }

    @Override
    public GL3ByteField getInverse() {
        ByteField.Element[][] g = GLnByteField.getInverse(new ByteField.Element[][]{{f.getElement(a11), f.getElement(a12), f.getElement(a13)}, 
            {f.getElement(a21), f.getElement(a22), f.getElement(a23)},
            {f.getElement(a31), f.getElement(a32), f.getElement(a33)}}, f);
        return new GL3ByteField(g[0][0].getIndex(), g[0][1].getIndex(), g[0][2].getIndex(), 
        g[1][0].getIndex(), g[1][1].getIndex(), g[1][2].getIndex(), 
        g[2][0].getIndex(), g[2][1].getIndex(), g[2][2].getIndex(), 
        f);
    }

    @Override
    public GL3ByteField getIdentity() {
        return new GL3ByteField(f);
    }

    /**
     * Returns true if the base field of h is the same as that of the element 
     * making the call.
     * 
     * @param h Any element of GL3ByteField.
     * @return true if the base field of h is the same as that of the element 
     * making the call.
     */
    @Override
    public boolean isOperationalWith(GL3ByteField h) {
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
        return "[" + f.getElement(a11).toString() + ", " + f.getElement(a12).toString() + ", " + f.getElement(a13).toString() + "; " 
                 + f.getElement(a21).toString() + ", " + f.getElement(a22).toString() + ", " + f.getElement(a23).toString() + "; " 
                 + f.getElement(a31).toString() + ", " + f.getElement(a32).toString() + ", " + f.getElement(a33).toString() + "]";
    }
    
    /**
     * Returns a representation of the element making the call as a String
     * consisting of the byte entries of the matrix, followed by the field order,
     * all separated by single spaces.
     * 
     * @return A String representation of the element making the call
     * consisting of the four entries of the matrix, followed by the field order,
     * all separated by single spaces.
     */
    public String toUnpunctuatedString() {
        return a11+" "+a12+" "+a13+" "+a21+" "+a22+" "+a23+" "+a31+" "+a32+" "+a33+" "+q;
    }

}
