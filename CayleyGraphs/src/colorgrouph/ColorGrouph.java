package colorgrouph;

import api.IndexedColorGraph;
import colorgrouph.ColorGrouphBase.GrouphVertex;
import java.util.Collection;
import java.util.Set;

/**
 *
 * @author pdokos
 */
public interface ColorGrouph extends IndexedColorGraph<GrouphVertex, Integer> {
    
    public void createShortestPathStore(Collection<GrouphVertex> verts);
    
    public void clear();
    
    @Override
    public GrouphVertex getNeighbor(GrouphVertex vertex, Integer color);
    
    /*
     * Return null if src and tgt are not edjacent
     */
    @Override
    public Integer getEdgeColor(GrouphVertex src, GrouphVertex tgt);
    
    @Override
    public Integer getInverseColor(Integer c);
    
    @Override
    public Set<Integer> getColorSet();
    
    @Override
    public int getShellStartIndex(int d);
    
}
