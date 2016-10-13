import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by impresyjna on 12.10.2016.
 */
public class HalfTSPRandom {
    public static HalfTSPResult randomCycle(Graph graph, int initialVertex) {
        ArrayList<Integer> path = new ArrayList<>();
        int cycleDistance = 0;
        List<Integer> freeVertices = IntStream.range(0, graph.getVerticesCount()).boxed().collect(Collectors.toList());

        int[][] adjacencyMatrix = graph.getAdjacencyMatrix();
        int verticesCount = graph.getVerticesCount();
        int verticesInPath = verticesCount / 2;
        int actualVertex = initialVertex;

        path.add(initialVertex);
        freeVertices.remove(new Integer(initialVertex));

        Random generator = new Random();

        for (int currentVerticesInPath = 1; currentVerticesInPath < verticesInPath; ++currentVerticesInPath) {
            int randomInt = generator.nextInt(freeVertices.size());
            int vertexToAdd = freeVertices.get(randomInt);
            cycleDistance += adjacencyMatrix[actualVertex][vertexToAdd];
            path.add(vertexToAdd);
            actualVertex = vertexToAdd;
            freeVertices.remove(new Integer(vertexToAdd));
        }
        cycleDistance += adjacencyMatrix[actualVertex][initialVertex];

        return new HalfTSPResult(cycleDistance, path);
    }
}
