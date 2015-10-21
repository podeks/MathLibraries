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
package base;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import api.NeighborGraph;

/**
 * A base implementation of the NeighborGraph&ltS&gt interface, founded upon the 
 * java.util.Collection and java.util.Map interfaces by utilizing a HashMap&ltS, 
 * T&gt to maintain neighbor collections.
 * <p>
 * Subclasses of this class can specify T as some specific subtype of Collection&ltS&gt, and
 * then override the getNeighborsOf(S s) method to return an unmodifiable view 
 * of the collection of that type using the java.util.Collections class.
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
 * </p>
 * 
 * @author pdokos
 * 
 * @param <S> The vertex type on which the graph structure is defined.  
 * 
 * @param <T> The subtype (e.g Set&ltS&gt, or List&ltS&gt) of Collection&ltS&gt used to maintain the collection 
 * of neighbors for each vertex.
 */
public class NeighborGraphBase<S, T extends Collection<S>> implements NeighborGraph<S> {
    
    /**
     * Map used to maintain neighbor collections for each of the vertices.
     */
    protected Map<S, T> neighborsMap;
    
    private boolean regular;
    private int degree;
    
    private boolean finished;
    
    /**
     * Constructor for a NeighborGraphBase. This constructor initializes the 
     * neighborsMap to a new HashMap&ltS, T&gt.
     */
    public NeighborGraphBase(){
        neighborsMap = new HashMap<S, T>();
        regular = false;
        finished=false;
    }
    
    /**
     * Constructor for a NeighborGraphBase which is of a uniform vertex degree 
     * (i.e.&#160a <em>regular</em> graph). This constructor initializes the 
     * neighborsMap to a new HashMap&ltS, T&gt.
     * 
     * @param degree the designated valency of each vertex.
     */
    public NeighborGraphBase(int degree){
        neighborsMap = new HashMap<S, T>();
        regular = true;
        this.degree = degree;
        finished=false;
    }
    
    /**
     * Constructor for a NeighborGraphBase which has a known number of vertices and 
     * is of uniform vertex degree 
     * (i.e.&#160a <em>regular</em> graph). This constructor initializes the neighborsMap to a new HashMap&ltS, T&gt with
     * initial capacity numVerts and load factor 1.
     * 
     * @param numVerts the designated number of vertices of the graph.
     * @param degree the designated valency of each vertex.
     */
    public NeighborGraphBase(int numVerts, int degree){
        neighborsMap = new HashMap<S, T>(numVerts, 1);
        regular = true;
        this.degree = degree;
        finished=false;
    }
    
    /**
     * Sets the state of construction to finished. This action cannot be undone, and 
     * subsequent calls to isFinished() return true.  This method is intended 
     * as a helper for builder subclasses (those that implement the 
     * ModifiableNeighborGraph&ltS&gt interface) in situations where one may need 
     * alternative implementations of some of the methods depending on the 
     * state of construction.  Builder subclasses should call this method within
     * their implementations of the finish() method.  If the graph was specified as regular, or as 
     * having a certain number of vertices upon construction, then this method
     * should only be called when these conditions are true.
     */
    protected void setFinished() {
        finished = true;
    }
    
    /**
     * Returns the state of construction of the graph.  This method is intended 
     * as a helper for builder subclasses in situations where one may need 
     * alternative implementations of some of the methods depending on the 
     * state of construction.
     * 
     * @return false until the setFinished() method is called, after which this 
     * method always returns true.
     */
    protected boolean isFinished() {
        return finished;
    }
    
    /**
     * Returns true if the graph was specified as having a fixed degree upon
     * construction.
     * 
     * @return true if the graph was specified as having a fixed degree upon
     * construction.
     */
    public boolean isRegular() {
        return regular;
    }
    
    /**
     * Returns the degree of the graph, if it was specified upon construction;
     * otherwise returns a value of -1.
     * 
     * @return the degree of the graph, if it was specified upon construction;
     * otherwise returns a value of -1.
     */
    public int getDegree() {
        if (regular)
            return degree;
        return -1;
    }
    
    

    @Override
    public boolean containsVertex(S s) {
        return neighborsMap.containsKey(s);
    }

    @Override
    public boolean hasEdgeJoining(S src, S tgt) {
        return getNeighborsOf(src).contains(tgt);
    }
    
    @Override
    public int getNumberOfVertices() {
        return neighborsMap.keySet().size();
    }

    @Override
    public int getNumberOfEdges() {
        int numEdges=0;
        
        if (!isRegular() || !isFinished()) {
            for (S s : neighborsMap.keySet()) {
                numEdges += neighborsMap.get(s).size();
            }
        } else {
            numEdges = neighborsMap.keySet().size() * degree;
        }
        
        return numEdges;
    }

    @Override
    public Set<S> getVertices() {
        return Collections.unmodifiableSet(neighborsMap.keySet());
    }
    
    /**
     * Returns an unmodifiable view of the collection of neighbors of the given 
     * vertex.  Subclasses that have the type parameter T specified as some subtype of Collection&ltS&gt can 
     * override this method to return a collection of that type.
     * 
     * @param s any element of type S.
     * @return an unmodifiable view of the of the collection of neighbors of s.
     */
    @Override
    public Collection<S> getNeighborsOf(S s) {
        return Collections.unmodifiableCollection(neighborsMap.get(s));
    }    
    
} 
