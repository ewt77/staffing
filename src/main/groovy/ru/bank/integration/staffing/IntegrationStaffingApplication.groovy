package ru.bank.integration.staffing

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.scheduling.annotation.EnableScheduling

import java.util.logging.Logger

/**
 *
 * @author Author
 */
@SpringBootApplication
@EnableScheduling
class IntegrationStaffingApplication {

    /**
     *
     * @author Author
     */
    enum RESOURCE_KEY {
        InfoApplicationStarting,
        InfoApplicationStarted
    }

    static final String RESOURCE_BUNDLE_NAME = "ru.bank.integration.staffing.IntegrationStaffingApplicationResource"
    static final ResourceBundleMessageSource RESOURCE_BUNDLE_NEW

    static {
        RESOURCE_BUNDLE_NEW = new ResourceBundleMessageSource()
        RESOURCE_BUNDLE_NEW.setBasenames("ru.bank.integration.staffing.IntegrationStaffingApplicationResource")
        RESOURCE_BUNDLE_NEW.setDefaultEncoding("UTF-8")
        RESOURCE_BUNDLE_NEW
    }

    private static final Logger LOGGER = Logger.getLogger(IntegrationStaffingApplication.class.getName(), RESOURCE_BUNDLE_NAME)

    static void main(String[] args) {
        LOGGER.info("${RESOURCE_KEY.InfoApplicationStarting}")
        SpringApplication.run(IntegrationStaffingApplication.class, args)
        LOGGER.info("${RESOURCE_KEY.InfoApplicationStarted}")
    }
}
