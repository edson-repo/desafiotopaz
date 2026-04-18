package br.com.topaz.desafiotopaz.link;

import br.com.topaz.desafiotopaz.link.dto.LinkEncurtadoMapper;
import br.com.topaz.desafiotopaz.link.dto.LinkEncurtadoRequestDTO;
import br.com.topaz.desafiotopaz.link.dto.LinkEncurtadoResponseDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Service responsável pelas regras de negócio do objeto LinkEncurtado.
 */
@ApplicationScoped
public class LinkEncurtadoService {

    //private static final String URL_BASE_ENCURTADA = "http://localhost:8080/desafiotopaz/api/link/r/";
    private static final String URL_BASE_ENCURTADA = "http://localhost:8080/desafiotopaz/r/";
    private static final String CARACTERES_PERMITIDOS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TAMANHO_CODIGO_CURTO = 6;

    @Inject
    private LinkEncurtadoRepository linkEncurtadoRepository;

    @Inject
    private LinkEncurtadoMapper linkEncurtadoMapper;

    /**
     * Cria um novo link encurtado.
     *
     * @param requestDTO dados recebidos na requisição
     * @return dados do link encurtado criado
     */
    @Transactional
    public synchronized LinkEncurtadoResponseDTO criar(LinkEncurtadoRequestDTO requestDTO) {
        validarUrlOriginal(requestDTO.getUrlOriginal());

        LinkEncurtadoEntity entidade = linkEncurtadoMapper.paraEntidade(requestDTO);
        entidade.setDataCriacao(LocalDateTime.now());

        if (aliasFoiInformado(requestDTO.getAlias())) {
            String alias = requestDTO.getAlias().trim();

            if (linkEncurtadoRepository.existePorAlias(alias)) {
                throw new IllegalArgumentException("O alias informado já está em uso.");
            }

            entidade.setAlias(alias);
            entidade.setCodigoCurto(null);
        } else {
            String codigoCurto = gerarCodigoCurtoUnico();
            entidade.setAlias(null);
            entidade.setCodigoCurto(codigoCurto);
        }

        LinkEncurtadoEntity entidadeSalva = linkEncurtadoRepository.salvar(entidade);

        return linkEncurtadoMapper.paraResponseDTO(
                entidadeSalva,
                montarUrlEncurtada(obterIdentificadorPublico(entidadeSalva))
        );
    }

    /**
     * Busca um link encurtado pelo id.
     *
     * @param id identificador interno
     * @return dados do registro encontrado
     */
    public LinkEncurtadoResponseDTO buscarPorId(Long id) {
        LinkEncurtadoEntity entidade = linkEncurtadoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Link não encontrado."));

        return linkEncurtadoMapper.paraResponseDTO(
                entidade,
                montarUrlEncurtada(obterIdentificadorPublico(entidade))
        );
    }

    /**
     * Lista todos os links cadastrados.
     *
     * @return lista de links cadastrados
     */
    public List<LinkEncurtadoResponseDTO> buscarTodos() {
        return linkEncurtadoRepository.buscarTodos()
                .stream()
                .map(entidade -> linkEncurtadoMapper.paraResponseDTO(
                        entidade,
                        montarUrlEncurtada(obterIdentificadorPublico(entidade))
                ))
                .collect(Collectors.toList());
    }

    /**
     * Atualiza um link existente.
     *
     * @param id identificador do link
     * @param requestDTO novos dados
     * @return link atualizado
     */
    @Transactional
    public synchronized LinkEncurtadoResponseDTO atualizar(Long id, LinkEncurtadoRequestDTO requestDTO) {
        validarUrlOriginal(requestDTO.getUrlOriginal());

        LinkEncurtadoEntity entidade = linkEncurtadoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Link não encontrado."));

        String aliasAtual = entidade.getAlias();
        String codigoCurtoAtual = entidade.getCodigoCurto();

        entidade.setUrlOriginal(requestDTO.getUrlOriginal().trim());

        if (aliasFoiInformado(requestDTO.getAlias())) {
            String novoAlias = requestDTO.getAlias().trim();

            if (!novoAlias.equals(aliasAtual) && linkEncurtadoRepository.existePorAlias(novoAlias)) {
                throw new IllegalArgumentException("O alias informado já está em uso.");
            }

            entidade.setAlias(novoAlias);
            entidade.setCodigoCurto(null);
        } else {
            entidade.setAlias(null);

            if (codigoCurtoAtual == null || codigoCurtoAtual.trim().isEmpty()) {
                entidade.setCodigoCurto(gerarCodigoCurtoUnico());
            } else {
                entidade.setCodigoCurto(codigoCurtoAtual);
            }
        }

        LinkEncurtadoEntity entidadeAtualizada = linkEncurtadoRepository.atualizar(entidade);

        return linkEncurtadoMapper.paraResponseDTO(
                entidadeAtualizada,
                montarUrlEncurtada(obterIdentificadorPublico(entidadeAtualizada))
        );
    }

