package groups;

import java.util.Arrays;
import basic_operations.Arithmetic;
import basic_operations.ReducedMatrixOperations;
import api.Group;

/**
 * A class that models the group of non-singular 
 * projective square matrices over a <code>short</code> integer prime field, implemented in 
 * the framework of the Group interface.
 * 
 * <p>
 * Elements of this group are represented
 * internally by the <em>uniquely</em> defined matrix representative, modulo nonzero scalar 
 * multiplication, which has an entry of 1 as the first nonzero entry of the
 * first column, and non-negative integer entries less than q throughout.  The reduced matrix representative is computed at the time of 
 * construction.  This has the subsequent benefit of rapid equality testing
 * between two PGLn_PrimeField elements.  In particular, the <code>equals</code> method is
 * overridden in this class to test for equivalence of group elements by checking for equality between their
 * reduced matrix representatives.
 * </p>
 * 
 * <p>
 * Remark: The matrix computations mod q in this class are delegated to the 
 * class basic_operations.ReducedMatrixOperations of the Arithmetic module..
 * </p>
 * 
 * @author pdokos
 */
public class PGLn_PrimeField implements Group<PGLn_PrimeField>{

    private short q; //The order of the field
    private short[][] rep;

    /**
     * Constructor for a PGLn_PrimeField object, invoked by specifying a square two-dimensional array for 
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
    public PGLn_PrimeField(int[][] sqMtx, short q) {
        this.q = q;
        rep = ReducedMatrixOperations.reduce(sqMtx, q);
        reduce();
    }
    
    /**
     * Constructor for a PGLn_PrimeField object, invoked by specifying a square two-dimensional array for 
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
    public PGLn_PrimeField(short[][] sqMtx, short q) {
        this.q = q;
        rep = ReducedMatrixOperations.reduce(sqMtx, q);
        reduce();
    }
    
    /**
     * Constructor for the identity element of PGLn_PrimeField for a space of dimension n over the field of 
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
    public PGLn_PrimeField(int n, short q) {
        this.q = q;
        rep = ReducedMatrixOperations.constructIdentityMtx(n);
    }
    
    /**
     * Constructor for an element of PGLn_PrimeField, defined by a specified element of
     * GLn_PrimeField.
     * 
     * @param g An element of GLn_PrimeField.
     */
    public PGLn_PrimeField(GLn_PrimeField g) {
        int dimension= g.getDimension();
        q= g.getFieldOrder();
        rep = new short[dimension][dimension];
        for (int i=0; i<dimension; i++) {
            for (int j=0; j<dimension; j++) {
                rep[i][j] = g.getEntry(i, j); //Arithmetic.reduce(g.getEntry(i, j), q);
            }
        }
        reduce();
    }
    
