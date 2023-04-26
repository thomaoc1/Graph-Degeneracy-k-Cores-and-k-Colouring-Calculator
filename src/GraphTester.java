/******************************************************************************
 * Graph API Tester, used to test algorithm effeciency and generate reports
 * ===========================================================================
 * 
 * @author Thomas O'Cuilleanain
 ******************************************************************************/

package src;

public class GraphTester {

    private static void average(String file, int totalReps) {

        // Time variables
        long recConstruction = 0;
        long recDegen =  0;
        long recColouring = 0;
        long recTotal = 0;

        // Results
        int k = 0, c = 0;

        Graph G;
        int v = 0, e = 0;
        
        // Repeats each action totalReps times
        for (int i = 0; i < totalReps; ++i){
            In in = new In(file);

            // Construction
            long startCon = System.nanoTime();
            G = new Graph(in);
            long endCon = System.nanoTime();

            // Degeneracy & Depth
            long startD = System.nanoTime();
            k = G.getDegeneracy();
            long endD = System.nanoTime();

            // Colouring
            long startCol = System.nanoTime();
            c = G.getColouring();
            long endCol = System.nanoTime();

            v = G.V();
            e = G.E();

            // Recording results
            recConstruction += (endCon - startCon) / 1000000;
            recDegen += (endD - startD) / 1000000;
            recColouring += (endCol - startCol) / 1000000;
            recTotal += (endCol - startCon) / 1000000;
        }
        // Report
        System.out.println("Graph = (V, E), |V| = " + v + ", |E| = " + e);
        System.out.println("========= Average Time(" + totalReps + " reps) ===========");
        System.out.println("Construction: " + recConstruction / totalReps + "ms");
        System.out.println("Calculating degeneracy(" + k + ") : " + + recDegen / totalReps + "ms");
        System.out.println("Calculating colouring(" + c + ") : " + + recColouring / totalReps + "ms");
        System.out.println("Total: " + recTotal / totalReps + "ms");
        System.out.println();
    }
    
    // Method declarations to call all each graph
    private static void largeGraph(int reps) {
        average("graphs/large/twitch.txt", reps);    
    }

    private static void mediumGraph(int reps) {
        average("graphs/medium/Deezer_Croatia.txt", reps);
        
    }

    private static void smallGraph(int reps) {
        average("graphs/medium/facebook_combined.txt", reps);
    }

    private static void tinyGraph(int reps) {
        average("graphs/small/mediumG.txt", reps);
    }

    public static void main(String[] args) {
        int reps = 1;
        if (args.length > 0) reps = Integer.valueOf(args[0]);
        tinyGraph(reps);
        smallGraph(reps);
        mediumGraph(reps);
        largeGraph(reps);
    }
}
