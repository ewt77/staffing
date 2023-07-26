package test.ru.bank.integration.staffing

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import ru.bank.integration.staffing.IntegrationStaffingApplication

/**
 *
 * @author Author
 */
@SpringBootTest(classes = IntegrationStaffingApplication.class)
@ActiveProfiles(profiles = "dev")
class BaseStaffingTest {}
