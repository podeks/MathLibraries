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
public class PGL4ByteField implements Group<PGL4ByteField>{
    
    private GL4ByteField rep;
    
    public PGL4ByteField(byte a11,byte a12,byte a13,byte a14,
            byte a21,byte a22,byte a23,byte a24,
            byte a31,byte a32,byte a33, byte a34,
            byte a41,byte a42,byte a43, byte a44, ByteField f) {
        rep=new GL4ByteField(a11, a12, a13, a14, a21, a22, a23, a24, a31, a32, a33, a34,
                a41, a42, a43, a44, f);
        rep.project();
    }
    
    public PGL4ByteField(ByteField.Element a11, ByteField.Element a12, ByteField.Element a13, ByteField.Element a14,
            ByteField.Element a21, ByteField.Element a22, ByteField.Element a23, ByteField.Element a24,
            ByteField.Element a31, ByteField.Element a32, ByteField.Element a33, ByteField.Element a34,
            ByteField.Element a41, ByteField.Element a42, ByteField.Element a43, ByteField.Element a44,
            ByteField f) {
        rep=new GL4ByteField(a11, a12, a13, a14, a21, a22, a23, a24, a31, a32, a33, a34,
                a41, a42, a43, a44, f); 
        rep.project();
    }
    
    public PGL4ByteField(GL4ByteField g) {
        this.rep=g;
        rep.project();
    }
    
    public PGL4ByteField(ByteField f) {
        this.rep=new GL4ByteField(f);
        rep.project();
    }
    
    public static PGL4ByteField constructIdentity(ByteField f) {
        return new PGL4ByteField(f);
    }
    
    public static PGL4ByteField convert(PGLnByteField g) {
        return new PGL4ByteField(g.getEntry(0, 0), g.getEntry(0, 1), g.getEntry(0, 2), g.getEntry(0, 3),
                g.getEntry(1, 0), g.getEntry(1, 1), g.getEntry(1, 2), g.getEntry(1, 3),
                g.getEntry(2, 0), g.getEntry(2, 1), g.getEntry(2, 2), g.getEntry(2, 3),
                g.getEntry(3, 0), g.getEntry(3, 1), g.getEntry(3, 2), g.getEntry(3, 3),
                g.getField() );
    }
    
    public static Set<PGL4ByteField> convert(Set<PGLnByteField> set) {
        Set<PGL4ByteField> newSet = new HashSet<PGL4ByteField>(set.size()+1, 1.0f);
        for (PGLnByteField g : set) {
            newSet.add(convert(g));
        }
        return newSet;
    }
    public ByteField getField() {
        return rep.getField();
    }
    
    public int getFieldOrder() {
        return rep.getFieldOrder();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof PGL4ByteField) {
            return rep.equals(((PGL4ByteField) o).rep);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return rep.hashCode();
    }
    
    @Override
    public String toString() {
        return rep.toString();
    }
    
    public String toUnpunctuatedString() {
        return rep.toUnpunctuatedString();
    }
    
    @Override
    public PGL4ByteField leftProductBy(PGL4ByteField h) {
        return new PGL4ByteField(rep.leftProductBy(h.rep));
    }

    @Override
    public PGL4ByteField rightProductBy(PGL4ByteField h) {
        return new PGL4ByteField(rep.rightProductBy(h.rep));
    }

    @Override
    public PGL4ByteField getInverse() {
        return new PGL4ByteField(rep.getInverse());
    }

    @Override
    public PGL4ByteField getIdentity() {
        return new PGL4ByteField(rep.getIdentity());
    }

    @Override
    public boolean isOperationalWith(PGL4ByteField h) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
