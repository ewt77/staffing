package ru.bank.integration.staffing.service

import groovyjarjarantlr4.v4.runtime.misc.NotNull
import reactor.core.publisher.Mono
import ru.bank.integration.staffing.exception.ispring.AddISpringUserServiceException
import ru.bank.integration.staffing.exception.ispring.RemoveISpringUserFromGroupServiceException
import ru.bank.integration.staffing.exception.ispring.RemoveISpringUserServiceException
import ru.bank.integration.staffing.exception.ispring.UpdateISpringUserServiceException
import ru.bank.integration.staffing.exception.ispring.UpdateStatusISpringUserServiceException
import ru.bank.integration.staffing.model.ispring.ISpringGroupList
import ru.bank.integration.staffing.model.ispring.ISpringUserId
import ru.bank.integration.staffing.model.ispring.ISpringUserNew
import ru.bank.integration.staffing.model.ispring.ISpringUserStatusUpdate
import ru.bank.integration.staffing.model.ispring.ISpringUserUpdate

/**
 *
 * @author Author
 */
// #/components/schemas/User:2106
interface ISpringUserService {
    Mono getISpringUsers()

    Mono getISpringUser(@NotNull ISpringUserId iSpringUserId)

    Mono addISpringUser(@NotNull ISpringUserNew iSpringUserNew) throws AddISpringUserServiceException

    Mono updateISpringUser(@NotNull ISpringUserUpdate iSpringUserUpdate) throws UpdateISpringUserServiceException

    Mono updateISpringUserStatus(@NotNull ISpringUserId iSpringUserId, @NotNull ISpringUserStatusUpdate iSpringUserStatusUpdate) throws UpdateStatusISpringUserServiceException

    Mono removeISpringUser(@NotNull ISpringUserId iSpringUserId) throws RemoveISpringUserServiceException

    Mono removeISpringUserFromGroup(@NotNull ISpringUserId iSpringUserId, @NotNull ISpringGroupList iSpringGroupList) throws RemoveISpringUserFromGroupServiceException
}