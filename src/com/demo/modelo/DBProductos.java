package com.demo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.zkoss.zk.ui.util.Clients;

import com.demo.control.ListaProductos;
import com.demo.entidades.Productos;


public class DBProductos {	
	/*
	 * 
	 */
	private ListaProductos listaProductos;

	public ArrayList<Productos> buscarProductos(String criterio){			
		ArrayList<Productos> lista= null;
		//conectar a la bd
		DBManager dbmanager = new DBManager();
		Connection con = dbmanager.getConection();
		if(con==null){return lista;}

		Statement sentencia;
		ResultSet resultados= null;

		String query="";
		if(criterio.equals("") ){
			query = "SELECT * FROM productos";
		}
		else{
			query = "SELECT * FROM productos where nombre like '%"+criterio+"%' ";

			System.out.println(query);
		}

		try {
			sentencia= con.createStatement();
			resultados= sentencia.executeQuery(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error en ejecucion de sentencia" + e.getMessage());
		}

		Productos prod = null;				
		lista= new ArrayList<Productos>();

		//recorrer los resultados
		try {
			while (resultados.next()){
				prod = new Productos();

				prod.setCodigo(resultados.getInt("codigo"));
				prod.setNombre(resultados.getString("nombre"));
				prod.setDescripcion(resultados.getString("descripcion"));
				prod.setPrecio(resultados.getDouble("precio"));
				prod.setEstado(resultados.getString("eliminado"));

				lista.add(prod);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error en recorrer los resultados");
		}
		try {
			con.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error al cerrar la conexion");
		}

		return lista;	
	}
	
	
	public boolean Registrar_Productos(String v_nom, String v_descrp, Double v_precio){
		DBManager dbmanager = new DBManager();
		Connection con = dbmanager.getConection();
		Boolean resp=false;
		String query="";
				
		try {
			con.setAutoCommit(false);
			query= "INSERT INTO productos(nombre,descripcion,precio,eliminado)"+
					"VALUES(?,?,?,?)";
			PreparedStatement sentencia = con.prepareStatement(query);
			sentencia.setString(1, v_nom);
			sentencia.setString(2, v_descrp);
			sentencia.setDouble(3, v_precio);
			sentencia.setInt(4, 0);
			sentencia.executeUpdate();
			con.commit();
			resp=true;
		} catch (Exception e) {
			try {
				con.rollback();
				System.out.println("Error al insertar un producto nuevo");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error al cerrar conexion");
			}
		}
		
		
		return resp;
	}
	
	
	public boolean Editar_Productos(int v_cod, String v_nom, String v_descrp, Double v_precio){
		DBManager dbmanager = new DBManager();
		Connection con = dbmanager.getConection();
		Boolean resp=false;
		String query="";
				
		try {
			con.setAutoCommit(false);
			query= "UPDATE productos SET nombre=?,descripcion=?, precio=? WHERE codigo=?";
			PreparedStatement sentencia = con.prepareStatement(query);
			sentencia.setString(1, v_nom);
			sentencia.setString(2, v_descrp);
			sentencia.setDouble(3, v_precio);
			sentencia.setInt(4, v_cod);
				
			sentencia.executeUpdate();
			con.commit();
			resp=true;
		} catch (Exception e) {
			try {
				con.rollback();
				System.out.println("Error al editar un producto nuevo");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error al cerrar conexion");
			}
		}
		
		
		return resp;
	}
	
	
	public boolean Eliminar_Productos(int v_cod){
		DBManager dbmanager = new DBManager();
		Connection con = dbmanager.getConection();
		Boolean resp=false;
		String query="";
				
		try {
			con.setAutoCommit(false);
			query= "UPDATE productos SET eliminado=? WHERE codigo=?";
			PreparedStatement sentencia = con.prepareStatement(query);
			sentencia.setInt(1, 1);
			sentencia.setInt(2, v_cod);
				
			sentencia.executeUpdate();
			con.commit();
			resp=true;
		} catch (Exception e) {
			try {
				con.rollback();
				System.out.println("Error al eliminar un producto.");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error al cerrar conexion");
			}
		}
		
		
		return resp;
	}
	
}