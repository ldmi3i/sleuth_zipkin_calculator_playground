package cloud_calculator.calculator.controller;

import cloud_calculator.calculator.model.CalculationRequest;
import cloud_calculator.calculator.model.CalculationResponse;
import cloud_calculator.calculator.service.CalculationService;
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

    public CalculationController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @PostMapping
    public Mono<ResponseEntity<CalculationResponse>> calculate(@RequestBody Mono<@Valid CalculationRequest> calculationRequestMono) {
        return calculationRequestMono
                .transform(calculationService::calculate)
                .map(ResponseEntity::ok);
    }
}
