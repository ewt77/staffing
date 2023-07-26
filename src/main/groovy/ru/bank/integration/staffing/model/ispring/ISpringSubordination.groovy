package ru.bank.integration.staffing.model.ispring

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

/**
 *
 * @author Author
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ISpringSubordination {

    /**
     *
     * @author Author
     */
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    enum SubordinationType {
        manual,
        inherit,
        no_supervisor
    }

    SubordinationType subordinationType
    UUID supervisorId

    String getSubordinationType() { this.subordinationType.toString() }

    ISpringSubordination() {}

    ISpringSubordination(String subordinationType, UUID supervisorId) {
        this.subordinationType = SubordinationType.valueOf(subordinationType)
        this.supervisorId = supervisorId
    }
}
