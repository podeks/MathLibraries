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
package finitefields;

import basic_operations.Arithmetic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * An optimized implementation for Finite Fields of order 256 or less, via byte-indexing 
 * of the field elements, and table lookups for the arithmetic operations.  This
 * class is written for situations where one has many objects that are composed
 * of field values in some way, and is performing many operations over the  
 * field (e.g. constructing Cayley graphs for matrix groups over finite fields,
 * where the field order needs to be small for the groups to have order less than
 * five million).
 * 
 * @author pdokos
 */
public class ByteField {
    private short p; //The characteristic of the base field
    private List<Short> minusXToTheN; //The irreducible polynomial defining the extension of the base field.
    private Map<List<Short>, Element> elements;  //Elements of the field as flyweights.
    private int order;
    private Element one;
    private Element zero;
    private Element primitiveElt;
    private Element[] indexedElements;
    //Tables:
    private byte[] inverses;
    private byte[] negatives;
    private byte[][] additionTableArray;
    private byte[][] multiplicationTableArray;

    public static ByteField getF125() {
        return new ByteField((short) 5, Arrays.asList((short) 1, (short) 1, (short) 0));
    }

    public static ByteField getF243() {
        return new ByteField((short) 3, Arrays.asList((short) 1, (short) 2, (short) 0, (short) 0, (short) 0));
    }
    
    public static ByteField getF81() {
        return new ByteField((short) 3, Arrays.asList((short) 2, (short) 1, (short) 0, (short) 0));
    }

    public static ByteField getF27() {
        return new ByteField((short) 3, Arrays.asList((short) 1, (short) 2, (short) 0));
    }

    public static ByteField getF8() {
        return new ByteField((short) 2, Arrays.asList((short) 1, (short) 1, (short) 0));
    }

    public static ByteField getF16() {
        return new ByteField((short) 2, Arrays.asList((short) 1, (short) 1, (short) 0, (short) 0));
    }

    public static ByteField getF32() {
        return new ByteField((short) 2, Arrays.asList((short) 1, (short) 0, (short) 1, (short) 0, (short) 0));
    }

    public static ByteField getF64() {
        return new ByteField((short) 2, Arrays.asList((short) 1, (short) 1, (short) 0, (short) 0, (short) 0, (short) 0));
    }

    public static ByteField getF128() {
        return new ByteField((short) 2, Arrays.asList((short) 1, (short) 1, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0));
    }
    
    public static ByteField getF256() {
        return new ByteField((short) 2, Arrays.asList((short) 1, (short) 1, (short) 0, (short) 1, (short) 1, (short) 0, (short) 0, (short) 0));
    }

    /**
     * 
     * @param n a positive <code>int</code> value less than or equal to 8.
     * @return 
     */
    public static ByteField getChar2Field(int n) {
        switch (n) {
            case 1:
                return getPrimeField((short) 2);
            case 2:
                return getQuadExtension((short) 2);
            case 3:
                return getF8();
            case 4:
                return getF16();
            case 5:
                return getF32();
            case 6: 
                return getF64();
            case 7:
                return getF128();
            case 8:
                return getF256();
        }
        return null;

    }
    
        private static int log2(int n) {
        int log=0;
        int quot = n;
        while (quot%2==0) {
            quot=quot/2;
            log++;
        }
        return log;
    }
    
    /**
     * A static convenience method for creating a finite field of order less than or equal to 256.
     * @param q any <code>short</code> integer prime power less than or equal to 256. 
     * @return a <code>ByteField</code> object modeling the field of q elements.
     */
    public static ByteField getField(short q) {
        if (Arithmetic.isPrime(q))
            return getPrimeField(q);
        int perfSqrt = Arithmetic.perfSqrt(q);
        if (Arithmetic.isPrime(perfSqrt))
            return getQuadExtension((short) perfSqrt);
        if (q%2==0) {
           return getChar2Field(log2(q));
        }
        if (q==125)
            return getF125();
        if (q==27)
            return getF27();
        if (q==81)
            return getF81();
        if (q==243)
            return getF243();
        return null;
    }

    /**
     * A static convenience method for creating a prime field.
     * @param p any <code>short</code> integer prime less than 256. 
     * @return a <code>ByteField</code> object modeling the field of p elements.
     */
    public static ByteField getPrimeField(short p) {
        return new ByteField(p, Arrays.asList(new Short((short) 0)));
    }

    /**
     * A static convenience method for creating a degree 2 extension of a prime field.
     * @param p any <code>short</code> integer prime less than 128. 
     * @return a <code>ByteField</code> object modeling the field of p elements.
     */
    public static ByteField getQuadExtension(short p) {
        return new ByteField(p, Arrays.asList(
                (short) Arithmetic.reducedProduct( -1,  Arithmetic.getMultiplicativeGenerator(p), p),
                (short) (1 - (p % 2))));
    }

