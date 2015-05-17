package utilities;

import basic_operations.Arithmetic;
import fastgroups.GLnByteField;
import fastgroups.PGLnByteField;
import finitefields.ByteField;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * Reference: D.E. Taylor, "Pair Generators for Matrix Groups I".
 *
 * @author pdokos
 */
public class GenSetCatalog {

    public static Set<GLnByteField> getSuzuki8() {
        Set<GLnByteField> genSet = new HashSet<GLnByteField>();
        ByteField f = ByteField.getF8();
        ByteField.Element[][] rep1 = new ByteField.Element[4][4];
        ByteField.Element[][] rep2 = new ByteField.Element[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i + j == 1 || i + j == 5) {
                    rep1[i][j] = f.one();
                } else {
                    rep1[i][j] = f.zero();
                }
            }
        }

        ByteField.Element xi = f.getMultiplicativeGenerator();

        for (int j = 0; j < 4; j++) {
            rep2[0][j] = j == 0 ? f.one() : f.zero();
            rep2[1][j] = j == 2 ? f.one() : f.zero();
        }
        rep2[2][0] = f.one();
        rep2[2][1] = f.getElement(f.pow(xi.getIndex(), 6));
        rep2[2][2] = f.zero();
        rep2[2][3] = f.getElement(f.pow(xi.getIndex(), 5));
        rep2[3][0] = f.getElement(f.pow(xi.getIndex(), 6));
        rep2[3][1] = f.getElement(f.pow(xi.getIndex(), 4));
        rep2[3][2] = f.getElement(f.pow(xi.getIndex(), 4));
        rep2[3][3] = f.one();

        GLnByteField a_n = new GLnByteField(f, rep1);
        GLnByteField b_n = new GLnByteField(f, rep2);

        genSet.add(b_n);
        genSet.add(a_n);
        genSet.add(b_n.getInverse());
        genSet.add(a_n.getInverse());
        return genSet;
    }
   
    
    public static Set<GLnByteField> getG2_3() {
        Set<GLnByteField> genSet = new HashSet<GLnByteField>();
        ByteField f = ByteField.getPrimeField((byte) 3);
        
        GLnByteField elt1;
        GLnByteField elt2;
        byte[][] rep1 = new byte[7][7];
        byte[][] rep2 = new byte[7][7];
        rep1[0] = new byte[]{0, 1, 0, 0, 0, 0, 0};
        rep1[1] = new byte[]{1, 0, 0, 0, 0, 0, 0};
        rep1[2] = new byte[]{0, 0, 0, 1, 0, 0, 0};
        rep1[3] = new byte[]{0, 0, 1, 0, 0, 0, 0};
        rep1[4] = new byte[]{0, 0, 0, 0, 0, 1, 0};
        rep1[5] = new byte[]{0, 0, 0, 0, 1, 0, 0};
        rep1[6] = new byte[]{1, 1, 1, 1, 2, 2, 2};
        elt1 = new GLnByteField(f, rep1);
        genSet.add(elt1);
        GLnByteField elt1Inv = elt1.getInverse();
        genSet.add(elt1Inv);
        rep2[0] = new byte[]{1, 0, 0, 0, 0, 0, 0};
        rep2[1] = new byte[]{0, 0, 1, 0, 0, 0, 0};
        rep2[2] = new byte[]{2, 2, 2, 0, 0, 0, 0};
        rep2[3] = new byte[]{0, 0, 0, 0, 1, 0, 0};
        rep2[4] = new byte[]{2, 0, 0, 2, 2, 0, 0};
        rep2[5] = new byte[]{0, 0, 0, 0, 0, 0, 1};
        rep2[6] = new byte[]{2, 0, 0, 0, 0, 2, 2};
        elt2 = new GLnByteField(f, rep2);
        genSet.add(elt2);
        genSet.add(elt2.getInverse());
        return genSet;
    }

    //From wikipedia
    public static Set<GLnByteField> getJanko1GenSet() {
        Set<GLnByteField> genSet = new HashSet<GLnByteField>();
        ByteField f = ByteField.getPrimeField((byte) 11);

        GLnByteField elt1;
        GLnByteField elt2;
        byte[][] rep1 = new byte[7][7];
        byte[][] rep2 = new byte[7][7];
        rep1[0] = new byte[]{0, 1, 0, 0, 0, 0, 0};
        rep1[1] = new byte[]{0, 0, 1, 0, 0, 0, 0};
        rep1[2] = new byte[]{0, 0, 0, 1, 0, 0, 0};
        rep1[3] = new byte[]{0, 0, 0, 0, 1, 0, 0};
        rep1[4] = new byte[]{0, 0, 0, 0, 0, 1, 0};
        rep1[5] = new byte[]{0, 0, 0, 0, 0, 0, 1};
        rep1[6] = new byte[]{1, 0, 0, 0, 0, 0, 0};

        elt1 = new GLnByteField(f, rep1);
        genSet.add(elt1);
        GLnByteField elt1Inv = elt1.getInverse();
        genSet.add(elt1Inv);
        rep2[0] = new byte[]{8, 2, 10, 10, 8, 10, 8};
        rep2[1] = new byte[]{9, 1, 1, 3, 1, 3, 3};
        rep2[2] = new byte[]{10, 10, 8, 10, 8, 8, 2};
        rep2[3] = new byte[]{10, 8, 10, 8, 8, 2, 10};
        rep2[4] = new byte[]{8, 10, 8, 8, 2, 10, 10};
        rep2[5] = new byte[]{1, 3, 3, 9, 1, 1, 3};
        rep2[6] = new byte[]{3, 3, 9, 1, 1, 3, 1};

        elt2 = new GLnByteField(f, rep2);
        genSet.add(elt2);
        GLnByteField elt2Inv = elt2.getInverse();
        genSet.add(elt2.getInverse());
        return genSet;
    }

    public static Set<GLnByteField> getGLnGeneratingSet(int n, ByteField f) {

        Set<GLnByteField> genSet = new HashSet<GLnByteField>();

        if (n > 1) {

            ByteField.Element[][] rep1 = new ByteField.Element[n][n];
            ByteField.Element[][] rep2 = new ByteField.Element[n][n];

            if (f.getCharacteristic() == 2 && f.getDimension() == 1) {
                return null;//getSLnGeneratingSet(n, f);
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (j == i - 1) {
                        rep1[i][j] = f.one().negative();
                    } else {
                        rep1[i][j] = f.zero();
                    }
                }
            }
            rep1[0][0] = f.one().negative();
            rep1[0][n - 1] = f.one();

            ByteField.Element xi = f.getMultiplicativeGenerator();

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (j == i) {
                        if (i > 0) {
                            rep2[i][j] = f.one();
                        } else {
                            rep2[i][j] = xi;
                        }
                    } else {
                        rep2[i][j] = f.zero();
                    }
                }
            }

            GLnByteField a_n = new GLnByteField(f, rep1);
            GLnByteField b_n = new GLnByteField(f, rep2);

            genSet.add(b_n);
            genSet.add(a_n);
            genSet.add(b_n.getInverse());
            genSet.add(a_n.getInverse());

        }
        return genSet;
    }

    public static Set<GLnByteField> getSLnGeneratingSet(int n, ByteField f) {

        Set<GLnByteField> genSet = new HashSet<GLnByteField>();

        if (n > 1) {
            byte[][] rep1 = new byte[n][n];
            byte[][] rep2 = new byte[n][n];

            if (f.getCharacteristic() > 3 || f.getDimension() > 1) {

                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (j == i - 1) {
                            rep1[i][j] = f.one().negative().getIndex();
                        } else {
                            rep1[i][j] = f.zero().getIndex();
                        }
                    }
                }
                rep1[0][0] = f.one().negative().getIndex();
                rep1[0][n - 1] = f.one().getIndex();

                ByteField.Element xi = f.getMultiplicativeGenerator();
                ByteField.Element xiInv = xi.inverse();
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (j == i) {
                            if (i > 1) {
                                rep2[i][j] = f.one().getIndex();
                            } else if (i == 0) {
                                rep2[i][j] = xi.getIndex();
                            } else {
                                rep2[i][j] = xiInv.getIndex();
                            }
                        } else {
                            rep2[i][j] = f.zero().getIndex();
                        }
                    }
                }
            } else {

                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (j == i - 1) {
                            rep1[i][j] = f.one().negative().getIndex();
                        } else {
                            rep1[i][j] = f.zero().getIndex();
                        }
                    }
                }
                rep1[0][0] = f.zero().getIndex();
                rep1[0][n - 1] = f.one().getIndex();

                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (j == i) {
                            rep2[i][j] = f.one().getIndex();
                        } else {
                            rep2[i][j] = f.zero().getIndex();
                        }
                    }
                }
                rep2[0][1] = f.one().getIndex();
            }

            GLnByteField a_n = new GLnByteField(f, rep1);
            GLnByteField b_n = new GLnByteField(f, rep2);

            genSet.add(b_n);
            genSet.add(a_n);
            genSet.add(b_n.getInverse());
            genSet.add(a_n.getInverse());
        }
        return genSet;
    }

    public static Set<GLnByteField> getSp_2mGeneratingSet(int m, ByteField f) {

        Set<GLnByteField> genSet = new HashSet<GLnByteField>();

        if (m > 1) {
            byte dim = (byte) (2 * m);

            byte[][] rep1 = new byte[dim][dim];
            byte[][] rep2 = new byte[dim][dim];


            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    if (i == j) {
                        rep1[i][j] = f.one().getIndex();
                    } else {
                        rep1[i][j] = f.zero().getIndex();
                    }
                }
            }

            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    if (((i == j + 1) && (i < m)) || ((j == i + 1) && (i >= m))) {
                        rep2[i][j] = f.one().getIndex();
                    } else {
                        rep2[i][j] = f.zero().getIndex();
                    }
                }
            }

            if (f.getCharacteristic() != 2) {
                ByteField.Element xi = f.getMultiplicativeGenerator();
                ByteField.Element xiInv = xi.inverse();
                rep1[0][0] = xi.getIndex();
                rep1[dim - 1][dim - 1] = xiInv.getIndex();
                rep2[0][0] = f.one().getIndex();
                rep2[0][m] = f.one().getIndex();
                rep2[dim - 2][m - 1] = f.one().getIndex();
                rep2[dim - 1][m - 1] = f.one().negative().getIndex();

            } else {

                if (f.getDimension() == 1) {

                    if (m == 2) {
                        for (int i = 0; i < dim; i++) {
                            for (int j = 0; j < dim; j++) {
                                if (((j == 0) && (i == 2)) || ((j == 1) && (i < 2)) || ((j == 2) && (i != 0) && (i != 3))) {
                                    rep1[i][j] = f.zero().getIndex();
                                } else {
                                    rep1[i][j] = f.one().getIndex();
                                }
                            }
                        }
                        rep2[3][1] = f.one().getIndex();
                        rep2[0][2] = f.one().getIndex();
                    } else {
                        rep1[0][m - 1] = f.one().getIndex();
                        rep1[0][dim - 1] = f.one().getIndex();
                        rep1[m][dim - 1] = f.one().getIndex();
                        rep2[0][m] = f.one().getIndex();
                        rep2[dim - 1][m - 1] = f.one().getIndex();
                    }
                } else {

                    ByteField.Element xi = f.getMultiplicativeGenerator();
                    ByteField.Element xiInv = xi.inverse();
                    rep1[0][0] = xi.getIndex();
                    rep1[m - 1][m - 1] = xi.getIndex();
                    rep1[dim - 1][dim - 1] = xiInv.getIndex();
                    rep1[m][m] = xiInv.getIndex();

                    rep2[0][m - 2] = f.one().getIndex();
                    rep2[0][m - 1] = f.one().getIndex();
                    rep2[0][m] = f.one().getIndex();
                    rep2[m][m - 1] = f.one().getIndex();
                    rep2[dim - 1][m - 1] = f.one().getIndex();

                }
            }
            GLnByteField a_n = new GLnByteField(f, rep1);
            GLnByteField b_n = new GLnByteField(f, rep2);

            genSet.add(b_n);
            genSet.add(a_n);
            genSet.add(b_n.getInverse());
            genSet.add(a_n.getInverse());
        }

        return genSet;
    }

    public static Set<GLnByteField> getGSp_2mGeneratingSet(int m, ByteField f) {

        Set<GLnByteField> genSet = new HashSet<GLnByteField>();

        if (m > 1) {
            byte dim = (byte) (2 * m);

            genSet.addAll(getSp_2mGeneratingSet(m, f));

            byte alpha = f.getMultiplicativeGenerator().getIndex();
            byte[][] rep = new byte[dim][dim];
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    if (i == j) {
                        if (i < m) {
                            rep[i][j] = f.one().getIndex();
                        } else {
                            rep[i][j] = alpha;
                        }
                    } else {
                        rep[i][j] = f.zero().getIndex();
                    }
                }
            }

            genSet.add(new GLnByteField(f, rep));
        }

        return genSet;
    }

    public static Set<GLnByteField> getU_nGeneratingSet(int n, ByteField f) {
        return (n%2==0) ? getU_2mGeneratingSet(n/2, f) : getU_2mPlus1GeneratingSet(n/2, f);
    }
    
    public static Set<GLnByteField> getSU_nGeneratingSet(int n, ByteField f) {
        return (n%2==0) ? getSU_2mGeneratingSet(n/2, f) : getSU_2mPlus1GeneratingSet(n/2, f);
    }
    
    public static Set<PGLnByteField> getPU_nGeneratingSet(int n, ByteField f) {
        return (n%2==0) ? getPU_2mGeneratingSet(n/2, f) : getPU_2mPlus1GeneratingSet(n/2, f);
    }
    
    public static Set<PGLnByteField> getPSU_nGeneratingSet(int n, ByteField f) {
        return (n%2==0) ? getPSU_2mGeneratingSet(n/2, f) : getPSU_2mPlus1GeneratingSet(n/2, f);
    }
    
    public static Set<GLnByteField> getU_2mGeneratingSet(int m, ByteField f) {

        Set<GLnByteField> genSet = new HashSet<GLnByteField>();

        int q = Arithmetic.perfSqrt(f.getOrder());

        if (m > 1 && q != -1) {
            int dim = 2 * m;

            byte[][] rep1 = new byte[dim][dim];
            byte[][] rep2 = new byte[dim][dim];


            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    if (i == j) {
                        rep1[i][j] = f.one().getIndex();
                    } else {
                        rep1[i][j] = f.zero().getIndex();
                    }
                }
            }

            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    if (((i == j + 1) && (i < m)) || ((j == i + 1) && (i >= m))) {
                        rep2[i][j] = f.one().getIndex();
                    } else {
                        rep2[i][j] = f.zero().getIndex();
                    }
                }
            }

            byte xi = f.getMultiplicativeGenerator().getIndex();
            byte barXi = f.pow(xi, q);
            byte eta = (q % 2 == 0) ? f.one().getIndex() : f.pow(xi, (q + 1) / 2);
            byte etaInv = f.inverse(eta);
            rep1[0][0] = xi;
            rep1[dim - 1][dim - 1] = f.inverse(barXi);
            rep2[0][0] = f.one().getIndex();
            rep2[0][m] = eta;
            rep2[dim - 2][m - 1] = etaInv;
            rep2[dim - 1][m - 1] = f.negative(etaInv);

            GLnByteField a_n = new GLnByteField(f, rep1);
            GLnByteField b_n = new GLnByteField(f, rep2);

            genSet.add(b_n);
            genSet.add(a_n);
            genSet.add(b_n.getInverse());
            genSet.add(a_n.getInverse());
        }

        return genSet;
    }

    public static Set<GLnByteField> getSU_2mGeneratingSet(int m, ByteField f) {

        Set<GLnByteField> genSet = new HashSet<GLnByteField>();

        int q = Arithmetic.perfSqrt(f.getOrder());

        if (m > 1 && q != -1) {
            int dim = 2 * m;

            byte[][] rep1 = new byte[dim][dim];
            byte[][] rep2 = new byte[dim][dim];


            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    if (i == j) {
                        rep1[i][j] = f.one().getIndex();
                    } else {
                        rep1[i][j] = f.zero().getIndex();
                    }
                }
            }

            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    if (((i == j + 1) && (i < m)) || ((j == i + 1) && (i >= m))) {
                        rep2[i][j] = f.one().getIndex();
                    } else {
                        rep2[i][j] = f.zero().getIndex();
                    }
                }
            }

            byte xi = f.getMultiplicativeGenerator().getIndex();
            byte barXi = f.pow(xi, q);
            byte eta = (q % 2 == 0) ? f.one().getIndex() : f.pow(xi, (q + 1) / 2);
            byte etaInv = f.inverse(eta);
            rep1[0][0] = xi;
            rep1[1][1] = f.inverse(xi);
            rep1[dim - 2][dim - 2] = barXi;
            rep1[dim - 1][dim - 1] = f.inverse(barXi);
            rep2[0][0] = f.one().getIndex();
            rep2[0][m] = eta;
            rep2[dim - 2][m - 1] = etaInv;
            rep2[dim - 1][m - 1] = f.negative(etaInv);

            GLnByteField a_n = new GLnByteField(f, rep1);
            GLnByteField b_n = new GLnByteField(f, rep2);

            genSet.add(b_n);
            genSet.add(a_n);
            genSet.add(b_n.getInverse());
            genSet.add(a_n.getInverse());
        }

        return genSet;
    }

    public static Set<GLnByteField> getU_2mPlus1GeneratingSet(int m, ByteField f) {

        Set<GLnByteField> genSet = new HashSet<GLnByteField>();

        int q = Arithmetic.perfSqrt(f.getOrder());

        if (m >= 1 && q != -1) {
            int dim = 2 * m + 1;

            byte[][] rep1 = new byte[dim][dim];
            byte[][] rep2 = new byte[dim][dim];

            byte one = f.one().getIndex();
            byte zero = f.zero().getIndex();

            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    if (i == j) {
                        rep1[i][j] = one;
                    } else {
                        rep1[i][j] = zero;
                    }
                }
            }

            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    if (((j == i + 1) && (j < m)) || ((i == j + 1) && (j >= m + 1))) {
                        rep2[i][j] = one;
                    } else {
                        rep2[i][j] = zero;
                    }
                }
            }

            byte minusOne = f.negative(one);
            byte xi = f.getMultiplicativeGenerator().getIndex();
            byte barXi = f.pow(xi, q);
            byte beta = f.negative(f.inverse(f.add(one, f.mult(barXi, f.inverse(xi)))));
            rep1[m - 1][m - 1] = xi;
            rep1[m + 1][m + 1] = f.inverse(barXi);

            rep2[m - 1][0] = beta;
            rep2[m][0] = minusOne;
            rep2[m + 1][0] = one;

            rep2[m - 1][m] = minusOne;
            rep2[m][m] = minusOne;
            rep2[m - 1][dim - 1] = one;

            GLnByteField a_n = new GLnByteField(f, rep1);
            GLnByteField b_n = new GLnByteField(f, rep2);

            genSet.add(b_n);
            genSet.add(a_n);
            genSet.add(b_n.getInverse());
            genSet.add(a_n.getInverse());
        }

        return genSet;
    }

    public static Set<GLnByteField> getSU_2mPlus1GeneratingSet(int m, ByteField f) {

        Set<GLnByteField> genSet = new HashSet<GLnByteField>();

        int q = Arithmetic.perfSqrt(f.getOrder());

        if (m >= 1 && q != -1) {
            int dim = 2 * m + 1;

            byte[][] rep1 = new byte[dim][dim];
            byte[][] rep2 = new byte[dim][dim];

            byte one = f.one().getIndex();
            byte zero = f.zero().getIndex();
            byte xi = f.getMultiplicativeGenerator().getIndex();

            if (m == 1 && q == 2) {
                for (int i = 0; i < dim; i++) {
                    for (int j = 0; j < dim; j++) {
                        if (i == j) {
                            rep1[i][j] = one;
                        } else if (i == 0) {
                            rep1[i][j] = xi;
                        } else if (i == 1 && j == 2) {
                            rep1[i][j] = f.mult(xi, xi);
                        } else {
                            rep1[i][j] = zero;
                        }

                        if (i + j < dim) {
                            if (i == 0 && j == 0) {
                                rep2[i][j] = xi;
                            } else {
                                rep2[i][j] = one;
                            }
                        } else {
                            rep2[i][j] = zero;
                        }
                    }
                }
            } else {

                for (int i = 0; i < dim; i++) {
                    for (int j = 0; j < dim; j++) {
                        if (i == j) {
                            rep1[i][j] = one;
                        } else {
                            rep1[i][j] = zero;
                        }
                    }
                }

                for (int i = 0; i < dim; i++) {
                    for (int j = 0; j < dim; j++) {
                        if (((j == i + 1) && (j < m)) || ((i == j + 1) && (j >= m + 1))) {
                            rep2[i][j] = one;
                        } else {
                            rep2[i][j] = zero;
                        }
                    }
                }

                byte minusOne = f.negative(one);
                byte barXi = f.pow(xi, q);
                byte beta = f.negative(f.inverse(f.add(one, f.mult(barXi, f.inverse(xi)))));
                rep1[m - 1][m - 1] = xi;
                rep1[m][m] = f.mult(barXi, f.inverse(xi));
                rep1[m + 1][m + 1] = f.inverse(barXi);

                rep2[m - 1][0] = beta;
                rep2[m][0] = minusOne;
                rep2[m + 1][0] = one;

                rep2[m - 1][m] = minusOne;
                rep2[m][m] = minusOne;
                rep2[m - 1][dim - 1] = one;
            }

            GLnByteField a_n = new GLnByteField(f, rep1);
            GLnByteField b_n = new GLnByteField(f, rep2);

            genSet.add(b_n);
            genSet.add(a_n);
            genSet.add(b_n.getInverse());
            genSet.add(a_n.getInverse());
        }

        return genSet;
    }

    private static Set<PGLnByteField> project(Set<GLnByteField> glnSet) {
        Set<PGLnByteField> genSet = new HashSet<PGLnByteField>(glnSet.size(), 1);
        for (GLnByteField g : glnSet) {
            genSet.add(new PGLnByteField(g));
        }
        return genSet;
    }

    public static Set<PGLnByteField> getPGLnGeneratingSet(int n, ByteField f) {
        return project(getGLnGeneratingSet(n, f));
    }

    public static Set<PGLnByteField> getPSLnGeneratingSet(int n, ByteField f) {
        return project(getSLnGeneratingSet(n, f));
    }

    public static Set<PGLnByteField> getPSp_2mGeneratingSet(int m, ByteField f) {
        return project(getSp_2mGeneratingSet(m, f));
    }

    public static Set<PGLnByteField> getPGSp_2mGeneratingSet(int m, ByteField f) {
        return project(getGSp_2mGeneratingSet(m, f));
    }

    public static Set<PGLnByteField> getPU_2mGeneratingSet(int m, ByteField f) {
        return project(getU_2mGeneratingSet(m, f));
    }

    public static Set<PGLnByteField> getPSU_2mGeneratingSet(int m, ByteField f) {
        return project(getSU_2mGeneratingSet(m, f));
    }

    public static Set<PGLnByteField> getPU_2mPlus1GeneratingSet(int m, ByteField f) {
        return project(getU_2mPlus1GeneratingSet(m, f));
    }

    public static Set<PGLnByteField> getPSU_2mPlus1GeneratingSet(int m, ByteField f) {
        return project(getSU_2mPlus1GeneratingSet(m, f));
    }

    public static long getOrderU_n(int n, int q) {
        int[] powers = new int[n + 1];
        powers[0] = 1;
        long order = 1;
        for (int i = 1; i <= n; i++) {
            powers[i] = q * powers[i - 1];
        }
        int minus1toTheI = 1;
        for (int i = 1; i <= n; i++) {
            minus1toTheI = -minus1toTheI;
            if (Long.MAX_VALUE / order <= (powers[i] - minus1toTheI) * powers[i - 1]) {
                return 0;
            }

            order = order * (powers[i] - minus1toTheI) * powers[i - 1];
        }
        return order;
    }

    public static long getOrderSU_n(int n, int q) {
        return getOrderU_n(n, q) / (q + 1);
    }

    public static long getOrderPSU_n(int n, int q) {
        long suOrder = getOrderSU_n(n, q);
        int gcd = Arithmetic.gcd(n, q + 1);
        if (suOrder == 0) {
            return 0;
        }
        if (Long.MAX_VALUE / suOrder <= gcd) {
            return 0;
        }
        return getOrderSU_n(n, q) / gcd;
    }

    public static long getOrderPU_n(int n, int q) {
        return getOrderSU_n(n, q);
    }

    public static long getOrderJanko1() {
        return 175560;
    }

    public static long getOrderSuzuki8() {
        return 29120;
    }

    public static long getOrderG2_3() {
        return 4245696;
    }
}



//G2(3)
//        1     3     7     7
//0100000
//1000000
//0001000
//0010000
//0000010
//0000100
//1111222
// 1     3     7     7
//1000000
//0010000
//2220000
//0000100
//2002200
//0000001
//2000022