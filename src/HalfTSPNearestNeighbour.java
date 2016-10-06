import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by impresyjna on 06.10.2016.
 */
public class HalfTSPNearestNeighbour {

    public static HalfTSPResult nearestNeighbour(Graph graph, int initialVertex) {
        HalfTSPResult result = new HalfTSPResult();
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
                    if(adjacencyMatrix[actualVertex][freeVertex] < nearestCost) {
                        nearestCost = adjacencyMatrix[actualVertex][freeVertex];
                        vertexToAdd = freeVertex;
                    }
            }
            cycleDistance += nearestCost;
            path.add(vertexToAdd);
            actualVertex = vertexToAdd;
            freeVertices.remove(new Integer(vertexToAdd));
        }

        result.setDistance(cycleDistance);
        result.setPath(path);
        return result;
    }
}
