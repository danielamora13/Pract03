package busqueda.GPF;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import busqueda.Busqueda;
import busqueda.BusquedaPrimeroMejor;
import busqueda.Criterio;
import busqueda.Heuristico;
import busqueda.Nodo;
import busqueda.Problema;

public class TestFicheroA {

	public static void main( String[] argv) throws IOException {

		try (PrintWriter out =
				new PrintWriter(new FileWriter("AEstrella.txt"))) {
			out.println("Problema, h0, hM, hE, h0, hM, hE, h0, hM, hE, h0, hM, hE");

			for (int i = 0; i < 10; i++) {
				pruebaUnProblema("prob10x10-" + i + ".txt", out, "prob10x10-" + i + ".txt");
			}
			for (int i = 0; i < 10; i++) {
				pruebaUnProblema("prob10x15-" + i + ".txt", out, "prob10x15-" + i + ".txt");
			}
			for (int i = 0; i < 10; i++) {
				pruebaUnProblema("prob15x15-" + i + ".txt", out, "prob15x15-" + i + ".txt");
			}
		}
		
	}
	
	
	/**
	 * Metodo para probar todas las busquedas sobre un problema concreto
	 * @param nomFichProb, el nombre del fichero con el enunciado del problema
	 * @param out fichero en el que se escribe la solucion
	 * @param problema nombre del archivo con el problema
	 */
	private static void pruebaUnProblema( String nomFichProb , PrintWriter out, String problema) {
		// Problema a resolver
		ProblemaGPF p = new ProblemaGPF( nomFichProb );
		p.muestraProblema();

		// Creamos heuristicos
		Heuristico<EstadoGPF> h0 = new Heuristico<EstadoGPF>();
		Heuristico<EstadoGPF> hM = new HeuristicoGPFManhattan( p );
		Heuristico<EstadoGPF> hE = new HeuristicoGPFMalo(p);
		
		// Lanzamos diversas busquedas
		// se puede comentar o descomentar segun la busqueda que quiera lanzarse

		// BUSQUEDA A* CON HEURISTICO TRIVIAL (AUNQUE EN REALIDAD YA LA HEMOS HECHO)
		System.out.println("---------------------------");
		System.out.println("A* CON H0. Deberia encontrar solucion optima");
		Busqueda<EstadoGPF,AccionGPF> bAStar00 = new BusquedaPrimeroMejor<EstadoGPF,AccionGPF>( p, Criterio.f, h0 );
		List<Nodo<EstadoGPF,AccionGPF>> solAStar00 = bAStar00.busqueda();
		muestraSol( solAStar00, bAStar00, p );
		
		// BUSQUEDA A* CON HEURISTICO MANHATTAN
		System.out.println("---------------------------");
		System.out.println("A* CON MANHATTAN. Deberia encontrar solucion optima");
		Busqueda<EstadoGPF,AccionGPF> bAStarM = new BusquedaPrimeroMejor<EstadoGPF,AccionGPF>( p, Criterio.f, hM );
		List<Nodo<EstadoGPF,AccionGPF>> solAStarM = bAStarM.busqueda();
		muestraSol( solAStarM, bAStarM, p );
		
		// BUSQUEDA A* CON HEURISTICO MALO
		System.out.println("---------------------------");
		System.out.println("A* CON MALO. No deberia encontrar solucion optima");
		Busqueda<EstadoGPF,AccionGPF> bAStarE = new BusquedaPrimeroMejor<EstadoGPF,AccionGPF>( p, Criterio.f, hE );
		List<Nodo<EstadoGPF,AccionGPF>> solAStarE = bAStarE.busqueda();
		muestraSol( solAStarE, bAStarE, p );
		
		int nodos1 = bAStar00.nodosExplorados() + bAStar00.nodosEnFrontera();
		int nodos2 = bAStarM.nodosExplorados() + bAStarM.nodosEnFrontera();
		int nodos3 = bAStarE.nodosExplorados() + bAStarE.nodosEnFrontera();

		out.println(problema + ","+
				(int) bAStar00.costeSolucion()+ ","+(int) bAStarM.costeSolucion()+ ","+(int) bAStarE.costeSolucion()+ ","+
				bAStar00.nodosExplorados()+ ","+bAStarM.nodosExplorados()+ ","+bAStarE.nodosExplorados()+ ","+
				bAStar00.nodosEnFrontera()+ ","+bAStarM.nodosEnFrontera()+ ","+bAStarE.nodosEnFrontera()+ ","+
				nodos1+ ","+nodos2+ ","+nodos3);
		
		}

	/**
	 * Metodo para mostrar una solucion por pantalla
	 * @param sol, la solucion (la rama del arbol de busqueda correspondiente al camino entre el inicio y la meta)
	 * @param b, la busqueda realizada (para saber numero de nodos expandidos, en frontera...)
	 * @param p, el problema a resolver
	 */
	private static void muestraSol ( List<Nodo<EstadoGPF,AccionGPF>> sol, Busqueda<EstadoGPF,AccionGPF> b, Problema<EstadoGPF,AccionGPF> p ){
		if( sol==null ) // para evitar excepciones por punteros nulos
			System.out.println("No se ha encontrado solucion");
		else {
			System.out.println("");
			System.out.println("Coste de la solucion: "+b.costeSolucion());
			System.out.println("Nodos explorados: "+b.nodosExplorados());
			System.out.println("Nodos en frontera: "+b.nodosEnFrontera());
			int nodos = b.nodosExplorados() + b.nodosEnFrontera();
			System.out.println("Nodos en memoria: "+nodos);
		}
		
		
	}
}
