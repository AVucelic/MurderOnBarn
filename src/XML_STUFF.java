import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * XML_STUFF - a way to make an xml file and someting with it 
 * @author Luka Lasic
 * @since 20-4-2023
 */
public class XML_STUFF {
    public AmongUsSettings player = null;
    /**
     * A construotr to inlize the amongUsSettings
     */
    public XML_STUFF(){
        player = new AmongUsSettings();
    }
    /**
     * writing the infomraiton from among Us Setting to an xml file 
     */
    public void writeXML() {
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

    /**
     * readXML - able to read the file from the xml file 
     */
    public void readXML() {
        File xmlFile = new File("settings.xml");

        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(AmongUsSettings.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            this.player = (AmongUsSettings) jaxbUnmarshaller.unmarshal(xmlFile);
            System.out.println(player);
            
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        new XML_STUFF().writeXML();
        new XML_STUFF().readXML();

    }

    



}
