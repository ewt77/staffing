package ru.bank.integration.staffing.task

import groovyjarjarantlr4.v4.runtime.misc.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.bank.integration.staffing.model.boss.BossISpringEntity
import ru.bank.integration.staffing.model.boss.BossISpringUser
import ru.bank.integration.staffing.model.ispring.*
import ru.bank.integration.staffing.model.mapper.BossISpringUserMapper
import ru.bank.integration.staffing.repository.BossISpringUserRepository
import ru.bank.integration.staffing.service.ISpringUserService

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
class UsersUploadScheduledTasks extends AbstractUploadScheduledTasks {

    /**
     *
     * @author Author
     */
    enum RESOURCE_KEY {
        ErrorInternalWhileSaveErrorTextBossUser,
        WarnGotWrongBossISpringEntityAsUser,
        InfoBossISpringUsersSynchronizationStarted,
        InfoAllRequiredBossISpringUsersSynchronizationsInitiated,
        DebugThereIsNoUnloadedBossISpringUsers,
        ErrorInternalWhileFindByUnloadedIsNullBossUser,
        DebugDoInsertISpringUserTaskStarted,
        DebugDoInsertISpringUserTaskInitiated,
        DebugGotNotNullISpringUserIdWhileAddISpringUser,
        DebugUpdatedBossISpringUserSavedIntoRepository,
        ErrorInternalWhileSetISpringIdBossUser,
        WarnGotNullISpringUserIdWhileAddISpringUser,
        WarnGotNullValueWhileAddISpringUser,
        ErrorGotISpringErrorResponseWhileAddISpringUser,
        ErrorGotUnexpectedTypeWhileAddISpringUser,
        ErrorWhileAddISpringUser,
        ErrorInternalWhileAddISpringUser,
        DebugDoUpdateISpringUserTaskStarted,
        DebugGotNotNullResponseWhileUpdateISpringUser,
        ErrorGotISpringErrorResponseWhileUpdateISpringUser,
        ErrorGotUnexpectedTypeWhileUpdateISpringUser,
        WarnGotNullValueWhileUpdateISpringUser,
        ErrorWhileUpdateISpringUser,
        WarnUserHasNoISpringIdWhileUpdateISpringUser,
        ErrorInternalWhileUpdateISpringUser,
        DebugDoUpdateISpringUserTaskInitiated,
        DebugDoRemoveISpringUserTaskStarted,
        DebugGotNotNullResponseWhileRemoveISpringUser,
        ErrorGotISpringErrorResponseWhileRemoveISpringUser,
        ErrorGotUnexpectedTypeWhileRemoveISpringUser,
        WarnGotNullValueWhileRemoveISpringUser,
        ErrorWhileRemoveISpringUser,
        WarnUserHasNoISpringIdWhileRemoveISpringUser,
        ErrorInternalWhileRemoveISpringUser,
        DebugDoRemoveISpringUserTaskInitiated,
        DebugDoUpdateStatusOfISpringUserTaskStarted,
        DebugGotNotNullResponseWhileUpdateStatusISpringUser,
        ErrorGotISpringErrorResponseWhileUpdateStatusISpringUser,
        ErrorGotUnexpectedTypeWhileUpdateStatusISpringUser,
        WarnGotNullValueWhileUpdateStatusISpringUser,
        ErrorWhileUpdateStatusISpringUser,
        WarnUserHasNoISpringIdWhileUpdateStatusISpringUser,
        ErrorInternalWhileUpdateStatusISpringUser,
        DebugDoUpdateStatusOfISpringUserTaskInitiated,
        DebugDoRemoveFromGroupOfISpringUserTaskStarted,
        DebugGotNotNullResponseWhileRemoveFromGroupISpringUser,
        ErrorGotISpringErrorResponseWhileRemoveFromGroupISpringUser,
        ErrorGotUnexpectedTypeWhileRemoveFromGroupISpringUser,
        WarnGotNullValueWhileRemoveFromGroupISpringUser,
        ErrorWhileRemoveFromGroupISpringUser,
        WarnUserHasNoISpringIdWhileRemoveFromGroupISpringUser,
        ErrorInternalWhileRemoveFromGroupISpringUser,
        DebugDoRemoveFromGroupOfISpringUserTaskInitiated
    }

    private static final Logger LOGGER = Logger.getLogger(UsersUploadScheduledTasks.class.getName(), RESOURCE_BUNDLE_NAME)
    private static isISpringServiceFree = true

    @Autowired
    private BossISpringUserRepository bossISpringUserRepository
    @Autowired
    private ISpringUserService iSpringUserService

