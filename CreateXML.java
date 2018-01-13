// file:       CreateXML.java
// author:     Michael Gonzalez
// class:      cs211e
// date:       May 22, 2017
// assignment: Quiz 2
// objective:  Programmatically create an XML file that contains
//             the properties from the System.
//
//             The output will verify if the XML is valid
//************************************************************************
package CreateXML;

import java.util.ArrayList;
import java.util.Arrays;
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
    private static Map<String, ArrayList<String>> propMap = new HashMap<>();
    //*****************( fillPropertyMap() )******************************
    private static void fillPropertyMap()
    {
        ArrayList<String> javaArray = new ArrayList<>();
        ArrayList<String> osArray = new ArrayList<>();
        ArrayList<String> userArray = new ArrayList<>();
        javaArray.add("version");
        javaArray.add("home");
        javaArray.add("vendor");
        osArray.add("name");
        osArray.add("version");
        osArray.add("arch");
        userArray.add("name");
        userArray.add("home");
        userArray.add("dir");
        propMap.put("java", javaArray);
        propMap.put("os", osArray);
        propMap.put("user", userArray);
    }//fillPropertyMap()
    //*****************( main() )*****************************************
    public static void main(String...arg)
    {
        fillPropertyMap();
        try
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("properties");
            doc.appendChild(rootElement);
            Set<String> keySet = propMap.keySet();
            System.out.println("Output:\n");
            for(String key: keySet)
            {
                Element child1 = doc.createElement(key);
                rootElement.appendChild(child1);
                if(propMap.containsKey(key))
                {
                    ArrayList<String> arrayList = propMap.get(key);
                    Object[] objectList = arrayList.toArray();
                    String[] stringArray = Arrays.copyOf(objectList, objectList.length, String[].class);
                    for(String innerElement: stringArray)
                    {
                        Element child2 = doc.createElement(innerElement);
                        child2.appendChild(doc.createTextNode(System.getProperty(key+"."+innerElement)));
                        child1.appendChild(child2);
                    }

                }

            }//for(set)

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
            DOMSource source = new DOMSource(doc);
//The following line creates an XML file
//StreamResult result = new StreamResult(new File("/Users/michaelgonzalez/Documents/javaEE_workspace/java_xml/src/java_xml/properties.xml"));
            StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);

        }
        catch (ParserConfigurationException|TransformerException e)
        {
            e.printStackTrace();
        }
    }
}

