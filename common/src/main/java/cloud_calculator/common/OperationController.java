package cloud_calculator.common;

import cloud_calculator.common.model.Operation;
import reactor.core.publisher.Mono;

public interface OperationController {
    Mono<Double> applyOperation(Mono<Operation> operationMono);
}
