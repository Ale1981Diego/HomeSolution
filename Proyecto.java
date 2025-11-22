package entidades;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Proyecto
{
	private static int codigo = 1000;
	private int codigoProyecto;
	private String estadoProyecto;
	private Cliente cliente;
	private String direccionVivienda;
	private HashMap<String, Tarea> tareas;
	private String fechaIncio;
	private String fechaEstimada;
	private String fechaFinalizada;
	private double costoFinal;
	private double dias;
	private boolean finalizado;
	
	public Proyecto()
	{
		this.codigoProyecto = 0;
		this.cliente = null;
		this.direccionVivienda = "";
		this.tareas = null;
		this.fechaIncio = "";
		this.fechaEstimada = "";
		this.fechaFinalizada = "";
		this.finalizado = false;
		}
	//modificado
	public Proyecto(String[] titulo, String[]descripcion, double[]dias, 
			String direccion, String[]cliente, String fechaInicio, String fechaEstimada)
	{
			this.codigoProyecto = codigo;
			incrementarCodigo();
			this.cliente = new Cliente(cliente);
			this.direccionVivienda = direccion;
			this.tareas = new HashMap<>();
			agregarTarea(titulo, descripcion, dias);
			this.fechaIncio = fechaInicio;
			this.fechaEstimada = fechaEstimada;
			this.fechaFinalizada = fechaEstimada;
			this.estadoProyecto = Estado.pendiente;
			this.finalizado = false;

	}
	
	public void agregarTarea(String[] tituloTarea, String[] descripcionTarea, double[] diasNecesarios)
	{
		if(!esNulo(tituloTarea)&& !esNulo(descripcionTarea)&& !esNulo(diasNecesarios))
		{
			if(tituloTarea.length == descripcionTarea.length && tituloTarea.length == diasNecesarios.length)
			{
				for(int i = 0; i < tituloTarea.length; i ++)
				{
					
					if(tituloTarea[i] == null || tituloTarea[i].isBlank())
					{
						throw new IllegalArgumentException("Titulo invalido.");
					}
					/*if(descripcionTarea[i] == null || descripcionTarea[i].isBlank())
					{
						throw new IllegalArgumentException("Descripción invalida.");
					}*/
					if(diasNecesarios[i] <= 0)
					{
						throw new IllegalArgumentException("El valor de días no puede ser negativo");
					}
					else
					{
						Tarea tarea = new Tarea(tituloTarea[i], descripcionTarea[i], diasNecesarios[i]);
				        
				        this.tareas.put(tarea.retornarTitulo(), tarea);
					}
				}
			}
			else
			{
				throw new  IllegalArgumentException("Error al agregar tarea!");
			}
		}
		else
		{
			throw new  IllegalArgumentException("No se pueden agregar las tareas en vacias!");
		}
		
	}
	
	public void agregarTarea(String nombre, String descripcion, double dias)
	{
		if(nombre == "" || descripcion == "" || dias <= 0)
		{
			throw new IllegalArgumentException("Algunos datos son incorrectos");
		}
		else
		{
			Tarea tarea = new Tarea(nombre, descripcion, dias);
			this.tareas.put(tarea.retornarTitulo(), tarea);
		}
	}
	
	public void finalizarTarea(String titulo)
	{
		Tarea tarea = this.tareas.get(titulo);
		if(tarea != null)
		{
			tarea.finalizarTarea();
		}
		else
		{
			throw new IllegalArgumentException("No existe tarea con el titulo: " + titulo);
		}
	}
	

	public String mostrarEstado()
	{
		return this.estadoProyecto;
	}
	
	public void modifcarEstado()
	{
		this.estadoProyecto = validarEstado();
	}
	
	public void modificarFechaFinalizacion(String fecha)
	{
		this.fechaFinalizada = fecha;
		this.estadoProyecto = Estado.finalizado; // esto aplica si el cliente solicita finalizar el proyecto, sin tener todas las tareas finalizadas.
		this.finalizado = true;
		for(Tarea t : this.tareas.values())
		{
			t.finalizarTarea();
		}
	}
	
	// nuevo -- para que este metodo funcione correctamente (para finalizar el proyecto), todas las tareas esten finalizadas.
	private String validarEstado()
	{
		boolean tareasFinalizadas = true;
		boolean tareasConEmpleados = true;
		for(Tarea tarea : this.tareas.values())
		{
			tareasFinalizadas &= tarea.retornarEstado();
			tareasConEmpleados &= tarea.tieneEmpleado();
		}
		if(tareasFinalizadas && tareasConEmpleados){
			this.estadoProyecto = Estado.finalizado;
			return this.estadoProyecto;
			
		}
		if(!tareasFinalizadas && tareasConEmpleados)
		{
			this.estadoProyecto = Estado.activo;
			return this.estadoProyecto;
		}
		else
		{
			this.estadoProyecto=Estado.pendiente;
			return this.estadoProyecto;
		}
	}
	
	//nuevo 
	public String mostrarDomicilio()
	{
		return this.direccionVivienda;
	}
	
	public void reasingarEmpleado(Empleado empleado, String tituloTarea)
	{
		Tarea tarea = this.tareas.get(tituloTarea);
		if(tarea.consultarEmpleado()!= null)
		{
			modificarCostoFinal(tarea.retornarCostoTarea());
			tarea.cambiarEmpleado(empleado);
			
			incrementarCostoFinal(tarea.retornarCostoTarea());
		}
	}
	
	
	public void registrarEmpleado(Empleado empleado, String tituloTarea)
	{
		Tarea tarea = this.tareas.get(tituloTarea);
		if(existe(tarea))
		{
			tarea.asignarEmpleado(empleado);
			incrementarCostoFinal(tarea.retornarCostoTarea());
		}
		else
		{
			throw new IllegalArgumentException("No existe una tarea con este titulo: " + tituloTarea);
		}
		
	}
	
	public int retornarCodigo()
	{
		return this.codigoProyecto;
	}
	
	//metodos agregados
	private boolean existe(Tarea tarea)
	{
		boolean existe = true;
		
		existe &= tarea != null;
		
		return existe;
	}
	
	private void incrementarCodigo()
	{
		this.codigo ++;
	}
	
	private boolean esNulo(String[]array)
	{
		boolean nulo = true;
		nulo &= array == null;
		return nulo;
	}
	
	private boolean esNulo(double[]array)
	{
		boolean nulo = true;
		nulo &= array == null;
		return nulo;
	}
	
	//////////
	//nuevo
	//////////
	private boolean valoresMayoresACero(double[]array)
	{
		boolean esMayorACero=true;
		if(!esNulo(array))
		{
			for(int i = 0; i < array.length; i++)
			{
				esMayorACero &= array[i]>0;
					
			}
			return esMayorACero;
		}
		else
		{
			return false;
		}
		
	}
	
	public boolean estaFinalizado()
	{
		return this.finalizado;
	}
	
	
	
	@Override
	public String toString() {
		return "Proyecto Nro: " + codigoProyecto + ", perteneciente al Cliente: " + this.cliente.toString() + ". Direccion: " + this.direccionVivienda;
	}
	
	//nuevo

    public HashMap<String, Tarea> retornarTareas()
    {
        return this.tareas;
    }
    
	public Tarea retornarTareas(String titulo)throws IllegalArgumentException
	{
		
	    Tarea tarea = this.tareas.get(titulo);
	    if (tarea != null)
	    {
	    	return tarea;
	    }
	    throw new IllegalArgumentException("Error al retornar tarea");
	}
	
	public String getFechaIncio()
	{
		return fechaIncio;
	}
	public void setFechaIncio(String fechaIncio) {
		this.fechaIncio = fechaIncio;
	}
	public String getFechaEstimada() {
		return fechaEstimada;
	}
	public void setFechaEstimada(String fechaEstimada) {
		this.fechaEstimada = fechaEstimada;
	}
	public String getFechaFinalizada() {
		return fechaFinalizada;
	}
	public void setFechaFinalizada(String fechaFinalizada) {
		this.fechaFinalizada = fechaFinalizada;
	}
	
	public void incrementarCostoFinal(double costoTarea)
	{
		
		this.costoFinal += costoTarea;
	}
	
	public void modificarCostoFinal(double valorTarea)
	{
		this.costoFinal -= valorTarea;
	}
	/*
	public double retornarCostoFinal()
	{
		
		return this.costoFinal;
	}*/
	
	public double retornarCostoFinal() {

	    double costoBase = this.costoFinal;
	    double costoTotal = costoBase * 1.35;

	    
	    DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate fechaEst = LocalDate.parse(this.fechaEstimada, f);
	    LocalDate fechaFin = LocalDate.parse(this.fechaFinalizada, f);

	    if (fechaFin.isAfter(fechaEst)) {
	        double diasRetraso = fechaFin.toEpochDay() - fechaEst.toEpochDay();
	        double penalizacion = diasRetraso * 0.10 * costoBase ;
	        costoTotal -= penalizacion;
	    	
	    }

	    return costoTotal;
	}
	
}
