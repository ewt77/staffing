package ru.bank.integration.staffing.model.ispring

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

/**
 *
 * @author Author
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
// #/components/schemas/UserProfileFields:3235
class ISpringUserProfileFields implements ISpringDTO {
    String login
    String email
    ISpringUser.Field[] additionalProperties = [] as ISpringUser.Field[]

    void setAdditionalProperties(ISpringUser.Field[] fields) {
        this.additionalProperties = this.additionalProperties + fields
    }
}
