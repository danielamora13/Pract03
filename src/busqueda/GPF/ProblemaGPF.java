/**
 * Clase Problema para Grid Path Finding (practica de Int Sist Inteligentes)
 */
package busqueda.GPF;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import busqueda.Problema;

/**
 * @author Ines y ??
 * @version 2021.09.*
 *
 */
public class ProblemaGPF extends Problema<EstadoGPF,AccionGPF> {
	// atributos
	private int[][] grid; // cuadricula
	private int gridNFilas; // dimensiones cuadricula
	private int gridNCols;
	private EstadoGPF inicio; // estado inicial
	private EstadoGPF meta; // estado final

	// CONSTRUCTORES
	/**
	 * Constructor a partir de datos de un problema
	 * @param g, la cuadricula
	 * @param iniX, fila de la posicion de inicio
	 * @param iniY, columna de la posicion de inicio
	 * @param metaX, fila de la posicion meta
	 * @param metaY, columna de la posicion meta
	*/
	public ProblemaGPF(int[][] g, int iniX, int iniY, int metaX, int metaY) {
		setGrid( g );
		setGridNFilas( g.length );
		int nc = (g.length>0? g[0].length : 0);
		setGridNCols( nc );
		setInicio( iniX,iniY );
		setMeta( metaX, metaY );
	}
	
	/**
	 * Constructor a partir de fichero de texto
	 * @param el nombre el fichero, un String
	 */
	public ProblemaGPF( String fichProblema ){
		leeDeFichero( fichProblema );
	}
	
	/**
	 * Constructor "aleatorio" a partir de parametros
	 * @param nFilas, numero de filas de la cuadricula
	 * @param nCols, numero de columnas
	 * @param pObstaculo, la probabilidad con que una casilla sera un obstaculo
	 * @param costeMin, coste minimo de transitar por una casilla
	 * @param costeMax, coste maximo de transitar por una casilla 
	*/
	public ProblemaGPF(int nFilas, int nCols, double pObstaculo, int costeMin, int costeMax) {
		int[][] g = new int[nFilas][nCols];
		Random r = new Random(); // generador de numeros aleatorios
		// posicion inicial
		int iniX = r.nextInt(nFilas);
		int iniY = r.nextInt(nCols);
		// meta
		int metaX = r.nextInt(nFilas);
		int metaY = r.nextInt(nCols);
		// grid
		for ( int i=0; i<nFilas; i++ ){
			for( int j=0; j<nCols; j++ )
				// bloqueo casilla (no ini y no meta) con probabilidad pObstaculo
				if( i!=iniX && i!=metaX && j!=iniY && j!=metaY && (r.nextDouble()<=pObstaculo) )
					g[i][j]=0;
				else // si no, coste entre costeMin y costeMax
					g[i][j]=costeMin+r.nextInt(costeMax);
		}
		// copiar en atributos
		setGrid( g );
		setGridNFilas( nFilas );
		setGridNCols( nCols );
		setInicio( iniX,iniY );
		setMeta( metaX, metaY );
	}
	
	
	// MODIFICADORES Y OBSERVADORES
	/**
	 * @return the grid
	 */
	public int[][] getGrid() {
		return grid;
	}

	/**
	 * @param grid the grid to set
	 */
	public void setGrid(int[][] grid) {
		this.grid = grid;
	}

	/**
	 * @return the gridNFilas
	 */
	public int getGridNFilas() {
		return gridNFilas;
	}

	/**
	 * @param gridNFilas the gridNFilas to set
	 */
	public void setGridNFilas(int gridNFilas) {
		this.gridNFilas = gridNFilas;
	}

	/**
	 * @return the gridNCols
	 */
	public int getGridNCols() {
		return gridNCols;
	}

	/**
	 * @param gridNCols the gridNCols to set
	 */
	public void setGridNCols(int gridNCols) {
		this.gridNCols = gridNCols;
	}
	
	/**
	 * @param x, fila de posicion de inicio
	 * @param y, columna de posicion de inicio
	 */
	private void setInicio(int x, int y) {
		if( 0<=x && x < getGridNFilas() && 0<= y && y < getGridNCols() )
			inicio = new EstadoGPF(x,y);
	}
	
