package com.ae.gateway;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ae_communityFilter extends AbstractGatewayFilterFactory<ae_communityFilter.Config> {
    private static final Logger logger = LogManager.getLogger(ae_communityFilter.class);
    public ae_communityFilter() {
        super(ae_communityFilter.Config.class);
    }

    @Override
    public GatewayFilter apply(ae_communityFilter.Config config) {
        return ((exchange, chain) -> {
            logger.info("ae_communityFilter baseMessage>>>>>>" + config.getBaseMessage());
            if (config.isPreLogger()) {
                logger.info("ae_communityFilter Start>>>>>>" + exchange.getRequest());
            }
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()) {
                    logger.info("ae_communityFilter End>>>>>>" + exchange.getResponse());
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
