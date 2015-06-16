package colorgrouph;

import api.Group;
import api.IndexedColorGraph;
import cayleygraphs.CayleyColorGraphBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pdokos
 */
public class ColorGrouphBase implements ColorGrouph {

    private GrouphVertex root;
    private List<ColorMapping> colorKeyedNeighborSets;
    private List<Integer> shellStartIndices;
    private Map<Integer, Integer> colorInvolution;
    private int degree;
    private CGNavigator navigator;
    
    private Map<GrouphVertex, List<Integer>> shortestPathStore;

    public ColorGrouphBase(int numverts, Map<Integer, Integer> colorInvolution) {
        colorKeyedNeighborSets = new ArrayList<ColorMapping>(numverts); //check loadfactor stuff...
        shellStartIndices = new ArrayList<Integer>();
        this.colorInvolution = colorInvolution;
        this.degree = colorInvolution.size();
        navigator = new CGNavigator(this);
        shortestPathStore = new HashMap<GrouphVertex, List<Integer>>();
    }

    private <S, C> ColorGrouphBase(IndexedColorGraph<S, C> graph) {
        colorKeyedNeighborSets = new ArrayList<ColorMapping>(graph.getNumberOfVertices()); //check loadfactor stuff...
        shellStartIndices = new ArrayList<Integer>();
        degree = graph.getColorSet().size();
        navigator = new CGNavigator(this);
        shortestPathStore = new HashMap<GrouphVertex, List<Integer>>();

        for (int d = 0; d <= graph.getMaxDistanceFromRoot(); d++) {
            shellStartIndices.add(graph.getShellStartIndex(d));
        }

        S root1 = graph.getRoot();
        Map<C, Integer> colorIndexing = new HashMap<C, Integer>();
        for (C c : graph.getColorSet()) {
            S neighbor = graph.getNeighbor(root1, c);
            colorIndexing.put(c, graph.getIndexOf(neighbor));
        }

        colorInvolution = new HashMap<Integer, Integer>(graph.getColorSet().size() + 1, 1.0f);
        for (C c : colorIndexing.keySet()) {
            colorInvolution.put(colorIndexing.get(c), colorIndexing.get(graph.getInverseColor(c)));
        }

        int size = graph.getNumberOfVertices();
        //ColorGrouphBase b = new ColorGrouphBase(size, colorInv);
        for (int is = 0; is < size; is++) {
            GrouphVertex vert = new GrouphVertex(is);
            colorKeyedNeighborSets.add(new ColorMapping(vert));
        }
        root = colorKeyedNeighborSets.get(0).getSource();
        for (int is = 0; is < size; is++) {
            S s = graph.getElement(is);
            ColorMapping colorMapping = colorKeyedNeighborSets.get(is);
            for (C c : graph.getColorSet()) {
                S t = graph.getNeighbor(s, c);
                int it = graph.getIndexOf(t);
                colorMapping.set(colorIndexing.get(c), getElement(it));
            }
        }

    }

    public static <S, C> ColorGrouph makeColorGrouph(IndexedColorGraph<S, C> graph) {
        return new ColorGrouphBase(graph);
    }
    
    public static ColorGrouph readFromFile(File file) {
        return new ColorGrouphBase(file);
    }

