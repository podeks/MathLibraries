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
package cayleygraphs;

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
import api.IndexedNavigableRootedNeighborGraph;
import api.IndexedNeighborGraph;
import api.ModifiableNeighborGraph;
import api.NeighborGraph;
import builder.BreadthFirstNeighborGraphBuilder;
import builder.IndexedNeighborGraphBuilder;
import builder.NeighborGraphBuilder;
import api.Group;

/**
 * This class provides capabilities similar to those of the CayleyGraphBuilder, but with
 * two additional features, namely (i) allowing for external progress listeners that 
 * receive updates on the construction process, and (ii) the ability to terminate the 
 * construction process.
 * 
 * <p>
 * Note that unlike the CayleyGraphBuilder class, the methods of this class are 
 * not static.  Rather, one instantiates an ObservableCayleyGraphBuilder object,
 * to which any desired ProgressListeners can be added via the 
 * addProgressListener method, and calls the Cayley graph creation methods from the object.
 * </p>
 * 
 * @author pdokos
 * @param <G> the group implementation
 */
public class ObservableCayleyGraphBuilder<G extends Group<G>> {

    private List<ProgressListener> listeners;
    private boolean terminated;

    /**
     * Listener interface for an ObservableCayleyGraphBuilder.
     * The update method of each listener of an ObservableCayleyGraphBuilder
     * object is called during the construction process for a graph,
     * updating listeners on the following three property values: "vertex_progress", 
     * "edge_progress", "post_status".  The updates are called on the vertex progress
     * at multiples of 100, and on the edge progress at multiples of 1000.
     * The "post_status" property is called after the completion of the
     * construction with a value of -1 before post-construction processes are
     * initiated, and 1 after they have completed.
     */
    public interface ProgressListener {

        /**
         * Performs a response to the call signal.
         * 
         * @param property one of the following three strings: "vertex_progress", 
         * "edge_progress", or "post_status"
         * @param size an integer which is: a multiple of 100 if property 
         * equals "vertex_progress", a multiple of 1000 if property equals 
         * "edge_progress", and +/-1 if property equals "post_status".
         */
        public void update(String property, int size);
    }

    /**
     * Adds the listener to this builder's ProgressListener list.
     * 
     * @param listener An implementation of the ProgressListener nested interface.
     */
    public void addProgressListener(ProgressListener listener) {
        listeners.add(listener);
    }

    /**
     * Instantiates a new ObservableCayleyGraphBuilder.
     */
    public ObservableCayleyGraphBuilder() {
        listeners = new ArrayList<ProgressListener>();
        terminated = false;
    }

    /**
     * Cancels this builder's graph construction process, if there is one in progress.
     */
    public void terminate() {
        terminated = true;
    }

    /**
     * This method produces a NeighborGraph&ltG&gt model for the connected component of the Cayley graph of G with 
     * respect to generatingSet, based at the designated root vertex.
     * 
     * <p>
     * A NeighborGraphBuilder is utilized as the underlying builder 
     * with which the model is produced.
     * </p>
     * 
     * @param generatingSet any Set&ltG&gt
     * @param root any element of G 
     * 
     * @return A NeighborGraph&ltG&gt model for the connected component of the Cayley graph of G with respect to 
     * genSet, based at the designated root vertex.
     */
    public NeighborGraph<G> getCayleyGraph(Set<G> generatingSet, G root) {
        ModifiableNeighborGraph<G> bigraphBase = new NeighborGraphBuilder<G>(generatingSet.size());
        buildCayleyGraph(bigraphBase, generatingSet, root);
        return bigraphBase;
    }

    /**
     * A method that can be used to build the connected component of the Cayley graph of G with 
     * respect to generatingSet, based at the designated root vertex, when it is
     * known that the subgroup of G generated by generatingSet has numVerts elements.
     * 
     * <p>
     * A NeighborGraphBuilder is utilized as the underlying builder 
     * with which the model is produced.
     * </p>
     * 
     * @param generatingSet any Set&ltG&gt
     * @param root any element of G
     * @param numVerts the number of elements in the subgroup generated by the
     * given generating set.  
     * 
     * @return A NeighborGraph&ltG&gt model for the connected component of the Cayley graph of G with respect to 
     * genSet, based at the designated root vertex.
     */
    public NeighborGraph<G> getCayleyGraph(Set<G> generatingSet, G root, int numVerts) {
        ModifiableNeighborGraph<G> bigraphBase = new NeighborGraphBuilder<G>(numVerts, generatingSet.size());//(25920,(short) 156);
        buildCayleyGraph(bigraphBase, generatingSet, root);
        return bigraphBase;
    }

