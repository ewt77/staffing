package test.ru.bank.integration.staffing.service

import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.bank.integration.staffing.model.ispring.*
import ru.bank.integration.staffing.service.ISpringUserService
import test.ru.bank.integration.staffing.BaseStaffingTest

import static java.util.concurrent.TimeUnit.SECONDS
import static org.awaitility.Awaitility.await

/**
 *
 * @author Author
 */
@Component
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ISpringUserServiceTest extends BaseStaffingTest {
    private static ISpringUserNew iSpringNewUser = new ISpringUserNew()
    private static ISpringUserUpdate iSpringUserUpdate = new ISpringUserUpdate()
    private static ISpringGroupList iSpringGroupList = new ISpringGroupList()

    @Autowired
    private ISpringUserService iSpringUserService

    @BeforeAll
    static void setUp() {
        iSpringNewUser?.setDepartmentId(UUID.fromString("60246916-96f8-11ed-8bb3-2ecf86751afb"))
        iSpringUserUpdate?.setDepartmentId(UUID.fromString("60246916-96f8-11ed-8bb3-2ecf86751afb"))

        Map<String, String> iSpringUserProfileFields = new HashMap<>()
        iSpringUserProfileFields.put("LOGIN", "login_test")
        iSpringUserProfileFields.put("EMAIL", "email_test@no.ru")
        iSpringNewUser?.setFields(iSpringUserProfileFields)
        iSpringNewUser?.setGroupIds([
                UUID.fromString("2428b8e8-05d1-11ee-9897-6a8398f95423"),
                UUID.fromString("bab52566-15b3-11ee-b539-7af98ac59c08")
        ] as UUID[])

        Map<String, String> iSpringUserProfileFieldsUpdate = new HashMap<>()
        iSpringUserProfileFields.put("FIRST_NAME", "FirstName") // adds new field without affecting existed ones
        iSpringUserUpdate?.setFields(iSpringUserProfileFieldsUpdate)
        iSpringGroupList?.setGroupIds([
                UUID.fromString("bab52566-15b3-11ee-b539-7af98ac59c08")
        ] as UUID[])
    }

    @Test
    @Order(value = 1)
    void addISpringUserTest() {
        Assertions.assertNotNull(iSpringUserService)
        def response = iSpringUserService.addISpringUser(iSpringNewUser)
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
                case ISpringUserId.class.name:
                    Assertions.assertNotNull((ISpringUserId) result)
                    Assertions.assertNotNull(((ISpringUserId) result)?.getUserId())
                    iSpringUserUpdate?.setUserId(((ISpringUserId) result)?.getUserId())
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

//    @Test
//    @Order(value = 2)
    void getISpringUsersTest() {
        Assertions.assertNotNull(iSpringUserService)
        def response = iSpringUserService.getISpringUsers()
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
                case ISpringUser[].class.name:
                    Assertions.assertEquals(false, ((ISpringUser[]) result)?.length == 0)
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
    @Order(value = 3)
    void getISpringUserAfterAddTest() {
        Assertions.assertNotNull(iSpringUserUpdate)
        Assertions.assertNotNull(iSpringUserUpdate?.userId)
        Assertions.assertEquals(false, iSpringUserUpdate?.getUserId()?.toString()?.isEmpty())
        Assertions.assertNotNull(iSpringUserService)
        def response = iSpringUserService.getISpringUser(iSpringUserUpdate)
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
                    Assertions.assertEquals(iSpringUserUpdate?.userId, ((ISpringUser) result)?.userId)
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
    @Order(value = 4)
    void updateISpringUserTest() {
        Assertions.assertNotNull(iSpringUserUpdate)
        Assertions.assertNotNull(iSpringUserUpdate?.userId)
        Assertions.assertEquals(false, iSpringUserUpdate?.getUserId()?.toString()?.isEmpty())
        Assertions.assertNotNull(iSpringUserService)

        iSpringUserUpdate?.fields?.put("FIRST_NAME", "FirstName (Updated)")

        def response = iSpringUserService.updateISpringUser(iSpringUserUpdate)
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
                case String.class.name:
                    Assertions.assertNotNull((String) result)
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
    @Order(value = 5)
    void getISpringUserAfterUpdateTest() {
        Assertions.assertNotNull(iSpringUserUpdate)
        Assertions.assertNotNull(iSpringUserUpdate?.userId)
        Assertions.assertEquals(false, iSpringUserUpdate?.getUserId()?.toString()?.isEmpty())
        Assertions.assertNotNull(iSpringUserService)
        def response = iSpringUserService.getISpringUser(iSpringUserUpdate)
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
                    Assertions.assertEquals(iSpringUserUpdate?.userId, ((ISpringUser) result)?.userId)
                    Assertions.assertEquals(
                            true,
                            Arrays
                                    .stream(((ISpringUser) result)?.fields)
                                    .anyMatch {
                                        "FIRST_NAME"::equals(it.name) && "FirstName (Updated)"::equals(it.value)
                                    })
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
    @Order(value = 6)
    void updateStatusISpringUserTest() {
        Assertions.assertNotNull(iSpringUserUpdate)
        Assertions.assertNotNull(iSpringUserUpdate?.userId)
        Assertions.assertEquals(false, iSpringUserUpdate?.getUserId()?.toString()?.isEmpty())

        ISpringUserStatusUpdate iSpringUserStatusUpdate = new ISpringUserStatusUpdate()
        iSpringUserStatusUpdate.setStatus(ISpringUser.StatusType.inactive)

        Assertions.assertNotNull(iSpringUserService)
        def response = iSpringUserService.updateISpringUserStatus(iSpringUserUpdate, iSpringUserStatusUpdate)
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
                case String.class.name:
                    Assertions.assertNotNull((String) result)
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
    @Order(value = 7)
    void getISpringUserAfterUpdateStatusTest() {
        Assertions.assertNotNull(iSpringUserUpdate)
        Assertions.assertNotNull(iSpringUserUpdate?.userId)
        Assertions.assertEquals(false, iSpringUserUpdate?.getUserId()?.toString()?.isEmpty())
        Assertions.assertNotNull(iSpringUserService)
        def response = iSpringUserService.getISpringUser(iSpringUserUpdate)
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
                    Assertions.assertEquals(iSpringUserUpdate?.userId, ((ISpringUser) result)?.userId)
                    Assertions.assertEquals(ISpringUser.StatusType.inactive.value, ((ISpringUser) result)?.status)
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
    @Order(value = 8)
    void removeFromGroupISpringUserTest() {
        Assertions.assertNotNull(iSpringUserUpdate)
        Assertions.assertNotNull(iSpringUserUpdate?.userId)
        Assertions.assertEquals(false, iSpringUserUpdate?.getUserId()?.toString()?.isEmpty())
        Assertions.assertNotNull(iSpringUserService)
        Assertions.assertNotNull(iSpringGroupList)
        Assertions.assertEquals(false, iSpringGroupList?.getGroupIds()?.length == 0)

        def response = iSpringUserService.removeISpringUserFromGroup(iSpringUserUpdate, iSpringGroupList)
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
                case String.class.name:
                    Assertions.assertNotNull((String) result)
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
    @Order(value = 9)
    void getISpringUserAfterRemoveFromGroupTest() {
        Assertions.assertNotNull(iSpringUserUpdate)
        Assertions.assertNotNull(iSpringUserUpdate?.userId)
        Assertions.assertEquals(false, iSpringUserUpdate?.getUserId()?.toString()?.isEmpty())
        Assertions.assertNotNull(iSpringUserService)
        def response = iSpringUserService.getISpringUser(iSpringUserUpdate)
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
                    Assertions.assertEquals(iSpringUserUpdate?.userId, ((ISpringUser) result)?.userId)
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
    @Order(value = 10)
    void removeISpringUserTest() {
        Assertions.assertNotNull(iSpringUserUpdate)
        Assertions.assertNotNull(iSpringUserUpdate?.userId)
        Assertions.assertEquals(false, iSpringUserUpdate?.getUserId()?.toString()?.isEmpty())
        Assertions.assertNotNull(iSpringUserService)
        def response = iSpringUserService.removeISpringUser(iSpringUserUpdate)
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
                case String.class.name:
                    Assertions.assertNotNull((String) result)
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
    @Order(value = 11)
    void getISpringUserAfterRemoveTest() {
        Assertions.assertNotNull(iSpringUserUpdate)
        Assertions.assertNotNull(iSpringUserUpdate?.userId)
        Assertions.assertEquals(false, iSpringUserUpdate?.getUserId()?.toString()?.isEmpty())
        Assertions.assertNotNull(iSpringUserService)
        def response = iSpringUserService.getISpringUser(iSpringUserUpdate)
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
                    Assertions.fail(((ISpringUser) result)?.userId?.toString())
                    break
                case ISpringErrorResponse.class.name:
                    Assertions.assertEquals(400,((ISpringErrorResponse) result)?.code)
                    break
                default:
                    Assertions.fail(result?.toString())
                    break
            }
        } else {
            Assertions.fail("result is null")
        }
    }
}