    protected setUnloadedAndSave(@NotNull BossISpringEntity bossISpringEntity) {
        if (bossISpringEntity instanceof BossISpringUser) {
            bossISpringEntity.setUnloaded(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
            try {
                bossISpringUserRepository?.save(bossISpringEntity)
            } catch (Throwable error) {
                LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorInternalWhileSaveErrorTextBossUser}", error)
            }
        } else {
            LOGGER.warning("${RESOURCE_KEY.WarnGotWrongBossISpringEntityAsUser}")
        }
    }

    @Scheduled(fixedRateString = '${staffing.task.UsersUploadScheduledTasks.fixedRateString}')
    void usersUpload() {
        LOGGER.info("${RESOURCE_KEY.InfoBossISpringUsersSynchronizationStarted}")
        try {
            Iterable<BossISpringUser> unloadedBossISpringUsers = bossISpringUserRepository.findByUnloadedIsNull()
            if (!unloadedBossISpringUsers.isEmpty()) {
                for (BossISpringUser bossISpringUser : unloadedBossISpringUsers) {
                    if (isISpringServiceFree && bossISpringUser != null) {
                        isISpringServiceFree = false
                        if (bossISpringUser.action?.toUpperCase() == 'S') {
                            DoUpdateStatusOfISpringUserTask(bossISpringUser)
                        } else {
                            boolean isUpdateStatusRequired = bossISpringUser.action?.toUpperCase()?.contains('S')
                            boolean isRemoveFromGroupRequired = bossISpringUser.action?.toUpperCase()?.contains('R')
                            if (bossISpringUser.action?.toUpperCase()?.contains('I')) {
                                DoInsertISpringUserTask(bossISpringUser, isUpdateStatusRequired)
                            } else if (bossISpringUser.action?.toUpperCase()?.contains('U')) {
                                DoUpdateISpringUserTask(bossISpringUser)
                                if (isUpdateStatusRequired) {
                                    DoUpdateStatusOfISpringUserTask(bossISpringUser)
                                }
                                if (isRemoveFromGroupRequired) {
                                    DoRemoveFromGroupISpringUserTask(bossISpringUser)
                                }
                            } else if (isRemoveFromGroupRequired) {
                                DoRemoveFromGroupISpringUserTask(bossISpringUser)
                            } else if (bossISpringUser.action?.toUpperCase()?.contains('D')) {
                                DoRemoveISpringUserTask(bossISpringUser)
                            }
                        }
                        isISpringServiceFree = true
                    }
                }
                LOGGER.info("${RESOURCE_KEY.InfoAllRequiredBossISpringUsersSynchronizationsInitiated}")
            } else {
                LOGGER.fine("${RESOURCE_KEY.DebugThereIsNoUnloadedBossISpringUsers}")
            }
        } catch (Throwable error) {
            LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorInternalWhileFindByUnloadedIsNullBossUser}", error)
        }
    }

    private DoInsertISpringUserTask(@NotNull BossISpringUser bossISpringUser, boolean isUpdateStatusRequired) {
        LOGGER.fine("${RESOURCE_KEY.DebugDoInsertISpringUserTaskStarted}")
        try {
            ISpringUserNew iSpringUserNew = BossISpringUserMapper.toISpringUserNew(bossISpringUser)
            iSpringUserService?.addISpringUser(iSpringUserNew)?.subscribe(
                    value -> {
                        if (value != null) {
                            switch (value.getClass().name) {
                                case ISpringUserId.class.name:
                                    if (((ISpringUserId) value).userId != null) {
                                        LOGGER.fine("${RESOURCE_KEY.DebugGotNotNullISpringUserIdWhileAddISpringUser}")
                                        bossISpringUser.setiSpringId(((ISpringUserId) value).userId.toString())
                                        try {
                                            bossISpringUserRepository?.save(bossISpringUser)
                                            LOGGER.fine("${RESOURCE_KEY.DebugUpdatedBossISpringUserSavedIntoRepository}")
                                            if (isUpdateStatusRequired) {
                                                DoUpdateStatusOfISpringUserTask(bossISpringUser)
                                            } else {
                                                setUnloadedAndSave(bossISpringUser)
                                            }
                                        } catch (Throwable error) {
                                            LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorInternalWhileSetISpringIdBossUser}", error)
                                        }
                                    } else {
                                        LOGGER.warning("${RESOURCE_KEY.WarnGotNullISpringUserIdWhileAddISpringUser}")
                                    }
                                    break
                                case ISpringErrorResponse.class.name:
                                    LOGGER.severe("${RESOURCE_KEY.ErrorGotISpringErrorResponseWhileAddISpringUser}")
                                    LOGGER.severe(((ISpringErrorResponse) value).message)
                                    saveErrorText(bossISpringUser,
                                            "${RESOURCE_KEY.ErrorGotISpringErrorResponseWhileAddISpringUser}",
                                            ((ISpringErrorResponse) value).message)
                                    break
                                default:
                                    LOGGER.severe("${RESOURCE_KEY.ErrorGotUnexpectedTypeWhileAddISpringUser}")
                                    saveErrorText(bossISpringUser,
                                            "${RESOURCE_KEY.ErrorGotUnexpectedTypeWhileAddISpringUser}")
                                    break
                            }
                        } else {
                            LOGGER.warning("${RESOURCE_KEY.WarnGotNullValueWhileAddISpringUser}")
                        }
                    },
                    error -> {
                        LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorWhileAddISpringUser}", error)
                        saveErrorText(bossISpringUser,
                                "${RESOURCE_KEY.ErrorWhileAddISpringUser}",
                                error)
                    })
        } catch (Throwable error) {
            LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorInternalWhileAddISpringUser}", error)
            saveErrorText(bossISpringUser,
                    "${RESOURCE_KEY.ErrorInternalWhileAddISpringUser}",
                    error)
        }
        LOGGER.fine("${RESOURCE_KEY.DebugDoInsertISpringUserTaskInitiated}")
    }

    private DoUpdateISpringUserTask(@NotNull BossISpringUser bossISpringUser) {
        LOGGER.fine("${RESOURCE_KEY.DebugDoUpdateISpringUserTaskStarted}")
        try {
            ISpringUserUpdate iSpringUserUpdate = BossISpringUserMapper.toISpringUserUpdate(bossISpringUser)
            if (iSpringUserUpdate.userId != null) {
                iSpringUserService?.updateISpringUser(iSpringUserUpdate)?.subscribe(
                        value -> {
                            if (value != null) {
                                switch (value.getClass().name) {
                                    case String.class.name:
                                        LOGGER.fine("${RESOURCE_KEY.DebugGotNotNullResponseWhileUpdateISpringUser}")
                                        setUnloadedAndSave(bossISpringUser)
                                        LOGGER.fine("${RESOURCE_KEY.DebugUpdatedBossISpringUserSavedIntoRepository}")
                                        break
                                    case ISpringErrorResponse.class.name:
                                        LOGGER.severe("${RESOURCE_KEY.ErrorGotISpringErrorResponseWhileUpdateISpringUser}")
                                        LOGGER.severe(((ISpringErrorResponse) value).message)
                                        saveErrorText(bossISpringUser,
                                                "${RESOURCE_KEY.ErrorGotISpringErrorResponseWhileUpdateISpringUser}",
                                                ((ISpringErrorResponse) value).message)
                                        break
                                    default:
                                        LOGGER.severe("${RESOURCE_KEY.ErrorGotUnexpectedTypeWhileUpdateISpringUser}")
                                        saveErrorText(bossISpringUser,
                                                "${RESOURCE_KEY.ErrorGotUnexpectedTypeWhileUpdateISpringUser}")
                                        break
                                }
                            } else {
                                LOGGER.warning("${RESOURCE_KEY.WarnGotNullValueWhileUpdateISpringUser}")
                            }
                        },
                        error -> {
                            LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorWhileUpdateISpringUser}", error)
                            saveErrorText(bossISpringUser,
                                    "${RESOURCE_KEY.ErrorWhileUpdateISpringUser}",
                                    error)
                        })
            } else {
                LOGGER.warning("${RESOURCE_KEY.WarnUserHasNoISpringIdWhileUpdateISpringUser}")
                saveErrorText(bossISpringUser,
                        "${RESOURCE_KEY.WarnUserHasNoISpringIdWhileUpdateISpringUser}")
            }
        } catch (Throwable error) {
            LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorInternalWhileUpdateISpringUser}", error)
            saveErrorText(bossISpringUser,
                    "${RESOURCE_KEY.ErrorInternalWhileUpdateISpringUser}",
                    error)
        }
        LOGGER.fine("${RESOURCE_KEY.DebugDoUpdateISpringUserTaskInitiated}")
    }

    private DoRemoveISpringUserTask(@NotNull BossISpringUser bossISpringUser) {
        LOGGER.fine("${RESOURCE_KEY.DebugDoRemoveISpringUserTaskStarted}")
        try {
            ISpringUserId iSpringUserId = BossISpringUserMapper.toISpringUserId(bossISpringUser)
            if (iSpringUserId.userId != null) {
                iSpringUserService?.removeISpringUser(iSpringUserId)?.subscribe(
                        value -> {
                            if (value != null) {
                                switch (value.getClass().name) {
                                    case String.class.name:
                                        LOGGER.fine("${RESOURCE_KEY.DebugGotNotNullResponseWhileRemoveISpringUser}")
                                        setUnloadedAndSave(bossISpringUser)
                                        LOGGER.fine("${RESOURCE_KEY.DebugUpdatedBossISpringUserSavedIntoRepository}")
                                        break
                                    case ISpringErrorResponse.class.name:
                                        LOGGER.severe("${RESOURCE_KEY.ErrorGotISpringErrorResponseWhileRemoveISpringUser}")
                                        LOGGER.severe(((ISpringErrorResponse) value).message)
                                        saveErrorText(bossISpringUser,
                                                "${RESOURCE_KEY.ErrorGotISpringErrorResponseWhileRemoveISpringUser}",
                                                ((ISpringErrorResponse) value).message)
                                        break
                                    default:
                                        LOGGER.severe("${RESOURCE_KEY.ErrorGotUnexpectedTypeWhileRemoveISpringUser}")
                                        saveErrorText(bossISpringUser,
                                                "${RESOURCE_KEY.ErrorGotUnexpectedTypeWhileRemoveISpringUser}")
                                        break
                                }
                            } else {
                                LOGGER.warning("${RESOURCE_KEY.WarnGotNullValueWhileRemoveISpringUser}")
                            }
                        },
                        error -> {
                            LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorWhileRemoveISpringUser}", error)
                            saveErrorText(bossISpringUser,
                                    "${RESOURCE_KEY.ErrorWhileRemoveISpringUser}",
                                    error)
                        })
            } else {
                LOGGER.warning("${RESOURCE_KEY.WarnUserHasNoISpringIdWhileRemoveISpringUser}")
                saveErrorText(bossISpringUser,
                        "${RESOURCE_KEY.WarnUserHasNoISpringIdWhileRemoveISpringUser}")
            }
        } catch (Throwable error) {
            LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorInternalWhileRemoveISpringUser}", error)
            saveErrorText(bossISpringUser,
                    "${RESOURCE_KEY.ErrorInternalWhileRemoveISpringUser}",
                    error)
        }
        LOGGER.fine("${RESOURCE_KEY.DebugDoRemoveISpringUserTaskInitiated}")
    }

    private DoUpdateStatusOfISpringUserTask(@NotNull BossISpringUser bossISpringUser) {
        LOGGER.fine("${RESOURCE_KEY.DebugDoUpdateStatusOfISpringUserTaskStarted}")
        try {
            ISpringUserId iSpringUserId = BossISpringUserMapper.toISpringUserId(bossISpringUser)
            if (iSpringUserId.userId != null && bossISpringUser.status != null) {
                ISpringUserStatusUpdate iSpringUserStatusUpdate = BossISpringUserMapper.toISpringUserStatusUpdate(bossISpringUser)
                iSpringUserService?.updateISpringUserStatus(iSpringUserId,
                        iSpringUserStatusUpdate)?.subscribe(
                        value -> {
                            if (value != null) {
                                switch (value.getClass().name) {
                                    case String.class.name:
                                        LOGGER.fine("${RESOURCE_KEY.DebugGotNotNullResponseWhileUpdateStatusISpringUser}")
                                        setUnloadedAndSave(bossISpringUser)
                                        LOGGER.fine("${RESOURCE_KEY.DebugUpdatedBossISpringUserSavedIntoRepository}")
                                        break
                                    case ISpringErrorResponse.class.name:
                                        LOGGER.severe("${RESOURCE_KEY.ErrorGotISpringErrorResponseWhileUpdateStatusISpringUser}")
                                        LOGGER.severe(((ISpringErrorResponse) value).message)
                                        saveErrorText(bossISpringUser,
                                                "${RESOURCE_KEY.ErrorGotISpringErrorResponseWhileUpdateStatusISpringUser}",
                                                ((ISpringErrorResponse) value).message)
                                        break
                                    default:
                                        LOGGER.severe("${RESOURCE_KEY.ErrorGotUnexpectedTypeWhileUpdateStatusISpringUser}")
                                        saveErrorText(bossISpringUser,
                                                "${RESOURCE_KEY.ErrorGotUnexpectedTypeWhileUpdateStatusISpringUser}")
                                        break
                                }
                            } else {
                                LOGGER.warning("${RESOURCE_KEY.WarnGotNullValueWhileUpdateStatusISpringUser}")
                            }
                        },
                        error -> {
                            LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorWhileUpdateStatusISpringUser}", error)
                            saveErrorText(bossISpringUser,
                                    "${RESOURCE_KEY.ErrorWhileUpdateStatusISpringUser}",
                                    error)
                        })
            } else {
                LOGGER.warning("${RESOURCE_KEY.WarnUserHasNoISpringIdWhileUpdateStatusISpringUser}")
                saveErrorText(bossISpringUser,
                        "${RESOURCE_KEY.WarnUserHasNoISpringIdWhileUpdateStatusISpringUser}")
            }
        } catch (Throwable error) {
            LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorInternalWhileUpdateStatusISpringUser}", error)
            saveErrorText(bossISpringUser,
                    "${RESOURCE_KEY.ErrorInternalWhileUpdateStatusISpringUser}",
                    error)
        }
        LOGGER.fine("${RESOURCE_KEY.DebugDoUpdateStatusOfISpringUserTaskInitiated}")
    }

    private DoRemoveFromGroupISpringUserTask(@NotNull BossISpringUser bossISpringUser) {
        LOGGER.fine("${RESOURCE_KEY.DebugDoRemoveFromGroupOfISpringUserTaskStarted}")
        try {
            ISpringUserId iSpringUserId = BossISpringUserMapper.toISpringUserId(bossISpringUser)
            if (iSpringUserId.userId != null && bossISpringUser.groupIds_remove != null && ! bossISpringUser.groupIds_remove.isEmpty()) {
                ISpringGroupList iSpringGroupList = BossISpringUserMapper.toISpringGroupList(bossISpringUser)
                iSpringUserService?.removeISpringUserFromGroup(iSpringUserId,
                        iSpringGroupList)?.subscribe(
                        value -> {
                            if (value != null) {
                                switch (value.getClass().name) {
                                    case String.class.name:
                                        LOGGER.fine("${RESOURCE_KEY.DebugGotNotNullResponseWhileRemoveFromGroupISpringUser}")
                                        setUnloadedAndSave(bossISpringUser)
                                        LOGGER.fine("${RESOURCE_KEY.DebugUpdatedBossISpringUserSavedIntoRepository}")
                                        break
                                    case ISpringErrorResponse.class.name:
                                        LOGGER.severe("${RESOURCE_KEY.ErrorGotISpringErrorResponseWhileRemoveFromGroupISpringUser}")
                                        LOGGER.severe(((ISpringErrorResponse) value).message)
                                        saveErrorText(bossISpringUser,
                                                "${RESOURCE_KEY.ErrorGotISpringErrorResponseWhileRemoveFromGroupISpringUser}",
                                                ((ISpringErrorResponse) value).message)
                                        break
                                    default:
                                        LOGGER.severe("${RESOURCE_KEY.ErrorGotUnexpectedTypeWhileRemoveFromGroupISpringUser}")
                                        saveErrorText(bossISpringUser,
                                                "${RESOURCE_KEY.ErrorGotUnexpectedTypeWhileRemoveFromGroupISpringUser}")
                                        break
                                }
                            } else {
                                LOGGER.warning("${RESOURCE_KEY.WarnGotNullValueWhileRemoveFromGroupISpringUser}")
                            }
                        },
                        error -> {
                            LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorWhileRemoveFromGroupISpringUser}", error)
                            saveErrorText(bossISpringUser,
                                    "${RESOURCE_KEY.ErrorWhileRemoveFromGroupISpringUser}",
                                    error)
                        })
            } else {
                LOGGER.warning("${RESOURCE_KEY.WarnUserHasNoISpringIdWhileRemoveFromGroupISpringUser}")
                saveErrorText(bossISpringUser,
                        "${RESOURCE_KEY.WarnUserHasNoISpringIdWhileRemoveFromGroupISpringUser}")
            }
        } catch (Throwable error) {
            LOGGER.log(Level.SEVERE, "${RESOURCE_KEY.ErrorInternalWhileRemoveFromGroupISpringUser}", error)
            saveErrorText(bossISpringUser,
                    "${RESOURCE_KEY.ErrorInternalWhileRemoveFromGroupISpringUser}",
                    error)
        }
        LOGGER.fine("${RESOURCE_KEY.DebugDoRemoveFromGroupOfISpringUserTaskInitiated}")
    }
}
