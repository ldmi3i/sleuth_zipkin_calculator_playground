package cloud_calculator.common.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ExpressionRequest {
    @NotNull
    private Double left;

    @NotNull
    private Double right;

    private Long id;
}
