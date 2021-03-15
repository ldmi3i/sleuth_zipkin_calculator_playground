package cloud_calculator.exponentiation.service;

import cloud_calculator.common.model.CalculationRequest;
import cloud_calculator.common.model.ExpressionRequest;
import cloud_calculator.common.model.ExpressionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.StringJoiner;

@Slf4j
@Service
public class DefaultExponentiationService implements ExponentiationService {
    private final RemoteCalcService remoteCalcService;

    public DefaultExponentiationService(RemoteCalcService remoteCalcService) {
        this.remoteCalcService = remoteCalcService;
    }

    @Override
    public Mono<ExpressionResponse> processExpression(Mono<ExpressionRequest> expressionRequestMono) {
        return expressionRequestMono
                .flatMap(expressionRequest -> Mono
                        .fromCallable(() -> createExpression(expressionRequest))
                        .map(CalculationRequest::new)
                .transform(remoteCalcService::requestResponse)
                .map(calculationResponse ->
                        new ExpressionResponse(expressionRequest.getId(), calculationResponse.getResult())));
    }

    private String createExpression(ExpressionRequest expressionRequest) {
        String base = Double.toString(expressionRequest.getLeft());
        StringJoiner stringJoiner = new StringJoiner("*");
        for (int i = 0; i < expressionRequest.getRight().intValue(); i++) {
            stringJoiner.add(base);
        }
        return stringJoiner.toString();
    }
}
