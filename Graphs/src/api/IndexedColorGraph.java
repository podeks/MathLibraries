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