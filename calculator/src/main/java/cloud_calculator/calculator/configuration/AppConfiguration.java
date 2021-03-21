package cloud_calculator.calculator.configuration;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan("cloud_calculator.calculator.configuration.properties")
public class AppConfiguration {
}
