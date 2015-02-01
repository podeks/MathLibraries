package basic_operations;

import java.util.Random;

/**
 * A collection of static methods for performing elementary square matrix operations
 * modulo a short prime.
 * 
 * @author pdokos
 */
public class ReducedMatrixOperations {
    
    private static Random rand = new Random();

    private ReducedMatrixOperations() {
    }
    
    /**
     * Computes the reduction of an integer matrix modulo a short integer n.
     * 
     * @param entries an integer matrix
     * @param n any nonzero short integer
     * @return the reduction of entries modulo n.
     */
    public static short[][] reduce(int[][] entries, short n) {
        if (entries.length == 0)
            return null;
        if (entries[0].length == 0)
            return null;

        short[][] reduction = new short[entries.length][entries[0].length];

        for (int i = 0; i < entries.length; i++) {
            for (int j = 0; j < entries[0].length; j++) {
                reduction[i][j] = Arithmetic.reduce(entries[i][j], n);
            }
        }
        return reduction;
    }
    
    /**
     * Computes the reduction of a short integer matrix modulo a short integer n.
     * 
     * @param entries a short integer matrix
     * @param n any nonzero short integer
     * @return the reduction of entries modulo n.
     */
    public static short[][] reduce(short[][] entries, short n) {
        if (entries.length == 0)
            return null;
        if (entries[0].length == 0)
            return null;

        short[][] reduction = new short[entries.length][entries[0].length];

        for (int i = 0; i < entries.length; i++) {
            for (int j = 0; j < entries[0].length; j++) {
                reduction[i][j] = Arithmetic.reduce(entries[i][j], n);
            }
        }
        return reduction;
    }

    /**
     * Computes the determinant of a square matrix modulo a prime q via Gaussian elimination.  
     * The return value is 0 if the entries is not a square matrix, or if q is not prime. 
     * 
     * @param entries a square matrix of shorts
     * @param q any short prime number
     * @return the inverse of the matrix mod q.  Returns null if the inverse does not exist, or q is not prime.
     */
    public static short determinantModQ(short[][] entries, short q) {
        if (Arithmetic.isPrime(q)) {
            int dimension = entries.length;

            if (dimension == 0) {
                return 0;
            }
            if (entries[0].length != dimension) {
                return 0;
            }

            short det = 1;
            short[][] entriesCopy = new short[dimension][dimension];
            for (int i = 0; i < dimension; i++) {
                System.arraycopy(entries[i], 0, entriesCopy[i], 0, dimension);
            }

            for (int k = 0; k < dimension; k++) {

                if (entriesCopy[k][k] == 0) { //switch rows; return 0 if not possible
                    boolean notFound = true;
                    for (int j = k + 1; j < dimension && notFound; j++) {
                        if (entriesCopy[j][k] != 0) {
                            notFound = false;
                            det = Arithmetic.reduce(-det, q);
                            short[] temp = new short[dimension];
                            System.arraycopy(entriesCopy[j], 0, temp, 0, dimension);
                            System.arraycopy(entriesCopy[k], 0, entriesCopy[j], 0, dimension);
                            System.arraycopy(temp, 0, entriesCopy[k], 0, dimension);
                        }
                    }
                    if (notFound) {
                        return 0;
                    }
                }

                short dInv = Arithmetic.findInverse(entriesCopy[k][k], q);
                for (int j = k + 1; j < dimension; j++) { //add - a[j][k] * a[k][k]^(-1) times kth row to the jth row
                    short factor = Arithmetic.reduce(-Arithmetic.reducedProduct(entriesCopy[j][k], dInv, q), q);
                    for (int l = k; l < dimension; l++) {
                        entriesCopy[j][l] = Arithmetic.reducedSum(entriesCopy[j][l], Arithmetic.reducedProduct(factor, entriesCopy[k][l], q), q);
                    }
                }
                det = Arithmetic.reducedProduct(det, entriesCopy[k][k], q);
            }
            return det;
        }
        return 0;

    }

