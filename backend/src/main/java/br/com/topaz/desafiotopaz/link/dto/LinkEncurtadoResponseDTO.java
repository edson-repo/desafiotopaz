package br.com.topaz.desafiotopaz.link.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO responsável por devolver os dados do link encurtado.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinkEncurtadoResponseDTO {

    /**
     * Identificador interno do registro.
     */
    private Long id;

    /**
     * URL original cadastrada.
     */
    private String urlOriginal;

    /**
     * Alias personalizado, quando informado.
     */
    private String alias;

    /**
     * Código curto gerado automaticamente, quando existir.
     */
    private String codigoCurto;

    /**
     * URL encurtada final exibida para o usuário.
     */
    private String urlEncurtada;

    /**
     * Data e hora de criação do registro.
     */
    private LocalDateTime dataCriacao;
}