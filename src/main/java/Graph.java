import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.xml.bind.annotation.*;

/**
 * Created by impresyjna on 04.10.2016.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Graph {

    @XmlElement(name = "vertex")
    private ArrayList<Vertex> vertices;

    private int[][] adjacencyMatrix = null;

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(ArrayList<Vertex> vertices) {
        this.vertices = vertices;
    }

    public Vertex getVertex(int id) {
        return vertices.get(id);
    }

    public int getVerticesCount() {
        return vertices.size();
    }

    public int[][] getAdjacencyMatrix() {
        if (adjacencyMatrix == null) {
            int matrixDim = vertices.size();
            adjacencyMatrix = new int[matrixDim][matrixDim];
            for (int i = 0; i < vertices.size(); ++i) {
                ArrayList<Edge> edges = getVertex(i).getEdges();
                for (Edge edge : edges) {
                    adjacencyMatrix[i][edge.getDestination()] = (int) Math.round(edge.getCost());
                }
            }
        }
        return adjacencyMatrix;
    }

    public static Graph readTSPFile(String fileName) throws IOException {
        Graph result = new Graph();

        ArrayList<Double> xCoordinates = new ArrayList<>();
        ArrayList<Double> yCoordinates = new ArrayList<>();
        Files.lines(Paths.get(fileName)).forEach(line -> {
            String[] lineElements = line.split(" ");
            if (lineElements.length == 3) {
                try {
                    Double.parseDouble(lineElements[0]);
                    double x = Double.parseDouble(lineElements[1]);
                    double y = Double.parseDouble(lineElements[2]);
                    xCoordinates.add(x);
                    yCoordinates.add(y);
                } catch (NumberFormatException e) {
                }
            }
        });

        int verticesCount = xCoordinates.size();
        ArrayList<Edge>[] edges = new ArrayList[verticesCount];

        for (int i = 0; i < verticesCount; ++i) {
            edges[i] = new ArrayList<>();
        }

        for (int firstVertex = 0; firstVertex < verticesCount; ++firstVertex) {
            double xFirst = xCoordinates.get(firstVertex);
            double yFirst = yCoordinates.get(firstVertex);
            for (int secondVertex = firstVertex + 1; secondVertex < verticesCount; ++secondVertex) {
                double xSecond = xCoordinates.get(secondVertex);
                double ySecond = yCoordinates.get(secondVertex);
                int cost = (int) Math.round(Math.sqrt((xSecond - xFirst) * (xSecond - xFirst) + (ySecond - yFirst) * (ySecond - yFirst)));

                Edge edge1 = new Edge();
                edge1.setCost(cost);
                edge1.setDestination(secondVertex);
                edges[firstVertex].add(edge1);

                Edge edge2 = new Edge();
                edge2.setCost(cost);
                edge2.setDestination(firstVertex);
                edges[secondVertex].add(edge2);
            }
        }

        ArrayList<Vertex> vertices = new ArrayList<>();
        for (int i = 0; i < verticesCount; ++i) {
            Vertex vertex = new Vertex();
            vertex.setEdges(edges[i]);
            vertices.add(vertex);
        }
        result.setVertices(vertices);
        return result;
    }
}
