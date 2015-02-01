package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import api.IndexedNavigableRootedNeighborGraph;

/**
 * Extension of IndexedNeighborGraphBase&ltS&gt which serves as a base implementation
 * of the IndexedNavigableRootedNeighborGraph&ltS&gt interface.
 * 
 * <p>
 * The constructors for this class are 
 * designed to be called by the constructors of its subclasses, and ultimately the builder subclasses (ie those which implement the 
 * ModifiableNeighborGraph&ltS&gt interface). 
 * </p>
 * <p>
 * The setFinished() method, inherited from IndexedNeighborGraph&ltS&gt, is designed to be called within 
 * the implementation of the finish() method of the builder subclasses.
 * A call to this method method sets the building state of the graph to finished,
 * and sorts the neighbor lists according to the indexing of the vertices. 
 * </p>
 * 
 * This class has two protected fields, shellSizes and shellStartVertices, which
 * are used to implement the NavigableRootedNeighborGraph&ltS&gt interface.
 * Builder subclasses need to be implemented so as to:</br> 
 * (i) index the vertices of the graph in insertion order by appropriately modifying 
 * the indexing field, and </br>
 * (ii) modify shellSizes and shellStartVertices appropriately as the graph is being modified.
 * 
 * @author pdokos
 * 
 * @param <S> The vertex type on which the graph structure is defined.
 */
