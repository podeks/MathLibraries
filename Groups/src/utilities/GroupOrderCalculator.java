package utilities;

import basic_operations.Arithmetic;

/**
 * A collection of static methods for calculating the orders of certain finite 
 * groups, as well as the logarithms of the orders. The following families of groups 
 * are covered by this utility:<br><br>
 * 
 * (1)The Symmetric Group.<br>
 * 
 * (2) Linear Groups:<br>
 * &#160&#160&#160(i) The General Linear Group over a finite field.<br>
 * &#160&#160(ii) The Special Linear Group over a finite field.<br>
 * &#160(iii) The Symplectic Similitude Group over a finite field.<br>
 * &#160(iv) The Symplectic Group over a finite field.<br><br>
 * 
 * (3) Projective Groups:<br>
 * &#160&#160&#160(i) The Projective General Linear Group over a finite field.<br>
 * &#160&#160(ii) The Projective Special Linear Group over a finite field.<br>
 * &#160(iii) The Projective Symplectic Similitude Group over a finite field.<br>
 * &#160(iv) The Projective Symplectic Group over a finite field.
 * 
 * <p>
 * The getOrder methods return a value of 0 if the actual order exceeds
 * <code>Long.MAX_VALUE==2^64-1</code>.
 * </p>
 * 
 * @author pdokos
 */
public class GroupOrderCalculator {
    
    private GroupOrderCalculator() {}
    
    /**
     * Returns the order of the General Linear Group on a vector space of dimension
     * n over a finite field of order q (where q is any prime power).
     * 
     * <p>
     * It is the responsibility of the client to ensure that n is positive, and 
     * that q is a prime power.
     * </p>
     * 
     * @param n any positive <code>int</code> value.
     * @param q any <code>int</code> value which is a prime power.
     * @return The order of the General Linear Group on a vector space of dimension
     * n over a finite field of (prime power) order q.  This method returns a 
     * value of 0 if the actual order exceeds <code>Long.MAX_VALUE</code>.
     */
    public static long getOrderGLn(int n, int q) {

        //if (n > 0 && Arithmetic.isPrime(q)) {
            int[] pows = new int[n + 1];
            pows[0] = 1;
            for (int i = 1; i <= n; i++) {
                pows[i] = q * pows[i - 1];
            }

            long order = 1;
            for (int i = 0; i < n; i++) {
                if (Long.MAX_VALUE / order <= pows[n] - pows[i]) {
                    return 0;
                }
                order = (pows[n] - pows[i]) * order;
            }
            return order;
        //}
        // return 0;
    }

    /**
     * Returns the order of the Special Linear Group on a vector space of dimension
     * n over a finite field of order q (where q is any prime power).
     * 
     * <p>
     * It is the responsibility of the client to ensure that n is positive, and 
     * that q is a prime power.
     * </p>
     * 
     * @param n any positive <code>int</code> value.
     * @param q any <code>int</code> value which is a prime power.
     * @return The order of the Special Linear Group on a vector space of dimension
     * n over a finite field of (prime power) order q.  This method returns a 
     * value of 0 if the actual order exceeds <code>Long.MAX_VALUE</code>.
     */
    public static long getOrderSLn(int n, int q) {

        //if (n > 0 && Arithmetic.isPrime(q)) {
            int[] pows = new int[n + 1];
            pows[0] = 1;
            for (int i = 1; i <= n; i++) {
                pows[i] = q * pows[i - 1];
            }

            long order = 1;
            for (int i = 0; i < n - 1; i++) {
                if (Long.MAX_VALUE / order <= pows[n] - pows[i]) {
                    return 0;
                }
                order = (pows[n] - pows[i]) * order;
            }
            if (Long.MAX_VALUE / order <= pows[n - 1]) {
                return 0;
            }
            order = pows[n - 1] * order;
            return order;
        //}
        //return 0;
    }

