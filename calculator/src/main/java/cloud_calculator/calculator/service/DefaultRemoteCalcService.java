package cloud_calculator.calculator.service;

import cloud_calculator.calculator.configuration.properties.CommonProperties;
import cloud_calculator.calculator.model.ExpressionType;
import cloud_calculator.calculator.model.TypedExpressionRequest;
import cloud_calculator.common.model.ExpressionRequest;
import cloud_calculator.common.model.ExpressionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class DefaultRemoteCalcService implements RemoteCalcService {
    private final WebClient webClient;
    private final Map<ExpressionType, String> serviceUrlMapping = new HashMap<>();
    private final CommonProperties commonProperties;

    public DefaultRemoteCalcService(WebClient.Builder webClientBuilder,
                                    CommonProperties commonProperties) {
        this.webClient = webClientBuilder.build();
        this.commonProperties = commonProperties;
    }

    @PostConstruct
    public void init() {
        String additionHost = commonProperties.getDebug() ? "localhost:8020" : "addition:8020";
        String subtractionHost = commonProperties.getDebug() ? "localhost:8021" : "subtraction:8021";
        String multiplicationHost = commonProperties.getDebug() ? "localhost:8022" : "multiplication:8022";
        String divisionHost = commonProperties.getDebug() ? "localhost:8023" : "division:8023";

        serviceUrlMapping.put(ExpressionType.ADDITION, "http://" + additionHost + "/addition");
        serviceUrlMapping.put(ExpressionType.SUBTRACTION, "http://" + subtractionHost + "/subtraction");
        serviceUrlMapping.put(ExpressionType.DIVISION, "http://" + divisionHost + "/division");
        serviceUrlMapping.put(ExpressionType.MULTIPLICATION, "http://" + multiplicationHost + "/multiplication");
    }

    @Override
    public Flux<ExpressionResponse> requestResponse(Flux<TypedExpressionRequest> expressionRequestFlux) {
        return expressionRequestFlux
                .flatMap(typedExpressionRequest -> webClient.post()
                        .uri(serviceUrlMapping.get(typedExpressionRequest.getExpressionType()))
                .body(BodyInserters.fromValue(typedExpressionRequest.getExpressionRequest()))
                .retrieve()
                .bodyToMono(ExpressionResponse.class), 10);
    }
}
