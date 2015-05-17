package api;

import java.util.Set;

/**
 *
 * @author pdokos
 */
public interface IndexedColorGraph<S, C> extends IndexedNavigableRootedNeighborGraph<S> {
    
    
    
    public S getNeighbor(S vertex, C color);
    
    /*
     * Return null if src and tgt are not edjacent
     */
    public C getEdgeColor(S src, S tgt);
    
    public C getInverseColor(C c);
    
    public Set<C> getColorSet();
    
    public int getShellStartIndex(int d);
    
    
    
}