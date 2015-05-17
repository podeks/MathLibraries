package fastgroups;

import api.Group;
import finitefields.ByteField;

/**
 *
 * @author pdokos
 */
public class PGL2ByteField implements Group<PGL2ByteField>{
    
    private GL2ByteField rep;
    
    public PGL2ByteField(byte a, byte b, byte c, byte d, ByteField f) {
        rep=new GL2ByteField(a, b, c, d, f);
        rep.project();
    }
    
    public PGL2ByteField(ByteField.Element a, ByteField.Element b, ByteField.Element c, ByteField.Element d, ByteField f) {
        rep=new GL2ByteField(a, b, c, d, f); 
        rep.project();
    }
    
    public PGL2ByteField(GL2ByteField g) {
        this.rep=g;
        rep.project();
    }
    
    public PGL2ByteField(ByteField f) {
        this.rep= new GL2ByteField(f);
        //rep.project();
    }
    
    public static PGL2ByteField constructIdentity(ByteField f) {
        return new PGL2ByteField(f);
    }
    
    public static PGL2ByteField convert(PGLnByteField g) {
        return new PGL2ByteField(g.getEntry(0, 0), g.getEntry(0, 1), g.getEntry(1, 0), g.getEntry(1, 1), g.getField() );
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof PGL2ByteField) {
            return rep.equals(((PGL2ByteField) o).rep);
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
    public PGL2ByteField leftProductBy(PGL2ByteField h) {
        return new PGL2ByteField(rep.leftProductBy(h.rep));
    }

    @Override
    public PGL2ByteField rightProductBy(PGL2ByteField h) {
        return new PGL2ByteField(rep.rightProductBy(h.rep));
    }

    @Override
    public PGL2ByteField getInverse() {
        return new PGL2ByteField(rep.getInverse());
    }

    @Override
    public PGL2ByteField getIdentity() {
        return new PGL2ByteField(rep.getField());
    }

    @Override
    public boolean isOperationalWith(PGL2ByteField h) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
