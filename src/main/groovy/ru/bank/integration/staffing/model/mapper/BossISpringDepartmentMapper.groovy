package ru.bank.integration.staffing.model.mapper

import groovyjarjarantlr4.v4.runtime.misc.NotNull
import ru.bank.integration.staffing.model.boss.BossISpringDepartment
import ru.bank.integration.staffing.model.ispring.*

/**
 *
 * @author Author
 */
class BossISpringDepartmentMapper {

    static ISpringDepartmentId toISpringDepartmentId(@NotNull BossISpringDepartment bossISpringDepartment) {
        ISpringDepartmentId iSpringDepartmentId = new ISpringDepartmentId()
        try {
            iSpringDepartmentId.setDepartmentId(UUID.fromString(bossISpringDepartment.getiSpringId()))
        } catch (IllegalArgumentException ignore) {
        }
        return iSpringDepartmentId
    }

//    static ISpringDepartment toISpringDepartment(@NotNull BossISpringDepartment bossISpringDepartment) {
//        ISpringDepartment iSpringDepartment = new ISpringDepartment()
//        try {
//            iSpringDepartment.setDepartmentId(UUID.fromString(bossISpringDepartment.iSpringId))
//        } catch (IllegalArgumentException ignore) {}
//        iSpringDepartment.setName(bossISpringDepartment.name)
//        if (bossISpringDepartment.iSpringParentDepartmentId != null) {
//            iSpringDepartment.setParentDepartmentId(UUID.fromString(bossISpringDepartment.iSpringParentDepartmentId))
//        }
//        try {
//            iSpringDepartment.setParentDepartmentId(UUID.fromString(bossISpringDepartment.iSpringParentDepartmentId))
//        } catch (IllegalArgumentException ignore) {}
//        iSpringDepartment.setCode(bossISpringDepartment.code)
//        iSpringDepartment
//    }

    static ISpringDepartmentNew toISpringDepartmentNew(@NotNull BossISpringDepartment bossISpringDepartment) {
        ISpringDepartmentNew iSpringDepartmentNew = new ISpringDepartmentNew()
        iSpringDepartmentNew.setName(bossISpringDepartment.name)
        if (bossISpringDepartment.iSpringParentDepartmentId != null) {
            try {
                iSpringDepartmentNew.setParentDepartmentId(UUID.fromString(bossISpringDepartment.iSpringParentDepartmentId))
            } catch (IllegalArgumentException ignore) {}
        }
        iSpringDepartmentNew.setCode(bossISpringDepartment.code)
        iSpringDepartmentNew
    }

    static ISpringDepartmentUpdate toISpringDepartmentUpdate(@NotNull BossISpringDepartment bossISpringDepartment) {
        ISpringDepartmentUpdate iSpringDepartmentUpdate = new ISpringDepartmentUpdate()
        try {
            iSpringDepartmentUpdate.setDepartmentId(UUID.fromString(bossISpringDepartment.iSpringId))
        } catch (IllegalArgumentException ignore) {}
        iSpringDepartmentUpdate.setName(bossISpringDepartment.name)
        if (bossISpringDepartment.iSpringParentDepartmentId != null) {
            try {
                iSpringDepartmentUpdate.setParentDepartmentId(UUID.fromString(bossISpringDepartment.iSpringParentDepartmentId))
            } catch (IllegalArgumentException ignore) {}
        }
        iSpringDepartmentUpdate.setCode(bossISpringDepartment.code)
        iSpringDepartmentUpdate
    }

    static ISpringDepartmentSubordinationUpdate toISpringDepartmentSubordinationUpdate(@NotNull BossISpringDepartment bossISpringDepartment) {
        ISpringSubordination iSpringSubordination = new ISpringSubordination()

        if (bossISpringDepartment.subordinationType != null && !bossISpringDepartment.subordinationType.isEmpty()) {
            iSpringSubordination.setSubordinationType(ISpringSubordination.SubordinationType.valueOf(bossISpringDepartment.subordinationType))
        }

        if (bossISpringDepartment.iSpringManagerId != null && !bossISpringDepartment.iSpringManagerId.isEmpty()) {
            try {
                iSpringSubordination.setSupervisorId(UUID.fromString(bossISpringDepartment.iSpringManagerId))
            } catch (IllegalArgumentException ignore) {}
        }

        ISpringDepartmentSubordinationUpdate iSpringDepartmentSubordinationUpdate = new ISpringDepartmentSubordinationUpdate()
        iSpringDepartmentSubordinationUpdate.setSubordination(iSpringSubordination)
        iSpringDepartmentSubordinationUpdate
    }
}
