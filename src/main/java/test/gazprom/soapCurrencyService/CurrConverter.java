package test.gazprom.soapCurrencyService;

import io.spring.guides.gs_producing_web_service.CurrencyConvRequest;
import io.spring.guides.gs_producing_web_service.CurrencyConvResponse;

import java.util.HashMap;
import java.util.Map;

class CurrConverter {

    private static Map<String,String> currencyHandbook = new HashMap<>();

    static CurrencyConvResponse convertCurrency(CurrencyConvRequest request) {
        getDailyCurrencyHandbookFromCBServer();
        return makeConvertation(request);
    }

    private static CurrencyConvResponse makeConvertation(CurrencyConvRequest request) {
        //TODO next day (dont remember about convertationExceptions - need try/catch, point 5 from task)
        return new CurrencyConvResponse();
    }

    private static void getDailyCurrencyHandbookFromCBServer() {
        //TODO next day, parse dat ref - http://www.cbr.ru/scripts/XML_daily.asp
    }


}
