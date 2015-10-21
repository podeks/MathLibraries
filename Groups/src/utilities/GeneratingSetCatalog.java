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
package utilities;

import java.util.HashSet;
import java.util.Set;
import basic_operations.Arithmetic;
import groups.GL2_PrimeField;
import groups.GLn_PrimeField;
import groups.PGL2_PrimeField;
import groups.PGLn_PrimeField;
import groups.SymmetricGroup;

/**
 * A collection of static methods that return generating sets for certain 
 * subgroups of the following three families of groups:</br>
 * &#160&#160(i) the General Linear Group on a finite dimensional vector space over a 
 * prime field, </br>
 * &#160(ii) the Projective General Linear Group on a finite dimensional vector space
 * over a prime field, </br> 
 * (iii) the Symmetric Group on n letters.
 * 
 * <p>
 * The subgroups produced by these methods include: (i) GLn, SLn, GSp2m, Sp2m, 
 * (ii) PGLn, PSLn, PGSp2m, PSp2m, and (iii) S_n, A_n.  
 * Besides these subgroups, there are also methods for producing
 * generating sets for some of the sporadic finite simple groups (as 
 * permutation groups or linear groups), namely the 
 * first three Mathieu groups M11, M12, M22, and the first two Janko groups
 * J1 and J2.
 * </p>
 * 
 * 
 * 
 * <p>
 * For each of the methods, the 
 * generating set is returned as a Set&ltG&gt, where G is one of the Group 
 * implementations of the three families (i)-(iii) above, found in the package 
 * groups.
 * </p>
 * 
 * <p>
 * The definitions of the pair-generating sets (those specified by the String 
 * "Pair" for the <code>name</code> parameter) for the linear groups can be 
 * found in the article [T].  The generating sets offered for the projective 
 * linear groups are the projections of those offered for the linear groups.
 * </p>
 * <p>
 * The generator sets for the alternating and symmetric 
 * groups are taken from the notes [C].  Most of the generator sets for the
 * sporadic finite simple groups are taken from the online ATLAS of Group 
 * Representations [ATLAS].
 * </p>
 * 
 * <p>
 * References:
 * </p>
 * <p>
 * &#160 &#160 [C] K. Conrad, "Generating Sets" <a href="http://www.math.uconn.edu/~kconrad/blurbs/grouptheory/genset.pdf">UConn link</a> </br>
 * &#160 &#160 [T] D. E. Taylor, "Pairs of Generators for Matrix Groups. I".</br>
 * &#160 &#160 [ATLAS] <a href="http://brauer.maths.qmul.ac.uk/Atlas/">ATLAS of Finite Group Representations</a>
 * </p>
 * 
 * @author pdokos
 */
public class GeneratingSetCatalog {
    
    private GeneratingSetCatalog() {}

    /**
     * Returns a pair-generating set for the group GL2(F_q), where q is a 
     * <code>short</code> integer prime.
     * 
     * <p>
     * Reference:
     * </p>
     * <p>
     * &#160 &#160 [T] D. E. Taylor, "Pairs of Generators for Matrix Groups. I".
     * </p>
     *
     * @param name The String "Pair" is the only choice offered for this group.
     * @param q Any <code>short</code> integer prime.
     * @return A pair-generating set for the group GL2(F_q), as a Set&ltGL2_PrimeField&gt.
     */
    public static Set<GL2_PrimeField> getGL2GenSet(String name, short q) {

        Set<GL2_PrimeField> genSet = new HashSet<GL2_PrimeField>();

        if (Arithmetic.isPrime(q)) {

            if (name.equals("Pair")) {

                if (q != 2) {
                    int xi = Arithmetic.getMultiplicativeGenerator(q);
                    int xiInv = Arithmetic.findInverse(xi, q);

                    genSet.add(new GL2_PrimeField(-1, 1, -1, 0, q));
                    genSet.add(new GL2_PrimeField(0, -1, 1, -1, q));
                    genSet.add(new GL2_PrimeField(xi, 0, 0, 1, q));
                    genSet.add(new GL2_PrimeField(xiInv, 0, 0, 1, q));
                } else {
                    genSet.add(new GL2_PrimeField(1, 1, 0, 1, q));
                    genSet.add(new GL2_PrimeField(0, 1, 1, 0, q));
                }
            }
        }
        return genSet;
    }
    
