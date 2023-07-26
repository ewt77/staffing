package ru.bank.integration.staffing.model.boss

import javax.persistence.*

/**
 *
 * @author Author
 */
@Entity
@Table(name = "ISPRING_USERS",
        indexes = @Index(name = "ISPRING_USER_PK", columnList = "ID", unique = true))
class BossISpringUser extends BossISpringEntity {
    @Column(name = "DEPT_ID", nullable = false)
    Long departmentId
    @Column(name = "USERID")
    String iSpringId
    @Column(name = "EMAIL", nullable = false)
    String email
    @Column(name = "LOGIN", nullable = false)
    String login
    @Column(name = "DEPARTMENTID", nullable = false)
    String iSpringDepartmentId
    @Column(name = "GROUPIDS")
    String groupIds
    @Column(name = "GROUPIDS_REMOVE")
    String groupIds_remove

    @Column(name = "FIRST_NAME", nullable = false)
    @BossISpringAutoField
    String firstName
    @Column(name = "LAST_NAME", nullable = false)
    @BossISpringAutoField
    String lastName
//    @Column(name = "MIDDLE_NAME")
//    @BossISpringAutoField
//    String middleName
    @Column(name = "ABOUT_ME")
    @BossISpringAutoField
    String aboutMe
    @Column(name = "JOB_TITLE")
    @BossISpringAutoField
    String jobTitle
    @Column(name = "TABNUM")
    @BossISpringAutoField
    String tabNum
    @Column(name = "PHONE")
    @BossISpringAutoField
    String phone
    @Column(name = "BIRTHDATE")
    @BossISpringAutoField
    String birthdate
    @Column(name = "WORKPHONE")
    @BossISpringAutoField
    String workPhone
    @Column(name = "ADDRESS")
    @BossISpringAutoField
    String address
    @Column(name = "ASSISTANT")
    @BossISpringAutoField
    String assistant
    @Column(name = "DATE_IN")
    @BossISpringAutoField
    String dateIn
    @Column(name = "DATE_OUT")
    @BossISpringAutoField
    String dateOut

    @Column(name = "SENDLOGINEMAIL", nullable = false)
    String ifSendLoginEmail
    @Column(name = "INVITATIONMESSAGE")
    String invitationMessage
    @Column(name = "STATUS")
    Integer status
}
