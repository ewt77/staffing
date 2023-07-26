package ru.bank.integration.staffing.model.boss

import javax.persistence.*

/**
 *
 * @author Author
 */
@Entity
@Table(name = "ISPRING_DEPARTMENTS",
        indexes = @Index(name = "ISPRING_DEPT_PK", columnList = "ID", unique = true))
class BossISpringDepartment extends BossISpringEntity {
    @Column(name = "PARENT_DEPT_ID")
    Long parentDepartmentId
    @Column(name = "NAME", nullable = false)
    String name
    @Column(name = "BOSS_ID")
    Long managerId
    @Column(name = "DEPARTMENTID")
    String iSpringId
    @Column(name = "PARENTDEPARTMENTID")
    String iSpringParentDepartmentId
    @Column(name = "CODE", nullable = false)
    String code
    @Column(name = "SUBORDINATIONTYPE", nullable = false)
    String subordinationType
    @Column(name = "SUPERVISORID")
    String iSpringManagerId
}
