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
package groups;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import api.Group;

/**
 * A class that models the group of permutations on the set 
 * {0, 1, ..., n-1}, for any int n>0, implemented in the framework of the Group 
 * interface.
 * 
 * <p>
 * Elements of the group are represented internally by an <code>int</code> array
 * of length n containing a permutation of the integers 0, 1, ..., n-1.
 * </p>
 * 
 * @author pdokos
 */
public class SymmetricGroup implements Group<SymmetricGroup>{

    private int numLetters;
    private int[] permutation;

    /**
     * Constructor for a SymmetricGroup object, invoked by specifying an array
     * containing some permutation of the set {0, 1, ..., n-1}, where n>0 is the
     * length of the array.
     * 
     * <p>
     * It is the responsibility of the client to ensure that the given array
     * is a permutation.
     * </p>
     * 
     * @param perm an <code>int</code> array listing the first n non-negative 
     * integers, n being the length of the array.
     */
    public SymmetricGroup(int[] perm) {
        numLetters = perm.length;
        permutation = new int[numLetters];
        System.arraycopy(perm, 0, permutation, 0, numLetters);
    }

    private SymmetricGroup(int numLetters) {
        this.numLetters = numLetters;
        permutation = new int[numLetters];
    }
    
    private void initializeValue(int m, int n) {
        //if (0 <= m && m < numLetters && 0 <= n && n < numLetters) {
            permutation[m]=n;
        //}
    }
    
    /**
     * A static helper for producing a SymmetricGroup element on n letters which
     * is a cycle.
     * 
     * @param seq An <code>int</code> array, representing a cycle, whose entries
     * are from the set {0, 1, ..., n-1} and without repeated elements.
     * 
     * @param n The number of elements that the object permutes.
     * 
     * @return The SymmetricGroup element representing the cycle specified by 
     * the parameters. 
     */
    public static SymmetricGroup createCycle(int[] seq, int n) {
        if (n>=seq.length) {
            int[] perm = new int[n];
            for (int i=0; i<n; i++) {
                perm[i]=i;
            }
            for (int i=0; i<seq.length-1; i++) {
                perm[seq[i]] = seq[i+1];
            }
            perm[seq[seq.length-1]] = seq[0];
            return new SymmetricGroup(perm);
        }
        return null;
    }
    
    /**
     * A static helper for producing a SymmetricGroup element on n letters which
     * is specified by a cycle decomposition.
     * 
     * <p>
     * It is the responsibility of the client to ensure that <code>cycles</code> is a valid
     * cycle decomposition.
     * </p>
     * 
     * @param cycles An two-dimensional <code>int</code> array, not necessarily 
     * rectangular, representing a cycle decomposition, whose entries are from 
     * the set {0, 1, ..., n-1} and without repeated elements.
     * 
     * @param n The number of elements that the object permutes.
     * 
     * @return The SymmetricGroup element representing the cycle specified by 
     * the parameters. 
     */
    public static SymmetricGroup cycleDec2Perm(int[][] cycles, int n) {
        int[] perm = new int[n];
        for (int i = 0; i < n; i++) {
            perm[i] = i;
        }
        for (int i=0; i<cycles.length; i++) {
            for (int j=0; j<cycles[i].length-1; j++) {
                perm[cycles[i][j]] = cycles[i][j+1];
            }
            perm[cycles[i][cycles[i].length-1]] = cycles[i][0];
        } 
        return new SymmetricGroup(perm);
    }
    
    /**
     * A static utility for checking if  the given array represents a legitimate 
     * permutation that can be passed to the constructor of this class.
     * 
     * @param arr an <code>int</code> array.
     * 
     * @return true if the entries of the array consist of the values 0, 1, ..., 
     * n-1, in any order and without repetition (so that n is the length of the 
     * array).
     */
    public static boolean isAPermutation(int[] arr) {
        Set<Integer> letters = new HashSet<Integer>();
        int len = arr.length;
        if (len==0)
            return false;
        for (int i=0; i<len; i++) {
            int elt = arr[i];
            if (elt<0 || elt>=len || letters.contains(elt))
                return false;
            else letters.add(elt);
        } 
        return true;
    }
    
    /**
     * A static helper for producing the identity element of SymmetricGroup 
     * on the set {0, 1, ..., n-1}.
     * 
     * @param n a positive <code>int</code> value.
     * 
     * @return The identity element of SymmetricGroup on the set {0, 1, ..., n-1}.
     */
    public static SymmetricGroup constructIdentity(int n) {
        SymmetricGroup id = new SymmetricGroup(n);
        for (int i = 0; i < n; i++) {
            id.initializeValue(i, i);
        }
        return id;
    }
    
