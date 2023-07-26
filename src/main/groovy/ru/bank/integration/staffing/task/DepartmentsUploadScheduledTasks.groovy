package ru.bank.integration.staffing.task

import groovyjarjarantlr4.v4.runtime.misc.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.bank.integration.staffing.model.boss.BossISpringDepartment
import ru.bank.integration.staffing.model.boss.BossISpringEntity
import ru.bank.integration.staffing.model.ispring.*
import ru.bank.integration.staffing.model.mapper.BossISpringDepartmentMapper
import ru.bank.integration.staffing.repository.BossISpringDepartmentRepository
import ru.bank.integration.staffing.service.ISpringDepartmentService

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.logging.Level
import java.util.logging.Logger

import static ru.bank.integration.staffing.IntegrationStaffingApplication.RESOURCE_BUNDLE_NAME

/**
 *
 * @author Author
 */
@Component
class DepartmentsUploadScheduledTasks extends AbstractUploadScheduledTasks {

    /**
     *
     * @author Author
     */
    enum RESOURCE_KEY {
        ErrorInternalWhileSaveErrorTextBossDepartment,
        WarnGotWrongBossISpringEntityAsDepartment,
        InfoBossISpringDepartmentsSynchronizationStarted,
        InfoAllRequiredBossISpringDepartmentsSynchronizationsInitiated,
        DebugThereIsNoUnloadedBossISpringDepartments,
        ErrorInternalWhileFindByUnloadedIsNullBossDepartments,
        DebugDoInsertISpringDepartmentTaskStarted,
        DebugDoInsertISpringDepartmentTaskInitiated,
        DebugGotNotNullISpringDepartmentIdWhileAddISpringDepartment,
        DebugUpdatedBossISpringDepartmentSavedIntoRepository,
        ErrorInternalWhileSetISpringIdBossDepartment,
        WarnGotNullISpringDepartmentIdWhileAddISpringDepartment,
        WarnGotNullValueWhileAddISpringDepartment,
        ErrorGotISpringErrorResponseWhileAddISpringDepartment,
        ErrorGotUnexpectedTypeWhileAddISpringDepartment,
        ErrorWhileAddISpringDepartment,
        ErrorInternalWhileAddISpringDepartment,
        DebugDoUpdateISpringDepartmentTaskStarted,
        DebugGotNotNullResponseWhileUpdateISpringDepartment,
        ErrorGotISpringErrorResponseWhileUpdateISpringDepartment,
        ErrorGotUnexpectedTypeWhileUpdateISpringDepartment,
        WarnGotNullValueWhileUpdateISpringDepartment,
        ErrorWhileUpdateISpringDepartment,
        WarnDepartmentHasNoISpringIdWhileUpdateISpringDepartment,
        ErrorInternalWhileUpdateISpringDepartment,
        DebugDoUpdateISpringDepartmentTaskInitiated,
        DebugDoRemoveISpringDepartmentTaskStarted,
        DebugGotNotNullResponseWhileRemoveISpringDepartment,
        ErrorGotISpringErrorResponseWhileRemoveISpringDepartment,
        ErrorGotUnexpectedTypeWhileRemoveISpringDepartment,
        WarnGotNullValueWhileRemoveISpringDepartment,
        ErrorWhileRemoveISpringDepartment,
        WarnDepartmentHasNoISpringIdWhileRemoveISpringDepartment,
        ErrorInternalWhileRemoveISpringDepartment,
        DebugDoRemoveISpringDepartmentTaskInitiated,
        DebugDoUpdateSubordinationOfISpringDepartmentTaskStarted,
        DebugGotNotNullResponseWhileUpdateSubordinationISpringDepartment,
        ErrorGotISpringErrorResponseWhileUpdateSubordinationISpringDepartment,
        ErrorGotUnexpectedTypeWhileUpdateSubordinationISpringDepartment,
        WarnGotNullValueWhileUpdateSubordinationISpringDepartment,
        ErrorWhileUpdateSubordinationISpringDepartment,
        WarnDepartmentHasNoISpringIdWhileUpdateSubordinationISpringDepartment,
        ErrorInternalWhileUpdateSubordinationISpringDepartment,
        DebugDoUpdateSubordinationOfISpringDepartmentTaskInitiated
    }