    /**
     * This method produces an IndexedNeighborGraph&ltG&gt model for the connected component of the Cayley graph of G with 
     * respect to generatingSet, based at the designated root vertex, with indexing on the vertex set
     * defined by the insertion order under a breadth-first construction.
     * 
     * <p>
     * An IndexedNeighborGraphBuilder is utilized as the underlying builder 
     * with which the model is produced.
     * </p>
     * 
     * @param generatingSet any Set&ltG&gt
     * @param root any element of G 
     * 
     * @return An IndexedNeighborGraph&ltG&gt model for the connected component of the Cayley graph of G with respect to 
     * genSet, based at the designated root vertex, with indexing on the vertex set
     * defined by the insertion order under a breadth-first construction.
     */
    public IndexedNeighborGraph<G> getIndexedCayleyGraph(Set<G> generatingSet, G root) {
        IndexedNeighborGraphBuilder<G> bigraphBase = new IndexedNeighborGraphBuilder<G>(generatingSet.size());
        buildCayleyGraph(bigraphBase, generatingSet, root);
        return bigraphBase;
    }
    
        /**
     * A method that can be used to build the connected component of the Cayley graph of G with 
     * respect to generatingSet, based at the designated root vertex, when it is
     * known that the subgroup of G generated by generatingSet has numVerts elements.
     * The indexing on the vertex set is
     * defined by the insertion order under a breadth-first construction.
     * 
     * <p>
     * An IndexedNeighborGraphBuilder is utilized as the underlying builder 
     * with which the model is produced.
     * </p>
     * 
     * @param generatingSet any Set&ltG&gt
     * @param root any element of G
     * @param numVerts the number of elements in the subgroup generated by the
     * given generating set.  
     * 
     * @return An IndexedNeighborGraph&ltG&gt model for the connected component of the Cayley graph of G with respect to 
     * genSet, based at the designated root vertex, with indexing on the vertex set
     * defined by the insertion order under a breadth-first construction.
     */
    public IndexedNeighborGraph<G> getIndexedCayleyGraph(Set<G> generatingSet, G root, int numVerts) {
        IndexedNeighborGraphBuilder<G> bigraphBase = new IndexedNeighborGraphBuilder<G>(numVerts, generatingSet.size());
        buildCayleyGraph(bigraphBase, generatingSet, root);
        return bigraphBase;
    }

    /**
     * This method produces an IndexedNavigableRootedNeighborGraph&ltG&gt model for the connected component of the Cayley graph of G with 
     * respect to generatingSet, based at the designated root vertex, with indexing on the vertex set
     * defined by the insertion order under a breadth-first construction.
     * 
     * <p>
     * A BreadthFirstNeighborGraphBuilder is utilized as the underlying builder 
     * with which the model is produced.
     * </p>
     * 
     * @param generatingSet any Set&ltG&gt
     * @param root any element of G 
     * 
     * @return An IndexedNavigableRootedNeighborGraph&ltG&gt model for the connected component of the Cayley graph of G with respect to 
     * genSet, based at the designated root vertex, with indexing on the vertex set
     * defined by the insertion order under a breadth-first construction.
     */
    public IndexedNavigableRootedNeighborGraph<G> getIndexedNavigableCayleyGraph(Set<G> generatingSet, G root) {
        BreadthFirstNeighborGraphBuilder<G> bigraphBase = new BreadthFirstNeighborGraphBuilder<G>(root, generatingSet.size());//(25920,(short) 156);
        buildCayleyGraph(bigraphBase, generatingSet, root);
        return bigraphBase;
    }
    