    /**
     * A static helper for producing a random element of SymmetricGroup 
     * on the set {0, 1, ..., n-1}.
     * 
     * @param n Any positive <code>int</code> value.
     * 
     * @return A random element of SymmetricGroup on the set {0, 1, ..., n-1}.
     */
    public static SymmetricGroup constructRandomElement(int n) {
        
        if (n>0) {
            List<Integer> letters = new ArrayList<Integer>();
            for (int i=0; i<n; i++) {
                letters.add(i);
            }
            Random rand = new Random();
            int[] perm = new int[n];
            for (int i=0; i<n; i++) {
                int nextLetterIndex = rand.nextInt(n-i);
                perm[i] = letters.remove(nextLetterIndex);
            }
            return new SymmetricGroup(perm);
        }
        return null;
    }
    
    /**
     * An override of the <code>equals</code> method so as to return
     * true if h is an instance of SymmetricGroup on the same number of 
     * letters, which represents the same permutation as the element making the 
     * call.
     * 
     * @param h Any object.
     * 
     * @return true if h is an instance of SymmetricGroup on the same number of 
     * letters, which represents the same permutation as the element making the 
     * call.
     */
    @Override
    public boolean equals(Object h) {

        if (h instanceof SymmetricGroup) {
            if (((SymmetricGroup) h).getNumLetters() == numLetters) {
                return Arrays.equals(permutation, ((SymmetricGroup) h).getPermutation());
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
        int hash = 5;
        hash = 19 * hash + this.numLetters;
        hash = 19 * hash + Arrays.hashCode(this.permutation);
        return hash;
    }
    
    /**
     * Returns the number of elements this element permutes (including fixed 
     * points).
     * 
     * @return the number of elements this element permutes (including fixed 
     * points).
     */
    public int getNumLetters() {
        return numLetters;
    }
    
    private int[] getPermutation() {
        return permutation;
    }
    
    /**
     * The value that m gets mapped to under this permutation.
     * 
     * @param m
     * 
     * @return The value that m gets mapped to under this permutation.
     */
    public int getImageOf(int m) {
        //if (0 <= m && m < numLetters) {
            return permutation[m];
        //}
        //return -1;
    }

    /**
     * @param h Any element of SymmetricGroup that is operational with the element making the call. 
     */
    @Override
    public SymmetricGroup leftProductBy(SymmetricGroup h) {

        if (h.getNumLetters()==this.getNumLetters()) {
            SymmetricGroup prod = new SymmetricGroup(numLetters);
            for (int i = 0; i < numLetters; i++) {
                prod.initializeValue(i, h.getImageOf(permutation[i]));
            }
            return prod;
        }

        return null;
    }

    /**
     * @param h Any element of SymmetricGroup that is operational with the element making the call. 
     */
    @Override
    public SymmetricGroup rightProductBy(SymmetricGroup h) {
        if (h.getNumLetters()==this.getNumLetters()) {
            SymmetricGroup prod = new SymmetricGroup(numLetters);
            for (int i = 0; i < numLetters; i++) {
                prod.initializeValue(i, permutation[h.getImageOf(i)]);
            }
            return prod;
        }

        return null;
        //return new SymmetricGroup(new int[0]);
    }

    @Override
    public SymmetricGroup getInverse() {
        SymmetricGroup inv = new SymmetricGroup(numLetters);
        for (int i = 0; i < numLetters; i++) {
            inv.initializeValue(permutation[i], i);
        }
        return inv;
    }

    @Override
    public SymmetricGroup getIdentity() {
        SymmetricGroup id = new SymmetricGroup(numLetters);
        for (int i = 0; i < numLetters; i++) {
            id.initializeValue(i, i);
        }
        return id;
    }

    /**
     * Returns true if h is a permutation on the same number of letters 
     * (including fixed points) as the object making the call.
     * 
     * @param h Any SymmetricGroup element.
     * @return <code>true</code> if h is a permutation on the same number of letters 
     * (including fixed points) as the object making the call.
     */
    @Override
    public boolean isOperationalWith(SymmetricGroup h) {
        return this.numLetters==h.getNumLetters();
    }
    
    /**
     * Overrides the <code>toString</code> method so as to return a String 
     * representation the permutation, in the 
     * format of a list of integer values separated by single spaces, surrounded by 
     * square brackets.
     * 
     * @return A String representation of the permutation, in the 
     * format of a list of integer values separated by single spaces, surrounded by 
     * square brackets.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < numLetters; i++) {
            sb.append(permutation[i]);
            if (i != numLetters-1)
                sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * Return a String representation of the permutation, in the 
     * format of a list of integer values separated by single spaces.
     * 
     * @return A String representation of the permutation, in the 
     * format of a list of integer values separated by single spaces.
     */
    public String toUnpunctuatedString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numLetters; i++) {
            sb.append(permutation[i]);
            if (i != numLetters-1)
                sb.append(" ");
        }
        return sb.toString();
    }

}
