package cloud_calculator.calculator.controller;

import brave.Tracer;
import cloud_calculator.common.model.CalculationRequest;
import cloud_calculator.common.model.CalculationResponse;
import cloud_calculator.calculator.service.CalculationService;
import cloud_calculator.common.model.TagTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/calculate")
public class CalculationController {
    private final CalculationService calculationService;
    private final Tracer tracer;

    public CalculationController(CalculationService calculationService,
                                 Tracer tracer) {
        this.calculationService = calculationService;
        this.tracer = tracer;
    }

    @PostMapping
    public Mono<ResponseEntity<CalculationResponse>> calculate(@RequestBody Mono<@Valid CalculationRequest> calculationRequestMono) {
        return calculationRequestMono
                .doOnNext(calculationRequest -> tracer.currentSpan().tag(TagTypes.EXPRESSION.getTagName(), calculationRequest.getExpression()))
                .transform(calculationService::calculate)
                .map(ResponseEntity::ok);
    }
}
