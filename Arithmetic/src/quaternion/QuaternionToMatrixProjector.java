package quaternion;

import basic_operations.Arithmetic;

/**
 * 
 * A class whose objects provide methods that map ReducedQuaternions onto 
 * reduced 2x2 integer matrices modulo q, where q is an odd prime specified in 
 * the constructor.  
 * The pullback to LipschitzQuaternions, and the reduction to ProjectiveReducedQuaternions
 * are also included.
 * This map is an important ingredient 
 * in the construction of LPS generating sets, bridging the quaternion unit group
 * with the group GL2.  The definition of this map is given in Chapter 2.5 of
 * [DSV].
 * 
 * <p>
 * References:
 * </p>
 * <p>
 * &#160 &#160 [DSV] G. Davidoff, P. Sarnak, A. Valette, "Elementary Number Theory, Group Theory, and Ramanujan Graphs," 
 * Cambridge, LMSST 55, 2003.
 * </p>
 * 
 * 
 * @author pdokos
 */
public class QuaternionToMatrixProjector {
    
    private int q;
    private int x;
    private int y;

    /**
     * Instantiates a projector onto reduced 2x2 integer matrices mod q, where q is an odd prime.
     * 
     * @param q any odd prime
     */
    public QuaternionToMatrixProjector(int q) {
        if (q>2 && Arithmetic.isPrime(q)) {
            this.q = q;
            int[] iotaVector = findIotaVector(q);
            x=iotaVector[0];
            y=iotaVector[1];
        }
    }
    
     /**
     * This method projects a Lipschitz quaternion a+bi+cj+dk 
     * onto a 2x2 <code>int</code> array with entries in 0, 1, ..., |q|-1.  The definition 
     * on which this implementation is based is given in [DSV], chapter 2.5.
     * 
     * @param a any integer
     * @param b any integer
     * @param c any integer
     * @param d any integer
     * 
     * @return An <code>int</code>[2][2] with entries in 0, 1, ... q-1,
     * representing the 2x2 reduced integer matrix mod q associated with x.
     */
    public int[][] pi(int a, int b, int c, int d) {
        int[][] arr = new int[2][2];
        arr[0][0] = Arithmetic.reducedSum(Arithmetic.reducedSum(a, Arithmetic.reducedProduct(x, b, q), q), Arithmetic.reducedProduct(y, d, q), q);
        arr[0][1] = Arithmetic.reducedSum(Arithmetic.reducedSum(c, Arithmetic.reducedProduct(x, d, q), q), Arithmetic.reducedProduct(-y, b, q), q);
        arr[1][0] = Arithmetic.reducedSum(Arithmetic.reducedSum(-c, Arithmetic.reducedProduct(x, d, q), q), Arithmetic.reducedProduct(-y, b, q), q);
        arr[1][1] = Arithmetic.reducedSum(Arithmetic.reducedSum(a, Arithmetic.reducedProduct(-x, b, q) , q), Arithmetic.reducedProduct(-y, d, q), q);
        return arr;
    }
    
    /**
     * Returns the 2x2 reduced integer matrix mod q associated with x.
     * @param x Any LipschitzQuaternion.
     * @return An <code>int</code>[2][2] with entries in 0, 1, ... q-1,
     * representing the 2x2 reduced integer matrix mod q associated with x.
     */
    public int[][] pi(LipschitzQuaternion x) {
        return pi(x.getEntry(0), x.getEntry(1), x.getEntry(2), x.getEntry(3));
    }
    
    /**
     * Returns the 2x2 reduced integer matrix mod q associated with x.
     * @param x Any ReducedQuaternion.
     * @return An <code>int</code>[2][2] with entries in 0, 1, ... q-1,
     * representing the 2x2 reduced integer matrix mod q associated with x.
     */
    public int[][] pi(ReducedQuaternion x) {
        return pi(x.getEntry(0), x.getEntry(1), x.getEntry(2), x.getEntry(3));
    }
    
    /**
     * Returns a 2x2 reduced integer matrix representing the <em>projective</em>
     * reduced integer matrix mod q associated with x.
     * @param x Any ProjectiveReducedQuaternion.
     * @return An <code>int</code>[2][2] with entries in 0, 1, ...q-1,
     * representing the 2x2 reduced integer matrix mod q associated with x.
     */
    public int[][] pi(ProjectiveReducedQuaternion x) {
        return pi(x.getEntry(0), x.getEntry(1), x.getEntry(2), x.getEntry(3));
    }
        
    //Returns the largest representative r, with 0<=r<q, which is a square in Z/qZ,
    //where q is a prime number.  q is not required to be prime.  Returns 0 if q<2.
    private static int findLargestSquare(int q) {
        int largestSquare = 0;
        if (q>1) {
            for (int i = 1; i <= q/2; i++) {
                int reducedSquare = Arithmetic.reducedProduct(i, i, q);
                if (reducedSquare > largestSquare) {
                    largestSquare = reducedSquare;
                }
            }
        }
        return largestSquare;
    }
    
    //Implementation based on DSV
    private static int[] findIotaVector(int q) {
        int[] pair = {0, 0};
        if (Arithmetic.isPrime(q)) {
            if (q%4 == 1) {
                pair[0] = Arithmetic.findIota(q);
                pair[1] = 0;
            } else {
                int a = findLargestSquare(q);
                pair[0] = Arithmetic.findSquareRoot(a, q);
                pair[1] = Arithmetic.findSquareRoot(-a-1, q);
            }
        }
        return pair;
    }
}


   
    




    /*
    public int[] piInverse(int a, int b, int c, int d) {
        int[] inv = {0, 0, 0, 0};
        int twoInv = Arithmetic.findInverse(2, q);
        inv[0] = Arithmetic.reducedProduct(twoInv, Arithmetic.reducedSum(a, d, q), q);
        inv[2] = Arithmetic.reducedProduct(twoInv, Arithmetic.reducedSum(b, -c, q), q);
        int v0=Arithmetic.reducedSum(a,-d, q);
        int v1=Arithmetic.reducedSum(b, c, q);
        inv[1] = Arithmetic.reducedProduct(twoInv, Arithmetic.reducedSum(Arithmetic.reducedProduct(x, v0, q), Arithmetic.reducedProduct(-y, v1, q), q), q);
        inv[3] = Arithmetic.reducedProduct(twoInv, Arithmetic.reducedSum(Arithmetic.reducedProduct(y, v0, q), Arithmetic.reducedProduct(x, v1, q), q), q);
        
        return inv;
    }
    */