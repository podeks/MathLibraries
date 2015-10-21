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
 * Conjunction of the NavigableRootedNeighborGraph and IndexedNeighborGraph interfaces,
 * designed as an API for graphs whose construction algorithms are of a breadth-first nature. 
 * The indexing is intended to be implemented so as to index the vertices 
 * according the insertion order by a builder class that implements the
 * ModifiableNeighborGraph interface.  
 * In this way, the set of vertices of any given path-distance from the root will
 * consist of an interval under the indexing.
 * This interface includes three global
 * graph methods for querying the following:</br>
 * &#160&#160&#160 (i) the path-distance of any given vertex from the root vertex.</br>
 * &#160&#160 (ii) the maximum path-distance from the root, among all vertices of the graph.</br>
 * &#160 (iii) the set of vertices of any given path-distance from the root vertex.
 * 
 * @author pdokos
 * @param <S> The vertex type on which the graph structure is defined.
 */
public interface IndexedNavigableRootedNeighborGraph<S> extends NavigableRootedNeighborGraph<S>, IndexedNeighborGraph<S> {
    
    /**
     * Returns the path-distance of s from the root.
     * 
     * @param s any element of type S.
     * 
     * @return the path-distance of s from the root.
     */
    public int getDistanceFromTheRoot(S s);
    
    /**
     * Returns the maximum path-distance from the root, among all vertices of the graph.
     * 
     * @return the maximum path-distance from the root, among all vertices of the graph.
     */
    public int getMaxDistanceFromRoot();
    
    /**
     * Returns an unmodifiable view of the list of vertices of path-distance d 
     * from the root, ordered by index.
     * 
     * @param d any integer
     * @return the list of vertices of path-distance d from the root, ordered by
     * index.
     */
    public List<S> getShell(int d);
    
}
