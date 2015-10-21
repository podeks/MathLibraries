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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import basic_operations.Arithmetic;
import groups.GL2_PrimeField;
import groups.GLn_PrimeField;
import groups.PGL2_PrimeField;
import groups.PGLn_PrimeField;
import groups.SymmetricGroup;

/**
 * A utility for writing a Collection of group elements to a text file, and for
 * reading such a file to either a primitive array or to a List&ltG&gt, with G
 * the appropriate Group implementation.
 *
 * <p>
 * The files are written according to the following format:</br>
 * &#160&#160&#160 (i) The first line of each file is one of
 * the five Strings "GL2", "PGL2", "GLn", "PGLn", or "Sn", corresponding to one
 * of the five Group implementations found in the package
 * groups of The Groups module.</br>
 * &#160&#160(ii) The group elements of the collection are separated by 
 * newline characters, and occur in the order defined by the collection's Iterator.</br>
 * &#160(iii) Each group element is written as a list of integer values separated by
 * single-spaces:</br>
 * &#160&#160&#160&#160&#160&#160 (a) For the Linear and Projective Groups, elements are written as lists of n^2 + 1
 * integers separated by single spaces, where n is the dimension of the vector
 * space for the group. The first n^2 integers of the list are the entries of
 * the reduced matrix representative, listed by concatenating the rows of the
 * matrix from top to bottom. The last entry is the order of the base field.
 * </br>
 * &#160&#160 &#160&#160&#160 (b) For the Symmetric Group, elements are written as lists of length n
 * containing permutations of the integers {0, 1, ..., n-1}.
 * </p>
 *
 * @author pdokos
 */
public class FileRWUtility {

    private FileRWUtility() {
    }

    /**
     * Writes the given Collection&ltGL2_PrimeField&gt to the given File. The
     * first line written to the file is the String "GL2". See the class description for
     * an explanation of the format by which the collection is represented within the file.
     *
     * @param file The File being written to.
     * @param col The Collection&ltGL2_PrimeField&gt being written to file.
     * @return true if the collection was successfully written to the given
     * file.
     * @throws IOException
     */
    public static boolean writeToGL2File(File file, Collection<GL2_PrimeField> col) throws IOException {
        FileWriter fw;
        fw = new FileWriter(file);
        StringBuilder line = new StringBuilder();
        line.append("GL2").append('\n');
        fw.write(line.toString());
        for (GL2_PrimeField s : col) {
            line = new StringBuilder();
            line.append(s.getA()).append(' ').append(s.getB()).append(' ').append(s.getC()).append(' ').append(s.getD()).append(' ').append(s.getFieldOrder()).append('\n');
            fw.write(line.toString());
        }
        fw.close();
        return true;
    }

    /**
     * Writes the given Collection&ltPGL2_PrimeField&gt to the given File. The
     * first line written to the file is the String "PGL2".  See the class description for
     * an explanation of the format by which the collection is represented within the file.
     *
     * @param file The File being written to.
     * @param col The Collection&ltPGL2_PrimeField&gt being written to file.
     * @return true if the collection was successfully written to the given
     * file.
     * @throws IOException
     */
    public static boolean writeToPGL2File(File file, Collection<PGL2_PrimeField> col) throws IOException {
        FileWriter fw;
        fw = new FileWriter(file);
        StringBuilder line = new StringBuilder();
        line.append("PGL2").append('\n');
        fw.write(line.toString());
        for (PGL2_PrimeField s : col) {
            line = new StringBuilder();
            line.append(s.getA()).append(' ').append(s.getB()).append(' ').append(s.getC()).append(' ').append(s.getD()).append(' ').append(s.getFieldOrder()).append('\n');
            fw.write(line.toString());
        }
        fw.close();
        return true;
    }

    /**
     * Writes the given Collection&ltGLn_PrimeField&gt to the given File. The
     * first line written to the file is the String "GLn". See the class description for
     * an explanation of the format by which the collection is represented 
     * within the file.
     *
     * @param file The File being written to.
     * @param col The Collection&ltGLn_PrimeField&gt being written to file.
     * @return true if the collection was successfully written to the given
     * file.
     * @throws IOException
     */
    public static boolean writeToGLnFile(File file, Collection<GLn_PrimeField> col) throws IOException {
        FileWriter fw;
        fw = new FileWriter(file);
        StringBuilder line = new StringBuilder();
        line.append("GLn").append('\n');
        fw.write(line.toString());
        for (GLn_PrimeField s : col) {
            line = new StringBuilder();
            line.append(s.toUnpunctuatedString()).append('\n');
            fw.write(line.toString());
        }
        fw.close();
        return true;
    }