    private static final Logger LOGGER = Logger.getLogger(DepartmentsUploadScheduledTasks.class.getName(), RESOURCE_BUNDLE_NAME)
    private static isISpringServiceFree = true

    @Autowired
    private BossISpringDepartmentRepository bossISpringDepartmentRepository
    @Autowired
    private ISpringDepartmentService iSpringDepartmentService

    protected setUnloadedAndSave(@NotNull BossISpringEntity bossISpringEntity) {
        if (bossISpringEntity instanceof BossISpringDepartment) {
            bossISpringEntity.setUnloaded(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
            try {
                bossISpringDepartmentRepository?.save(bossISpringEntity)
            } catch (Throwable error) {
                LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorInternalWhileSaveErrorTextBossDepartment}", error)
            }
        } else {
            LOGGER.warning("${RESOURCE_KEY.WarnGotWrongBossISpringEntityAsDepartment}")
        }
    }

    @Scheduled(fixedRateString = '${staffing.task.DepartmentsUploadScheduledTasks.fixedRateString}')
    void departmentsUpload() {
        LOGGER.info("${RESOURCE_KEY.InfoBossISpringDepartmentsSynchronizationStarted}")
        try {
            Iterable<BossISpringDepartment> unloadedBossISpringDepartments = bossISpringDepartmentRepository?.findByUnloadedIsNull()
            if (unloadedBossISpringDepartments != null && !unloadedBossISpringDepartments.isEmpty()) {
                for (BossISpringDepartment bossISpringDepartment : unloadedBossISpringDepartments) {
                    if (isISpringServiceFree && bossISpringDepartment != null) {
                        isISpringServiceFree = false
                        if (bossISpringDepartment.action?.toUpperCase() == 'S') {
                            DoUpdateSubordinationOfISpringDepartmentTask(bossISpringDepartment)
                        } else {
                            boolean isUpdateSubordinationRequired = bossISpringDepartment.action?.toUpperCase()?.contains('S')
                            if (bossISpringDepartment.action?.toUpperCase()?.contains('I')) {
                                DoInsertISpringDepartmentTask(bossISpringDepartment, isUpdateSubordinationRequired)
                            } else if (bossISpringDepartment.action?.toUpperCase()?.contains('U')) {
                                DoUpdateISpringDepartmentTask(bossISpringDepartment)
                                if (isUpdateSubordinationRequired) {
                                    DoUpdateSubordinationOfISpringDepartmentTask(bossISpringDepartment)
                                }
                            } else if (bossISpringDepartment.action?.toUpperCase()?.contains('D')) {
                                DoRemoveISpringDepartmentTask(bossISpringDepartment)
                            }
                        }
                        isISpringServiceFree = true
                    }
                }
                LOGGER.info("${RESOURCE_KEY.InfoAllRequiredBossISpringDepartmentsSynchronizationsInitiated}")
            } else {
                LOGGER.fine("${RESOURCE_KEY.DebugThereIsNoUnloadedBossISpringDepartments}")
            }
        } catch (Throwable error) {
            LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorInternalWhileFindByUnloadedIsNullBossDepartments}", error)
        }
    }

    private DoInsertISpringDepartmentTask(@NotNull BossISpringDepartment bossISpringDepartment, boolean isUpdateSubordinationRequired) {
        LOGGER.fine("${RESOURCE_KEY.DebugDoInsertISpringDepartmentTaskStarted}")
        try {
            ISpringDepartmentNew iSpringNewDepartment = BossISpringDepartmentMapper.toISpringDepartmentNew(bossISpringDepartment)
            iSpringDepartmentService?.addISpringDepartment(iSpringNewDepartment)?.subscribe(
                    value -> {
                        if (value != null) {
                            switch (value.getClass().name) {
                                case ISpringDepartmentId.class.name:
                                    if (((ISpringDepartmentId) value).departmentId != null) {
                                        LOGGER.fine("${RESOURCE_KEY.DebugGotNotNullISpringDepartmentIdWhileAddISpringDepartment}")
                                        bossISpringDepartment.setiSpringId(((ISpringDepartmentId) value).departmentId.toString())
                                        try {
                                            bossISpringDepartmentRepository?.save(bossISpringDepartment)
                                            LOGGER.fine("${RESOURCE_KEY.DebugUpdatedBossISpringDepartmentSavedIntoRepository}")
                                            if (isUpdateSubordinationRequired) {
                                                DoUpdateSubordinationOfISpringDepartmentTask(bossISpringDepartment)
                                            } else {
                                                setUnloadedAndSave(bossISpringDepartment)
                                            }
                                        } catch (Throwable error) {
                                            LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorInternalWhileSetISpringIdBossDepartment}", error)
                                        }
                                    } else {
                                        LOGGER.warning("${RESOURCE_KEY.WarnGotNullISpringDepartmentIdWhileAddISpringDepartment}")
                                    }
                                    break
                                case ISpringErrorResponse.class.name:
                                    LOGGER.severe("${RESOURCE_KEY.ErrorGotISpringErrorResponseWhileAddISpringDepartment}")
                                    LOGGER.severe(((ISpringErrorResponse) value).message)
                                    saveErrorText(bossISpringDepartment,
                                            "${RESOURCE_KEY.ErrorGotISpringErrorResponseWhileAddISpringDepartment}",
                                            ((ISpringErrorResponse) value).message)
                                    break
                                default:
                                    LOGGER.severe("${RESOURCE_KEY.ErrorGotUnexpectedTypeWhileAddISpringDepartment}")
                                    saveErrorText(bossISpringDepartment,
                                            "${RESOURCE_KEY.ErrorGotUnexpectedTypeWhileAddISpringDepartment}")
                                    break
                            }
                        } else {
                            LOGGER.warning("${RESOURCE_KEY.WarnGotNullValueWhileAddISpringDepartment}")
                        }
                    },
                    error -> {
                        LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorWhileAddISpringDepartment}", error)
                        saveErrorText(bossISpringDepartment,
                                "${RESOURCE_KEY.ErrorWhileAddISpringDepartment}",
                                error)
                    })
        } catch (Throwable error) {
            LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorInternalWhileAddISpringDepartment}", error)
            saveErrorText(bossISpringDepartment,
                    "${RESOURCE_KEY.ErrorInternalWhileAddISpringDepartment}",
                    error)
        }
        LOGGER.fine("${RESOURCE_KEY.DebugDoInsertISpringDepartmentTaskInitiated}")
    }

    private DoUpdateISpringDepartmentTask(@NotNull BossISpringDepartment bossISpringDepartment) {
        LOGGER.fine("${RESOURCE_KEY.DebugDoUpdateISpringDepartmentTaskStarted}")
        try {
            ISpringDepartmentUpdate iSpringDepartmentUpdate = BossISpringDepartmentMapper.toISpringDepartmentUpdate(bossISpringDepartment)
            if (iSpringDepartmentUpdate.departmentId != null) {
                iSpringDepartmentService?.updateISpringDepartment(iSpringDepartmentUpdate)?.subscribe(
                        value -> {
                            if (value != null) {
                                switch (value.getClass().name) {
                                    case String.class.name:
                                        LOGGER.fine("${RESOURCE_KEY.DebugGotNotNullResponseWhileUpdateISpringDepartment}")
                                        setUnloadedAndSave(bossISpringDepartment)
                                        LOGGER.fine("${RESOURCE_KEY.DebugUpdatedBossISpringDepartmentSavedIntoRepository}")
                                        break
                                    case ISpringErrorResponse.class.name:
                                        LOGGER.severe("${RESOURCE_KEY.ErrorGotISpringErrorResponseWhileUpdateISpringDepartment}")
                                        LOGGER.severe(((ISpringErrorResponse) value).message)
                                        saveErrorText(bossISpringDepartment,
                                                "${RESOURCE_KEY.ErrorGotISpringErrorResponseWhileUpdateISpringDepartment}",
                                                ((ISpringErrorResponse) value).message)
                                        break
                                    default:
                                        LOGGER.severe("${RESOURCE_KEY.ErrorGotUnexpectedTypeWhileUpdateISpringDepartment}")
                                        saveErrorText(bossISpringDepartment,
                                                "${RESOURCE_KEY.ErrorGotUnexpectedTypeWhileUpdateISpringDepartment}")
                                        break
                                }
                            } else {
                                LOGGER.warning("${RESOURCE_KEY.WarnGotNullValueWhileUpdateISpringDepartment}")
                            }
                        },
                        error -> {
                            LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorWhileUpdateISpringDepartment}", error)
                            saveErrorText(bossISpringDepartment,
                                    "${RESOURCE_KEY.ErrorWhileUpdateISpringDepartment}",
                                    error)
                        })
            } else {
                LOGGER.warning("${RESOURCE_KEY.WarnDepartmentHasNoISpringIdWhileUpdateISpringDepartment}")
                saveErrorText(bossISpringDepartment,
                        "${RESOURCE_KEY.WarnDepartmentHasNoISpringIdWhileUpdateISpringDepartment}")
            }
        } catch (Throwable error) {
            LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorInternalWhileUpdateISpringDepartment}", error)
            saveErrorText(bossISpringDepartment,
                    "${RESOURCE_KEY.ErrorInternalWhileUpdateISpringDepartment}",
                    error)
        }
        LOGGER.fine("${RESOURCE_KEY.DebugDoUpdateISpringDepartmentTaskInitiated}")
    }

    private DoRemoveISpringDepartmentTask(@NotNull BossISpringDepartment bossISpringDepartment) {
        LOGGER.fine("${RESOURCE_KEY.DebugDoRemoveISpringDepartmentTaskStarted}")
        try {
            ISpringDepartmentId iSpringDepartmentId = BossISpringDepartmentMapper.toISpringDepartmentId(bossISpringDepartment)
            if (iSpringDepartmentId.departmentId != null) {
                iSpringDepartmentService?.removeISpringDepartment(iSpringDepartmentId)?.subscribe(
                        value -> {
                            if (value != null) {
                                switch (value.getClass().name) {
                                    case String.class.name:
                                        LOGGER.fine("${RESOURCE_KEY.DebugGotNotNullResponseWhileRemoveISpringDepartment}")
                                        setUnloadedAndSave(bossISpringDepartment)
                                        LOGGER.fine("${RESOURCE_KEY.DebugUpdatedBossISpringDepartmentSavedIntoRepository}")
                                        break
                                    case ISpringErrorResponse.class.name:
                                        LOGGER.severe("${RESOURCE_KEY.ErrorGotISpringErrorResponseWhileRemoveISpringDepartment}")
                                        LOGGER.severe(((ISpringErrorResponse) value).message)
                                        saveErrorText(bossISpringDepartment,
                                                "${RESOURCE_KEY.ErrorGotISpringErrorResponseWhileRemoveISpringDepartment}",
                                                ((ISpringErrorResponse) value).message)
                                        break
                                    default:
                                        LOGGER.severe("${RESOURCE_KEY.ErrorGotUnexpectedTypeWhileRemoveISpringDepartment}")
                                        saveErrorText(bossISpringDepartment,
                                                "${RESOURCE_KEY.ErrorGotUnexpectedTypeWhileRemoveISpringDepartment}")
                                        break
                                }
                            } else {
                                LOGGER.warning("${RESOURCE_KEY.WarnGotNullValueWhileRemoveISpringDepartment}")
                            }
                        },
                        error -> {
                            LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorWhileRemoveISpringDepartment}", error)
                            saveErrorText(bossISpringDepartment,
                                    "${RESOURCE_KEY.ErrorWhileRemoveISpringDepartment}",
                                    error)
                        })
            } else {
                LOGGER.warning("${RESOURCE_KEY.WarnDepartmentHasNoISpringIdWhileRemoveISpringDepartment}")
                saveErrorText(bossISpringDepartment,
                        "${RESOURCE_KEY.WarnDepartmentHasNoISpringIdWhileRemoveISpringDepartment}")
            }
        } catch (Throwable error) {
            LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorInternalWhileRemoveISpringDepartment}", error)
            saveErrorText(bossISpringDepartment,
                    "${RESOURCE_KEY.ErrorInternalWhileRemoveISpringDepartment}",
                    error)
        }
        LOGGER.fine("${RESOURCE_KEY.DebugDoRemoveISpringDepartmentTaskInitiated}")
    }

    private DoUpdateSubordinationOfISpringDepartmentTask(@NotNull BossISpringDepartment bossISpringDepartment) {
        LOGGER.fine("${RESOURCE_KEY.DebugDoUpdateSubordinationOfISpringDepartmentTaskStarted}")
        try {
            ISpringDepartmentId iSpringDepartmentId = BossISpringDepartmentMapper.toISpringDepartmentId(bossISpringDepartment)
            if (iSpringDepartmentId.departmentId != null) {
                ISpringDepartmentSubordinationUpdate iSpringDepartmentSubordinationUpdate = BossISpringDepartmentMapper.toISpringDepartmentSubordinationUpdate(bossISpringDepartment)
                iSpringDepartmentService?.updateSubordinationISpringDepartment(
                        iSpringDepartmentId,
                        iSpringDepartmentSubordinationUpdate)?.subscribe(
                        value -> {
                            if (value != null) {
                                switch (value.getClass().name) {
                                    case String.class.name:
                                        LOGGER.fine("${RESOURCE_KEY.DebugGotNotNullResponseWhileUpdateSubordinationISpringDepartment}")
                                        setUnloadedAndSave(bossISpringDepartment)
                                        LOGGER.fine("${RESOURCE_KEY.DebugUpdatedBossISpringDepartmentSavedIntoRepository}")
                                        break
                                    case ISpringErrorResponse.class.name:
                                        LOGGER.severe("${RESOURCE_KEY.ErrorGotISpringErrorResponseWhileUpdateSubordinationISpringDepartment}")
                                        LOGGER.severe(((ISpringErrorResponse) value).message)
                                        saveErrorText(bossISpringDepartment,
                                                "${RESOURCE_KEY.ErrorGotISpringErrorResponseWhileUpdateSubordinationISpringDepartment}",
                                                ((ISpringErrorResponse) value).message)
                                        break
                                    default:
                                        LOGGER.severe("${RESOURCE_KEY.ErrorGotUnexpectedTypeWhileUpdateSubordinationISpringDepartment}")
                                        saveErrorText(bossISpringDepartment,
                                                "${RESOURCE_KEY.ErrorGotUnexpectedTypeWhileUpdateSubordinationISpringDepartment}")
                                        break
                                }
                            } else {
                                LOGGER.warning("${RESOURCE_KEY.WarnGotNullValueWhileUpdateSubordinationISpringDepartment}")
                            }
                        },
                        error -> {
                            LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorWhileUpdateSubordinationISpringDepartment}", error)
                            saveErrorText(bossISpringDepartment,
                                    "${RESOURCE_KEY.ErrorWhileUpdateSubordinationISpringDepartment}",
                                    error)
                        })
            } else {
                LOGGER.warning("${RESOURCE_KEY.WarnDepartmentHasNoISpringIdWhileUpdateSubordinationISpringDepartment}")
                saveErrorText(bossISpringDepartment,
                        "${RESOURCE_KEY.WarnDepartmentHasNoISpringIdWhileUpdateSubordinationISpringDepartment}")
            }
        } catch (Throwable error) {
            LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorInternalWhileUpdateSubordinationISpringDepartment}", error)
            saveErrorText(bossISpringDepartment,
                    "${RESOURCE_KEY.ErrorInternalWhileUpdateSubordinationISpringDepartment}",
                    error)
        }
        LOGGER.fine("${RESOURCE_KEY.DebugDoUpdateSubordinationOfISpringDepartmentTaskInitiated}")
    }
}
