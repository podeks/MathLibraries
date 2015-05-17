package api;

/**
 *
 * @author pdokos
 */
public interface ModifiableColorGraph<S, C> extends NeighborGraph<S> {

    public boolean addVertex(S s);

    public boolean join(S src, S tgt, C color);

    public void finish();
}