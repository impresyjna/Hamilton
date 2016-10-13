import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by jakub on 11.10.2016.
 */
public class HalfTSPLocalSearch {
    public static HalfTSPResult localSearch(Graph graph, int initialVertex, BiFunction<Graph, Integer, HalfTSPResult> algorithm) {
        boolean notFound;
        HalfTSPResult initialResult = algorithm.apply(graph, initialVertex);
        ArrayList<Integer> cycle = initialResult.getPath();
        int distance = initialResult.getDistance();
        List<Integer> freeVertices = IntStream.range(0, graph.getVerticesCount()).filter(v -> !cycle.contains(v)).boxed().collect(Collectors.toList());
        int verticesInCycleCount = graph.getVerticesCount() / 2;
        int[][] adjacencyMatrix = graph.getAdjacencyMatrix();
        do {
            notFound = true;
            int minChange = 0;
            int vertex1IndexToChange = -1;
            int vertex2IndexToChange = -1;
            int vertexToReplace = -1;
            boolean changeInsideCycle = false;

            for (int index1 = 0; index1 < verticesInCycleCount; ++index1) {
                int vertex1 = cycle.get(index1);
                int vertex1Next = cycle.get((index1 + 1) % verticesInCycleCount);
                int vertex1Prev = cycle.get((index1 + verticesInCycleCount - 1) % verticesInCycleCount);

                //wymiana łuków 2-opt
                for (int index2 = index1 + 2; index2 < verticesInCycleCount && index2 != (index1 - 1) % verticesInCycleCount; ++index2) {
                    int vertex2 = cycle.get(index2);
                    int vertex2Next = cycle.get((index2 + 1) % verticesInCycleCount);
                    int change = -adjacencyMatrix[vertex1][vertex1Next] - adjacencyMatrix[vertex2][vertex2Next] +
                            adjacencyMatrix[vertex1][vertex2] + adjacencyMatrix[vertex1Next][vertex2Next];
                    if (change < minChange) {
                        minChange = change;
                        vertex1IndexToChange = index1;
                        vertex2IndexToChange = index2;
                        changeInsideCycle = true;
                    }
                }

                //wymiana wierzchołków 2-opt
                for (int vertex2 : freeVertices) {
                    int change = -adjacencyMatrix[vertex1][vertex1Prev] - adjacencyMatrix[vertex1][vertex1Next] +
                            adjacencyMatrix[vertex2][vertex1Prev] + adjacencyMatrix[vertex2][vertex1Next];
                    if (change < minChange) {
                        minChange = change;
                        vertex1IndexToChange = index1;
                        vertexToReplace = vertex2;
                        changeInsideCycle = false;
                    }
                }

                if (minChange < 0) {
                    notFound = false;
                    if (changeInsideCycle) {
                        ArrayList<Integer> subPath = new ArrayList<>(cycle.subList(vertex1IndexToChange + 1, vertex2IndexToChange));
                        cycle.removeAll(subPath);
                        Collections.reverse(subPath);
                        cycle.addAll(vertex1IndexToChange + 1, subPath);
                    } else {
                        cycle.remove(vertex1IndexToChange);
                        cycle.add(vertex1IndexToChange, vertexToReplace);
                        distance += minChange;
                    }
                }
            }
        } while (notFound);
        return new HalfTSPResult(distance, cycle);
    }
}