    /**
     * Returns a generating set for the group SL2(F_q), where q is a 
     * <code>short</code> integer prime.
     * 
     * <p>
     * The choices offered by this method are the following:</br> 
     * "Standard", "L1", "L2", "L3", or "Pair"
     * </p>
     * The generating sets L1, L2, and L3 are those of the Lubotzky 1-2-3 
     * problem (see [L]).
     * </p>
     * <p>
     * The definition of the Pair generating set can be found in [T].
     * </p>
     * <p>
     * References:
     * </p>
     * <p>
     * &#160 &#160 [L] A. Lubotzky, "Expanders in Pure and Applied Mathematics", 
     * Bulletin of the American Mathematical Society, 2012.</br>
     * &#160 &#160 [T] D. E. Taylor, "Pairs of Generators for Matrix Groups. I".
     * </p>
     * 
     * 
     * @param name One of the following Strings: "Standard", "L1", "L2", "L3", or "Pair".
     * @param q A <code>short</code> integer prime.
     * @return A generating set for the group SL2(F_q), as a Set&ltGL2_PrimeField&gt.
     */
    public static Set<GL2_PrimeField> getSL2GenSet(String name, short q) {

        
        Set<GL2_PrimeField> genSet = new HashSet<GL2_PrimeField>();

        if (Arithmetic.isPrime(q)) {

            if (name.equals("Standard")) {
                genSet.add(new GL2_PrimeField(1, 1, 0, 1, q));
                genSet.add(new GL2_PrimeField(0, -1, 1, 0, q));
                genSet.add(new GL2_PrimeField(1, -1, 0, 1, q));
                genSet.add(new GL2_PrimeField(0, 1, -1, 0, q));
            }
            if (name.equals("L1")) {
                genSet.add(new GL2_PrimeField(1, 1, 0, 1, q));
                genSet.add(new GL2_PrimeField(1, 0, 1, 1, q));
                genSet.add(new GL2_PrimeField(1, -1, 0, 1, q));
                genSet.add(new GL2_PrimeField(1, 0, -1, 1, q));
            }
            if (name.equals("L2")) {
                genSet.add(new GL2_PrimeField(1, 2, 0, 1, q));
                genSet.add(new GL2_PrimeField(1, 0, 2, 1, q));
                genSet.add(new GL2_PrimeField(1, -2, 0, 1, q));
                genSet.add(new GL2_PrimeField(1, 0, -2, 1, q));
            }
            if (name.equals("L3")) {
                genSet.add(new GL2_PrimeField(1, 3, 0, 1, q));
                genSet.add(new GL2_PrimeField(1, 0, 3, 1, q));
                genSet.add(new GL2_PrimeField(1, -3, 0, 1, q));
                genSet.add(new GL2_PrimeField(1, 0, -3, 1, q));
            }
            if (name.equals("Pair")) {
                if (q > 3) {
                    int xi = Arithmetic.getMultiplicativeGenerator(q);
                    int xiInv = Arithmetic.findInverse(xi, q);

                    genSet.add(new GL2_PrimeField(-1, 1, -1, 0, q));
                    genSet.add(new GL2_PrimeField(0, -1, 1, -1, q));
                    genSet.add(new GL2_PrimeField(xi, 0, 0, xiInv, q));
                    genSet.add(new GL2_PrimeField(xiInv, 0, 0, xi, q));
                } else {
                    genSet.add(new GL2_PrimeField(1, 1, 0, 1, q));
                    genSet.add(new GL2_PrimeField(1, -1, 0, 1, q));
                    genSet.add(new GL2_PrimeField(0, 1, -1, 0, q));
                    genSet.add(new GL2_PrimeField(0, -1, 1, 0, q));
                }
            }
        }
        return genSet;
    }
    
