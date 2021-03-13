package cloud_calculator.calculator.service;

import cloud_calculator.calculator.model.CalculationRequest;
import cloud_calculator.calculator.model.CalculationResponse;
import reactor.core.publisher.Mono;

public interface CalculationService {
    Mono<CalculationResponse> calculate(Mono<CalculationRequest> calculationRequestMono);
}