public class IndexedNavigableRootedNeighborGraphBase<S> extends IndexedNeighborGraphBase<S> 
                                             implements IndexedNavigableRootedNeighborGraph<S> {

    private S root;
    
    /**
     * List of sizes of consecutive radial shells.
     */
    protected List<Integer> shellSizes;
    
    /**
     * A List, over consecutive radial shells, of the first vertex in each shell (according to the indexing).
     */
    protected List<S> shellStartVertices;
    

    /**
     * Constructor for an IndexedNavigableRootedNeighborGraphBase. This constructor initializes the 
     * neighborsMap to a new HashMap&ltS, T&gt, initializes the Indexing&ltS&gt and
     * IndexComparator&ltS&gt, and also initializes the shellSizes and shellStartVertices Lists.
     * 
     * @param root the designated root vertex of the graph.
     */
    public IndexedNavigableRootedNeighborGraphBase(S root) {
        super();
        this.root = root;
        shellSizes = new ArrayList<Integer>();
        shellSizes.add(1);
        shellStartVertices = new ArrayList<S>();
        shellStartVertices.add(root);
    }
    
    /**
     * Constructor for an IndexedNavigableRootedNeighborGraphBase which is of uniform vertex degree 
     * (i.e.&#160a <em>regular</em> graph). This constructor initializes the 
     * neighborsMap to a new HashMap&ltS, T&gt, initializes the Indexing&ltS&gt 
     * and IndexComparator&ltS&gt, and also initializes the shellSizes and 
     * shellStartVertices Lists.
     * 
     * @param root the designated root vertex.
     * @param degree the designated valency of each vertex.
     */
    public IndexedNavigableRootedNeighborGraphBase(S root, int degree){
        super(degree);
        this.root = root;
        shellSizes = new ArrayList<Integer>();
        shellSizes.add(1);
        shellStartVertices = new ArrayList<S>();
        shellStartVertices.add(root);
    }
    
    /**
     * Constructor for an IndexedNavigableRootedNeighborGraphBase which has a 
     * known number of vertices and is of uniform vertex degree 
     * (i.e.&#160a <em>regular</em> graph). This constructor initializes the
     * neighborsMap to a new HashMap&ltS, T&gt with
     * initial capacity numVerts and load factor 1, and 
     * also initializes the Indexing&ltS&gt and IndexComparator&ltS&gt.
     * 
     * @param root the designated root vertex.
     * @param numVerts the designated number of vertices of the graph.
     * @param degree the designated valency of each vertex.
     */
    public IndexedNavigableRootedNeighborGraphBase(S root, int numVerts, int degree){
        super(numVerts, degree);
        this.root = root;
        shellSizes = new ArrayList<Integer>();
        shellSizes.add(1);
        shellStartVertices = new ArrayList<S>();
        shellStartVertices.add(root);
    }
    
    @Override
    public S getRoot() {
        return root;
    }
    
    @Override
    public int getDistanceFromTheRoot(S s) {
        
        if (neighborsMap.containsKey(s)) {
            int index = indexing.getIndexOf(s);
            int d=0;
            int i=0;
            for (int delta : shellSizes) {
                i += delta;
                if (i >= index)
                    return d;
                else d++;
            }
        }
        return -1;
    }
    

    private int getShellStartIndex(int d) {
        if (d>=0 && d<shellSizes.size()) {
            int index=1;
            for (int i=0; i<d; i++) {
                index += shellSizes.get(i);
            }
            return index;
        }
        return -1;
    }

    @Override
    public List<S> getNeighborsInNextShell(S s) {
        if (neighborsMap.containsKey(s)) {
            
            List <S> neighbors = neighborsMap.get(s);
            
            if (!isFinished()) {
                System.out.println("WHOA");
                Collections.sort(neighbors, indexComparator);
            }
            
            int dist = getDistanceFromTheRoot(s);
            
            int startIndex;
            if (dist==this.getMaxDistanceFromRoot()) {
                startIndex = neighbors.size();
            } else {
                S nextShellStartVertex = shellStartVertices.get(1 + dist);

                startIndex = Collections.binarySearch(neighbors, nextShellStartVertex, indexComparator);
                if (startIndex < 0) {
                    startIndex = -startIndex - 1;
                }
            }
            
            return Collections.unmodifiableList(neighbors.subList(startIndex, neighbors.size()));
        } 
        return null;
     }

    @Override
    public List<S> getNeighborsInSameShell(S s) {
        if (neighborsMap.containsKey(s)) {
            
            List <S> neighbors = neighborsMap.get(s);
            
            if (!isFinished()) {
                Collections.sort(neighbors, indexComparator);
            }
            
            int dist = getDistanceFromTheRoot(s);
            S sameShellStartVertex = shellStartVertices.get(dist);
            
            
            int startIndex = Collections.binarySearch(neighbors, sameShellStartVertex, indexComparator);
            if (startIndex<0)
                startIndex = -startIndex-1;
            
            int endIndex;
            
            if (dist==this.getMaxDistanceFromRoot()) {
                endIndex = neighbors.size();
            } else {
                S nextShellStartVertex = shellStartVertices.get(1 + dist);

                endIndex = Collections.binarySearch(neighbors, nextShellStartVertex, indexComparator);
                if (endIndex < 0) {
                    endIndex = -endIndex - 1;
                }
            }

            return Collections.unmodifiableList(neighbors.subList(startIndex, endIndex));

        } 
        return null;
    }

    @Override
    public List<S> getNeighborsInPreviousShell(S s) {
        if (neighborsMap.containsKey(s)) {
            
            List <S> neighbors = neighborsMap.get(s);
            
            if (!isFinished()) {
                Collections.sort(neighbors, indexComparator);
            }
            
            S sameShellStartVertex = shellStartVertices.get(getDistanceFromTheRoot(s));
            
            int endIndex = Collections.binarySearch(neighbors, sameShellStartVertex, indexComparator);
            if (endIndex<0)
                endIndex = -endIndex-1;
            
            return Collections.unmodifiableList(neighbors.subList(0, endIndex));

        } 
        return null;    
    }

    @Override
    public List<S> getShell(int d) {
        
        if (d>=0 && d<shellSizes.size()) {
            int shellStartIndex = getShellStartIndex(d);
            int shellEndIndex;
            
            if (d<shellSizes.size() - 1) 
                shellEndIndex = getShellStartIndex(d+1)-1;
            else
                shellEndIndex = this.getNumberOfVertices();
            
            return getElements(shellStartIndex, shellEndIndex);
        }
        return new ArrayList<S>();
        
    }

    @Override
    public int getMaxDistanceFromRoot() {
        return shellSizes.size()-1;
    }

    
}
