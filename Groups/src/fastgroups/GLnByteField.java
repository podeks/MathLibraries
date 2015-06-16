package fastgroups;

import api.Group;
import finitefields.ByteField;
import groups.GLn_PrimeField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author pdokos
 */
public class GLnByteField implements Group<GLnByteField>{
    
    ByteField f;
    byte dim;
    byte[] entries;
    
    public GLnByteField(ByteField field, ByteField.Element[][] ents) {
        f=field;
        dim = (byte) ents.length;
        entries = new byte[ents.length*ents.length];
        int ind=0;
        for (int i=0; i<ents.length; i++) {
            ByteField.Element[] entsI = ents[i];
            for (int j=0; j<ents.length; j++) {
                entries[ind]=entsI[j].getIndex();
                ind++;
            }
        }
    }
    
    public GLnByteField(ByteField field, byte[][] ents) {
        f=field;
        dim = (byte) ents.length;
        entries = new byte[ents.length*ents.length];
        int ind=0;
        for (int i=0; i<ents.length; i++) {
            byte[] entsI = ents[i];
            for (int j=0; j<ents.length; j++) {
                entries[ind]=entsI[j];
                ind++;
            }
        }
    }
    
    public GLnByteField(ByteField field, byte dimension, byte[] ents) {
        f=field;
        dim=dimension;
        entries = ents;
    }
    
    public GLnByteField(ByteField field, int n) {
        f=field;
        dim = (byte) n;
        entries = new byte[n*n];
        int ind=0;
        byte one = f.one().getIndex();
        byte zero = f.zero().getIndex();
        for (int i=0; i<n; i++) {
            for (int j=0; j<n; j++) {
                entries[ind] = (i==j) ? one : zero;
                ind++;
            }
        }
    }
    
//    public static Set<InvertibleByteFieldMatrix> convert(Set<InvertibleFiniteFieldMatrix> set) {
//        Set<InvertibleByteFieldMatrix> convSet = new HashSet<InvertibleByteFieldMatrix>();
//        for (InvertibleFiniteFieldMatrix e : set) {
//            convSet.add(new InvertibleByteFieldMatrix());
//        }
//        return null;
//    }
    
    /**
     * A static helper for producing the identity element of GLnByteField 
     * over the field f.
     * 
     * @param n the dimension.
     * @param f a <code>ByteField</code>.
     * @return The identity element of GLnByteField.
     */
    public static GLnByteField constructIdentity(int n, ByteField f) {
        return new GLnByteField(f, n);
    }
    
    private void scale(byte lambda) {
        for (int i=0; i<entries.length; i++) {
            entries[i]=f.mult(lambda, entries[i]);
        }
    }
    
    public void project() {
        byte firstNonzeroIndex = 0;
        byte zero = f.zero().getIndex();
        byte firstNonzeroEntry=zero;
        
        while (firstNonzeroEntry==zero && firstNonzeroIndex< entries.length) {
            firstNonzeroEntry=entries[firstNonzeroIndex];
            firstNonzeroIndex += dim;
        }
        
        if (firstNonzeroEntry != zero) {
            scale(f.inverse(firstNonzeroEntry));
        } 
    }
    
    public int getFieldOrder(){
        return f.getOrder();
    }
    
    public ByteField getField() {
        return f;
    }
    
    public int getDimension() {
        return dim;
    }
    
    public ByteField.Element getEntry(int i, int j) {
        return f.getElement(entries[dim*i + j]);
    }
    
    public ByteField.Element getEntry(int ind) {
        return f.getElement(entries[ind]);
    }
    
