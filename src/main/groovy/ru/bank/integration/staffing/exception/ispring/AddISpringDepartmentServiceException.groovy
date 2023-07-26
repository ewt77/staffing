package ru.bank.integration.staffing.exception.ispring

import ru.bank.integration.staffing.exception.StaffingException

/**
 *
 * @author Author
 */
class AddISpringDepartmentServiceException
        extends StaffingException
        implements ISpringDepartmentServiceException {
    AddISpringDepartmentServiceException(String message, Throwable cause) { super(message, cause) }
}
