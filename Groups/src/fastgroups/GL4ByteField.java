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
public class GL4ByteField implements Group<GL4ByteField>{
    
    private byte a11, a12, a13, a14;
    private byte a21, a22, a23, a24;
    private byte a31, a32, a33, a34;
    private byte a41, a42, a43, a44;

    
    private short q;
    private ByteField f;

    public GL4ByteField(byte a11,byte a12,byte a13,byte a14,
            byte a21,byte a22,byte a23,byte a24,
            byte a31,byte a32,byte a33, byte a34,
            byte a41,byte a42,byte a43, byte a44, ByteField f) {
        this.f = f;
        q= (short) f.getOrder();
        this.a11 = a11;
        this.a12 = a12;
        this.a13 = a13;
        this.a14 = a14;
        this.a21 = a21;
        this.a22 = a22;
        this.a23 = a23;
        this.a24 = a24;
        this.a31 = a31;
        this.a32 = a32;
        this.a33 = a33;
        this.a34 = a34;
        this.a41 = a41;
        this.a42 = a42;
        this.a43 = a43;
        this.a44 = a44;

     }
    
    public GL4ByteField(ByteField.Element a11, ByteField.Element a12, ByteField.Element a13, ByteField.Element a14,
            ByteField.Element a21, ByteField.Element a22, ByteField.Element a23, ByteField.Element a24,
            ByteField.Element a31, ByteField.Element a32, ByteField.Element a33, ByteField.Element a34,
            ByteField.Element a41, ByteField.Element a42, ByteField.Element a43, ByteField.Element a44,
            ByteField f) {

        this(a11.getIndex(), a12.getIndex(), a13.getIndex(), a14.getIndex(),
                a21.getIndex(), a22.getIndex(), a23.getIndex(),a24.getIndex(),
                a31.getIndex(), a32.getIndex(), a33.getIndex(),a34.getIndex(),
                a41.getIndex(), a42.getIndex(), a43.getIndex(),a44.getIndex(), f);
    }

    
    public GL4ByteField(ByteField.Element[][] e, ByteField f) {
        this(e[0][0], e[0][1], e[0][2], e[0][3],
        e[1][0], e[1][1], e[1][2], e[1][3],
        e[2][0], e[2][1], e[2][2], e[2][3],
        e[3][0], e[3][1], e[3][2], e[3][3], f);
    }
    
    /**
     * Constructor for the identity element of GL4ByteField over the field f.  
     * 
     * @param f any <code>ByteField</code>.
     */
    public GL4ByteField(ByteField f) {

        this((byte) 1,(byte) 0,(byte) 0,(byte) 0,
                (byte) 0,(byte) 1,(byte) 0,(byte) 0,
                (byte) 0,(byte) 0,(byte) 1, (byte) 0,
                (byte) 0,(byte) 0,(byte) 0,(byte) 1,
                f);

    }
    
    /**
     * A static helper for producing the identity element of GL4ByteField 
     * over the field f.
     * 
     * @param f a <code>ByteField</code>.
     * @return The identity element of GL4ByteField.
     */
    public static GL4ByteField constructIdentity(ByteField f) {
        return new GL4ByteField(f);
    }
    
    public static GL4ByteField convert(GLnByteField g) {
        return new GL4ByteField(g.getEntry(0, 0), g.getEntry(0, 1), g.getEntry(0, 2), g.getEntry(0, 3), 
                g.getEntry(1, 0), g.getEntry(1, 1), g.getEntry(1, 2), g.getEntry(1, 3),
                g.getEntry(2, 0), g.getEntry(2, 1), g.getEntry(2, 2), g.getEntry(2, 3),
                g.getEntry(3, 0), g.getEntry(3, 1), g.getEntry(3, 2), g.getEntry(3, 3),
                g.getField() );
    }

    public static Set<GL4ByteField> convert(Set<GLnByteField> set) {
        Set<GL4ByteField> newSet = new HashSet<GL4ByteField>(set.size()+1, 1.0f);
        for (GLnByteField g : set) {
            newSet.add(convert(g));
        }
        return newSet;
    }

