package si.uni_lj.fri.rso.ir_image_retrieval.cdi;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Health
@ApplicationScoped
public class ImageRetrievalConfigModifiableHealthCheck implements HealthCheck {
    @Inject
    private Config config;

    @Override
    public HealthCheckResponse call() {
        if (config.getHealthy()) {
            return HealthCheckResponse.named(ImageRetrievalConfigModifiableHealthCheck.class.getSimpleName()).up().build();
        } else {
            return HealthCheckResponse.named(ImageRetrievalConfigModifiableHealthCheck.class.getSimpleName()).down().build();
        }
    }
}
