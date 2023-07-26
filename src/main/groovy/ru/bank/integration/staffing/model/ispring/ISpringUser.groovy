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
// #/components/schemas/User:4887
class ISpringUser extends ISpringUserId {

//    @Accessors(chain = true)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Field {
        String name
        String value

        Field() {}

        Field(String name, String value) {
            this.name = name
            this.value = value
        }
    }

    /**
     *
     * @author Author
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    // #/components/schemas/UserStatus:3524
    enum StatusType {
        active(1),
        deleted(2),
        inactive(3),
        unconfirmed(4)

        private int value
        private static Map map = new HashMap<>()

        static {
            for (StatusType statusType : values()) {
                map.put(statusType.value, statusType)
            }
        }

        static StatusType valueOf(int statusType) {
            return (StatusType) map.get(statusType)
        }

        StatusType(int value) { this.value = value }

        int getValue() { value }
    }

//    String role // #/components/schemas/UserRoleEnum:3507
//    UUID roleId
    UUID departmentId
    StatusType status // #/components/schemas/UserStatus:3524
    Field[] fields = [] as Field[] // #/components/schemas/UserProfileFields:3495 ??? -> []:4942
    Date addedDate
    Date lastLoginDate
    UUID[] manageableDepartmentIds // #/components/schemas/ArrayOfIds:3515
    ISpringUserRole[] userRoles // #/components/schemas/UserRole:5006
    UUID[] groups // #/components/schemas/ArrayOfIds:3515
    ISpringSubordination subordination // #/components/schemas/Subordination:4799
    ISpringSubordination coSubordination // #/components/schemas/Subordination:4799

    void setStatus(String status) {
        try {
            this.status = StatusType.valueOf(Integer.valueOf(status).intValue())
        } catch (IllegalArgumentException ignore) {
        }
    }

    int getStatus() { this.status.value }

    void setFields(Field[] fields) { this.fields = this.fields + fields }
}