    /**
     * Constructor for a ByteField object, invoked by specifying a 
     * <code>short</code> integer prime &le 256 for the prime base field, and
     * a monic irreducible polynomial (in the form of a List of its coefficients)
     * over the field of p elements by which the field extension is defined. 
     * 
     * @param p any <code>short</code> integer prime &le 256. 
     * @param coeffs a <code>List&ltShort></code> representing the coefficients, 
     * starting with the constant term and ending with that of the next to 
     * largest order term, of a monic irreducible polynomial over the field of 
     * p elements.
     * 
     * The value of <code>p^(coeffs.size())</code> must be less than or equal to 256.
     */
    public ByteField(short p, List<Short> coeffs) {
        this.p = p;
        minusXToTheN = new ArrayList<Short>(coeffs.size());
        for (short a : coeffs) {
            minusXToTheN.add((short) -a);
        }
        int dim = coeffs.size();
        order = (int) Math.round(Math.pow(p, dim));
        elements = new LinkedHashMap<List<Short>, Element>(order);

        List<Short> baseField = new ArrayList<Short>(p);
        for (short i = 0; i < p; i++) {
            baseField.add(i);
        }

        //Create elements.
        for (Short k : baseField) {
            List<Short> nextElt = new ArrayList<Short>(dim);
            nextElt.add(k);
            for (int i = 1; i < dim; i++) {
                nextElt.add((short) 0);
            }
            elements.put(nextElt, new Element(nextElt, (byte) elements.size()));

        }

        for (int i = 1; i < dim; i++) {
            List<List<Short>> degIMinusOneElts = new ArrayList<List<Short>>(elements.keySet());
            for (List<Short> elt : degIMinusOneElts) {
                for (Short k : baseField) {
                    if (k.compareTo((short) 0) != 0) { //works without this
                        List<Short> nextElt = new ArrayList<Short>(elt);  //Collections.copy(nextElt, elt);
                        nextElt.set(i, k);
                        elements.put(nextElt, new Element(nextElt, (byte) elements.size()));
                    }
                }
            }
        }

        List<Short> oneAsList = new ArrayList<Short>(dim);
        oneAsList.add((short) 1);
        for (int i = 1; i < dim; i++) {
            oneAsList.add((short) 0);
        }
        one = elements.get(oneAsList);

        List<Short> zeroAsList = new ArrayList<Short>(dim);
        for (int i = 0; i < dim; i++) {
            zeroAsList.add((short) 0);
        }
        zero = elements.get(zeroAsList);

        inverses = new byte[order];
        negatives = new byte[order];
        additionTableArray = new byte[order][order];
        multiplicationTableArray = new byte[order][order];
        for (int i = 0; i < order; i++) {
            inverses[i] = (byte) 0;
            negatives[i] = (byte) 0;
            for (int j = 0; j < order; j++) {
                additionTableArray[i][j] = (byte) 0;
                multiplicationTableArray[i][j] = (byte) 0;
            }
        }

        indexedElements = new Element[order];
        for (int i = 0; i < order; i++) {
            indexedElements[i] = zero;
        }
        for (List<Short> e : elements.keySet()) {
            indexedElements[elements.get(e).getNormalizedIndex()] = elements.get(e);
            System.out.println("--> " + elements.get(e).getNormalizedIndex());
        }

        createTables();
        findPrimitiveElt();

    }

    public void test() {
        for (List<Short> elt : elements.keySet()) {
            Element a = elements.get(elt);
            for (List<Short> eltb : elements.keySet()) {
                Element b = elements.get(eltb);
                Element prod = indexedElements[getNormalizedIndex(multiplicationTableArray[a.getNormalizedIndex()][b.getNormalizedIndex()])];
                System.out.println(a.toString() + " * " + b.toString() + " = " + prod.toString());
            }
            System.out.println(a.toString());
        }
    }

    private void createTables() {

        for (List<Short> x : elements.keySet()) {
            for (List<Short> y : elements.keySet()) {
                additionTableArray[elements.get(x).getNormalizedIndex()][elements.get(y).getNormalizedIndex()] = elements.get(add(x, y)).getIndex();
            }
        }

        for (List<Short> x : elements.keySet()) {
            for (List<Short> y : elements.keySet()) {
                multiplicationTableArray[elements.get(x).getNormalizedIndex()][elements.get(y).getNormalizedIndex()] = elements.get(multiply(x, y)).getIndex();
            }
        }

        for (List<Short> elt : elements.keySet()) {
            negatives[elements.get(elt).getNormalizedIndex()] = elements.get(getNegative(elt)).getIndex();
        }

        for (List<Short> elt : elements.keySet()) {
            Element elt1 = elements.get(elt);
            for (List<Short> eltb : elements.keySet()) {
                Element elt2 = elements.get(eltb);
                if (multiplicationTableArray[elt1.getNormalizedIndex()][elt2.getNormalizedIndex()] == one.getIndex()) {
                    inverses[elt1.getNormalizedIndex()] = elt2.getIndex();
                    break;
                }
            }
        }
    }

    private List<Short> add(List<Short> x, List<Short> y) {
        List<Short> sum = new ArrayList<Short>(x.size());
        for (int i = 0; i < x.size(); i++) {
            sum.add(Arithmetic.reducedSum(x.get(i), y.get(i), p));
        }
        return sum;
    }

