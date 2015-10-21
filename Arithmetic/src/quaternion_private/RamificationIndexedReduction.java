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
package quaternion_private;

import quaternion.LipschitzQuaternion;
import quaternion.ReducedQuaternion;

/**
 *
 * @author pdokos
 */
public class RamificationIndexedReduction {
    ReducedQuaternion redQuat;
    Integer ramifIndex;
    
    public RamificationIndexedReduction(LipschitzQuaternion lipQuat, int q) {
        redQuat = (new ReducedQuaternion(lipQuat, q)).getFundamentalRepresentative();
        int[] type = lipQuat.getCoxeterOrbitClass();//IntegerThreeSpaceUtility.getCoxeterOrbitClass(imaginaryPart);
        int[] typeReduced = redQuat.getCoxeterOrbitClass();//IntegerThreeSpaceUtility.getCoxeterOrbitClass(imaginaryPartReduced);
        setRamificationIndex(type, typeReduced);
        if (redQuat.getEntry(0)==0 && lipQuat.getEntry(0) != 0)
            ramifIndex = ramifIndex*2;
    }
    
    public Integer getRamificationIndex() {
        return ramifIndex;
    }
    
    public ReducedQuaternion getReduction() {
        return redQuat;
    }
    
    private void setRamificationIndex(int[] type, int[] typeReduced) {
        if (type[0] == typeReduced[0]) {
            ramifIndex = 1;
        } else if (type[0] == 1) {
            if (typeReduced[0]==2) {
                ramifIndex = 2;
            } else if (typeReduced[0]==3) {
                if (typeReduced[1] == 1) {
                    ramifIndex = 8;
                } else if (typeReduced[1] == 2) {
                    ramifIndex = 4;
                } else if (typeReduced[1] == 3) {
                    ramifIndex = 6;
                }
            } else if (typeReduced[0]==4) {
                ramifIndex = 48;
            }
        } else if (type[0] == 2) {
            if (typeReduced[0] == 3) {
                if (typeReduced[1] == 1) {
                    ramifIndex = 4;
                } else if (typeReduced[1] == 2) {
                    ramifIndex = 2;
                } else if (typeReduced[1] == 3) {
                    ramifIndex = 3;
                }
            } else { //typeReduced[0]==4
                ramifIndex = 24;
            }
        } else if (type[0]==3 && typeReduced[0]==4) { 
            if (type[1] == 1) {
                ramifIndex = 6;
            } else if (type[1] == 2) {
                ramifIndex = 12;
            } else if (type[1] == 3) {
                ramifIndex = 8;
            }
        }
        
        
    }
    
}
