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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
 * This class contains several static methods for building <em>Cayley</em> graph
 * structures.
 * These methods are generic, taking a type parameter G that extends 
 * the interface Group&ltG&gt of the Groups module, and return models of Cayley graphs
 * in terms of the interfaces in the api package of the Graphs module.
 * 
 * <p>
 * The method
 * buildCayleyGraph performs all of the actual constructions of the Cayley graphs in this class.  
 * This is a general purpose method that 
 * takes as parameters:<br>
 * &#160&#160&#160&#160(i) an empty ModifiableNeighborGraph&ltG&gt,<br>
 * &#160&#160&#160(ii) a Set&ltG&gt for the generating set,<br>
 * &#160&#160(iii) a designated element of G as the root vertex.<br> 
 * The generating set should be 
 * closed under the inversion operation, but is enlarged to be so if 
 * necessary.  With the ModifiableNeighborGraph&ltG&gt, this method then builds <em>the connected component based at the 
 * root vertex, of the Cayley graph of  G with respect to the generating set</em>.
 * </p>
 * 
 * <p>
 * The get methods of this class utilize the builder implementations 
 * from the builder package of the Graphs module to produce read-only NeighborGraph models 
 * (as specified by the NeighborGraph
 * interfaces in the api package of the Graphs module),
 * by passing the appropriate builder as the ModifiableNeighborGraph parameter
 * in the buildCayleyGraph method of this class.  
 * </p>
 * <p>
 * For each of the three interfaces
 * NeighborGraph, IndexedNeighborGraph, and IndexedNavigableNeighborGraph, there are 
 * two overloaded 'get' methods of this class that return a graph of that type.  Both take as 
 * parameters a generating set and a root vertex, while
 * one of them also takes a third parameter, namely an int numVerts, for the size
 * of the generated subgroup by the given set, if it is known.  This has the benefit
 * of preallocating the required memory at the outset of construction.
 * </p>
 * 
 * @author pdokos
 */
public class CayleyGraphBuilder {
    
    private CayleyGraphBuilder() {}

