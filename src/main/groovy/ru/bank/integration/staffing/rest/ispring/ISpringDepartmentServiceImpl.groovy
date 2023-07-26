package ru.bank.integration.staffing.rest.ispring

import groovyjarjarantlr4.v4.runtime.misc.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import reactor.core.publisher.Mono
import ru.bank.integration.staffing.exception.ispring.AddISpringDepartmentServiceException
import ru.bank.integration.staffing.exception.ispring.ChangeSubordinationISpringDepartmentServiceException
import ru.bank.integration.staffing.exception.ispring.RemoveISpringDepartmentServiceException
import ru.bank.integration.staffing.exception.ispring.UpdateISpringDepartmentServiceException
import ru.bank.integration.staffing.model.ispring.*
import ru.bank.integration.staffing.service.ISpringDepartmentService

/**
 *
 * @author Author
 */
@Configuration
class ISpringDepartmentServiceImpl extends ISpringServiceImpl implements ISpringDepartmentService {

    /**
     *
     * @author Author
     */
    enum RESOURCE_KEY {
        ErrorGotExceptionWileAddISpringDepartment,
        ErrorGotExceptionWileChangeSubordinationISpringDepartment,
        ErrorGotExceptionWileUpdateISpringDepartment,
        ErrorGotExceptionWileRemoveISpringDepartment
    }

    @Autowired
    private ISpringWebClientBuilderFactory iSpringWebClientBuilderFactory

    Mono getISpringDepartments() {
        iSpringWebClientBuilderFactory
                ?.setHeaders(iSpringWebClientBuilderFactory
                        ?.getObject()
                        ?.get()
                        ?.uri("/departments"))
                ?.exchangeToMono { response ->
                    {
                        if (response?.statusCode() == HttpStatus.OK) {
                            // departments: { type: array, items: { $ref: '#/components/schemas/Department:4340' } }:719
                            return response?.bodyToMono(ISpringDepartmentList.class)
                        } else return doErrorsHandle(response)
                    }
                }
    }

    Mono getISpringDepartment(@NotNull ISpringDepartmentId iSpringDepartmentId) {
        iSpringWebClientBuilderFactory
                ?.setHeaders(iSpringWebClientBuilderFactory
                        ?.getObject()
                        ?.get()
                        ?.uri(uriBuilder -> uriBuilder
                                ?.path("/department/{departmentId}")
                                ?.build(iSpringDepartmentId.departmentId)))
                ?.exchangeToMono { response ->
                    {
                        if (response?.statusCode() == HttpStatus.OK) {
                            // #/components/schemas/Department:44080
                            return response?.bodyToMono(ISpringDepartment.class)
                        } else return doErrorsHandle(response)
                    }
                }
    }

    Mono addISpringDepartment(@NotNull ISpringDepartmentNew iSpringDepartmentNew) throws AddISpringDepartmentServiceException {
        try {
            // #/components/schemas/NewDepartment:3628
            iSpringWebClientBuilderFactory
                    ?.setHeaders(iSpringWebClientBuilderFactory
                            ?.getObject()
                            ?.post()
                            ?.uri("/department")
                            ?.bodyValue(iSpringDepartmentNew))
                    ?.header("X-Return-Object", "true") // #/components/parameters/returnObject:5116
                    ?.exchangeToMono { response ->
                        {
                            if (response?.statusCode() == HttpStatus.CREATED) {
                                return response?.bodyToMono(ISpringDepartmentId.class) // :761
                            } else return doErrorsHandle(response)
                        }
                    }
        } catch (Throwable ex) {
            throw new AddISpringDepartmentServiceException("${RESOURCE_KEY.ErrorGotExceptionWileAddISpringDepartment}", ex)
        }
    }

    Mono updateISpringDepartment(@NotNull ISpringDepartmentUpdate iSpringDepartmentUpdate) throws UpdateISpringDepartmentServiceException {
        try {
            iSpringWebClientBuilderFactory
                    ?.setHeaders(iSpringWebClientBuilderFactory
                            ?.getObject()
                            ?.post()
                            ?.uri(uriBuilder -> uriBuilder
                                    ?.path("/department/{departmentId}")
                                    ?.build(iSpringDepartmentUpdate.departmentId))
                            ?.bodyValue(iSpringDepartmentUpdate))
                    ?.exchangeToMono clNoContent
        } catch (Throwable ex) {
            throw new UpdateISpringDepartmentServiceException("${RESOURCE_KEY.ErrorGotExceptionWileUpdateISpringDepartment}", ex)
        }
    }

    Mono updateSubordinationISpringDepartment(@NotNull ISpringDepartmentId iSpringDepartmentId, @NotNull ISpringDepartmentSubordinationUpdate iSpringDepartmentSubordinationUpdate) throws ChangeSubordinationISpringDepartmentServiceException {
        try {
            iSpringWebClientBuilderFactory
                    ?.setHeaders(iSpringWebClientBuilderFactory
                            ?.getObject()
                            ?.post()
                            ?.uri(uriBuilder -> uriBuilder
                                    ?.path("/department/{departmentId}/subordination")
                                    ?.build(iSpringDepartmentId.departmentId))
                            ?.bodyValue(iSpringDepartmentSubordinationUpdate))
                    ?.exchangeToMono clNoContent
        } catch (Throwable ex) {
            throw new ChangeSubordinationISpringDepartmentServiceException("${RESOURCE_KEY.ErrorGotExceptionWileChangeSubordinationISpringDepartment}", ex)
        }
    }

    Mono removeISpringDepartment(@NotNull ISpringDepartmentId iSpringDepartmentId) throws RemoveISpringDepartmentServiceException {
        try {
            iSpringWebClientBuilderFactory
                    ?.setHeaders(iSpringWebClientBuilderFactory
                            ?.getObject()
                            ?.delete()
                            ?.uri(uriBuilder -> uriBuilder
                                    ?.path("/department/{departmentId}")
                                    ?.build(iSpringDepartmentId.departmentId)))
                    ?.exchangeToMono clNoContent
        } catch (Throwable ex) {
            throw new RemoveISpringDepartmentServiceException("${RESOURCE_KEY.ErrorGotExceptionWileRemoveISpringDepartment}", ex)
        }
    }
}
