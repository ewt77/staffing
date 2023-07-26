package ru.bank.integration.staffing.task

import groovyjarjarantlr4.v4.runtime.misc.NotNull
import ru.bank.integration.staffing.model.boss.BossISpringEntity

import static ru.bank.integration.staffing.IntegrationStaffingApplication.RESOURCE_BUNDLE_NEW

/**
 *
 * @author Author
 */
abstract class AbstractUploadScheduledTasks {

    abstract protected setUnloadedAndSave(@NotNull BossISpringEntity bossISpringEntity)

    protected saveErrorText(@NotNull BossISpringEntity bossISpringEntity, @NotNull String messageResourceKey) {
        saveErrorText(bossISpringEntity, messageResourceKey, null, null)
    }

    protected saveErrorText(@NotNull BossISpringEntity bossISpringEntity, @NotNull String messageResourceKey, @NotNull String iSpringMessage) {
        saveErrorText(bossISpringEntity, messageResourceKey, iSpringMessage, null)
    }

    protected saveErrorText(@NotNull BossISpringEntity bossISpringEntity, @NotNull String messageResourceKey, @NotNull Throwable error) {
        saveErrorText(bossISpringEntity, messageResourceKey, null, error)
    }

    protected saveErrorText(@NotNull BossISpringEntity bossISpringEntity, @NotNull String messageResourceKey, @NotNull String iSpringMessage, @NotNull Throwable error) {
        String oldText = ""
        if (bossISpringEntity.getErrorText() != null && !bossISpringEntity.getErrorText().empty) {
            oldText = bossISpringEntity.errorText
        }

        StringJoiner joinErrorText = new StringJoiner("\n")
        joinErrorText.add(RESOURCE_BUNDLE_NEW.getMessage("${messageResourceKey}", null, Locale.getDefault()))
        if (iSpringMessage != null) {
            joinErrorText.add(iSpringMessage)
        }
        if (error != null) {
            joinErrorText.add("${error}")
        }
        if (!oldText.isEmpty()) {
            joinErrorText.add(oldText)
        }

        bossISpringEntity.setErrorText(joinErrorText.toString())
        setUnloadedAndSave(bossISpringEntity)
    }
}