	/**
	 * Observador para conocer el estado meta
	 * @return el estado meta
	 */
	public EstadoGPF getMeta(){
		return meta;
	}
	
	/**
	 * @param x, fila de posicion de inicio
	 * @param y, columna de posicion de inicio
	 * @return 
	 */
	private void setMeta(int x, int y) {
		if( 0<=x && x < getGridNFilas() && 0<= y && y < getGridNCols() )
			meta = new EstadoGPF(x,y);
	}

	// METODOS HERADOS DE LA CLASE ABSTRACTA Problema QUE HAY QUE IMPLEMENTAR

	/* (non-Javadoc)
	 * @see busqueda.Problema#getInicio()
	 */
	@Override
	public EstadoGPF getInicio() {
		return inicio;
	}

	/*
	 * (non-Javadoc)
	 * @see busqueda.Problema#acciones(busqueda.Estado)
	 */
	@Override
	public List<AccionGPF> acciones(EstadoGPF eactual) {
		List<AccionGPF> lista = new LinkedList<AccionGPF>();
		AccionGPF accionArriba = new AccionGPF(Acciones.ARRIBA);
		AccionGPF accionAbajo = new AccionGPF(Acciones.ABAJO);
		AccionGPF accionIzquierda = new AccionGPF(Acciones.IZQUIERDA);
		AccionGPF accionDerecha = new AccionGPF(Acciones.DERECHA);
		if(aplicable(eactual, accionArriba)) {
			lista.add(accionArriba);
		}
		if(aplicable(eactual, accionAbajo)) {
			lista.add(accionAbajo);
		}
		if(aplicable(eactual, accionDerecha)) {
			lista.add(accionDerecha);
		}
		if(aplicable(eactual, accionIzquierda)) {
			lista.add(accionIzquierda);
		}
		return lista;
	}
	
