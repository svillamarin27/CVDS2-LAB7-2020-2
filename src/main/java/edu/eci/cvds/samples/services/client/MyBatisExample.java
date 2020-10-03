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
package edu.eci.cvds.samples.services.client;


import edu.eci.cvds.sampleprj.dao.mybatis.mappers.ClienteMapper;
import edu.eci.cvds.sampleprj.dao.mybatis.mappers.ItemMapper;
import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.samples.entities.TipoItem;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 *
 * @author hcadavid
 */
public class MyBatisExample {

    /**
     * Método que construye una fábrica de sesiones de MyBatis a partir del
     * archivo de configuración ubicado en src/main/resources
     *
     * @return instancia de SQLSessionFactory
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        SqlSessionFactory sqlSessionFactory = null;
        if (sqlSessionFactory == null) {
            InputStream inputStream;
            try {
                inputStream = Resources.getResourceAsStream("mybatis-config.xml");
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e.getCause());
            }
        }
        return sqlSessionFactory;
    }

    /**
     * Programa principal de ejempo de uso de MyBATIS
     * @param args
     * @throws SQLException 
     */
    public static void main(String args[]) throws SQLException {
    	SqlSessionFactory sessionfact = getSqlSessionFactory();
    	SqlSession sqlss = sessionfact.openSession();

        
        //Crear el mapper y usarlo: 
        //ClienteMapper cm=sqlss.getMapper(ClienteMapper.class)
        //cm...
        ClienteMapper cm=sqlss.getMapper(ClienteMapper.class);
        System.out.println("--Consulta de clientes--");
        System.out.println(cm.consultarClientes());
        System.out.println("--Consulta de clientes por ID--");
        System.out.println(cm.consultarCliente(2));
        Date date = null;
        Date date2 = null;
        try {
        	date=new SimpleDateFormat("yyyy-MM-dd").parse("2019-03-12");
            date2=new SimpleDateFormat("yyyy-MM-dd").parse("2019-04-12");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        
        cm.agregarItemRentadoACliente(5, 1,date, date2);
        ItemMapper im= sqlss.getMapper(ItemMapper.class);
        TipoItem tipoIt= new TipoItem(3,"Peliculas");
        Item it = new Item(tipoIt,9999,"Nuevo Item","Nuevo Item",date,1234, "formato","genero final"); 
        im.insertarItem(it);
        
        System.out.println("--Consulta de items--");
        System.out.println(im.consultarItems());
        System.out.println("--Consulta de items por ID--");
        System.out.println(im.consultarItem(9999));
        
        sqlss.commit();
        
        
        sqlss.close();

        
        
    }


}
