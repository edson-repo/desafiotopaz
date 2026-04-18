package br.com.topaz.desafiotopaz.link.dto;

import br.com.topaz.desafiotopaz.link.LinkEncurtadoEntity;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe responsável por converter DTOs e entidade do objeto LinkEncurtado.
 */
@ApplicationScoped
public class LinkEncurtadoMapper {

    /**
     * Formato simples de data.
     */
    private static final String FORMATO_DATA = "dd/MM/yyyy";

    /**
     * Converte o DTO de entrada em entidade.
     *
     * @param requestDTO dados recebidos na requisição
     * @return entidade inicial
     */
    public LinkEncurtadoEntity paraEntidade(LinkEncurtadoRequestDTO requestDTO) {
        LinkEncurtadoEntity entidade = new LinkEncurtadoEntity();
        entidade.setUrlOriginal(requestDTO.getUrlOriginal());

        // Evita salvar a string "null"
        if (requestDTO.getAlias() != null && !requestDTO.getAlias().trim().isEmpty()) {
            entidade.setAlias(requestDTO.getAlias().trim());
        } else {
            entidade.setAlias(null);
        }

        return entidade;
    }

    /**
     * Converte a entidade em DTO de resposta.
     *
     * @param entidade entidade persistida
     * @param urlEncurtada url curta montada para resposta
     * @return dto de saída
     */
    public LinkEncurtadoResponseDTO paraResponseDTO(LinkEncurtadoEntity entidade, String urlEncurtada) {
        return new LinkEncurtadoResponseDTO(
                entidade.getId(),
                entidade.getUrlOriginal(),
                entidade.getAlias(),
                entidade.getCodigoCurto(),
                urlEncurtada,
                formatarData(entidade.getDataCriacao())
        );
    }

    /**
     * Formata LocalDateTime para string simples.
     *
     * @param data data original
     * @return data formatada
     */
    private String formatarData(LocalDateTime data) {
        if (data == null) {
            return null;
        }

        return data.format(DateTimeFormatter.ofPattern(FORMATO_DATA));
    }
}