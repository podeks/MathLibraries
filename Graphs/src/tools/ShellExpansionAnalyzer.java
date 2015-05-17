package tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import api.IndexedNavigableRootedNeighborGraph;

/**
 * A tool for determining some basic numerical quantities pertaining 
 * to a given IndexedNavigableRootedNeighborGraph, tabulating these quantities
 * for display in a JTable, and saving the data to a file.
 * 
 * <p>
 * Before tabulating or saving any data, the data must first be created by
 * calling the method generateData(). This method determines the following:<br>
 * &#160&#160&#160&#160(i) The number of vertices and edges of the graph.<br>
 * &#160&#160&#160(ii) Whether or not the graph is bipartite.<br>
 * &#160&#160(iii) The length of the shortest cycle based at the root.<br>
 * &#160&#160(iv) The maximum distance from the root among all vertices of the
 * graph.<br>
 * In addition, three arrays of length one more than the maximum distance of
 * item (iv) above are created:<br>
 * &#160&#160&#160(ai) An array s_n, whose i-th entry is the number of vertices
 * of distance i from the root.<br>
 * &#160&#160(aii) An array e_n, whose 0-th is 0, and whose i-th entry is the
 * number of edges joining the shells of radius i and i-1, centered at the
 * root.<br>
 * &#160(aiii) An array t_n, whose i-th entry is the number of edges whose
 * endpoints are both of distance i from the root.
 * </p>
 * 
 * Remark: This tool is primarily intended to be used for graphs which are known 
 * to be <em>vertex-transitive</em> (meaning that for every pair of vertices of the graph, there is a 
 * graph symmetry carrying one vertex to the other), in particular the Cayley
 * graphs.  In this case, item (iii) is
 * the girth of the graph, item (iv) is the diameter, and the three arrays above 
 * are independent of the root.
 * 
 * @author pdokos
 */
public class ShellExpansionAnalyzer<S> {

    protected IndexedNavigableRootedNeighborGraph<S> graph;
     
    protected int numVerts;
    protected int numEdges;
    protected int diameter;
    protected double avgDist;
    protected int girth;
    protected boolean bipartite;
    
    protected List<Integer> s_n;
    protected List<Integer> e_n;
    protected List<Integer> t_n;

    /**
     * Instantiates a new ShellExpansionAnalyzer on the given IndexedNavigableRootedNeighborGraph.
     * The graph assigned to the analyzer through this constructor cannot be changed.
     * 
     * @param graph The graph assigned to this analyzer.
     */
    public ShellExpansionAnalyzer(IndexedNavigableRootedNeighborGraph<S> graph) {
        this.graph = graph;
    }

    /**
     * Returns the graph that this analyzer has been assigned.
     * 
     * @return The graph that this analyzer had been assigned.
     */
    public IndexedNavigableRootedNeighborGraph<S> getGraph() {
        return this.graph;
    }
    
    /**
     * Generates all of the numerical data pertaining to the graph.  More specifically,
     * this method determines the following:<br>
     * &#160&#160&#160&#160(i) The number of vertices and edges of the graph.<br>
     * &#160&#160&#160(ii) Whether or not the graph is bipartite.<br>
     * &#160&#160(iii) The length of the shortest cycle based at the root.<br>
     * &#160&#160(iv) The maximum distance from the root among all vertices of the graph.<br>
     * In addition, three arrays of length one more than the maximum 
     * distance of item (iv) are created:<br>
     * &#160&#160&#160(ai) An array s_n, whose i-th entry is the number of 
     * vertices of distance i from the root.<br>
     * &#160&#160(aii) An array e_n, whose 0-th is 0, and whose i-th entry is the 
     * number of edges joining the shells of radius i and i-1, centered at the root.<br>
     * &#160(aiii) An array t_n, whose i-th entry is the number of edges whose 
     * endpoints are both of distance i from the root.
     */
    public void generateData() {
        
        //System.out.print("GENERATING SHELL DATA...     ");
        
        numVerts = graph.getNumberOfVertices();
        numEdges = graph.getNumberOfEdges()/2;
        
        s_n = new ArrayList<Integer>();
        t_n = new ArrayList<Integer>();
        e_n = new ArrayList<Integer>();
        e_n.add(0);
        
        girth=0;
        bipartite=true;
        diameter=graph.getMaxDistanceFromRoot();
        int sum=0;
        
        for (int i=0; i<=diameter; i++) {
            List<S> currentShell = graph.getShell(i);
            
            int numOutEdges=0;
            int numTangEdges=0;
            
            for (S s : currentShell) {
                numOutEdges += graph.getNeighborsInNextShell(s).size();
                numTangEdges += graph.getNeighborsInSameShell(s).size();
            }
            
            sum += currentShell.size();
            s_n.add(currentShell.size());
            e_n.add(numOutEdges);
            t_n.add(numTangEdges/2);
            
            if (numTangEdges>0) 
                bipartite=false;
            if (girth==0) {
                if (!bipartite) {
                    girth = 2*i + 1;
                } else if (e_n.get(i) != currentShell.size()) {
                    girth = 2*i;
                }
            }
            
        }
        
        avgDist = sum/numVerts;
        //System.out.println("DONE");

    }
    