    /**
     * This method produces an IndexedNavigableRootedNeighborGraph&ltG&gt model for the connected component of the Cayley graph of G with 
     * respect to generatingSet, based at the designated root vertex, when it is
     * known that the subgroup of G generated by generatingSet has numVerts elements.
     * The indexing on the vertex set is
     * defined by the insertion order under a breadth-first construction.
     * 
     * 
     * <p>
     * A BreadthFirstNeighborGraphBuilder is utilized as the underlying builder 
     * with which the model is produced.
     * </p>
     * 
     * @param generatingSet any Set&ltG&gt
     * @param root any element of G 
     * 
     * @return An IndexedNavigableRootedNeighborGraph&ltG&gt model for the connected component of the Cayley graph of G with respect to 
     * genSet, based at the designated root vertex, with indexing on the vertex set
     * defined by the insertion order under a breadth-first construction.
     */
    public IndexedNavigableRootedNeighborGraph<G> getIndexedNavigableCayleyGraph(Set<G> generatingSet, G root, int numVerts) {
        BreadthFirstNeighborGraphBuilder<G> bigraphBase = new BreadthFirstNeighborGraphBuilder<G>(root, numVerts, generatingSet.size());
        buildCayleyGraph(bigraphBase, generatingSet, root);
        return bigraphBase;
    }

    private static <G extends Group<G>> G getVertex(G m, Collection<G> s) {

        for (G g : s) {
            if (g.equals(m)) {
                return g;
            }
        }
        return null;
    }