    /**
     * Writes the given Collection&ltPGLn_PrimeField&gt to the given File. The
     * first line written to the file is the String "PGLn". See the class description for
     * an explanation of the format by which the collection is represented within the file.
     *
     * @param file The File being written to.
     * @param col The Collection&ltPGLn_PrimeField&gt being written to file.
     * @return true if the collection was successfully written to the given
     * file.
     * @throws IOException
     */
    public static boolean writeToPGLnFile(File file, Collection<PGLn_PrimeField> col) throws IOException {

        FileWriter fw;
        fw = new FileWriter(file);

        StringBuilder line = new StringBuilder();
        line.append("PGLn").append('\n');
        fw.write(line.toString());
        for (PGLn_PrimeField s : col) {
            line = new StringBuilder();
            line.append(s.toUnpunctuatedString()).append('\n');
            fw.write(line.toString());
        }

        fw.close();

        return true;
    }

    /**
     * Writes the given Collection&ltSymmetricGroup&gt to the given File. The
     * first line written to the file is the String "Sn". See the class description for
     * an explanation of the format by which the collection is represented within the file.
     *
     * @param file The File being written to.
     * @param col The Collection&ltSymmetricGroup&gt being written to file.
     * @return true if the collection was successfully written to the given
     * file.
     * @throws IOException
     */
    public static boolean writeToSymGroupFile(File file, Collection<SymmetricGroup> col) throws IOException {
        FileWriter fw;
        fw = new FileWriter(file);
        StringBuilder line = new StringBuilder();
        line.append("Sn").append('\n');
        fw.write(line.toString());
        for (SymmetricGroup s : col) {
            line = new StringBuilder();
            line.append(s.toUnpunctuatedString()).append('\n');
            fw.write(line.toString());
        }
        fw.close();
        return true;
    }

