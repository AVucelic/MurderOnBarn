import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XML_STUFF {

    void writeXML() {
        // Example how to write
        AmongUsSettings amongUsSettings = new AmongUsSettings("127.0.0.1", 32001, 10.0, 2, 30, 100);

        try {

            File file = new File("settings.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(AmongUsSettings.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(amongUsSettings, file);
            jaxbMarshaller.marshal(amongUsSettings, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    void readXML() {
        File xmlFile = new File("settings.xml");

        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(AmongUsSettings.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            AmongUsSettings employee = (AmongUsSettings) jaxbUnmarshaller.unmarshal(xmlFile);
            System.out.println(employee);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new XML_STUFF().writeXML();
        new XML_STUFF().readXML();

    }

}
