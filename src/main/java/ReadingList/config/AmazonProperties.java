package ReadingList.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "amazon")
public class AmazonProperties {

    private String associateId;

    public AmazonProperties(){
    }

    public String getAssociateId() {
        return associateId;
    }

    public void setAssociateId(String associateId) {
        this.associateId = associateId;
    }

}
