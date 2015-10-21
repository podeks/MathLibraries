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
package basic_operations;

/**
 * A collection of static methods containing several elementary arithmetic functions.
 * 
 * @author pdokos
 */
public class Arithmetic {
    
    private Arithmetic() {}

    /**
     * For n &#8800; 0, returns the representative in {0, 1, ..., |n|-1} for the
     * residue class of m mod n.
     * 
     * @param m Any integer.
     * @param n Any non-zero integer.
     * @return The representative in {0, 1, ..., |n|-1} for the residue class of m mod n
     * @throws ArithmeticException if n is 0
     */
    public static int reduce(int m, int n) {
        
        if (n==0)
            throw new ArithmeticException("Division by zero ocurred.");
        
        int n0 = Math.abs(n);
        if (m >= 0) {
            return m % n0;
        }
        if (n0 == 1) {
            return 0;
        }
        return n0 + m % n0;
    }
    
    /**
     * For n &#8800; 0, returns the representative in {0, 1, ..., |n|-1} for the
     * residue class of m mod n.
     * 
     * @param m Any long integer.
     * @param n Any non-zero integer.
     * @return The representative in {0, 1, ..., |n|-1} for the residue class of m mod n
     * @throws ArithmeticException if n is 0
     */
    public static int reduce(long m, int n) {
        
        if (n==0)
            throw new ArithmeticException("Division by zero ocurred.");
        
        int n0 = Math.abs(n);
        if (m >= 0) {
            return (int) (m % n0);
        }
        if (n0 == 1) {
            return 0;
        }
        return (int) (n0 + m % n0);
    }
    
    /**
     * For n &#8800; 0, returns the representative in {0, 1, ..., |n|-1} for the
     * residue class of m mod n.
     * 
     * @param a any integer
     * @param n a nonzero short integer
     * @return The representative in {0, 1, ..., |n|-1} for the residue class of m mod n
     * @throws ArithmeticException if n is 0
     */
    public static short reduce(int a, short n) {
        
        if (n==0)
            throw new ArithmeticException("Division by zero ocurred.");
        
        short n0 = (short) Math.abs(n);
        if (a >= 0) {
            return (short) (a % n0);
        }
        if (n0 == 1) {
            return 0;
        }
        return (short) (n0 + a % n0);
        
    }
    
    /**
     * Safely computes the reduced sum of two integers a and b mod n
     * by casting a and b to long prior to computation. 
     * 
     * @param a any integer
     * @param b any integer
     * @param n any non-zero integer
     * @return reduced sum of a and b mod n.
     */
    public static int reducedSum(int a, int b, int n) { 
        return reduce((((long) a) + ((long) b)), n);
    }
    
    /**
     * Computes the reduced sum of two shorts a and b mod n. 
     * 
     * @param a any short
     * @param b any short
     * @param n any non-zero short
     * @return reduced sum of a and b mod n.
     */
    public static short reducedSum(short a, short b, short n) { 
        return reduce(a+b, n);
    }
    
    /**
     * Safely computes the reduced product of two integers a and b mod n
     * by casting a and b to long prior to computation.
     * 
     * @param a any integer
     * @param b any integer
     * @param n any non-zero integer
     * @return reduced product of a and b mod n.
     */
    public static int reducedProduct(int a, int b, int n) {
        return reduce((((long) a) * ((long) b)), n);
    }
    
    /**
     * Computes the reduced product of two shorts a and b mod n. 
     * 
     * @param a any short
     * @param b any short
     * @param n any non-zero short
     * @return reduced product of a and b mod n.
     */
    public static short reducedProduct(short a, short b, short n) { 
        return reduce(a*b, n);
    }
    
    /**
     * Finds the multiplicative inverse of a mod n; returns 0 if the inverse does not exist.  
     * Note that a is invertible mod n if and only if gcd(a, n)=1.  
     * 
     * @param a Any integer.
     * @param n Any integer.
     * @return The representative r, with 0&lt=r&ltn, of a^(-1) mod n, if it exists.  Returns 0 if the inverse does not exist, or if n=0.
     */
    public static int findInverse(int a, int n) {
        int max = n/2;
        for (int i=1; i<=max; i++) {
            int r = reducedProduct(a, i, n);
            if (r==1)
                return i;
            if (r==n-1)
                return n-i;
        }
        return 0;
    }
    
    /**
     * Finds the multiplicative inverse of a mod n; returns 0 if the inverse does not exist.  
     * Note that a is invertible mod n if and only if gcd(a, n)=1.  
     * 
     * @param a Any short integer.
     * @param n Any short integer.
     * @return The representative r, with 0&lt=r&ltn, of a^(-1) mod n, if it exists.  Returns 0 if the inverse does not exist, or if n=0.
     */
    public static short findInverse(short a, short n) {
        int max = n/2;
        for (short i=1; i<=max; i++) {
            int r = reducedProduct(a, i, n);
            if (r==1)
                return i;
            if (r==n-1)
                return (short) (n-i);
        }
        return 0;
    }
    