    public static GLnByteField convert(ByteField f, GLn_PrimeField g) {
        int dim = g.getDimension();
        ByteField.Element[][] entries = new ByteField.Element[dim][dim]; 
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                List<Short> entry = new ArrayList<Short>(1); 
                entry.add(g.getEntry(i, j));
                entries[i][j] = f.lookup(entry);
            }
        }
        return new GLnByteField(f, entries);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof GLnByteField) {
            for (int i = 0; i < entries.length; i++) {
                if (entries[i] != ((GLnByteField) o).entries[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Arrays.hashCode(entries);
        return hash;
    }
    
    
    @Override
    public GLnByteField leftProductBy(GLnByteField h) {
        return h.rightProductBy(this);
    }

    @Override
    public GLnByteField rightProductBy(GLnByteField h) {
        byte[] prod = new byte[entries.length];
        byte zero =f.zero().getIndex();
        int ind=0;
        int rowStartInd=0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                int rowInd=rowStartInd;
                int colInd=j;
                byte entry = zero;
                for (int k = 0; k < dim; k++) {
                    entry = f.add(entry, f.mult(entries[rowInd], h.entries[colInd]));
                    rowInd++;
                    colInd += dim;
                }
                prod[ind] = entry;
                ind++;
            }
            rowStartInd += dim;
        }
        return new GLnByteField(f, dim, prod);
    }

    @Override
    public GLnByteField getInverse() {

        ByteField.Element[][] inv = new ByteField.Element[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                inv[i][j] = (i==j) ? f.one() : f.zero();
            }
        }



        ByteField.Element[][] entriesCopy = new ByteField.Element[dim][dim];
        int ind=0;
        
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                entriesCopy[i][j] = f.getElement(entries[ind]);
                ind++;
            }
        }

        short q=f.getCharacteristic();
        
        for (int k = 0; k < dim; k++) {

                if (entriesCopy[k][k].equals(f.zero())) { //switch rows; return 0 if not possible
                    boolean notFound = true;
                    for (int j = k + 1; j < dim && notFound; j++) {
                        if (!entriesCopy[j][k].equals(f.zero())) {
                            notFound = false;
                            ByteField.Element[] temp = new ByteField.Element[dim];
                            System.arraycopy(entriesCopy[j], 0, temp, 0, dim);
                            System.arraycopy(entriesCopy[k], 0, entriesCopy[j], 0, dim);
                            System.arraycopy(temp, 0, entriesCopy[k], 0, dim);
                            temp = new ByteField.Element[dim];
                            System.arraycopy(inv[j], 0, temp, 0, dim);
                            System.arraycopy(inv[k], 0, inv[j], 0, dim);
                            System.arraycopy(temp, 0, inv[k], 0, dim);
                        }
                    }
                    if (notFound) {
                        return null;
                    }
                }

                ByteField.Element dInv = entriesCopy[k][k].inverse();
                for (int l = 0; l < dim; l++) {
                    entriesCopy[k][l] = dInv.times(entriesCopy[k][l]);
                    inv[k][l] = dInv.times(inv[k][l]);
                }

                for (int j = 0; j < dim; j++) { //add - a[j][k] * a[k][k]^(-1) times kth row to the jth row
                    if (j != k) {
                        ByteField.Element factor = entriesCopy[j][k].negative();// * dInv;
                        for (int l = 0; l < dim; l++) {
                            entriesCopy[j][l] = entriesCopy[j][l].plus(factor.times(entriesCopy[k][l]));
                            inv[j][l] = inv[j][l].plus(factor.times(inv[k][l]));
                            //System.out.print("inv[" + j + "][" +l + "] = " + inv[j][l] + "  ");
                        }
                    }
                }
            }


        return new GLnByteField(f, inv);
    }

    @Override
    public GLnByteField getIdentity() {
        return new GLnByteField(f, dim);
    }

    @Override
    public boolean isOperationalWith(GLnByteField h) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
        @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int dimension = dim;
        sb.append("[");
        int ind=0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                sb.append(f.getElement(entries[ind]).toString());
                if (j != dimension - 1) {
                    sb.append(", ");
                }
                ind++;
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
        int ind=0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                sb.append(entries[ind]).append(' ');
                ind++;
                //if (!((j == dimension-1) && (i == dimension-1))) sb.append(" ")
            }
        }
        sb.append(f.getOrder());
        return sb.toString();
    }  
        
        
        
        
        public static ByteField.Element[][] getInverse(ByteField.Element[][] g, ByteField f) {

        int dim = g.length;

        ByteField.Element[][] inv = new ByteField.Element[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (i == j) {
                    inv[i][j] = f.one();
                } else {
                    inv[i][j] = f.zero();
                }
            }
        }



        ByteField.Element[][] entriesCopy = new ByteField.Element[dim][dim];
        for (int i = 0; i < dim; i++) {
            System.arraycopy(g[i], 0, entriesCopy[i], 0, dim);
        }

        //short q = f.getCharacteristic();

        for (int k = 0; k < dim; k++) {

            if (entriesCopy[k][k].equals(f.zero())) { //switch rows; return 0 if not possible
                boolean notFound = true;
                for (int j = k + 1; j < dim && notFound; j++) {
                    if (!entriesCopy[j][k].equals(f.zero())) {
                        notFound = false;
                        ByteField.Element[] temp = new ByteField.Element[dim];
                        System.arraycopy(entriesCopy[j], 0, temp, 0, dim);
                        System.arraycopy(entriesCopy[k], 0, entriesCopy[j], 0, dim);
                        System.arraycopy(temp, 0, entriesCopy[k], 0, dim);
                        temp = new ByteField.Element[dim];
                        System.arraycopy(inv[j], 0, temp, 0, dim);
                        System.arraycopy(inv[k], 0, inv[j], 0, dim);
                        System.arraycopy(temp, 0, inv[k], 0, dim);
                    }
                }
                if (notFound) {
                    return null;
                }
            }

            ByteField.Element dInv = entriesCopy[k][k].inverse();
            for (int l = 0; l < dim; l++) {
                entriesCopy[k][l] = dInv.times(entriesCopy[k][l]);
                inv[k][l] = dInv.times(inv[k][l]);
            }

            for (int j = 0; j < dim; j++) { //add - a[j][k] * a[k][k]^(-1) times kth row to the jth row
                if (j != k) {
                    ByteField.Element factor = entriesCopy[j][k].negative();// * dInv;
                    for (int l = 0; l < dim; l++) {
                        entriesCopy[j][l] = entriesCopy[j][l].plus(factor.times(entriesCopy[k][l]));
                        inv[j][l] = inv[j][l].plus(factor.times(inv[k][l]));
                        //System.out.print("inv[" + j + "][" +l + "] = " + inv[j][l] + "  ");
                    }
                }
            }
        }

        return inv;
    }
    
}


