import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by impresyjna on 06.10.2016.
 */
public class HalfTSPNearestNeighbour {

    public static HalfTSPResult nearestNeighbour(Graph graph, int initialVertex) {
        ArrayList<Integer> path = new ArrayList<>();
        int cycleDistance = 0;
        List<Integer> freeVertices = IntStream.range(0, graph.getVerticesCount()).boxed().collect(Collectors.toList());

        int[][] adjacencyMatrix = graph.getAdjacencyMatrix();
        int verticesCount = graph.getVerticesCount();
        int verticesInPath = verticesCount / 2;
        int actualVertex = initialVertex;

        path.add(initialVertex);
        freeVertices.remove(new Integer(initialVertex));

        for (int currentVerticesInPath = 1; currentVerticesInPath < verticesInPath; ++currentVerticesInPath) {
            int nearestCost = Integer.MAX_VALUE;
            int vertexToAdd = -1;
            for (int freeVertex : freeVertices) {
                if (adjacencyMatrix[actualVertex][freeVertex] < nearestCost) {
                    nearestCost = adjacencyMatrix[actualVertex][freeVertex];
                    vertexToAdd = freeVertex;
                }
            }
            cycleDistance += nearestCost;
            path.add(vertexToAdd);
            actualVertex = vertexToAdd;
            freeVertices.remove(new Integer(vertexToAdd));
        }
        cycleDistance += adjacencyMatrix[actualVertex][initialVertex];

        return new HalfTSPResult(cycleDistance, path);
    }

    public static HalfTSPResult GRASP(Graph graph, int initialVertex) {
        Random generator = new Random();

        ArrayList<Integer> path = new ArrayList<>();
        int cycleDistance = 0;
        List<Integer> freeVertices = IntStream.range(0, graph.getVerticesCount()).boxed().collect(Collectors.toList());

        int[][] adjacencyMatrix = graph.getAdjacencyMatrix();
        int verticesCount = graph.getVerticesCount();
        int verticesInPath = verticesCount / 2;
        int actualVertex = initialVertex;

        path.add(initialVertex);
        freeVertices.remove(new Integer(initialVertex));

        for (int currentVerticesInPath = 1; currentVerticesInPath < verticesInPath; ++currentVerticesInPath) {
            int[] nearestCost = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
            int[] bestVerticesToAdd = {-1, -1, -1};
            int index = 0;
            for (int freeVertex : freeVertices) {
                for (index = 0; index < 3 && adjacencyMatrix[actualVertex][freeVertex] > nearestCost[index]; ++index) ;
                for (int j = 2; j > index; --j) {
                    nearestCost[j] = nearestCost[j - 1];
                    bestVerticesToAdd[j] = bestVerticesToAdd[j - 1];
                }
                if (index < 3) {
                    nearestCost[index] = adjacencyMatrix[actualVertex][freeVertex];
                    bestVerticesToAdd[index] = freeVertex;
                }
            }
            int randomInt = generator.nextInt(3);
            cycleDistance += nearestCost[randomInt];
            path.add(bestVerticesToAdd[randomInt]);
            actualVertex = bestVerticesToAdd[randomInt];
            freeVertices.remove(new Integer(bestVerticesToAdd[randomInt]));
        }

        cycleDistance += adjacencyMatrix[actualVertex][initialVertex];

        return new HalfTSPResult(cycleDistance, path);
    }
}