    /**
     * Returns the order of the Symplectic Similitude Group on a 
     * vector space of dimension 2m over a finite field of order q (where q is 
     * any prime power).
     * 
     * <p>
     * It is the responsibility of the client to ensure that m is positive, and 
     * that q is a prime power.
     * </p>
     * 
     * @param m any positive <code>int</code> value.
     * @param q any <code>int</code> value which is a prime power.
     * @return The order of the Symplectic Similitude Group on a vector space of dimension
     * 2m over a finite field of (prime power) order q.  This method returns a 
     * value of 0 if the actual order exceeds <code>Long.MAX_VALUE</code>.
     */
    public static long getOrderGSp_2m(int m, int q) {
        long spOrder = getOrderSp_2m(m, q);
        if (spOrder == 0) {
            return 0;
        }
        if (Long.MAX_VALUE / spOrder <= (q - 1)) {
            return 0;
        }
        return (q - 1) * spOrder;
    }

    /**
     * Returns the order of the Symplectic Group on a 
     * vector space of dimension 2m over a finite field of order q (where q is 
     * any prime power).
     * 
     * <p>
     * It is the responsibility of the client to ensure that m is positive, and 
     * that q is a prime power.
     * </p>
     * 
     * @param m any positive <code>int</code> value.
     * @param q any <code>int</code> value which is a prime power.
     * @return The order of the Symplectic Group on a vector space of dimension
     * 2m over a finite field of (prime power) order q.  This method returns a 
     * value of 0 if the actual order exceeds <code>Long.MAX_VALUE</code>.
     */
    public static long getOrderSp_2m(int m, int q) {
        int[] powers = new int[m + 1];
        powers[0] = 1;
        long order = 1;
        int qSq = q * q;
        for (int i = 1; i <= m; i++) {
            powers[i] = qSq * powers[i - 1];
        }
        for (int i = 1; i <= m; i++) {
            if (Long.MAX_VALUE / order <= (powers[i] - 1) * q * powers[i - 1]) {
                return 0;
            }
            order = order * (powers[i] - 1) * q * powers[i - 1];
        }
        return order;
    }
    
    /**
     * Returns the order of the Projective General Linear Group on a vector space of dimension
     * n over a finite field of order q (where q is any prime power).
     * 
     * <p>
     * It is the responsibility of the client to ensure that n is positive, and 
     * that q is a prime power.
     * </p>
     * 
     * @param n any positive <code>int</code> value.
     * @param q any <code>int</code> value which is a prime power.
     * @return The order of the Projective General Linear Group on a vector space of dimension
     * n over a finite field of (prime power) order q.  This method returns a 
     * value of 0 if the actual order exceeds <code>Long.MAX_VALUE</code>.
     */
    public static long getOrderPGLn(int n, int q) {
        return getOrderSLn(n, q);
    }
    
    /**
     * Returns the order of the Projective Special Linear Group on a vector space of dimension
     * n over a finite field of order q (where q is any prime power).
     * 
     * <p>
     * It is the responsibility of the client to ensure that n is positive, and 
     * that q is a prime power.
     * </p>
     * 
     * @param n any positive <code>int</code> value.
     * @param q any <code>int</code> value which is a prime power.
     * @return The order of the Projective Special Linear Group on a vector space of dimension
     * n over a finite field of (prime power) order q.  This method returns a 
     * value of 0 if the actual order exceeds <code>Long.MAX_VALUE</code>.
     */
    public static long getOrderPSLn(int n, int q) {
        return getOrderSLn(n, q)/Arithmetic.gcd(q-1, n);
    }
    
    /**
     * Returns the order of the Projective Symplectic Similitude Group on a 
     * vector space of dimension 2m over a finite field of order q (where q is 
     * any prime power).
     * 
     * <p>
     * It is the responsibility of the client to ensure that m is positive, and 
     * that q is a prime power.
     * </p>
     * 
     * @param m any positive <code>int</code> value.
     * @param q any <code>int</code> value which is a prime power.
     * @return The order of the Projective Symplectic Similitude Group on a vector space of dimension
     * 2m over a finite field of (prime power) order q.  This method returns a 
     * value of 0 if the actual order exceeds <code>Long.MAX_VALUE</code>.
     */
    public static long getOrderPGSp_2m(int m, int q) {
        return getOrderSp_2m(m, q);
    }
    