    /**
     * For n &#8800; 0, returns the integer representative r, with -|n|/2 &lt; r &le; |n|/2, for the
     * residue class of m mod n.
     * 
     * @param m Any integer.
     * @param n Any non-zero integer.
     * @return The integer representative r, with -|n|/2 &lt; r &le; |n|/2, for the residue class of m mod n
     * @throws ArithmeticException if n is 0
     */
    public static int centeredReduction(int m, int n) {
        if (n==0)
            throw new ArithmeticException("Division by zero ocurred.");
        int reduction = reduce(m, n);
        if (reduction > (n-1)/2) {
            return reduction-n;
        }
        return reduction;
    }
    
    /**
     * Finds the multiplicative inverse of a mod n; returns 0 if the inverse does not exist.  
     * Note that a is invertible mod n if and only if gcd(a, n)=1.  
     * 
     * @param a Any integer.
     * @param n Any integer.
     * @return The integer representative r, with -|n|/2 &lt; r &le; |n|/2, of a^(-1) mod n, if it exists.  Returns 0 if the inverse does not exist, or if n=0.
     */
    public static int centeredInverse(int a, int n) {
        return centeredReduction(findInverse(a, n), n);
    }

    /**
     * Returns the representative r, with -|n|/2 &lt; r &le; |n|/2, for the 
     * reduced product of two <code>int</code> values a and b mod n.
     * 
     * @param a any <code>int</code> value
     * @param b any <code>int</code> value
     * @param n any non-zero <code>int</code> value
     * @return the representative r, with -|n|/2 &lt; r &le; |n|/2, for the 
     * reduced product of two <code>int</code> values a and b mod n.
     */
    public static int centeredProduct(int a, int b, int n) {
        return centeredReduction(reducedProduct(a, b, n), n);
    }
    
    /**
     * Computes the multiplicative order of a mod n; returns 0 if gcd(a,n)>1.  The order of a mod n is by definition the exponent k for which a^k is congruent to 1 mod n.
     * 
     * @param a any integer.
     * @param n any non-zero integer.
     * @return the multiplicative order of a mod n.  Returns 0 if n=0, or a is not invertible mod n 
     */
    public static int getOrder(int a, int n) {
        
        if (n != 0) {
            if (gcd(a,n)>1) 
                return 0;
            int pow = reduce(a, n);
            int order=1;
            while (pow != 1) {
                pow = reducedProduct(a, pow, n);
                order++;
            }
            return order;
        }
        return 0;
    }
    
    /**
     * Finds the smallest positive integer whose reduction modulo the prime q is a generator
     * for the multiplicative group of the field Z/qZ.
     * 
     * @param q a prime number
     * @return a generator for the unit group of the field Z/qZ.  Returns 0 if q is not prime.
     */
    public static int getMultiplicativeGenerator(int q) {
        if (isPrime(q)) {
            for (int i=1; i<q; i++) {
                if (getOrder(i, q) == q-1)
                    return i;
            }
        }
        return 0;
    }

    /**
     * Finds the representative r, with 0&le;r&lt;q/2, of the square root of -1 in Z/qZ, 
     * where q is a prime number, if it exists; otherwise returns 0.  Note that the square root exists if and only if q is congruent to 1 mod 4.
     */
    public static int findIota(int q) {
        if (isPrime(q) && q % 4 == 1) {
            for (int i = 2; i <= q/2; i++) {
                if (((i * i + 1) % q) == 0) {
                    return i;
                }
            }
        }
        return 0;
    }

    /**
     * Finds the representative r, with 0&le;r&lt;q/2, of the square root of a in Z/qZ, 
     * where q is a prime number, if it exists; otherwise returns 0.
     * 
     * @param a any integer
     * @param q a prime number
     * @return square root of a mod q.  Returns 0 if q is not prime, or a square root does not exist.
     */
    public static int findSquareRoot(int a, int q) {
        if (isPrime(q)) {
            int aRed = reduce(a, q);
            for (int i = 1; i <= q/2; i++) {
                if (reduce(i * i - aRed, q) == 0) {
                    return i;
                }
            }
        }
        return 0;
    }
    
    /**
     * Determines whether a is a non-zero square mod the prime q via the Jacobi symbol.
     * 
     * @param a any integer
     * @param q a prime number
     * @return true if a is a non-zero square mod q.  Returns false otherwise, or if q is not prime.
     */
    public static boolean isSquare(int a, int q) {
        if (isPrime(q)) {
            if (q==2) 
                return (a%2 != 0);
            if (jacobiSymbol(a, q)==1)
                return true;
        }
        return false;
    }
    
