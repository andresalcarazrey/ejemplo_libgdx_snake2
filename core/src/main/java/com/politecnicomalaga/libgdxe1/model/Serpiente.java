package com.politecnicomalaga.libgdxe1.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class Serpiente {
    //Atributos

    //Serpiente es una agregación de cuadrados
    private Cuadrado[] cuerpo; //Tenemos un array donde almacenamos los cuadrados. La cabeza, será uno de los cuadrados en uso
    private int iCabeza, iCola,  iTam; //índice de la cabeza y tamaño de la serpiente
    private Cuadrado.Direccion direccion;
    private Texture[] imagenes;
    private int iLadoCuadrado;
    private int iAnchoPantalla, iAltoPantalla;

    //Métodos
    public Serpiente(Texture[] imagenes, int iAnchoPantalla, int iAltoPantalla, int iLadoCuadrado) {

        //Calculamos el número máximo de cuadrados que caben en la pantalla
        int maxCuadrados = (iAnchoPantalla/iLadoCuadrado) * (iAltoPantalla/iLadoCuadrado);
        cuerpo = new Cuadrado[maxCuadrados];

        //Ya tenemos el array dimensionado. Ahora creamos la serpiente con al menos un cuadrado, la cabeza
        cuerpo[0] = new Cuadrado(iAnchoPantalla/2,iAltoPantalla/2,imagenes[0],iLadoCuadrado);
        cuerpo[1] = new Cuadrado(iAnchoPantalla/2,iAltoPantalla/2,imagenes[1],iLadoCuadrado);
        cuerpo[2] = new Cuadrado(iAnchoPantalla/2,iAltoPantalla/2,imagenes[2],iLadoCuadrado);
        cuerpo[1].moverse(Cuadrado.Direccion.DER); //Ponemos la cola al lado de la cabeza.
        cuerpo[2].moverse(Cuadrado.Direccion.DER);
        cuerpo[2].moverse(Cuadrado.Direccion.DER);
        iCabeza = 0;
        iCola = 2;
        iTam = 3;
        direccion = Cuadrado.Direccion.IZQ;
        this.imagenes = imagenes;
        this.iLadoCuadrado=iLadoCuadrado;
        this.iAltoPantalla = iAltoPantalla;
        this.iAnchoPantalla = iAnchoPantalla;
    }

    public void moverse() {
        //Podríamos mover cada uno de los cuadrados, pero eso es muy muy ineficiente. Mejor cogemos la cola, la cambiamos de sitio
        //y pasa a ser la cabeza.

        /*En un array podemos tener las siguientes configuraciones:
        Inicial: donde H es cabeza (head) y T es cola (tail).
        - H,T
        - T,H
        - Vuelta a empezar

        Una vez hemos crecido tamaño 3
        - H,X,T
        - X,T,H
        - T,H,X
        - Y repetimos...

        Una vez hemos crecido tamaño n
        - H,X,X,...,X,X,T
        - X,X,X,...,X,T,H
        - X,X,X,...,T,H,X
        ...
        - X,T,H,X,...,X,X
        - T,H,X,...,X,X,X
        - Vuelta a empezar.
        */

        //Por lo tanto la T siempre pasa a ser la H cuando hay movimiento
        //y la nueva T está una posición menos, salvo cuando la T estaba en el 0 que pasaría a la Tam-1
        //También, por supuesto, tenemos que modificar la posición de la T a la cabeza y mover el cuadrado

        //Calculamos la posición de la nueva Cola
        int nuevaCola = iCola-1;
        if (nuevaCola==-1) nuevaCola = iTam-1;

        //Ahora la cola la ponemos "encima de la cabeza"
        cuerpo[iCola].moverEncima(cuerpo[iCabeza]);
        //Movemos la nueva cabeza a su sitio
        cuerpo[iCola].moverse(direccion);

        //Actualizamos índices
        iCabeza = iCola; //La cola pasa a ser la cabeza
        iCola = nuevaCola; //actualizamos la nueva cola
    }

    public void crecer() {
        //Para crecer, tenemos que tener en cuenta que nuestra serpiente siempre tiene
        //que tener T y H a una distacia de 1 (array circular)

        //La idea es añadir un cuadrado justo detrás de la cola (en el array), que esté "delante" en la pantalla de la posición
        //original de H, actualizando después los índices.

        //Ejemplos
        //- H1,T ---> X,T,H2  (X es la antigua cabeza, H2 es el cuadrado "insertado" detrás de la T,
        // Copiando los valores de la antigua H y moviéndose.

        //- T,H ---> T,H2,H1 --> T,H2,X

        //- H,X,X,X,...,X,X,T --> H,X,X,X,...,X,X,T,NH --> X,X,X,X,...,X,X,T,H
        //- X,X,T,H,...,X,X,X --> X,X,T,NH,H,...,X,X,X --> X,X,T,NH,...,X,X,X
        // Por lo tanto, se trata de insertar una nueva cabeza detrás justo de T moviendo los cuadrados desde esa posición
        // de nueva cabeza (NH) hasta el final +1 posición hacia atrás

        Cuadrado nuevaCabeza = new Cuadrado(0,0,imagenes[(new Random().nextInt(5))],iLadoCuadrado);
        nuevaCabeza.moverEncima(cuerpo[iCabeza]); //Colocamos la cabeza nueva encima de la antigua (en la pantalla)
        nuevaCabeza.moverse(direccion); //la movemos a su sitio (en la pantalla)

        //Ahora toca "insertar" la nueva cabeza en su sitio, justo detrás de la cola
        //y actualizar índices
        //X,X,X,X,T,H,X,...,X,X,X --> X,X,X,X,T,#,X,...,X,X,X (# es la posición de T+1). Los demás se atrasan +1
        for(int i=iTam;i>iCola;i--) { //lo de detrás de la cola, se debe de atrasar una posición
            //Copiamos a partir de la T cada elemento en el siguiente
            cuerpo[i+1] = cuerpo[i];
        }
        //Ya tenemos el hueco
        cuerpo[iCola+1] = nuevaCabeza;
        //Ahora actualizamos índices.
        iCabeza = iCola+1;
        iTam++;

        //tachán!!!
    }

    public void draw(SpriteBatch sp) {
        //Para dibujar la serpiente dibujamos todos los cuadrados
        for(int i=0;i<iTam;i++) {
            cuerpo[i].draw(sp);
        }
    }

    public float getPosXCabeza() {
        return cuerpo[iCabeza].getfPosX();
    }


    public float getPosYCabeza() {
        return cuerpo[iCabeza].getfPosY();
    }

    public void setDireccion(Cuadrado.Direccion direccion) {
        this.direccion = direccion;
    }

    public Cuadrado.Direccion getDireccion() {
        return direccion;
    }

    public boolean estaViva() {
        //Respondemos false si nos hemos salido de las pantalla o si la cabeza toca al cuerpo
        return !(fueraDePantalla() || haChocado());
    }

    //Caso especial: un método privado porque es un apoyo del método público estaViva. No es necesario
    //usarlo desde fuera de la clase, por eso es privado
    private boolean fueraDePantalla() {
        return (getPosXCabeza()<0 || getPosXCabeza()+iLadoCuadrado>iAnchoPantalla || getPosYCabeza()<0 || getPosYCabeza()+iLadoCuadrado>iAltoPantalla);
    }

    private boolean haChocado() {
        //Tenemos que tener más de 4 cuadrados para poder chocar, y cuando se de esa condición, tenemos que mirar la cabeza
        //si colisiona contra el cuadrado de la posición en pantalla 5, 6, 7 ... hasta la cola

        if (iTam < 5) return false;

        //VAmos a ver si la cabeza colisiona con el cuerpo
        //En nuestro array circular, caben dos posibilidades:
        //A. Que la cola esté al final y la cabeza al principio -> lo más intuitivo
        //B. Que la cola no esté al final, por lo que la cabeza (H) está justo después y a partir de ahí tenemos que buscar
        //el quinto elemento y siguientes desde esa posición hasta el final y después empezar de nuevo en el 0.

        //Otra opción es crear una función que me traduzca la posición en "pantalla" a la posición del array.
        //Si quiero el "quinto" en la pantalla, por ejemplo, me pongo donde está la cabeza (es la 0), y sumo a
        // esa posición 4 puntos. Después hacemos el módulo con iTam y ¡Tachán!, tenemos lo que necesitamos
        //Esta solución es la planteada
        for(int i=4;i<iTam;i++) { //El bucle de cálculo de colisiones lo ponemos como si el array siempre fuera del tipo A
            if (cuerpo[iCabeza].colisiona(cuerpo[this.calculaIndiceReal(i)])) {
                return true;
            }
        }
        return false;

    }

    private int calculaIndiceReal(int indicePantalla) {
        return ((indicePantalla+iCabeza) % iTam);
    }
}
