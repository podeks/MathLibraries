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
package api;

import java.util.List;

/**
 * Extension of the NeighborGraph API for graphs whose vertices are indexed by
 * the the natural numbers 1, 2, ..., getNumberOfVertices(). 
 * 
 * @author pdokos
 * @param <S> The vertex type on which the graph structure is defined.
 */
public interface IndexedNeighborGraph<S> extends NeighborGraph<S> {
    
    /**
     * Returns the vertex indexed by i.
     * 
     * @param i an integer with value &lt= getNumVerts(). 
     * @return the vertex indexed by i.
     */
    public S getElement(int i);

    /**
     * Returns the index of the given vertex.
     * 
     * @param t An element of T which is contained in the graph.
     * @return the index of the vertex t.
     */
    public int getIndexOf(S t);
    
    /**
     * Returns an unmodifiable view of the List&ltS&gt of vertices in the 
     * interval [minIncl, maxExcl).
     * 
     * @param minIncl any integer
     * @param maxExcl any integer
     * @return an unmodifiable view of the List&ltS&gt of vertices in the interval [minIncl, maxExcl).  
     * Returns an empty list if minIncl &gt getNumVerts(), maxExcl &lt= minIncl, 
     * or maxExcl &lt=1
     */
    public List<S> getElements(int minIncl, int maxExcl);
    
}
