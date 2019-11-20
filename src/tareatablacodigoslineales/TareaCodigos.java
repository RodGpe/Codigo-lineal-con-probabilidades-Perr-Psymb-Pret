/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tareatablacodigoslineales;

import java.util.Arrays;

/**
 *
 * @author Rodrigo
 */
public class TareaCodigos {

    /**
     * @param args the command line arguments
     */
    static int[][] G = {{1, 0, 1, 1}, {0, 1, 0, 1}};
    //static int[][] G = {{1,0,1,1,1}, {0,1,1,0,1}};
    //static int[][] G = {{1,0,1,1,0}, {0,1,1,0,1}};
    //static int[][] G = {{1, 0, 0, 0, 1, 1}, {0, 1, 0, 1, 0, 1}, {0, 0, 1, 1, 1, 0}};
    //static int[][] G = {{1,1,1,1,1}};
    //static int[][] G = {{1,0,0,0,0,1,1,1}, {0,1,0,0,1,0,1,1}, {0,0,1,0,1,1,0,1}, {0,0,0,1,1,1,1,0}};

    public static int n = G[0].length;
    public static int k = G.length;
    public static double p = (0.01);
    public static double q = (1 - p);

    public static void main(String[] args) {
        TareaCodigos tarea = new TareaCodigos();
        //tarea.imprimirArreglo2D(tarea.G);
        //tarea.generarPalabrasCodigo(tarea.G);
        //tarea.sumaVectores(tarea.G[0], tarea.G[1]);

        //aqui empiezan los calculos de probabilidades
        //------------inicia Perr
        int[][] palabrasCodigo = new int[(int) Math.pow(2, k)][n];
        int[][][] matriz3d = tarea.generarPalabrasCodigo(G);
        palabrasCodigo = matriz3d[0];
        double proba = 0.0;
        for (int i = 0; i < matriz3d.length; i++) {
            proba += tarea.probabilidadVector(matriz3d[i][0]);
        }
        System.out.println("Perr: " + (1 - proba));
        //System.out.println("" + tarea.probabilidadVector(matriz3d[0][0]));
        //-----------termina Perr
        //-----------inicia Psymb
        double ka = 0.0;
        double pSym = 0.0;
        double pSymAux = 0.0;
        for (int i = 1; i < matriz3d[0].length; i++) {
            for (int j = 0; j < matriz3d.length; j++) {
                pSymAux += tarea.probabilidadVector(matriz3d[j][i]);
            }
            pSymAux = (Fi(matriz3d[0][i], k))
                    * (pSymAux);
            pSym += pSymAux;
            pSymAux = 0;
        }
        pSym = pSym * (double) (1.0 / (double) k);
        System.out.println("Psymb: "+pSym);
        //-----------termina Psymb
        //-----------inicia Pundetected 
        double pUndetected = 0.0;
        //int [] A = new int[n];
        for (int i = 1; i < matriz3d[0].length; i++) {
            pUndetected += (tarea.probabilidadVector(palabrasCodigo[i]));
        }
        System.out.println("Pundetected: " + pUndetected);
        //-----------termina Pundetected
        //-----------inicia Pret
        double pRet = 0.0;
        pRet = (1 - Math.pow(q, (double) n)) - pUndetected;
        System.out.println("Pret: " + pRet);
        //-----------termina Pret
        //-----------inicia Distancia minima
        int distanciaMinima = n;
        for (int i = 0; i < matriz3d[0].length; i++) {
            if(tarea.pesoHamming(matriz3d[0][i])<distanciaMinima && tarea.pesoHamming(matriz3d[0][i])!=0 ){
                distanciaMinima=tarea.pesoHamming(matriz3d[0][i]);
            }
        }
        System.out.println("Distancia minima: "+ distanciaMinima);
        //-----------termina distancia minima
        double R = 0; //eficiencia
        R = (double)k / (double)n;
        System.out.println("Eficiencia: "+ R);
    }

