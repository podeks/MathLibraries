package tools;

import api.IndexedColorGraph;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author pdokos
 */
public class ICGNavigator<S, C> {
    
    protected IndexedColorGraph<S, C> graph;
    
    public ICGNavigator(IndexedColorGraph<S, C> graph) {
        this.graph= graph;
    }
    
    public List<C> getAShortestPathTo(S vert) {
        return getAShortestPathTo(vert, graph.getDistanceFromTheRoot(vert));
    }
    
    private List<C> getAShortestPathTo(S vert, int d) {
        
        if (d==0) {
            return new LinkedList<C>();
        }
        
        int shellStartIndex = graph.getShellStartIndex(d);
        Collection<S> neighbors = graph.getNeighborsOf(vert);
        Iterator<S> iterator = neighbors.iterator();
        
        S closerNeighbor=null;
        boolean found=false;
        while (iterator.hasNext() && !found) {
            S next = iterator.next();
            if (graph.getIndexOf(next)<shellStartIndex) {
                closerNeighbor = next;
                found=true;
            }
        }
        
        List<C> path;
        if (found) {
            path = getAShortestPathTo(closerNeighbor, d-1);
            path.add(graph.getEdgeColor(closerNeighbor, vert));
        } else {
            path = new LinkedList<C>();
        }

        return path;
    }
    
    
    
public List<C> getAShortestPathFrom(S vert) {
        return getAShortestPathFrom(vert, graph.getDistanceFromTheRoot(vert));
    }
    
    private List<C> getAShortestPathFrom(S vert, int d) {
        
        if (d==0) {
            return new LinkedList<C>();
        }
        
        int shellStartIndex = graph.getShellStartIndex(d);
        Collection<S> neighbors = graph.getNeighborsOf(vert);
        Iterator<S> iterator = neighbors.iterator();
        
        S closerNeighbor=null;
        boolean found=false;
        while (iterator.hasNext() && !found) {
            S next = iterator.next();
            if (graph.getIndexOf(next)<shellStartIndex) {
                closerNeighbor = next;
                found=true;
            }
        }
        
        List<C> path;
        if (found) {
            path = getAShortestPathFrom(closerNeighbor, d-1);
            path.add(0,graph.getEdgeColor(vert, closerNeighbor));
        } else {
            path = new LinkedList<C>();
        }

        return path;
    }
    
    public S getEndpointOfPath(List<C> path) {
        S endpoint=graph.getRoot();
        for(C c : path) {
            endpoint = graph.getNeighbor(endpoint, c);
        } 
        return endpoint;
    }
    
    public S getEndpointOfPath(S start, List<C> path) {
        S endpoint=start;
        for(C c : path) {
            endpoint = graph.getNeighbor(endpoint, c);
        } 
        return endpoint;
    }
    
    
}