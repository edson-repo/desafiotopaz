package br.com.topaz.desafiotopaz.link;

import br.com.topaz.desafiotopaz.link.dto.LinkEncurtadoMapper;
import br.com.topaz.desafiotopaz.link.dto.LinkEncurtadoRequestDTO;
import br.com.topaz.desafiotopaz.link.dto.LinkEncurtadoResponseDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class LinkEncurtadoMapperTest {

    private final LinkEncurtadoMapper mapper = new LinkEncurtadoMapper();

    @Test
    void deveConverterRequestParaEntidadeComAlias() {
        LinkEncurtadoRequestDTO requestDTO =
                new LinkEncurtadoRequestDTO("https://www.google.com", "google");

        LinkEncurtadoEntity entidade = mapper.paraEntidade(requestDTO);

        assertEquals("https://www.google.com", entidade.getUrlOriginal());
        assertEquals("google", entidade.getAlias());
    }

    @Test
    void deveConverterRequestParaEntidadeSemAliasQuandoVazio() {
        LinkEncurtadoRequestDTO requestDTO =
                new LinkEncurtadoRequestDTO("https://www.google.com", "   ");

        LinkEncurtadoEntity entidade = mapper.paraEntidade(requestDTO);

        assertEquals("https://www.google.com", entidade.getUrlOriginal());
        assertNull(entidade.getAlias());
    }

    @Test
    void deveConverterEntidadeParaResponseDTO() {
        LinkEncurtadoEntity entidade = new LinkEncurtadoEntity();
        entidade.setId(1L);
        entidade.setUrlOriginal("https://www.google.com");
        entidade.setAlias("google");
        entidade.setCodigoCurto(null);
        entidade.setDataCriacao(LocalDateTime.of(2026, 4, 18, 10, 0));

        LinkEncurtadoResponseDTO responseDTO =
                mapper.paraResponseDTO(entidade, "http://localhost:8080/desafiotopaz/r/google");

        assertEquals(1L, responseDTO.getId());
        assertEquals("https://www.google.com", responseDTO.getUrlOriginal());
        assertEquals("google", responseDTO.getAlias());
        assertEquals("http://localhost:8080/desafiotopaz/r/google", responseDTO.getUrlEncurtada());
        assertEquals("18/04/2026", responseDTO.getDataCriacao());
    }
}