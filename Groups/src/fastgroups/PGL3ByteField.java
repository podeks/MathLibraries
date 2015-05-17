package fastgroups;

import api.Group;
import finitefields.ByteField;

/**
 *
 * @author pdokos
 */
public class PGL3ByteField implements Group<PGL3ByteField>{
    
    private GL3ByteField rep;
    
    public PGL3ByteField(byte a11,byte a12,byte a13,
            byte a21,byte a22,byte a23,
            byte a31,byte a32,byte a33, ByteField f) {
        rep=new GL3ByteField(a11, a12, a13, a21, a22, a23, a31, a32, a33, f);
        rep.project();
    }
    
    public PGL3ByteField(ByteField.Element a11, ByteField.Element a12, ByteField.Element a13,
            ByteField.Element a21, ByteField.Element a22, ByteField.Element a23,
            ByteField.Element a31, ByteField.Element a32, ByteField.Element a33, ByteField f) {
        rep=new GL3ByteField(a11, a12, a13, a21, a22, a23, a31, a32, a33, f); 
        rep.project();
    }
    
    public PGL3ByteField(GL3ByteField g) {
        this.rep=g;
        rep.project();
    }
    
    public PGL3ByteField(ByteField f) {
        this.rep=new GL3ByteField(f);
        rep.project();
    }
    
    public static PGL3ByteField constructIdentity(ByteField f) {
        return new PGL3ByteField(f);
    }
    
    public static PGL3ByteField convert(PGLnByteField g) {
        return new PGL3ByteField(g.getEntry(0, 0), g.getEntry(0, 1), g.getEntry(0, 2),
                g.getEntry(1, 0), g.getEntry(1, 1), g.getEntry(1, 2), 
                g.getEntry(2, 0), g.getEntry(2, 1), g.getEntry(2, 2), 
                g.getField() );
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof PGL3ByteField) {
            return rep.equals(((PGL3ByteField) o).rep);
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
    
    
    @Override
    public PGL3ByteField leftProductBy(PGL3ByteField h) {
        return new PGL3ByteField(rep.leftProductBy(h.rep));
    }

    @Override
    public PGL3ByteField rightProductBy(PGL3ByteField h) {
        return new PGL3ByteField(rep.rightProductBy(h.rep));
    }

    @Override
    public PGL3ByteField getInverse() {
        return new PGL3ByteField(rep.getInverse());
    }

    @Override
    public PGL3ByteField getIdentity() {
        return new PGL3ByteField(rep.getIdentity());
    }

    @Override
    public boolean isOperationalWith(PGL3ByteField h) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