    /**
     * An override of the <code>equals</code> method so as to return
     * true if: (i) h is an instance of GL4ByteField over an equal ByteField f, 
     * and (ii) the corresponding entries of the two matrices are equal.
     * 
     * @param h Any object.
     * 
     * @return true if: (i) h is an instance of GL4ByteField over an equal 
     * ByteField f, and (ii) the corresponding entries of the two matrices are equal.
     */
    @Override
    public boolean equals(Object h) {
        if (h instanceof GL4ByteField) {
            return (a11==((GL4ByteField) h).a11 &&
                    a12==((GL4ByteField) h).a12 &&
                    a13==((GL4ByteField) h).a13 &&
                    a14==((GL4ByteField) h).a14 &&
                    a21==((GL4ByteField) h).a21 &&
                    a22==((GL4ByteField) h).a22 &&
                    a23==((GL4ByteField) h).a23 &&
                    a24==((GL4ByteField) h).a24 &&
                    a31==((GL4ByteField) h).a31 &&
                    a32==((GL4ByteField) h).a32 &&
                    a33==((GL4ByteField) h).a33 &&
                    a34==((GL4ByteField) h).a34 &&
                    a41==((GL4ByteField) h).a41 &&
                    a42==((GL4ByteField) h).a42 &&
                    a43==((GL4ByteField) h).a43 &&
                    a44==((GL4ByteField) h).a44 &&
                    q==((GL4ByteField) h).q);
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
        hash = q * hash + this.a14;
        hash = q * hash + this.a21;
        hash = q * hash + this.a22;
        hash = q * hash + this.a23;
        hash = q * hash + this.a24;
        hash = q * hash + this.a31;
        hash = q * hash + this.a32;
        hash = q * hash + this.a33;
        hash = q * hash + this.a34;
        hash = q * hash + this.a41;
        hash = q * hash + this.a42;
        hash = q * hash + this.a43;
        hash = q * hash + this.a44;
        //hash = 59 * hash + this.q;
        return hash;
    }
    
    public void project() {
        byte factor;
        if (a11!=f.zero().getIndex()) {
            factor = f.inverse(a11);
        } else if (a21!=f.zero().getIndex()) {
            factor = f.inverse(a21);
        } else if (a31!=f.zero().getIndex()) {
            factor = f.inverse(a31);
        } else {
            factor = f.inverse(a41);
        }
        scale(factor);
    }
    
    private void scale(byte factor) {
        a11=f.mult(factor, a11);
        a12=f.mult(factor, a12);
        a13=f.mult(factor, a13);
        a14=f.mult(factor, a14);
        a21=f.mult(factor, a21);
        a22=f.mult(factor, a22);
        a23=f.mult(factor, a23);
        a24=f.mult(factor, a24);
        a31=f.mult(factor, a31);
        a32=f.mult(factor, a32);
        a33=f.mult(factor, a33);
        a34=f.mult(factor, a34);
        a41=f.mult(factor, a41);
        a42=f.mult(factor, a42);
        a43=f.mult(factor, a43);
        a44=f.mult(factor, a44);
    }
    
    /**
     * Returns the order of the base field.
     * 
     * @return The order of the base field.
     */
    public int getFieldOrder(){
        return f.getOrder();
    }

    public ByteField getField() {
        return f;
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
     * @param h Any element of GL4ByteField that is operational with the element making the call. 
     */
    @Override
    public GL4ByteField leftProductBy(GL4ByteField h) {
        return h.rightProductBy(this);
    }

    /**
     * @param h Any element of GL4ByteField that is operational with the element making the call. 
     */
    @Override
    public GL4ByteField rightProductBy(GL4ByteField h) {
        if (h.getFieldOrder() == q) {
            return new GL4ByteField(f.add(f.add(f.add(f.mult(a11, h.a11), f.mult(a12, h.a21)), f.mult(a13, h.a31)), f.mult(a14, h.a41)),
                    f.add(f.add(f.add(f.mult(a11, h.a12), f.mult(a12, h.a22)), f.mult(a13, h.a32)), f.mult(a14, h.a42)),
                    f.add(f.add(f.add(f.mult(a11, h.a13), f.mult(a12, h.a23)), f.mult(a13, h.a33)), f.mult(a14, h.a43)),
                    f.add(f.add(f.add(f.mult(a11, h.a14), f.mult(a12, h.a24)), f.mult(a13, h.a34)), f.mult(a14, h.a44)),
                    f.add(f.add(f.add(f.mult(a21, h.a11), f.mult(a22, h.a21)), f.mult(a23, h.a31)), f.mult(a24, h.a41)),
                    f.add(f.add(f.add(f.mult(a21, h.a12), f.mult(a22, h.a22)), f.mult(a23, h.a32)), f.mult(a24, h.a42)),
                    f.add(f.add(f.add(f.mult(a21, h.a13), f.mult(a22, h.a23)), f.mult(a23, h.a33)), f.mult(a24, h.a43)),
                    f.add(f.add(f.add(f.mult(a21, h.a14), f.mult(a22, h.a24)), f.mult(a23, h.a34)), f.mult(a24, h.a44)),
                    f.add(f.add(f.add(f.mult(a31, h.a11), f.mult(a32, h.a21)), f.mult(a33, h.a31)), f.mult(a34, h.a41)),
                    f.add(f.add(f.add(f.mult(a31, h.a12), f.mult(a32, h.a22)), f.mult(a33, h.a32)), f.mult(a34, h.a42)),
                    f.add(f.add(f.add(f.mult(a31, h.a13), f.mult(a32, h.a23)), f.mult(a33, h.a33)), f.mult(a34, h.a43)),
                    f.add(f.add(f.add(f.mult(a31, h.a14), f.mult(a32, h.a24)), f.mult(a33, h.a34)), f.mult(a34, h.a44)),
                    f.add(f.add(f.add(f.mult(a41, h.a11), f.mult(a42, h.a21)), f.mult(a43, h.a31)), f.mult(a44, h.a41)),
                    f.add(f.add(f.add(f.mult(a41, h.a12), f.mult(a42, h.a22)), f.mult(a43, h.a32)), f.mult(a44, h.a42)),
                    f.add(f.add(f.add(f.mult(a41, h.a13), f.mult(a42, h.a23)), f.mult(a43, h.a33)), f.mult(a44, h.a43)),
                    f.add(f.add(f.add(f.mult(a41, h.a14), f.mult(a42, h.a24)), f.mult(a43, h.a34)), f.mult(a44, h.a44)),
                    f);
        }
        return null;
    }

    @Override
    public GL4ByteField getInverse() {
        ByteField.Element[][] g = GLnByteField.getInverse(new ByteField.Element[][]{{f.getElement(a11), f.getElement(a12), f.getElement(a13), f.getElement(a14)}, 
            {f.getElement(a21), f.getElement(a22), f.getElement(a23), f.getElement(a24)},
            {f.getElement(a31), f.getElement(a32), f.getElement(a33), f.getElement(a34)},
            {f.getElement(a41), f.getElement(a42), f.getElement(a43), f.getElement(a44)}}, f);
        return new GL4ByteField(g[0][0].getIndex(), g[0][1].getIndex(), g[0][2].getIndex(), g[0][3].getIndex(),
        g[1][0].getIndex(), g[1][1].getIndex(), g[1][2].getIndex(), g[1][3].getIndex(),
        g[2][0].getIndex(), g[2][1].getIndex(), g[2][2].getIndex(), g[2][3].getIndex(), 
                g[3][0].getIndex(), g[3][1].getIndex(), g[3][2].getIndex(), g[3][3].getIndex(), 
        f);
    }

    @Override
    public GL4ByteField getIdentity() {
        return new GL4ByteField(f);
    }

    /**
     * Returns true if the base field of h is the same as that of the element 
     * making the call.
     * 
     * @param h Any element of GL4ByteField.
     * @return true if the base field of h is the same as that of the element 
     * making the call.
     */
    @Override
    public boolean isOperationalWith(GL4ByteField h) {
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
        return "[" + f.getElement(a11).toString() + ", " + f.getElement(a12).toString() + ", " + f.getElement(a13).toString() + ", " + f.getElement(a14).toString() + "; " 
                 + f.getElement(a21).toString() + ", " + f.getElement(a22).toString() + ", " + f.getElement(a23).toString() + ", " + f.getElement(a24).toString() + "; " 
                 + f.getElement(a31).toString() + ", " + f.getElement(a32).toString() + ", " + f.getElement(a33).toString() + ", " + f.getElement(a34).toString() + "; " 
                + f.getElement(a41).toString() + ", " + f.getElement(a42).toString() + ", " + f.getElement(a43).toString() + ", "  + f.getElement(a44).toString() + "]";
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
        return a11+" "+a12+" "+a13+" "+a14+" "+a21+" "+a22+" "+a23+" "+a24+" "+a31+" "+a32+" "+a33+" "+a34+" "+a41+" "+a42+" "+a43+" "+a44+" "+q;
    }

}
