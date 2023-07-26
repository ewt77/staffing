package ru.bank.integration.staffing.model.ispring

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

/**
 *
 * @author Author
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ISpringUserId implements ISpringDTO {
    @JsonAlias("id")
    UUID userId
}
