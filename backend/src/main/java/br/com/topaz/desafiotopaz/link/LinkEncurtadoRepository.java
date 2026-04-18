package br.com.topaz.desafiotopaz.link;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * Repository responsável pelo acesso aos dados do objeto LinkEncurtado.
 * Centraliza as operações de persistência e consulta do banco.
 */
@ApplicationScoped
public class LinkEncurtadoRepository {

    /**
     * Gerenciador de entidades utilizado nas operações com o banco.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Salva um novo link encurtado.
     *
     * @param entidade entidade a ser persistida
     * @return entidade salva
     */
    public LinkEncurtadoEntity salvar(LinkEncurtadoEntity entidade) {
        entityManager.persist(entidade);
        return entidade;
    }

    /**
     * Atualiza um link existente.
     *
     * @param entidade entidade com dados atualizados
     * @return entidade atualizada
     */
    public LinkEncurtadoEntity atualizar(LinkEncurtadoEntity entidade) {
        return entityManager.merge(entidade);
    }

    /**
     * Remove um link pelo id.
     *
     * @param id identificador do registro
     */
    public void excluirPorId(Long id) {
        LinkEncurtadoEntity entidade = entityManager.find(LinkEncurtadoEntity.class, id);

        if (entidade != null) {
            entityManager.remove(entidade);
        }
    }

    /**
     * Busca um link encurtado pelo id.
     *
     * @param id identificador do registro
     * @return entidade encontrada, se existir
     */
    public Optional<LinkEncurtadoEntity> buscarPorId(Long id) {
        return Optional.ofNullable(entityManager.find(LinkEncurtadoEntity.class, id));
    }

    /**
     * Lista todos os links cadastrados.
     *
     * @return lista de links
     */
    public List<LinkEncurtadoEntity> buscarTodos() {
        return entityManager.createQuery(
                        "SELECT l FROM LinkEncurtadoEntity l ORDER BY l.id DESC",
                        LinkEncurtadoEntity.class
                )
                .getResultList();
    }

    /**
     * Busca um link encurtado pelo alias informado.
     *
     * @param alias alias personalizado
     * @return entidade encontrada, se existir
     */
    public Optional<LinkEncurtadoEntity> buscarPorAlias(String alias) {
        List<LinkEncurtadoEntity> resultados = entityManager.createQuery(
                        "SELECT l FROM LinkEncurtadoEntity l WHERE l.alias = :alias",
                        LinkEncurtadoEntity.class
                )
                .setParameter("alias", alias)
                .getResultList();

        if (resultados.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(resultados.get(0));
    }

    /**
     * Busca um link encurtado pelo código curto informado.
     *
     * @param codigoCurto código curto gerado pelo sistema
     * @return entidade encontrada, se existir
     */
    public Optional<LinkEncurtadoEntity> buscarPorCodigoCurto(String codigoCurto) {
        List<LinkEncurtadoEntity> resultados = entityManager.createQuery(
                        "SELECT l FROM LinkEncurtadoEntity l WHERE l.codigoCurto = :codigoCurto",
                        LinkEncurtadoEntity.class
                )
                .setParameter("codigoCurto", codigoCurto)
                .getResultList();

        if (resultados.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(resultados.get(0));
    }

    /**
     * Verifica se já existe um alias cadastrado.
     *
     * @param alias alias a validar
     * @return true quando já existir
     */
    public boolean existePorAlias(String alias) {
        Long quantidade = entityManager.createQuery(
                        "SELECT COUNT(l) FROM LinkEncurtadoEntity l WHERE l.alias = :alias",
                        Long.class
                )
                .setParameter("alias", alias)
                .getSingleResult();

        return quantidade != null && quantidade > 0;
    }

    /**
     * Verifica se já existe um código curto cadastrado.
     *
     * @param codigoCurto código curto a validar
     * @return true quando já existir
     */
    public boolean existePorCodigoCurto(String codigoCurto) {
        Long quantidade = entityManager.createQuery(
                        "SELECT COUNT(l) FROM LinkEncurtadoEntity l WHERE l.codigoCurto = :codigoCurto",
                        Long.class
                )
                .setParameter("codigoCurto", codigoCurto)
                .getSingleResult();

        return quantidade != null && quantidade > 0;
    }
}