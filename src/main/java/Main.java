import java.io.IOException;
import java.util.ArrayList;
import java.util.function.BiFunction;

/**
 * Created by impresyjna on 04.10.2016.
 */
public class Main {
    public static void main(String[] args) {
        Graph graph = null;
        try {
            graph = Graph.readTSPFile("kroA100.tsp");

            //greedyCycle wersja deterministyczna
            greedyCycle(graph);

            //greedyCycle wersja niedeterministyczna
            greedyCycle_GRASP(graph);

            //nearestNeighbour wersja deterministyczna
            nearestNeighbour(graph);

            //nearestNeighbour wersja niedeterministyczna
            nearestNeighbourGRASP(graph);

            //lokalne wyszukiwanie
            localSearch_nearestNeighbour(graph);
            localSearch_nearestNeighbourGRASP(graph);
            localSearch_greedyCycle(graph);
            localSearch_greedyCycleGRASP(graph);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printResults(Graph graph, String algorithmName, BiFunction<Graph, Integer, HalfTSPResult> algorithm) {
        System.out.println(algorithmName);
        ArrayList<Integer> minPath = new ArrayList<>();
        HalfTSPResult result = null;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int sum = 0;
        int average;
        for (int initialVertex = 0; initialVertex < graph.getVerticesCount(); ++initialVertex) {
            result = algorithm.apply(graph, initialVertex);
            int distance = result.getDistance();
            sum += distance;
            if (distance > max) {
                max = distance;
            } else if (distance < min) {
                min = distance;
                minPath = result.getPath();
            }
        }
        average = sum / graph.getVerticesCount();
        System.out.println("MAX: " + max);
        System.out.println("MIN: " + min);
        System.out.println("AVERAGE: " + average);
        for (int vertex : minPath) {
            System.out.print(vertex + " -> ");
        }
        System.out.println(minPath.get(0));
        System.out.println();
    }

    private static void greedyCycle(Graph graph) {
        printResults(graph, "GreedyCycle", (g, initialVertex) -> HalfTSPGreedyCycle.greedyCycle(g, initialVertex));
    }

    private static void greedyCycle_GRASP(Graph graph) {
        printResults(graph, "GreedyCycle - GRASP", (g, initialVertex) -> HalfTSPGreedyCycle.GRASP(g, initialVertex));
    }

    private static void nearestNeighbour(Graph graph) {
        printResults(graph, "Nearest Neihbour", (g, initialVertex) -> HalfTSPNearestNeighbour.nearestNeighbour(g, initialVertex));
    }

    private static void nearestNeighbourGRASP(Graph graph) {
        printResults(graph, "Nearest Neihbour - GRASP", (g, initialVertex) -> HalfTSPNearestNeighbour.GRASP(g, initialVertex));
    }

    private static void localSearch_nearestNeighbour(Graph graph) {
        printResults(graph, "Local Cycle with initial Nearest Neighbour", (g, initialVertex) -> HalfTSPLocalSearch.localSearch(g, initialVertex, (g2, initialVertex2) -> HalfTSPNearestNeighbour.nearestNeighbour(g2, initialVertex2)));
    }

    private static void localSearch_nearestNeighbourGRASP(Graph graph) {
        printResults(graph, "Local Cycle with initial Nearest Neighbour with GRASP", (g, initialVertex) -> HalfTSPLocalSearch.localSearch(g, initialVertex, (g2, initialVertex2) -> HalfTSPNearestNeighbour.GRASP(g2, initialVertex2)));
    }

    private static void localSearch_greedyCycle(Graph graph) {
        printResults(graph, "Local Cycle with initial Greedy Cycle", (g, initialVertex) -> HalfTSPLocalSearch.localSearch(g, initialVertex, (g2, initialVertex2) -> HalfTSPGreedyCycle.greedyCycle(g2, initialVertex2)));
    }

    private static void localSearch_greedyCycleGRASP(Graph graph) {
        printResults(graph, "Local Cycle with initial Greedy Cycle with GRASP", (g, initialVertex) -> HalfTSPLocalSearch.localSearch(g, initialVertex, (g2, initialVertex2) -> HalfTSPGreedyCycle.GRASP(g2, initialVertex2)));
    }
}
