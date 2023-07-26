package ru.bank.integration.staffing.model.ispring

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

/**
 *
 * @author Author
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class ISpringDepartment extends ISpringDepartmentId {
    String name
    UUID parentDepartmentId
    String code
    ISpringSubordination subordination // #/components/schemas/Subordination:4799
//    ISpringSubordination coSubordination // #/components/schemas/Subordination:4799
}
