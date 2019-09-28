package com.demo.control;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.demo.modelo.DBProductos;
import com.demo.entidades.Productos;

public class ListaProductos extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire Textbox textoBusquedaNombre;
	@Wire Listbox listProductos;
	
	private List<Productos> productos;
	
	DBProductos dbProductos = new DBProductos();
	
	public void cargarLista(){
		
		if(productos != null){
			productos = null;
		}

		productos = dbProductos.buscarProductos(textoBusquedaNombre.getValue());
		listProductos.setModel(new ListModelList(productos));
		System.out.println("entroo");
	}
	
	@Listen("onClick=#buttonBuscar")
	public void Buscar(){
		//System.err.println("Aqui");
		cargarLista();
	}
	
	@Listen("onClick=#toolbarButtonNuevo")
	public void Nuevo(){
		Window win = (Window) Executions.createComponents("Registroproducto.zul", null, null);
		win.setClosable(true);
		win.doModal();
	}
	
	@Listen("onClick=#toolbarButtonEditar")
	public void Editar(){
		if(listProductos.getSelectedItem()==null){
			alert("Debe seleccionar un item de la lista");
		}else{
		Window win = (Window) Executions.createComponents("Registroproducto.zul", null, null);
		win.setClosable(true);
		win.doModal();
		win.setAttribute("v_opcion","listaProductos");
		win.setAttribute("control_primario", this);
		Productos prod = (Productos)listProductos.getSelectedItem().getValue();
		win.setAttribute("productos",prod);
		cargarLista();
		}
	}
	
	@Listen("onClick=#toolbarButtonEliminar")
	public void Borrar(){
		Productos prod = (Productos)listProductos.getSelectedItem().getValue();
		if(prod!=null){
			dbProductos = new DBProductos();
			Boolean resultado = dbProductos.Eliminar_Productos(prod.getCodigo());
			if(resultado==true){
				Messagebox.show("Registro eliminado correctamente","Exito", Messagebox.OK , Messagebox.INFORMATION);
				cargarLista();
			}else{
				Messagebox.show("Error al eliminar el registro.","Exito", Messagebox.ABORT , Messagebox.ERROR);
			}
		}else{
			Messagebox.show("Debe seleccionar un registro de la lista..","Exito", Messagebox.OK , Messagebox.EXCLAMATION);
		}
		
	}
		
	
	//getter and setter
	public List<Productos> getProductos() {
		return productos;
	}

	public void setProductos(List<Productos> productos) {
		this.productos = productos;
	}
}