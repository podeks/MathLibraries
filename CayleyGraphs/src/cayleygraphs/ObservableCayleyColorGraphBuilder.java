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
import api.Group;
import api.IndexedColorGraph;
import api.ModifiableColorGraph;
import builder.IndexedColorGraphBuilder;

/**
 * This class provides capabilities similar to those of the CayleyGraphBuilder, but with
 * two additional features, namely (i) allowing for external progress listeners that 
 * receive updates on the construction process, and (ii) the ability to terminate the 
 * construction process.
 * 
 * <p>
 * Note that unlike the CayleyGraphBuilder class, the methods of this class are 
 * not static.  Rather, one instantiates an ObservableCayleyColorGraphBuilder object,
 * to which any desired ProgressListeners can be added via the 
 * addProgressListener method, and calls the Cayley graph creation methods from the object.
 * </p>
 * 
 * @author pdokos
 * @param <G> the group implementation
 */
public class ObservableCayleyColorGraphBuilder<G extends Group<G>> {

    private List<ProgressListener> listeners;
    private boolean terminated;
    
    
    List<Integer> s_n;
        List<Integer> t_n;
        List<Integer> e_n;
        
        int girth;
        boolean bipartite;
        
        int diam;
        
        long milliseconds;
        
        
    

    /**
     * Listener interface for an ObservableCayleyColorGraphBuilder.
     * The update method of each listener of an ObservableCayleyColorGraphBuilder
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
     * Instantiates a new ObservableCayleyColorGraphBuilder.
     */
    public ObservableCayleyColorGraphBuilder() {
        listeners = new ArrayList<ProgressListener>();
        terminated = false;
        s_n = new ArrayList<Integer>();
        t_n = new ArrayList<Integer>();
        e_n = new ArrayList<Integer>();
        e_n.add(0);
        
        girth=0;
        bipartite=true;
        diam=0;
        
        milliseconds=0;
        
    }

    /**
     * Cancels this builder's graph construction process, if there is one in progress.
     */
    public void terminate() {
        terminated = true;
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
    public IndexedColorGraph<G, G> getCayleyColorGraph(Set<G> generatingSet, G root) {
        Map<G, G> colorInvolution = new HashMap<G, G>(generatingSet.size() + 1, 1.0f);
        for (G g : generatingSet) {
            colorInvolution.put(g, g.getInverse());
        }
        IndexedColorGraphBuilder<G, G> bigraphBase = new IndexedColorGraphBuilder<G, G>(root, colorInvolution);
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
    public IndexedColorGraph<G, G> getCayleyColorGraph(Set<G> generatingSet, G root, int numVerts) {
        Map<G, G> colorInvolution = new HashMap<G, G>(generatingSet.size() + 1, 1.0f);
        for (G g : generatingSet) {
            colorInvolution.put(g, g.getInverse());
        }
        IndexedColorGraphBuilder<G, G> bigraphBase = new IndexedColorGraphBuilder<G, G>(root, numVerts, colorInvolution);
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
    public void buildCayleyGraph(ModifiableColorGraph<G, G> cayleyGraph, Set<G> generatingSet, G root) {

        Map<G, G> labelInvolution = new HashMap<G, G>();   //unnecessary once generatingSet is made a SymSet
        for (G g : generatingSet) {
            labelInvolution.put(g, getVertex(g.getInverse(), generatingSet));
        }

        int degree = generatingSet.size();
        Map<G, Set<G>> generatorsToTraverse = new LinkedHashMap<G, Set<G>>();

        generatorsToTraverse.put(root, new HashSet<G>(degree, 1));
        generatorsToTraverse.get(root).addAll(generatingSet);

        System.out.println("GENERATING CAYLEY GRAPH");

        int numJoins = 0;
        int vertCount = 1;
        
        long startTime = System.nanoTime();          
        
        while (!generatorsToTraverse.isEmpty() && !terminated) {
            
            int numOutEdges=0;
            int numTangEdges=0;
            int shellSize=generatorsToTraverse.size();
            
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
                            cayleyGraph.join(g, existingVertex, s);
                            numJoins += 2;
                            numTangEdges+=1;
                        }
                    } else {
                        cayleyGraph.join(g, existingVertex, s);
                        numJoins += 2;
                        numOutEdges++;
                    }

                    if (generatorsToTraverse.containsKey(existingVertex)) {
                        generatorsToTraverse.get(existingVertex).remove(labelInvolution.get(s));
                    }

                    if (numJoins % 2000 == 0) {
                        for (ProgressListener listener : listeners) {
                            listener.update("edge_progress", numJoins / 2);
                        }
                        //if (numJoins % 200000 == 0) {
                        //    System.out.println(numJoins / 2);
                        //}
                    }
                }
            }
            
            System.out.println(diam + " : " + shellSize);
            s_n.add(shellSize);
            e_n.add(numOutEdges);
            t_n.add(numTangEdges);
            
            if (numTangEdges>0) 
                bipartite=false;
            if (girth==0) {
                if (e_n.get(diam) != shellSize) {
                    girth = 2*diam;
                } else if (!bipartite) {
                    girth = 2*diam + 1;
                }
            }
            diam++;
        }
        diam--;

        if (!terminated) {
            //System.out.println("SORTING TARGET LISTS...");
            for (ProgressListener listener : listeners) {
                listener.update("status", 1);
            }
            cayleyGraph.finish();

            long endTime = System.nanoTime();
            milliseconds = (endTime - startTime) / 1000000; //divide by 1000000 to get milliseconds.
            System.out.println("TIME: " + milliseconds);
            
            System.out.println("DONE");
            for (ProgressListener listener : listeners) {
                listener.update("status", -1);
            }
        }
        
        for (int i=0; i<=diam; i++) {
            System.out.println(i + " | " + s_n.get(i) + " | " + e_n.get(i) + " | " + t_n.get(i));
        }
        System.out.println("BIPARTITE: "+ bipartite);
        System.out.println("GIRTH: " + girth);
        
        
    }
    
    public List<Integer> getSn() {
        return s_n;
    }
    public List<Integer> getEn() {
        return e_n;
    }
    public List<Integer> getTn() {
        return t_n;
    }
    public boolean getBipartite() {
        return bipartite;
    }
    public int getGirth() {
        return girth;
    }
    
    public long getMilliseconds() {
        return milliseconds;
    }

    
    
    
    
    
    
    public void buildCayleyGraphOld(ModifiableColorGraph<G, G> cayleyGraph, Set<G> generatingSet, G root) {

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
                            cayleyGraph.join(g, existingVertex, s);
                            numJoins += 2;
                        }
                    } else {
                        cayleyGraph.join(g, existingVertex, s);
                        numJoins += 2;
                    }

                    if (generatorsToTraverse.containsKey(existingVertex)) {
                        generatorsToTraverse.get(existingVertex).remove(labelInvolution.get(s));
                    }

                    if (numJoins % 2000 == 0) {
                        for (ProgressListener listener : listeners) {
                            listener.update("edge_progress", numJoins / 2);
                        }
                        if (numJoins % 20000 == 0) {
                            System.out.println(numJoins / 2);
                        }
                    }
                }
            }
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
