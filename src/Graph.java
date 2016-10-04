import java.util.ArrayList;
import javax.xml.bind.annotation.*;

/**
 * Created by impresyjna on 04.10.2016.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Graph {

    @XmlElement(name = "vertex")
    private ArrayList<Vertex> vertices;

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(ArrayList<Vertex> vertices) {
        this.vertices = vertices;
    }

    public Vertex getVertex(int id) {
        return vertices.get(id);
    }

    public int[][] getAdjacencyMatrix()
    {
        int matrixDim = vertices.size();
        int[][] adjacencyMatrix = new int[matrixDim][matrixDim];
        for (int i = 0; i < vertices.size(); ++i){
            ArrayList<Edge> edges = getVertex(i).getEdges();
            for (Edge edge : edges) {
                adjacencyMatrix[i][edge.getDestination()] = edge.getCost();
            }
        }
    }
}