    /**
     * A static helper for producing the identity element of PGLn_PrimeField 
     * for a space of dimension n
     * over the field of q elements, where q is prime.
     * 
     * @param n a positive <code>int</code> value.
     * @param q a <code>short</code> integer prime.
     * 
     * @return The identity element of GLn_PrimeField.
     */
    public static PGLn_PrimeField constructIdentity(int n, short q) {
        return new PGLn_PrimeField(n, q);
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
    public static PGLn_PrimeField constructElementaryMtx(int n, short q, int h, int k) {
        return new PGLn_PrimeField(ReducedMatrixOperations.constructElementaryMtx(n, h, k), q);
    }
    
    /**
     * A static helper for producing a random element of PGLn_PrimeField 
     * over the field of q elements, where q is prime.
     * 
     * @param n Any positive <code>int</code> value.
     * @param q A <code>short</code> integer prime.
     * @return A random element of PGLn_PrimeField.
     */
    public static PGLn_PrimeField constructRandomElement(int n, short q) {
        return new PGLn_PrimeField(ReducedMatrixOperations.constructRandomInvertibleElement(n, q), q);
    }
    
    private void reduce() {
        short firstNonzeroIndex = -1;
        short firstNonzeroEntry=0;
        int dimension = getDimension();
        
        while (firstNonzeroEntry==0 && firstNonzeroIndex< dimension-1) {
            firstNonzeroIndex++;
            firstNonzeroEntry=rep[firstNonzeroIndex][0];
        }
        
        if (firstNonzeroEntry != 0) {
            short factor = Arithmetic.findInverse(firstNonzeroEntry, q);
            rep = ReducedMatrixOperations.scalarMultiply(factor, rep, q);
            /*
            for (int i=0; i< dimension; i++) {
                for (int j=0; j< dimension; j++) {
                    rep[i][j] = Arithmetic.reducedProduct(factor, rep[i][j], q);
                }
            }
            */
        } 
    }
    

    /**
     * An override of the <code>equals</code> method which returns
     * true if both: (i) h is an instance of PGLn_PrimeField over the same prime
     * and of the same dimension as the element making the call, and (ii) the 
     * corresponding entries of the reduced matrix representatives are equal.
     * 
     * @param h Any object.
     * 
     * @return <code>true</code> if both: (i) h is an instance of PGLn_PrimeField over the same prime field
     * and of the same dimension as the element making the call, and (ii) the 
     * corresponding entries of the reduced matrix representatives are equal.
     */
    @Override
    public boolean equals(Object h) {

        if (h instanceof PGLn_PrimeField) {
            if (((PGLn_PrimeField) h).getDimension() == getDimension() && ((PGLn_PrimeField) h).getFieldOrder() == q) {
                return Arrays.deepEquals(rep, ((PGLn_PrimeField) h).getRep());
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
        int hash = 1;
        hash = 2 * hash + this.getDimension();
        hash = 3 * hash + this.q;
        hash = 7 * hash + Arrays.deepHashCode(this.rep);
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
     * @param i A positive <code>int</code> value, less than or equal to n.
     * @param j A positive <code>int</code> value, less than or equal to n.
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
     * Computes the determinant of this element (which is only defined up to 
     * multiplication by non-zero <em>n-th powers</em> in the field Z/qZ). 
     * 
     * @return The integer representative r, with 0&lt=r&ltq, for
     * the determinant of the reduced matrix representative mod q.
     */
    public short determinant() {
        return ReducedMatrixOperations.determinantModQ(rep, q);
    }
    
    /**
     * Returns the transposed element of PGLn_PrimeField.
     * 
     * @return The transposed element of PGLn_PrimeField.
     */
    public PGLn_PrimeField transpose() {
        return new PGLn_PrimeField(ReducedMatrixOperations.transpose(rep), q);
    }

    /**
     * @param h Any element of PGLn_PrimeField that is operational with the element making the call. 
     */
    @Override
    public PGLn_PrimeField leftProductBy(PGLn_PrimeField h) {
        if (h.getDimension() == getDimension()) {
            return new PGLn_PrimeField(ReducedMatrixOperations.reducedProduct(h.getRep(), rep, q), q);
        }
        return null;
    }

    /**
     * @param h Any element of PGLn_PrimeField that is operational with the element making the call. 
     */
    @Override
    public PGLn_PrimeField rightProductBy(PGLn_PrimeField h) {
        if (h.getDimension() == getDimension()) {
            return new PGLn_PrimeField(ReducedMatrixOperations.reducedProduct(rep, h.getRep(), q), q);        
        }
        return null;
    }

    @Override
    public PGLn_PrimeField getInverse() {
        return new PGLn_PrimeField(ReducedMatrixOperations.inverseModQ(rep, q), q);
    }

    @Override
    public PGLn_PrimeField getIdentity() {
        return new PGLn_PrimeField(getDimension(), q);
    }

    /**
     * Returns true if the vector space associated with h is over the same field
     * and of the same dimension as that of the element making the call.
     * 
     * @param h Any element of PGLn_PrimeField.
     * @return true if the vector space associated with h is over the same field
     * and of the same dimension as that of the element making the call.
     */
    @Override
    public boolean isOperationalWith(PGLn_PrimeField h) {
        return q==h.getFieldOrder() && getDimension()==h.getDimension();
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
                if (j != dimension-1)
                    sb.append(", ");
            }
            if (i != dimension-1)
                sb.append("; ");
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
                sb.append(rep[i][j]).append(" ");
                //if (!((j == dimension-1) && (i == dimension-1))) sb.append(" ")
            }
        }
        sb.append(q);
        return sb.toString();
    }
    
}

