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
package builder;

import base.IndexedNeighborGraphBase;
import java.util.ArrayList;
import api.ModifiableNeighborGraph;

/**
 * An extension of IndexedNeighborGraphBase that implements the ModifiableNeighborGraph interface.  
 * The indexing of the vertex set is defined by the <em>insertion order</em> of the graph construction.
 * 
 * @author pdokos
 * 
 * @param <S> The vertex type on which the graph structure is defined.
 */
public class IndexedNeighborGraphBuilder<S> extends IndexedNeighborGraphBase<S> implements ModifiableNeighborGraph<S>{
    
    /**
     * Constructor for an IndexedNeighborGraphBuilder.
     */
    public IndexedNeighborGraphBuilder() {
        super();
    }
    
    /**
     * Constructor for an IndexedNeighborGraphBuilder which is of a uniform vertex degree 
     * (i.e.&#160a <em>regular</em> graph).
     * 
     * @param degree the designated valency of each vertex.
     */
    public IndexedNeighborGraphBuilder(int degree) {
        super(degree);
    }
    
    /**
     * Constructor for an IndexedNeighborGraphBuilder which has a known number of vertices and 
     * is of uniform vertex degree (i.e.&#160a <em>regular</em> graph). By specifying
     * numVerts and degree, this constructor preallocates the requisite memory for holding the graph.
     * 
     * @param numVerts the designated number of vertices of the graph.
     * @param degree the designated valency of each vertex.
     */
    public IndexedNeighborGraphBuilder(int numVerts, int degree) {
        super(numVerts, degree);
    }
    
    /**
     * Adds the object s to the vertex set of the graph, with the neighbor
     * set of s initialized as empty; the index of the s is set to 1 more than 
     * that of the previously added vertex, with the index of the first added 
     * vertex set to 1.  Subsequent calls to the containsVertex method of the 
     * NeighborGraph interface return true when evaluated on an object 
     * equivalent to s under the equals method of S.
     * 
     * @param s any object of type S.
     * @return true if the graph does not already contain s, and s was successfully
     * added to the vertex set of the graph.
     */
    @Override
    public boolean addVertex(S s) {
        
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
     * Joins the vertices src and tgt within the graph.  
     * These vertices are added to the vertex set of the graph if they are not
     * already members.
     * If the vertices were successfully joined, then subsequent calls to the 
     * method hasEdgeJoining(src, tgt) and hasEdgeJoining(tgt, src) return true.
     * 
     * @param src any object of type S.
     * @param tgt any object of type S.
     * @return true if src and tgt were successfully joined.
     */
    @Override
    public boolean join(S src, S tgt) {

        addVertex(src);
        addVertex(tgt);
        
        return neighborsMap.get(src).add(tgt) && neighborsMap.get(tgt).add(src);
        
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
        super.setFinished();
    }
    
}
