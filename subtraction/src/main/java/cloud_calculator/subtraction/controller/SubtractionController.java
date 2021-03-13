package cloud_calculator.subtraction.controller;

import cloud_calculator.common.OperationController;
import cloud_calculator.common.model.Expression;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/subtraction")
public class SubtractionController implements OperationController {
    @PostMapping
    @Override
    public Mono<Double> applyOperation(Mono<@Valid Expression> operationMono) {
        return operationMono.map(expression -> expression.getLeft() - expression.getRight());
    }
}
