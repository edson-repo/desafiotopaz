package br.com.topaz.desafiotopaz.link;

import br.com.topaz.desafiotopaz.link.dto.LinkEncurtadoRequestDTO;
import br.com.topaz.desafiotopaz.link.dto.LinkEncurtadoResponseDTO;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

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
     * POST /api/link/salvar
     *
     * @param requestDTO dados recebidos na requisição
     * @return dados do link criado
     */
    @POST
    @Path("/salvar")
    public Response salvar(LinkEncurtadoRequestDTO requestDTO) {
        LinkEncurtadoResponseDTO responseDTO =
                linkEncurtadoService.criar(requestDTO);

        return Response.status(Response.Status.CREATED)
                .entity(responseDTO)
                .build();
    }

    /**
     * Busca um link pelo id.
     *
     * GET /api/link/buscarPorId/{id}
     *
     * @param id identificador interno
     * @return dados encontrados
     */
    @GET
    @Path("/buscarPorId/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        LinkEncurtadoResponseDTO responseDTO =
                linkEncurtadoService.buscarPorId(id);

        return Response.ok(responseDTO).build();
    }

    /**
     * Lista todos os links cadastrados.
     *
     * GET /api/link/buscarTodos
     *
     * @return lista de links
     */
    @GET
    @Path("/buscarTodos")
    public Response buscarTodos() {
        List<LinkEncurtadoResponseDTO> responseDTO =
                linkEncurtadoService.buscarTodos();

        return Response.ok(responseDTO).build();
    }

    /**
     * Atualiza um link existente.
     *
     * PUT /api/link/atualizar/{id}
     *
     * @param id identificador do link
     * @param requestDTO novos dados
     * @return link atualizado
     */
    @PUT
    @Path("/atualizar/{id}")
    public Response atualizar(@PathParam("id") Long id,
                              LinkEncurtadoRequestDTO requestDTO) {

        LinkEncurtadoResponseDTO responseDTO =
                linkEncurtadoService.atualizar(id, requestDTO);

        return Response.ok(responseDTO).build();
    }

    /**
     * Exclui um link pelo id.
     *
     * DELETE /api/link/excluir/{id}
     *
     * @param id identificador do link
     * @return resposta sem conteúdo
     */
    @DELETE
    @Path("/excluir/{id}")
    public Response excluir(@PathParam("id") Long id) {
        linkEncurtadoService.excluirPorId(id);

        return Response.noContent().build();
    }

    /**
     * Redireciona para URL original.
     *
     * GET /api/link/redirecionar/{identificador}
     *
     * @param identificador alias ou código curto
     * @return redirecionamento
     */
    @GET
    @Path("/redirecionar/{identificador}")
    public Response redirecionar(
            @PathParam("identificador") String identificador) {

        String urlOriginal =
                linkEncurtadoService.buscarUrlOriginal(identificador);

        return Response.seeOther(URI.create(urlOriginal)).build();
    }
}