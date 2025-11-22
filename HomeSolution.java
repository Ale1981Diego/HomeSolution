package entidades;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class HomeSolution implements IHomeSolution
{
	HashMap<Integer, Proyecto> proyectos;
	HashMap<Integer, Empleado> empleados;
	
	public HomeSolution()
	{
		this.proyectos = new HashMap<>();
		this.empleados = new HashMap<>();
	}
	
	// ============================================================
    // REGISTRO DE EMPLEADOS
    // ============================================================
	
	public void registrarEmpleado(String nombre, double valorHora)throws IllegalArgumentException
	{
		if(stringNoVacio(nombre) && valorHora > 0)
		{
			EmpleadoContratado empleado = new EmpleadoContratado(nombre, valorHora);
			this.empleados.put(empleado.retornarLegajo(), empleado);			
		}
		else
		{
			throw new IllegalArgumentException("No pudo registrarse dicho empleado, sus valores son incorrectos");
		}
	}
	
	
	public void registrarEmpleado(String nombre, double valorDia, String categoria)throws IllegalArgumentException
	{
		if(stringNoVacio(nombre) && stringNoVacio(categoria) && valorDia > 0)
		{
			EmpleadoPermanente empleado = new EmpleadoPermanente(nombre, valorDia,categoria);
			this.empleados.put(empleado.retornarLegajo(), empleado);			
		}
		else
		{
			throw new IllegalArgumentException("Los valores ingresados no son correctos.");
		}
	}
	
	 // ============================================================
    // REGISTRO Y GESTIÓN DE PROYECTOS
    // ============================================================
	
	public void registrarProyecto(String[] titulos, String[] descripcion, double[] dias,
            String domicilio, String[] cliente, String inicio, String fin)throws IllegalArgumentException
	{
		
		if(titulos == null || descripcion == null || dias == null || domicilio == null || cliente == null || inicio == null || fin ==  null)
		{
			throw new IllegalArgumentException("No pudo registrarse dicho proyecto, ya que uno de sus datos estaba vacio");
		}
		if (!validarFechas(inicio, fin))
		{
			throw new IllegalArgumentException("No pudo registrarse dicho proyecto, ya que las fechas son incorrectas");
		}
		else
		{
			Proyecto proyecto = new Proyecto(titulos, descripcion, dias, domicilio, cliente, inicio, fin);
			if(proyecto != null)
			{
				this.proyectos.put(proyecto.retornarCodigo(), proyecto);
			}
			
		}

	}
	
	
	// ============================================================
    // ASIGNACIÓN Y GESTIÓN DE TAREAS
    // ============================================================

	public void asignarResponsableEnTarea(Integer numero, String titulo)throws IllegalArgumentException
	{
		Proyecto proyecto = this.proyectos.get(numero);
		Empleado empleado = null;
		if(existe(proyecto))
		{
			for(Empleado emp:this.empleados.values()) 
			{
				if(emp.mostrarDisponible())
				{
					empleado = emp;
					break;
				}
			}
			if(empleado == null)
			{
				throw new IllegalArgumentException("no se encontraron empleados disponibles");
			}
			else
			{
				proyecto.registrarEmpleado(empleado, titulo);
				proyecto.modifcarEstado();
			}
			
		}
		else
		{
			throw new IllegalArgumentException("El proyecto " + numero + " no existe.");
		}
	}

	
	public void asignarResponsableMenosRetraso(Integer numero, String titulo)throws Exception
	{
	    Proyecto proyecto = this.proyectos.get(numero);
	    if (proyecto == null)
	    {
	        throw new IllegalArgumentException("No existe el proyecto");
	    } 
	    Empleado empleadoMenosRetraso = null;
	    int menorRetraso = Integer.MAX_VALUE;
	    for (Empleado emp : listaEmpleadosNoAsignados()) 
	    {
	        int retrasos = emp.consultarCantidadRetrazos();
	        if (retrasos < menorRetraso) {
	            menorRetraso = retrasos;
	            empleadoMenosRetraso = emp;
	        }
	    }
	    if (empleadoMenosRetraso != null) {   
	    	proyecto.registrarEmpleado(empleadoMenosRetraso, titulo);
			proyecto.modifcarEstado();
	    } else {
	        throw new IllegalStateException("No se pudo determinar un empleado valido para asignar.");
	    }
	}

	 
	 public void registrarRetrasoEnTarea(Integer numero, String titulo, double cantidadDias)throws IllegalArgumentException
	 
	 {
		 Proyecto proyecto = this.proyectos.get(numero);
		 if(existe(proyecto))
		 {
			 Empleado empleado = (proyecto.retornarTareas(titulo)).retornarEmpleadoTarea();
			 empleado.incremetarRetrazos();
			 proyecto.retornarTareas(titulo).actualizarDiasRetrazo(cantidadDias);
	     }
		 else
		 {
			 throw new IllegalArgumentException("");
		 }
	 }
	 
	 
	 public void agregarTareaEnProyecto(Integer numero, String titulo, String descripcion, double dias)throws IllegalArgumentException
	 {
		 Proyecto proyecto = this.proyectos.get(numero);
		 if(proyecto == null)
		 {
			 throw new IllegalArgumentException("no existe el proyecto");
		 }
		 else
		 {
			proyecto.agregarTarea(titulo, descripcion, dias); 
		 }
		 
	 }
	 
	 public void finalizarTarea(Integer numero, String titulo)throws Exception
	 {
		 Proyecto proyecto = this.proyectos.get(numero);
		 if(proyecto != null)
		 {
			 proyecto.finalizarTarea(titulo);
		 }
	 }
	 
	 public void finalizarProyecto(Integer numero, String fechaFinalización)throws IllegalArgumentException
	 {
			Proyecto proyecto = this.proyectos.get(numero);
			
			if(proyecto == null)
			{
				throw new IllegalArgumentException("No existe el proyecto nro. " + numero);
			}
			if(proyecto.estaFinalizado())
			{
				throw new IllegalArgumentException("el proyecto nro. " + numero + " ya esta finalizado.");
			}
			else
			{
				LocalDate fechaInicio = LocalDate.parse(proyecto.getFechaIncio());
				LocalDate fechaFin = LocalDate.parse(fechaFinalización);
				if(fechaInicio.isAfter(fechaFin))
				{
					throw new IllegalArgumentException("La fecha de inicio del proyecto: " + fechaInicio + " no puede ser mayor a la fecha " + fechaFin);
				}
				else
				{
					if(tareasProyectoNoAsignadas(numero).length == 0)
					{
						proyecto.modificarFechaFinalizacion(fechaFinalización);
					}
					else
					{
						throw new IllegalArgumentException("El proyecto no tiene todas las tareas asignadas!!!");
					}
				}
			}
	 }
	 
	 // ============================================================
	 // REASIGNACIÓN DE EMPLEADOS
	 // ============================================================
	 
	 public void reasignarEmpleadoEnProyecto(Integer numero, Integer legajo, String titulo)throws Exception
	 {
			Proyecto proyecto = this.proyectos.get(numero);
			Empleado empleado = empleados.get(legajo);
			if(existe(proyecto) && empleado != null)
			{
				if(empleado.mostrarDisponible())
				{					
					proyecto.reasingarEmpleado(empleado, titulo);
				}
				else
				{
					throw new Exception("El empleado con nro. de legajo " + legajo + ", no se encuentra disponible!");
				}
			}
			else
			{
				throw new Exception("El proyecto nro.: " + numero + ", no existe o el empleado con legajo: " + legajo + ", no existe.");
			}
	 }
	 
	 public void reasignarEmpleadoConMenosRetraso(Integer numero, String titulo)throws Exception
	 {
		Proyecto proyecto = this.proyectos.get(numero);
		if(existe(proyecto))
		{
		    Empleado empleadoMenosRetraso = null;
		    int menorRetraso = Integer.MAX_VALUE;
		    for (Empleado emp : listaEmpleadosNoAsignados()) 
		    {
		        int retrasos = emp.consultarCantidadRetrazos();
		        if (retrasos < menorRetraso)
		        {
		            menorRetraso = retrasos;
		            empleadoMenosRetraso = emp;
		        }
		    }
		    if (empleadoMenosRetraso != null) {   // posible problema actual es que no asgina los retrasos, y adem�s no cambia la disponibilidad
		        proyecto.reasingarEmpleado(empleadoMenosRetraso, titulo);
		        empleadoMenosRetraso.modificaDisponible();
		    } else {
		        throw new IllegalStateException("No se pudo determinar un empleado valido para asignar.");
		    }
		}
		else {
	        throw new IllegalStateException("No existe el proyecto nro. " + numero);
	    }
	 }
	 
	  // ============================================================
	  // CONSULTAS Y REPORTES
	  // ============================================================
	 

	 
	 public List<Tupla<Integer, String>> proyectosFinalizados()
	 {
		 List<Tupla<Integer, String>>listaProyectosFinalizados = new ArrayList<>();
		 
		 for(Map.Entry<Integer, Proyecto> proy: this.proyectos.entrySet())
		 {
			 Proyecto proyecto = proy.getValue();
			 if(estaFinalizado(proyecto))
			 {
				 listaProyectosFinalizados.add(new Tupla<>(proy.getKey(), proy.getValue().toString()));
			 }
		 }
		 return listaProyectosFinalizados;
	 }
	 
	 public List<Tupla<Integer, String>> proyectosPendientes()
	 {
		 List<Tupla<Integer, String>>listaProyectosPendientes = new ArrayList<>();
		 
		 for(Map.Entry<Integer, Proyecto> proy: this.proyectos.entrySet())
		 {
			 Proyecto proyecto = proy.getValue();
			 if(estaPendiente(proyecto)&& proyecto.mostrarEstado()!= "finalizado")
			 {
				 listaProyectosPendientes.add(new Tupla<>(proy.getKey(), proy.getValue().toString()));
			 }
		 }
		 return listaProyectosPendientes;
	 }
	 
	 public List<Tupla<Integer, String>> proyectosActivos()
	 {
		 List<Tupla<Integer, String>>listaProyectosActivos = new ArrayList<>();
		 
		 for(Map.Entry<Integer, Proyecto> proy: this.proyectos.entrySet())
		 {
			 Proyecto proyecto = proy.getValue();
			 if(estaActivo(proyecto))
			 {
				 listaProyectosActivos.add(new Tupla<>(proy.getKey(), proy.getValue().toString()));
			 }
		 }
		 return listaProyectosActivos;
	 }
	 
	 public Object[] empleadosNoAsignados()
	 {
		 List<Empleado>empleadosNoAsignados = new ArrayList<>();
		 for(Empleado empleado: this.empleados.values())
		 {
			 if(empleado.mostrarDisponible())
			 {
				 empleadosNoAsignados.add(empleado);
			 }
		 }
		 
		 return empleadosNoAsignados.toArray();
	 }
	 
	 private List<Empleado> listaEmpleadosNoAsignados()
	 {
		 List<Empleado>empleadosNoAsignados = new ArrayList<>();
		 for(Empleado empleado: this.empleados.values())
		 {
			 if(empleado.mostrarDisponible())
			 {
				 empleadosNoAsignados.add(empleado);
			 }
		 }
		 
		 return empleadosNoAsignados;
	 }
	 
	 public int consultarCantidadRetrasosEmpleado(Integer legajo)
	 {
		 Empleado emp = this.empleados.get(legajo);
		 if(emp == null)
		 {
			 throw new IllegalArgumentException("El empleado no existe");
		 }
		 else
		 {
			 return emp.consultarCantidadRetrazos();
		 }
	 }
	 

	 
	 public List<Tupla<Integer, String>> empleadosAsignadosAProyecto(Integer numero)
	 {
		 List<Tupla<Integer, String>>listaEmpeladosProyectos = new ArrayList<>();
		 Proyecto proyecto = this.proyectos.get(numero);
		 if(proyecto != null)
		 {
			 for(Tarea tarea : proyecto.retornarTareas().values())
			 {
				 Empleado empleado = tarea.retornarEmpleadoTarea();
				 if(empleado != null)
				 {
					 listaEmpeladosProyectos.add(new Tupla<>(empleado.retornarLegajo(), empleado.retornarNombre()));					 
				 }

			 }
			 return listaEmpeladosProyectos;
		 }
		 else
		 {
			 throw new IllegalArgumentException();
		 }
	 }
	 

	  // ============================================================
	  // NUEVOS REQUERIMIENTOS
	  // ============================================================
	 
	 public Object[] tareasProyectoNoAsignadas(Integer numero)
	 {
		 List<Tarea>tareasProyectoNoAsignadas = new ArrayList<>();
		 Proyecto proyecto = this.proyectos.get(numero);
		 if(proyecto == null)
		 {
			 throw new IllegalArgumentException("no existe el proyecto nro. " + numero);
		 }
		 if(proyecto.estaFinalizado())
		 {
			 throw new IllegalArgumentException("El proyecto está finalizado"); 
		 }
		 for(Tarea tarea: proyecto.retornarTareas().values())
		 {
			 if(tarea.retornarEmpleadoTarea() == null)
			 {
				 tareasProyectoNoAsignadas.add(tarea);
			 }
		 }
		 return tareasProyectoNoAsignadas.toArray();
		}
	 
	 public String consultarDomicilioProyecto(Integer numero)
	 {
		 Proyecto proyecto = this.proyectos.get(numero);
		 if(proyecto != null)
		 {
			 return proyecto.mostrarDomicilio();
		 }
		 else
		 {
			 throw new IllegalArgumentException("El proyecto nro: " + numero + ", no existe!");
		 }
	 }
	 
	 
	 public List<Tupla<Integer, String>> empleados()//borra boolean
	 {
		 List<Tupla<Integer, String>>listaEmpleados = new ArrayList<>(); //borrar boolean
		 
		 for(Map.Entry<Integer, Empleado> emp: this.empleados.entrySet())
		 {
			 listaEmpleados.add(new Tupla<>(emp.getKey(), emp.getValue().retornarNombre())); //borrar tercer parametro
		 }
		 return listaEmpleados;
	 }

	 
	 public Object[] tareasDeUnProyecto(Integer numero)
	{
		 Object[]tareasProyecto; 

		 Proyecto proyecto = this.proyectos.get(numero);
		 
		 if(proyecto == null) 
		 {
			 throw new IllegalArgumentException("No existe el proyecto " + numero);
		 }
		 tareasProyecto = new Object[proyecto.retornarTareas().size()];
		 Collection<Tarea> tareasCollection = proyecto.retornarTareas().values();
		 List<Tarea> tareasList = new ArrayList<>(tareasCollection);
		 for(int i = 0; i < proyecto.retornarTareas().size(); i++)
		 {
			 Tarea t = tareasList.get(i);
			 tareasProyecto[i] =  t.retornarTitulo();
		 }
		 return tareasProyecto;
	}

	public String consultarProyecto(Integer numero)
	{
		Proyecto proyecto = this.proyectos.get(numero);
		if(proyecto == null)
		{
			throw new IllegalArgumentException("No existe el proyecto " + numero);
		}
		return proyecto.toString();
	}


	@Override
	public double costoProyecto(Integer numero)
	{
		Proyecto proyecto = this.proyectos.get(numero);
		if(proyecto == null)
		{
			throw new IllegalArgumentException("El proyecto no existe");
		}
		else
		{
			return proyecto.retornarCostoFinal();			
		}
	}

	public boolean tieneRestrasos(Integer legajo) {
		boolean esRetrasado=true;
         Empleado empleado = empleados.get(legajo);
         esRetrasado &= empleado.consultarPoseeRetrasos();
         return esRetrasado;
	}
	
	//agregadas
	private boolean existe(Proyecto proyecto)
	{
		boolean existe = true;
		
		existe&= proyecto != null;
		
		return existe;
	}
	
	private boolean estaPendiente(Proyecto p)
	{
		return  p.mostrarEstado() =="PENDIENTE";
	}
	
	private boolean estaActivo(Proyecto p)
	{
		return  p.mostrarEstado()== "ACTIVO";
	}
	
	private boolean estaFinalizado(Proyecto p)
	{
		return  p.mostrarEstado()== "FINALIZADO";
	}
	
	
	private boolean validarFechas(String inicio, String fin)
	{
		boolean fechaValida= true;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate inicio1 = LocalDate.parse(inicio, formatter);
        LocalDate fin1 = LocalDate.parse(fin, formatter);
        if (inicio1.isAfter(fin1))
        {
            fechaValida=false;
        }
        
        return fechaValida;
	}
	
	/*private Empleado[] convertirAEmpleados(Object[] objetos) {
	    return Arrays.copyOf(objetos, objetos.length, Empleado[].class);
	}
	private Tarea[] convertirATareas(Object[] objetos) {
	    return Arrays.copyOf(objetos, objetos.length, Tarea[].class);
	}*/
	
	private boolean stringNoVacio(String palabra)
	{
		boolean noVacio = true;
		noVacio &= palabra != null && palabra != "";
		
		return noVacio;
	}
	
	private boolean validarArray(String[]a)
	{
		boolean noEsNull = true;
		noEsNull &= a != null;
		return noEsNull;
	}
	
	private boolean validarArray(double[]a)
	{
		boolean noEsNull = true;
		noEsNull &= a != null;
		return noEsNull;
	}
	
	private boolean stringVacio(String variable)
	{
		return variable != "";
	}

	@Override
	public boolean estaFinalizado(Integer numero)
	{
		Proyecto proy = this.proyectos.get(numero);
		if(proy == null)
		{
			throw new IllegalArgumentException("No existe el proyecto nro.: " + numero);
		}
		
		
		return proy.estaFinalizado();
	}
	

}