    /**
     * Exclui um link pelo id.
     *
     * @param id identificador do link
     */
    @Transactional
    public void excluirPorId(Long id) {
        linkEncurtadoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Link não encontrado."));

        linkEncurtadoRepository.excluirPorId(id);
    }

    /**
     * Busca a URL original a partir do alias ou código curto.
     *
     * @param identificadorPublico alias ou código curto
     * @return URL original correspondente
     */
    public String buscarUrlOriginal(String identificadorPublico) {
        if (identificadorPublico == null || identificadorPublico.trim().isEmpty()) {
            throw new IllegalArgumentException("O identificador é obrigatório.");
        }

        String identificadorTratado = identificadorPublico.trim();

        Optional<LinkEncurtadoEntity> entidadePorAlias =
                linkEncurtadoRepository.buscarPorAlias(identificadorTratado);

        if (entidadePorAlias.isPresent()) {
            return entidadePorAlias.get().getUrlOriginal();
        }

        Optional<LinkEncurtadoEntity> entidadePorCodigo =
                linkEncurtadoRepository.buscarPorCodigoCurto(identificadorTratado);

        return entidadePorCodigo
                .map(LinkEncurtadoEntity::getUrlOriginal)
                .orElseThrow(() -> new IllegalArgumentException("Link não encontrado."));
    }

    /**
     * Valida a URL original recebida.
     *
     * @param urlOriginal url informada pelo usuário
     */
    private void validarUrlOriginal(String urlOriginal) {
        if (urlOriginal == null || urlOriginal.trim().isEmpty()) {
            throw new IllegalArgumentException("A URL original é obrigatória.");
        }

        String urlTratada = urlOriginal.trim();

        if (!urlTratada.startsWith("http://") && !urlTratada.startsWith("https://")) {
            throw new IllegalArgumentException("A URL original deve começar com http:// ou https://");
        }
    }

    /**
     * Verifica se o alias foi informado.
     *
     * @param alias alias recebido
     * @return true quando estiver preenchido
     */
    private boolean aliasFoiInformado(String alias) {
        return alias != null && !alias.trim().isEmpty();
    }

    /**
     * Gera um código curto único.
     *
     * @return código curto disponível
     */
    private String gerarCodigoCurtoUnico() {
        String codigoCurto;

        do {
            codigoCurto = gerarCodigoCurto();
        } while (linkEncurtadoRepository.existePorCodigoCurto(codigoCurto));

        return codigoCurto;
    }

    /**
     * Gera um código curto aleatório.
     *
     * @return código curto gerado
     */
    private String gerarCodigoCurto() {
        Random random = new Random();
        StringBuilder codigo = new StringBuilder();

        for (int i = 0; i < TAMANHO_CODIGO_CURTO; i++) {
            int indice = random.nextInt(CARACTERES_PERMITIDOS.length());
            codigo.append(CARACTERES_PERMITIDOS.charAt(indice));
        }

        return codigo.toString();
    }

    /**
     * Obtém o identificador público do link.
     *
     * @param entidade registro persistido
     * @return alias ou código curto
     */
    private String obterIdentificadorPublico(LinkEncurtadoEntity entidade) {
        if (entidade.getAlias() != null && !entidade.getAlias().trim().isEmpty()) {
            return entidade.getAlias();
        }

        return entidade.getCodigoCurto();
    }

    /**
     * Monta a URL encurtada que será devolvida para o usuário.
     *
     * @param identificadorPublico alias ou código curto
     * @return URL encurtada final
     */
    private String montarUrlEncurtada(String identificadorPublico) {
        return URL_BASE_ENCURTADA + identificadorPublico;
    }
}