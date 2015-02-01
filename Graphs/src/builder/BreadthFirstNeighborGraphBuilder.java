package builder;

import java.util.ArrayList;
import base.IndexedNavigableRootedNeighborGraphBase;
import api.ModifiableNeighborGraph;

/**
 * An extension of IndexedNavigableRootedNeighborGraphBase that implements the 
 * ModifiableNeighborGraph interface, designed for building graphs in a 
 * breadth-first fashion.
 * 
 * </p>
 * The addVertex method in this implementation is unsupported.  Instead, vertices are added
 * to the graph via the join method (with the exception of the root vertex, which is specified in the constructor).
 * The join method is implemented so as to enforce breadth-first construction (see the description).
 * <p>
 * 
 * </p>
 * The indexing of the vertex set is defined by the <em>insertion order</em> under the graph construction.
 * </p>
 * 
 * @author pdokos
 * 
 * @param <S> The vertex type on which the graph structure is defined.
 */
public class BreadthFirstNeighborGraphBuilder<S> extends IndexedNavigableRootedNeighborGraphBase<S> implements ModifiableNeighborGraph<S> {

    private int diameter;

    /**
     * Constructor for a BreadthFirstNeighborGraphBuilder with specified root 
     * vertex.  The index of the root vertex is set to 1.  
     * 
     * @param root any element of S.
     */
    public BreadthFirstNeighborGraphBuilder(S root) {
        super(root);
        attachVertex(root);
        diameter = 0;
    }

    /**
     * Constructor for a BreadthFirstNeighborGraphBuilder with specified root 
     * vertex, and which is of a uniform vertex degree 
     * (i.e.&#160a <em>regular</em> graph).
     * The index of the root vertex is set to 1.
     * 
     * @param root any element of S.
     * @param degree the designated valency of each vertex.
     */
    public BreadthFirstNeighborGraphBuilder(S root, int degree) {
        super(root, degree);
        attachVertex(root);
        diameter = 0;
    }

    /**
     * Constructor for a BreadthFirstNeighborGraphBuilder with specified root 
     * vertex, which has a known number of vertices, and 
     * is of uniform vertex degree (i.e.&#160a <em>regular</em> graph). 
     * By specifying numVerts and degree, this constructor preallocates the 
     * requisite memory for holding the graph.
     * The index of the root vertex is set to 1.
     * 
     * @param numVerts the designated number of vertices of the graph.
     * @param degree the designated valency of each vertex.
     */
    public BreadthFirstNeighborGraphBuilder(S root, int numVerts, int degree) {
        super(root, numVerts, degree);
        attachVertex(root);
        diameter = 0;
    }
    
    
    /**
     * Unsupported operation.
     * @param s any object of type S
     * @return no return value; UnsupportedOperationException is thrown.
     */
    @Override
    public boolean addVertex(S s) {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean attachVertex(S s) {
        if (!neighborsMap.containsKey(s)) {

            if (isRegular()) {
                neighborsMap.put(s, new ArrayList<S>(getDegree()));
            } else {
                neighborsMap.put(s, new ArrayList<S>());
            }

            indexing.add(s);
            return true;
        }
        return false;
    }

    /**
     * 
     * This method provides restricted graph building capabilities in such a way 
     * as to enforce breadth-first construction.
     * This is accomplished by 
     * imposing the requirement that <em>either</em>:</br>
     * &#160 &#160 (i) src and tgt are vertices of the graph whose distances from 
     * the root are within one of each other, </br>
     * -or-</br>
     * &#160&#160 (ii) tgt is not currently a vertex of the graph, and src is 
     * either of maximal distance from the root or one unit less than the 
     * current maximal distance.  In this case tgt is added as a new vertex of 
     * the graph, and has distance from the root one unit greater than that of the 
     * source.  The index of the new vertex is set to one more than the index of 
     * the most recently added vertex</br>
     * 
     * <p>
     * Note that in the second case above, the src vertex is typically of 
     * distance from the root one unit less than the current maximal distance.
     * A call to the join method with src being of maximal distance and tgt being 
     * a new vertex increases the maximal distance by one; this is done when 
     * initiating the addition of vertices in the next layer of the graph.
     * </p>
     * <p>
     * With these restrictions, the vertices of the graph are added in a 
     * breadth-first fashion, and the distance from the root of any vertex never 
     * changes from its initial distance.  Furthermore, under the indexing of 
     * the vertex set, the sets of vertices of any given distance from the root
     * consist of intervals. 
     * </p>
     * 
     * @param src any object of type S which is equal to a vertex in the graph.
     * @param tgt any object of type S.
     * @return true if src and tgt were successfully joined.
     */
    @Override
    public boolean join(S src, S tgt) {

        int srcDist = this.getDistanceFromTheRoot(src);
        int tgtDist = this.getDistanceFromTheRoot(tgt);

        if (srcDist >= diameter - 1) {

            if (tgtDist == -1) {
                if (srcDist == diameter - 1) {
                    attachVertex(tgt);
                    shellSizes.add(1 + shellSizes.remove(diameter));
                } else if (srcDist == diameter) {
                    attachVertex(tgt);
                    diameter++;
                    shellSizes.add(1);
                    shellStartVertices.add(tgt);
                }
            } else {
                int diff = tgtDist - srcDist;
                if (diff > 1 || diff < 0) {
                    return false;
                }
            }
            return neighborsMap.get(src).add(tgt) && neighborsMap.get(tgt).add(src);
        }
        return false;
    }
    
    /**
     * Sets the state of building to finished, and sorts the neighbor lists 
     * according to the indexing on the vertices. This cannot be undone, subsequent 
     * calls to addVertex and join return false and do not modify the graph.  If
     * the graph was specified as regular, or as having a certain number of 
     * vertices upon construction, then this method should only be called when 
     * these conditions are true.
     */
    @Override
    public void finish() {
        //sort target sets...
        super.setFinished();
    }
    
}
