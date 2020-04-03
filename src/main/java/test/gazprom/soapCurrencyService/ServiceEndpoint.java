package test.gazprom.soapCurrencyService;

import io.spring.guides.gs_producing_web_service.CurrResponse;
import io.spring.guides.gs_producing_web_service.CurrencyConvRequest;
import io.spring.guides.gs_producing_web_service.CurrencyConvResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class ServiceEndpoint {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    @Autowired
    public ServiceEndpoint() {

    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "currencyConvRequest")
    @ResponsePayload
    public CurrencyConvResponse getCurrencyConv(@RequestPayload CurrencyConvRequest request) {
        CurrencyConvResponse response = CurrConverter.convertCurrency(request);
        System.out.println("Итоговая сумма = " + response.getCurrResponse().getConvAmount()); //duplicate to console

        return response;
    }
}