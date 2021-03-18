package cloud_calculator.exponentiation.service;

import cloud_calculator.common.model.CalculationRequest;
import cloud_calculator.common.model.CalculationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Slf4j
@Service
public class DefaultRemoteCalcService implements RemoteCalcService {
    private final WebClient webClient;

    public DefaultRemoteCalcService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<CalculationResponse> requestResponse(Mono<CalculationRequest> calculationRequestMono) {
        return calculationRequestMono
                .flatMap(calculationRequest -> webClient.post()
                .uri("http://calculator:8019/calculate")
                .body(BodyInserters.fromValue(calculationRequest))
                .retrieve()
                .bodyToMono(CalculationResponse.class));
    }
}
