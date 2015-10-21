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

/**
 *
 * @author pdokos
 */
public class PGLnByteField implements Group<PGLnByteField>{
   
    private GLnByteField g;
    
    public PGLnByteField(ByteField field, ByteField.Element[][] entries) {
        g=new GLnByteField(field, entries);
        g.project();
    }
    
    public PGLnByteField(ByteField field, byte[][] ents) {
        g=new GLnByteField(field, ents);
        g.project();
    }
    
    public PGLnByteField(ByteField f, int n) {
        g=new GLnByteField(f, n); //g.project();
    }
    
    public PGLnByteField(GLnByteField g) {
        this.g=g;
        g.project();
    }
    
    /**
     * A static helper for producing the identity element of GLnByteField 
     * over the field f.
     * 
     * @param n the dimension.
     * @param f a <code>ByteField</code>.
     * @return The identity element of GLnByteField.
     */
    public static PGLnByteField constructIdentity(int n, ByteField f) {
        return new PGLnByteField(f, n);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof PGLnByteField) {
            return g.equals(((PGLnByteField) o).g);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return g.hashCode();
    }
    
    @Override
    public String toString() {
        return g.toString();
    }
    
    public String toUnpunctuatedString() {
        return g.toUnpunctuatedString();
    }
    
    public ByteField getField() {
        return g.getField();
    }
    
    public int getFieldOrder() {
        return g.getFieldOrder();
    }
    
    public int getDimension() {
        return g.getDimension();
    }
    
    public ByteField.Element getEntry(int i, int j) {
        return g.getEntry(i, j);
    }
    
    @Override
    public PGLnByteField leftProductBy(PGLnByteField h) {
        return new PGLnByteField(g.leftProductBy(h.g));
    }

    @Override
    public PGLnByteField rightProductBy(PGLnByteField h) {
        return new PGLnByteField(g.rightProductBy(h.g));
    }

    @Override
    public PGLnByteField getInverse() {
        return new PGLnByteField(g.getInverse());
    }

    @Override
    public PGLnByteField getIdentity() {
        return new PGLnByteField(g.getField(), g.getDimension());
    }

    @Override
    public boolean isOperationalWith(PGLnByteField h) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
//    public ByteField getField() {
//        return f;
//    }
//    

    
}
