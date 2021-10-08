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

public class TestFicheroPrimeroMejor {

	public static void main( String[] argv) throws IOException {

		try (PrintWriter out =
				new PrintWriter(new FileWriter("BusquedaPrimeroMejor.txt"))) {
			out.println("Problema, h0, hM, hE, h0, hM, hE, h0, hM, hE, h0, hM, hE");
//			for (Persona p:personas) {
//				out.println(p.nombre()+" " +p.edad()+" "+p.altura());
//				//out.printf("%-35s   %3d   %5.2f %n", p.nombre(), p.edad(), p.altura());
//			}
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
	 * Metodo para probar todas las busquedas (informadas y ciegas) sobre un problema concreto
	 * @param nomFichProb, el nombre del fichero con el enunciado del problema
	 */
	private static void pruebaUnProblema( String nomFichProb , PrintWriter out, String problema) {
		// Problema a resolver
		ProblemaGPF p = new ProblemaGPF( nomFichProb );
		p.muestraProblema();

		// Creamos heuristicos
		Heuristico<EstadoGPF> h0 = new Heuristico<EstadoGPF>();
		Heuristico<EstadoGPF> hM = new HeuristicoGPFManhattan( p );
		Heuristico<EstadoGPF> hE = new HeuristicoGPFMalo(p);
		
		// aqui podria aniadirse el heuristico malo a programar
		
		// Lanzamos diversas busquedas
		// se puede comentar o descomentar segun la busqueda que quiera lanzarse

		// BUSQUEDA PRIMERO EL MEJOR VORAZ CON HEURISTICO TRIVIAL
		System.out.println("---------------------------");
		System.out.println("1o MEJOR VORAZ CON H0");
		Busqueda<EstadoGPF,AccionGPF> bGBF0 = new BusquedaPrimeroMejor<EstadoGPF,AccionGPF>( p, Criterio.h, h0 );
		List<Nodo<EstadoGPF,AccionGPF>> solGBF0 = bGBF0.busqueda();
		muestraSol( solGBF0, bGBF0, p );
		
		// BUSQUEDA PRIMERO EL MEJOR VORAZ CON HEURISTICO MANHATTAN
		System.out.println("---------------------------");
		System.out.println("1o MEJOR VORAZ CON MANHATTAN. ");
		Busqueda<EstadoGPF,AccionGPF> bGBFM = new BusquedaPrimeroMejor<EstadoGPF,AccionGPF>( p, Criterio.h, hM );
		List<Nodo<EstadoGPF,AccionGPF>> solGBFM = bGBFM.busqueda();
		muestraSol( solGBFM, bGBFM, p );
		
		// BUSQUEDA PRIMERO EL MEJOR VORAZ CON HEURISTICO MALO
		System.out.println("---------------------------");
		System.out.println("1o MEJOR VORAZ CON MALO. ");
		Busqueda<EstadoGPF,AccionGPF> bGBFE = new BusquedaPrimeroMejor<EstadoGPF,AccionGPF>( p, Criterio.h, hE );
		List<Nodo<EstadoGPF,AccionGPF>> solGBFE = bGBFE.busqueda();
		muestraSol( solGBFE, bGBFE, p );

		int nodos1 = bGBF0.nodosExplorados() + bGBF0.nodosEnFrontera();
		int nodos2 = bGBFM.nodosExplorados() + bGBFM.nodosEnFrontera();
		int nodos3 = bGBFE.nodosExplorados() + bGBFE.nodosEnFrontera();

		out.println(problema + ","+
				(int) bGBF0.costeSolucion()+ ","+(int) bGBFM.costeSolucion()+ ","+(int) bGBFE.costeSolucion()+ ","+
				bGBF0.nodosExplorados()+ ","+bGBFM.nodosExplorados()+ ","+bGBFE.nodosExplorados()+ ","+
				bGBF0.nodosEnFrontera()+ ","+bGBFM.nodosEnFrontera()+ ","+bGBFE.nodosEnFrontera()+ ","+
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
