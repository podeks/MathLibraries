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
    
    public PGLnByteField(ByteField f, int n) {
        g=new GLnByteField(f, n); //g.project();
    }
    
    public PGLnByteField(GLnByteField g) {
        this.g=g;
        g.project();
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
    
    public ByteField getField() {
        return g.getField();
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
