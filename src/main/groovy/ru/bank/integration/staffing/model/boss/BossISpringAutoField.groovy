package ru.bank.integration.staffing.model.boss

import java.lang.annotation.Retention
import java.lang.annotation.Target

import static java.lang.annotation.ElementType.FIELD
import static java.lang.annotation.RetentionPolicy.RUNTIME

/**
 *
 * @author Author
 */
@Target(FIELD)
@Retention(RUNTIME)
@interface BossISpringAutoField {}