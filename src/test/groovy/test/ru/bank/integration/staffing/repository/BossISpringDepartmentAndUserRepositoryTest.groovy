package test.ru.bank.integration.staffing.repository

import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.bank.integration.staffing.model.boss.BossISpringDepartment
import ru.bank.integration.staffing.model.boss.BossISpringUser
import ru.bank.integration.staffing.repository.BossISpringDepartmentRepository
import ru.bank.integration.staffing.repository.BossISpringUserRepository
import test.ru.bank.integration.staffing.BaseStaffingTest

import java.time.LocalDateTime
import java.time.ZoneId

/**
 *
 * @author Author
 */
@Component
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BossISpringDepartmentAndUserRepositoryTest extends BaseStaffingTest {
    private static BossISpringDepartment bossISpringDepartment
    private static BossISpringUser bossISpringUser

    @Autowired
    private BossISpringDepartmentRepository bossISpringDepartmentRepository
    @Autowired
    private BossISpringUserRepository bossISpringUserRepository

//    @Test
    @Order(value = 1)
    void createBossDepartmentTest() {
        Assertions.assertNotNull(bossISpringDepartmentRepository)

        BossISpringDepartment bossISpringDepartmentNew = new BossISpringDepartment()
        bossISpringDepartmentNew.setName("Test department")
        bossISpringDepartmentNew.setiSpringId("X")
        bossISpringDepartmentNew.setiSpringParentDepartmentId("60246916-96f8-11ed-8bb3-2ecf86751afb")
        bossISpringDepartmentNew.setCode("10000")
        bossISpringDepartmentNew.setSubordinationType("X")
        bossISpringDepartmentNew.setAction("X")
        bossISpringDepartmentNew.setUpdated(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
        bossISpringDepartmentNew.setUnloaded(null)

        BossISpringDepartment bossISpringDepartmentResult = bossISpringDepartmentRepository.save(bossISpringDepartmentNew)

        Assertions.assertNotNull(bossISpringDepartmentResult)
        Assertions.assertNotNull(bossISpringDepartmentResult.id)

        if (bossISpringDepartmentResult != null) {
            bossISpringDepartment = bossISpringDepartmentResult
        }

        Optional<BossISpringDepartment> result = bossISpringDepartmentRepository.findById(bossISpringDepartmentResult.id)
        Assertions.assertNotNull(result)
        Assertions.assertEquals(true, result.isPresent())
        Assertions.assertEquals(bossISpringDepartment.id, result.get().id)
    }

//    @Test
    @Order(value = 2)
    void findUnloadedBossDepartmentTest() {
        Assertions.assertNotNull(bossISpringDepartmentRepository)

        Iterable<BossISpringDepartment> result = bossISpringDepartmentRepository.findByUnloadedIsNull()
        Assertions.assertNotNull(result)
        Assertions.assertEquals(false, result.isEmpty())
    }

//    @Test
    @Order(value = 3)
    void createBossUserTest() {
        Assertions.assertNotNull(bossISpringUserRepository)
        Assertions.assertNotNull(bossISpringDepartment)
        Assertions.assertNotNull(bossISpringDepartment.id)

        BossISpringUser bossISpringUserNew = new BossISpringUser()
        bossISpringUserNew.setDepartmentId(bossISpringDepartment.id)
        bossISpringUserNew.setEmail("email_test@no.ru")
        bossISpringUserNew.setLogin("login_test")
        bossISpringUserNew.setiSpringId("X")
        bossISpringUserNew.setiSpringDepartmentId("X")
        bossISpringUserNew.setFirstName("FirstName")
        bossISpringUserNew.setLastName("LastName")
        bossISpringUserNew.setIfSendLoginEmail("false")
        bossISpringUserNew.setAction("X")
        bossISpringUserNew.setUpdated(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
        bossISpringUserNew.setUnloaded(null)

        BossISpringUser bossISpringUserResult = bossISpringUserRepository.save(bossISpringUserNew)

        Assertions.assertNotNull(bossISpringUserResult)
        Assertions.assertNotNull(bossISpringUserResult.id)

        if (bossISpringUserResult != null) {
            bossISpringUser = bossISpringUserResult
        }

        Optional<BossISpringUser> result = bossISpringUserRepository.findById(bossISpringUser.id)
        Assertions.assertNotNull(result)
        Assertions.assertEquals(true, result.isPresent())
        Assertions.assertEquals(bossISpringUser.id, result.get().id)
    }

//    @Test
    @Order(value = 4)
    void findUnloadedBossUserTest() {
        Assertions.assertNotNull(bossISpringUserRepository)

        Iterable<BossISpringUser> result = bossISpringUserRepository.findByUnloadedIsNull()
        Assertions.assertNotNull(result)
        Assertions.assertEquals(false, result.isEmpty())
    }

//    @Test
    @Order(value = 11)
    void updateBossDepartmentTest() {
        Assertions.assertNotNull(bossISpringDepartmentRepository)
        Assertions.assertNotNull(bossISpringDepartment)
        Assertions.assertNotNull(bossISpringDepartment.id)

        final String SUBORDINATION_TYPE = "inherit"
        bossISpringDepartment.setSubordinationType(SUBORDINATION_TYPE)
        final String ACTION = "I"
        bossISpringDepartment.setAction(ACTION)
        bossISpringDepartment.setUpdated(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
        bossISpringDepartment.setUnloaded(null)

        BossISpringDepartment bossISpringDepartmentResult = bossISpringDepartmentRepository.save(bossISpringDepartment)

        Assertions.assertEquals(SUBORDINATION_TYPE, bossISpringDepartmentResult.subordinationType)
        Assertions.assertEquals(ACTION, bossISpringDepartmentResult.action)
    }

//    @Test
    @Order(value = 12)
    void updateBossUserTest() {
        Assertions.assertNotNull(bossISpringUserRepository)
        Assertions.assertNotNull(bossISpringUser)
        Assertions.assertNotNull(bossISpringUser.id)

        final String PHONE_TYPE = "4565"
        bossISpringUser.setPhone(PHONE_TYPE)
        final String ACTION = "I"
        bossISpringUser.setAction(ACTION)
        bossISpringUser.setUpdated(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
        bossISpringUser.setUnloaded(null)

        BossISpringUser bossISpringUserResult = bossISpringUserRepository.save(bossISpringUser)

        Assertions.assertEquals(PHONE_TYPE, bossISpringUserResult.phone)
        Assertions.assertEquals(ACTION, bossISpringUserResult.action)
    }

//    @Test
    @Order(value = 21)
    void deleteBossUserTest() {
        Assertions.assertNotNull(bossISpringUserRepository)
        Assertions.assertNotNull(bossISpringUser)
        Assertions.assertNotNull(bossISpringUser.id)

        bossISpringUserRepository.delete(bossISpringUser)

        Optional<BossISpringUser> result = bossISpringUserRepository.findById(bossISpringUser.id)
        Assertions.assertNotNull(result)
        Assertions.assertEquals(false, result.isPresent())
    }

//    @Test
    @Order(value = 22)
    void deleteBossDepartmentTest() {
        Assertions.assertNotNull(bossISpringDepartmentRepository)
        Assertions.assertNotNull(bossISpringDepartment)
        Assertions.assertNotNull(bossISpringDepartment.id)

        bossISpringDepartmentRepository.delete(bossISpringDepartment)

        Optional<BossISpringDepartment> result = bossISpringDepartmentRepository.findById(bossISpringDepartment.id)
        Assertions.assertNotNull(result)
        Assertions.assertEquals(false, result.isPresent())
    }
}
