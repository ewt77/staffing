package test.ru.bank.integration.staffing.integration

import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.bank.integration.staffing.model.boss.BossISpringDepartment
import ru.bank.integration.staffing.model.boss.BossISpringUser
import ru.bank.integration.staffing.model.ispring.ISpringDepartment
import ru.bank.integration.staffing.model.ispring.ISpringErrorResponse
import ru.bank.integration.staffing.model.ispring.ISpringSubordination
import ru.bank.integration.staffing.model.ispring.ISpringUser
import ru.bank.integration.staffing.model.mapper.BossISpringDepartmentMapper
import ru.bank.integration.staffing.model.mapper.BossISpringUserMapper
import ru.bank.integration.staffing.repository.BossISpringDepartmentRepository
import ru.bank.integration.staffing.repository.BossISpringUserRepository
import ru.bank.integration.staffing.service.ISpringDepartmentService
import ru.bank.integration.staffing.service.ISpringUserService
import test.ru.bank.integration.staffing.BaseStaffingTest

import java.time.LocalDateTime
import java.time.ZoneId

import static java.util.concurrent.TimeUnit.SECONDS
import static org.awaitility.Awaitility.await

/**
 *
 * @author Author
 */
@Component
@ComponentScan(basePackages = "ru.bank.integration.staffing.task")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BossISpringDepartmentAndUserIntegrationTest extends BaseStaffingTest {

    private static BossISpringDepartment bossISpringDepartment
    private static BossISpringUser bossISpringUser

    private static boolean isCreateDepartmentSynchronized = false
    private static boolean isUpdateDepartmentSynchronized = false
    private static boolean isSubordinationUpdateDepartmentSynchronized = false
    private static boolean isRemoveDepartmentSynchronized = false

    private static boolean isCreateUserSynchronized = false
    private static boolean isUpdateUserSynchronized = false
    private static boolean isStatusUpdateUserSynchronized = false
    private static boolean isRemoveFromGroupUserSynchronized = false
    private static boolean isRemoveUserSynchronized = false

    @Autowired
    private BossISpringDepartmentRepository bossISpringDepartmentRepository
    @Autowired
    private BossISpringUserRepository bossISpringUserRepository

    @Autowired
    private ISpringDepartmentService iSpringDepartmentService
    @Autowired
    private ISpringUserService iSpringUserService

    @Scheduled(fixedDelay = 1000l)
    void checkIfBossDepartmentSynchronized() {
        if (bossISpringDepartment != null) {
            Optional<BossISpringDepartment> result = bossISpringDepartmentRepository.findById(bossISpringDepartment.id)
            if (result != null
                    && result?.isPresent()
                    && bossISpringDepartment.id == result?.get()?.id
                    && result?.get()?.iSpringId != null
                    && !result?.get()?.iSpringId?.isEmpty()
                    && result?.get()?.iSpringId != 'X'
                    && result?.get()?.unloaded != null) {
                switch (result?.get()?.action) {
                    case 'I':
                        bossISpringDepartment.setiSpringId(result?.get()?.iSpringId)
                        isCreateDepartmentSynchronized = true
                        break
                    case 'U':
                        isUpdateDepartmentSynchronized = true
                        break
                    case 'S':
                        isSubordinationUpdateDepartmentSynchronized = true
                        break
                    case 'D':
                        isRemoveDepartmentSynchronized = true
                        break
                    default:
                        break
                }
            }
        }
    }

    @Scheduled(fixedDelay = 1000l)
    void checkIfBossUserSynchronized() {
        if (bossISpringUser != null) {
            Optional<BossISpringUser> result = bossISpringUserRepository.findById(bossISpringUser.id)
            if (result != null
                    && result?.isPresent()
                    && bossISpringUser.id == result?.get()?.id
                    && result?.get()?.iSpringId != null
                    && !result?.get()?.iSpringId?.isEmpty()
                    && result?.get()?.iSpringId != 'X'
                    && result?.get()?.unloaded != null) {
                switch (result?.get()?.action) {
                    case 'I':
                        bossISpringUser.setiSpringId(result?.get()?.iSpringId)
                        isCreateUserSynchronized = true
                        break
                    case 'U':
                        isUpdateUserSynchronized = true
                        break
                    case 'S':
                        isStatusUpdateUserSynchronized = true
                        break
                    case 'R':
                        isRemoveFromGroupUserSynchronized = true
                        break
                    case 'D':
                        isRemoveUserSynchronized = true
                        break
                    default:
                        break
                }
            }
        }
    }

    @Test
    @Order(1)
    void createDepartmentTest() {
        Assertions.assertNotNull(bossISpringDepartmentRepository)

        BossISpringDepartment bossISpringDepartmentNew = new BossISpringDepartment()
        bossISpringDepartmentNew.setName("Test department")
        bossISpringDepartmentNew.setiSpringId("X")
        bossISpringDepartmentNew.setiSpringParentDepartmentId("60246916-96f8-11ed-8bb3-2ecf86751afb")
        bossISpringDepartmentNew.setCode("11000")
        bossISpringDepartmentNew.setSubordinationType("X")
        bossISpringDepartmentNew.setAction("I")
        bossISpringDepartmentNew.setUpdated(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
        bossISpringDepartmentNew.setUnloaded(null)

        BossISpringDepartment bossISpringDepartmentResult = bossISpringDepartmentRepository.save(bossISpringDepartmentNew)

        Assertions.assertNotNull(bossISpringDepartmentResult)
        Assertions.assertNotNull(bossISpringDepartmentResult?.id)

        if (bossISpringDepartmentResult != null) {
            bossISpringDepartment = bossISpringDepartmentResult
        }

        Optional<BossISpringDepartment> result = bossISpringDepartmentRepository.findById(bossISpringDepartment.id)
        Assertions.assertNotNull(result)
        Assertions.assertEquals(true, result?.isPresent())
        Assertions.assertEquals(bossISpringDepartment.id, result?.get()?.id)
    }

    @Test
    @Order(2)
    void checkSyncToISpringForCreateDepartmentTest() {
        await().atMost(30, SECONDS).until(() -> isCreateDepartmentSynchronized)

        Assertions.assertNotNull(bossISpringDepartment)
        Assertions.assertNotNull(bossISpringDepartment.iSpringId)
        Assertions.assertEquals(false, bossISpringDepartment.iSpringId.isEmpty())
        Assertions.assertNotNull(iSpringDepartmentService)

        def response = iSpringDepartmentService.getISpringDepartment(BossISpringDepartmentMapper.toISpringDepartmentId(bossISpringDepartment))
        Assertions.assertNotNull(response)
        boolean taskDone = false
        def result = null
        Throwable throwable = null
        response
                .doFinally(r -> { taskDone = true })
                .subscribe(value -> result = value,
                        error -> throwable = error)
        await().atMost(60, SECONDS).until(() -> taskDone)
        if (throwable != null) {
            Assertions.fail(throwable)
        }
        if (result != null) {
            Assertions.assertNotNull(result)
            switch (result?.getClass()?.name) {
                case ISpringDepartment.class.name:
                    Assertions.assertEquals(bossISpringDepartment.iSpringId, ((ISpringDepartment) result)?.departmentId?.toString())
                    break
                case ISpringErrorResponse.class.name:
                    Assertions.fail(((ISpringErrorResponse) result)?.message)
                    break
                default:
                    Assertions.fail(result?.toString())
                    break
            }
        } else {
            Assertions.fail("result is null")
        }
    }

    @Test
    @Order(11)
    void createUserTest() {
        Assertions.assertNotNull(bossISpringUserRepository)
        Assertions.assertNotNull(bossISpringDepartment)
        Assertions.assertNotNull(bossISpringDepartment.id)
        Assertions.assertNotNull(bossISpringDepartment.iSpringId)

        BossISpringUser bossISpringUserNew = new BossISpringUser()
        bossISpringUserNew.setDepartmentId(bossISpringDepartment.id)
        bossISpringUserNew.setEmail("email_test@no.ru")
        bossISpringUserNew.setLogin("login_test_2")
        bossISpringUserNew.setiSpringId("X")
        bossISpringUserNew.setiSpringDepartmentId(bossISpringDepartment.iSpringId)
        bossISpringUserNew.setFirstName("FirstName")
        bossISpringUserNew.setLastName("LastName")
        bossISpringUserNew.setGroupIds("2428b8e8-05d1-11ee-9897-6a8398f95423,bab52566-15b3-11ee-b539-7af98ac59c08")
        bossISpringUserNew.setIfSendLoginEmail("false")
        bossISpringUserNew.setAction("I")
        bossISpringUserNew.setUpdated(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
        bossISpringUserNew.setUnloaded(null)

        BossISpringUser bossISpringUserResult = bossISpringUserRepository.save(bossISpringUserNew)

        Assertions.assertNotNull(bossISpringUserResult)
        Assertions.assertNotNull(bossISpringUserResult?.id)

        if (bossISpringUserResult != null) {
            bossISpringUser = bossISpringUserResult
        }

        Optional<BossISpringUser> result = bossISpringUserRepository.findById(bossISpringUser.id)
        Assertions.assertNotNull(result)
        Assertions.assertEquals(true, result?.isPresent())
        Assertions.assertEquals(bossISpringUser.id, result?.get()?.id)
    }

    @Test
    @Order(12)
    void checkSyncToISpringForCreateUserTest() {
        await().atMost(30, SECONDS).until(() -> isCreateUserSynchronized)

        Assertions.assertNotNull(bossISpringUser)
        Assertions.assertNotNull(bossISpringUser.iSpringId)
        Assertions.assertEquals(false, bossISpringUser.iSpringId.isEmpty())
        Assertions.assertNotNull(iSpringUserService)

        def response = iSpringUserService.getISpringUser(BossISpringUserMapper.toISpringUserId(bossISpringUser))
        Assertions.assertNotNull(response)
        boolean taskDone = false
        def result = null
        Throwable throwable = null
        response
                .doFinally(r -> { taskDone = true })
                .subscribe(value -> result = value,
                        error -> throwable = error)
        await().atMost(60, SECONDS).until(() -> taskDone)
        if (throwable != null) {
            Assertions.fail(throwable)
        }
        if (result != null) {
            Assertions.assertNotNull(result)
            switch (result?.getClass()?.name) {
                case ISpringUser.class.name:
                    Assertions.assertEquals(bossISpringUser.iSpringId, ((ISpringUser) result)?.userId?.toString())
                    Assertions.assertEquals(true, ((ISpringUser) result)?.groups?.contains(UUID.fromString("2428b8e8-05d1-11ee-9897-6a8398f95423")))
                    Assertions.assertEquals(true, ((ISpringUser) result)?.groups?.contains(UUID.fromString("bab52566-15b3-11ee-b539-7af98ac59c08")))
                    break
                case ISpringErrorResponse.class.name:
                    Assertions.fail(((ISpringErrorResponse) result)?.message)
                    break
                default:
                    Assertions.fail(result?.toString())
                    break
            }
        } else {
            Assertions.fail("result is null")
        }
    }

    @Test
    @Order(21)
    void updateDepartmentTest() {
        Assertions.assertNotNull(bossISpringDepartmentRepository)
        Assertions.assertNotNull(bossISpringDepartment)
        Assertions.assertNotNull(bossISpringDepartment.id)

        Optional<BossISpringDepartment> result = bossISpringDepartmentRepository.findById(bossISpringDepartment.id)
        Assertions.assertNotNull(result)
        Assertions.assertEquals(true, result?.isPresent())
        Assertions.assertEquals(bossISpringDepartment.id, result?.get()?.id)
        BossISpringDepartment bossISpringDepartmentUpdated = result?.get()

        bossISpringDepartmentUpdated.setName("Test department (Updated)")
        final String ACTION = "U"
        bossISpringDepartmentUpdated.setAction(ACTION)
        bossISpringDepartmentUpdated.setUpdated(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
        bossISpringDepartmentUpdated.setUnloaded(null)

        BossISpringDepartment bossISpringDepartmentResult = bossISpringDepartmentRepository.save(bossISpringDepartmentUpdated)

        Assertions.assertEquals(ACTION, bossISpringDepartmentResult?.action)
        bossISpringDepartment = bossISpringDepartmentResult
    }

    @Test
    @Order(22)
    void checkSyncToISpringForUpdateDepartmentTest() {
        await().atMost(30, SECONDS).until(() -> isUpdateDepartmentSynchronized)

        Assertions.assertNotNull(bossISpringDepartment)
        Assertions.assertNotNull(bossISpringDepartment.iSpringId)
        Assertions.assertEquals(false, bossISpringDepartment.iSpringId.isEmpty())
        Assertions.assertNotNull(iSpringDepartmentService)

        def response = iSpringDepartmentService.getISpringDepartment(BossISpringDepartmentMapper.toISpringDepartmentId(bossISpringDepartment))
        Assertions.assertNotNull(response)
        boolean taskDone = false
        def result = null
        Throwable throwable = null
        response
                .doFinally(r -> { taskDone = true })
                .subscribe(value -> result = value,
                        error -> throwable = error)
        await().atMost(60, SECONDS).until(() -> taskDone)
        if (throwable != null) {
            Assertions.fail(throwable)
        }
        if (result != null) {
            Assertions.assertNotNull(result)
            switch (result?.getClass()?.name) {
                case ISpringDepartment.class.name:
                    Assertions.assertEquals(bossISpringDepartment.iSpringId, ((ISpringDepartment) result)?.departmentId?.toString())
                    Assertions.assertEquals("Test department (Updated)", ((ISpringDepartment) result)?.name?.toString())
                    break
                case ISpringErrorResponse.class.name:
                    Assertions.fail(((ISpringErrorResponse) result)?.message)
                    break
                default:
                    Assertions.fail(result?.toString())
                    break
            }
        } else {
            Assertions.fail("result is null")
        }
    }

    @Test
    @Order(31)
    void updateUserTest() {
        Assertions.assertNotNull(bossISpringUserRepository)
        Assertions.assertNotNull(bossISpringUser)
        Assertions.assertNotNull(bossISpringUser.id)

        Optional<BossISpringUser> result = bossISpringUserRepository.findById(bossISpringUser.id)
        Assertions.assertNotNull(result)
        Assertions.assertEquals(true, result?.isPresent())
        Assertions.assertEquals(bossISpringUser.id, result?.get()?.id)
        BossISpringUser bossISpringUserUpdated = result?.get()

        bossISpringUserUpdated.setFirstName("FirstName (Updated)")
        final String ACTION = "U"
        bossISpringUserUpdated.setAction(ACTION)
        bossISpringUserUpdated.setUpdated(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
        bossISpringUserUpdated.setUnloaded(null)

        BossISpringUser bossISpringUserResult = bossISpringUserRepository.save(bossISpringUserUpdated)

        Assertions.assertEquals(ACTION, bossISpringUserResult?.action)
        bossISpringUser = bossISpringUserResult
    }

    @Test
    @Order(32)
    void checkSyncToISpringForUpdateUserTest() {
        await().atMost(30, SECONDS).until(() -> isUpdateUserSynchronized)

        Assertions.assertNotNull(bossISpringUser)
        Assertions.assertNotNull(bossISpringUser.iSpringId)
        Assertions.assertEquals(false, bossISpringUser.iSpringId.isEmpty())
        Assertions.assertNotNull(iSpringUserService)

        def response = iSpringUserService.getISpringUser(BossISpringUserMapper.toISpringUserId(bossISpringUser))
        Assertions.assertNotNull(response)
        boolean taskDone = false
        def result = null
        Throwable throwable = null
        response
                .doFinally(r -> { taskDone = true })
                .subscribe(value -> result = value,
                        error -> throwable = error)
        await().atMost(60, SECONDS).until(() -> taskDone)
        if (throwable != null) {
            Assertions.fail(throwable)
        }
        if (result != null) {
            Assertions.assertNotNull(result)
            switch (result?.getClass()?.name) {
                case ISpringUser.class.name:
                    Assertions.assertEquals(bossISpringUser.iSpringId, ((ISpringUser) result)?.userId?.toString())
                    boolean isFirstNameFoundAndCorrect = false
                    ((ISpringUser) result)?.fields.each {
                        isFirstNameFoundAndCorrect = isFirstNameFoundAndCorrect | (it.getName() == "FIRST_NAME" && it.getValue() == "FirstName (Updated)")
                    }
                    Assertions.assertEquals(true, isFirstNameFoundAndCorrect)
                    break
                case ISpringErrorResponse.class.name:
                    Assertions.fail(((ISpringErrorResponse) result).message)
                    break
                default:
                    Assertions.fail(result?.toString())
                    break
            }
        } else {
            Assertions.fail("result is null")
        }
    }

    @Test
    @Order(41)
    void updateDepartmentSubordinationTest() {
        await().atMost(30, SECONDS).until(() -> isCreateUserSynchronized)
        Assertions.assertNotNull(bossISpringDepartmentRepository)
        Assertions.assertNotNull(bossISpringDepartment)
        Assertions.assertNotNull(bossISpringDepartment.id)

        Optional<BossISpringDepartment> result = bossISpringDepartmentRepository.findById(bossISpringDepartment.id)
        Assertions.assertNotNull(result)
        Assertions.assertEquals(true, result?.isPresent())
        Assertions.assertEquals(bossISpringDepartment.id, result?.get()?.id)
        BossISpringDepartment bossISpringDepartmentSubordinationUpdated = result?.get()

        bossISpringDepartmentSubordinationUpdated.setSubordinationType(ISpringSubordination.SubordinationType.no_supervisor.toString())
        final String ACTION = "S"
        bossISpringDepartmentSubordinationUpdated.setAction(ACTION)
        bossISpringDepartmentSubordinationUpdated.setUpdated(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
        bossISpringDepartmentSubordinationUpdated.setUnloaded(null)

        BossISpringDepartment bossISpringDepartmentResult = bossISpringDepartmentRepository.save(bossISpringDepartmentSubordinationUpdated)

        Assertions.assertEquals(ACTION, bossISpringDepartmentResult?.action)
        bossISpringDepartment = bossISpringDepartmentResult
    }

    @Test
    @Order(42)
    void checkSyncToISpringForUpdateDepartmentSubordinationTest() {
        await().atMost(30, SECONDS).until(() -> isSubordinationUpdateDepartmentSynchronized)

        Assertions.assertNotNull(bossISpringDepartment)
        Assertions.assertNotNull(bossISpringDepartment.iSpringId)
        Assertions.assertEquals(false, bossISpringDepartment.iSpringId.isEmpty())
        Assertions.assertNotNull(iSpringDepartmentService)

        def response = iSpringDepartmentService.getISpringDepartment(BossISpringDepartmentMapper.toISpringDepartmentId(bossISpringDepartment))
        Assertions.assertNotNull(response)
        boolean taskDone = false
        def result = null
        Throwable throwable = null
        response
                .doFinally(r -> { taskDone = true })
                .subscribe(value -> result = value,
                        error -> throwable = error)
        await().atMost(60, SECONDS).until(() -> taskDone)
        if (throwable != null) {
            Assertions.fail(throwable)
        }
        if (result != null) {
            Assertions.assertNotNull(result)
            switch (result?.getClass()?.name) {
                case ISpringDepartment.class.name:
                    Assertions.assertEquals(bossISpringDepartment.iSpringId, ((ISpringDepartment) result)?.departmentId?.toString())
                    Assertions.assertEquals(ISpringSubordination.SubordinationType.no_supervisor.toString(), ((ISpringDepartment) result)?.subordination?.subordinationType)
//                    Assertions.assertEquals(ISpringSubordination.SubordinationType.no_supervisor.toString(), ((ISpringDepartment) result)?.subordination?.subordinationType)
                    break
                case ISpringErrorResponse.class.name:
                    Assertions.fail(((ISpringErrorResponse) result)?.message)
                    break
                default:
                    Assertions.fail(result?.toString())
                    break
            }
        } else {
            Assertions.fail("result is null")
        }
    }

    @Test
    @Order(51)
    void updateUserStatusTest() {
        Assertions.assertNotNull(bossISpringUserRepository)
        Assertions.assertNotNull(bossISpringUser)
        Assertions.assertNotNull(bossISpringUser.id)

        Optional<BossISpringUser> result = bossISpringUserRepository.findById(bossISpringUser.id)
        Assertions.assertNotNull(result)
        Assertions.assertEquals(true, result?.isPresent())
        Assertions.assertEquals(bossISpringUser.id, result?.get()?.id)
        BossISpringUser bossISpringUserUpdated = result?.get()

        bossISpringUserUpdated.setStatus(3)
        final String ACTION = "S"
        bossISpringUserUpdated.setAction(ACTION)
        bossISpringUserUpdated.setUpdated(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
        bossISpringUserUpdated.setUnloaded(null)

        BossISpringUser bossISpringUserResult = bossISpringUserRepository.save(bossISpringUserUpdated)

        Assertions.assertEquals(ACTION, bossISpringUserResult?.action)
        bossISpringUser = bossISpringUserResult
    }

    @Test
    @Order(52)
    void checkSyncToISpringForUpdateUserStatusTest() {
        await().atMost(30, SECONDS).until(() -> isStatusUpdateUserSynchronized)

        Assertions.assertNotNull(bossISpringUser)
        Assertions.assertNotNull(bossISpringUser.iSpringId)
        Assertions.assertNotNull(bossISpringUser.status)
        Assertions.assertEquals(false, bossISpringUser.iSpringId.isEmpty())
        Assertions.assertNotNull(iSpringUserService)

        def response = iSpringUserService.getISpringUser(BossISpringUserMapper.toISpringUserId(bossISpringUser))
        Assertions.assertNotNull(response)
        boolean taskDone = false
        def result = null
        Throwable throwable = null
        response
                .doFinally(r -> { taskDone = true })
                .subscribe(value -> result = value,
                        error -> throwable = error)
        await().atMost(60, SECONDS).until(() -> taskDone)
        if (throwable != null) {
            Assertions.fail(throwable)
        }
        if (result != null) {
            Assertions.assertNotNull(result)
            switch (result?.getClass()?.name) {
                case ISpringUser.class.name:
                    Assertions.assertEquals(bossISpringUser.iSpringId, ((ISpringUser) result)?.userId?.toString())
                    Assertions.assertEquals(bossISpringUser.status.intValue(), ((ISpringUser) result)?.status?.intValue())
                    break
                case ISpringErrorResponse.class.name:
                    Assertions.fail(((ISpringErrorResponse) result)?.message)
                    break
                default:
                    Assertions.fail(result?.toString())
                    break
            }
        } else {
            Assertions.fail("result is null")
        }
    }

    @Test
    @Order(61)
    void removeUserFromGroupTest() {
        Assertions.assertNotNull(bossISpringUserRepository)
        Assertions.assertNotNull(bossISpringUser)
        Assertions.assertNotNull(bossISpringUser.id)

        Optional<BossISpringUser> result = bossISpringUserRepository.findById(bossISpringUser.id)
        Assertions.assertNotNull(result)
        Assertions.assertEquals(true, result?.isPresent())
        Assertions.assertEquals(bossISpringUser.id, result?.get()?.id)
        BossISpringUser bossISpringUserUpdated = result?.get()

        bossISpringUserUpdated.setStatus(3)
        final String ACTION = "R"
        bossISpringUserUpdated.setAction(ACTION)
        bossISpringUserUpdated.setGroupIds_remove("bab52566-15b3-11ee-b539-7af98ac59c08")
        bossISpringUserUpdated.setUpdated(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
        bossISpringUserUpdated.setUnloaded(null)

        BossISpringUser bossISpringUserResult = bossISpringUserRepository.save(bossISpringUserUpdated)

        Assertions.assertEquals(ACTION, bossISpringUserResult?.action)
        bossISpringUser = bossISpringUserResult
    }

    @Test
    @Order(62)
    void checkSyncToISpringForRemoveUserFromGroupTest() {
        await().atMost(30, SECONDS).until(() -> isRemoveFromGroupUserSynchronized)

        Assertions.assertNotNull(bossISpringUser)
        Assertions.assertNotNull(bossISpringUser.iSpringId)
        Assertions.assertNotNull(bossISpringUser.status)
        Assertions.assertEquals(false, bossISpringUser.iSpringId.isEmpty())
        Assertions.assertNotNull(iSpringUserService)

        def response = iSpringUserService.getISpringUser(BossISpringUserMapper.toISpringUserId(bossISpringUser))
        Assertions.assertNotNull(response)
        boolean taskDone = false
        def result = null
        Throwable throwable = null
        response
                .doFinally(r -> { taskDone = true })
                .subscribe(value -> result = value,
                        error -> throwable = error)
        await().atMost(60, SECONDS).until(() -> taskDone)
        if (throwable != null) {
            Assertions.fail(throwable)
        }
        if (result != null) {
            Assertions.assertNotNull(result)
            switch (result?.getClass()?.name) {
                case ISpringUser.class.name:
                    Assertions.assertEquals(bossISpringUser.iSpringId, ((ISpringUser) result)?.userId?.toString())
                    Assertions.assertEquals(true, ((ISpringUser) result)?.groups?.contains(UUID.fromString("2428b8e8-05d1-11ee-9897-6a8398f95423")))
                    Assertions.assertEquals(false, ((ISpringUser) result)?.groups?.contains(UUID.fromString("bab52566-15b3-11ee-b539-7af98ac59c08")))
                    break
                case ISpringErrorResponse.class.name:
                    Assertions.fail(((ISpringErrorResponse) result)?.message)
                    break
                default:
                    Assertions.fail(result?.toString())
                    break
            }
        } else {
            Assertions.fail("result is null")
        }
    }

    @Test
    @Order(71)
    void removeUserTest() {
        Assertions.assertNotNull(bossISpringUserRepository)
        Assertions.assertNotNull(bossISpringUser)
        Assertions.assertNotNull(bossISpringUser.id)

        Optional<BossISpringUser> result = bossISpringUserRepository.findById(bossISpringUser.id)
        Assertions.assertNotNull(result)
        Assertions.assertEquals(true, result?.isPresent())
        Assertions.assertEquals(bossISpringUser.id, result?.get()?.id)
        bossISpringUser = result?.get()

        final String ACTION = "D"
        bossISpringUser.setAction(ACTION)
        bossISpringUser.setUpdated(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
        bossISpringUser.setUnloaded(null)

        BossISpringUser bossISpringUserResult = bossISpringUserRepository.save(bossISpringUser)

        Assertions.assertEquals(ACTION, bossISpringUserResult?.action)
    }

    @Test
    @Order(72)
    void checkSyncToISpringForRemoveUserTest() {
        await().atMost(30, SECONDS).until(() -> isRemoveUserSynchronized)

        Assertions.assertNotNull(bossISpringUser)
        Assertions.assertNotNull(bossISpringUser.iSpringId)
        Assertions.assertEquals(false, bossISpringUser.iSpringId.isEmpty())
        Assertions.assertNotNull(iSpringUserService)

        def response = iSpringUserService.getISpringUser(BossISpringUserMapper.toISpringUserId(bossISpringUser))
        Assertions.assertNotNull(response)
        boolean taskDone = false
        def result = null
        Throwable throwable = null
        response
                .doFinally(r -> { taskDone = true })
                .subscribe(value -> result = value,
                        error -> throwable = error)
        await().atMost(60, SECONDS).until(() -> taskDone)
        if (throwable != null) {
            Assertions.fail(throwable)
        }
        if (result != null) {
            Assertions.assertNotNull(result)
            switch (result?.getClass()?.name) {
                case ISpringUser.class.name:
                    Assertions.fail(result?.toString())
                    break
                case ISpringErrorResponse.class.name:
                    Assertions.assertEquals(400, ((ISpringErrorResponse) result)?.code)
                    break
                default:
                    Assertions.fail(result?.toString())
                    break
            }
        } else {
            Assertions.fail("result is null")
        }
    }

    @Test
    @Order(81)
    void removeDepartmentTest() {
        await().atMost(30, SECONDS).until(() -> isRemoveUserSynchronized)

        Assertions.assertNotNull(bossISpringDepartmentRepository)
        Assertions.assertNotNull(bossISpringDepartment)
        Assertions.assertNotNull(bossISpringDepartment.id)

        Optional<BossISpringDepartment> result = bossISpringDepartmentRepository.findById(bossISpringDepartment.id)
        Assertions.assertNotNull(result)
        Assertions.assertEquals(true, result?.isPresent())
        Assertions.assertEquals(bossISpringDepartment.id, result?.get()?.id)
        bossISpringDepartment = result?.get()

        final String ACTION = "D"
        bossISpringDepartment.setAction(ACTION)
        bossISpringDepartment.setUpdated(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
        bossISpringDepartment.setUnloaded(null)

        BossISpringDepartment bossISpringDepartmentResult = bossISpringDepartmentRepository.save(bossISpringDepartment)

        Assertions.assertEquals(ACTION, bossISpringDepartmentResult?.action)
    }

    @Test
    @Order(82)
    void checkSyncToISpringForRemoveDepartmentTest() {
        await().atMost(30, SECONDS).until(() -> isRemoveDepartmentSynchronized)

        Assertions.assertNotNull(bossISpringDepartment)
        Assertions.assertNotNull(bossISpringDepartment.iSpringId)
        Assertions.assertEquals(false, bossISpringDepartment.iSpringId.isEmpty())
        Assertions.assertNotNull(iSpringDepartmentService)

        def response = iSpringDepartmentService.getISpringDepartment(BossISpringDepartmentMapper.toISpringDepartmentId(bossISpringDepartment))
        Assertions.assertNotNull(response)
        boolean taskDone = false
        def result = null
        Throwable throwable = null
        response
                .doFinally(r -> { taskDone = true })
                .subscribe(value -> result = value,
                        error -> throwable = error)
        await().atMost(60, SECONDS).until(() -> taskDone)
        if (throwable != null) {
            Assertions.fail(throwable)
        }
        if (result != null) {
            Assertions.assertNotNull(result)
            switch (result?.getClass()?.name) {
                case ISpringDepartment.class.name:
                    Assertions.fail(result?.toString())
                    break
                case ISpringErrorResponse.class.name:
                    Assertions.assertEquals(400, ((ISpringErrorResponse) result)?.code)
                    break
                default:
                    Assertions.fail(result?.toString())
                    break
            }
        } else {
            Assertions.fail("result is null")
        }
    }

    @Test
    @Order(101)
    void finalCleanUser() {
        await().atMost(30, SECONDS).until(() -> isRemoveUserSynchronized)

        Assertions.assertNotNull(bossISpringUserRepository)
        Assertions.assertNotNull(bossISpringUser)
        Assertions.assertNotNull(bossISpringUser.id)

        bossISpringUserRepository.delete(bossISpringUser)

        Optional<BossISpringUser> result = bossISpringUserRepository.findById(bossISpringUser.id)
        Assertions.assertNotNull(result)
        Assertions.assertEquals(false, result?.isPresent())
    }

    @Test
    @Order(102)
    void finalCleanDepartment() {
        await().atMost(30, SECONDS).until(() -> isRemoveDepartmentSynchronized)

        Assertions.assertNotNull(bossISpringDepartmentRepository)
        Assertions.assertNotNull(bossISpringDepartment)
        Assertions.assertNotNull(bossISpringDepartment.id)

        bossISpringDepartmentRepository.delete(bossISpringDepartment)

        Optional<BossISpringDepartment> result = bossISpringDepartmentRepository.findById(bossISpringDepartment.id)
        Assertions.assertNotNull(result)
        Assertions.assertEquals(false, result?.isPresent())
    }
}
