package edu.eci.cvds.sampleprj.jdbc.example;
/*
 * Copyright (C) 2015 hcadavid
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class JDBCExample {
    
	private static final String SQL_INSERT_REGISTRAR_NUEVO_PRODUCTO= "INSERT INTO ORD_PRODUCTOS(codigo, nombre, precio) VALUES (?,?,?)";
	private static final String SQL_SELECT_NOMBRES_PRODUCTOS_PEDIDOS= "SELECT nombre FROM ORD_PRODUCTOS WHERE codigo = ?";
	private static final String SQL_SELECT_VALOR_TOTAL_PEDIDO= "SELECT SUM(cantidad*ORD_PRODUCTOS.precio) FROM ORD_DETALLE_PEDIDO,ORD_PRODUCTOS WHERE producto_fk = ORD_PRODUCTOS.codigo && pedido_fk = ?;";
	
    public static void main(String args[]){
        try {
            String url="jdbc:mysql://desarrollo.is.escuelaing.edu.co:3306/bdprueba";
            String driver="com.mysql.jdbc.Driver";
            String user="bdprueba";
            String pwd="prueba2019";
                        
            Class.forName(driver);
            Connection con=DriverManager.getConnection(url,user,pwd);
            con.setAutoCommit(false);
                 
            
            System.out.println("Valor total pedido 1:"+valorTotalPedido(con, 1));
            
            List<String> prodsPedido=nombresProductosPedido(con, 1);
            
            
            System.out.println("Productos del pedido 1:");
            System.out.println("-----------------------");
            for (String nomprod:prodsPedido){
                System.out.println(nomprod);
            }
            System.out.println("-----------------------");
            
            
            int suCodigoECI=20134423;
            registrarNuevoProducto(con, suCodigoECI, "SU NOMBRE", 99999999);            
            con.commit();
                        
            
            con.close();
                                   
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(JDBCExample.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    /**
     * Agregar un nuevo producto con los parÃ¡metros dados
     * @param con la conexiÃ³n JDBC
     * @param codigo
     * @param nombre
     * @param precio
     * @throws SQLException 
     */
    public static void registrarNuevoProducto(Connection con, int codigo, String nombre,int precio) throws SQLException{
        //Crear preparedStatement
        //Asignar parÃ¡metros
        //usar 'execute'
    	PreparedStatement state=null;
    	state=con.prepareStatement(SQL_INSERT_REGISTRAR_NUEVO_PRODUCTO);
    	//Contador de columnas.
    	int index=1;
    	state.setInt(index++, codigo);
    	state.setString(index++, nombre);
    	state.setInt(index++, precio);
    	System.out.println("Ejecutando linea:"+state);
        con.commit();
        
    }
    
    /**
     * Consultar los nombres de los productos asociados a un pedido
     * @param con la conexiÃ³n JDBC
     * @param codigoPedido el cÃ³digo del pedido
     * @return 
     */
    public static List<String> nombresProductosPedido(Connection con, int codigoPedido){
        List<String> np=new LinkedList<>();
        
        //Crear prepared statement
        //asignar parÃ¡metros
        //usar executeQuery
        //Sacar resultados del ResultSet
        //Llenar la lista y retornarla
        PreparedStatement state = null;
        ResultSet result = null;
        String nombre = null;
        
        try {
        	state = con.prepareStatement(SQL_SELECT_NOMBRES_PRODUCTOS_PEDIDOS);
        	state.setInt(1, codigoPedido);
        	result = state.executeQuery();
        	while(result.next()) {
        		nombre=result.getString(1);
        		np.add(nombre);
        	}
        }
        catch(SQLException e) {
        	e.printStackTrace();
        }
        
        
        return np;
    }

    
    /**
     * Calcular el costo total de un pedido
     * @param con
     * @param codigoPedido cÃ³digo del pedido cuyo total se calcularÃ¡
     * @return el costo total del pedido (suma de: cantidades*precios)
     */
    public static int valorTotalPedido(Connection con, int codigoPedido){
        
        //Crear prepared statement
        //asignar parÃ¡metros
        //usar executeQuery
        //Sacar resultado del ResultSet
    	PreparedStatement state = null;
        ResultSet result = null;
        int ValorTotal=0;
        try {
        	state=con.prepareStatement(SQL_SELECT_VALOR_TOTAL_PEDIDO);
        	state.setInt(1, codigoPedido);
        	result=state.executeQuery();
        	while(result.next()) ValorTotal+=result.getInt(1);
        }
        catch(SQLException e) {
        	e.printStackTrace();
        }
        return ValorTotal;
    }
    

    
    
    
}