package entidades;

public class Cliente 
{
	private String nombre;
	private String email;
	private String telefono;
	
	Cliente()
	{
		this.nombre = "";
		this.telefono = "";
		this.email = "";
	}
	//modificada
	Cliente(String[] cliente)
	{
		this.nombre = cliente[0];
		this.email = cliente[1];
		this.telefono = cliente[2];
	}

	@Override
	public String toString() 
	{
		return "Nombre Cliente " + nombre + ", teléfono " + telefono + " y email " + email + ".";
	}

}
