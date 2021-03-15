package cloud_calculator.calculator.model;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ExpressionType {
    ADDITION("+"),
    EXPONENTIATION("^"),
    MULTIPLICATION("*"),
    DIVISION("/");

    ExpressionType(String operator) {
        this.operator = operator;
    }

    private final String operator;

    private static final Map<String, ExpressionType> operatorMap;

    static {
        operatorMap = Arrays.stream(values())
                .collect(Collectors.toUnmodifiableMap(ExpressionType::getOperator, Function.identity()));
    }

    public String getOperator() {
        return operator;
    }

    public static ExpressionType getByOperator(String operator) {
        return operatorMap.get(operator);
    }
}
