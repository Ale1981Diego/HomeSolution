package entidades;

import java.util.Objects;

public abstract class Empleado  {
	private static int nroLegajo = 1000;
	private int legajo;
	private String nombre;
	private boolean disponible;
	private int cantidadRetrazos;

	
	public Empleado ()
	{
		this.nombre = "";
		this.disponible = true;
		this.cantidadRetrazos = 0;
		
	}
	
	public Empleado(String nombre)
	{
		this.legajo = nroLegajo;
		this.nombre = nombre;
		this.disponible = true;
		this.cantidadRetrazos = 0;
		
		incrementarLegajo();
	}
	
	private void incrementarLegajo()
	{
		this.nroLegajo ++;
	}
	
	// ver posibilidad de crear una función auxiliar para validar si o no verdadero.
	public void modificaDisponible()
	{
		if(this.disponible)
		{
			this.disponible  = false;
		}
		else
		{
			this.disponible = true;			
		}
	}
	
	
	// a revisar - Opciones posibles: 1 - incrementar por día de retrazo / 2 - pasar por parametros la cantidad de dias de retrazo
	void incremetarRetrazos()
	{
		this.cantidadRetrazos ++;
	}
	

	public abstract void cobrarSueldo();
	
	public int consultarCantidadRetrazos()
	{
		return this.cantidadRetrazos;
	}
	//extras
	public int retornarLegajo()
	{
		return this.legajo;
	}
	//extras
	public String retornarNombre()
	{
		return this.nombre;
	}
	
	//extra
	public boolean mostrarDisponible()
	{
		return this.disponible;
	}

	@Override
	public String toString()
	{
		return this.legajo+""; 
	}

	public int retornarCantidadRetrazos()
	{
		return this.cantidadRetrazos;
	}

	public boolean consultarPoseeRetrasos()
	{
        if (this.cantidadRetrazos > 0)
        {
        	return true;
        }
        return false;
	}
	

	@Override
	public int hashCode() {
		return Objects.hash(cantidadRetrazos, disponible, legajo, nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Empleado emp = (Empleado) obj;
		return cantidadRetrazos == emp.cantidadRetrazos && disponible == emp.disponible && legajo == emp.legajo
				&& Objects.equals(nombre, emp.nombre);
	}
	
	public abstract double retornarValor(double dias);

}