    private List<Short> multiply(List<Short> a, List<Short> b) {
        List<Short> xToTheItimesA = new ArrayList<Short>(a);
        List<Short> product = multByBaseElt(a, b.get(0));
        for (int i = 1; i < b.size(); i++) {
            xToTheItimesA = multByX(xToTheItimesA);
            product = add(product, multByBaseElt(xToTheItimesA, b.get(i)));
        }
        return product;
    }

    private List<Short> multByX(List<Short> elt) {
        List<Short> shift = new ArrayList<Short>(elt.size());
        shift.add((short) 0);
        for (int i = 1; i < elt.size(); i++) {
            shift.add(elt.get(i - 1));
        }
        List<Short> product = add(shift, new ArrayList<Short>(multByBaseElt(minusXToTheN, elt.get(elt.size() - 1))));
        return product;
    }

    private List<Short> multByBaseElt(List<Short> x, Short a) {
        List<Short> product = new ArrayList<Short>(x.size());
        for (int i = 0; i < x.size(); i++) {
            product.add(Arithmetic.reducedProduct(x.get(i), a, p));
        }
        return product;
    }

    private List<Short> getNegative(List<Short> x) {
        List<Short> neg = new ArrayList<Short>(x.size());
        for (Short e : x) {
            neg.add(Arithmetic.reduce(-e, p));
        }
        return neg;
    }

    public byte add(byte x, byte y) {
        return additionTableArray[getNormalizedIndex(x)][getNormalizedIndex(y)];
    }

    public byte mult(byte x, byte y) {
        return multiplicationTableArray[getNormalizedIndex(x)][getNormalizedIndex(y)];
    }

    public byte inverse(byte x) {
        return inverses[getNormalizedIndex(x)];
    }

    public byte negative(byte x) {
        return negatives[getNormalizedIndex(x)];
    }
    
    public byte pow(byte x, int n) {
        if (n<0) {
            return pow(inverse(x), -n);
        } else if (n==0) {
            return one.getIndex();
        }
        return mult(x, pow(x, n-1));
    }

    public Element one() {
        return one;
    }

    public Element zero() {
        return zero;
    }

    public short getCharacteristic() {
        return p;
    }

    public int getDimension() {
        return minusXToTheN.size();
    }

    public int getOrder() {
        return order;
    }

    public Element getMultiplicativeGenerator() {
        return primitiveElt;
    }

    private void findPrimitiveElt() {
        Iterator<List<Short>> iterator = elements.keySet().iterator();
        boolean found = false;
        Element elt = zero;
        while (iterator.hasNext() && !found) {
            elt = elements.get(iterator.next());
            if (getOrder(elt) == (getOrder() - 1)) {
                found = true;
            }
        }
        primitiveElt = elt;
    }

    public int getOrder(Element e) {
        int k = 1;
        Element eToTheK = e;
        if (e.equals(zero)) {
            return 0;
        }
        while (!eToTheK.equals(one)) {
            eToTheK = eToTheK.times(e);
            k++;
        }
        return k;
    }

    public Element lookup(List<Short> e) {
        if (!elements.containsKey(e)) {
            elements.put(e, new Element(e, (byte) elements.size()));
        }
        return elements.get(e);
    }
    
    public Element getElement(byte index) {
        return indexedElements[getNormalizedIndex(index)];
    }
    
    public static int getNormalizedIndex(byte b) {
            return (b<0) ? 256+b : b;
        }

    public class Element {

        short[] coeffs;
        byte index;

        public Element(List<Short> coefficients) {
            coeffs = new short[coefficients.size()];
            int i = 0;
            for (Short e : coefficients) {
                coeffs[i] = e;
                i++;
            }
        }

        public Element(List<Short> coefficients, byte ind) {
            coeffs = new short[coefficients.size()];
            int i = 0;
            for (Short e : coefficients) {
                coeffs[i] = e;
                i++;
            }
            index = ind;

        }

        public byte getIndex() {
            return index;
        }
        
        public int getNormalizedIndex() {
            return (index<0) ? 256+index : index;
        }
        
        public Element plus(Element e) {
            return indexedElements[ByteField.getNormalizedIndex(additionTableArray[getNormalizedIndex()][e.getNormalizedIndex()])];
        }

        public Element times(Element e) {
            return indexedElements[ByteField.getNormalizedIndex(multiplicationTableArray[getNormalizedIndex()][e.getNormalizedIndex()])];
        }

        public Element inverse() {
            return indexedElements[ByteField.getNormalizedIndex(inverses[getNormalizedIndex()])];
        }

        public Element negative() {
            return indexedElements[ByteField.getNormalizedIndex(negatives[getNormalizedIndex()])];
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Element) {
                return index == ((Element) o).index; 
            }
            return false;
        }

        @Override
        public int hashCode() {
            return index; 
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(coeffs[0]);
            for (int i = 1; i < coeffs.length; i++) {
                sb.append("+").append(coeffs[i]);
                if (i == 1) {
                    sb.append("x");
                } else {
                    sb.append("x^").append(i);
                }
            }
            return sb.toString();
        }
    }
}
