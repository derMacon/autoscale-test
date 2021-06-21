package dps.hoffmann.springconsumer.utils;

import dps.hoffmann.springconsumer.service.ExtractionService;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;

public class XmlUtils {

    /**
     * Checks if a xml content conforms to a given xsd specification
     * src: https://www.journaldev.com/895/how-to-validate-xml-against-xsd-in-java
     *
     * @param xsdPath    path to the xsd specification
     * @param xmlContent xml content to check
     * @return true if the xml content conforms to the xsd specification
     */
//    public static boolean isValidXMLSchema(String xsdPath, String xmlContent) {
//        try {
//            SchemaFactory factory =
//                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//            Schema schema = factory.newSchema();
//            schema.
//            Validator validator = schema.newValidator();
//            validator.validate(new StreamSource(new StringReader(xmlContent)));
//        } catch (IOException | SAXException e) {
//            System.out.println("Exception: " + e.getMessage());
//            return false;
//        }
//        return true;
//    }

    // todo remove sneaky throw
    @SneakyThrows
    public static boolean validateAgainstXSD(String xmlStr, String xsdPath) {
        String xsdContent = ResourceUtils.readResource(ExtractionService.class, xsdPath);
        InputStream xsdInput = IOUtils.toInputStream(xsdContent, "UTF-8");
        InputStream xmlInput = IOUtils.toInputStream(xmlStr, "UTF-8");

        try {
            SchemaFactory factory = SchemaFactory
                    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(xsdInput));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xmlInput));
            return true;
        } catch (Exception ex) {
            return false;
        }

    }


    /**
     * Extracts an element from a xml content using a xpath specification
     *
     * @param xPath      path to the element to extract
     * @param xmlContent content from which the element should be extracted
     * @return element from the xml content
     */
    @SneakyThrows
    public static String extractElem(String xPath, String xmlContent) {
        String output = null;
        try {
            InputStream in = IOUtils.toInputStream(xmlContent, "UTF-8");
            Document doc = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(in);
            //create XPathExpression object
            XPathExpression expr = XPathFactory
                    .newInstance()
                    .newXPath()
                    .compile(xPath);
            //evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            output = nodes.item(0).getFirstChild().getNodeValue();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return output;
    }

}
