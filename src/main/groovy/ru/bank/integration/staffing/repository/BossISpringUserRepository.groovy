package ru.bank.integration.staffing.repository

import org.springframework.data.repository.CrudRepository
import ru.bank.integration.staffing.model.boss.BossISpringUser

/**
 *
 * @author Author
 */
interface BossISpringUserRepository extends CrudRepository<BossISpringUser, Long> {
    List<BossISpringUser> findByUnloadedIsNull()
}