package br.com.topaz.desafiotopaz.link.dto;


import br.com.topaz.desafiotopaz.link.LinkEncurtadoEntity;

import javax.enterprise.context.ApplicationScoped;

/**
 * Classe responsável por converter DTOs e entidade do objeto LinkEncurtado.
 */
@ApplicationScoped
public class LinkEncurtadoMapper {

    /**
     * Converte o DTO de entrada em entidade.
     *
     * @param requestDTO dados recebidos na requisição
     * @return entidade inicial
     */
    public LinkEncurtadoEntity paraEntidade(LinkEncurtadoRequestDTO requestDTO) {
        LinkEncurtadoEntity entidade = new LinkEncurtadoEntity();
        entidade.setUrlOriginal(requestDTO.getUrlOriginal());
        entidade.setAlias(requestDTO.getAlias());
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
                entidade.getDataCriacao()
        );
    }
}