package dps.hoffmann.springconsumer.utils;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class XmlUtils {

    /**
     * Checks if a xml content conforms to a given xsd specification
     * src: https://www.journaldev.com/895/how-to-validate-xml-against-xsd-in-java
     *
     * @param xsdPath path to the xsd specification
     * @param xmlContent xml content to check
     * @return true if the xml content conforms to the xsd specification
     */
    public static boolean isValidXMLSchema(String xsdPath, String xmlContent){
        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            // todo
            validator.validate(new StreamSource(new File(xmlContent)));
        } catch (IOException | SAXException e) {
            System.out.println("Exception: "+e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Extracts an element from a xml content using a xpath specification
     * @param xPath path to the element to extract
     * @param xmlContent content from which the element should be extracted
     * @return element from the xml content
     */
    public String extractElem(String xPath, String xmlContent) {
        // todo
        return "todo extract elem";
    }

}