    /**
     * Returns the square root of n if n&ge;0 is a perfect square, and -1 otherwise
     */
    public static int perfSqrt(int n) {
        if (n<0 || n==2 || n==3 || n==5)
            return -1;
        if (n==0 || n==1)
            return n;
        if (n==4)
            return 2;
        return perfSqrt(n, 0, n/2);
            
    }
    
    private static int perfSqrt(int n, int a, int b) {
        if (b==a+1)
            return -1;
        int mdpt = (a+b)/2;
        int mdptSq = mdpt*mdpt;
        if (mdptSq==n) {
            return mdpt;
        } else if (mdptSq<n) {
            return perfSqrt(n, mdpt, b);
        } else {
            return perfSqrt(n, a, mdpt);
        }

    }
    
        /*
     * Old. Returns square root of n if n>=0 is a perfect square, and -1 otherwise
     */
    private static int perfSqrt02(int n) {
        //int i=1;
        double sqrt = Math.sqrt(n);
        int i = (int) Math.round(Math.floor(sqrt));
        while (i <= sqrt) {
            if (i * i == n) {
                return i;
            }
            i++;
        }
        return -1;
    }
    
    /**
     * Basic primality test for n.  Checks n for divisibility by numbers &lt= sqrt(n)
     * which are congruent to &#177;1 mod 6.
     * 
     * @param n Any integer.
     */
    public static boolean isPrime(int n) {
        if (n == 2 || n == 3) {
            return true;
        }
        if (n%2==0 || n%3==0) {
            return false;
        }
        if (n > 4) {
            double maxI = (Math.sqrt(n) + 1) / 6;
            for (int i = 1; i <= maxI; i++) {
                int multOf6 = 6 * i;
                if ((n % (multOf6 - 1) == 0) || (n % (multOf6 + 1) == 0)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Computes the gcd of m and n recursively via Euclidean algorithm.
     * 
     * @param n Any integer.
     * @param m Any integer.
     * @return The greatest common divisor of m and n.  Returns 0 if m=n=0.  
     */
    public static int gcd(int n, int m) {
        int n0 = Math.abs(n);
        int m0 = Math.abs(m);

        if (m0 == 0) {
            return n0;
        }

        return gcd(m0, reduce(n0, m0));
    }

    /**
     * Computes Jacobi symbol recursively via reciprocity.
     * The Jacobi symbol is defined only for n odd and positive.
     * 
     * @param m any integer
     * @param n an odd positive integer
     * @return Jacobi symbol (m/n) of m and n.  
     * @throws ArithmeticException if n is even or negative.
     */
    public static int jacobiSymbol(int m, int n) {

        if (n > 0 && n % 2 != 0) {
            if (m == -1) {
                if ((n - 1) % 4 == 0) {
                    return 1;
                }
                return -1;
            }
            if (m == 2) {
                int resMod8 = n % 8;
                if (resMod8 == 1 || resMod8 == 7) {
                    return 1;
                }
                return -1;
            }
            if (m == 0) {
                if (n == 1) {
                    return 1;
                }
                return 0;
            }
            if (m < 0) {
                return jacobiSymbol(-1, n) * jacobiSymbol(-m, n);
            }
            if (m > n) {
                return jacobiSymbol(m % n, n);
            }
            if (m % 2 == 0) {
                return jacobiSymbol(2, n) * jacobiSymbol(m / 2, n);
            }

            if ((m - 3) % 4 == 0 && (n - 3) % 4 == 0) {
                return -jacobiSymbol(n, m);
            }
            return jacobiSymbol(n, m);
        }
        throw new ArithmeticException("jacobiSymbol undefined at parameter value.");
    }
    
    
    /**
     * Extension of the Jacobi symbol to all integers m and n. 
     * 
     * @param m any integer
     * @param n any integer
     * @return Kronecker symbol (m/n) of m and n.
     */
    public static int kroneckerSymbol(int m, int n) {
        
        if (n==-1) {
            if (m<0)
                return -1;
            return 1;
        }
        
        if (n==0) {
            if (m==-1)
                return -1;
            if (m==1)
                return 1;
            return 0;
        }
        
        if (n==2) {
            int redMod8 = reduce(m, 8);
            if (redMod8==1 || redMod8==7)
                return 1;
            if (redMod8==3 || redMod8==5)
                return -1;
            return 0;
        }
        
        if (n<0) {
            return kroneckerSymbol(m, -1) * kroneckerSymbol(m, -n);
        }
        if (n%2 == 0) {
            return kroneckerSymbol(m, 2) * kroneckerSymbol(m, n/2);
        }
        return jacobiSymbol(m, n);
    }
}

