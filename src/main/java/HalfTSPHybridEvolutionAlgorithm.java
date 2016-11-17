import javafx.util.Pair;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by jakub on 08.11.2016.
 */
public class HalfTSPHybridEvolutionAlgorithm {
    public static HalfTSPResult_HybridEvolutionAlgorithm algorithm(Graph graph, BiFunction<Graph, Integer, HalfTSPResult> algorithm, long constraint) {
        long startTime = System.nanoTime();
        ArrayList<Pair<Long, Integer>> chartData = new ArrayList<>();
        int verticesCount = graph.getVerticesCount();
        int[][] adjacencyMatrix = graph.getAdjacencyMatrix();
        Random generator = new Random();
        ArrayList<HalfTSPResult> population = new ArrayList<>();
        HalfTSPResult minResult = null;
        for (int i = 0; i < 20; ++i) {
            HalfTSPResult result;
            do {
                result = algorithm.apply(graph, generator.nextInt(verticesCount));
                final int dist = result.getDistance();
                if (population.stream().noneMatch(p -> p.getDistance() == dist)) {
                    break;
                }
            } while (true);
            population.add(result);
            if (minResult == null || minResult.getDistance() > result.getDistance()) {
                minResult = result;
            }
        }

        int iteration = 1;
        while (System.nanoTime() - startTime < constraint) {
            int index1 = generator.nextInt(20);
            int index2;
            do {
                index2 = generator.nextInt(20);
            } while (index1 == index2);
            ArrayList<Integer> path1 = population.get(index1).getPath();
            ArrayList<Integer> path2 = population.get(index2).getPath();
            ArrayList<Integer> newPath = new ArrayList<>();

            ArrayList<ArrayList<Integer>> intersection = intersect(path1, path2);
            while (!intersection.isEmpty()) {
                ArrayList<Integer> fragment = intersection.get(generator.nextInt(intersection.size()));
                intersection.remove(fragment);
                if (generator.nextBoolean()) {
                    Collections.reverse(fragment);
                }

                newPath.addAll(fragment);
            }
            List<Integer> freeVertices = IntStream.range(0, verticesCount).filter(v -> !newPath.contains(v)).boxed().collect(Collectors.toList());
            int verticesInPath = verticesCount / 2;
            while (newPath.size() < verticesInPath) {
                int randIndex = generator.nextInt(freeVertices.size());
                int nextVertex = freeVertices.get(randIndex);
                freeVertices.remove(randIndex);
                newPath.add(nextVertex);
            }
            int distance = 0;
            for (int i = 0; i < verticesInPath; ++i) {
                int vertex1 = newPath.get(i);
                int vertex2 = newPath.get((i + 1) % verticesInPath);
                distance += adjacencyMatrix[vertex1][vertex2];
            }
            HalfTSPResult initialResult = new HalfTSPResult(distance, newPath);

            long localSearchStartTime = System.nanoTime();
            HalfTSPResult newResult = HalfTSPLocalSearch.localSearch(graph, -1, (g, init) -> initialResult);
            chartData.add(new Pair<>(System.nanoTime() - localSearchStartTime, newResult.getDistance()));

            HalfTSPResult maxResult = population.stream().max((r1, r2) -> r1.getDistance() < r2.getDistance() ? -1 : 1).get();

            if (maxResult.getDistance() > newResult.getDistance()) {
                population.remove(maxResult);
                population.add(newResult);
            }

            if (minResult.getDistance() > newResult.getDistance()) {
                minResult = newResult;
            }
        }
        return new HalfTSPResult_HybridEvolutionAlgorithm(minResult.getDistance(), minResult.getPath(), chartData);
    }

    private static ArrayList<ArrayList<Integer>> intersect(ArrayList<Integer> path1, ArrayList<Integer> path2) {
        List<Pair<Integer, Integer>> edges1 = new ArrayList<>();
        List<Pair<Integer, Integer>> edges2 = new ArrayList<>();
        List<Pair<Integer, Integer>> revertEdges2 = new ArrayList<>();
        int verticesInPath = path1.size();
        for (int i = 0; i < verticesInPath; ++i) {
            edges1.add(new Pair<>(path1.get(i), path1.get((i + 1) % verticesInPath)));
            edges2.add(new Pair<>(path2.get(i), path2.get((i + 1) % verticesInPath)));
            revertEdges2.add(new Pair<>(path2.get((i + 1) % verticesInPath), path2.get(i)));
        }

        List<Pair<Integer, Integer>> edges1And2 = edges1.stream().filter(e -> edges2.contains(e) || revertEdges2.contains(e)).collect(Collectors.toList());
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        ArrayList<Integer> subpath = new ArrayList<>();
        ArrayList<Integer> verticesInResult = new ArrayList<>();
        if (edges1And2.size() == verticesInPath) {
            result.add(path1);
            return result;
        }
        if (!edges1And2.isEmpty()) {
            Pair<Integer, Integer> initialEdge = edges1And2.get(0);
            while (true) {
                int key = initialEdge.getKey();
                if (edges1And2.stream().noneMatch(e -> e.getValue() == key)) {
                    break;
                }
                initialEdge = edges1And2.stream().filter(e -> e.getValue() == key).findFirst().get();
            }

            int lastVertex = initialEdge.getKey();
            subpath.add(lastVertex);
            while (!edges1And2.isEmpty()) {
                int key = lastVertex;
                if (edges1And2.stream().noneMatch(e -> e.getKey() == key)) {
                    result.add(subpath);
                    verticesInResult.addAll(subpath);
                    subpath = new ArrayList<>();
                    initialEdge = edges1And2.get(0);
                    while (true) {
                        int initialKey = initialEdge.getKey();
                        if (edges1And2.stream().noneMatch(e -> e.getValue() == initialKey)) {
                            break;
                        }
                        initialEdge = edges1And2.stream().filter(e -> e.getValue() == initialKey).findFirst().get();
                    }
                    lastVertex = initialEdge.getKey();
                    subpath.add(lastVertex);
                }
                int key2 = lastVertex;
                Pair<Integer, Integer> edge = edges1And2.stream().filter(e -> e.getKey() == key2).findFirst().get();
                lastVertex = edge.getValue();
                subpath.add(lastVertex);
                edges1And2.remove(edge);
            }
            result.add(subpath);
            verticesInResult.addAll(subpath);
        }
        for (int vertex : path1.stream().filter(v -> (path2.contains(v) && !verticesInResult.contains(v))).collect(Collectors.toList())) {
            subpath = new ArrayList<>();
            subpath.add(vertex);
            result.add(subpath);
        }
        return result;
    }
}
