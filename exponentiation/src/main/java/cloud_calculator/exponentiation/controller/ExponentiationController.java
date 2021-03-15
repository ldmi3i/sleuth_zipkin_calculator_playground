package cloud_calculator.exponentiation.controller;

import brave.Span;
import brave.Tracer;
import cloud_calculator.common.OperationController;
import cloud_calculator.common.model.ExpressionRequest;
import cloud_calculator.common.model.ExpressionResponse;
import cloud_calculator.common.model.TagTypes;
import cloud_calculator.exponentiation.service.ExponentiationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/exponentiation")
public class ExponentiationController implements OperationController {
    private final Tracer tracer;
    private final ExponentiationService exponentiationService;

    public ExponentiationController(Tracer tracer, ExponentiationService exponentiationService) {
        this.tracer = tracer;
        this.exponentiationService = exponentiationService;
    }

    @PostMapping
    @Override
    public Mono<ExpressionResponse> applyOperation(@RequestBody Mono<ExpressionRequest> operationMono) {
        return operationMono
                .doOnNext(expressionRequest -> {
                    Span span = tracer.currentSpan();
                    span.tag(TagTypes.EXPRESSION.getTagName(), expressionRequest.toString());
                    span.annotate("Retrieve expression");
                    log.info("Retrieve expression {}", expressionRequest);
                })
                .transform(exponentiationService::processExpression)
//                .map(expression -> ExpressionResponse.builder()
//                        .result(expression.getLeft() - expression.getRight())
//                        .id(expression.getId())
//                        .build())
                .doOnNext(expressionResponse -> log.info("Produce response {}", expressionResponse));
    }
}
