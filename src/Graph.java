/******************************************************************************
 * Graph API based on Graph.java from: 
 * 
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu/code/
 * 
 * ===========================================================================
 * 
 * @author Thomas O'Cuilleanain 
 ******************************************************************************/

package src;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Graph API
 */
public class Graph {

    // For displaying graphs
    private static final String NEWLINE = System.getProperty("line.separator");

    // Basic attributes used in algorithms
    private int V;
    private int E;
    private int colouring = -1;
    private int degeneracy = -1;

    // Array of vertices adjacent to veretex adj[i] 
    private ArrayList< ArrayList<Integer> > adj;

    // List of vertices
    private ArrayList<Integer> vertices;

    // Array of vertex degrees
    // Elements in array i are of degree i
    private ArrayList< ArrayList<Integer> > D;

    // Smallest-last ordering of vertices
    private ArrayList<Integer> SLOrdering;

    // Colour of each vertex after graph has been coloured
    private int[] vertexColour;
    // Number of vertices coloured
    private int numColoured = 0;
    
    // Depth of each vertex 
    private int[] vertexDepth;


    /**
     * Initialises a graph from an input
     * 
     * The first two lines of the .txt file must contain the number of vertices
     * and the number of edges respectively.
     * From then onwards, the file must contain the edges represented by two vertices.
     * Example:
     * 
     *          5
     *          3
     *          0 1
     *          0 2
     *          2 3
     * 
     * @param in
     */
    public Graph(In in) {
        if (in == null) throw new IllegalArgumentException("argument is null");
        try {

            /* Initialising Vertices */
            // Read first line of text file
            this.V = in.readInt();
            if (V < 0) throw new IllegalArgumentException("number of vertices in a Graph must be non-negative");
            
            // Initialise empty arrays
            vertices = new ArrayList<Integer>(V);
            adj = new ArrayList< ArrayList<Integer> >(V);

            // Algorithm arrays
            D = new ArrayList< ArrayList<Integer> >(V - 1);
            SLOrdering = new ArrayList<Integer>(V);
            vertexColour = new int[V];
            vertexDepth = new int[V];
            
            for (int v = 0; v < V; ++v) {
                vertices.add(v);
                adj.add(new ArrayList<Integer>());
                // Max degree of a vertex is V - 1
                if (v > 0) D.add(new ArrayList<Integer>());
            }
            
            /* Initialising Edges */
            int E = in.readInt();
            if (E < 0) throw new IllegalArgumentException("number of edges in a Graph must be non-negative");

            // Read content of file
            for (int i = 0; i < E; i++) {
                int v = in.readInt();
                int w = in.readInt();
                addEdge(v, w); 
            }
            // Degree list initialization
            initDegrees();
        }
        catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in Graph constructor", e);
        }
    }


    /**
     * Copy constructor
     * 
     * @param other foreign graph
     */
    public Graph(Graph other) {
        this.V = other.V();
        this.E = other.E();
        vertices = new ArrayList<Integer> ();
        adj = new ArrayList< ArrayList<Integer> >();
        D = new ArrayList< ArrayList<Integer> >();

        for (int v = 0; v < other.V(); ++v) {
            vertices.add(v);
            adj.add(new ArrayList<Integer>(other.adj(v)));   
            if (v > 0) D.add(new ArrayList<Integer>(other.D.get(v - 1)));
        }
    }


    /**
     * Returns the number of vertices in this graph.
     *
     * @return the number of vertices in this graph
     */
    public int V() {
        return V;
    }


    /**
     * @return vertices array
     */
    public ArrayList<Integer> vertices() {
        return vertices;
    }


    /**
     * @return the number of edges in this graph
     */
    public int E() {
        return E;
    }


    /** 
     * @return degree matrix
     */
    public ArrayList< ArrayList<Integer> > D() {
        return D;
    }


    /**
     * @return Degeneracy of the graph
     */
    public int getDegeneracy() {
        if (degeneracy == -1) degeneracy();
        return degeneracy;
    }


    /**
     * @return k-colouring
     */
    public int getColouring() {
        if (colouring == -1) colouring();
        return colouring;
    }

    
    /**
     * @return array of depth of every vertex
     */
    public int[] vertexDepth() {
        if (degeneracy == -1) degeneracy();
        return vertexDepth;
    }


    /**
     * Returns depth of a specific vertex
     * 
     * @param v vertex
     * @return depth of v
     */
    public int vertexDepth(int v) {
        if (degeneracy == -1) degeneracy();
        return vertexDepth[v];
    }


    /**
     * Adds the undirected edge v-w to this graph.
     *
     * @param  v one vertex in the edge
     * @param  w the other vertex in the edge
     * @throws IllegalArgumentException unless both {@code 0 <= v < V} and {@code 0 <= w < V}
     */
    private void addEdge(int v, int w) {
        E++;
        adj.get(v).add(w);
        adj.get(w).add(v);
    }


    /**
     * Initialises degree array using adjacency array
     * Used in degeneracy and depth algorithms
     */
    private void initDegrees() {
        for (int v : vertices) D.get(adj(v).size()).add(v);
    }


    /**
     * Removes vertex v 
     * 
     * @param v vertex to be removed
     */
    public void removeVertex(int v) {
        
        ArrayList<Integer> adjV = adj(v);
        for (int i = 0; i < adjV.size(); ++i) {
            int neighbour = adjV.get(i);
            // Neighbour degree array demotion
            D.get(degree(neighbour)).remove(Integer.valueOf(neighbour));
            D.get(degree(neighbour) - 1).add(Integer.valueOf(neighbour));
           
            // Removal from neighbours' adjacency arrays
            adj(neighbour).remove(Integer.valueOf(v));
            E--;
        }
        adjV = new ArrayList<Integer>();
        // Removal from Degree array
        D.get(degree(v)).remove(Integer.valueOf(v));
        // Removal from vertices array
        vertices.remove(Integer.valueOf(v));
        V--;
    }


    /**
     * Returns the vertices adjacent to vertex {@code v}.
     *
     * @param  v the vertex
     * @return the vertices adjacent to vertex {@code v}, as an iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public ArrayList<Integer> adj(int v) {
        return adj.get(v);
    }


    /**
     * Returns the degree of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the degree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int degree(int v) {
        return adj.get(v).size();
    }


    /** 
     * Matula & Beck Smallest-Last ordering algorithm implementation (Q1)
     * Also initialises the depth array (Q3)
     * 
     * @return Degeneracy of the graph
     */
    private void degeneracy() {

        Graph H = new Graph(this);
        int k = 0;
        int selected;
        int initial = H.V();

        for (int i = 0; i < initial; ++i) {
            for (ArrayList<Integer> degs : H.D()) {
                int degree = H.D().indexOf(degs);
                if (degs.size() > 0) {
                    if (k <= degree) k = degree;
                    selected = degs.get(0); 
                    vertexDepth[selected] = k;
                    SLOrdering.add(0, selected);          
                    H.removeVertex(selected);
                    break;
                }
            }
        }
        degeneracy = k;
    }


    /**
     * Used in colouring to check if all vertices have been coloured
     * 
     * @return colouring status
     */
    private boolean allColoured() {
        return numColoured == V;
    }

    
    /**
     * Used in colouring to check if vertices in a colour group are adjacent to
     * a vertex 
     * 
     * @param a array
     * @param b array
     * @return intersection existance
     */
    private boolean intersection(ArrayList<Integer> a, ArrayList<Integer> b) {
        for (int i : a) {
            if (b.contains(i)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Greedy Colouring algorithm using SL Ordering (Q2)
     * 
     * @return (k)-colourable
     */
    private void colouring() {
        
        ArrayList< ArrayList<Integer> > colourGroups = new ArrayList< ArrayList<Integer> >();
        
        // Number of colour groups is <= k
        int k = getDegeneracy();
        for (int i = 0; i <= k; ++i) colourGroups.add(new ArrayList<Integer>());

        int currentColour = 1;
        int selected;
        int i = 0;
        while(! allColoured()) {
            ArrayList<Integer> colourGroup = colourGroups.get(currentColour - 1);
            selected = SLOrdering.get(i);
            // If the vertex has not been coloured
            if (vertexColour[selected] == 0) {
                // Colour vertex
                vertexColour[selected] = currentColour;
                // Add to colour group
                colourGroup.add(selected);
                // Increment coloured variable
                numColoured++;
                for (int j = i + 1; j < V; ++j) {
                    int vertex = SLOrdering.get(j);
                    // If the vertex has not been coloured AND is not adjacent to any vertex 
                    // in the current colour group
                    if (vertexColour[vertex] == 0 
                        && !intersection(adj(vertex), colourGroup)) {
                        // Colour vertex
                        vertexColour[vertex] = currentColour;
                        // Add to colour group
                        colourGroup.add(vertex);
                        // Increment coloured variable
                        numColoured++;
                    }
                } 
                currentColour++;
            }
            i++;         
        }
        colouring = currentColour - 1;
    }


    /**
     * Display colour groups and every member (vertex)
     * ! not to be used for big graphs !
     */
    public void displayColouring() {
        StringBuilder s = new StringBuilder();
        s.append(colouring + "-Colouring" + NEWLINE);
        s.append("=================" + NEWLINE);
        for (int i = 1; i <= colouring; ++i) {
            s.append("Color " + i + ": ");
            for (int j = 0; j < V; ++j) {
                if (vertexColour[j] == i) s.append(vertices.get(j) + " ");
            }
            s.append(NEWLINE);
            s.append(NEWLINE);
        }
        System.out.println(s);
    }


    /**
     * Returns a string representation of this graph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *         followed by the <em>V</em> adjacency lists
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Adjacency List" + NEWLINE);
        s.append("=================" + NEWLINE);
        s.append(V + " vertices, " + E + " edges " + NEWLINE);
        for (int v : vertices) {
            s.append(v + ": ");
            for (int w : adj.get(v)) {
                s.append(w + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

}