    public int[][][] generarPalabrasCodigo(int[][] G) {
        int[][] palabrasCodigo = new int[(int) Math.pow(2, k)][n];
        imprimirArreglo2D(palabrasCodigo);
        int indice;
        int[] indiceBits = new int[k];
        int[] cero;
        for (int l = 0; l < (int) Math.pow(2, k); l++) {   //este es el for para llenar el arreglo de las palabras de codigo
            indice = l;
            for (int i = 0; i < k; i++) {   //este for crea un arreglo con los valores binarios de input
                indiceBits[k - 1 - i] = boolToInt((indice & (1 << i)) != 0);
            }
            cero = new int[n];
            for (int j = 0; j < indiceBits.length; j++) {
                if (indiceBits[j] == 1) {
                    cero = sumaVectores(cero, G[j]);
                }
            }
            imprimirArreglo1D(cero);
            palabrasCodigo[l] = cero; //aqui asigna el resultado de las combinaciones lineales 
            //agregarVectorAMatriz(l, cero, palabrasCodigo);
            System.out.println("---");
            imprimirArreglo2D(palabrasCodigo);
            System.out.println("---");
        }
        imprimirArreglo2D(palabrasCodigo);
        //--------pruebas de encontrar buscar vector----------------------------
        int[][][] matriz3d = new int[(int) Math.pow(2, n - k)][(int) Math.pow(2, k)][n];
        System.out.println("tamaño de matriz3d = " + matriz3d.length);
        System.out.println("tamaño de matriz3d[0] = " + matriz3d[0].length);
        matriz3d[0] = palabrasCodigo;
        int[] entrada = {0, 1, 0, 1};
        int[] cualquierVector = new int[n];
        int contador = 0;
        int contador2 = 1;
        int contadorPeso = 0;
        while (contadorPeso != matriz3d.length) {//while donde se construye la matriz estadar
            for (int l = 0; l < (int) Math.pow(2, n); l++) { //for para probar cada uno de los posibles vectores
                contador = l;
                for (int i = 0; i < n; i++) {   //este for crea un arreglo con los valores binarios de input
                    cualquierVector[n - 1 - i] = boolToInt((contador & (1 << i)) != 0);
                }
                System.out.println(Arrays.toString(cualquierVector));
                if (buscarVector(cualquierVector, matriz3d)) { //si encuentra el vector no debe hacer nada 

                } else { //pero si no lo encuentra entonces no esta calculada todavia esa clase lateral
                    if (pesoHamming(cualquierVector) == contadorPeso) {
                        matriz3d[contador2][0] = cualquierVector;
                        for (int i = 0; i < matriz3d[0].length; i++) {
                            matriz3d[contador2][i] = sumaVectores(matriz3d[0][i], cualquierVector);
                        }
                        contador2++;
                    }
                }
                imprimirArreglo3D(matriz3d);
                cualquierVector = new int[n];
            }
            contadorPeso++;
        }
        return matriz3d;
//        if (buscarVector(entrada, matriz3d)) {
//
//        }
        //--------fin de pruebas de buscar vector-------------------------------
    }

    public int[] sumaVectores(int[] vector1, int[] vector2) {
        int[] resultado = new int[vector1.length];
        for (int i = 0; i < vector1.length; i++) {
            resultado[i] = vector1[i] ^ vector2[i];
        }
        //imprimirArreglo1D(resultado);
        return resultado;
    }

    public void imprimirArreglo3D(int[][][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                for (int l = 0; l < matriz[0][0].length; l++) {
                    System.out.print(matriz[i][j][l]);
                }
                System.out.print(",    ");
            }
            System.out.println("");
        }
    }

    public void imprimirArreglo2D(int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                System.out.print(matriz[i][j]);
            }
            System.out.println("");
        }
    }

    public void imprimirArreglo1D(int[] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            System.out.print(matriz[i]);
        }
        System.out.println("");
    }

    public static int boolToInt(Boolean b) {
        return b ? 1 : 0;
    }

    public int[] llenarVectorCeros(int[] entrada) {
        for (int i = 0; i < entrada.length; i++) {
            entrada[i] = 0;
        }
        //int[] salida = entrada;
        return entrada;
    }

    public void agregarVectorAMatriz(int indice, int[] v1D, int[][] v2D) {
        for (int i = 0; i < v2D.length; i++) {
            v2D[indice][i] = v1D[i];
        }
    }

    public boolean buscarVector(int[] vector, int[][][] matriz3D) {
        for (int i = 0; i < matriz3D.length; i++) {
            for (int j = 0; j < matriz3D[0].length; j++) {
                if (Arrays.equals(vector, matriz3D[i][j])) {
                    //System.out.println(Arrays.toString(vector));
                    //System.out.println(Arrays.toString(matriz3D[i][j]));
                    System.out.println("encontre el vector en las posiciones " + i + "  " + j);
                    return true;
                }

            }
        }
        return false;
    }

    public int pesoHamming(int[] vector) {
        int contadorPeso = 0;
        for (int i = 0; i < vector.length; i++) {
            if (vector[i] != 0) {
                contadorPeso++;
            }
        }
        //System.out.println("peso de hamming es " + contadorPeso);
        return contadorPeso;
    }

    public double probabilidadVector(int[] vector) {
        double proba = 0;
        proba = (Math.pow(p, (double) pesoHamming(vector))) * (Math.pow(q, (double) (vector.length - pesoHamming(vector))));
        return proba;
    }

    static public double Fi(int[] vector, int k) {
        int contadorPeso = 0;
        for (int i = 0; i < k; i++) {
            if (vector[i] != 0) {
                contadorPeso++;
            }
        }
        //System.out.println("peso de hamming es " + contadorPeso);
        return (double) contadorPeso;
    }

}
