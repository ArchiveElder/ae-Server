package com.ae.gateway;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ae_SpringFilter extends AbstractGatewayFilterFactory<ae_SpringFilter.Config> {
    private static final Logger logger = LogManager.getLogger(ae_SpringFilter.class);
    public ae_SpringFilter() {
        super(ae_SpringFilter.Config.class);
    }

    @Override
    public GatewayFilter apply(ae_SpringFilter.Config config) {
        return ((exchange, chain) -> {
            logger.info("ae_SpringFilter baseMessage>>>>>>" + config.getBaseMessage());
            if (config.isPreLogger()) {
                logger.info("ae_SpringFilter Start>>>>>>" + exchange.getRequest());
            }
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()) {
                    logger.info("ae_SpringFilter End>>>>>>" + exchange.getResponse());
                }
            }));
        });
    }

    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
