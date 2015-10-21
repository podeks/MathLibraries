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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author pdokos
 */
public class ColorCorrespondence<S, C> {
    private Map<C, S> colorKeyedElts;
    private Map<S, C> coloredElts;

    public ColorCorrespondence(int size) {
        colorKeyedElts = new HashMap<C, S>(size + 1, 1.0f);
        coloredElts = new HashMap<S, C>(size + 1, 1.0f);
    }

    public void set(C color, S neighbor) {
        colorKeyedElts.put(color, neighbor);
        coloredElts.put(neighbor, color);
    }

    public S getTarget(C color) {
        return colorKeyedElts.get(color);
    }

    public C getColor(S target) {
        return coloredElts.get(target);
    }

    public Set<C> getColors() {
        return Collections.unmodifiableSet(colorKeyedElts.keySet());
    }

    public Set<S> getUncoloredElts() {
        return Collections.unmodifiableSet(coloredElts.keySet());
    }

    public boolean containsColor(C c) {
        return colorKeyedElts.keySet().contains(c);
    }

    public boolean contains(S s) {
        return coloredElts.keySet().contains(s);
    }
    
}
