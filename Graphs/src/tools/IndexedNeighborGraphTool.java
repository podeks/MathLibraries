package tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import api.IndexedNeighborGraph;

/**
 * This class contains tools, in the form of static methods, that enable one to 
 * save a (typically sparse) IndexedNeighborGraph to a text file.  The graph is
 * saved in the form of a sparse adjacency matrix.
 * The file produced consists of a list of rows of triplets of integers
 * corresponding to the nonzero entries of the adjacency matrix.  More specifically, the first
 * two entries in a row consist of the indices corresponding to a pair of 
 * neighboring vertices.  The third entry of each row is a 1, corresponding the 
 * value of the matrix entry at that index pair.  The rows in the list are 
 * ordered lexicographically.
 * 
 * <p>
 * In this format, the adjacency matrix of the graph can be loaded into any
 * software (e.g. MATLAB) that deals with sparse matrices.
 * </p>
 * 
 * <p>
 * In addition, there is also the capability of saving a list of String 
 * representations of the vertex elements, ordered according to the indexing.
 * The method createIndexedElementsFile, which performs this action, requires
 * an implementation of the StringConverter interface for translating the vertex
 * elements to Strings.
 * 
 * </p>
 * 
 * @author pdokos
 */
public class IndexedNeighborGraphTool {

    private IndexedNeighborGraphTool() {}
    
    /**
     * A helper interface for translating vertex elements of a graph to Strings.
     * For a graph whose vertices are of some type T, an implementation of this
     * interface for use by the class IndexedNeighborGraphTool will typically
     * return a String representation for any object of type T. 
     */
    public interface StringConverter {

        /**
         * Returns a String representation of the object t, if there is one.  
         * Otherwise this method returns an empty String.
         * 
         * @param t any object
         * @return A String representation of t, if it is defined; otherwise 
         * returns any empty String.
         */
        public String asString(Object t);
    }

    /**
     * This method writes the given IndexedNeighborGraph to the given file, in the 
     * form of a sparse adjacency matrix.
     * The file produced consists of a list of rows of triplets of integers
     * corresponding to the nonzero entries of the adjacency matrix.  More specifically, the first
     * two entries in a row consist of the indices corresponding to a pair of 
     * neighboring vertices.  The third entry of each row is a 1, corresponding to the 
     * value of the matrix entry at that index pair.  The rows in the list are 
     * ordered lexicographically.
     * 
     * @param <T> The vertex type on which the graph structure is defined.
     * @param graph The graph being written to file.
     * @param sparseMatrixFile The file to which the graph is written.
     * 
     * @return true if the graph was successfully written to file.
     */
    public static <T> boolean createSparseMatrixFile(IndexedNeighborGraph<T> graph, File sparseMatrixFile) {
        try {
            FileWriter fwSparse;
            fwSparse = new FileWriter(sparseMatrixFile);
            System.out.println("CREATING SPARSE");

            StringBuilder line;
            int size = graph.getNumberOfVertices();
            int it;

            for (int is = 1; is <= size; is++) {
                T s = graph.getElement(is);
                for (T t : graph.getNeighborsOf(s)) {
                    line = new StringBuilder();
                    it = graph.getIndexOf(t);
                    line.append(is).append(' ').append(it).append(' ').append(1).append('\n');
                    fwSparse.write(line.toString());
                }
            }

            fwSparse.close();
            System.out.println("SPARSE DONE");
            return true;
        } catch (IOException ex) {
            Logger.getLogger(IndexedNeighborGraphTool.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /**
     * This method writes a list of String representations of the vertices of 
     * the given graph to the given file, using the given StringConvertor as a
     * translation mechanism.  The String representations are separated by newline
     * characters, and ordered according to the indexing on the vertex set.
     * 
     * @param <T> The vertex type on which the graph structure is defined.
     * @param graph The graph whose vertex set is being written to file.
     * @param indexedElementsFile The file to which the String representations of the vertices are written. 
     * @param eltsFileHeader A header String as the first line of the file. 
     * The null reference may be passed to produce a file without a header.
     * @param strConverter A StringConverter implementation that translates objects of type T to Strings.
     * 
     * @return true if the list of vertices of the graph was successfully written to file.
     */
    public static <T> boolean createIndexedElementsFile(IndexedNeighborGraph<T> graph, File indexedElementsFile, String eltsFileHeader, StringConverter strConverter) {
        FileWriter fwElements;
        try {
            fwElements = new FileWriter(indexedElementsFile);
            int size = graph.getNumberOfVertices();

            if (eltsFileHeader != null) {
                StringBuilder header = new StringBuilder(eltsFileHeader);
                fwElements.write(header.append('\n').toString());
            }

            for (int is = 1; is <= size; is++) {
                T s = graph.getElement(is);
                fwElements.write(strConverter.asString(s) + "\n");
            }

            fwElements.close();
            System.out.println("ELEMENTS DONE");
        } catch (IOException ex) {
            Logger.getLogger(IndexedNeighborGraphTool.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public static <T> boolean createIndexedSparseMatrixFiles(IndexedNeighborGraph<T> graph, File sparseMatrixFile, File indexedElementsFile, String eltsFileHeader, StringConverter strConverter) {
        FileWriter fwSparse;
        FileWriter fwElements;
        int size = graph.getNumberOfVertices();
        try {
            fwSparse = new FileWriter(sparseMatrixFile);
            System.out.println("CREATING SPARSE");

            StringBuilder line;
            int it;

            for (int is = 1; is <= size; is++) {

                T s = graph.getElement(is);
                for (T t : graph.getNeighborsOf(s)) {
                    line = new StringBuilder();
                    it = graph.getIndexOf(t);
                    line.append(is).append(' ').append(it).append(' ').append(1).append('\n');
                    fwSparse.write(line.toString());
                }
            }


            fwSparse.close();
            System.out.println("SPARSE DONE");
        } catch (IOException ex) {
            Logger.getLogger(IndexedNeighborGraphTool.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }


        try {
            fwElements = new FileWriter(indexedElementsFile);

            if (eltsFileHeader != null) {
                StringBuilder header = new StringBuilder(eltsFileHeader);
                fwElements.write(header.append('\n').toString());
            }

            for (int is = 1; is <= size; is++) {
                T s = graph.getElement(is);
                fwElements.write(strConverter.asString(s) + "\n");
            }

            fwElements.close();
            System.out.println("ELEMENTS DONE");
        } catch (IOException ex) {
            Logger.getLogger(IndexedNeighborGraphTool.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;

    }
}



/*
    public static <T> boolean createSparseMatrixFile(IndexedNeighborGraph<T> graph, String path) {

        File f = new File(path); //"/Users/perry/desktop/Sparse.txt"
        try {
            f.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(IndexedNeighborGraphTool.class.getName()).log(Level.SEVERE, null, ex);
        }

        return createSparseMatrixFile(graph, f);

    }
    */