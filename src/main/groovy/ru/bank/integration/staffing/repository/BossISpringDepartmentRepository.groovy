package ru.bank.integration.staffing.repository

import org.springframework.data.repository.CrudRepository
import ru.bank.integration.staffing.model.boss.BossISpringDepartment

/**
 *
 * @author Author
 */
interface BossISpringDepartmentRepository extends CrudRepository<BossISpringDepartment, Long> {
    Iterable<BossISpringDepartment> findByUnloadedIsNull()
}