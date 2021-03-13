package cloud_calculator.calculator.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("calculator")
public class CommonProperties {
    private Boolean debug = true;
}
