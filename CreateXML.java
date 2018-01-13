package CreateXML;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CreateXML
{

    //*****************( fillPropertyMap() )******************************
    private static void fillPropertyMap(Map map)
    {
        String[] javaArray = {"version", "home", "vendor"};
        String[] osArray = {"name", "version", "arch"};
        String[] userArray = {"name", "home", "dir"};
        map.put("java", javaArray);
        map.put("os", osArray);
        map.put("user", userArray);
    }//fillPropertyMap()

    //*****************( getDocument() )******************************
    private static Document getDocument() throws ParserConfigurationException
    {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        return docBuilder.newDocument();
    }

    //*****************( buildXMLDoc() )******************************
    private static void buildXMLDoc(Document doc, Map propMap)
    {
        Element rootElement = doc.createElement("properties");
        doc.appendChild(rootElement);
        Set<String> keySet = propMap.keySet();
        System.out.println("\nOutput: \n");
        for(String key: keySet)
        {
            Element child1 = doc.createElement(key);
            rootElement.appendChild(child1);
            if(propMap.containsKey(key))
            {
                String[] stringArray = (String[]) propMap.get(key); //Not sure why I have to cast when I wrapped the code in a method.  It worked when it was in main()
                for(String innerElement: stringArray)
                {
                    Element child2 = doc.createElement(innerElement);
                    child2.appendChild(doc.createTextNode(System.getProperty(key+"."+innerElement)));
                    child1.appendChild(child2);
                }
            }
        }
    }

    //*****************( displayXMLDoc() )******************************
    private static void displayXMLDoc(Document doc) throws TransformerException
    {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
        DOMSource source = new DOMSource(doc);
        //The following line creates and stores an XML file instead:
        //   StreamResult result = new StreamResult(new File(
        //   "/Users/me/Documents/javaEE_workspace/java_xml/src/java_xml/properties.xml"));
        StreamResult result = new StreamResult(System.out);
        transformer.transform(source, result);
    }

    //*****************( main() )*****************************************
    public static void main(String...arg)
    {
        Map<String, String[]> propMap = new HashMap<>();
        fillPropertyMap(propMap);
        try
        {
            Document doc = getDocument();
            buildXMLDoc(doc, propMap);
            displayXMLDoc(doc);
        }
        catch (ParserConfigurationException|TransformerException e)
        {
            e.printStackTrace();
        }
    }
}

