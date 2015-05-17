package base;

import api.IndexedColorGraph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author pdokos
 */
public class IndexedColorGraphBase<S, C> implements IndexedColorGraph<S, C> {

    protected S root;
    //protected Map<S, ColorCorrespondence<S, C>> colorKeyedNeighborSets;
    protected Map<S, Map<S, C>> colorKeyedNeighborSets;
    protected IndexedSet<S> indexedVertSet;
    protected List<Integer> shellStartIndices;
    protected Map<C, C> colorInvolution;
    protected ColorCorrespondence<Integer, C> colorIndices;
    //private ICGNavigator<S, C> navigator;

    public IndexedColorGraphBase(S root, int numverts, Map<C, C> colorInvolution) {
        this.root = root;
        colorKeyedNeighborSets = new HashMap<S, Map<S, C>>(numverts+1, 1.0f);//HashMap<S, Map<S, C>>(numverts + 1, 1.0f); //check loadfactor stuff...
        indexedVertSet = new IndexedSet<S>(numverts);
        shellStartIndices = new ArrayList<Integer>();
        shellStartIndices.add(0);
        this.colorInvolution = colorInvolution;
        colorIndices = new ColorCorrespondence<Integer, C>(colorInvolution.size());
        initColorIndices();
        //navigator = new ICGNavigator<S, C>(this);
    }

    public IndexedColorGraphBase(S root, Map<C, C> colorInvolution) {
        this.root = root;
        colorKeyedNeighborSets = new HashMap<S, Map<S, C>>();
        indexedVertSet = new IndexedSet<S>();
        shellStartIndices = new ArrayList<Integer>();
        shellStartIndices.add(0);
        this.colorInvolution = colorInvolution;
        colorIndices = new ColorCorrespondence<Integer, C>(colorInvolution.size());
        initColorIndices();
        //navigator = new ICGNavigator<S, C>(this);
    }

    private void initColorIndices() {
        int i = 0;
        for (C c : colorInvolution.keySet()) {
            colorIndices.set(c, i);
            i++;
        }
    }

    @Override
    public S getNeighbor(S vertex, C color) {
        //return colorKeyedNeighborSets.get(vertex).getTarget(color);
        Map<S, C> neighs = colorKeyedNeighborSets.get(vertex);
        for (S s : neighs.keySet()) {
            if (neighs.get(s).equals(color)) {
                return s;
            }
        }
        return null;
        //return colorKeyedNeighborSets.get(vertex).get(colorIndices.getTarget(color));//.getTarget(color);
    }

    @Override
    public C getEdgeColor(S src, S tgt) {
        //return colorKeyedNeighborSets.get(src).getColor(tgt);
        return colorKeyedNeighborSets.get(src).get(tgt);//colorIndices.getColor();
    }

    @Override
    public C getInverseColor(C c) {
        return colorInvolution.get(c);
    }

    @Override
    public S getRoot() {
        return root;
    }

    @Override
    public Collection<S> getNeighborsInNextShell(S s) {
        int dist = getDistanceFromTheRoot(s);
        Set<S> neighs = new HashSet<S>();
        if (dist < getMaxDistanceFromRoot() && dist != -1) {
            int ind = shellStartIndices.get(dist + 1);
            for (S t : colorKeyedNeighborSets.get(s).keySet()) {//.getUncoloredElts()) {
                if (this.getIndexOf(t) >= ind) {
                    neighs.add(t);
                }
            }
        }
        return neighs;
    }

    @Override
    public Collection<S> getNeighborsInSameShell(S s) {
        int dist = getDistanceFromTheRoot(s);
        Set<S> neighs = new HashSet<S>();
        if (dist != -1) {
            if (dist < getMaxDistanceFromRoot()) {
                int startInd = shellStartIndices.get(dist);
                int endInd = shellStartIndices.get(dist + 1);
                for (S t : colorKeyedNeighborSets.get(s).keySet()) {//.getUncoloredElts()) {
                    if (getIndexOf(t) >= startInd && getIndexOf(t) < endInd) {
                        neighs.add(t);
                    }
                }
            } else {
                int startInd = shellStartIndices.get(dist);
                for (S t : colorKeyedNeighborSets.get(s).keySet()) {//.getUncoloredElts()) {
                    if (getIndexOf(t) >= startInd) {
                        neighs.add(t);
                    }
                }
            }
        }
        return neighs;
    }

