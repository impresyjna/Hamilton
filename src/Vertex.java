import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;

/**
 * Created by impresyjna on 04.10.2016.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Vertex {

    @XmlElement(name="edge")
    private ArrayList<Edge> edges;

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public void getEdge(int id) {

    }
}
