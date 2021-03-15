package cloud_calculator.calculator.service;

import cloud_calculator.common.model.CalculationRequest;
import cloud_calculator.common.model.CalculationResponse;
import reactor.core.publisher.Mono;

public interface CalculationService {
    Mono<CalculationResponse> calculate(Mono<CalculationRequest> calculationRequestMono);
}
