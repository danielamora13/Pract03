package busqueda.GPF;

import busqueda.Heuristico;

public class HeuristicoGPFMalo extends Heuristico<EstadoGPF> {
	private EstadoGPF meta; // para calcular la distancia Manhattan hay que conocer la meta
	private ProblemaGPF problema;
	/**
	 * Constructor
	 * @param prob, un objeto ProblemaGPF 
	 */
	public HeuristicoGPFMalo(ProblemaGPF prob) {
		setMeta(prob.getMeta());
		problema = prob;
	}

	/**
	 * @param m el EstadoGPF para guardar en el atributo
	 */
	public void setMeta(EstadoGPF m) {
		meta = m;
	}
	
	/**
	 * Heuristico Malo
	 * @param un Estado e (del problema de grid pathfinding)
	 * @return el valor h(e), que es un valor aleatorio de 0 a 1000
	 */	
	@Override
	public double calculaH(EstadoGPF e){
		
		return Math.random()*1000;

		}


}
