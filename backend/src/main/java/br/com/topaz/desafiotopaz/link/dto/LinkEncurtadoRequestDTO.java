package br.com.topaz.desafiotopaz.link.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO responsável por receber os dados de entrada
 * para geração de um link encurtado.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinkEncurtadoRequestDTO {

    /**
     * URL original que será encurtada.
     */
    private String urlOriginal;

    /**
     * Alias personalizado opcional.
     */
    private String alias;
}