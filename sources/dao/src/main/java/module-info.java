/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/module-info.java to edit this template
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