    /**
     * Returns a pair-generating set for the General Linear Group on a finite 
     * dimensional vector space over a prime field.
     * 
     * <p>
     * Reference:
     * </p>
     * <p>
     * &#160 &#160 [T] D. E. Taylor, "Pairs of Generators for Matrix Groups. I".
     * </p>
     * 
     * @param name The String "Pair" is the only choice offered for this group.
     * @param n A positive <code>int</code> value for the dimension.
     * @param q Any <code>short</code> integer prime.
     * @return A pair-generating set for the group GLn(F_q), as a Set&ltGLn_PrimeField&gt.
     */
    public static Set<GLn_PrimeField> getGLnGeneratingSet(String name, int n, short q) {

        Set<GLn_PrimeField> genSet = new HashSet<GLn_PrimeField>();

        if (Arithmetic.isPrime(q) && n > 2) {
            if (name.equals("Pair")) {

                int[][] rep1 = new int[n][n];
                int[][] rep2 = new int[n][n];

                if (q==2) return getSLnGeneratingSet(name, n, q); 
                if (q > 2) {

                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            if (j == i - 1) {
                                rep1[i][j] = -1;
                            } else {
                                rep1[i][j] = 0;
                            }
                        }
                    }
                    rep1[0][0] = -1;
                    rep1[0][n - 1] = 1;

                    int xi = Arithmetic.getMultiplicativeGenerator(q);

                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            if (j == i) {
                                if (i > 0) {
                                    rep2[i][j] = 1;
                                } else {
                                    rep2[i][j] = xi;
                                } 
                            } else {
                                rep2[i][j] = 0;
                            }
                        }
                    }
                }
                GLn_PrimeField a_n = new GLn_PrimeField(rep1, q);
                GLn_PrimeField b_n = new GLn_PrimeField(rep2, q);

                genSet.add(b_n);
                genSet.add(a_n);
                genSet.add(b_n.getInverse());
                genSet.add(a_n.getInverse());
            }
        }
        return genSet;
    }


    /**
     * Returns a pair-generating set for the Special Linear Group on a finite 
     * dimensional vector space over a prime field.
     * 
     * <p>
     * The definition of the Pair generating set can be found in [T].
     * </p>
     * <p>
     * Reference:
     * </p>
     * <p>
     * &#160 &#160 [T] D. E. Taylor, "Pairs of Generators for Matrix Groups. I".
     * </p>
     * 
     * @param name One of the Strings "Standard" or "Pair".
     * @param n A positive <code>int</code> value for the dimension.
     * @param q Any <code>short</code> integer prime.
     * @return A pair-generating set for the group SLn(F_q), as a Set&ltGLn_PrimeField&gt.
     */
    public static Set<GLn_PrimeField> getSLnGeneratingSet(String name, int n, short q) {

        Set<GLn_PrimeField> genSet = new HashSet<GLn_PrimeField>();

        if (Arithmetic.isPrime(q) && n > 2) {

            if (name.equals("Standard")) {
                int[][] rep = new int[n][n];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (j == i + 1) {
                            rep[i][j] = 1;
                        } else {
                            rep[i][j] = 0;
                        }
                    }
                }
                rep[n - 1][0] = (int) Math.pow(-1, (n - 1) % 2);

                GLn_PrimeField a_n = GLn_PrimeField.constructElementaryMtx(n, q, 1, 2);
                GLn_PrimeField b_n = new GLn_PrimeField(rep, q);

                genSet.add(b_n);
                genSet.add(a_n);
                genSet.add(b_n.getInverse());
                genSet.add(a_n.getInverse());
            }

            if (name.equals("Pair")) {

                int[][] rep1 = new int[n][n];
                int[][] rep2 = new int[n][n];

                if (q > 3) {

                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            if (j == i - 1) {
                                rep1[i][j] = -1;
                            } else {
                                rep1[i][j] = 0;
                            }
                        }
                    }
                    rep1[0][0] = -1;
                    rep1[0][n - 1] = 1;

                    int xi = Arithmetic.getMultiplicativeGenerator(q);
                    int xiInv = Arithmetic.findInverse(xi, q);

                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            if (j == i) {
                                if (i > 1) {
                                    rep2[i][j] = 1;
                                } else if (i == 0) {
                                    rep2[i][j] = xi;
                                } else {
                                    rep2[i][j] = xiInv;
                                }
                            } else {
                                rep2[i][j] = 0;
                            }
                        }
                    }
                } else {

                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            if (j == i - 1) {
                                rep1[i][j] = -1;
                            } else {
                                rep1[i][j] = 0;
                            }
                        }
                    }
                    rep1[0][0] = 0;
                    rep1[0][n - 1] = 1;


                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            if (j == i) {
                                rep2[i][j] = 1;
                            } else {
                                rep2[i][j] = 0;
                            }
                        }
                    }
                    rep2[0][1] = 1;
                }

                GLn_PrimeField a_n = new GLn_PrimeField(rep1, q);
                GLn_PrimeField b_n = new GLn_PrimeField(rep2, q);

                genSet.add(b_n);
                genSet.add(a_n);
                genSet.add(b_n.getInverse());
                genSet.add(a_n.getInverse());


            }
        }

        return genSet;

    }    
    
    /**
     * Returns a generating set for the Symplectic Similitude Group on a finite 
     * dimensional vector space over a prime field.
     * 
     * <p>
     * The returned generating set is a triplet consisting of the 
     * pair-generators defined in [T] for the Symplectic Group, together with
     * one diagonal similitude matrix.
     * </p>
     * 
     * <p>
     * Reference:
     * </p>
     * <p>
     * &#160 &#160 [T] D. E. Taylor, "Pairs of Generators for Matrix Groups. I".
     * </p>
     * 
     * @param name The String "Triplet" is the only choice offered for this group.
     * @param m A positive <code>int</code> value for the dimension.
     * @param q Any <code>short</code> integer prime.
     * @return A generating set for the group GSp_2m(F_q), as a Set&ltGLn_PrimeField&gt.
     */
    public static Set<GLn_PrimeField> getGSp_2mGeneratingSet(String name, int m, short q) {
        
        Set<GLn_PrimeField> genSet = new HashSet<GLn_PrimeField>();
        
        if (Arithmetic.isPrime(q) && m > 1) {
            short dim= (short) (2*m);
            
            if (name.equals("Triplet")) {
                genSet.addAll(getSp_2mGeneratingSet("Pair", m, q));
                
                int alpha = Arithmetic.getMultiplicativeGenerator(q);
                int[][] rep = new int[dim][dim];
                for (int i = 0; i < dim; i++) {
                    for (int j = 0; j < dim; j++) {
                        if (i == j) {
                            if (i < m) {
                                rep[i][j] = 1;
                            } else {
                                rep[i][j] = alpha;
                            }
                        } else {
                            rep[i][j] = 0;
                        }
                    }
                }
                
                genSet.add(new GLn_PrimeField(rep, q));
            }
        }
        
        return genSet;
    }
    
    
    /**
     * Returns a pair-generating set for the Symplectic Group on a finite 
     * dimensional vector space over a prime field.
     * 
     * <p>
     * Reference:
     * </p>
     * <p>
     * &#160 &#160 [T] D. E. Taylor, "Pairs of Generators for Matrix Groups. I".
     * </p>
     * 
     * @param name The String "Pair" is the only choice offered for this group.
     * @param m A positive <code>int</code> value for the dimension.
     * @param q Any <code>short</code> integer prime.
     * @return A pair-generating set for the group Sp_2m(F_q), as a Set&ltGLn_PrimeField&gt.
     */
    public static Set<GLn_PrimeField> getSp_2mGeneratingSet(String name, int m, short q) {

        Set<GLn_PrimeField> genSet = new HashSet<GLn_PrimeField>();

        if (Arithmetic.isPrime(q) && m > 1) {
            short dim=(short) (2*m);
            
            if (name.equals("Pair")) {

                int[][] rep1 = new int[dim][dim];
                int[][] rep2 = new int[dim][dim];
                
                
                for (int i=0; i<dim; i++) {
                    for (int j=0; j<dim; j++) {
                        if (i==j) 
                            rep1[i][j] = 1;
                        else rep1[i][j] = 0;
                    }
                }
                
                for (int i=0; i<dim; i++) {
                    for (int j=0; j<dim; j++) {
                        if ( ((i==j+1) && (i<m)) || ((j==i+1) && (i>=m)) )
                            rep2[i][j] = 1;
                        else rep2[i][j] = 0;
                    }
                }
                
                if (q!=2) {
                    int xi = Arithmetic.getMultiplicativeGenerator(q);
                    int xiInv = Arithmetic.findInverse(xi, q);
                    rep1[0][0] = xi;
                    rep1[dim-1][dim-1] = xiInv;
                    rep2[0][0] = 1;
                    rep2[0][m] = 1;
                    rep2[dim-2][m-1] = 1;
                    rep2[dim-1][m-1] = -1;
                    
                } else {
                    if (m==2) {
                        for (int i=0; i<dim; i++) {
                            for (int j=0; j<dim; j++) {
                                if( ((j==0)&&(i==2)) || ((j==1)&&(i<2)) || ((j==2)&&(i!=0)&&(i!=3)) ){
                                    rep1[i][j] = 0;
                                } else
                                    rep1[i][j] = 1;
                            }
                        }
                        rep2[3][1]=1;
                        rep2[0][2]=1;
                    } else {
                        rep1[0][m-1] = 1;
                        rep1[0][dim-1] = 1;
                        rep1[m][dim-1] = 1;
                        rep2[0][m] = 1;
                        rep2[dim-1][m-1] = 1;
                    }
                }
                GLn_PrimeField a_n = new GLn_PrimeField(rep1, q);
                GLn_PrimeField b_n = new GLn_PrimeField(rep2, q);

                genSet.add(b_n);
                genSet.add(a_n);
                genSet.add(b_n.getInverse());
                genSet.add(a_n.getInverse());
            }
        }
        
        return genSet;
    }
    
    /**
     * Returns a pair-generating set for the group PGL2(F_q), where q is a 
     * <code>short</code> integer prime.
     * 
     * <p>
     * Reference:
     * </p>
     * <p>
     * &#160 &#160 [T] D. E. Taylor, "Pairs of Generators for Matrix Groups. I".
     * </p>
     *
     * @param name The String "Pair" is the only choice offered for this group.
     * @param q Any <code>short</code> integer prime.
     * @return A pair-generating set for the group PGL2(F_q), as a Set&ltPGL2_PrimeField&gt.
     */
    public static Set<PGL2_PrimeField> getPGL2GenSet(String name, short q) {
        
        Set<GL2_PrimeField> glnSet = getGL2GenSet(name, q);
        Set<PGL2_PrimeField> genSet = new HashSet<PGL2_PrimeField>(glnSet.size(), 1);
        for (GL2_PrimeField g : glnSet) {
            genSet.add(new PGL2_PrimeField(g));
        }
        return genSet;
    }
    
    /**
     * Returns a generating set for the group PSL2(F_q), where q is a 
     * <code>short</code> integer prime.
     * 
     * <p>
     * The choices offered by this method are the following:</br> 
     * "Standard", "L1", "L2", "L3", or "Pair"
     * </p>
     * The generating sets L1, L2, and L3 are those of the Lubotzky's 1-2-3 
     * problem (see [L]).
     * </p>
     * <p>
     * The definition of the Pair generating set can be found in [T].
     * </p>
     * <p>
     * References:
     * </p>
     * <p>
     * &#160 &#160 [L] A. Lubotzky, "Expanders in Pure and Applied Mathematics", 
     * Bulletin of the American Mathematical Society, 2012.</br>
     * &#160 &#160 [T] D. E. Taylor, "Pairs of Generators for Matrix Groups. I".
     * </p>
     * 
     * @param name One of the following Strings: "Standard", "L1", "L2", "L3", or "Pair".
     * @param q A <code>short</code> integer prime.
     * @return A generating set for the group PSL2(F_q), as a Set&ltPGL2_PrimeField&gt.
     */
    public static Set<PGL2_PrimeField> getPSL2GenSet(String name, short q) {
        
        Set<GL2_PrimeField> glnSet = getSL2GenSet(name, q);
        Set<PGL2_PrimeField> genSet = new HashSet<PGL2_PrimeField>(glnSet.size(), 1);
        for (GL2_PrimeField g : glnSet) {
            genSet.add(new PGL2_PrimeField(g));
        }
        return genSet;
    }
    
    /**
     * Returns a pair-generating set for the Projective General Linear Group on a finite 
     * dimensional vector space over a prime field.
     * 
     * <p>
     * Reference:
     * </p>
     * <p>
     * &#160 &#160 [T] D. E. Taylor, "Pairs of Generators for Matrix Groups. I".
     * </p>
     * 
     * @param name The String "Pair" is the only choice offered for this group.
     * @param n A positive <code>int</code> value for the dimension.
     * @param q Any <code>short</code> integer prime.
     * @return A pair-generating set for the group PGLn(F_q), as a Set&ltPGLn_PrimeField&gt.
     */
    
    public static Set<PGLn_PrimeField> getPGLnGeneratingSet(String name, int n, short q) {
        
        Set<GLn_PrimeField> glnSet = getGLnGeneratingSet(name, n, q);
        Set<PGLn_PrimeField> genSet = new HashSet<PGLn_PrimeField>(glnSet.size(), 1);
        for (GLn_PrimeField g : glnSet) {
            genSet.add(new PGLn_PrimeField(g));
        }
        return genSet;
    }
    
    /**
     * Returns a pair-generating set for the Projective Special Linear Group on 
     * a finite dimensional vector space over a prime field.
     * 
     * <p>
     * The definition of the Pair generating set can be found in [T].
     * </p>
     * <p>
     * Reference:
     * </p>
     * <p>
     * &#160 &#160 [T] D. E. Taylor, "Pairs of Generators for Matrix Groups. I".
     * </p>
     * 
     * @param name One of the Strings "Standard" or "Pair".
     * @param n A positive <code>int</code> value for the dimension.
     * @param q Any <code>short</code> integer prime.
     * 
     * @return A pair-generating set for the group PSLn(F_q), as a Set&ltPGLn_PrimeField&gt.
     */
    public static Set<PGLn_PrimeField> getPSLnGeneratingSet(String name, int n, short q) {
        
        Set<GLn_PrimeField> glnSet = getSLnGeneratingSet(name, n, q);
        Set<PGLn_PrimeField> genSet = new HashSet<PGLn_PrimeField>(glnSet.size(), 1);
        for (GLn_PrimeField g : glnSet) {
            genSet.add(new PGLn_PrimeField(g));
        }
        return genSet;
    }
    
    /**
     * Returns a generating set for the Projective Symplectic Similitude Group 
     * on a finite dimensional vector space over a prime field.
     * 
     * <p>
     * The returned generating set is a triplet consisting of the 
     * pair-generators defined in [T] for the Symplectic Group, together with
     * one diagonal similitude matrix.
     * </p>
     * 
     * <p>
     * Reference:
     * </p>
     * <p>
     * &#160 &#160 [T] D. E. Taylor, "Pairs of Generators for Matrix Groups. I".
     * </p>
     * 
     * @param name The String "Triplet" is the only choice offered for this group.
     * @param m A positive <code>int</code> value for the dimension.
     * @param q Any <code>short</code> integer prime.
     * @return A generating set for the group PGSp_2m(F_q), as a Set&ltGLn_PrimeField&gt.
     */
    public static Set<PGLn_PrimeField> getPGSp_2mGeneratingSet(String name, int m, short q) {
        
        Set<GLn_PrimeField> glnSet = getGSp_2mGeneratingSet(name, m, q);
        Set<PGLn_PrimeField> genSet = new HashSet<PGLn_PrimeField>(glnSet.size(), 1);
        for (GLn_PrimeField g : glnSet) {
            genSet.add(new PGLn_PrimeField(g));
        }
        return genSet;
    }
    
    /**
     * Returns a pair-generating set for the Projective Symplectic Group on a finite 
     * dimensional vector space over a prime field.
     * 
     * <p>
     * Reference:
     * </p>
     * <p>
     * &#160 &#160 [T] D. E. Taylor, "Pairs of Generators for Matrix Groups. I".
     * </p>
     * 
     * @param name The String "Pair" is the only choice offered for this group.
     * @param m A positive <code>int</code> value for the dimension.
     * @param q Any <code>short</code> integer prime.
     * @return A pair-generating set for the group PSp_2m(F_q), as a Set&ltGLn_PrimeField&gt.
     */
    public static Set<PGLn_PrimeField> getPSp_2mGeneratingSet(String name, int m, short q) {
        
        Set<GLn_PrimeField> glnSet = getSp_2mGeneratingSet(name, m, q);
        Set<PGLn_PrimeField> genSet = new HashSet<PGLn_PrimeField>(glnSet.size(), 1);
        for (GLn_PrimeField g : glnSet) {
            genSet.add(new PGLn_PrimeField(g));
        }
        return genSet;
    }
    
    /**
     * Returns a generating set for the Symmetric Group on n letters.
     * 
     * <p>
     * The choices offered by this method are the following:</br>  
     * &#160(i) A pair-generating set, of which there are two 
     * choices, specified by the Strings "S_n Pair-1" and "S_n Pair-2".</br>
     * (ii) A generating set of size n-1 consisting of transpositions, of which there are two 
     * choices, specified by the Strings "S_n Transp-1" and "S_n Transp-2".
     * </p>
     * The definitions are as follows:</br>
     * &#160 &#160 Pair-1: (0 1), (0 1 ... n-1)</br>
     * &#160 &#160 Pair-2: (0 1), (1 2 ... n-1)</br>
     * &#160 &#160 Transp-1: (0 i) for i=1,2,...,n</br>
     * &#160 &#160 Transp-2: (i i+1) for i=0,1,...,n-2</br>
     * 
     * <p>
     * Reference:
     * </p>
     * <p>
     * &#160 &#160 [C] K. Conrad, "Generating Sets" <a
     * href="http://www.math.uconn.edu/~kconrad/blurbs/grouptheory/genset.pdf">UConn
     * link</a> </br>
     * </p> 
     * 
     * @param name One of the following Strings: "S_n - Pair 1", "S_n - Pair 2", 
     * "S_n Transp-1", or "S_n Transp-2".
     * @param n A positive <code>int</code> value for number of letters.
     * @return A generating set for the Symmetric Group on n letters, as a Set&ltSymmetricGroup&gt.
     */
    public static Set<SymmetricGroup> getSymmetricGroupGenSet(String name, int n) {
        
        Set<SymmetricGroup> genSet = new HashSet<SymmetricGroup>();
        
        if (n>1) {
            
            if (name.equals("S_n - Pair 1") || name.equals("S_n - Pair 2")) {
                int[] perm = new int[n];
                perm[0]=1;
                perm[1]=0;
                for (int i=2; i<n; i++) {
                    perm[i] = i;
                }
                SymmetricGroup elt = new SymmetricGroup(perm);
                genSet.add(elt);
                genSet.add(elt.getInverse());
                
                
                if (name.equals("S_n - Pair 1")) {
                    perm[0] = 1;
                    perm[n-1] = 0;
                } else {
                    perm[0] = 0;
                    perm[n-1] = 1;
                }
                
                for (int i=1; i<n-1; i++) {
                    perm[i] = i+1;
                }
                
                elt = new SymmetricGroup(perm);
                genSet.add(elt);
                genSet.add(elt.getInverse());
            } 
            else if (name.equals("S_n - Transp 1") || name.equals("S_n - Transp 2")) {
                int[] perm = new int[n];
                
                for (int k = 1; k < n; k++) {
                    
                    if (name.equals("S_n - Transp 1")) {
                        perm[0] = k;
                        perm[k] = 0;
                        for (int i = 1; i < n; i++) {
                            if (i != k) {
                                perm[i] = i;
                            }
                        }
                    } else if (name.equals("S_n - Transp 2")){
                        perm[k - 1] = k;
                        perm[k] = k - 1;
                        for (int i = 0; i < n; i++) {
                            if (i != k && i != k - 1) {
                                perm[i] = i;
                            }
                        }
                    }
                    SymmetricGroup elt = new SymmetricGroup(perm);
                    genSet.add(elt);
                    genSet.add(elt.getInverse());
                }
           }
        }
        return genSet;
    }
    
    
   
    
    
    /**
     * Returns a generating set for the Alternating Group on n letters.
     * 
     * <p>
     * The choices offered by this method are the following:</br>  
     * &#160(i) A pair-generating set, specified by the String "A_n Pair".</br>
     * (ii) A generating set of size n-2 consisting of 3-cycles, of which there are two 
     * choices, specified by the Strings "A_n 3Cycle-1" and "A_n 3Cycle-2".
     * </p>
     * 
     * The definitions are as follows:</br>
     * &#160&#160&#160 Pair: (0 1 2), (0 1 ... n-1) &#160&#160 n&gt=4 odd</br>
     * &#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160 (0 1 2), (1 2 ... n-1) &#160&#160 n&gt=4 even</br>
     * &#160&#160&#160 3Cycle-1: (0 1 k) for k=2,3,...,n-1 </br>
     * &#160&#160&#160 3Cycle-2: (k k+1 k+2) for k=0,1,...,n-3 </br>
     * 
     * <p>
     * Reference:
     * </p>
     * <p>
     * &#160 &#160 [C] K. Conrad, "Generating Sets" <a
     * href="http://www.math.uconn.edu/~kconrad/blurbs/grouptheory/genset.pdf">UConn
     * link</a> </br>
     * </p>
     * 
     * @param name One of the Strings "A_n Pair", "A_n 3Cycle-1", or
     * "A_n 3Cycle-2".
     * @param n A positive <code>int</code> value for number of letters.
     * @return A generating set for the Alternating Group on n letters, as a Set&ltSymmetricGroup&gt.
     */
    public static Set<SymmetricGroup> getAlternatingGroupGenSet(String name, int n) {
        
        Set<SymmetricGroup> genSet = new HashSet<SymmetricGroup>();
        
        if (n>2) {
            
            if (name.equals("A_n - Pair")) {
                int[] perm = new int[n];
                perm[0]=1;
                perm[1]=2;
                perm[2]=0;
                for (int i=3; i<n; i++) {
                    perm[i] = i;
                }
                SymmetricGroup elt = new SymmetricGroup(perm);
                genSet.add(elt);
                genSet.add(elt.getInverse());
                
                
                if (n%2==1) {
                    perm[0] = 1;
                    perm[n-1] = 0;
                } else {
                    perm[0] = 0;
                    perm[n-1] = 1;
                }
                
                for (int i=1; i<n-1; i++) {
                    perm[i] = i+1;
                }
                
                elt = new SymmetricGroup(perm);
                genSet.add(elt);
                genSet.add(elt.getInverse());
            } 
            else if (name.equals("A_n - 3Cycle 1") || name.equals("A_n - 3Cycle 2")) {
                int[] perm = new int[n];
                
                for (int k = 2; k < n; k++) {
                    
                    if (name.equals("A_n - 3Cycle 1")) {
                        perm[0] = 1;
                        perm[1] = k;
                        perm[k] = 0;
                        for (int i = 2; i < n; i++) {
                            if (i != k) {
                                perm[i] = i;
                            }
                        }
                    } else if (name.equals("A_n - 3Cycle 2")){
                        perm[k - 2] = k-1;
                        perm[k - 1] = k;
                        perm[k] = k - 2;
                        for (int i = 0; i < n; i++) {
                            if (i != k && i != k - 1 && i != k - 2) {
                                perm[i] = i;
                            }
                        }
                    }
                    SymmetricGroup elt = new SymmetricGroup(perm);
                    genSet.add(elt);
                    genSet.add(elt.getInverse());
                }
           }
        }
        return genSet;
    }
    
    
    
    /**
     * Returns a generating set for one of the following sporadic finite simple 
     * groups, each represented as a permutation group:</br>
     * &#160(i) the first three Mathieu groups M11, M12, M22 (the subscript indicates the number of letters in the permutation representation)</br>
     * (ii) the second Janko group J2.
     * 
     * <p>
     * The definitions are as follows:</br>
     * &#160&#160 M11-Pair 1: (0 1 ... 10), (2 6 10 7)(3 9 4 5) &#160&#160 (Ref: Wikepedia)</br>
     * &#160&#160 M11-Pair 2: (1 9)(3 10)(4 6)(7 8), (0 3 2 7)(1 4 5 8) &#160&#160 (Ref: Atlas)</br>
     * &#160&#160 M11-5Tuple: A 5-tuple of generators, each of which is a product of four transpositions (Ref: Taslaman)</br>
     * &#160&#160 M12-Pair: (0 3)(2 9)(4 10)(5 11), (0 7 8)(1 2 3)(4 11 10)(5 9 6) &#160&#160 (Ref: Atlas)</br>
     * &#160&#160 M12-Pair 2: (1 2)(4 5)(7 8)(10 11) (0 1 3)(2 4 6)(5 7 9)(8 10 11) &#160&#160 (Ref: Atlas)</br>
     * &#160&#160 M12-Triplet: A pair generating set for PSL2(F11), represented 
     * as a permutation group on 12 elements via the natural projective action, </br>
     * &#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160
     * &#160&#160&#160&#160&#160&#160&#160
     * together with the element (1 9)(2 3)(4 8)(5 6) (Ref: Wikepedia)</br>
     * &#160&#160 M22-Pair: (0 12)(1 7)(2 15)(3 11)(5 21)(6 16)(8 9)(10 13), </br>
     * &#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160&#160
     * &#160&#160&#160&#160
     * (0 21 2 20)(1 17 3 12)(4 11)(5 10 6 14)(7 13 19 9)(16 18) &#160&#160 (Ref: Atlas)</br>
     * &#160&#160 J2-Pair: generators for a permutation representation on 100 letters &#160&#160 (Ref: Atlas)</br>
     * </p>
     * 
     * <p>
     * References:
     * </p>
     * <p>
     * &#160&#160 L. Taslaman, The Mathieu Groups, 2009.</br>
     * &#160&#160 <a href="http://brauer.maths.qmul.ac.uk/Atlas/">ATLAS of Finite Group Representations</a>
     * </p>
     * 
     * @param name One of the following Strings: "M11-Pair 1", "M11-Pair 2", 
     * "M11-5Tuple", "M12-Pair", "M12-Pair 2", "M12-Triplet", "M22-Pair", or 
     * "J2-Pair".
     * 
     * @return A Set&ltSymmetricGroup&gt which is a generating set for the 
     * specified group.
     */
    public static Set<SymmetricGroup> getSporadicGroupGenSet(String name) {

        Set<SymmetricGroup> genSet = new HashSet<SymmetricGroup>();

        if (name.equals("M11 - Pair 1")) { //From Wikipedia <(1, 2, ..., 11), (3, 7, 11, 8)(4, 10, 5, 6)>
            
            SymmetricGroup elt = SymmetricGroup.createCycle(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 11);
            genSet.add(elt);
            genSet.add(elt.getInverse());
            
            int[][] cycles={{2, 6, 10, 7}, {3, 9, 4, 5}};
            elt = SymmetricGroup.cycleDec2Perm(cycles, 11);
            genSet.add(elt);
            genSet.add(elt.getInverse());
            
        } else if (name.equals("M11 - Pair 2")) { //From Atlas <(2, 10)(4, 11)(5, 7)(8, 9), (1, 4, 3, 8)(2, 5, 6, 9)>
            
            SymmetricGroup elt = new SymmetricGroup(new int[]{0, 9, 2, 10, 6, 5, 4, 8, 7, 1, 3});
            genSet.add(elt);
            genSet.add(elt.getInverse());
            int[][] cycles={{0, 3, 2, 7}, {1, 4, 5, 8}};
            elt = SymmetricGroup.cycleDec2Perm(cycles, 11);
            genSet.add(elt);
            genSet.add(elt.getInverse());
        } else if (name.equals("M11 - 5Tuple")) { //From Taslaman <x1, x2, x3, x4, x5>,    with    x1 = (1 2)(3 4)(5 6)(7 8)
            //x2 = (1 2)(5 7)(6 8)(9 10)  x3 = (1 3)(2 4)(5 6)(9 10) x4 = (1 3)(5 9)(6 10)(7 8)  x5 = (1 6)(2 5)(7 8)(9 11)
            
            SymmetricGroup elt;
            elt = new SymmetricGroup(new int[]{1, 0, 3, 2, 5, 4, 7, 6, 8, 9, 10});
            genSet.add(elt);
            genSet.add(elt.getInverse());
            elt = new SymmetricGroup(new int[]{1, 0, 2, 3, 6, 7, 4, 5, 9, 8, 10});
            genSet.add(elt);
            genSet.add(elt.getInverse());
            elt = new SymmetricGroup(new int[]{2, 3, 0, 1, 5, 4, 6, 7, 9, 8, 10});
            genSet.add(elt);
            genSet.add(elt.getInverse());
            elt = new SymmetricGroup(new int[]{2, 1, 0, 3, 8, 9, 7, 6, 4, 5, 10});
            genSet.add(elt);
            genSet.add(elt.getInverse());
            elt = new SymmetricGroup(new int[]{5, 4, 2, 3, 1, 0, 7, 6, 10, 9, 8});
            genSet.add(elt);
            genSet.add(elt.getInverse());
        } else if (name.equals("M12 - Pair")) { //From Atlas 
            
            SymmetricGroup elt = new SymmetricGroup(new int[]{3, 1, 9, 0, 10, 11, 6, 7, 8, 2, 4, 5});
            genSet.add(elt);
            genSet.add(elt.getInverse());
            elt = SymmetricGroup.createCycle(new int[]{0, 7, 8}, 12);
            elt = elt.rightProductBy(SymmetricGroup.createCycle(new int[]{1, 2, 3}, 12));
            elt = elt.rightProductBy(SymmetricGroup.createCycle(new int[]{4, 11, 10}, 12));
            elt = elt.rightProductBy(SymmetricGroup.createCycle(new int[]{5, 9, 6}, 12));
            genSet.add(elt);
            genSet.add(elt.getInverse());
            
        } else if (name.equals("M12 - Pair 2")) { //From Atlas
            
            SymmetricGroup elt = new SymmetricGroup(new int[]{0, 2, 1, 3, 5, 4, 6, 8, 7, 9, 11, 10});
            genSet.add(elt);
            genSet.add(elt.getInverse());
            elt = SymmetricGroup.createCycle(new int[]{0, 1, 3}, 12);
            elt = elt.rightProductBy(SymmetricGroup.createCycle(new int[]{2, 4, 6}, 12));
            elt = elt.rightProductBy(SymmetricGroup.createCycle(new int[]{5, 7, 9}, 12));
            elt = elt.rightProductBy(SymmetricGroup.createCycle(new int[]{8, 10, 11}, 12));
            genSet.add(elt);
            genSet.add(elt.getInverse());
            
        } else if (name.equals("M12 - Triplet")) { //From Wikipedia <PSL2(F11), (2, 10)(3, 4)(5, 9)(6, 7)>

            Set<GL2_PrimeField> pairGL2gens = getSL2GenSet("Pair", (short) 11);

            for (GL2_PrimeField g : pairGL2gens) {
                genSet.add(new PGL2_PrimeField(g).getPermutationRepr());
            }

            SymmetricGroup elt = new SymmetricGroup(new int[]{0, 9, 3, 2, 8, 6, 5, 7, 4, 1, 10, 11});
            genSet.add(elt);
            genSet.add(elt.getInverse());
        } else if (name.equals("M22 - Pair")) { //From Atlas
            SymmetricGroup elt = new SymmetricGroup(new int[]{12, 7, 15, 11, 4, 21, 16, 1, 9, 8, 13, 3, 0, 10, 14, 2, 6, 17, 18, 19, 20, 5});
            genSet.add(elt);
            genSet.add(elt.getInverse());
            elt = new SymmetricGroup(new int[]{21, 17, 20, 12, 11, 10, 14, 13, 8, 7, 6, 4, 1, 19, 5, 15, 18, 3, 16, 9, 0, 2});
            genSet.add(elt);
            genSet.add(elt.getInverse());
        } else if (name.equals("J2 - Pair")) { //From Atlas
            
            int[][] cycles1 = {{0, 83}, {1, 19}, {2, 47}, {3, 55}, {4, 81}, 
                {5, 66}, {6, 54}, {7, 40}, {8, 34}, {9, 39}, {10, 77}, {11, 99},
                {12, 48}, {13, 36}, {14, 93}, {15, 75}, {16, 18}, {17, 43}, 
                {20, 33}, {21, 84}, {22, 91}, {23, 56}, {24, 74}, {25, 27}, 
                {26, 63}, {28, 89}, {29, 96}, {30, 37}, {31, 67}, {32, 68}, 
                {35, 52}, {38, 60}, {41, 72}, {42, 90}, {44, 85}, {45, 80}, 
                {46, 88}, {49, 92}, {50, 95}, {51, 71}, {53, 73}, {57, 98}, 
                {58, 94}, {59, 62}, {61, 82}, {64, 69}, {65, 87}, {70, 86}, 
                {76, 97}, {78, 79}};

            SymmetricGroup elt = SymmetricGroup.cycleDec2Perm(cycles1, 100);

            genSet.add(elt);
            genSet.add(elt.getInverse());
            
            int[][] cycles2 = {{0,79,21}, {1,8,10}, {2,52,86}, {3,22,77},
                    {4,50,17}, {5,36,23}, {7,26,59}, {9,61,46}, {11,64,30}, 
                    {12,63,18}, {13,60,51}, {14,97,24}, {15,72,31}, {16,38,32}, 
                    {19,96,57}, {20,95,66}, {25,92,98}, {27,56,34}, {28,70,54}, 
                    {29,68,44}, {33,85,81}, {37,58,93}, {39,42,90}, {41,67,43}, 
                    {45,84,88}, {47,75,89}, {48,91,76}, {49,65,87}, {53,94,55}, 
                    {62,73,71}, {69,80,74}, {78,99,82}};
            
            elt = SymmetricGroup.cycleDec2Perm(cycles2, 100);
            genSet.add(elt);
            genSet.add(elt.getInverse());
        }

        return genSet;
    }
    
    /**
     * Returns a generating set for the first Janko group J1 (which is one of 
     * the sporadic finite simple groups), represented
     * as a subgroup of GL_7(F_11).
     * 
     * <p>
     * There are two choices offered by this method, both of which return
     * a pair generating set.  They are specified by the Strings "J1-Pair 1", or
     * "J1-Pair 2".
     * </p>
     * 
     * <p>
     * J1-Pair 1: Ref. [J]</br>
     * J1-Pair 2: Ref. Atlas
     * </p>
     * 
     * <p>
     * References:
     * </p>
     * <p>
     * &#160&#160 [J] Z Janko, "A new finite simple group with abelian Sylow subgroups", Proc. Nat. Acad. Sci. USA 53 (1965) 657-658</br>
     * &#160&#160 <a href="http://brauer.maths.qmul.ac.uk/Atlas/">ATLAS of Finite Group Representations</a> </br>
     * </p>
     * 
     * @param name One of the Strings "J1-Pair 1" or "J1-Pair 2".
     * @return A pair-generating set for the first Janko group J1, represented
     * as a subgroup of GL_7(F_11).
     */
    public static Set<GLn_PrimeField> getJanko1GenSet(String name) {
        Set<GLn_PrimeField> genSet = new HashSet<GLn_PrimeField>();
        GLn_PrimeField elt;
        if (name.equals("J1 - Pair 1")) { //From Wikipedia
            int[][] rep = new int[7][7];
            rep[0] = new int[]{0, 1, 0, 0, 0, 0, 0};
            rep[1] = new int[]{0, 0, 1, 0, 0, 0, 0};
            rep[2] = new int[]{0, 0, 0, 1, 0, 0, 0};
            rep[3] = new int[]{0, 0, 0, 0, 1, 0, 0};
            rep[4] = new int[]{0, 0, 0, 0, 0, 1, 0};
            rep[5] = new int[]{0, 0, 0, 0, 0, 0, 1};
            rep[6] = new int[]{1, 0, 0, 0, 0, 0, 0};
            elt = new GLn_PrimeField(rep, (short) 11);
            genSet.add(elt);
            genSet.add(elt.getInverse());
            rep[0] = new int[]{-3, 2, -1, -1, -3, -1, -3};
            rep[1] = new int[]{-2, 1, 1, 3, 1, 3, 3};
            rep[2] = new int[]{-1, -1, -3, -1, -3, -3, 2};
            rep[3] = new int[]{-1, -3, -1, -3, -3, 2, -1};
            rep[4] = new int[]{-3, -1, -3, -3, 2, -1, -1};
            rep[5] = new int[]{1, 3, 3, -2, 1, 1, 3};
            rep[6] = new int[]{3, 3, -2, 1, 1, 3, 1};
            elt = new GLn_PrimeField(rep, (short) 11);
            genSet.add(elt);
            genSet.add(elt.getInverse());
        } 
//        else if (name.equals("J1-Pair 2")) { //From Atlas
//            int[][] rep = new int[7][7];
//            rep[0] = new int[]{0,9,10,10,5,9,10};
//            rep[1] = new int[]{9,0,6,2,7,7,2};
//            rep[2] = new int[]{10,6,1,4,7,3,1};
//            rep[3] = new int[]{10,2,4,5,6,3,0};
//            rep[4] = new int[]{5,7,7,6,4,10,6};
//            rep[5] = new int[]{9,7,3,3,10,6,7};
//            rep[6] = new int[]{10,2,1,0,6,7,0};
//            elt = new GLn_PrimeField(rep, (short) 11);
//            genSet.add(elt);
//            genSet.add(elt.getInverse());
//            rep[0] = new int[]{8,9,6,4,8,4,1};
//            rep[1] = new int[]{5,2,5,0,7,7,4};
//            rep[2] = new int[]{8,5,4,10,6,1,6};
//            rep[3] = new int[]{1,7,7,3,6,1,10};
//            rep[4] = new int[]{9,2,9,1,5,9,7};
//            rep[5] = new int[]{7,10,0,2,1,7,8};
//            rep[6] = new int[]{7,2,7,7,10,2,10};
//            elt = new GLn_PrimeField(rep, (short) 11);
//            genSet.add(elt);
//            genSet.add(elt.getInverse());
//        }
        
        return genSet;
    }
    
    //"M11-Pair 1" "M11-Pair 2" "M12-Pair 1" "M12-Pair 2" "M22-Pair" "J1-Pair 1" "J1-Pair 2" "J2-Pair"
    
}
