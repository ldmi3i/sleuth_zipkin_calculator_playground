package cloud_calculator.calculator.service;

import cloud_calculator.common.model.CalculationRequest;
import cloud_calculator.common.model.CalculationResponse;
import cloud_calculator.calculator.model.ExpressionType;
import cloud_calculator.calculator.model.TypedExpressionRequest;
import cloud_calculator.calculator.utils.IdProvider;
import cloud_calculator.common.model.ExpressionRequest;
import cloud_calculator.common.model.ExpressionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DefaultCalculationService implements CalculationService {
    //TODO to list of priorities mb?
    private final String firstPriority = "*/";
    private final String secondPriority = "+-";
    private final String idPattern = "^\\{\\d+}$";
    private final List<String> operationPriorities = new ArrayList<>();
    private final Set<Character> allAvailableOperations = new HashSet<>();

    private final IdProvider idProvider;
    private final RemoteCalcService remoteCalcService;

    public DefaultCalculationService(IdProvider idProvider,
                                     RemoteCalcService remoteCalcService) {
        this.idProvider = idProvider;
        this.remoteCalcService = remoteCalcService;
    }

    @PostConstruct
    public void init() {
        operationPriorities.add("^");
        operationPriorities.add("*/");
        operationPriorities.add("+-");

        operationPriorities.forEach(operations -> {
            for (char c : operations.toCharArray()) {
                allAvailableOperations.add(c);
            }
        });
    }

    @Override
    public Mono<CalculationResponse> calculate(Mono<CalculationRequest> calculationRequestMono) {
        return calculationRequestMono
                .map(request -> {
                    String noSpacesExpr = request.getExpression().replaceAll("\\s+", "");
                    return splitExpression(noSpacesExpr);
                })
                .expand(splitted -> {
                    if (splitted.size() == 1) {
                        log.debug("expand finishing with splitted {}", splitted);
                        return Mono.empty();
                    }

                    return Mono.fromCallable(() -> {
                                int i = 0;
                                List<TypedExpressionRequest> resultList = new ArrayList<>();
                                while (i < operationPriorities.size() && resultList.isEmpty()) {
                                    resultList.addAll(getWithOperations(splitted, operationPriorities.get(i++)));
                                }
                                return resultList;
                            })
                            .flatMapMany(Flux::fromIterable)
                            .doOnNext(typedExpressionRequest -> log.trace("capture request {}", typedExpressionRequest))
                            .transform(remoteCalcService::requestResponse)
                            .collectList()
                            .map(responses -> updateExpressions(responses, splitted));
                })
                .last()
                .map(calcResult -> {
                    if (calcResult.size() != 1)
                        throw new InternalError("Final result has incorrect size");
                    CalculationResponse calculationResponse = new CalculationResponse();
                    calculationResponse.setResult(Double.parseDouble(calcResult.get(0)));
                    return calculationResponse;
                });
    }

    private List<String> updateExpressions(List<ExpressionResponse> responses, List<String> splittedExpressions) {
        Map<Long, ExpressionResponse> resultMap = responses.stream()
                .collect(Collectors.toMap(ExpressionResponse::getId, Function.identity()));

        for (int i = 0; i < splittedExpressions.size(); i++) {
            String element = splittedExpressions.get(i);
            if (element.matches(idPattern)) {
                Long id = Long.parseLong(element.substring(1, element.length() - 1));
                ExpressionResponse expressionResponse = resultMap.get(id);
                if (expressionResponse == null)
                    throw new InternalError("Operation with id has no result!");
                splittedExpressions.set(i, Double.toString(expressionResponse.getResult()));
            }
        }
        return splittedExpressions;
    }

    private List<String> splitExpression(String expression) {
        if (expression.isBlank())
            return new ArrayList<>();

        List<String> resultElements = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (char c : expression.toCharArray()) {
            if (allAvailableOperations.contains(c)) {
                resultElements.add(sb.toString());
                sb = new StringBuilder();
                if (c == '-') {
                    resultElements.add("+");
                    sb.append(c);
                } else {
                    resultElements.add(Character.toString(c));
                }
            } else {
                sb.append(c);
            }
        }
        resultElements.add(sb.toString());
        return resultElements;
    }

    private List<TypedExpressionRequest> getWithOperations(List<String> expressions, String operations) {
        String prev = null;
        List<TypedExpressionRequest> resultExpressions = new ArrayList<>();
        for (int i = 0; i < expressions.size(); i++) {
            String left;
            String right;
            String currentExpression = expressions.get(i);
            if (operations.contains(currentExpression) &&
                    i > 0 && i < expressions.size() - 1 &&
                    !(left = expressions.get(i - 1)).matches(idPattern)
                    && !(right = expressions.get(i + 1)).matches(idPattern)) {
                TypedExpressionRequest typedExpression = new TypedExpressionRequest();
                ExpressionRequest expressionRequest = new ExpressionRequest();
                typedExpression.setExpressionRequest(expressionRequest);
                typedExpression.setExpressionType(ExpressionType.getByOperator(currentExpression));
                expressionRequest.setLeft(Double.parseDouble(left));
                expressionRequest.setRight(Double.parseDouble(right));
                expressions.remove(i);
                expressions.remove(i);
                Long id = idProvider.getNext();
                expressions.set(i - 1, "{" + id + "}");
                expressionRequest.setId(id);
                resultExpressions.add(typedExpression);
                i--;
            }
        }
        return resultExpressions;
    }
}
