import java.io.IOException;
import java.util.function.BiFunction;

/**
 * Created by impresyjna on 04.10.2016.
 */
public class Main {
    public static void main(String[] args) {
        Graph graph;
        try {
            graph = Graph.readTSPFile("kroA100.tsp");
/*
            //nearest neighbour
            nearestNeighbour(graph);
            nearestNeighbour_GRASP(graph);

            //greedyCycle
            greedyCycle(graph);
            greedyCycle_GRASP(graph);

            //lokalne wyszukiwanie
            localSearch_nearestNeighbour(graph);
            localSearch_nearestNeighbourGRASP(graph);
            localSearch_greedyCycle(graph);
            localSearch_greedyCycleGRASP(graph);
            localSearch_randomCycle(graph);
*/

            compareMultipleStartAndIteratedLocalSearch(graph, (g, initialVertex) -> HalfTSPNearestNeighbour.GRASP(g, initialVertex));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void compareMultipleStartAndIteratedLocalSearch(Graph graph, BiFunction<Graph, Integer, HalfTSPResult> algorithm) {
        System.out.println("Multiple Start Local Search");
        int repeatNumber = 10;
        HalfTSPResult_LocalSearch minResult = null;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int sum = 0;
        int average;

        long minTime = Long.MAX_VALUE;
        long maxTime = Long.MIN_VALUE;
        long sumTime = 0;
        double averageTime;

        for (int i = 0; i < repeatNumber; ++i) {
            long startTime = System.nanoTime();
            HalfTSPResult_LocalSearch result = HalfTSPLocalSearch.multipleStartLocalSearch(graph, algorithm, 1000);
            int distance = result.getDistance();
            if (min > distance) {
                minResult = result;
                min = distance;
            } else if (max < distance) {
                max = distance;
            }
            sum += distance;
            long endTime = System.nanoTime();

            long timeDifference = endTime - startTime;
            if (minTime > timeDifference) {
                minTime = timeDifference;
            }
            if (maxTime < timeDifference) {
                maxTime = timeDifference;
            }
            sumTime += timeDifference;
        }
        average = sum / repeatNumber;
        System.out.println("MAX distance: " + max);
        System.out.println("MIN distance: " + min);
        System.out.println("AVERAGE distance: " + average);

        averageTime = sumTime / (repeatNumber * 1.0);
        System.out.println("MAX time: " + maxTime / 1000000.0 + "ms");
        System.out.println("MIN time: " + minTime / 1000000.0 + "ms");
        System.out.println("AVERAGE time: " + averageTime / 1000000.0 + "ms");

        HalfTSPResult minResult_InitialResult = minResult.getInitialResult();
        System.out.println("MIN initial distance: " + minResult_InitialResult.getDistance());
        System.out.println("MIN initial cycle:");
        for (int vertex : minResult_InitialResult.getPath()) {
            System.out.print(vertex + " -> ");
        }
        System.out.println(minResult_InitialResult.getPath().get(0));

        System.out.println("MIN cycle:");
        for (int vertex : minResult.getPath()) {
            System.out.print(vertex + " -> ");
        }
        System.out.println(minResult.getPath().get(0));
        System.out.println();


        System.out.println("Iterated Local Search");

        minResult = null;
        min = Integer.MAX_VALUE;
        max = Integer.MIN_VALUE;
        sum = 0;

        minTime = Long.MAX_VALUE;
        maxTime = Long.MIN_VALUE;
        sumTime = 0;

        for (int i = 0; i < repeatNumber; ++i) {
            long startTime = System.nanoTime();
            HalfTSPResult_LocalSearch result = HalfTSPLocalSearch.iteratedLocalSearch(graph, algorithm, (long) averageTime);
            int distance = result.getDistance();
            if (min > distance) {
                minResult = result;
                min = distance;
            }
            if (max < distance) {
                max = distance;
            }
            sum += distance;
            long endTime = System.nanoTime();

            long timeDifference = endTime - startTime;
            if (minTime > timeDifference) {
                minTime = timeDifference;
            } else if (maxTime < timeDifference) {
                maxTime = timeDifference;
            }
            sumTime += timeDifference;
        }
        average = sum / repeatNumber;
        System.out.println("MAX distance: " + max);
        System.out.println("MIN distance: " + min);
        System.out.println("AVERAGE distance: " + average);

        averageTime = sumTime / (repeatNumber * 1.0);
        System.out.println("MAX time: " + maxTime / 1000000.0 + "ms");
        System.out.println("MIN time: " + minTime / 1000000.0 + "ms");
        System.out.println("AVERAGE time: " + averageTime / 1000000.0 + "ms");

        minResult_InitialResult = minResult.getInitialResult();
        System.out.println("MIN initial distance: " + minResult_InitialResult.getDistance());
        System.out.println("MIN initial cycle:");
        for (int vertex : minResult_InitialResult.getPath()) {
            System.out.print(vertex + " -> ");
        }
        System.out.println(minResult_InitialResult.getPath().get(0));

        System.out.println("MIN cycle:");
        for (int vertex : minResult.getPath()) {
            System.out.print(vertex + " -> ");
        }
        System.out.println(minResult.getPath().get(0));
        System.out.println();
    }

    private static void printResults(Graph graph, String algorithmName, BiFunction<Graph, Integer, HalfTSPResult> algorithm) {
        System.out.println(algorithmName);
        HalfTSPResult minResult = null;
        HalfTSPResult result;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int sum = 0;
        int average;
        long start = System.nanoTime();
        for (int initialVertex = 0; initialVertex < graph.getVerticesCount(); ++initialVertex) {
            result = algorithm.apply(graph, initialVertex);
            int distance = result.getDistance();
            sum += distance;
            if (distance > max) {
                max = distance;
            } else if (distance < min) {
                min = distance;
                minResult = result;
            }
        }
        long time = System.nanoTime() - start;
        average = sum / graph.getVerticesCount();
        System.out.println("MAX distance: " + max);
        System.out.println("MIN distance: " + min);
        System.out.println("AVERAGE distance: " + average);
        System.out.println("TIME: " + time / 1000000.0 + "ms");

        //Wstępny wynik w algorytmie local search
        if (minResult.getClass() == HalfTSPResult_LocalSearch.class) {
            HalfTSPResult_LocalSearch minResult_localSearch = (HalfTSPResult_LocalSearch) minResult;
            HalfTSPResult minResult_localSearchInitialResult = minResult_localSearch.getInitialResult();
            System.out.println("MIN initial distance: " + minResult_localSearchInitialResult.getDistance());
            System.out.println("MIN initial cycle:");
            for (int vertex : minResult_localSearchInitialResult.getPath()) {
                System.out.print(vertex + " -> ");
            }
            System.out.println(minResult_localSearchInitialResult.getPath().get(0));
        }

        System.out.println("MIN cycle:");
        for (int vertex : minResult.getPath()) {
            System.out.print(vertex + " -> ");
        }
        System.out.println(minResult.getPath().get(0));
        System.out.println();
    }

    private static void nearestNeighbour(Graph graph) {
        printResults(graph, "Nearest neighbour", (g, initialVertex) -> HalfTSPNearestNeighbour.nearestNeighbour(g, initialVertex));
    }

    private static void nearestNeighbour_GRASP(Graph graph) {
        printResults(graph, "Nearest neighbour with GRASP", (g, initialVertex) -> HalfTSPNearestNeighbour.GRASP(g, initialVertex));
    }

    private static void greedyCycle(Graph graph) {
        printResults(graph, "Greedy cycle", (g, initialVertex) -> HalfTSPGreedyCycle.greedyCycle(g, initialVertex));
    }

    private static void greedyCycle_GRASP(Graph graph) {
        printResults(graph, "Greedy cycle with GRASP", (g, initialVertex) -> HalfTSPGreedyCycle.GRASP(g, initialVertex));
    }

    private static void localSearch_nearestNeighbour(Graph graph) {
        printResults(graph, "Local Search with initial Nearest Neighbour", (g, initialVertex) -> HalfTSPLocalSearch.localSearch(g, initialVertex, (g2, initialVertex2) -> HalfTSPNearestNeighbour.nearestNeighbour(g2, initialVertex2)));
    }

    private static void localSearch_nearestNeighbourGRASP(Graph graph) {
        printResults(graph, "Local Search with initial Nearest Neighbour with GRASP", (g, initialVertex) -> HalfTSPLocalSearch.localSearch(g, initialVertex, (g2, initialVertex2) -> HalfTSPNearestNeighbour.GRASP(g2, initialVertex2)));
    }

    private static void localSearch_greedyCycle(Graph graph) {
        printResults(graph, "Local Search with initial Greedy Cycle", (g, initialVertex) -> HalfTSPLocalSearch.localSearch(g, initialVertex, (g2, initialVertex2) -> HalfTSPGreedyCycle.greedyCycle(g2, initialVertex2)));
    }

    private static void localSearch_greedyCycleGRASP(Graph graph) {
        printResults(graph, "Local Search with initial Greedy Cycle with GRASP", (g, initialVertex) -> HalfTSPLocalSearch.localSearch(g, initialVertex, (g2, initialVertex2) -> HalfTSPGreedyCycle.GRASP(g2, initialVertex2)));
    }

    private static void localSearch_randomCycle(Graph graph) {
        printResults(graph, "Local Search with initial random Cycle", (g, initialVertex) -> HalfTSPLocalSearch.localSearch(g, initialVertex, (g2, initialVertex2) -> HalfTSPRandom.randomCycle(g2, initialVertex2)));
    }
}
