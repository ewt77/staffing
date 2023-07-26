package ru.bank.integration.staffing.rest.ispring

import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ClientResponse
import reactor.core.publisher.Mono
import ru.bank.integration.staffing.model.ispring.ISpringErrorResponse

/**
 *
 * @author Author
 */
abstract class ISpringServiceImpl {

    static Mono doErrorsHandle( ClientResponse response) {
        if (response?.statusCode() == HttpStatus.BAD_REQUEST) {
            // #/components/responses/BadRequest:5052 = #/components/schemas/ErrorResponse:4015
            return response?.bodyToMono(ISpringErrorResponse.class)
        } else if (response?.statusCode() == HttpStatus.UNAUTHORIZED) {
            // #/components/responses/Unauthorized:5088 = #/components/schemas/ErrorResponse:4015
            return response?.bodyToMono(ISpringErrorResponse.class)
        } else if (response?.statusCode() == HttpStatus.FORBIDDEN) {
            // #/components/responses/PermissionDenied:5070 = #/components/schemas/ErrorResponse:4015
            return response?.bodyToMono(ISpringErrorResponse.class)
        }
        return response?.createException()?.flatMap(Mono::error)
    }

    protected clNoContent = { ClientResponse response ->
        {
            if (response?.statusCode() == HttpStatus.NO_CONTENT) {
                return response
                        ?.bodyToMono(String.class)
                        ?.defaultIfEmpty(response?.statusCode()?.getReasonPhrase())
            } else return doErrorsHandle(response)
        }
    }

    protected clOk = { ClientResponse response ->
        {
            if (response?.statusCode() == HttpStatus.OK) {
                return response
                        ?.bodyToMono(String.class)
                        ?.defaultIfEmpty(response?.statusCode()?.getReasonPhrase())
            } else return doErrorsHandle(response)
        }
    }
}
