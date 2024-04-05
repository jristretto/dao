/**
 * A DAO API with a default in-memory implementation.
 */

module io.github.jristretto.dao{
    requires java.logging;
    exports io.github.jristretto.annotations;
    exports io.github.jristretto.mappers;
    exports io.github.jristretto.dao;
    exports io.github.jristretto.inmemorydao;
    provides io.github.jristretto.dao.DAOFactory with io.github.jristretto.inmemorydao.MemoryDAOFactory;
    uses io.github.jristretto.dao.DAOFactory;
}
