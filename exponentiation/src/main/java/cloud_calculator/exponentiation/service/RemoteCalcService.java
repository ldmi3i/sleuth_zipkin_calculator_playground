package cloud_calculator.exponentiation.service;

import cloud_calculator.common.model.CalculationRequest;
import cloud_calculator.common.model.CalculationResponse;
import reactor.core.publisher.Mono;

public interface RemoteCalcService {
    Mono<CalculationResponse> requestResponse(Mono<CalculationRequest> calculationRequestMono);
}