    /**
     * Computes the determinant of a square matrix modulo a prime q via Gaussian elimination.  
     * The return value is 0 if the entries is not a square matrix, or if q is not prime. 
     * 
     * @param entries a square matrix
     * @param q any prime number
     * @return the inverse of the matrix mod q.  Returns null if the inverse does not exist, or q is not prime.
     */
    public static int determinantModQ(int[][] entries, int q) {

        if (Arithmetic.isPrime(q)) {
            int dimension = entries.length;

            if (dimension == 0) {
                return 0;
            }
            if (entries[0].length != dimension) {
                return 0;
            }

            int det = 1;
            int[][] entriesCopy = new int[dimension][dimension];
            for (int i = 0; i < dimension; i++) {
                System.arraycopy(entries[i], 0, entriesCopy[i], 0, dimension);
            }

            for (int k = 0; k < dimension; k++) {

                if (entriesCopy[k][k] == 0) { //switch rows; return 0 if not possible
                    boolean notFound = true;
                    for (int j = k + 1; j < dimension && notFound; j++) {
                        if (entriesCopy[j][k] != 0) {
                            notFound = false;
                            det = Arithmetic.reduce(-det, q);
                            int[] temp = new int[dimension];
                            System.arraycopy(entriesCopy[j], 0, temp, 0, dimension);
                            System.arraycopy(entriesCopy[k], 0, entriesCopy[j], 0, dimension);
                            System.arraycopy(temp, 0, entriesCopy[k], 0, dimension);
                        }
                    }
                    if (notFound) {
                        return 0;
                    }
                }

                int dInv = Arithmetic.findInverse(entriesCopy[k][k], q);
                for (int j = k + 1; j < dimension; j++) { //add - a[j][k] * a[k][k]^(-1) times kth row to the jth row
                    int factor = Arithmetic.reducedProduct(-entriesCopy[j][k], dInv, q);
                    for (int l = k; l < dimension; l++) {
                        entriesCopy[j][l] = Arithmetic.reducedSum(entriesCopy[j][l], Arithmetic.reducedProduct(factor, entriesCopy[k][l], q), q);
                    }
                }
                det = Arithmetic.reducedProduct(det, entriesCopy[k][k], q);
            }
            return det;
        }
        return 0;
    }

    /**
     * Computes the inverse of a square matrix modulo a prime q via Gaussian elimination.  
     * The return value is null if the inverse does not exist, or if q is not prime. 
     * 
     * @param entries a square matrix of shorts
     * @param q any short prime number
     * @return the inverse of the matrix mod q.  Returns null if the inverse does not exist, or q is not prime.
     */
    public static short[][] inverseModQ(short[][] entries, short q) {

        if (Arithmetic.isPrime(q)) {
            int dimension = entries.length;

            /*
            if (dimension == 0) {
                return null;
            }
            if (entries[0].length != dimension) {
                return null;
            }
*/
            short[][] entriesCopy = new short[dimension][dimension];
            for (int i = 0; i < dimension; i++) {
                System.arraycopy(entries[i], 0, entriesCopy[i], 0, dimension);
            }

            short[][] inv = new short[dimension][dimension];
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (i == j) {
                        inv[i][j] = 1;
                    } else {
                        inv[i][j] = 0;
                    }
                }
            }


