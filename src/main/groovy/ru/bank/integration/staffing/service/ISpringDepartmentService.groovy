package ru.bank.integration.staffing.service

import groovyjarjarantlr4.v4.runtime.misc.NotNull
import reactor.core.publisher.Mono
import ru.bank.integration.staffing.exception.ispring.AddISpringDepartmentServiceException
import ru.bank.integration.staffing.exception.ispring.ChangeSubordinationISpringDepartmentServiceException
import ru.bank.integration.staffing.exception.ispring.RemoveISpringDepartmentServiceException
import ru.bank.integration.staffing.exception.ispring.UpdateISpringDepartmentServiceException
import ru.bank.integration.staffing.model.ispring.ISpringDepartmentId
import ru.bank.integration.staffing.model.ispring.ISpringDepartmentNew
import ru.bank.integration.staffing.model.ispring.ISpringDepartmentSubordinationUpdate
import ru.bank.integration.staffing.model.ispring.ISpringDepartmentUpdate

/**
 *
 * @author Author
 */
interface ISpringDepartmentService {
    Mono getISpringDepartments()

    Mono getISpringDepartment(@NotNull ISpringDepartmentId iSpringDepartmentId)

    Mono addISpringDepartment(@NotNull ISpringDepartmentNew iSpringNewDepartment) throws AddISpringDepartmentServiceException

    Mono updateISpringDepartment(@NotNull ISpringDepartmentUpdate iSpringDepartmentUpdate) throws UpdateISpringDepartmentServiceException

    Mono updateSubordinationISpringDepartment(@NotNull ISpringDepartmentId iSpringDepartmentId,
                                              @NotNull ISpringDepartmentSubordinationUpdate iSpringDepartmentSubordinationUpdate) throws ChangeSubordinationISpringDepartmentServiceException

    Mono removeISpringDepartment(@NotNull ISpringDepartmentId iSpringDepartmentId) throws RemoveISpringDepartmentServiceException
}
