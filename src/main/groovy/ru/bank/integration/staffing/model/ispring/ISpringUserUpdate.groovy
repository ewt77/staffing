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
class ISpringUserUpdate extends ISpringUserId {
    UUID departmentId
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
    public Map<String, String> fields // #/components/schemas/UserProfileFields:3495
//    String role // #/components/schemas/UserRoleEnum:3507
//    UUID roleId
//    UUID[] manageableDepartmentIds // #/components/schemas/ArrayOfIds:3515
    UUID[] groupIds // #/components/schemas/ArrayOfIds:3515
//    ISpringUserRole[] roles // #/components/schemas/UserRole:5006

    void setFields(Map<String, String> fields) { this.fields = fields }
}
