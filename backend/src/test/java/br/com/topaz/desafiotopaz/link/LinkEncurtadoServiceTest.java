package br.com.topaz.desafiotopaz.link;

import br.com.topaz.desafiotopaz.link.dto.LinkEncurtadoMapper;
import br.com.topaz.desafiotopaz.link.dto.LinkEncurtadoRequestDTO;
import br.com.topaz.desafiotopaz.link.dto.LinkEncurtadoResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LinkEncurtadoServiceTest {

    @Mock
    private LinkEncurtadoRepository repository;

    @Mock
    private LinkEncurtadoMapper mapper;

    @InjectMocks
    private LinkEncurtadoService service;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);

        injetarCampo(service, "linkEncurtadoRepository", repository);
        injetarCampo(service, "linkEncurtadoMapper", mapper);
    }

    @Test
    void deveCriarLinkComAlias() {
        LinkEncurtadoRequestDTO request =
                new LinkEncurtadoRequestDTO("https://www.google.com", "google");

        LinkEncurtadoEntity entidade = new LinkEncurtadoEntity();
        entidade.setUrlOriginal("https://www.google.com");
        entidade.setAlias("google");

        LinkEncurtadoEntity entidadeSalva = new LinkEncurtadoEntity();
        entidadeSalva.setId(1L);
        entidadeSalva.setUrlOriginal("https://www.google.com");
        entidadeSalva.setAlias("google");
        entidadeSalva.setDataCriacao(LocalDateTime.now());

        LinkEncurtadoResponseDTO response =
                new LinkEncurtadoResponseDTO(1L, "https://www.google.com", "google", null,
                        "http://localhost:8080/desafiotopaz/r/google", "18/04/2026");

        when(mapper.paraEntidade(request)).thenReturn(entidade);
        when(repository.existePorAlias("google")).thenReturn(false);
        when(repository.salvar(entidade)).thenReturn(entidadeSalva);
        when(mapper.paraResponseDTO(eq(entidadeSalva), contains("/r/google"))).thenReturn(response);

        LinkEncurtadoResponseDTO resultado = service.criar(request);

        assertNotNull(resultado);
        assertEquals("google", resultado.getAlias());
        verify(repository).salvar(entidade);
    }

    @Test
    void naoDeveCriarQuandoAliasJaExiste() {
        LinkEncurtadoRequestDTO request =
                new LinkEncurtadoRequestDTO("https://www.google.com", "google");

        LinkEncurtadoEntity entidade = new LinkEncurtadoEntity();

        when(mapper.paraEntidade(request)).thenReturn(entidade);
        when(repository.existePorAlias("google")).thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> service.criar(request));

        assertEquals("O alias informado já está em uso.", exception.getMessage());
        verify(repository, never()).salvar(any());
    }

    @Test
    void naoDeveCriarQuandoUrlForInvalida() {
        LinkEncurtadoRequestDTO request =
                new LinkEncurtadoRequestDTO("google.com", "google");

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> service.criar(request));

        assertEquals("A URL original deve começar com http:// ou https://", exception.getMessage());
    }

    @Test
    void deveBuscarPorId() {
        LinkEncurtadoEntity entidade = new LinkEncurtadoEntity();
        entidade.setId(1L);
        entidade.setUrlOriginal("https://www.google.com");
        entidade.setAlias("google");
        entidade.setDataCriacao(LocalDateTime.now());

        LinkEncurtadoResponseDTO response =
                new LinkEncurtadoResponseDTO(1L, "https://www.google.com", "google", null,
                        "http://localhost:8080/desafiotopaz/r/google", "18/04/2026");

        when(repository.buscarPorId(1L)).thenReturn(Optional.of(entidade));
        when(mapper.paraResponseDTO(eq(entidade), contains("/r/google"))).thenReturn(response);

        LinkEncurtadoResponseDTO resultado = service.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("google", resultado.getAlias());
    }

    @Test
    void deveListarTodos() {
        LinkEncurtadoEntity entidade1 = new LinkEncurtadoEntity();
        entidade1.setId(1L);
        entidade1.setUrlOriginal("https://www.google.com");
        entidade1.setAlias("google");
        entidade1.setDataCriacao(LocalDateTime.now());

        LinkEncurtadoEntity entidade2 = new LinkEncurtadoEntity();
        entidade2.setId(2L);
        entidade2.setUrlOriginal("https://www.youtube.com");
        entidade2.setCodigoCurto("abc123");
        entidade2.setDataCriacao(LocalDateTime.now());

        when(repository.buscarTodos()).thenReturn(Arrays.asList(entidade1, entidade2));
        when(mapper.paraResponseDTO(eq(entidade1), contains("/r/google")))
                .thenReturn(new LinkEncurtadoResponseDTO());
        when(mapper.paraResponseDTO(eq(entidade2), contains("/r/abc123")))
                .thenReturn(new LinkEncurtadoResponseDTO());

        assertEquals(2, service.buscarTodos().size());
    }

    @Test
    void deveAtualizarLinkComNovoAlias() {
        LinkEncurtadoRequestDTO request =
                new LinkEncurtadoRequestDTO("https://www.oracle.com", "oracle");

        LinkEncurtadoEntity entidade = new LinkEncurtadoEntity();
        entidade.setId(1L);
        entidade.setUrlOriginal("https://www.google.com");
        entidade.setAlias("google");
        entidade.setCodigoCurto(null);
        entidade.setDataCriacao(LocalDateTime.now());

        LinkEncurtadoEntity entidadeAtualizada = new LinkEncurtadoEntity();
        entidadeAtualizada.setId(1L);
        entidadeAtualizada.setUrlOriginal("https://www.oracle.com");
        entidadeAtualizada.setAlias("oracle");
        entidadeAtualizada.setCodigoCurto(null);
        entidadeAtualizada.setDataCriacao(LocalDateTime.now());

        LinkEncurtadoResponseDTO response =
                new LinkEncurtadoResponseDTO(1L, "https://www.oracle.com", "oracle", null,
                        "http://localhost:8080/desafiotopaz/r/oracle", "18/04/2026");

        when(repository.buscarPorId(1L)).thenReturn(Optional.of(entidade));
        when(repository.existePorAlias("oracle")).thenReturn(false);
        when(repository.atualizar(entidade)).thenReturn(entidadeAtualizada);
        when(mapper.paraResponseDTO(eq(entidadeAtualizada), contains("/r/oracle"))).thenReturn(response);

        LinkEncurtadoResponseDTO resultado = service.atualizar(1L, request);

        assertEquals("oracle", resultado.getAlias());
        assertEquals("https://www.oracle.com", resultado.getUrlOriginal());
    }

    @Test
    void deveExcluirPorId() {
        LinkEncurtadoEntity entidade = new LinkEncurtadoEntity();
        entidade.setId(1L);

        when(repository.buscarPorId(1L)).thenReturn(Optional.of(entidade));

        service.excluirPorId(1L);

        verify(repository).excluirPorId(1L);
    }

    @Test
    void deveBuscarUrlOriginalPorAlias() {
        LinkEncurtadoEntity entidade = new LinkEncurtadoEntity();
        entidade.setUrlOriginal("https://www.google.com");

        when(repository.buscarPorAlias("google")).thenReturn(Optional.of(entidade));

        String resultado = service.buscarUrlOriginal("google");

        assertEquals("https://www.google.com", resultado);
    }

    @Test
    void deveBuscarUrlOriginalPorCodigoCurto() {
        LinkEncurtadoEntity entidade = new LinkEncurtadoEntity();
        entidade.setUrlOriginal("https://www.youtube.com");

        when(repository.buscarPorAlias("abc123")).thenReturn(Optional.empty());
        when(repository.buscarPorCodigoCurto("abc123")).thenReturn(Optional.of(entidade));

        String resultado = service.buscarUrlOriginal("abc123");

        assertEquals("https://www.youtube.com", resultado);
    }

    private void injetarCampo(Object alvo, String nomeCampo, Object valor) throws Exception {
        Field field = alvo.getClass().getDeclaredField(nomeCampo);
        field.setAccessible(true);
        field.set(alvo, valor);
    }
}