package ru.bank.integration.staffing.model.ispring

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

/**
 *
 * @author Author
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
// #/components/schemas/ChangeDepartmentSubordination:3395
class ISpringDepartmentSubordinationUpdate implements ISpringDTO {
    ISpringSubordination subordination // #/components/schemas/Subordination:4799
//    ISpringSubordination coSubordination // #/components/schemas/Subordination:4799
}
