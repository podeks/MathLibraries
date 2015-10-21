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
package builder;

import api.ModifiableColorGraph;
import base.IndexedColorGraphBase;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pdokos
 */
public class IndexedColorGraphBuilder<S, C> extends IndexedColorGraphBase<S, C> implements ModifiableColorGraph<S, C> {

    private int diameter;
    private int degree;
    
    public IndexedColorGraphBuilder(S root, int numverts, Map<C, C> colorInvolution) {
        super(root, numverts, colorInvolution);
        
        diameter=0;
        degree=colorInvolution.size();
        attachVertex(root);
    }
    
    public IndexedColorGraphBuilder(S root, Map<C, C> colorInvolution) {
        super(root, colorInvolution);
        
        diameter=0;
        degree=colorInvolution.size();
        attachVertex(root);
    }
    
    private boolean attachVertex(S s) {
        if (!this.containsVertex(s)) {
            Map<S, C> neighborList = new HashMap<S, C>(degree +1, 1.0f);
            //for (int i=0; i<degree; i++) {
            //    neighborList.add(null);
            //}
            
                colorKeyedNeighborSets.put(s, neighborList);//new ColorCorrespondence<S, C>(degree));

                indexedVertSet.add(s);

            return true;
        }
        return false;
    }
    
    @Override
    public boolean addVertex(S s) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean join(S src, S tgt, C color) {
        int srcDist = this.getDistanceFromTheRoot(src);
        int tgtDist = this.getDistanceFromTheRoot(tgt);

        if (srcDist >= diameter - 1) {

            if (tgtDist == -1) {
                if (srcDist == diameter - 1) {
                    attachVertex(tgt);
                    //shellSizes.add(1 + shellSizes.remove(diameter));
                } else if (srcDist == diameter) {
                    attachVertex(tgt);
                    diameter++;
                    //shellSizes.add(1);
                    shellStartIndices.add(this.getIndexOf(tgt));
                }
            } else {
                int diff = tgtDist - srcDist;
                if (diff > 1 || diff < 0) {
                    return false;
                }
            }
            colorKeyedNeighborSets.get(src).put(tgt, color);//.set(color, tgt);
            colorKeyedNeighborSets.get(tgt).put(src, getInverseColor(color));//.set(getInverseColor(color), src);
            
            return true;
        }
        return false;
    }

    @Override
    public void finish() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
