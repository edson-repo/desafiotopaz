package br.com.topaz.desafiotopaz.shared.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe simples para padronizar respostas de erro da API.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErroResponse {

    /**
     * Mensagem de erro devolvida para o cliente.
     */
    private String mensagem;
}