    @Override
    public Collection<S> getNeighborsInPreviousShell(S s) {
        int d = shellStartIndices.get(getDistanceFromTheRoot(s));
        Set<S> neighs = new HashSet<S>();
        if (d != -1) {
            for (S t : colorKeyedNeighborSets.get(s).keySet()) {//.getUncoloredElts()) {
                if (this.getIndexOf(t) < d) {
                    neighs.add(t);
                }
            }
        }
        return neighs;
    }

    @Override
    public boolean containsVertex(S s) {
        return colorKeyedNeighborSets.containsKey(s);
    }

    @Override
    public boolean hasEdgeJoining(S src, S tgt) {
        return colorKeyedNeighborSets.get(src).containsKey(tgt);//.contains(tgt);//.getUncoloredElts()
    }

    @Override
    public int getNumberOfVertices() {
        return colorKeyedNeighborSets.size();
    }

    @Override
    public int getNumberOfEdges() {
        return colorKeyedNeighborSets.size() * colorInvolution.size();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<S> getVertices() {
        return Collections.unmodifiableSet(colorKeyedNeighborSets.keySet());
    }

    @Override
    public Collection<S> getNeighborsOf(S s) {
        return colorKeyedNeighborSets.get(s).keySet();//.getUncoloredElts();
    }

    @Override
    public int getDistanceFromTheRoot(S s) {
        if (this.containsVertex(s)) {
            int index = getIndexOf(s);
            int d = 1;
            while (d < shellStartIndices.size()) {
                if (shellStartIndices.get(d) > index) {
                    return d - 1;
                }
                d++;
            }
            return d - 1;
        }
        return -1;
    }

    @Override
    public int getMaxDistanceFromRoot() {
        return shellStartIndices.size() - 1;
    }

    @Override
    public List<S> getShell(int d) {
        if (d >= 0) {
            if (d < shellStartIndices.size() - 1) {
                return getElements(shellStartIndices.get(d), shellStartIndices.get(d + 1));
            }
            if (d == shellStartIndices.size() - 1) {
                return getElements(shellStartIndices.get(d), indexedVertSet.size());
            }
        }
        return new ArrayList<S>();
    }

    @Override
    public S getElement(int i) {
        return indexedVertSet.get(i);
    }

    @Override
    public int getIndexOf(S t) {
        return indexedVertSet.getIndex(t);
    }

    @Override
    public List<S> getElements(int minIncl, int maxExcl) {
        return Collections.unmodifiableList(indexedVertSet.subList(minIncl, maxExcl));
    }

    @Override
    public Set<C> getColorSet() {
        return colorInvolution.keySet();
    }

    @Override
    public int getShellStartIndex(int d) {
        return shellStartIndices.get(d);
    }

    protected class IndexedSet<S> extends ArrayList<S> {

        Map<S, Integer> indexing;

        public IndexedSet() {
            super();
            indexing = new HashMap<S, Integer>();
        }

        public IndexedSet(int initialCapacity) {
            super(initialCapacity);
            indexing = new HashMap<S, Integer>(initialCapacity + 1, 1.0f);
        }

        public int getIndex(S s) {
            if (indexing.containsKey(s)) {
                return indexing.get(s);
            }
            return -1;
        }

        @Override
        public boolean add(S s) {
            if (super.add(s)) {
                indexing.put(s, size() - 1);
                //System.out.println((size() - 1)); //s.toString() + " : " + 
                return true;
            }
            return false;
        }
    }
}
