package test.ru.bank.integration.staffing.service

import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.bank.integration.staffing.model.ispring.*
import ru.bank.integration.staffing.service.ISpringDepartmentService
import test.ru.bank.integration.staffing.BaseStaffingTest

import static java.util.concurrent.TimeUnit.SECONDS
import static org.awaitility.Awaitility.await

/**
 *
 * @author Author
 */
@Component
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ISpringDepartmentServiceTest extends BaseStaffingTest {
    private static ISpringDepartmentNew iSpringNewDepartment = new ISpringDepartmentNew()
    private static ISpringDepartmentUpdate iSpringDepartmentUpdate = new ISpringDepartmentUpdate()

    @Autowired
    private ISpringDepartmentService iSpringDepartmentService

    @BeforeAll
    static void setUp() {
        iSpringNewDepartment.setName("Test department")
        iSpringNewDepartment.setParentDepartmentId(UUID.fromString("60246916-96f8-11ed-8bb3-2ecf86751afb"))
        iSpringNewDepartment.setCode("10000")

        iSpringDepartmentUpdate?.setName("Test department (updated)")
        iSpringDepartmentUpdate?.setParentDepartmentId(UUID.fromString("60246916-96f8-11ed-8bb3-2ecf86751afb"))
        iSpringDepartmentUpdate?.setCode("10000")
    }

    @Test
    @Order(value = 1)
    void addISpringDepartmentTest() {
        Assertions.assertNotNull(iSpringDepartmentService)
        def response = iSpringDepartmentService.addISpringDepartment(iSpringNewDepartment)
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
                case ISpringDepartmentId.class.name:
                    Assertions.assertNotNull((ISpringDepartmentId) result)
                    Assertions.assertNotNull(((ISpringDepartmentId) result)?.getDepartmentId())
                    iSpringDepartmentUpdate?.setDepartmentId(((ISpringDepartmentId) result)?.departmentId)
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
    @Order(value = 2)
    void getISpringDepartmentsTest() {
        Assertions.assertNotNull(iSpringDepartmentService)
        def response = iSpringDepartmentService.getISpringDepartments()
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
                case ISpringDepartmentList.class.name:
                    Assertions.assertEquals(false, ((ISpringDepartmentList) result)?.departments.size() == 0)
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
    void getISpringDepartmentAfterAddTest() {
        Assertions.assertNotNull(iSpringDepartmentUpdate)
        Assertions.assertNotNull(iSpringDepartmentUpdate?.departmentId)
        Assertions.assertEquals(false, iSpringDepartmentUpdate?.getDepartmentId()?.toString()?.isEmpty())
        Assertions.assertNotNull(iSpringDepartmentService)
        def response = iSpringDepartmentService.getISpringDepartment(iSpringDepartmentUpdate)
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
                    Assertions.assertEquals(iSpringDepartmentUpdate?.departmentId, ((ISpringDepartment) result)?.departmentId)
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
    void updateISpringDepartmentTest() {
        Assertions.assertNotNull(iSpringDepartmentUpdate)
        Assertions.assertNotNull(iSpringDepartmentUpdate?.departmentId)
        Assertions.assertEquals(false, iSpringDepartmentUpdate?.getDepartmentId()?.toString()?.isEmpty())
        Assertions.assertNotNull(iSpringDepartmentService)
        def response = iSpringDepartmentService.updateISpringDepartment(iSpringDepartmentUpdate)
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
    void getISpringDepartmentAfterUpdateTest() {
        Assertions.assertNotNull(iSpringDepartmentUpdate)
        Assertions.assertNotNull(iSpringDepartmentUpdate?.departmentId)
        Assertions.assertEquals(false, iSpringDepartmentUpdate?.getDepartmentId()?.toString()?.isEmpty())
        Assertions.assertNotNull(iSpringDepartmentService)
        def response = iSpringDepartmentService.getISpringDepartment(iSpringDepartmentUpdate)
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
                    Assertions.assertEquals(iSpringDepartmentUpdate?.departmentId, ((ISpringDepartment) result)?.departmentId)
                    Assertions.assertEquals(iSpringDepartmentUpdate?.name, ((ISpringDepartment) result)?.name)
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
    void updateSubordinationISpringDepartmentTest() {
        Assertions.assertNotNull(iSpringDepartmentUpdate)
        Assertions.assertNotNull(iSpringDepartmentUpdate?.departmentId)
        Assertions.assertEquals(false, iSpringDepartmentUpdate?.getDepartmentId()?.toString()?.isEmpty())

        ISpringSubordination iSpringSubordination = new ISpringSubordination()
        iSpringSubordination.setSubordinationType(ISpringSubordination.SubordinationType.no_supervisor)

        ISpringDepartmentSubordinationUpdate iSpringDepartmentSubordinationUpdate = new ISpringDepartmentSubordinationUpdate()
        iSpringDepartmentSubordinationUpdate.setSubordination(iSpringSubordination)
//        iSpringDepartmentSubordinationUpdate.setCoSubordination(iSpringSubordination)

        Assertions.assertNotNull(iSpringDepartmentService)
        def response = iSpringDepartmentService.updateSubordinationISpringDepartment(iSpringDepartmentUpdate, iSpringDepartmentSubordinationUpdate)
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
    void getISpringDepartmentAfterUpdateSubordinationTest() {
        Assertions.assertNotNull(iSpringDepartmentUpdate)
        Assertions.assertNotNull(iSpringDepartmentUpdate?.departmentId)
        Assertions.assertEquals(false, iSpringDepartmentUpdate?.getDepartmentId()?.toString()?.isEmpty())
        Assertions.assertNotNull(iSpringDepartmentService)
        def response = iSpringDepartmentService.getISpringDepartment(iSpringDepartmentUpdate)
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
                    Assertions.assertEquals(iSpringDepartmentUpdate?.departmentId, ((ISpringDepartment) result)?.departmentId)
                    Assertions.assertEquals(ISpringSubordination.SubordinationType.no_supervisor.toString(), ((ISpringDepartment) result)?.subordination.subordinationType.toString())
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
    void removeISpringDepartmentTest() {
        Assertions.assertNotNull(iSpringDepartmentUpdate)
        Assertions.assertNotNull(iSpringDepartmentUpdate?.departmentId)
        Assertions.assertEquals(false, iSpringDepartmentUpdate?.getDepartmentId()?.toString()?.isEmpty())
        Assertions.assertNotNull(iSpringDepartmentService)
        def response = iSpringDepartmentService.removeISpringDepartment(iSpringDepartmentUpdate)
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
    void getISpringDepartmentAfterRemoveTest() {
        Assertions.assertNotNull(iSpringDepartmentUpdate)
        Assertions.assertNotNull(iSpringDepartmentUpdate?.departmentId)
        Assertions.assertEquals(false, iSpringDepartmentUpdate?.getDepartmentId()?.toString()?.isEmpty())
        Assertions.assertNotNull(iSpringDepartmentService)
        def response = iSpringDepartmentService.getISpringDepartment(iSpringDepartmentUpdate)
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
                    Assertions.fail(((ISpringDepartment) result)?.departmentId?.toString())
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
