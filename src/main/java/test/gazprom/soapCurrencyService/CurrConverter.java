package test.gazprom.soapCurrencyService;

import io.spring.guides.gs_producing_web_service.CurrencyConvRequest;
import io.spring.guides.gs_producing_web_service.CurrencyConvResponse;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.net.*;
import java.util.Scanner;

class CurrConverter {

    private static Map<String,String> currencyHandbook = new HashMap<>();
    private static String content = null;

    static CurrencyConvResponse convertCurrency(CurrencyConvRequest request) {
        getDailyCurrencyHandbookFromCBServer();
        return makeConvertation(request);
    }

    private static CurrencyConvResponse makeConvertation(CurrencyConvRequest request) {
        //TODO next day (dont remember about convertationExceptions - need try/catch, point 5 from task)
        return new CurrencyConvResponse();
    }

    private static void getDailyCurrencyHandbookFromCBServer() {

        getContentFromCBServer();
        parseContentToLocalHandbook();

        //currencyHandbook.forEach((currCharCode,currValue) -> System.out.println(currCharCode + " --- " + currValue));

    }

    private static void parseContentToLocalHandbook() {
        try
        {
            Document doc = convertStringToXMLDocument();
            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList valutesList = doc.getElementsByTagName("Valute");

            for (int temp = 0; temp < valutesList.getLength(); temp++) {
                Node nNode = valutesList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String currCharCode = eElement.getElementsByTagName("CharCode").item(0).getTextContent();
                    String currValue = eElement.getElementsByTagName("Value").item(0).getTextContent();
                    currencyHandbook.putIfAbsent(currCharCode,currValue);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private static Document convertStringToXMLDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(content)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static void getContentFromCBServer() {
        URLConnection connection = null;
        try {
            connection =  new URL("http://www.cbr.ru/scripts/XML_daily.asp").openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();
            scanner.close();
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }

}
