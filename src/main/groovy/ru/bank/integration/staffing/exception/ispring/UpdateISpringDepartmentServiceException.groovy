package ru.bank.integration.staffing.exception.ispring

import ru.bank.integration.staffing.exception.StaffingException

/**
 *
 * @author Author
 */
class UpdateISpringDepartmentServiceException
        extends StaffingException
        implements ISpringDepartmentServiceException {
    UpdateISpringDepartmentServiceException(String message, Throwable cause) { super(message, cause) }
}
