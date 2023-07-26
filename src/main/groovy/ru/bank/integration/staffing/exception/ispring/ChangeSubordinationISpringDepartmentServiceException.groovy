package ru.bank.integration.staffing.exception.ispring

import ru.bank.integration.staffing.exception.StaffingException

/**
 *
 * @author Author
 */
class ChangeSubordinationISpringDepartmentServiceException
        extends StaffingException
        implements ISpringDepartmentServiceException {
    ChangeSubordinationISpringDepartmentServiceException(String message, Throwable cause) { super(message, cause) }
}
