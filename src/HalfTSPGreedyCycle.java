import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Jakub on 05.10.2016.
 */
public class HalfTSPGreedyCycle {

    public static HalfTSPResult GreedyCycle(Graph graph, int initialVertex) {
        HalfTSPResult result = new HalfTSPResult();
        ArrayList<Integer> path = new ArrayList<>();
        List<Integer> freeVertices = IntStream.range(0, graph.getVerticesCount()).boxed().collect(Collectors.toList());

        int[][] adjacencyMatrix = graph.getAdjacencyMatrix();
        int verticesCount = graph.getVerticesCount();
        int verticesInPath = verticesCount / 2;

        path.add(initialVertex);
        freeVertices.remove(new Integer(initialVertex));
        int minDistance = Integer.MAX_VALUE;
        int nearestVertex = -1;
        for (int vertex : freeVertices) {
            if (adjacencyMatrix[initialVertex][vertex] < minDistance) {
                minDistance = adjacencyMatrix[initialVertex][vertex];
                nearestVertex = vertex;
            }
        }

        path.add(nearestVertex);
        freeVertices.remove(new Integer(nearestVertex));

        for (int currentVerticesInPath = 2; currentVerticesInPath < verticesInPath; ++currentVerticesInPath) {
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
            path.add(vertexToAddIndex, vertexToAdd);
            freeVertices.remove(new Integer(vertexToAdd));
        }

        int pathDistance = 0;
        for (int i = 0; i < verticesInPath; ++i) {
            int vertex1 = path.get(i);
            int vertex2 = path.get((i + 1) % verticesInPath);
            pathDistance += adjacencyMatrix[vertex1][vertex2];
        }
        result.setDistance(pathDistance);
        result.setPath(path);
        return result;
    }
}
