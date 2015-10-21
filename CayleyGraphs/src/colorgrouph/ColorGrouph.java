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
