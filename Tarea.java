package entidades;

import java.util.ArrayList;
import java.time.*;

public class Tarea {
	private Empleado empleadoAsignado;
	private ArrayList<Empleado>historicoEmpleadoConDemoras;
	private String titulo;
	private String descripcion;
	private double diasNecesarios;
	private double diasDeRetrazo;
	private String fechaFinalizacion;
	private String fechaAsignacionEmpleado;
	private double costoTarea;
	private boolean estaFinalizada; // nuevo
	
	public Tarea()
	{
		this.titulo = "";
		this.descripcion = "";
		this.diasNecesarios = 0.0;
		this.fechaFinalizacion = "";//duda
		this.diasDeRetrazo = 0.0;
		this.estaFinalizada = false;
	}
	
	public Tarea(String titulo, String descripcion, double diasNecesarios)
	{
		if(titulo == "" || //descripcion == "" || 
				diasNecesarios <= 0)
		{
			throw new IllegalArgumentException("No pueden existir campo vacios");
		}
		else
		{
			this.titulo = titulo;
			this.descripcion = descripcion;
			this.diasNecesarios = diasNecesarios;
			this.fechaFinalizacion = ""; //duda
			this.diasDeRetrazo = 0.0;
			this.estaFinalizada = false;
	
		}
	}
	
	public void asignarEmpleado(Empleado empleado)throws IllegalArgumentException
	{
		if(this.empleadoAsignado == null) {
			
			this.empleadoAsignado = empleado;
			this.empleadoAsignado.modificaDisponible();
			aniadirCostoTarea();
		}
		else
		{
			throw new IllegalArgumentException("No se puede asignar un empleado");
		}
	}
	
	public void cambiarEmpleado(Empleado empleadoNuevo)
	{

		if (this.empleadoAsignado == null)
		{
            throw new IllegalArgumentException("La tarea no tiene empleado asignado: " + this.empleadoAsignado);
		}
		if(!this.estaFinalizada)
		{  
			if(this.diasDeRetrazo > 0)
			{
				this.empleadoAsignado.incremetarRetrazos();
				this.empleadoAsignado.modificaDisponible();
				this.historicoEmpleadoConDemoras.add(empleadoAsignado);
				this.empleadoAsignado = empleadoNuevo;
				this.empleadoAsignado.modificaDisponible();
				aniadirCostoTarea();
			}
			else
			{
				this.empleadoAsignado.modificaDisponible();
				this.empleadoAsignado = empleadoNuevo;
				this.empleadoAsignado.modificaDisponible();
				aniadirCostoTarea();
			}

		}
		else
		{   
			throw new IllegalArgumentException("No se puede asignar, la tarea esta Finalizada!!!");
		}
	}
	
	public void actualizarDiasRetrazo(double cantidadDias)
	{
		this.diasDeRetrazo = (this.diasNecesarios + cantidadDias) - this.diasNecesarios;
	}
	
	
	public void finalizarTarea()
	{
		this.estaFinalizada = true;
		this.empleadoAsignado.modificaDisponible();
	}

	public String consultarEstado()
	{
		return this.estaFinalizada?"Finalizada":"Activa";
	}
	
	public boolean retornarEstado()
	{
		return this.estaFinalizada;
	}
	
	public double consultarDiasDeRetrazo()
	{
		return this.diasDeRetrazo;
	}
	
	
	public void consultarHistoricoEmpleadosConDemora() //verificar si es String o void
	{
		if(this.historicoEmpleadoConDemoras != null)
		{
			for(Empleado empleado: this.historicoEmpleadoConDemoras)
			{
				empleado.toString();
			}
		}
		else
		{
			throw new IllegalArgumentException("Esta tarea no tiene empleados con demoras.");
		}
	}
	
	public String consultarEmpleado()
	{
		return this.empleadoAsignado.toString();
	}
	
	public Empleado retornarEmpleadoTarea()
	{
		return this.empleadoAsignado;
	}
	
	public String retornarTitulo()
	{
		return this.titulo;
	}
	
	@Override
	public String toString()
	{
		return "Titulo de la tarea: " + titulo + ".";
	}
	
	//añadida
	public boolean tieneEmpleado()
	{
		boolean tiene = true;
		tiene &= this.empleadoAsignado != null;
		
		return tiene;
	}

	public int consultarLegajoEmpleado() 
	{ 
		return this.empleadoAsignado.retornarLegajo();
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getDiasNecesarios() {
		return diasNecesarios;
	}

	public void setDiasNecesarios(double diasNecesarios) {
		this.diasNecesarios = diasNecesarios;
	}

	public String getFechaFinalizacion() {
		return fechaFinalizacion;
	}

	public void setFechaFinalizacion(String fechaFinalizacion) {
		this.fechaFinalizacion = fechaFinalizacion;
	}

	public String getFechaAsignacionEmpleado() {
		return fechaAsignacionEmpleado;
	}

	public void setFechaAsignacionEmpleado(String fechaAsignacionEmpleado) {
		this.fechaAsignacionEmpleado = fechaAsignacionEmpleado;
	}

	public void aniadirCostoTarea()
	{
		if(this.empleadoAsignado == null)
		{
			this.costoTarea = 0;
		}
		else
		{
			this.costoTarea = this.empleadoAsignado.retornarValor(this.diasNecesarios);
		}

	}
	
	public double retornarCostoTarea() {
		
		if(this.empleadoAsignado.retornarCantidadRetrazos()<= 0)
		{
			return this.costoTarea * 1.02;
		}
		return this.costoTarea;
	}
	

}
