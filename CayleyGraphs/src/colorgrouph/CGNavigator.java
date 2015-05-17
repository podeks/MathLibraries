package colorgrouph;

import api.IndexedColorGraph;
import colorgrouph.ColorGrouphBase.GrouphVertex;
import tools.ICGNavigator;

/**
 *
 * @author pdokos
 */
public class CGNavigator extends ICGNavigator<GrouphVertex, Integer> {
    
    public CGNavigator(IndexedColorGraph<GrouphVertex, Integer> graph) {
        super(graph);
    }
    
//    private ColorGrouph graph;
//    
//    public CGNavigator(ColorGrouph graph) {
//        this.graph= graph;
//    }
//    
////    public List<C> getAShortestPathTo(GrouphVertex vert) {
////        List<C> path = new LinkedList<C>();
////        graph.getNeighborsInPreviousShell(vert);
////        Collection<GrouphVertex> neighborsOf = graph.getNeighborsOf(vert);
////        //graph.
////        //path.add(0, );
////        return path;
////    }
////    
////    public List<C> getAShortestPathFrom(GrouphVertex vert) {
////        List<C> path = new LinkedList<C>();
////        graph.getNeighborsInPreviousShell(vert);
////        //graph.
////        //path.add(0, );
////        return path;
////    }
//    
//    public List<Integer> getAShortestPathTo(GrouphVertex vert) {
//        return getAShortestPathTo(vert, graph.getDistanceFromTheRoot(vert));
//    }
//    
//    private List<Integer> getAShortestPathTo(GrouphVertex vert, int d) {
//        
//        if (d==0) {
//            return new LinkedList<Integer>();
//        }
//        
//        int shellStartIndex = graph.getShellStartIndex(d);
//        Collection<GrouphVertex> neighbors = graph.getNeighborsOf(vert);
//        Iterator<GrouphVertex> iterator = neighbors.iterator();
//        
//        GrouphVertex closerNeighbor=null;
//        boolean found=false;
//        while (iterator.hasNext() && !found) {
//            GrouphVertex next = iterator.next();
//            if (graph.getIndexOf(next)<shellStartIndex) {
//                closerNeighbor = next;
//                found=true;
//            }
//        }
//        
//        List<Integer> path;
//        if (found) {
//            path = getAShortestPathTo(closerNeighbor, d-1);
//            path.add(graph.getEdgeColor(closerNeighbor, vert));
//        } else {
//            path = new LinkedList<Integer>();
//        }
//
//        return path;
//    }
//    
//    
//    
//public List<Integer> getAShortestPathFrom(GrouphVertex vert) {
//        return getAShortestPathFrom(vert, graph.getDistanceFromTheRoot(vert));
//    }
//    
//    private List<Integer> getAShortestPathFrom(GrouphVertex vert, int d) {
//        
//        if (d==0) {
//            return new LinkedList<Integer>();
//        }
//        
//        int shellStartIndex = graph.getShellStartIndex(d);
//        Collection<GrouphVertex> neighbors = graph.getNeighborsOf(vert);
//        Iterator<GrouphVertex> iterator = neighbors.iterator();
//        
//        GrouphVertex closerNeighbor=null;
//        boolean found=false;
//        while (iterator.hasNext() && !found) {
//            GrouphVertex next = iterator.next();
//            if (graph.getIndexOf(next)<shellStartIndex) {
//                closerNeighbor = next;
//                found=true;
//            }
//        }
//        
//        List<Integer> path;
//        if (found) {
//            path = getAShortestPathFrom(closerNeighbor, d-1);
//            path.add(0,graph.getEdgeColor(vert, closerNeighbor));
//        } else {
//            path = new LinkedList<Integer>();
//        }
//
//        return path;
//    }
//    
//    public GrouphVertex getEndpointOfPath(List<Integer> path) {
//        GrouphVertex endpoint=graph.getRoot();
//        GrouphVertex next;
//        for(Integer c : path) {
//            next = graph.getNeighbor(endpoint, c);
//            endpoint=next;
//        } 
//        return endpoint;
//    }
//    
//    public GrouphVertex getEndpointOfPath(GrouphVertex start, List<Integer> path) {
//        GrouphVertex endpoint=start;
//        GrouphVertex next;
//        for(Integer c : path) {
//            next = graph.getNeighbor(endpoint, c);
//            endpoint=next;
//        } 
//        return endpoint;
//    }
    
    
}
