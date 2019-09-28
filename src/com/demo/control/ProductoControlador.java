package com.demo.control;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.demo.entidades.Productos;
import com.demo.modelo.DBProductos;
public class ProductoControlador extends GenericForwardComposer<Component>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	
	private Window win_RegistroProducto, winListaProductos;
	private Productos producto = null;
	private Textbox txt_Codigo, txt_Nombre, txt_Descripcion;
	private Intbox txt_Precio;
	private Toolbarbutton toolbarButtonGrabar;
	private int v_accion;
	
	public void doAfteComposer(Component comp) throws Exception{
		super.doAfterCompose(comp);
	}

	private void Limpiar(){
		txt_Codigo.setText("");
		txt_Nombre.setText("");
		txt_Descripcion.setText("");
		txt_Precio.setText("");
	}
	
	//se ejecuta el crear ventana
	public void onCreate$win_RegistroProducto(){
		producto=(Productos) win_RegistroProducto.getAttribute("productos");
		if(producto!=null){
			v_accion=2;
			txt_Codigo.setText(String.valueOf(producto.getCodigo()));
			txt_Nombre.setText(producto.getNombre());
			txt_Descripcion.setText(producto.getDescripcion());
			txt_Precio.setText(String.valueOf(producto.getPrecio()));
		}else{
			v_accion=1;
			Limpiar();
		}
		
	}
	
	
	public void onClick$toolbarButtonGrabar(){
		DBProductos db_productos = new DBProductos();
		Boolean resultado= false;
		if(v_accion==1){
			resultado = db_productos.Registrar_Productos(txt_Nombre.getText(),
														 txt_Descripcion.getText(), 
														 Double.valueOf(txt_Precio.getText()));
		}else if(v_accion==2){
			resultado = db_productos.Editar_Productos(Integer.parseInt(txt_Codigo.getText()),
													txt_Nombre.getText(),
													txt_Descripcion.getText(), 
													Double.valueOf(txt_Precio.getText()));
		}
		
		if(resultado==true){
			Messagebox.show("Datos almacenado correctamente","Exito", Messagebox.OK , Messagebox.INFORMATION);
			
			String v_opcion=(String)win_RegistroProducto.getAttribute("v_opcion");
			if(v_opcion!=null && v_opcion.equals("listaProductos")){
				ListaProductos lista = (ListaProductos) win_RegistroProducto.getAttribute("control_primario");
				if(lista!=null){
					lista.cargarLista();
					win_RegistroProducto.detach();
				}
			}
		}else{
			Messagebox.show("Error al almacenar los datos","Error", Messagebox.ABORT , Messagebox.ERROR);
		}
	}
	
}
