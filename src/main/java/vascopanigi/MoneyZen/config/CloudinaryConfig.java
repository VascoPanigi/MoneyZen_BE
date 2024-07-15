package vascopanigi.MoneyZen.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

        @Bean
        public Cloudinary uploader(@Value("${CLOUD_NAME}") String name,
                                   @Value("${CLOUD_KEY}") String secret,
                                   @Value("${CLOUD_SECRET}") String key) {
            Map<String, String> configuration = new HashMap<>();
            configuration.put("cloud_name", name);
            configuration.put("api_key", key);
            configuration.put("api_secret", secret);
            return new Cloudinary(configuration);
        }

}