    /**
     * A general purpose method that, with a given empty ModifiableNeighborGraph&ltG&gt,
     * builds the connected component of the Cayley graph of G with 
     * respect to generatingSet, based at the designated root vertex.
     * 
     * The graph is constructed in a breadth-first fashion. 
     * 
     * @param cayleyGraph an empty ModifiableNeighborGraph&ltG&gt into which the Cayley graph is built.
     * @param generatingSet any Set&ltG&gt
     * @param root any element of G
     */
    public void buildCayleyGraph(ModifiableNeighborGraph<G> cayleyGraph, Set<G> generatingSet, G root) {

        Map<G, G> labelInvolution = new HashMap<G, G>();   //unnecessary once generatingSet is made a SymSet
        for (G g : generatingSet) {
            labelInvolution.put(g, g.getInverse());
        }

        int degree = generatingSet.size();
        Map<G, Set<G>> generatorsToTraverse = new LinkedHashMap<G, Set<G>>();

        generatorsToTraverse.put(root, new HashSet<G>(degree, 1));
        generatorsToTraverse.get(root).addAll(generatingSet);

        System.out.println("GENERATING CAYLEY GRAPH");

        int numJoins = 0;
        int vertCount = 1;

        int excess = 0;

        while (!generatorsToTraverse.isEmpty() && !terminated) {
            
            Iterator<Map.Entry<G, Set<G>>> iterator = (new LinkedList<Map.Entry<G, Set<G>>>(generatorsToTraverse.entrySet())).iterator();
            generatorsToTraverse.clear();
            
            while (iterator.hasNext()) {
            
                if (terminated) {
                    break;
                }
                Map.Entry<G, Set<G>> fiber = iterator.next();
                iterator.remove();

                G g = fiber.getKey();
                
                for (G s : fiber.getValue()) {

                    G nextVertex = g.rightProductBy(s);                                          // Right here is the heart of it all!
                    G existingVertex;

                    if (!cayleyGraph.containsVertex(nextVertex)) {
                        generatorsToTraverse.put(nextVertex, new HashSet<G>(generatingSet));
                        vertCount++;
                        if (vertCount % 100 == 0) {
                            for (ProgressListener listener : listeners) {
                                listener.update("vertex_progress", vertCount);
                            }
                        }
                        existingVertex = nextVertex;
                    } else {
                        Collection<G> verticesToCheck = cayleyGraph.getNeighborsOf(cayleyGraph.getNeighborsOf(nextVertex).iterator().next());
                        existingVertex = getVertex(nextVertex, verticesToCheck);
                    }
                    
                    if (!generatorsToTraverse.containsKey(existingVertex)) {
                        if (!cayleyGraph.hasEdgeJoining(g, existingVertex)) {
                            cayleyGraph.join(g, existingVertex);
                            numJoins += 2;
                        }
                    } else {
                        cayleyGraph.join(g, existingVertex);
                        numJoins += 2;
                    }
                    
                    //if (!cayleyGraph.hasEdgeJoining(g, existingVertex)) {
                    //    cayleyGraph.join(g, existingVertex);
                    //    numJoins += 2;
                    //} else excess++;

                    if (generatorsToTraverse.containsKey(existingVertex)) {
                        generatorsToTraverse.get(existingVertex).remove(labelInvolution.get(s));
                    }

                    if (numJoins % 2000 == 0) {
                        for (ProgressListener listener : listeners) {
                            listener.update("edge_progress", numJoins / 2);
                        }

                    }
                }
            }
            //System.out.println("GENERATORS TO TRAVERSE SIZE: " + generatorsToTraverse.size());
        }

        if (!terminated) {
            System.out.println("EXCESS: " + excess);
            System.out.println("SORTING TARGET LISTS...");
            for (ProgressListener listener : listeners) {
                listener.update("status", 1);
            }
            cayleyGraph.finish();

            System.out.println("DONE");
            for (ProgressListener listener : listeners) {
                listener.update("status", -1);
            }
        }
    }
    
    
    
    
    
    
    
    
    
    
    private void cayleyBigraphGenerator02(ModifiableNeighborGraph<G> cayleyGraph, Set<G> generatingSet, G root) {

        Map<G, G> labelInvolution = new HashMap<G, G>();   //unnecessary once generatingSet is made a SymSet
        for (G g : generatingSet) {
            labelInvolution.put(g, g.getInverse());
        }

        int degree = generatingSet.size();
        Set<G> currentLayer = new HashSet<G>();
        Map<G, Set<G>> generatorsToTraverse = new HashMap<G, Set<G>>();

        generatorsToTraverse.put(root, new HashSet<G>(degree, 1));
        generatorsToTraverse.get(root).addAll(generatingSet);
        currentLayer.addAll(generatorsToTraverse.keySet());

        System.out.println("GENERATING CAYLEY GRAPH");

        int numJoins = 0;
        int vertCount = 1;

        int excess = 0;

        while (!currentLayer.isEmpty() && !terminated) {

            for (G g : currentLayer) {

                if (terminated) {
                    break;
                }

                Set<G> gensToTrav = new HashSet<G>(generatorsToTraverse.get(g).size(), 1);
                gensToTrav.addAll(generatorsToTraverse.get(g));

                for (G s : gensToTrav) {

                    G nextVertex = g.rightProductBy(s);                                              // Right here is the heart of it all!
                    G existingVertex;

                    if (currentLayer.contains(nextVertex)) {
                        excess++;
                    }

                    //boolean check = cayleyGraph.containsVertex(nextVertex);

                    if (!cayleyGraph.containsVertex(nextVertex)) {
                        generatorsToTraverse.put(nextVertex, new HashSet<G>(degree, 1));
                        generatorsToTraverse.get(nextVertex).addAll(generatingSet);
                        vertCount++;
                        if (vertCount % 100 == 0) {
                            for (ProgressListener listener : listeners) {
                                listener.update("vertex_progress", vertCount);
                            }
                            System.out.println("VERTEX COUNT: " + vertCount);
                            System.out.println("VERTICES: " + cayleyGraph.getNumberOfVertices());
                        }
                        existingVertex = nextVertex;
                    } else {
                        Collection<G> verticesToCheck = cayleyGraph.getNeighborsOf(cayleyGraph.getNeighborsOf(nextVertex).iterator().next());
                        existingVertex = getVertex(nextVertex, verticesToCheck);
                    }
                    //boolean join = 
                    if (cayleyGraph.join(g, existingVertex)) {
                        numJoins += 2;
                    }

                    if (generatorsToTraverse.containsKey(existingVertex)) {
                        generatorsToTraverse.get(existingVertex).remove(labelInvolution.get(s));
                    }



                    if (numJoins % 2000 == 0) {
                        for (ProgressListener listener : listeners) {
                            listener.update("edge_progress", numJoins / 2);
                        }
                        System.out.println("EDGE COUNT: " + numJoins);
                    }
                }
                gensToTrav.clear();
                generatorsToTraverse.get(g).clear();
                generatorsToTraverse.remove(g);
            }
            currentLayer.clear();
            currentLayer.addAll(generatorsToTraverse.keySet());
            System.out.println(currentLayer.size());
        }

        if (!terminated) {
            System.out.println("EXCESS: " + excess);
            System.out.println("SORTING TARGET LISTS...");
            for (ProgressListener listener : listeners) {
                listener.update("status", 1);
            }
            cayleyGraph.finish();

            System.out.println("DONE");
            for (ProgressListener listener : listeners) {
                listener.update("status", -1);
            }
        }
    }
    
}