    /**
     * Returns the order of the Projective Symplectic Group on a 
     * vector space of dimension 2m over a finite field of order q (where q is 
     * any prime power).
     * 
     * <p>
     * It is the responsibility of the client to ensure that m is positive, and 
     * that q is a prime power.
     * </p>
     * 
     * @param m any positive <code>int</code> value.
     * @param q any <code>int</code> value which is a prime power.
     * @return The order of the Projective Symplectic Group on a vector space of dimension
     * 2m over a finite field of (prime power) order q.  This method returns a 
     * value of 0 if the actual order exceeds <code>Long.MAX_VALUE</code>.
     */
    public static long getOrderPSp_2m(int m, int q) {
        if (q != 2) {
            return getOrderSp_2m(m, q) / 2;
        } else {
            return getOrderSp_2m(m, q);
        }
    }
    
    /**
     * Returns the order of the Symmetric Group on n letters.  This is just the
     * factorial of n.
     * 
     * <p>
     * It is the responsibility of the client to ensure that n is positive.
     * </p>
     * 
     * @param n any positive <code>int</code> value.

     * @return The order of the Symmetric Group on n letters.  This method returns a 
     * value of 0 if the actual order exceeds <code>Long.MAX_VALUE</code>.
     */
    public static long getOrderSymmetricGroup(int n) {
        long order = 1;
        if (n > 0 && getLogOrderSymmetricGroup(n) < 64 * Math.log(2)) {
            for (int i = 1; i <= n; i++) {
                order = order * i;
            }
            return order;
        }
        return 0;
    }

    /**
     * Returns the logarithm of the order of the General Linear Group on a vector space of dimension
     * n over a finite field of order q (where q is any prime power).
     * 
     * <p>
     * It is the responsibility of the client to ensure that n is positive, and 
     * that q is a prime power.
     * </p>
     * 
     * @param n any positive <code>int</code> value.
     * @param q any <code>int</code> value which is a prime power.
     * @return The logarithm of the order of the General Linear Group on a vector space of dimension
     * n over a finite field of (prime power) order q.
     */
    public static double getLogOrderGLn(int n, int q) {
        //if (n > 0 && Arithmetic.isPrime(q)) {
            return Math.log(q - 1) + getLogOrderSLn(n, q);
        //}
        //return -1;
    }

    /**
     * Returns the logarithm of the order of the Special Linear Group on a vector space of dimension
     * n over a finite field of order q (where q is any prime power).
     * 
     * <p>
     * It is the responsibility of the client to ensure that n is positive, and 
     * that q is a prime power.
     * </p>
     * 
     * @param n any positive <code>int</code> value.
     * @param q any <code>int</code> value which is a prime power.
     * @return The logarithm of the order of the Special Linear Group on a vector space of dimension
     * n over a finite field of (prime power) order q.
     */
    public static double getLogOrderSLn(int n, int q) {
        //if (n > 0 && Arithmetic.isPrime(q)) {
            int[] pows = new int[n + 1];
            pows[0] = 1;
            for (int i = 1; i <= n; i++) {
                pows[i] = q * pows[i - 1];
            }

            double order = 0;
            for (int i = 0; i < n - 1; i++) {
                order += Math.log(pows[n] - pows[i]);
            }
            order += Math.log(pows[n - 1]);
            return order;
        //}
        //return -1;
    }

    /**
     * Returns the logarithm of the order of the Symplectic Similitude Group on a vector space of dimension
     * 2m over a finite field of order q (where q is any prime power).
     * 
     * <p>
     * It is the responsibility of the client to ensure that m is positive, and 
     * that q is a prime power.
     * </p>
     * 
     * @param m any positive <code>int</code> value.
     * @param q any <code>int</code> value which is a prime power.
     * @return The logarithm of the order of the Symplectic Similitude Group on a vector space of dimension
     * 2m over a finite field of (prime power) order q.
     */
    public static double getLogOrderGSp_2m(int m, int q) {
        return Math.log(q - 1) + getLogOrderSp_2m(m, q);
    }
    
