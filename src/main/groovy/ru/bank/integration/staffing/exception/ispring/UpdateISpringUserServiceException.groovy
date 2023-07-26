package ru.bank.integration.staffing.exception.ispring

import ru.bank.integration.staffing.exception.StaffingException

/**
 *
 * @author Author
 */
class UpdateISpringUserServiceException
        extends StaffingException
        implements ISpringUserServiceException {
    UpdateISpringUserServiceException(String message, Throwable cause) { super(message, cause) }
}