    /**
     * Returns true if the graph is bipartite.
     * 
     * @return true if the graph is bipartite.
     */
    public boolean isBipartite() {
        return bipartite;
    }
    
    /**
     * Returns the girth of the graph.
     * 
     * @return the girth of the graph.
     */
    public int getGirth() {
        return girth;
    }
    
    /**
     * A helper method for clients to initialize their user interface 
     * prior filling a basic data TableModel.  
     * 
     * @return An empty basic data TableModel.
     */
    public static TableModel getBasicDataEmptyTableModel() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{
            "Vertices", "Edges", "Bipartite", "Avg Dist", "Diameter", "Girth"
        });
        return model;
    }
    
    /**
     * A helper method for clients to initialize their user interface 
     * prior filling a vertex shell TableModel.  
     * 
     * @return An empty basic data TableModel.
     */
    public static TableModel getVertexShellEmptyTableModel() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{
            "n", "|S(n)|", "|S(n)|/|S(n-1)|", "", ""
        });
        return model;
    }
    
    /**
     * A helper method for clients to initialize their user interface 
     * prior filling a edge shell TableModel.  
     * 
     * @return An empty basic data TableModel.
     */
    public static TableModel getEdgeEmptyTableModel() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{
            "n", "|E(n)|", "|E(n)|/|E(n-1)|", "T(n)", "|E(n)|/|S(n)|"
        });
        
        return model;
    }
    
    /**
     * Creates a basic data TableModel.  This is a single-row, five-column 
     * TableModel, with columns labeled by: "Vertices", "Edges", "Bipartite", 
     * "Diameter", "Girth".
     * 
     * <p>
     * Note that the labels "Diameter" and "Girth" reflect the intent that the
     * graph is actually vertex-transitive, as explained in the description of this class.
     * If it is not the case that the graph is vertex-transitive, then the meaning 
     * of the quantities associated with these properties are as described in items 
     * (iii) and (iv) in the description of this class.
     * </p>
     * 
     * @return An basic data TableModel.
     */
    public final TableModel getBasicDataTableModel() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{
            "Vertices", "Edges", "Bipartite", "Avg Dist", "Diameter", "Girth"
        });
               

        String bip;
        if (bipartite) {
            bip="Yes";
        } else {
            bip="No";
        }

        DecimalFormat formatter = new DecimalFormat("#.##");
        model.addRow(new Object[]{NumberFormat.getIntegerInstance().format(numVerts), NumberFormat.getIntegerInstance().format(numEdges), bip, formatter.format(avgDist), diameter, girth});
        return model;
    }

    /**
     * Creates a vertex shell TableModel.  This is a five-column TableModel with
     * the first three columns labeled by: "n", "|S(n)|", "|S(n)|/|S(n-1)|", and 
     * the last two columns blank.  The rows run from n=0, 1, ..., maxDistance from root.
     * The entry in the third column of the first row is set to null.
     * 
     * @return An vertex shell TableModel.
     */
    public final TableModel getVertexShellTableModel() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{
            "n", "|S(n)|", "|S(n)|/|S(n-1)|", "", ""
        });

        DecimalFormat formatter = new DecimalFormat("#.############");
        model.addRow(new Object[]{0, s_n.get(0), null, null, null});
        for (int i = 1; i <= diameter; i++) {
            model.addRow(new Object[]{i, s_n.get(i), formatter.format((double) s_n.get(i) / (double) s_n.get(i-1)), null, null});
        }

        return model;
    }

    /**
     * Creates an edge shell TableModel.  This is a five-column TableModel with
     * the following labels: "n", "|E(n)|", "|E(n)|/|E(n-1)|", "T(n)", "|E(n)|/|S(n)|".  
     * The rows run from n=1, ..., maxDistance from root.
     * The entry in the third column of the first row is set to null.
     * 
     * @return An edge shell TableModel.
     */
    public final TableModel getEdgeTableModel() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{
            "n", "|E(n)|", "|E(n)|/|E(n-1)|", "T(n)", "|E(n)|/|S(n)|"
        });
        
        
        //NumberFormat numberInstance = NumberFormat.getNumberInstance();
        DecimalFormat formatter = new DecimalFormat("#.############");
        
        for (int i = 1; i <= diameter; i++) {
            if (i == 1) {
                model.addRow(new Object[]{i, e_n.get(i), null, t_n.get(i), formatter.format((double) e_n.get(i) / (double) s_n.get(i))});
            } else {
                model.addRow(new Object[]{i, e_n.get(i), formatter.format((double) e_n.get(i) / (double) e_n.get(i-1)), t_n.get(i), formatter.format((double) e_n.get(i) / (double) s_n.get(i))});
            }
        }
        
        return model;
    }
    
    /**
     * This method writes a single line text file containing the following five 
     * integer values separated by single spaces: (1) number of vertices, 
     * (2) degree, (3) diameter, (4) girth, (5) bipartite (a boolean value
     * represented by 0 or 1).
     * 
     * <p>
     * The second entry actually contains the size of the set of neighbors of 
     * the root.  For a vertex-transitive transitive graph, this is the degree.
     * </p>
     * 
     * @param f The file to which the data is written
     * @return true if the data was successfully written to file.
     * @throws IOException 
     */
    public boolean writeBasicDataToFile(File f) throws IOException {
        FileWriter fw;
        try {
            fw = new FileWriter(f);
        } catch (IOException ex) {
            Logger.getLogger(ShellExpansionAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        StringBuilder line = new StringBuilder();

        int deg;
        if (diameter>0) {
            deg = s_n.get(1);
        } else deg = t_n.get(0);
        int bip=0;
        if (bipartite) {
            bip=1;
        }
        line.append(numVerts).append(' ').append(deg).append(' ').append(diameter).append(' ').append(girth).append(' ').append(bip);
        try {
            fw.write(line.toString());
        } catch (IOException ex) {
            Logger.getLogger(ShellExpansionAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        fw.close();
        
        return true;
    }
    
    /**
     * This method writes the three arrays s_n, e_n, and t_n to a text file (see
     * items (ai), (aii), and (aiii) in the class description for the 
     * definitions of these arrays).
     * The entries are written to the file as a line-separated sequence of
     * triplets of integers, the i-th triplet of the list containing
     * the i-th entries of the three arrays in the order listed above (separated by 
     * single-spaces).
     * 
     * @param f The file to which the data is written
     * @return true if the data was successfully written to file.
     * @throws IOException  
     */
    public boolean writeRadialDataToFile(File f) throws IOException {
        
        FileWriter fw;
        try {
            fw = new FileWriter(f);
        } catch (IOException ex) {
            Logger.getLogger(ShellExpansionAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        StringBuilder line;
        
        for (int i=0; i<=diameter; i++) {
            line = new StringBuilder();
            line.append(s_n.get(i)).append(' ').append(e_n.get(i)).append(' ').append(t_n.get(i)).append('\n');
            fw.write(line.toString());
        }
        
        fw.close();
        
        return true;
    }
    
}




/*
    private final String getExpansionData() {

        StringBuilder expansionData = new StringBuilder();

        expansionData.append("Vertices: ").append(numVerts).append("      " + "Edges: ").append(numEdges).append("\n");
        expansionData.append("\nVERTEX SHELLS:\n");

        for (int i = 0; i <= diameter; i++) {
            expansionData.append("Radius ").append(i).append(" : ").append(s_n.get(i)).append("           "); //distanceFromRoot.getPreImage(i).size()
            if (i != 0) {
                expansionData.append((double) s_n.get(i) / (double) s_n.get(i-1)).append("\n");
            } else {
                expansionData.append("" + "\n");
            }
        }

        expansionData.append("\nEDGES BETWEEN SHELLS: " + "\n");

        int degree = s_n.get(1);
        for (int i = 1; i <= diameter; i++) {
            
            expansionData.append("Radius ").append(i - 1).append("-->").append(i).append(" : ").append(e_n.get(i)).append("           ");
            if (i != 1) {
                double edgeRatio = (double) e_n.get(i) / (double) e_n.get(i-1);
                double alpha = (double) degree / (edgeRatio + 1.0);
                expansionData.append(edgeRatio).append("           ").append(alpha).append("\n");
            } else {
                expansionData.append("" + "\n");
            }
        }
        return expansionData.toString();
    }
    */