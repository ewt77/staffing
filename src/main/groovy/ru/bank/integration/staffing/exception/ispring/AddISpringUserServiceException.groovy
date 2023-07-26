package ru.bank.integration.staffing.exception.ispring

import ru.bank.integration.staffing.exception.StaffingException

/**
 *
 * @author Author
 */
class AddISpringUserServiceException
        extends StaffingException
        implements ISpringUserServiceException {
    AddISpringUserServiceException(String message, Throwable cause) { super(message, cause) }
}
