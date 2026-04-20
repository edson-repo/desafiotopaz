package br.com.topaz.desafiotopaz.link;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RedirectServletTest {

    @Mock
    private LinkEncurtadoService service;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private RedirectServlet servlet;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        servlet = new RedirectServlet();

        Field field = RedirectServlet.class.getDeclaredField("linkEncurtadoService");
        field.setAccessible(true);
        field.set(servlet, service);
    }

    @Test
    void deveRedirecionarQuandoIdentificadorForValido() throws Exception {
        when(request.getPathInfo()).thenReturn("/google");
        when(service.buscarUrlOriginal("google")).thenReturn("https://www.google.com");

        servlet.doGet(request, response);

        verify(response).sendRedirect("https://www.google.com");
    }

    @Test
    void deveRetornarErroQuandoIdentificadorNaoForInformado() throws Exception {
        when(request.getPathInfo()).thenReturn("/");

        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Identificador não informado.");
    }
}