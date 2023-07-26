package ru.bank.integration.staffing.rest.ispring

import groovyjarjarantlr4.v4.runtime.misc.NotNull
import org.springframework.beans.factory.FactoryBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import ru.bank.integration.staffing.IntegrationStaffingApplication

import java.nio.charset.StandardCharsets
import java.util.logging.Logger

/**
 *
 * @author Author
 */
@Component
@EnableConfigurationProperties
class ISpringWebClientBuilderFactory implements FactoryBean<WebClient>, InitializingBean {

    /**
     *
     * @author Author
     */
    enum RESOURCE_KEY {
        ErrorConfigurationExceptionIncorrectISpringPortFormat
    }

    private static final Logger LOGGER = Logger.getLogger(ISpringWebClientBuilderFactory.class.getName(), IntegrationStaffingApplication.RESOURCE_BUNDLE_NAME)

    private String baseUrl = ""

    @Value('${staffing.rest.client.iSpring.protocol}')
    private String protocol
    @Value('${staffing.rest.client.iSpring.host}')
    private String host
    @Value('${staffing.rest.client.iSpring.port}')
    private String port

    @Value('${staffing.rest.client.iSpring.x-auth.account-url}')
    private String accountUrl
    @Value('${staffing.rest.client.iSpring.x-auth.email}')
    private String email
    @Value('${staffing.rest.client.iSpring.x-auth.password}')
    private String password

    private WebClient webClient

    WebClient.RequestHeadersSpec setHeaders(@NotNull WebClient.RequestHeadersSpec requestHeadersSpec) {
        return requestHeadersSpec
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("X-Auth-Account-Url", accountUrl) // #/components/parameters/authAccountUrl:5098
                .header("X-Auth-Email", email) // #/components/parameters/authEmail:5104
                .header("X-Auth-Password", password) // #/components/parameters/authPassword:5110
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .ifNoneMatch("*")
    }

    @Override
    WebClient getObject() { this.webClient }

    @Override
    Class<WebClient> getObjectType() { return WebClient.class }

    @Override
    boolean isSingleton() { return true }

    String getBaseUrl() {
        if (this.baseUrl?.isEmpty()) {
            StringBuilder baseUrlSB = new StringBuilder()
            protocol == null || protocol.isEmpty() ? baseUrlSB.append("https") : baseUrlSB.append(protocol)
            baseUrlSB.append("://")
            host == null || host.isEmpty() ? baseUrlSB.append("localhost") : baseUrlSB.append(host)
            try {
                if (port != null && !port.isEmpty() && Integer.valueOf(port) != null) {
                    baseUrlSB.append(":").append(Integer.valueOf(port))
                }
            } catch (NumberFormatException ignored) {
                LOGGER.severe("${RESOURCE_KEY.ErrorConfigurationExceptionIncorrectISpringPortFormat}")
                System.exit(1)
            }
            this.baseUrl = baseUrlSB.toString()
        }
        this.baseUrl
    }

    @Override
    void afterPropertiesSet() {
        this.webClient = WebClient.builder()
                ?.baseUrl(getBaseUrl())
                ?.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                ?.defaultUriVariables(Collections.singletonMap("url", getBaseUrl()))
                ?.build()
    }
}