    private ColorGrouphBase(File file) {
        System.out.println("READING FILE...");
        List<int[]> list = readGrouphFileToListOfArrays(file);
        int size = list.size();
        
        System.out.println("CREATING GROUPH...");
        
        
        degree = 0;
        while (degree<size && list.get(degree)[2]>degree) {
            degree++;
        }
        
        int numVerts = (degree != 0) ? size/degree : 0;

        colorKeyedNeighborSets = new ArrayList<ColorMapping>(numVerts);
        shellStartIndices = new ArrayList<Integer>();
        
        for (int is = 0; is < numVerts; is++) {
            GrouphVertex vert = new GrouphVertex(is);
            colorKeyedNeighborSets.add(new ColorMapping(vert));
        }
        root= (numVerts != 0) ? colorKeyedNeighborSets.get(0).getSource() : null;
        
        colorInvolution = new HashMap<Integer, Integer>(degree + 1, 1.0f);
        for (int i=1; i<=degree; i++) {
            int j=0;
            boolean found=false;
            while (j<degree && !found) {
                int[] mtxEntry = list.get(i*degree+j);
                if (mtxEntry[1]==1) {
                    colorInvolution.put(i, j+1);
                    found=true;
                }
                j++;
            }
            
        }
        
        
        
        Iterator<int[]> iterator = list.iterator();
        
        //int[] rootElt = iterator.next();
        int iSrc = 1;//rootElt[0];
        
        //int prevShellStart=-1;
        int shellStart = 0;
        boolean shellStartVert=true;
        
        if (numVerts != 0) {

            ColorMapping fiber = colorKeyedNeighborSets.get(iSrc - 1);
            while (iterator.hasNext()) {
                int[] sparseEntry = iterator.next();

                if (sparseEntry[0] != iSrc) {
                    if (shellStartVert) {
                        shellStartIndices.add(iSrc - 1);
                        //prevShellStart=shellStart;
                        shellStart = iSrc;
                    }
                    shellStartVert = true;
                    iSrc++;
                    fiber = colorKeyedNeighborSets.get(iSrc - 1);
                    //shellStartVert=true;
                }
                if (sparseEntry[1] < shellStart) {
                    shellStartVert = false;
                }
                fiber.set(sparseEntry[2], colorKeyedNeighborSets.get(sparseEntry[1] - 1).getSource());
                
            }
        }
        navigator = new CGNavigator(this);
        shortestPathStore = new HashMap<GrouphVertex, List<Integer>>();
        //System.out.println("SIZE: " + this.getNumberOfVertices() + " : " +this.getNumberOfEdges());
        
//        short color=0;
//        int degree = 0;
//        while (color != 1) {
//            degree++;
//            color = list.get(degree)[2];
//        }
//        
//        //root = colorKeyedNeighborSets.get(0).getSource();
//        for (int is = 0; is < size; is++) {
//            GrouphVertex s = getElement(is);
//            ColorMapping colorMapping = colorKeyedNeighborSets.get(is);
//            for (int c =1; c<=degree; c++) {
//                GrouphVertex t = getNeighbor(s, c);
//                int it = getIndexOf(t);
//                colorMapping.set(c, getElement(it));
//            }
//        }
        
        
    }

    public static int[][] readFileToArray(File file) throws FileNotFoundException, IOException {
        List<int[]> list = readGrouphFileToListOfArrays(file);
        int size = list.size();
        int dim = list.get(0).length;
        int[][] arr = new int[size][dim];
        for (int i = 0; i < size; i++) {
            System.arraycopy(list.get(i), 0, arr[i], 0, dim);
        }
        list.clear();
        return arr;
    }

