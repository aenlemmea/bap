package ana.lemma.bap.config;

import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Value("${application.storage.location}")
  private String storageLocation;

 @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String absPath = Paths.get(storageLocation).toAbsolutePath().toUri().toString();
    registry.addResourceHandler("/api/v1/images/**").addResourceLocations(absPath);
  }
}
