package cloud_calculator.calculator.model;

import cloud_calculator.common.model.ExpressionRequest;
import lombok.Data;

@Data
public class TypedExpressionRequest {
    private ExpressionType expressionType;

    private ExpressionRequest expressionRequest;
}