	/*
	 * (non-Javadoc)
	 * @see busqueda.Problema#aplicable(Estado, Accion)
	 */
	@Override
	public boolean aplicable(EstadoGPF e, AccionGPF a) {
		if (!(e.getX() >= 0 && e.getY() >= 0 && e.getX() <= gridNFilas-1 && 
				e.getY() <= gridNCols-1)) {
			return false;
		}
		

		switch (a.getAcciones()) {
			case ARRIBA:
				if (e.getX() > 0 && grid[e.getX() - 1][e.getY()] != 0) {
					return true;
				}
			case ABAJO:
				if (e.getX() < gridNFilas - 1 && grid[e.getX() + 1][e.getY()] != 0) {
					return true;
					}
			case IZQUIERDA:
				if (e.getY() > 0 && grid[e.getX()][e.getY() - 1] != 0) {
					return true;
				}
			case DERECHA:
				if (e.getY() < gridNCols - 1 && grid[e.getX()][e.getY() + 1] != 0) {
					return true;
				}
			}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see busqueda.Problema#resul(busqueda.Estado, busqueda.Accion)
	 */
	@Override
	public EstadoGPF resul(EstadoGPF e, AccionGPF a) {
		// TODO
		EstadoGPF nuevoEstado;
		
		if (!aplicable(e,a)) {
			nuevoEstado = null;
		}
		switch (a.getAcciones()) {
		case ARRIBA:
			nuevoEstado = new EstadoGPF(e.getX() - 1, e.getY());
			break;
			
		case ABAJO:
			nuevoEstado = new EstadoGPF(e.getX() + 1, e.getY());
			break;
			
		case IZQUIERDA:
			nuevoEstado = new EstadoGPF(e.getX(), e.getY() - 1);
				break;
				
		case DERECHA:
			nuevoEstado = new EstadoGPF(e.getX(), e.getY() + 1);
				break;
		
		default:
			nuevoEstado = null;
			break;	
		}
		return nuevoEstado; 
	}
	
	/* (non-Javadoc)
	 * @see busqueda.Problema#esMeta(Estado)
	 */
	@Override
	public boolean esMeta(EstadoGPF e) {
		return e.equals( meta );
	}
	
	/*
	 * (non-Javadoc)
	 * @see busqueda.Problema#coste(Estado, Accion, Estado)
	 */
	@Override
	public double coste(EstadoGPF e1, AccionGPF a, EstadoGPF e2) {
		if (e2.getX() >= 0 && e2.getY() >= 0 && e2.getX() <= gridNFilas-1 && 
				e2.getY() <= gridNCols-1) {
			return coste(e2);
		}
		// TODO
		// Tened en cuenta que, en el problema GPF, el coste solo depende de uno de los estados
		return 0;
	}


	// METODOS PRIVADOS (LOS QUE HACEN "EL TRABAJO SUCIO")
	
	

	/* 
	 * coste de transitar por un estado/celda (independiente de donde se viene o mediante que accion)
	 */
	private double coste( EstadoGPF e2 ) {
		return grid[e2.getX()][e2.getY()];
	}
	/**
	 * Lee el problema de un fichero con formato determinado como sigue
	 *  (cuando pone $ se refiere a una variable):
	 * NUMERO DE FILAS
	 * $NF
	 * NUMERO DE COLUMNAS
	 * $NC
	 * INICIO
	 * $Xini $Yini
	 * META
	 * $Xmeta $Ymeta
	 * CUADRICULA
	 * $Array
	 * 
	 * @param fichProblema, un String con el nombre del fichero
	 */
	private void leeDeFichero(String fichProblema) {
		Scanner s =null;
		try{
			s = new Scanner(new BufferedReader( new FileReader(fichProblema)));
			// leemos primeras lineas (num filas y columnas, inicio y meta) 
			int num, num2 = 0;
			s.nextLine();// comentario "NUMERO DE FILAS"
			num=s.nextInt(); // numero de filas
			s.nextLine();// saltamos linea
			setGridNFilas(num);
			s.nextLine();// comentario "NUMERO DE COLUMNAS"
			num=s.nextInt(); // numero de columnas
			s.nextLine();// saltamos linea
			setGridNCols(num);
			s.nextLine();// comentario "INICIO"
			num=s.nextInt(); // fila de inicio
			num2=s.nextInt(); // columna de inicio
			s.nextLine();// saltamos linea
			setInicio(num,num2);
			s.nextLine();// comentario "META"
			num=s.nextInt(); // fila de META
			num2=s.nextInt(); // columna de META
			setMeta(num,num2);
			s.nextLine();// saltamos linea
			// leemos cuadricula
			s.nextLine();// comentario "CUADRICULA"
			grid = new int[getGridNFilas()][getGridNCols()];
			for( int i=0; i<getGridNFilas();i++ ){
				for( int j=0; j<getGridNCols(); j++ )
					grid[i][j]=s.nextInt();
				if( i<getGridNFilas()-1) s.nextLine();
			}
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage() );
			e.printStackTrace();
		} finally{
			if( s!= null )
				s.close();
		}	
	}
	
	/**
	 * Metodo "chapuza" para mostrar el problema por pantalla
	 */
	public void muestraProblema(){
		System.out.println("NUMERO DE FILAS");
		System.out.println(this.getGridNFilas());
		System.out.println("NUMERO DE COLUMNAS");
		System.out.println(this.getGridNCols());
		System.out.println("INICIO");
		System.out.println(this.getInicio().toString());
		System.out.println("FIN");
		System.out.println(this.getMeta().toString());
		System.out.println("CUADRICULA");
		for( int i=0; i<getGridNFilas();i++ ){
			for( int j=0; j<getGridNCols(); j++ )
				System.out.print( grid[i][j]+" " );
			System.out.println();
		}
	}

	public void escribeEnFichero(String nomFichProb) throws IOException {
		PrintWriter out = new PrintWriter(new FileWriter( nomFichProb ));
		escribeProblema(out);
		out.close();
	}
	
	private void escribeProblema( PrintWriter out ){
	out.println("NUMERO DE FILAS");
	out.println(this.getGridNFilas());
	out.println("NUMERO DE COLUMNAS");
	out.println(this.getGridNCols());
	out.println("INICIO");
	out.println(this.getInicio().getX()+" "+this.getInicio().getY());
	out.println("FIN");
	out.println(this.getMeta().getX()+" "+this.getMeta().getY());
	out.println("CUADRICULA");
	for( int i=0; i<getGridNFilas();i++ ){
		for( int j=0; j<getGridNCols(); j++ )
			out.print( grid[i][j]+" " );
		out.println();
	}
	}





}
