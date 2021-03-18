package cloud_calculator.exponentiation.service;

import cloud_calculator.common.model.ExpressionRequest;
import cloud_calculator.common.model.ExpressionResponse;
import reactor.core.publisher.Mono;

public interface ExponentiationService {
    Mono<ExpressionResponse> processExpression(Mono<ExpressionRequest> expressionRequestMono);
}
