package test.gazprom.soapCurrencyService;

import io.spring.guides.gs_producing_web_service.CurrResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import io.spring.guides.gs_producing_web_service.GetCurrencyConvRequest;
import io.spring.guides.gs_producing_web_service.GetCurrencyConvResponse;

@Endpoint
public class ServiceEndpoint {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

//    private CountryRepository countryRepository;

    @Autowired
    public ServiceEndpoint() {

    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCurrencyConvRequest")
    @ResponsePayload
    public GetCurrencyConvResponse getCurrencyConv(@RequestPayload GetCurrencyConvRequest request) {
        GetCurrencyConvResponse response = new GetCurrencyConvResponse();
        CurrResponse tmpResponse = new CurrResponse();
        tmpResponse.setConvAmount(100);
        tmpResponse.setError("Thats good");
        tmpResponse.setStatus(200);
        response.setCurrResponse(tmpResponse);

        return response;
    }
}