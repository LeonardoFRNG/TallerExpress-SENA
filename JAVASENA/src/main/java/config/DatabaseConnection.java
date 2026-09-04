package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    //Primero definimos las constantes de conexion las cuales son la URL, USER, PASSWORD.
    //"jdbc:mysql://" es el protocolo
    //"localhost:3306 es nuestro servidor local y el puerto por defecto de MySQL"
    //"taller_express_db" es el nombre exacto de la base de datos que se creo.
    private static final String URL = "jdbc:mysql://localhost:3306/taller_express_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Qwe.123*"; 
    
    //aca el constructor lo ponemos private para que alguien no haga: new DatabaseConnection()
    private DatabaseConnection() {}
    
    
    //este es el metodo estatico que agrega la conexion
    public static  Connection getConnection() throws SQLException {
        //driver manager lee la url, busca el driver de mysql que ya eventualmente puse en el pom.xml y abre el tunel de onexion a la base de datos.
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

