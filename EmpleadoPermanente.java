package entidades;

import java.util.Objects;

public class EmpleadoPermanente extends Empleado {
	private String categoria;
	private double valorDia;
	private double diasTrabajados;
	
	public EmpleadoPermanente()
	{
		super();
		this.categoria = "";
		this.valorDia = 0.00;
		this.diasTrabajados = 0.00;
	}
	
	public EmpleadoPermanente(String nombre,double valorDia,String categoria)
	{
		super(nombre);
		if(validarCategoria(categoria.toLowerCase()))
		{
			this.categoria = categoria.toLowerCase();
			this.valorDia = valorDia;
			this.diasTrabajados = 0.00;			
		}
		else
		{
			throw new IllegalArgumentException("Categoria invalida: " + categoria);
		}
	}
	
	//función auxiliar, valida que la categoria cumpla el IREP
	private boolean validarCategoria(String categoria)
	{
		String[]cat = {"inicial", "tecnico", "experto"};
		boolean existe = false;
		for(String c : cat)
		{
			existe |= c.equals(categoria);				
		}
		return existe;
	}
	
	public void cobrarSueldo()
	{
		
	}
	
	@Override
	public String toString()
	{
		return super.toString(); 
	}
	
	public boolean mostrarDisponible()
	{
		return super.mostrarDisponible();
	}

	public int consultarCantidadRetrazos()
	{
		return super.consultarCantidadRetrazos();
	}

	public double retornarValor(double dias) {
		return valorDia * dias;
	}

	public void agregarValorDia(double valorDia) {
		this.valorDia = valorDia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(categoria, valorDia);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))
		{
			return false;
		}
		EmpleadoPermanente emp = (EmpleadoPermanente) obj;
		return Objects.equals(categoria, emp.categoria);
	}
	
	

}