            for (int k = 0; k < dimension; k++) {

                if (entriesCopy[k][k] == 0) { //switch rows; return 0 if not possible
                    boolean notFound = true;
                    for (int j = k + 1; j < dimension && notFound; j++) {
                        if (entriesCopy[j][k] != 0) {
                            notFound = false;
                            short[] temp = new short[dimension];
                            System.arraycopy(entriesCopy[j], 0, temp, 0, dimension);
                            System.arraycopy(entriesCopy[k], 0, entriesCopy[j], 0, dimension);
                            System.arraycopy(temp, 0, entriesCopy[k], 0, dimension);
                            temp = new short[dimension];
                            System.arraycopy(inv[j], 0, temp, 0, dimension);
                            System.arraycopy(inv[k], 0, inv[j], 0, dimension);
                            System.arraycopy(temp, 0, inv[k], 0, dimension);
                        }
                    }
                    if (notFound) {
                        return null;
                    }
                }

                short dInv = Arithmetic.findInverse(entriesCopy[k][k], q);
                for (int l = 0; l < dimension; l++) {
                    entriesCopy[k][l] = Arithmetic.reducedProduct(dInv, entriesCopy[k][l], q);
                    inv[k][l] = Arithmetic.reducedProduct(dInv, inv[k][l], q);
                }

                for (int j = 0; j < dimension; j++) { //add - a[j][k] * a[k][k]^(-1) times kth row to the jth row
                    if (j != k) {
                        short factor = Arithmetic.reduce(-entriesCopy[j][k], q);// * dInv;
                        for (int l = 0; l < dimension; l++) {
                            entriesCopy[j][l] = Arithmetic.reducedSum(entriesCopy[j][l], Arithmetic.reducedProduct(factor, entriesCopy[k][l], q), q);
                            inv[j][l] = Arithmetic.reducedSum(inv[j][l], Arithmetic.reducedProduct(factor, inv[k][l], q), q);
                            //System.out.print("inv[" + j + "][" +l + "] = " + inv[j][l] + "  ");
                        }
                    }
                }
            }
            return inv;
        }
        return null;
    }

    /**
     * Computes the reduced negative of a short integer matrix modulo n.
     * 
     * @param a any short integer matrix
     * @param n any non-zero short integer
     * @return The reduced negative of the matrix a modulo n.  Returns null if either the row or column dimension is 0.
     */
    public static short[][] negative(short[][] a, short n) {
        
        int rowDimension = a.length;
        if (rowDimension == 0)
            return null;
        int columnDimension = a[0].length;
        if (columnDimension == 0)
            return null;

        short[][] negative = new short[rowDimension][columnDimension];

        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < columnDimension; j++) {
                negative[i][j] = Arithmetic.reduce(-a[i][j], n);
            }
        }
        return negative;
    }

    /**
     * Computes the reduced sum of two short integer matrices a and b modulo n.
     * 
     * @param a any short integer matrix
     * @param b any short integer matrix
     * @param n any non-zero short integer
     * @return the reduced sum of a and b mod n.  Returns null if there is a dimension mismatch, 
     * or if any dimensions are 0
     */
    public static short[][] reducedSum(short[][] a, short[][] b, short n) {
        
        int rowDimension = a.length;
        if (rowDimension != b.length || rowDimension == 0)
            return null;
        int columnDimension = a[0].length;
        if (columnDimension != b[0].length || columnDimension==0) 
            return null;

        short[][] sum = new short[rowDimension][columnDimension];

        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < columnDimension; j++) {
                sum[i][j] = Arithmetic.reducedSum(a[i][j], b[i][j], n);
            }
        }
        return sum;
    }

    /**
     * Computes the reduced product of two short integer matrices a and b modulo n.
     * 
     * @param a any short integer matrix
     * @param b any short integer matrix
     * @param n any non-zero short integer
     * @return the reduced product of a and b mod n.  Returns null if there is a dimension mismatch, 
     * or if any dimensions are 0
     */
    public static short[][] reducedProduct(short[][] a, short[][] b, short n) {

        int rowDimension = a.length;
        if (rowDimension == 0)
            return null;
        
        int middleDim = b.length;
        if (middleDim == 0)
            return null;
        
        if (a[0].length !=  middleDim) 
            return null;
        
        int columnDimension = b[0].length;

        short[][] product = new short[rowDimension][columnDimension];
        short entry;

        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < columnDimension; j++) {
                entry = 0;
                for (int k = 0; k < middleDim; k++) {
                    entry = Arithmetic.reducedSum(entry, Arithmetic.reducedProduct(a[i][k], b[k][j], n), n);
                }
                product[i][j] = entry;
            }
        }
        return product;
    }
    
    
    /**
     * Creates the transpose matrix.
     * @param a any short integer matrix
     * @return the transpose of a
     */
    public static short[][] transpose(short[][] a) {
        if (a.length==0)
            return null;
        if (a[0].length==0)
            return null;
        short[][] transpose = new short[a[0].length][a.length];
        for (int i = 0; i < a[0].length; i++) {
            for (int j = 0; j < a.length; j++) {
                transpose[i][j] = a[j][i];
            }
        }
        return transpose;
    }
    
    /**
     * Computes the product of the matrix b by the scalar a modulo n.
     * 
     * @param a any short integer
     * @param b any short integer matrix
     * @param n any non-zero short integer
     * 
     * @return the product of the matrix b by the scalar a modulo n.
     */
    public static short[][] scalarMultiply(short a, short[][] b, short n) {
        short[][] product = new short[b.length][b[0].length];
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                product[i][j] = Arithmetic.reducedProduct(a, b[i][j], n);
            }
        } 
        return product;
    }
    
    /**
     * Constructs the identity matrix of the given dimension.
     * 
     * @param dimension the dimension of the desired identity matrix
     * @return the identity matrix of dimension n.
     */
    public static short[][] constructIdentityMtx(int dimension) {
        
        short[][] rep = new short[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (i == j) {
                    rep[i][j] = 1;
                } else {
                    rep[i][j] = 0;
                }
            }
        }
        return rep;
    }
    
    /**
     * Constructs the matrix which has ones along the diagonal and at the (h, k) entry, with zeros everywhere else.
     * 
     * @param dimension any positive integer
     * @param h the row index of the non-zero element, in the range 1,...,dimension
     * @param k the column index of the non-zero element, in the range 1,...,dimension
     * @return the matrix which has ones along the diagonal and at the (h, k) entry, with zeros everywhere else.
     */
    public static short[][] constructElementaryMtx(int dimension, int h, int k) {
        
        short[][] entries = constructIdentityMtx(dimension);
        entries[h - 1][k - 1] = 1;
        return entries;
    }

    /**
     * Constructs a matrix with random integer entries mod n.
     * 
     * @param rowLength any positive integer
     * @param columnLength any positive integer
     * @param n any non-zero short integer 
     * 
     * @return a matrix with random integer entries in the range 0, 1, ... |n|-1
     */
    public static short[][] constructRandomElement(int rowLength, int columnLength, short n) {
        short[][] entries = new short[rowLength][columnLength];
        //Random rand = new Random();
        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < columnLength; j++) {
                entries[i][j] = (short) rand.nextInt(n);
            }
        }
        return entries;
    }
    
    /**
     * Constructs a square matrix with random entries modulo a prime q, which is also non-singular mod q.
     * 
     * @param dimension any positive integer
     * @param q any short integer prime
     * @return a square matrix with random entries in the range 0, 1, ... |q|-1, which is invertible mod q
     */
    public static short[][] constructRandomInvertibleElement(int dimension, short q) {
        short[][] entries = new short[dimension][dimension];
        //Random rand = new Random();
        short det = 0;
        while (det == 0) {
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    entries[i][j] = (short) rand.nextInt(q);
                }
            }
            det = determinantModQ(entries, q);
        }
        return entries;
    }
}
