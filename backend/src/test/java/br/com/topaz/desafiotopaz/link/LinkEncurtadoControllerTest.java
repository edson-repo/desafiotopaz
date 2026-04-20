package br.com.topaz.desafiotopaz.link;

import br.com.topaz.desafiotopaz.link.dto.LinkEncurtadoRequestDTO;
import br.com.topaz.desafiotopaz.link.dto.LinkEncurtadoResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;
import java.lang.reflect.Field;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class LinkEncurtadoControllerTest {

    @Mock
    private LinkEncurtadoService service;

    private LinkEncurtadoController controller;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        controller = new LinkEncurtadoController();

        Field field = LinkEncurtadoController.class.getDeclaredField("linkEncurtadoService");
        field.setAccessible(true);
        field.set(controller, service);
    }

    @Test
    void deveSalvar() {
        LinkEncurtadoRequestDTO request =
                new LinkEncurtadoRequestDTO("https://www.google.com", "google");

        LinkEncurtadoResponseDTO responseDTO = new LinkEncurtadoResponseDTO();
        when(service.criar(request)).thenReturn(responseDTO);

        Response response = controller.salvar(request);

        assertEquals(201, response.getStatus());
    }

    @Test
    void deveBuscarPorId() {
        LinkEncurtadoResponseDTO responseDTO = new LinkEncurtadoResponseDTO();
        when(service.buscarPorId(1L)).thenReturn(responseDTO);

        Response response = controller.buscarPorId(1L);

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveBuscarTodos() {
        when(service.buscarTodos()).thenReturn(Collections.singletonList(new LinkEncurtadoResponseDTO()));

        Response response = controller.buscarTodos();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveAtualizar() {
        LinkEncurtadoRequestDTO request =
                new LinkEncurtadoRequestDTO("https://www.oracle.com", "oracle");

        when(service.atualizar(1L, request)).thenReturn(new LinkEncurtadoResponseDTO());

        Response response = controller.atualizar(1L, request);

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveExcluir() {
        Response response = controller.excluir(1L);

        assertEquals(204, response.getStatus());
    }
}