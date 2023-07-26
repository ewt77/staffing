package ru.bank.integration.staffing.exception.ispring

import ru.bank.integration.staffing.exception.StaffingException

/**
 *
 * @author Author
 */
class RemoveISpringUserFromGroupServiceException
        extends StaffingException
        implements ISpringUserServiceException {
    RemoveISpringUserFromGroupServiceException(String message, Throwable cause) { super(message, cause) }
}
