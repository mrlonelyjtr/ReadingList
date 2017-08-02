package ReadingList.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class ApplicationContextMetrics implements PublicMetrics {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Collection<Metric<?>> metrics() {
        List<Metric<?>> metrics = new ArrayList<>();
        metrics.add(new Metric<>("spring.context.startup-date", applicationContext.getStartupDate()));
        metrics.add(new Metric<>("spring.beans.definition", applicationContext.getBeanDefinitionCount()));
        metrics.add(new Metric<>("spring.beans", applicationContext.getBeanNamesForType(Object.class).length));
        metrics.add(new Metric<>("spring.controllers", applicationContext.getBeanNamesForAnnotation(Controller.class).length));
        return metrics;
    }
}
