/**
 * Accion para el problema GPF (grid pathfinding)
 * Puede ser: ir a derecha|arriba|izquierda|abajo
 */
package busqueda.GPF;

/**
 * @author ????
 * @version 2021.09.*
 */
public class AccionGPF {
	
	Acciones posiblesAcciones;
	public AccionGPF(Acciones accion) {
		this.posiblesAcciones = accion;
	}

	// TODO
	// HAY QUE COMPLETAR ESTA CLASE PARA REPRESENTAR TODAS LAS POSIBLES ACCIONES QUE SE PUEDEN APLICAR EN UN ESTADO DEL PROBLEMA DE GPF
	@Override
	public String toString() {
		String sa="";
		switch(posiblesAcciones) {
			case IZQUIERDA:
				sa = "Ir hacia la izquierda";
				break;
			case DERECHA:
				sa = "Ir hacia la derecha";
				break;
			case ARRIBA:
				sa = "Ir hacia arriba";
				break;
			case ABAJO:
				sa = "Ir hacia abajo";
				break;
			default:
				break;
		}
		// TODO es necesario implementar el metodo toString() para mostrar soluciones en la consola
		return sa;
	}
	
	public Acciones getAcciones() {
		return posiblesAcciones;
	}
}
