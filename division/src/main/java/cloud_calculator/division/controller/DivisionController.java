package cloud_calculator.division.controller;

import cloud_calculator.common.OperationController;
import cloud_calculator.common.model.ExpressionRequest;
import cloud_calculator.common.model.ExpressionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/division")
public class DivisionController implements OperationController {
    @Override
    public Mono<ExpressionResponse> applyOperation(@RequestBody Mono<ExpressionRequest> operationMono) {
        return operationMono
                .doOnNext(expressionRequest -> log.info("Retrieve expression {}", expressionRequest))
                .map(expression -> ExpressionResponse.builder()
                        .result(expression.getLeft() / expression.getRight())
                        .id(expression.getId())
                        .build())
                .doOnNext(expressionResponse -> log.info("Produce response {}", expressionResponse));
    }
}
