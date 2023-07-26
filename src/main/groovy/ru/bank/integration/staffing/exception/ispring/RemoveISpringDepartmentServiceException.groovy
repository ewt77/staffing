package ru.bank.integration.staffing.exception.ispring

import ru.bank.integration.staffing.exception.StaffingException

/**
 *
 * @author Author
 */
class RemoveISpringDepartmentServiceException
        extends StaffingException
        implements ISpringDepartmentServiceException {
    RemoveISpringDepartmentServiceException(String message, Throwable cause) { super(message, cause) }
}
