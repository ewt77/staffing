package ru.bank.integration.staffing.model.ispring

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

/**
 *
 * @author Author
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
// #/components/schemas/UpdateUser:3873
class ISpringUserStatusUpdate implements ISpringDTO {
    ISpringUser.StatusType status // #/components/schemas/UserStatus:3524

    int getStatus() { this.status.value }
}
