package test.gazprom.soapCurrencyService;

import io.spring.guides.gs_producing_web_service.CurrResponse;
import io.spring.guides.gs_producing_web_service.CurrencyConvRequest;
import io.spring.guides.gs_producing_web_service.CurrencyConvResponse;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.net.*;
import java.util.Scanner;

class CurrConverter {

    static class LocalCurr {

        LocalCurr() {};

        String nominal;
        String value;
    }

    private static Map<String,LocalCurr> currencyHandbook = new HashMap<>();
    private static String content = null;

    static CurrencyConvResponse convertCurrency(CurrencyConvRequest request) {
        getDailyCurrencyHandbookFromCBServer();
        return makeConvertation(request);
    }

    private static CurrencyConvResponse makeConvertation(CurrencyConvRequest request) {
        CurrencyConvResponse response = new CurrencyConvResponse();
        CurrResponse currResponse = new CurrResponse();
        try
        {
            if (!currencyHandbook.containsKey(request.getCode().toString()))
                throw new Exception();

            LocalCurr currencyFromHandbook = currencyHandbook.get(request.getCode().toString());
            String currensyCourse = currencyFromHandbook.value.replace(',','.');

            BigDecimal requestAmount = new BigDecimal(request.getAmount());
            BigDecimal bdCurrensyCourse = new BigDecimal(currensyCourse);
            BigDecimal currensyNominal = new BigDecimal(currencyFromHandbook.nominal);

            BigDecimal firstDevide = requestAmount.divide(bdCurrensyCourse,4,RoundingMode.CEILING);
            BigDecimal secondMultiply = firstDevide.multiply(currensyNominal);

            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            df.setMinimumIntegerDigits(0);
            df.setGroupingUsed(false);

            currResponse.setStatus(0);
            currResponse.setConvAmount(df.format(secondMultiply));
            currResponse.setError("Converted successful");

            response.setCurrResponse(currResponse);
        }
        catch (Exception e)
        {
            currResponse.setStatus(1);
            currResponse.setConvAmount("0");
            currResponse.setError("Conversion failed");
            e.printStackTrace();
        }
        response.setCurrResponse(currResponse);
        return response;
    }

    private static void getDailyCurrencyHandbookFromCBServer() {

        getContentFromCBServer();
        parseContentToLocalHandbook();

//        currencyHandbook.forEach((currCharCode,currValue) -> System.out.println(currCharCode + " --- " + currValue));

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
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String currCharCode = eElement.getElementsByTagName("CharCode").item(0).getTextContent();
                    String currValue = eElement.getElementsByTagName("Value").item(0).getTextContent();
                    String currNominal = eElement.getElementsByTagName("Nominal").item(0).getTextContent();

                    LocalCurr localCurr = new LocalCurr();
                    localCurr.nominal = currNominal;
                    localCurr.value = currValue;

                    currencyHandbook.putIfAbsent(currCharCode,localCurr);
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
