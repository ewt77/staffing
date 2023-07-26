package ru.bank.integration.staffing.exception.ispring

import ru.bank.integration.staffing.exception.StaffingException

/**
 *
 * @author Author
 */
class UpdateStatusISpringUserServiceException
        extends StaffingException
        implements ISpringUserServiceException {
    UpdateStatusISpringUserServiceException(String message, Throwable cause) { super(message, cause) }
}
