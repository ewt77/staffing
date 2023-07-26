package ru.bank.integration.staffing.rest.ispring

import groovyjarjarantlr4.v4.runtime.misc.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import reactor.core.publisher.Mono
import ru.bank.integration.staffing.exception.ispring.AddISpringUserServiceException
import ru.bank.integration.staffing.exception.ispring.RemoveISpringUserFromGroupServiceException
import ru.bank.integration.staffing.exception.ispring.RemoveISpringUserServiceException
import ru.bank.integration.staffing.exception.ispring.UpdateISpringUserServiceException
import ru.bank.integration.staffing.exception.ispring.UpdateStatusISpringUserServiceException
import ru.bank.integration.staffing.model.ispring.*
import ru.bank.integration.staffing.service.ISpringUserService

/**
 *
 * @author Author
 */
@Configuration
class ISpringUserServiceImpl extends ISpringServiceImpl implements ISpringUserService {

    /**
     *
     * @author Author
     */
    enum RESOURCE_KEY {
        ErrorGotExceptionWileAddISpringUser,
        ErrorGotExceptionWileChangeStatusISpringUser,
        ErrorGotExceptionWileUpdateISpringUser,
        ErrorGotExceptionWileRemoveISpringUser,
        ErrorGotExceptionWileRemoveISpringUserFromGroup
    }

    @Autowired
    private ISpringWebClientBuilderFactory iSpringWebClientBuilderFactory

    Mono getISpringUsers() {
        iSpringWebClientBuilderFactory
                ?.setHeaders(iSpringWebClientBuilderFactory
                        ?.getObject()
                        ?.get()
                        ?.uri("/user"))
                ?.exchangeToMono { response ->
                    {
                        if (response?.statusCode() == HttpStatus.OK) {
                            // users: { type: array, items: { $ref: '#/components/schemas/User:4887' } }
                            return response?.bodyToMono(ISpringUser[].class)
                        } else return doErrorsHandle(response)
                    }
                }
    }

    Mono getISpringUser(@NotNull ISpringUserId iSpringUserId) {
        iSpringWebClientBuilderFactory
                ?.setHeaders(iSpringWebClientBuilderFactory
                        ?.getObject()
                        ?.get()
                        ?.uri(uriBuilder -> uriBuilder
                                ?.path("/user/{userId}")
                                ?.build(iSpringUserId.userId)))
                ?.exchangeToMono { response -> {
                        if (response?.statusCode() == HttpStatus.OK) {
                            // #/components/schemas/User:4887
                            return response?.bodyToMono(ISpringUser.class)
                        } else return doErrorsHandle(response)
                    }
                }
    }

    Mono addISpringUser(@NotNull ISpringUserNew iSpringUserNew) throws AddISpringUserServiceException {
        try {
            // #/components/schemas/NewUser:3838
            iSpringWebClientBuilderFactory
                    ?.setHeaders(iSpringWebClientBuilderFactory
                            ?.getObject()
                            ?.post()
                            ?.uri("/user")
                            ?.bodyValue(iSpringUserNew))
                    ?.header("X-Return-Object", "true") // #/components/parameters/returnObject:5116
                    ?.exchangeToMono { response ->
                        {
                            if (response?.statusCode() == HttpStatus.CREATED) {
                                return response?.bodyToMono(ISpringUserId.class) // :761
                            } else return doErrorsHandle(response)
                        }
                    }
        } catch (Throwable ex) {
            throw new AddISpringUserServiceException("${RESOURCE_KEY.ErrorGotExceptionWileAddISpringUser}", ex)
        }
    }

    Mono updateISpringUser(@NotNull ISpringUserUpdate iSpringUserUpdate) throws UpdateISpringUserServiceException {
        try {
            iSpringWebClientBuilderFactory
                    ?.setHeaders(iSpringWebClientBuilderFactory
                            ?.getObject()
                            ?.post()
                            ?.uri(uriBuilder -> uriBuilder
                                    ?.path("/user/{userId}")
                                    ?.build(iSpringUserUpdate.userId))
                    ?.bodyValue(iSpringUserUpdate))
                    ?.exchangeToMono clOk
        } catch (Throwable ex) {
            throw new UpdateISpringUserServiceException("${RESOURCE_KEY.ErrorGotExceptionWileUpdateISpringUser}", ex)
        }
    }

    Mono updateISpringUserStatus(@NotNull ISpringUserId iSpringUserId, @NotNull ISpringUserStatusUpdate iSpringUserStatusUpdate) throws UpdateStatusISpringUserServiceException {
        try {
            iSpringWebClientBuilderFactory
                    ?.setHeaders(iSpringWebClientBuilderFactory
                            ?.getObject()
                            ?.post()
                            ?.uri(uriBuilder -> uriBuilder
                                    ?.path("/user/{userId}/status")
                                    ?.build(iSpringUserId.userId))
                    ?.bodyValue(iSpringUserStatusUpdate))
                    ?.exchangeToMono clOk
        } catch (Throwable ex) {
            throw new UpdateStatusISpringUserServiceException("${RESOURCE_KEY.ErrorGotExceptionWileChangeStatusISpringUser}", ex)
        }
    }

    Mono removeISpringUser(@NotNull ISpringUserId iSpringUserId) throws RemoveISpringUserServiceException {
        try {
            iSpringWebClientBuilderFactory
                    ?.setHeaders(iSpringWebClientBuilderFactory
                            ?.getObject()
                            ?.delete()
                            ?.uri(uriBuilder -> uriBuilder
                                    ?.path("/user/{userId}")
                                    ?.build(iSpringUserId.userId)))
                    ?.exchangeToMono clNoContent
        } catch (Throwable ex) {
            throw new RemoveISpringUserServiceException("${RESOURCE_KEY.ErrorGotExceptionWileRemoveISpringUser}", ex)
        }
    }

    Mono removeISpringUserFromGroup(@NotNull ISpringUserId iSpringUserId, @NotNull ISpringGroupList iSpringGroupList) throws RemoveISpringUserFromGroupServiceException {
        try {
            iSpringWebClientBuilderFactory
                    ?.setHeaders(iSpringWebClientBuilderFactory
                            ?.getObject()
                            ?.post()
                            ?.uri(uriBuilder -> uriBuilder
                                    ?.path("/user/{userId}/groups/remove")
                                    ?.build(iSpringUserId.userId))
                            ?.bodyValue(iSpringGroupList))
                    ?.exchangeToMono clOk
        } catch (Throwable ex) {
            throw new RemoveISpringUserFromGroupServiceException("${RESOURCE_KEY.ErrorGotExceptionWileRemoveISpringUserFromGroup}", ex)
        }
    }
}
