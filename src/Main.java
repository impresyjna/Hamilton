import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by impresyjna on 04.10.2016.
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        FileReader fileReader = new FileReader("kroA100.xml");
        Document document = null;
        try {
            document = (Document) JAXBContext.newInstance(Document.class).createUnmarshaller().unmarshal(fileReader);
            Graph graph = document.getGraph();

            //GreedyCycle wersja deterministyczna
            greedyCycle(graph);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private static void greedyCycle(Graph graph){
        ArrayList<Integer> minPath = new ArrayList<>();
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int sum = 0;
        int average;
        for (int initialVertex = 0; initialVertex < graph.getVerticesCount(); ++initialVertex) {
            HalfTSPResult result = HalfTSPGreedyCycle.GreedyCycle(graph, initialVertex);
            int distance = result.getDistance();
            sum += distance;
            if (distance > max) {
                max = distance;
            } else if (distance < min) {
                min = distance;
                minPath = result.getPath();
            }
        }
        average = sum / graph.getVerticesCount();
        System.out.println("MAX: " + max);
        System.out.println("MIN: " + min);
        System.out.println("AVERAGE: " + average);
        for (int vertex : minPath) {
            System.out.print(vertex + " -> ");
        }
        System.out.println(minPath.get(0));
    }
}
