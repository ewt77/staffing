package ru.bank.integration.staffing.exception.ispring

import ru.bank.integration.staffing.exception.StaffingException

/**
 *
 * @author Author
 */
class RemoveISpringUserServiceException
        extends StaffingException
        implements ISpringUserServiceException {
    RemoveISpringUserServiceException(String message, Throwable cause) { super(message, cause) }
}
