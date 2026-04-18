package br.com.topaz.desafiotopaz.link;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidade responsável por representar um link encurtado.
 * Guarda a URL original, o alias ou código curto e a data de criação.
 */
@Entity
@Table(name = "link_encurtado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinkEncurtadoEntity {

    /**
     * Identificador interno do registro.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * URL original informada pelo usuário.
     */
    @Column(name = "url_original", nullable = false, length = 1000)
    private String urlOriginal;

    /**
     * Alias personalizado informado pelo usuário.
     * Pode ficar nulo quando o sistema gerar um código automático.
     */
    @Column(name = "alias", unique = true, length = 100)
    private String alias;

    /**
     * Código curto gerado automaticamente pelo sistema.
     * Pode ficar nulo quando o usuário informar um alias.
     */
    @Column(name = "codigo_curto", unique = true, length = 20)
    private String codigoCurto;

    /**
     * Data e hora de criação do link.
     */
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
}