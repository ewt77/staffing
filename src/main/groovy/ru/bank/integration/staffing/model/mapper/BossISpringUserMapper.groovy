package ru.bank.integration.staffing.model.mapper

import groovyjarjarantlr4.v4.runtime.misc.NotNull
import ru.bank.integration.staffing.model.boss.BossISpringAutoField
import ru.bank.integration.staffing.model.boss.BossISpringUser
import ru.bank.integration.staffing.model.ispring.*

import javax.persistence.Column
import java.lang.reflect.Field

/**
 *
 * @author Author
 */
class BossISpringUserMapper {

    private static Map<String, String> toISpringUserFieldsMap(@NotNull BossISpringUser bossISpringUser) {
        Map<String, String> fields = new HashMap<>()
        for (Field field : BossISpringUser.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(BossISpringAutoField.class)) {
                if (field.isAnnotationPresent(Column.class)) {
                    def columnAnnotation = field.getAnnotation(Column.class)
                    if (columnAnnotation != null
                            && !columnAnnotation.name().isEmpty()
                            && field.type.isAssignableFrom(String.class)
                            && bossISpringUser."${field.name}" != null
                            && !((String) bossISpringUser."${field.name}").isEmpty()
                    ) {
                        fields.put(
                                columnAnnotation.name(),
                                (String) bossISpringUser."${field.name}"
                        )
                    }
                }
            }
        }
        fields
    }

    private static UUID[] toUUIDArray(String uuids) {
        ArrayList<UUID> result = new ArrayList<UUID>()
        StringTokenizer st = new StringTokenizer(uuids, ',')
        while (st.hasMoreTokens()) {
            try {
                result.add(UUID.fromString(st.nextToken()))
            } catch (IllegalArgumentException ignore) {}
        }
        result.toArray(new UUID[result.size()])
    }

    static ISpringUserId toISpringUserId(@NotNull BossISpringUser bossISpringUser) {
        ISpringUserId iSpringUserId = new ISpringUserId()
        try {
            iSpringUserId.setUserId(UUID.fromString(bossISpringUser.iSpringId))
        } catch (IllegalArgumentException ignore) {}
        iSpringUserId
    }

//    static ISpringUser toISpringUser(@NotNull BossISpringUser bossISpringUser) {
//        ISpringUser iSpringUser = new ISpringUser()
//        try {
//            iSpringUser.setUserId(UUID.fromString(bossISpringUser.iSpringId))
//        } catch (IllegalArgumentException ignore) {}
////        iSpringUserNew.setRole()
////        iSpringUserNew.setRoleId()
//        try {
//            iSpringUser.setDepartmentId(UUID.fromString(bossISpringUser.iSpringDepartmentId))
//        } catch (IllegalArgumentException ignore) {}
//        iSpringUser.setStatus(new ISpringUser.StatusType(bossISpringUser.status))
////        iSpringUser.setAddedDate()
////        iSpringUser.setLastLoginDate()
////        iSpringUser.setManageableDepartmentIds()
////        iSpringUser.setUserRoles()
////        iSpringUser.setGroups()
////        iSpringUser.setSubordination()
////        iSpringUser.setCoSubordination()
//
//        ISpringUser.Field[] fields = new ArrayList<ISpringUser.Field>()
//        toISpringUserFieldsMap(bossISpringUser)
//                .each { fields << new ISpringUser.Field(it?.key, it?.value) }
//        iSpringUser.setFields(fields)
//        iSpringUser
//    }

    static ISpringUserNew toISpringUserNew(@NotNull BossISpringUser bossISpringUser) {
        ISpringUserNew iSpringUserNew = new ISpringUserNew()
        try {
            iSpringUserNew.setDepartmentId(UUID.fromString(bossISpringUser.iSpringDepartmentId))
        } catch (IllegalArgumentException ignore) {}
//        iSpringUserNew.setPassword()
//        iSpringUserNew.setRole()
//        iSpringUserNew.setRoleId()
//        iSpringUserNew.setManageableDepartmentIds()
        if (bossISpringUser.groupIds != null && ! bossISpringUser.groupIds.isEmpty()) {
            iSpringUserNew.setGroupIds(toUUIDArray(bossISpringUser.groupIds))
        }
//        iSpringUserNew.setRoles()
        iSpringUserNew.setSendLoginEmail(bossISpringUser.ifSendLoginEmail?.equalsIgnoreCase("true"))
        iSpringUserNew.setInvitationMessage(bossISpringUser.invitationMessage)
//        iSpringUserNew.setSendLoginSMS()
//        iSpringUserNew.setInvitationSMSMessage()

        Map<String, String> fields = toISpringUserFieldsMap(bossISpringUser)
        fields.put("LOGIN", bossISpringUser.login)
        fields.put("EMAIL", bossISpringUser.email)
        iSpringUserNew.setFields(fields)

        iSpringUserNew
    }

    static ISpringUserUpdate toISpringUserUpdate(@NotNull BossISpringUser bossISpringUser) {
        ISpringUserUpdate iSpringUserUpdate = new ISpringUserUpdate()
        try {
            iSpringUserUpdate.setUserId(UUID.fromString(bossISpringUser.iSpringId))
        } catch (IllegalArgumentException ignore) {}
        try {
            iSpringUserUpdate.setDepartmentId(UUID.fromString(bossISpringUser.iSpringDepartmentId))
        } catch (IllegalArgumentException ignore) {}
//        iSpringUserUpdate.setRole()
//        iSpringUserUpdate.setRoleId()
//        iSpringUserUpdate.setManageableDepartmentIds()
        if (bossISpringUser.groupIds != null && ! bossISpringUser.groupIds.isEmpty()) {
            iSpringUserUpdate.setGroupIds(toUUIDArray(bossISpringUser.groupIds))
        }
//        iSpringUserUpdate.setRoles()
        iSpringUserUpdate.setFields(toISpringUserFieldsMap(bossISpringUser))
        iSpringUserUpdate
    }

    static ISpringUserStatusUpdate toISpringUserStatusUpdate(@NotNull BossISpringUser bossISpringUser) {
        ISpringUserStatusUpdate iSpringUserStatusUpdate = new ISpringUserStatusUpdate()
        iSpringUserStatusUpdate.setStatus(
                bossISpringUser.status != null
                        ? ISpringUser.StatusType.valueOf(bossISpringUser.status.intValue())
                        : null)
        iSpringUserStatusUpdate
    }

    static ISpringGroupList toISpringGroupList(@NotNull BossISpringUser bossISpringUser) {
        ISpringGroupList iSpringGroupList = new ISpringGroupList()
        iSpringGroupList.setGroupIds(toUUIDArray(bossISpringUser.groupIds_remove))
        iSpringGroupList
    }
}
