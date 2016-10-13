import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Jakub on 05.10.2016.
 */
public class HalfTSPGreedyCycle {

    public static HalfTSPResult greedyCycle(Graph graph, int initialVertex) {
        ArrayList<Integer> path = new ArrayList<>();
        int cycleDistance = 0;
        List<Integer> freeVertices = IntStream.range(0, graph.getVerticesCount()).boxed().collect(Collectors.toList());

        int[][] adjacencyMatrix = graph.getAdjacencyMatrix();
        int verticesCount = graph.getVerticesCount();
        int verticesInPath = verticesCount / 2;

        path.add(initialVertex);
        freeVertices.remove(new Integer(initialVertex));

        for (int currentVerticesInPath = 1; currentVerticesInPath < verticesInPath; ++currentVerticesInPath) {
            int minChange = Integer.MAX_VALUE;
            int vertexToAdd = -1;
            int vertexToAddIndex = -1;
            for (int freeVertex : freeVertices) {
                for (int i = 0; i < currentVerticesInPath; ++i) {
                    int vertex1 = path.get(i);
                    int vertex2 = path.get((i + 1) % currentVerticesInPath);
                    int change = adjacencyMatrix[vertex1][freeVertex] + adjacencyMatrix[freeVertex][vertex2] - adjacencyMatrix[vertex1][vertex2];
                    if (change < minChange) {
                        minChange = change;
                        vertexToAdd = freeVertex;
                        vertexToAddIndex = i + 1;
                    }
                }
            }
            cycleDistance += minChange;
            path.add(vertexToAddIndex, vertexToAdd);
            freeVertices.remove(new Integer(vertexToAdd));
        }

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

        path.add(initialVertex);
        freeVertices.remove(new Integer(initialVertex));

        for (int currentVerticesInPath = 1; currentVerticesInPath < verticesInPath; ++currentVerticesInPath) {
            int[] minChanges = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
            int[] bestVerticesToAdd = {-1, -1, -1};
            int[] bestVerticesToAddIndex = {-1, -1, -1};
            for (int freeVertex : freeVertices) {
                for (int i = 0; i < currentVerticesInPath; ++i) {
                    int vertex1 = path.get(i);
                    int vertex2 = path.get((i + 1) % currentVerticesInPath);
                    int change = adjacencyMatrix[vertex1][freeVertex] + adjacencyMatrix[freeVertex][vertex2] - adjacencyMatrix[vertex1][vertex2];
                    int index;
                    for (index = 0; index < 3 && minChanges[index] < change; ++index) ;
                    for (int j = 2; j > index; --j) {
                        minChanges[j] = minChanges[j - 1];
                        bestVerticesToAdd[j] = bestVerticesToAdd[j - 1];
                        bestVerticesToAddIndex[j] = bestVerticesToAddIndex[j - 1];
                    }
                    if (index < 3) {
                        minChanges[index] = change;
                        bestVerticesToAdd[index] = freeVertex;
                        bestVerticesToAddIndex[index] = i + 1;
                    }
                }
            }
            int index = generator.nextInt(3);
            cycleDistance += minChanges[index];
            path.add(bestVerticesToAddIndex[index], bestVerticesToAdd[index]);
            freeVertices.remove(new Integer(bestVerticesToAdd[index]));
        }

        return new HalfTSPResult(cycleDistance, path);
    }
}
