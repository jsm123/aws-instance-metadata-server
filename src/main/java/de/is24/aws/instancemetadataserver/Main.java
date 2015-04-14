package de.is24.aws.instancemetadataserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(AwsCredentialsConfigurationProperties.class)
public class Main {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  @Bean
  public EmbeddedServletContainerCustomizer servletContainerCustomizer() {
    return servletContainer -> ((TomcatEmbeddedServletContainerFactory) servletContainer).
      addConnectorCustomizers(
        connector -> connector.setEnableLookups(true)
      );
  }
}
