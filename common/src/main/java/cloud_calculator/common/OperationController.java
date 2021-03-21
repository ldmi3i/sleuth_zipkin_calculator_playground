package cloud_calculator.common;

import cloud_calculator.common.model.ExpressionRequest;
import cloud_calculator.common.model.ExpressionResponse;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

public interface OperationController {
    Mono<ExpressionResponse> applyOperation(@RequestBody Mono<ExpressionRequest> operationMono);
}