    /**
     * Returns the logarithm of the order of the Symplectic Group on a vector space of dimension
     * 2m over a finite field of order q (where q is any prime power).
     * 
     * <p>
     * It is the responsibility of the client to ensure that m is positive, and 
     * that q is a prime power.
     * </p>
     * 
     * @param m any positive <code>int</code> value.
     * @param q any <code>int</code> value which is a prime power.
     * @return The logarithm of the order of the Symplectic Group on a vector space of dimension
     * 2m over a finite field of (prime power) order q.
     */
    public static double getLogOrderSp_2m(int m, int q) {
        int[] powers = new int[m + 1];
        powers[0] = 1;
        double order = 0;
        int qSq = q * q;
        for (int i = 1; i <= m; i++) {
            powers[i] = qSq * powers[i - 1];
        }
        for (int i = 1; i <= m; i++) {
            order += Math.log((powers[i] - 1) * q * powers[i - 1]);
        }
        return order;
    }    
    
    /**
     * Returns the logarithm of the order of the Projective General Linear Group on a vector space of dimension
     * n over a finite field of order q (where q is any prime power).
     * 
     * <p>
     * It is the responsibility of the client to ensure that n is positive, and 
     * that q is a prime power.
     * </p>
     * 
     * @param n any positive <code>int</code> value.
     * @param q any <code>int</code> value which is a prime power.
     * @return The logarithm of the order of the Projective General Linear Group on a vector space of dimension
     * n over a finite field of (prime power) order q.
     */
    public static double getLogOrderPGLn(int n, int q) {
        return getLogOrderSLn(n, q);
    }
    
    /**
     * Returns the logarithm of the order of the Projective Special Linear Group on a vector space of dimension
     * n over a finite field of order q (where q is any prime power).
     * 
     * <p>
     * It is the responsibility of the client to ensure that n is positive, and 
     * that q is a prime power.
     * </p>
     * 
     * @param n any positive <code>int</code> value.
     * @param q any <code>int</code> value which is a prime power.
     * @return The logarithm of the order of the Projective Special Linear Group on a vector space of dimension
     * n over a finite field of (prime power) order q.
     */
    public static double getLogOrderPSLn(int n, int q) {
        return getLogOrderSLn(n, q) - Math.log(Arithmetic.gcd(q-1, n));
    }
    
    /**
     * Returns the logarithm of the order of the Projective Symplectic Similitude Group on a vector space of dimension
     * 2m over a finite field of order q (where q is any prime power).
     * 
     * <p>
     * It is the responsibility of the client to ensure that m is positive, and 
     * that q is a prime power.
     * </p>
     * 
     * @param m any positive <code>int</code> value.
     * @param q any <code>int</code> value which is a prime power.
     * @return The logarithm of the order of the Projective Symplectic Similitude Group on a vector space of dimension
     * 2m over a finite field of (prime power) order q.
     */
    public static double getLogOrderPGSp_2m(int m, int q) {
        return getLogOrderSp_2m(m, q);
    }
    
    /**
     * Returns the logarithm of the order of the Projective Symplectic Group on a vector space of dimension
     * 2m over a finite field of order q (where q is any prime power).
     * 
     * <p>
     * It is the responsibility of the client to ensure that m is positive, and 
     * that q is a prime power.
     * </p>
     * 
     * @param m any positive <code>int</code> value.
     * @param q any <code>int</code> value which is a prime power.
     * @return The logarithm of the order of the Projective Symplectic Group on a vector space of dimension
     * 2m over a finite field of (prime power) order q.
     */
    public static double getLogOrderPSp_2m(int m, int q) {
        if (q != 2) {
            return getLogOrderSp_2m(m, q) - Math.log(2);
        } else {
            return getLogOrderSp_2m(m, q);
        }
    }
    
    /**
     * Returns the logarithm of the order of the Symmetric Group on n letters.
     * 
     * <p>
     * It is the responsibility of the client to ensure that n is positive.
     * </p>
     * 
     * @param n any positive <code>int</code> value.

     * @return The order of the Symmetric Group on n letters.
     */
    public static double getLogOrderSymmetricGroup(int n) {
        double log = 0;
        if (n > 0) {
            for (int i = 2; i <= n; i++) {
                log += Math.log(i);
            }
        }
        return log;
    }
    
}
