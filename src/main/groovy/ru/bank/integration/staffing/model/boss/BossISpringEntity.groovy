package ru.bank.integration.staffing.model.boss

import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
 *
 * @author Author
 */
@MappedSuperclass
abstract class BossISpringEntity {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id
    @Column(name = "ACTION", nullable = false)
    String action
    @Column(name = "UPDATED", nullable = false)
    Date updated
    @Column(name = "UNLOADED")
    Date unloaded
    @Column(name = "ERROR_TEXT")
    String errorText
}
