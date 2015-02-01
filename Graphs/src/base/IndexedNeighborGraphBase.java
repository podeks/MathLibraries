package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import api.IndexedNeighborGraph;

/**
 * Extension of NeighborGraphBase&ltS, List&ltS&gt&gt which serves as a base implementation of the
 * IndexedNeighborGraph&ltS&gt interface.
 * 
 * <p>
 * Note that by having the second type parameter of NeighborGraphBase set to 
 * List&ltS&gt, the neighbor collections are then maintained as Lists.  Accordingly,
 * the getNeighborsOf(S s) method has been overridden to return a List&ltS&gt.
 * </p>
 * 
 * <p>
 * The constructors for this class are 
 * designed to be called by the constructors of its subclasses, and ultimately the builder subclasses (ie those which implement the 
 * ModifiableNeighborGraph&ltS&gt interface). 
 * </p>
 * <p>
 * The setFinished() method is designed to be called within 
 * the implementation of the finish() method of the builder subclasses.
 * A call to this method method sets the building state of the graph to finished,
 * and sorts the neighbor lists according to the indexing of the vertices. 
 * </p>
 * 
 * 
 * The indexing functionality 
 * of this class is delegated to the two nested classes Indexing&ltT&gt and 
 * IndexComparator&ltT&gt.
 * 
 * 
 * @author pdokos
 * 
 * @param <S> The vertex type on which the graph structure is defined.
 */
public class IndexedNeighborGraphBase<S> extends NeighborGraphBase<S, List<S>> implements IndexedNeighborGraph<S>{

    /**
     * Instance of an Indexing&ltS&gt for maintaining the indexing of the vertex set.
     */
    protected Indexing<S> indexing;
    
    /**
     * Instance of an IndexComparator&ltT&gt for enabling the usage of the 
     * java.util.Collections sorting and searching utilities.
     */
    protected Comparator<S> indexComparator; 
        
    /**
     * Implementation of the java.util.Comparator&ltT&gt interface based on 
     * Indexing&ltT&gt. This enables the usage of the java.util.Collections
     * sorting and searching utilities.
     * 
     * @param <T> the type whose instances are being indexed.
     */
    protected class IndexComparator<T> implements Comparator<T> {

        private Indexing<T> indices;
        
        public IndexComparator(Indexing<T> indices) {
            this.indices = indices;
        }
        
        @Override
        public int compare(T s, T t) {
            return Integer.signum(indices.getIndexOf(s) - indices.getIndexOf(t));
        }
        
    }
    
    /**
     * A nested class of IndexedNeighborGraphBase, serving to index the vertex set of the graph with the natural
     * numbers (starting at 1) by maintaining
     * a Map&ltT, Integer&gt together with an ArrayList&ltT&gt.
     * 
     * @param <T> the type whose instances are being indexed. 
     */
    protected class Indexing<T> {
        private Map<T, Integer> indexing;
        private ArrayList<T> list;
        
        public Indexing() {
            indexing = new HashMap<T, Integer>();
            list = new ArrayList<T>();
        }
        
        public Indexing(int numVerts) {
            indexing = new HashMap<T, Integer>(numVerts);
            list = new ArrayList<T>(numVerts);
        }
        
        public void add(T t) {
            if (list.add(t)) 
                indexing.put(t, list.size());
        }
        
        public void clear() {
            indexing.clear();
            list.clear();
        }
        
        public T getElement(int i) {
            return list.get(i-1);
        }

        public int getIndexOf(T t) {
            return indexing.get(t);
        }
    
        public List<T> getElements(int minIncl, int maxExcl) {
            return Collections.unmodifiableList(list.subList(minIncl-1, maxExcl-1));
        }
        
        
        
    }
    
    /**
     * Constructor for an IndexedNeighborGraphBase. This constructor initializes the 
     * neighborsMap to a new HashMap&ltS, T&gt, and also initializes the Indexing&ltS&gt and
     * IndexComparator&ltS&gt.
     */
    public IndexedNeighborGraphBase(){
        super();
        indexing = new Indexing<S>();
        indexComparator = new IndexComparator<S>(indexing);
    }
    
    /**
     * Constructor for an IndexedNeighborGraphBase which is of uniform vertex degree 
     * (i.e.&#160a <em>regular</em> graph). This constructor initializes the 
     * neighborsMap to a new HashMap&ltS, T&gt, and 
     * also initializes the Indexing&ltS&gt and IndexComparator&ltS&gt.
     * 
     * @param degree the designated valency of each vertex.
     */
    public IndexedNeighborGraphBase(int degree){
        super(degree);
        indexing = new Indexing<S>();
        indexComparator = new IndexComparator<S>(indexing);
    }
    
    /**
     * Constructor for an IndexedNeighborGraphBase which has a known number of vertices and 
     * is of uniform vertex degree 
     * (i.e.&#160a <em>regular</em> graph). This constructor initializes the
     * neighborsMap to a new HashMap&ltS, T&gt with
     * initial capacity numVerts and load factor 1, and 
     * also initializes the Indexing&ltS&gt and IndexComparator&ltS&gt.
     * 
     * @param numVerts the designated number of vertices of the graph.
     * @param degree the designated valency of each vertex.
     */
    public IndexedNeighborGraphBase(int numVerts, int degree){
        super(numVerts, degree);
        indexing = new Indexing<S>(numVerts);
        indexComparator = new IndexComparator<S>(indexing);
    }
    
    /**
     * Sets the state of construction to finished, and sorts the neighbor lists
     * according to the indexing. This action cannot be undone, and 
     * subsequent calls to isFinished() return true.  Builder subclasses should call this method within
     * their implementations of the finish() method.  If the graph was specified as regular, or as 
     * having a certain number of vertices upon construction, then this method
     * should only be called when these conditions are true.
     */
    @Override
    protected void setFinished() {
        //Sort target Lists...
        for (Map.Entry<S, List<S>> e : neighborsMap.entrySet()) {
            Collections.sort(e.getValue(), indexComparator);
        }
        super.setFinished();
    }
    
    /**
     * Returns an unmodifiable view of the List&ltS&gt of neighbors of the given 
     * vertex.
     * 
     * @param s any element of type S.
     * @return an unmodifiable view of the of the List&ltS&gt of neighbors of s.
     */
    @Override
    public List<S> getNeighborsOf(S s) {
        return Collections.unmodifiableList(neighborsMap.get(s));
    }

    
    //Indexing methods:
    
    @Override
    public S getElement(int i) {
        return indexing.getElement(i);
    }

    @Override
    public int getIndexOf(S s) {
        return indexing.getIndexOf(s);
    }
    
    @Override
    public List<S> getElements(int minIncl, int maxIncl) {
        return indexing.getElements(minIncl, maxIncl+1);
    }    

    
}