    private static List<int[]> readGrouphFileToListOfArrays(File file) {
        List<int[]> list = new ArrayList<int[]>();
        FileReader fr;
        try {
            long startTime = System.nanoTime();
            fr = new FileReader(file);
            BufferedReader in = new BufferedReader(fr);
            String s = in.readLine();
            while (!(s == null)) {
//                Scanner scanner = new Scanner(s);
//                List<Integer> entriesList = new ArrayList<Integer>();
//                while (scanner.hasNextInt()) {
//                    entriesList.add(scanner.nextInt());
//                }
//                scanner.close();
//                int size = entriesList.size();
//                int[] entries = new int[size];
//                for (int i = 0; i < size; i++) {
//                    entries[i] = entriesList.get(i);
//                }
                int j=0;
                //i++;
                int[] entries = new int[3];
                for (String t: s.split(" ")) {
                    //entriesList.add(Short.valueOf(t));//Short.parseShort(t));
                    try {
                        entries[j] = Integer.valueOf(t);
                    } catch (java.lang.NumberFormatException e) {
                        return new ArrayList<int[]>();
                    } catch (java.lang.IndexOutOfBoundsException e) {
                        return new ArrayList<int[]>();
                    }
                    j++;
                }
                list.add(entries);
                s = in.readLine();
            }
            in.close();
            fr.close();
            
            long endTime = System.nanoTime();
            System.out.println((endTime - startTime)/1000000 + " seconds");
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ColorGrouphBase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ColorGrouphBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public Set<Integer> getColorSet() {
        return Collections.unmodifiableSet(colorInvolution.keySet());
    }

    @Override
    public int getShellStartIndex(int d) {
        return shellStartIndices.get(d);
    }

    @Override
    public void createShortestPathStore(Collection<GrouphVertex> verts) {
        for (GrouphVertex s : verts) {
            shortestPathStore.put(s, navigator.getAShortestPathTo(s));
        }
    }
    
    @Override
    public void clear() {
        colorKeyedNeighborSets.clear();
    }
    
    public class GrouphVertex implements Group<GrouphVertex> {

        int index;

        public GrouphVertex(int n) {
            if (n >= 0) {
                index = n;
            } else {
                index = -1;
            }
        }

        public int getIndex() {
            return index;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof GrouphVertex) {
                return ((GrouphVertex) o).index == index;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return this.index;
        }

        @Override
        public String toString() {
            return Integer.toString(index);
        }

        @Override
        public GrouphVertex leftProductBy(GrouphVertex h) {
            return navigator.getEndpointOfPath(h, navigator.getAShortestPathTo(this));
            //return navigator.getEndpointOfPath(this, navigator.getAShortestPathTo(h));
        }

        @Override
        public GrouphVertex rightProductBy(GrouphVertex h) {
            if (shortestPathStore.containsKey(h))
                return navigator.getEndpointOfPath(this, shortestPathStore.get(h));
            return navigator.getEndpointOfPath(this, navigator.getAShortestPathTo(h));
            //return navigator.getEndpointOfPath(h, navigator.getAShortestPathTo(this));
        }

        @Override
        public GrouphVertex getInverse() {
            List<Integer> shortestPathFrom = navigator.getAShortestPathFrom(this);
            return navigator.getEndpointOfPath(shortestPathFrom);
        }

        @Override
        public GrouphVertex getIdentity() {
            return getRoot();
        }

        @Override
        public boolean isOperationalWith(GrouphVertex h) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    public static boolean writeToFile(File file, Collection<GrouphVertex> col, int order, String source) throws IOException {
        FileWriter fw;
        fw = new FileWriter(file);
        StringBuilder line = new StringBuilder();
        line.append("ORIGIN: ").append(source).append('\n');
        fw.write(line.toString());
        line = new StringBuilder();
        line.append("ORDER: ").append(order).append('\n');
        fw.write(line.toString());
        for (GrouphVertex s : col) {
            line = new StringBuilder();
            line.append(s.getIndex()).append('\n');
            fw.write(line.toString());
        }
        fw.close();
        return true;
    }
    
    public class ColorMapping {//implements Collection<GrouphVertex>{

        private GrouphVertex source;
        private List<GrouphVertex> colorKeyedList;
        private Map<GrouphVertex, Integer> edgeColors;

        public ColorMapping(GrouphVertex src) {
            source = src;
            colorKeyedList = new ArrayList<GrouphVertex>(degree);
            for (int i = 0; i < degree; i++) {
                colorKeyedList.add(null);
            }
            edgeColors = new HashMap<GrouphVertex, Integer>(degree);
        }

        public void set(Integer color, GrouphVertex neighbor) {
            colorKeyedList.set(color - 1, neighbor);
            edgeColors.put(neighbor, color);
        }

        public GrouphVertex getSource() {
            return source;
        }

        public GrouphVertex getTarget(Integer color) {
            return colorKeyedList.get(color - 1);
        }

        public Integer getColor(GrouphVertex target) {
            return edgeColors.get(target);
        }

        public List<GrouphVertex> getVertexList() {
            return Collections.unmodifiableList(colorKeyedList);
        }

        public boolean containsTarget(GrouphVertex tgt) {
            return edgeColors.containsKey(tgt);
        }
    }

    public IndexedColorGraph<GrouphVertex, GrouphVertex> getCayley(Set<GrouphVertex> genSet) {
        return CayleyColorGraphBuilder.buildCayleyColorGraph(genSet, root);//.getIndexedNavigableCayleyGraph(genSet, root);//.getCayleyGraph(genSet, root);
    }

    @Override
    public int getDistanceFromTheRoot(GrouphVertex s) {
        if (this.containsVertex(s)) {
            int index = s.getIndex();
            int d = 1;
            while (d < shellStartIndices.size()) {
                if (shellStartIndices.get(d) > index) {
                    return d - 1;
                }
                d++;
            }
            return d - 1;
        }
        return -1;
    }

    @Override
    public int getMaxDistanceFromRoot() {
        return shellStartIndices.size() - 1;
    }

    @Override
    public List<GrouphVertex> getShell(int d) {
        if (d >= 0) {
            if (d < shellStartIndices.size() - 1) {
                return getElements(shellStartIndices.get(d), shellStartIndices.get(d + 1));
            }
            if (d == shellStartIndices.size() - 1) {
                return getElements(shellStartIndices.get(d), colorKeyedNeighborSets.size());
            }
        }
        return new ArrayList<GrouphVertex>();
    }

    @Override
    public final GrouphVertex getElement(int i) {
        return colorKeyedNeighborSets.get(i).getSource();
    }

    @Override
    public int getIndexOf(GrouphVertex t) {
        return t.getIndex();
    }

    @Override
    public List<GrouphVertex> getElements(int minIncl, int maxExcl) {
        List<GrouphVertex> elements = new ArrayList<GrouphVertex>(maxExcl - minIncl);
        for (int i = minIncl; i < maxExcl; i++) {
            elements.add(colorKeyedNeighborSets.get(i).getSource());
        }
        return elements;
    }

    @Override
    public GrouphVertex getNeighbor(GrouphVertex vertex, Integer color) {
        return colorKeyedNeighborSets.get(vertex.getIndex()).getTarget(color);
    }

    @Override
    public Integer getEdgeColor(GrouphVertex src, GrouphVertex tgt) {
        return colorKeyedNeighborSets.get(src.getIndex()).getColor(tgt);
    }

    @Override
    public Integer getInverseColor(Integer c) {
        return colorInvolution.get(c);
    }

    @Override
    public GrouphVertex getRoot() {
        return root;
    }

    @Override
    public Collection<GrouphVertex> getNeighborsInNextShell(GrouphVertex s) {
        int dist = getDistanceFromTheRoot(s);
        Set<GrouphVertex> neighs = new HashSet<GrouphVertex>();
        if (dist < getMaxDistanceFromRoot() && dist != -1) {
            int ind = shellStartIndices.get(dist + 1);
            for (GrouphVertex t : getNeighborsOf(s)) {
                if (this.getIndexOf(t) >= ind) {
                    neighs.add(t);
                }
            }
        }
        return neighs;
    }

    @Override
    public Collection<GrouphVertex> getNeighborsInSameShell(GrouphVertex s) {
        int dist = getDistanceFromTheRoot(s);
        Set<GrouphVertex> neighs = new HashSet<GrouphVertex>();
        if (dist != -1) {
            if (dist < getMaxDistanceFromRoot()) {
                int startInd = shellStartIndices.get(dist);
                int endInd = shellStartIndices.get(dist + 1);
                for (GrouphVertex t : getNeighborsOf(s)) {
                    if (getIndexOf(t) >= startInd && getIndexOf(t) < endInd) {
                        neighs.add(t);
                    }
                }
            } else {
                int startInd = shellStartIndices.get(dist);
                for (GrouphVertex t : getNeighborsOf(s)) {
                    if (getIndexOf(t) >= startInd) {
                        neighs.add(t);
                    }
                }
            }
        }
        return neighs;
    }

    @Override
    public Collection<GrouphVertex> getNeighborsInPreviousShell(GrouphVertex s) {
        int d = shellStartIndices.get(getDistanceFromTheRoot(s));
        Set<GrouphVertex> neighs = new HashSet<GrouphVertex>();
        if (d != -1) {
            Collection<GrouphVertex> neighborsOf = getNeighborsOf(s);
            for (GrouphVertex t : neighborsOf) {
                if (this.getIndexOf(t) < d) {
                    neighs.add(t);
                }
            }
        }
        return neighs;
    }

    @Override
    public boolean containsVertex(GrouphVertex s) {
        return s.getIndex() >= 0 && s.getIndex() <= colorKeyedNeighborSets.size();
    }

    @Override
    public boolean hasEdgeJoining(GrouphVertex src, GrouphVertex tgt) {
        return colorKeyedNeighborSets.get(src.getIndex()).containsTarget(tgt);
    }

    @Override
    public final int getNumberOfVertices() {
        return colorKeyedNeighborSets.size();
    }

    @Override
    public final int getNumberOfEdges() {
        return colorKeyedNeighborSets.size() * colorInvolution.size();
    }

    @Override
    public Set<GrouphVertex> getVertices() {
        return new HashSet<GrouphVertex>(getElements(0, colorKeyedNeighborSets.size()));
    }

    @Override
    public Collection<GrouphVertex> getNeighborsOf(GrouphVertex s) {
        return colorKeyedNeighborSets.get(s.getIndex()).getVertexList();
    }
}
