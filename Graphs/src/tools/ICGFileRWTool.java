package tools;

import api.IndexedColorGraph;
import base.ColorCorrespondence;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pdokos
 */
public class ICGFileRWTool {
    public static <S, C> boolean createSparseColorMatrixFile(IndexedColorGraph<S, C> graph, File sparseMatrixFile) {
        try {
            FileWriter fwSparse;
            fwSparse = new FileWriter(sparseMatrixFile);
            System.out.println("CREATING SPARSE");

            StringBuilder line;
            int size = graph.getNumberOfVertices();
            int it;

            S root = graph.getElement(0);
            Collection<S> neighbors = graph.getNeighborsOf(root);
            int degree = neighbors.size();
            ColorCorrespondence<Integer, C> colorIndexing=new ColorCorrespondence<Integer, C>(degree);
            for (S t : neighbors) {
                colorIndexing.set(graph.getEdgeColor(root, t), graph.getIndexOf(t));
            }
            
            for (int is = 0; is < size; is++) {
                S s = graph.getElement(is);
                for (int i = 1; i<=degree; i++) {
                    line = new StringBuilder();
                    S t = graph.getNeighbor(s, colorIndexing.getColor(i));
                    it = graph.getIndexOf(t);
                    line.append(is+1).append(' ').append(it+1).append(' ').append(i).append('\n');
                    fwSparse.write(line.toString());
                }
            }

            fwSparse.close();
            System.out.println("SPARSE DONE");
            return true;
        } catch (IOException ex) {
            System.out.println("SPARSE NOT DONE");
            Logger.getLogger(ICGFileRWTool.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public static <S, C> boolean createIndexedSparseMatrixFiles(IndexedColorGraph<S, C> graph, File sparseMatrixFile, File indexedElementsFile, String eltsFileHeader, IndexedNeighborGraphTool.StringConverter strConverter) {
        FileWriter fwSparse;
        FileWriter fwElements;
        int size = graph.getNumberOfVertices();
        try {
            fwSparse = new FileWriter(sparseMatrixFile);
            System.out.println("CREATING SPARSE");

            StringBuilder line;
            int it;

            S root = graph.getElement(0);
            Collection<S> neighbors = graph.getNeighborsOf(root);
            int degree = neighbors.size();
            ColorCorrespondence<Integer, C> colorIndexing=new ColorCorrespondence<Integer, C>(degree);
            for (S t : neighbors) {
                colorIndexing.set(graph.getEdgeColor(root, t), graph.getIndexOf(t));
            }
            
            for (int is = 0; is < size; is++) {
                S s = graph.getElement(is);
                for (int i = 1; i<=degree; i++) {
                    line = new StringBuilder();
                    S t = graph.getNeighbor(s, colorIndexing.getColor(i));
                    it = graph.getIndexOf(t);
                    line.append(is+1).append(' ').append(it+1).append(' ').append(i).append('\n');
                    fwSparse.write(line.toString());
                }
            }
            
            fwSparse.close();
            System.out.println("SPARSE DONE");
        } catch (IOException ex) {
            Logger.getLogger(ICGFileRWTool.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }


        try {
            fwElements = new FileWriter(indexedElementsFile);

            if (eltsFileHeader != null) {
                StringBuilder header = new StringBuilder(eltsFileHeader);
                fwElements.write(header.append('\n').toString());
            }

            for (int is = 0; is < size; is++) {
                S s = graph.getElement(is);
                fwElements.write(strConverter.asString(s) + "\n");
            }

            fwElements.close();
            System.out.println("ELEMENTS DONE");
        } catch (IOException ex) {
            Logger.getLogger(ICGFileRWTool.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;

    }
    
//    public static ColorGrouph readColorGrouph(File file) {
//        ColorGrouphBase grouph=null;
//        return grouph;
//    }
    
    public static short[][] readFileToArray(File file) throws FileNotFoundException, IOException {
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
    
    private static List<short[]> readGroupFileToListOfArrays(File file) throws FileNotFoundException, IOException {
        List<short[]> list = new ArrayList<short[]>();
        FileReader fr;
        fr = new FileReader(file);
        BufferedReader in = new BufferedReader(fr);
        String s = in.readLine();
        //if (s.equals("GL2") || s.equals("PGL2") || s.equals("GLn") || s.equals("PGLn") || s.equals("Sn")) {
            //s = in.readLine();
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
        //}
        in.close();
        fr.close();
        return list;
    }
    
    
}



//            Map<C, Integer> colorIndexing = new HashMap<C, Integer>();
//            int colInd=1;
//            for (C c : graph.getColorSet())  {
//                colorIndexing.put(c, colInd);
//                colInd++;
//            }
//            
//            for (int is = 0; is < size; is++) {
//                S s = graph.getElement(is);
//                for (S t : graph.getNeighborsOf(s)) {
//                    line = new StringBuilder();
//                    it = graph.getIndexOf(t);
//                    line.append(is+1).append(' ').append(it+1).append(' ').append(colorIndexing.get(graph.getEdgeColor(s, t))).append('\n');
//                    fwSparse.write(line.toString());
//                }
//            }
