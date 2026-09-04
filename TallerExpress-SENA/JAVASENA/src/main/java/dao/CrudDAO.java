/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;


import java.sql.SQLException;
import java.util.List;

// Interfaz que usare mas adelante, aqui hago uso de polimorfismo y a la vez es algo eficiente por el bajo acoplamiento que se genera
public interface CrudDAO<T, ID> {
    void crear(T entidad) throws SQLException;
    T obtenerPorId(ID id) throws SQLException;
    List<T> obtenerTodos() throws SQLException;
}

