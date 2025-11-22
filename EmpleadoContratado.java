package entidades;

import java.util.Objects;

public class EmpleadoContratado extends Empleado{
	private double valorHora;
	
	public EmpleadoContratado()
	{
		super();
		this.valorHora = 0.00;
	}
	
	public EmpleadoContratado(String nombre, double valorHora)
	{
		super(nombre);
		if(valorHora > 0)
		{		
			this.valorHora = valorHora;
		}
		else
		{
			throw new IllegalArgumentException("Valor invalido " + valorHora + ". Debe ser mayor a cero.");
		}
	}
	
	public double retornarValor(double dias)
	{
		return this.valorHora * (dias * 8);
	}
	
	public void cobrarSueldo()
	{
		this.valorHora++;
	}
	
	public boolean mostrarDisponible()
	{
		return super.mostrarDisponible();
	}

	public int consultarCantidadRetrazos()
	{
		return super.consultarCantidadRetrazos();
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(valorHora);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))
		{
			return false;
		}
		EmpleadoContratado emp = (EmpleadoContratado) obj;
		return Double.doubleToLongBits(valorHora) == Double.doubleToLongBits(emp.valorHora);
	}
		
	
}
