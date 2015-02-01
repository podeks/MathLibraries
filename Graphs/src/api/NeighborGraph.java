package api;

import java.util.Collection;
import java.util.Set;

/**
 * An API for graph data-structures which are both: 
 * (i) <em>undirected</em>, and (ii) <em>without multiple edges</em>, on objects
 * of vertex type S.
 * If there is a desired notion of equivalence between objects of the type 
 * passed as S, then it is required that the equals and hashcode methods be 
 * overridden accordingly in that type. Otherwise, the methods that accept 
 * S-parameters will not behave as expected.
 * 
 * <p>
 * This interface provides methods for querying the following:</br>
 * &#160&#160&#160(i) a view of the vertex set of the graph.</br>
 * &#160&#160&#160(ii) containment of a given object of type S in 
 * the vertex set, and the existence of a link between two given vertices.</br>
 * &#160&#160(iii) the sizes of the vertex and edge sets.</br>
 * &#160&#160(iv) the collection of neighbors of any given vertex. </br>
 * </p>
 * 
 * <p>
 * This interface is designed as an API for graph data-structures which do not 
 * utilize edge objects explicitly, but rather, keep track of neighbor 
 * collections for each vertex.
 * </p>
 * 
 * <p>
 * To be a legitimate implementation of a undirected graph without multiple edges,
 * the following conditions must be satisfied:</br>
 * &#160&#160&#160(i) getNeighborsOf(s) contains no repeated elements.</br>
 * &#160&#160&#160(ii) getNeighborsOf(s).contains(t) == getNeighborsOf(t).contains(s)</br>
 * </p>
 * 
 * <p>
 * Of course, the expression hasEdgeJoining(s, t) == getNeighborsOf(s).contains(t) must always 
 * be true.  The method hasEdgeJoining is typically implemented precisely in this
 * way, and it is included in the interface solely for convenience.
 * </p>
 * 
 * Note that despite condition (i) above, the return type of getNeighborsOf(s)
 * is a Collection&ltS&gt and not a Set&ltS&gt.  This allows for implementations 
 * to have the flexibility of returning the neighbor set as a 
 * List&ltS&gt (or perhaps some other type of Collection over &ltS&gt), when there is 
 * a meaningful ordering on the neighbor set.
 * 
 * 
 * @author pdokos
 * @param <S> The vertex type on which the graph structure is defined.
 */
public interface NeighborGraph<S> {

    /**
     * Returns true if the vertex set of the graph contains the given element of
     * S.
     * 
     * @param s any element of type S.
     * @return true if the vertex set of the graph contains s.
     */
    public boolean containsVertex(S s);
    
    /**
     * Returns true if src and tgt are neighbors.  
     * 
     * @param src any vertex
     * @param tgt any vertex
     * @return true if src and tgt are neighbors.
     */
    public boolean hasEdgeJoining(S src, S tgt);

    /**
     * Returns the size of the vertex set of the graph.
     * 
     * @return the size of the vertex set of the graph.
     */
    public int getNumberOfVertices();
    
    /**
     * Returns the number of undirected edges of the graph.
     * 
     * @return the number of undirected edges of the graph.
     */
    public int getNumberOfEdges();
    
    /**
     * Returns an unmodifiable view of the vertex set of the graph.
     * 
     * @return an unmodifiable view of the vertex set of the graph
     */
    public Set<S> getVertices();
    
    /**
     * Returns an unmodifiable view of the collection of neighbors of the given 
     * vertex.
     * 
     * @param s any element of type S.
     * @return an unmodifiable view of the of the collection of neighbors of s.
     */
    public Collection<S> getNeighborsOf(S s);

}
