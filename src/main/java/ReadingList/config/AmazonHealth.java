package ReadingList.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AmazonHealth implements HealthIndicator{

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Health health() {
        try{
            restTemplate.getForEntity("http://www.amazon.com", String.class);
            return Health.up().build();
        }catch (Exception e) {
            return Health.down().withDetail("reason", e.getMessage()).build();
        }
    }
}
