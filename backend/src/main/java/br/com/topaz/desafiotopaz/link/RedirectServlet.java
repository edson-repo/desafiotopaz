package br.com.topaz.desafiotopaz.link;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet responsável pelo acesso da URL curta pública.
 * Recebe o identificador e redireciona para a URL original.
 */
@WebServlet("/r/*")
public class RedirectServlet extends HttpServlet {

    @Inject
    private LinkEncurtadoService linkEncurtadoService;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.trim().isEmpty() || "/".equals(pathInfo)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Identificador não informado.");
            return;
        }

        String identificador = pathInfo.substring(1);
        String urlOriginal = linkEncurtadoService.buscarUrlOriginal(identificador);

        response.sendRedirect(urlOriginal);
    }
}