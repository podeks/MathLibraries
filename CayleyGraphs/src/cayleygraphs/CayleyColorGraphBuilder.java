package cayleygraphs;

import api.Group;
import api.IndexedColorGraph;
import builder.IndexedColorGraphBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 *
 * @author pdokos
 */
public class CayleyColorGraphBuilder {

    public static <G extends Group<G>> IndexedColorGraph<G, G> buildCayleyColorGraph(Set<G> generatingSet, G root) {
        Map<G, G> colorInvolution = new HashMap<G, G>(generatingSet.size() + 1, 1.0f);
        for (G g : generatingSet) {
            colorInvolution.put(g, g.getInverse());
        }
        IndexedColorGraphBuilder<G, G> cayleyGraph = new IndexedColorGraphBuilder<G, G>(root, colorInvolution);

        List<G> currentShell = new ArrayList<G>();
        List<G> nextShell = new ArrayList<G>();
        currentShell.add(root);

        System.out.print("GENERATING CAYLEY GRAPH...   ");

        while (!currentShell.isEmpty()) {

            Iterator<G> iterator = currentShell.iterator();

            while (iterator.hasNext()) {

                G g = iterator.next();
                iterator.remove();

                for (G s : generatingSet) {
                    
                    //if (cayleyGraph.colorKeyedNeighborSets.get(g).containsColor(s)) {//get(cayleyGraph.colorIndices.getTarget(s))==null) {

                    if (true) {
                        G nextVertex = g.rightProductBy(s);                                          // Right here is the heart of it all!
                        G existingVertex;

                        if (!cayleyGraph.containsVertex(nextVertex)) {
                            nextShell.add(nextVertex);
                            //existingVertex = nextVertex;
                        } //else {
                            //Collection<G> verticesToCheck = cayleyGraph.getNeighborsOf(cayleyGraph.getNeighborsOf(nextVertex).iterator().next());
                            //existingVertex = getVertex(nextVertex, verticesToCheck);
                        //}
                        existingVertex = nextVertex;
                        if (!currentShell.contains(existingVertex)) {
                            if (!cayleyGraph.hasEdgeJoining(g, existingVertex)) {
                                cayleyGraph.join(g, existingVertex, s);
                            }
                        } else {
                            cayleyGraph.join(g, existingVertex, s);
                        }

                    }
                }
            }
            currentShell.addAll(nextShell);
            nextShell.clear();
        }

        System.out.println("DONE");

        return cayleyGraph;
    }

    /**
     * A general purpose method that, with a given empty
     * ModifiableNeighborGraph&ltG&gt, builds the connected component of the
     * Cayley graph of G with respect to generatingSet, based at the designated
     * root vertex.
     *
     * The graph is constructed in a breadth-first fashion.
     *
     * @param cayleyGraph an empty ModifiableNeighborGraph&ltG&gt into which the
     * Cayley graph is built.
     * @param generatingSet any Set&ltG&gt
     * @param root any element of G
     */
    public static <G extends Group<G>> IndexedColorGraph<G, G> buildCayleyColorGraphOld(Set<G> generatingSet, G root) {

        IndexedColorGraphBuilder<G, G> cayleyGraph = new IndexedColorGraphBuilder<G, G>(root, 0, null);

        Map<G, G> labelInvolution = new HashMap<G, G>();   //unnecessary once generatingSet is made a SymSet
        for (G g : generatingSet) {
            labelInvolution.put(g, g.getInverse());
        }

        int degree = generatingSet.size();
        Map<G, Set<G>> generatorsToTraverse = new LinkedHashMap<G, Set<G>>();

        generatorsToTraverse.put(root, new HashSet<G>(degree, 1));
        generatorsToTraverse.get(root).addAll(generatingSet);

        System.out.print("GENERATING CAYLEY GRAPH...   ");

        //int excess = 0;

        while (!generatorsToTraverse.isEmpty()) {

            Iterator<Map.Entry<G, Set<G>>> iterator = (new LinkedList<Map.Entry<G, Set<G>>>(generatorsToTraverse.entrySet())).iterator();
            generatorsToTraverse.clear();

            while (iterator.hasNext()) {

                Map.Entry<G, Set<G>> fiber = iterator.next();
                iterator.remove();

                G g = fiber.getKey();

                for (G s : fiber.getValue()) {

                    G nextVertex = g.rightProductBy(s);                                          // Right here is the heart of it all!
                    G existingVertex;

                    if (!cayleyGraph.containsVertex(nextVertex)) {
                        generatorsToTraverse.put(nextVertex, new HashSet<G>(generatingSet));
                        existingVertex = nextVertex;
                    } else {
                        Collection<G> verticesToCheck = cayleyGraph.getNeighborsOf(cayleyGraph.getNeighborsOf(nextVertex).iterator().next());
                        existingVertex = getVertex(nextVertex, verticesToCheck);
                    }
                    if (!generatorsToTraverse.containsKey(existingVertex)) {
                        if (!cayleyGraph.hasEdgeJoining(g, existingVertex)) {
                            cayleyGraph.join(g, existingVertex, s);
                        }
                    } else {
                        cayleyGraph.join(g, existingVertex, s);
                    }

                    if (generatorsToTraverse.containsKey(existingVertex)) {
                        generatorsToTraverse.get(existingVertex).remove(labelInvolution.get(s));
                    }

                }
            }
        }

        System.out.println("DONE");
        //System.out.println("EXCESS: " + excess);
        System.out.print("SORTING NEIGHBOR LISTS...    ");
        cayleyGraph.finish();
        System.out.println("DONE");
        return cayleyGraph;
    }

    private static <G extends Group<G>> G getVertex(G m, Collection<G> s) {

        for (G g : s) {
            if (g.equals(m)) {
                return g;
            }
        }
        return null;
    }
}
