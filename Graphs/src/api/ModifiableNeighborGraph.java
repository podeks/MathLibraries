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

/**
 * Interface for NeighborGraph builder classes that includes methods for adding vertices
 * to the graph, and for joining pairs of vertices.
 * 
 * @author pdokos
 * @param <S> The vertex type on which the graph structure is defined.
 */
public interface ModifiableNeighborGraph<S> extends NeighborGraph<S>{
    
    /**
     * Method for adding an object of type S to the vertex set of the graph.
     * 
     * @param s any element of S.
     * @return true if the graph does not already contain a vertex equivalent to 
     * s, and s was successfully added to the vertex set of the graph.
     */
    public boolean addVertex(S s);
    
    /**
     * Method for joining two vertices of the graph.
     * 
     * @param src any element of S.
     * @param tgt any element of S.
     * 
     * @return true if a new edge was successfully added to the graph.
     */
    public boolean join(S src, S tgt);
    
    /**
     * Method for signaling the completion of graph construction, and for executing 
     * any post-construction processes.  
     */
    public void finish();
    //Subsequent calls to addVertex and join
     //should not modify the graph, and return a value of false.
    
}
