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