    /**
     * This method produces a NeighborGraph&ltG&gt model for the connected component of the Cayley graph of G with 
     * respect to generatingSet, based at the designated root vertex.
     * 
     * <p>
     * A NeighborGraphBuilder is utilized as the underlying builder 
     * with which the model is produced.
     * </p>
     * 
     * @param <G> the group implementation
     * @param generatingSet any Set&ltG&gt
     * @param root any element of G 
     * 
     * @return A NeighborGraph&ltG&gt model for the connected component of the Cayley graph of G with respect to 
     * genSet, based at the designated root vertex.
     */
    public static <G extends Group<G>> NeighborGraph<G> getCayleyGraph(Set<G> generatingSet, G root) {
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
     * @param <G> the group implementation
     * @param generatingSet any Set&ltG&gt
     * @param root any element of G
     * @param numVerts the number of elements in the subgroup generated by the
     * given generating set.  
     * 
     * @return A NeighborGraph&ltG&gt model for the connected component of the Cayley graph of G with respect to 
     * genSet, based at the designated root vertex.
     */
    public static <G extends Group<G>> NeighborGraph<G> getCayleyGraph(Set<G> generatingSet, G root, int numVerts) {
        ModifiableNeighborGraph<G> bigraphBase = new NeighborGraphBuilder<G>(numVerts, generatingSet.size());
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
     * @param <G> the group implementation
     * @param generatingSet any Set&ltG&gt
     * @param root any element of G 
     * 
     * @return An IndexedNeighborGraph&ltG&gt model for the connected component of the Cayley graph of G with respect to 
     * genSet, based at the designated root vertex, with indexing on the vertex set
     * defined by the insertion order under a breadth-first construction.
     */
    public static <G extends Group<G>> IndexedNeighborGraph<G> getIndexedCayleyGraph(Set<G> generatingSet, G root) {
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
     * @param <G> the group implementation
     * @param generatingSet any Set&ltG&gt
     * @param root any element of G
     * @param numVerts the number of elements in the subgroup generated by the
     * given generating set.  
     * 
     * @return An IndexedNeighborGraph&ltG&gt model for the connected component of the Cayley graph of G with respect to 
     * genSet, based at the designated root vertex, with indexing on the vertex set
     * defined by the insertion order under a breadth-first construction.
     */
    public static <G extends Group<G>> IndexedNeighborGraph<G> getIndexedCayleyGraph(Set<G> generatingSet, G root, int numVerts) {
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
     * @param <G> the group implementation
     * @param generatingSet any Set&ltG&gt
     * @param root any element of G 
     * 
     * @return An IndexedNavigableRootedNeighborGraph&ltG&gt model for the connected component of the Cayley graph of G with respect to 
     * genSet, based at the designated root vertex, with indexing on the vertex set
     * defined by the insertion order under a breadth-first construction.
     */
    public static <G extends Group<G>> IndexedNavigableRootedNeighborGraph<G> getIndexedNavigableCayleyGraph(Set<G> generatingSet, G root) {
        BreadthFirstNeighborGraphBuilder<G> bigraphBase = new BreadthFirstNeighborGraphBuilder<G>(root, generatingSet.size());
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
     * @param <G> the group implementation
     * @param generatingSet any Set&ltG&gt
     * @param root any element of G 
     * 
     * @return An IndexedNavigableRootedNeighborGraph&ltG&gt model for the connected component of the Cayley graph of G with respect to 
     * genSet, based at the designated root vertex, with indexing on the vertex set
     * defined by the insertion order under a breadth-first construction.
     */
    public static <G extends Group<G>> IndexedNavigableRootedNeighborGraph<G> getIndexedNavigableCayleyGraph(Set<G> generatingSet, G root, int numVerts) {
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
    public static <G extends Group<G>> void buildCayleyGraph(ModifiableNeighborGraph<G> cayleyGraph, Set<G> generatingSet, G root) {

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
                            cayleyGraph.join(g, existingVertex);
                        }
                    } else {
                        cayleyGraph.join(g, existingVertex);
                    }
                    
                    //if (!cayleyGraph.hasEdgeJoining(g, existingVertex)) {
                    //    cayleyGraph.join(g, existingVertex);
                    //    numJoins += 2;
                    //} else excess++;

                    if (generatorsToTraverse.containsKey(existingVertex)) {
                        generatorsToTraverse.get(existingVertex).remove(labelInvolution.get(s));
                    }

                }
            }
            //System.out.println("GENERATORS TO TRAVERSE SIZE: " + generatorsToTraverse.size());
        }

            System.out.println("DONE");
            //System.out.println("EXCESS: " + excess);
            System.out.print("SORTING NEIGHBOR LISTS...    ");
            cayleyGraph.finish();
            System.out.println("DONE");
    }
    
    
    /**
     * A general purpose method that, with a given empty ModifiableNeighborGraph&ltG&gt,
     * builds the connected component of the Cayley graph of G with 
     * respect to generatingSet, based at the designated root vertex.
     * 
     * The graph is constructed in a breadth-first fashion. 
     * 
     * @param <G> the group implementation
     * @param cayleyGraph an empty ModifiableNeighborGraph&ltG&gt into which the Cayley graph is built.
     * @param generatingSet any Set&ltG&gt
     * @param root any element of G
     */
    private static <G extends Group<G>> void buildCayleyGraphOld(ModifiableNeighborGraph<G> cayleyGraph, Set<G> generatingSet, G root) {

        Map<G, G> labelInvolution = new HashMap<G, G>();
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

        while (!currentLayer.isEmpty()) {

            for (G g : currentLayer) {

                Set<G> gensToTrav = new HashSet<G>(generatorsToTraverse.get(g).size(), 1);
                gensToTrav.addAll(generatorsToTraverse.get(g));

                for (G s : gensToTrav) {                    

                    G nextVertex = g.rightProductBy(s);                                              // Right here is the heart of it all!
                    G existingVertex;

                    if (currentLayer.contains(nextVertex)) {
                        excess++;
                    }
                    
                    if (!cayleyGraph.containsVertex(nextVertex)) {
                        generatorsToTraverse.put(nextVertex, new HashSet<G>(degree, 1));
                        generatorsToTraverse.get(nextVertex).addAll(generatingSet);
                        vertCount++;
                        if (vertCount % 100 == 0) {
                            System.out.println("VERTEX COUNT: " + vertCount);
                            System.out.println("VERTICES: " + cayleyGraph.getNumberOfVertices());
                        }
                        existingVertex = nextVertex;
                    } else {
                        Collection<G> verticesToCheck = cayleyGraph.getNeighborsOf(cayleyGraph.getNeighborsOf(nextVertex).iterator().next());
                        existingVertex = getVertex(nextVertex, verticesToCheck);
                    }
                    if (cayleyGraph.join(g, existingVertex))
                        numJoins += 2;

                    if (generatorsToTraverse.containsKey(existingVertex)) {
                        generatorsToTraverse.get(existingVertex).remove(labelInvolution.get(s));
                    }

                    if (numJoins % 1000 == 0) {
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
        System.out.println("EXCESS: " + excess);
        System.out.println("SORTING TARGET LISTS...");
        cayleyGraph.finish();
        System.out.println("DONE");
    }
    
}