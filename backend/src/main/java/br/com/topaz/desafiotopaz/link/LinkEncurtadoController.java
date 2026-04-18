package br.com.topaz.desafiotopaz.link;

import br.com.topaz.desafiotopaz.link.dto.LinkEncurtadoRequestDTO;
import br.com.topaz.desafiotopaz.link.dto.LinkEncurtadoResponseDTO;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * Controller responsável por expor os endpoints REST do objeto LinkEncurtado.
 * Recebe a requisição e delega o processamento para o service.
 */
@Path("/link")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LinkEncurtadoController {

    @Inject
    private LinkEncurtadoService linkEncurtadoService;

    /**
     * Cria um novo link encurtado.
     *
     * POST /api/link/save
     *
     * @param requestDTO dados recebidos na requisição
     * @return dados do link criado
     */
    @POST
    @Path("/save")
    public Response salvar(LinkEncurtadoRequestDTO requestDTO) {
        LinkEncurtadoResponseDTO responseDTO = linkEncurtadoService.criar(requestDTO);
        return Response.status(Response.Status.CREATED).entity(responseDTO).build();
    }

    /**
     * Busca um link encurtado pelo id.
     *
     * GET /api/link/findById/{id}
     *
     * @param id identificador interno
     * @return dados do link encontrado
     */
    @GET
    @Path("/findById/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        LinkEncurtadoResponseDTO responseDTO = linkEncurtadoService.buscarPorId(id);
        return Response.ok(responseDTO).build();
    }

    /**
     * Redireciona para a URL original com base no alias ou código curto.
     *
     * GET /api/link/redireciona/{identificador}
     *
     * @param identificador alias ou código curto
     * @return resposta de redirecionamento
     */
    @GET
    @Path("/redireciona/{identificador}")
    public Response redirecionar(@PathParam("identificador") String identificador) {
        String urlOriginal = linkEncurtadoService.buscarUrlOriginal(identificador);
        return Response.seeOther(URI.create(urlOriginal)).build();
    }
}