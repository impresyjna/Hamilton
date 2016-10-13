import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.BiFunction;

/**
 * Created by impresyjna on 04.10.2016.
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        FileReader fileReader = new FileReader("kroA100.xml");
        Document document = null;
        try {
            document = (Document) JAXBContext.newInstance(Document.class).createUnmarshaller().unmarshal(fileReader);
            //Graph graph = document.getGraph();
            Graph graph = Graph.readTSPFile("kroA100.tsp");
            //greedyCycle wersja deterministyczna
            greedyCycle(graph);

            //greedyCycle wersja niedeterministyczna
            greedyCycle_GRASP(graph);

            //nearestNeighbour wersja deterministyczna
            nearestNeighbour(graph);

            //nearestNeighbour wersja niedeterministyczna
            nearestNeighbourGRASP(graph);

            //randomCycle wersja deterministyczna
            randomCycle(graph);

        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printResults(Graph graph, String algorithmName, BiFunction<Graph, Integer, HalfTSPResult> algorithm){
        System.out.println(algorithmName);
        ArrayList<Integer> minPath = new ArrayList<>();
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int sum = 0;
        int average;
        for (int initialVertex = 0; initialVertex < graph.getVerticesCount(); ++initialVertex) {
            HalfTSPResult result = algorithm.apply(graph, initialVertex);
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
        printResults(graph, "GreedyCycle", (g, initialVertex) ->HalfTSPGreedyCycle.greedyCycle(g, initialVertex));
    }

    private static void greedyCycle_GRASP(Graph graph) {
        printResults(graph, "GreedyCycle - GRASP", (g, initialVertex)->HalfTSPGreedyCycle.GRASP(g, initialVertex));
    }

    private static void nearestNeighbour(Graph graph) {
        printResults(graph, "Nearest Neihbour", (g, initialVertex)->HalfTSPNearestNeighbour.nearestNeighbour(g, initialVertex));
    }

    private static void nearestNeighbourGRASP(Graph graph) {
        printResults(graph, "Nearest Neihbour - GRASP", (g, initialVertex)->HalfTSPNearestNeighbour.GRASP(g, initialVertex));
    }

    private static void randomCycle(Graph graph) {
        printResults(graph, "Random", (g, initialVertex)->HalfTSPRandom.randomCycle(g, initialVertex));
    }
}
