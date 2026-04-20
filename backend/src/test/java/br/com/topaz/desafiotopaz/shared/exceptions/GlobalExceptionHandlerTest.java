package br.com.topaz.desafiotopaz.shared.exceptions;

import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void deveRetornarBadRequestParaIllegalArgumentException() {
        Response response = handler.toResponse(new IllegalArgumentException("Erro de validação"));

        ErroResponse erro = (ErroResponse) response.getEntity();

        assertEquals(400, response.getStatus());
        assertEquals("Erro de validação", erro.getMensagem());
    }

    @Test
    void deveRetornarErroInternoParaExceptionGenerica() {
        Response response = handler.toResponse(new RuntimeException("Falha"));

        ErroResponse erro = (ErroResponse) response.getEntity();

        assertEquals(500, response.getStatus());
        assertEquals("Erro interno no servidor.", erro.getMensagem());
    }
}