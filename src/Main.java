import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by impresyjna on 04.10.2016.
 */
public class Main {
    public static void main(String [ ] args) throws FileNotFoundException {
        FileReader fileReader = new FileReader("kroA100.xml");
        Document document = null;
        try {
            document = (Document) JAXBContext.newInstance(Document.class).createUnmarshaller().unmarshal(fileReader);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        System.out.println(document.getName());
    }
}
