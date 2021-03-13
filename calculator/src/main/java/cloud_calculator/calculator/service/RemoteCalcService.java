package cloud_calculator.calculator.service;

import cloud_calculator.calculator.model.TypedExpressionRequest;
import cloud_calculator.common.model.ExpressionResponse;
import reactor.core.publisher.Flux;

public interface RemoteCalcService {
    Flux<ExpressionResponse> requestResponse(Flux<TypedExpressionRequest> expressionRequestFlux);
}
