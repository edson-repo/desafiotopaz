package br.com.topaz.desafiotopaz.shared.config;

import br.com.topaz.desafiotopaz.link.LinkEncurtadoController;
import br.com.topaz.desafiotopaz.shared.exceptions.GlobalExceptionHandler;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe responsável por ativar e configurar a aplicação REST.
 */
@ApplicationPath("/api")
public class AplicacaoRestConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(LinkEncurtadoController.class);
        classes.add(GlobalExceptionHandler.class);
        return classes;
    }
}