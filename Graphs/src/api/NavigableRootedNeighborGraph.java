package api;

import java.util.Collection;

/**
 * Extension of the NeighborGraph&ltS&gt API for <em>rooted</em> connected neighbor graphs in which one can query, for each vertex of the graph, 
 * the neighbors which are either closer, further, or of the same distance to 
 * the root vertex.  Note that it only makes sense to use this interface for graphs which are undirected
 * (a is a neighbor of b if and only if b is a neighbor of a) and connected.  
 * With the navigability features of this interface, one can find all shortest 
 * paths to the root vertex from any given vertex by recursively calling the 
 * method getNeighborsInPreviousShell until the root vertex is reached.
 * 
 * @author pdokos
 * @param <S> The vertex type on which the graph structure is defined.
 */
public interface NavigableRootedNeighborGraph<S> extends NeighborGraph<S> {

    // public static String[] edgeOrientations = {"NEXT", "SAME", "TANGENT"};
    
    /**
     * Returns the designated root vertex of the graph.
     * 
     * @return the root vertex of the graph.
     */
    public S getRoot();
    
    /**
     * Returns an unmodifiable view of the neighbors of s which are one unit farther from the root than 
     * s, if the graph contains s.
     * 
     * @param s an element of S
     * 
     * @return an unmodifiable view of the neighbors of s which are one unit farther from the root than 
     * s, if the graph contains s.
     */
    public Collection<S> getNeighborsInNextShell(S s);
    
    /**
     * Returns an unmodifiable view of the neighbors of s which are of the same distance from the root 
     * as s, if the graph contains s.
     * 
     * @param s an element of S
     * 
     * @return an unmodifiable view of the neighbors of s which are of the same distance from the root 
     * as s, if the graph contains s.
     */
    public Collection<S> getNeighborsInSameShell(S s);
    
    /**
     * Returns an unmodifiable view of the neighbors of s which have distance from the root one unit 
     * less than that of s, if the graph contains s.
     * 
     * @param s an element of S
     * 
     * @return an unmodifiable view of the neighbors of s which have distance from the root one unit 
     * less than that of s, if the graph contains s.
     */
    public Collection<S> getNeighborsInPreviousShell(S s);

}