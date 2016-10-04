import javax.xml.bind.annotation.*;

/**
 * Created by impresyjna on 04.10.2016.
 */
@XmlRootElement(name="travellingSalesmanProblemInstance")
@XmlAccessorType(XmlAccessType.FIELD)
public class Document {

    @XmlElement
    public Graph graph;
    @XmlElement
    public String name;
    @XmlElement
    public String description;

    public Graph getGraph() {
        return graph;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
