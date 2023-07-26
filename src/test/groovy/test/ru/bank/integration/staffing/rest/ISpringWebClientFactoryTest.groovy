package test.ru.bank.integration.staffing.rest

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.bank.integration.staffing.rest.ispring.ISpringWebClientBuilderFactory
import test.ru.bank.integration.staffing.BaseStaffingTest

/**
 *
 * @author Author
 */
@Component
class ISpringWebClientFactoryTest extends BaseStaffingTest {

    @Value('${staffing.rest.client.iSpring.protocol}')
    private String protocol
    @Value('${staffing.rest.client.iSpring.host}')
    private String host
    @Value('${staffing.rest.client.iSpring.port}')
    private String port

    @Autowired
    private ISpringWebClientBuilderFactory iSpringWebClientFactory

    @Test
    void getISpringWebClientFactoryTest() {
        Assertions.assertNotNull(iSpringWebClientFactory)
        Assertions.assertEquals("${protocol}://${host}:${port}".toString(),
                iSpringWebClientFactory.getBaseUrl())
        Assertions.assertNotNull(iSpringWebClientFactory.getObject())
    }
}
