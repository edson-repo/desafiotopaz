package br.com.topaz.desafiotopaz.shared.exceptions;

import br.com.topaz.desafiotopaz.shared.exceptions.ErroResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Classe responsável por tratar exceções da aplicação
 * e devolver respostas JSON simples para o cliente.
 */
@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    /**
     * Trata exceções lançadas pela aplicação.
     *
     * @param exception exceção capturada
     * @return resposta HTTP padronizada
     */
    @Override
    public Response toResponse(Exception exception) {

        if (exception instanceof IllegalArgumentException) {
            ErroResponse erroResponse = new ErroResponse(exception.getMessage());

            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(erroResponse)
                    .build();
        }

        ErroResponse erroResponse = new ErroResponse("Erro interno no servidor.");

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(erroResponse)
                .build();
    }
}