package busqueda;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Clase que realiza una busqueda primero el mejor:
 * busqueda general en grafos con frontera cola de prioridad
 * Segun el criterio de ordenacion puede ser:
 * - g(): busqueda coste uniforme
 * - h(): busqueda primero el mejor voraz
 * - f(): busqueda A*
 * Para cualquiera de los 2 ultimos, por defecto h=0, aunque puede ser otro
 * Generaliza (y hace innecesaria) la clase BusquedaCosteUniforme
 * @author Ines
 * @version 2021.10.*
 */
public class BusquedaPrimeroMejor<Estado,Accion> extends Busqueda<Estado,Accion> {
	private Heuristico<Estado> h;// heuristico que se va a usar
	private Comparator<Nodo<Estado,Accion>> comp;// comparador para comparar nodos (elegir el mejor)

	/**
	 * Constructor
	 * Instancia la frontera a una cola de prioridad ordenada por g():
	 * BUSQUEDA DE COSTE UNIFORME
	 * @param p, el problema para el que se va a realizar la busqueda
	 */
	public BusquedaPrimeroMejor(Problema<Estado,Accion> p) {
		super(p);
		frontera = new FronteraPrioridad<Estado,Accion>(Criterio.g);
		h = new Heuristico<Estado>();// h trivial
		comp = new ComparaNodos<Estado,Accion>(Criterio.g);// comparar segun g
	}
	
	/**
	 * Constructor con criterio de prioridad c (g(), h() o f()) y heuristico h
	 * BUSQUEDA PRIMERO EL MEJOR:
	 * - si c es g(), busqueda de coste uniforme
	 * - si c es h(), busqueda primero el mejor voraz
	 * - si c es f(), A*
	 * @param p, el Problema a resolver
	 * @param c, el criterio de ordenacion de la frontera (puede ser g(), f() o h())
	 * @param h, la funcion heuristica a utilizar
	 */
	public BusquedaPrimeroMejor(Problema<Estado,Accion> p, Criterio c, Heuristico<Estado> h){
		super(p);
		frontera = new FronteraPrioridad<Estado,Accion>(c);
		this.h = h;
		comp = new ComparaNodos<Estado,Accion>(c);
	}
	
	/**
	 * Metodo inicia() modificado para calcular valor de H de nodo inicial
	 */
	@Override
	protected void inicia(){
		frontera.clear();
		Nodo<Estado,Accion> inicial = new Nodo<Estado,Accion>( prob.getInicio());
		// al crear el nodo no se calcula h, hay que hacerlo explicitamente
		inicial.setH(h.calculaH(inicial.getEstado()));
		frontera.aniade(inicial);
		explorados.clear();
	}

	
	/**
	 * Metodo tratarRepetidos
	 * Se modifica el generico para que, si encuentra un nodo "repetido" en la frontera,
	 * en lugar de descartar el hijo (el mas nuevo), deje en la frontera el mejor de ambos nodos
	 * (segun el criterio de comparacion)
	 * @param lista de nodos hijos a tratar
	 */
	@Override
	protected void tratarRepetidos(List<Nodo<Estado,Accion>> hijos){
		// TODO Hay que completarlo (o copiarlo de la practica anterior de la clase BusquedaCosteUniforme)
		/* INDICACIONES:
		 * - aqui hay que recorrer la lista de sucesores (hijos)
		 * - para comprobar si un hijo esta en explorados, basta utilizar el metodo Map.get, recordando que la clave del mapa es el estado
		 * - para comprobar si un hijo esta en la frontera o esta pero peor (en cuyo caso, el "peor" se borra),
		 * puede utilizarse el metodo auxiliar noRepeOMejorEnFrontera( Nodo hijo )
		 */
		Iterator<Nodo<Estado,Accion>> it = hijos.iterator();
		while( it.hasNext() ){ 		// recorremos lista hijos
			Nodo<Estado,Accion> hijo=it.next();
			if( explorados.get(hijo.getEstado())==null){ // hijo no esta en explorados
				if( noRepeOMejorEnFrontera( hijo ) )
					frontera.aniade(hijo);;
			}
		}
	}// fin tratarRepetidos()

	/**
	 * metodo para decidir si hay que aniadir un nodo sucesor a la frontera:
	 * - si no esta todavia o
	 * - si ya hay otro nodo para el mismo estado pero es peor (en cuyo caso, de paso, se borra el peor de la frontera)
	 * @param hijo, el nodo que se acaba de crear
	 * @return cierto, si hay que aniadir el hijo a la frontera, falso en otro caso
	 * <dt><b>Modifica:</b><dd> la frontera, si borra un elemento
	 * <dt><b>Modifica:</b><dd> el Nodo hijo, si lo va a aniadir, actualizando el valor de h
	 */
	private boolean noRepeOMejorEnFrontera(Nodo<Estado, Accion> hijo) {
		Iterator<Nodo<Estado,Accion>> iter = frontera.iterator(); // iterador para recorrer la frontera
		boolean sinEncontrar = true; // booleano, cierto mientras no se encuentre el nodo
		boolean aniadir = true; // booleano, cierto si hay que aniadir el hijo
		while( iter.hasNext() && sinEncontrar ) { // recorremos la frontera buscando repetido
			Nodo<Estado,Accion> nf = iter.next(); // nodo en frontera
			if( nf.equals(hijo) ) { // nf es un nodo para el mismo estado que hijo
				sinEncontrar=false; // no necesitamos seguir buscando
				hijo.setH(nf.getH()); // copiamos el valor del heuristico para el estado del hijo
				if( comp.compare(nf,hijo) > 0 ) // el nodo que esta en la frontera es "peor" que el hijo que se ha generado
					iter.remove(); // hay que borrar el nodo en la frontera para aniadir al hijo
				else// el nodo en la frontera es "mejor" que el hijo,
					aniadir = false; // no hay que aniadir al hijo
			}
		}
		if( sinEncontrar ) hijo.setH(h.calculaH(hijo.getEstado())); // no estaba en la frontera, calculamos h
		return aniadir;
	} // fin aniadeSiNoRepeOMejorEnFrontera
		
		
	

}
