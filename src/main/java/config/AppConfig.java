package config;

import model.Model;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({"controller", "dao", "view"})
@Import({RepositoryConfig.class, ViewConfig.class})
public class AppConfig {

    @Bean
    public Model model(){
        return new Model();
    }

}