    /**
     * Reads the given file to a List of GL2_PrimeField elements.  
     * 
     * <p>
     * This method is designed to read files created by the writeToGL2File 
     * method of this class.  In particular, the first 
     * line of the file must be the String "GL2"  
     * (see the class description for details on the format that the given file 
     * must follow to be interpreted by this method).
     * </p>
     * 
     * @param file The file to be read.
     * @return The List of GL2_PrimeField elements contained in the file.
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static List<GL2_PrimeField> readGL2File(File file) throws FileNotFoundException, IOException {
        List<GL2_PrimeField> list = new ArrayList<GL2_PrimeField>();
        FileReader fr;
        fr = new FileReader(file);
        BufferedReader in = new BufferedReader(fr);
        String s = in.readLine();
        if (s.equals("GL2")) {
            s = in.readLine();
            while (!(s == null)) {
                Scanner scanner = new Scanner(s);
                short[] entries = new short[5];
                for (int i = 0; i < 5; i++) {
                    entries[i] = scanner.nextShort();
                }
                scanner.close();
                list.add(new GL2_PrimeField(entries[0], entries[1], entries[2], entries[3], entries[4]));
                s = in.readLine();
            }
        }
        in.close();
        fr.close();
        return list;
    }

    /**
     * Reads the given file to a List of PGL2_PrimeField elements.  
     * 
     * <p>
     * This method is designed to read files created by the writeToPGL2File 
     * method of this class.  In particular, the first 
     * line of the file must be the String "PGL2" (see the class description 
     * for details on the format that the given file 
     * must follow to be interpreted by this method).
     * </p>
     * 
     * @param file The file to be read.
     * @return The List of PGL2_PrimeField elements contained in the file.
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static List<PGL2_PrimeField> readPGL2File(File file) throws FileNotFoundException, IOException {
        List<PGL2_PrimeField> list = new ArrayList<PGL2_PrimeField>();
        FileReader fr;
        fr = new FileReader(file);
        BufferedReader in = new BufferedReader(fr);
        String s = in.readLine();
        if (s.equals("PGL2")) {
            s = in.readLine();
            while (!(s == null)) {
                Scanner scanner = new Scanner(s);
                short[] entries = new short[5];
                for (int i = 0; i < 5; i++) {
                    entries[i] = scanner.nextShort();
                }
                scanner.close();
                list.add(new PGL2_PrimeField(entries[0], entries[1], entries[2], entries[3], entries[4]));
                s = in.readLine();
            }
        }
        in.close();
        fr.close();
        return list;
    }

    /**
     * Reads the given file to a List of GLn_PrimeField elements.  
     * 
     * <p>
     * This method is designed to read files created by the writeToGLnFile 
     * method of this class.  In particular, the first 
     * line of the file must be the String "GLn" (see the class description 
     * for details on the format that the given file 
     * must follow to be interpreted by this method).
     * </p>
     * 
     * @param file The file to be read.
     * @return The List of GLn_PrimeField elements contained in the file.
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static List<GLn_PrimeField> readGLnFile(File file) throws FileNotFoundException, IOException {
        List<GLn_PrimeField> set = new ArrayList<GLn_PrimeField>();
        FileReader fr;
        fr = new FileReader(file);
        BufferedReader in = new BufferedReader(fr);
        String s = in.readLine();
        if (s.equals("GLn")) {
            s = in.readLine();
            while (!(s == null)) {
                Scanner scanner = new Scanner(s);
                List<Integer> entries = new ArrayList<Integer>();
                while (scanner.hasNextInt()) {
                    entries.add(scanner.nextInt());
                }
                scanner.close();
                int dim = Arithmetic.perfSqrt(entries.size() - 1);
                int q = entries.get(entries.size() - 1);
                if (dim > 0 && Arithmetic.isPrime(q)) {
                    int[][] mtx = new int[dim][dim];
                    for (int i = 0; i < dim; i++) {
                        for (int j = 0; j < dim; j++) {
                            mtx[i][j] = entries.get(dim * i + j);
                        }
                    }
                    GLn_PrimeField g = new GLn_PrimeField(mtx, (short) q);
                    set.add(g);
                }
                s = in.readLine();
            }
        }
        in.close();
        fr.close();
        return set;
    }

    /**
     * Reads the given file to a List of PGLn_PrimeField elements.  
     * 
     * <p>
     * This method is designed to read files created by the writeToPGLnFile 
     * method of this class.  In particular, the first 
     * line of the file must be the String "PGLn" (see the class description 
     * for details on the format that the given file 
     * must follow to be interpreted by this method).
     * </p>
     * 
     * @param file The file to be read.
     * @return The List of PGLn_PrimeField elements contained in the file.
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static List<PGLn_PrimeField> readPGLnFile(File file) throws FileNotFoundException, IOException {
        List<PGLn_PrimeField> set = new ArrayList<PGLn_PrimeField>();
        FileReader fr;
        fr = new FileReader(file);
        BufferedReader in = new BufferedReader(fr);
        String s = in.readLine();
        if (s.equals("PGLn")) {
            s = in.readLine();
            while (!(s == null)) {
                Scanner scanner = new Scanner(s);
                List<Integer> entries = new ArrayList<Integer>();
                while (scanner.hasNextInt()) {
                    entries.add(scanner.nextInt());
                }
                scanner.close();
                int dim = Arithmetic.perfSqrt(entries.size() - 1);
                int q = entries.get(entries.size() - 1);
                if (dim > 0 && Arithmetic.isPrime(q)) {
                    int[][] mtx = new int[dim][dim];
                    for (int i = 0; i < dim; i++) {
                        for (int j = 0; j < dim; j++) {
                            mtx[i][j] = entries.get(dim * i + j);
                        }
                    }
                    PGLn_PrimeField g = new PGLn_PrimeField(mtx, (short) q);
                    set.add(g);
                }
                s = in.readLine();
            }
        }
        in.close();
        fr.close();
        return set;
    }

    /**
     * Reads the given file to a List of SymmetricGroup elements.  
     * 
     * <p>
     * This method is designed to read files created by the writeToSymGroupFile 
     * method of this class.  In particular, the first 
     * line of the file must be the String "Sn" (see the class description 
     * for details on the format that the given file 
     * must follow to be interpreted by this method).
     * </p>
     * 
     * @param file The file to be read.
     * @return The List of SymmetricGroup elements contained in the file.
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static List<SymmetricGroup> readSymFile(File file) throws FileNotFoundException, IOException {
        List<SymmetricGroup> set = new ArrayList<SymmetricGroup>();
        FileReader fr;
        fr = new FileReader(file);
        BufferedReader in = new BufferedReader(fr);
        String s = in.readLine();
        if (s.equals("Sn")) {
            s = in.readLine();
            while (!(s == null)) {
                Scanner scanner = new Scanner(s);
                List<Integer> entries = new ArrayList<Integer>();
                while (scanner.hasNextInt()) {
                    entries.add(scanner.nextInt());
                }
                scanner.close();
                int len = entries.size();
                if (len > 0) {
                    int[] perm = new int[len];
                    for (int i = 0; i < len; i++) {
                        perm[i] = entries.get(i);
                    }
                    SymmetricGroup g = new SymmetricGroup(perm);
                    set.add(g);
                }
                s = in.readLine();
            }
        }
        in.close();
        fr.close();
        return set;
    }
    
    /**
     * Reads the given file into a two-dimensional array, whose
     * rows constitute a list of the group elements (represented as 
     * one-dimensional arrays) contained in the file.  
     * 
     * <p>
     * This method is designed to read files created by any one of the 
     * writeToGroupFile methods of this class.  In particular, the first 
     * line of the file must be one of the Strings "GL2", "PGL2", "GLn", "PGLn", 
     * or "Sn".  
     * 
     * See the class description
     * for details on the format by which the group elements are represented as 
     * one-dimensional arrays.
     * </p>
     * 
     * @param file The file to be read.
     * @return A two-dimensional <code>short</code> array, whose
     * rows constitute a list of the group elements contained in the file.
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static short[][] readFileToArrayOld(File file) throws FileNotFoundException, IOException {
        List<short[]> list = readGroupFileToListOfArrays(file);
        int size = list.size();
        int dim = list.get(0).length;
        short[][] arr = new short[size][dim];
        for (int i = 0; i < size; i++) {
            System.arraycopy(list.get(i), 0, arr[i], 0, dim);
        }
        list.clear();
        return arr;
    }
    
    public static short[][] readFileToArray(File file) throws FileNotFoundException, IOException {
        FileReader fr;
        fr = new FileReader(file);
        BufferedReader in = new BufferedReader(fr);
        String s = in.readLine();
        Scanner scanner = new Scanner(s);
        System.out.println(scanner.next());
        int size = scanner.nextInt();
        scanner.close();
        
        String firstElt = in.readLine();
        List<Short> entriesList = new ArrayList<Short>();
        for (String t : firstElt.split(" ")) {
            entriesList.add(Short.valueOf(t));//Short.parseShort(t));
        }
        int eltSize=entriesList.size();
        
        short[][] list = new short[size][eltSize];
        int i=0;
        for (int j=0; j<eltSize; j++) {
            list[i][j]=entriesList.get(j);
        }

        long startTime = System.nanoTime();

        if (s.startsWith("GL2") || s.startsWith("PGL2") || s.startsWith("GLn") || s.startsWith("PGLn") || s.startsWith("Sn")) {
            s = in.readLine();
            while (!(s == null)) {
                int j=0;
                i++;
                for (String t: s.split(" ")) {
                    list[i][j] = Short.valueOf(t);
                    j++;
                }
                s = in.readLine();
            }
        }
        in.close();
        fr.close();
        System.out.println(list.length);
        long endTime = System.nanoTime();

        System.out.println((endTime - startTime)/1000000);  
        return list;
    }
    
    private static List<short[]> readGroupFileToListOfArrays(File file) throws FileNotFoundException, IOException {
        List<short[]> list = new ArrayList<short[]>();
        FileReader fr;
        fr = new FileReader(file);
        BufferedReader in = new BufferedReader(fr);
        String s = in.readLine();
        if (s.startsWith("GL2") || s.startsWith("PGL2") || s.startsWith("GLn") || s.startsWith("PGLn") || s.startsWith("Sn")) {
            s = in.readLine();
            while (!(s == null)) {
                Scanner scanner = new Scanner(s);
                List<Short> entriesList = new ArrayList<Short>();
                while (scanner.hasNextShort()) {
                    entriesList.add(scanner.nextShort());
                }
                scanner.close();
                int size = entriesList.size();
                short[] entries = new short[size];
                for (int i = 0; i < size; i++) {
                    entries[i] = entriesList.get(i);
                }
                list.add(entries);
                s = in.readLine();
            }
        }
        in.close();
        fr.close();
        return list;
    }
    
}
