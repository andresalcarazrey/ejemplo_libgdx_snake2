package com.politecnicomalaga.libgdxe1;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.politecnicomalaga.libgdxe1.model.Cuadrado;
import com.politecnicomalaga.libgdxe1.model.Serpiente;
import com.politecnicomalaga.libgdxe1.view.NumbersPanel;

import java.util.Random;

/*  AAR
    Esta es la clase de partida de todos los videojuegos Libgdx. Desde esta clase, que es llamada desde el lanzador
    de escritorio, de android, de html, etc... se llamarán a los demás objetos de la capa de modelo, vista o controlador
 */

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch; //Es la clase que representa una pantalla donde se pueden pintar imágenes
    //private Texture image;  //Esta es una instancia/objeto imagen
    private Texture[] player;
    private Texture endImage;
    private Texture portadaImage;
    //Aquí ponemos todas las Texture que necesitemos ahora mismo en el videojuego
    //Además todas las variables (son realmente atributos de Main) que necesitemos
    private int iPosXClicked;
    private int iPosYClicked;
    private boolean bGanamos;

    // serpiente
    private Serpiente snaky;

    //Primera aproximación al control del tiempo. Contador entero básico
    private int contador;

    //Panel de puntuación:
    private NumbersPanel panelPuntos;

    @Override
    public void create() {
        /* AAR
            Método de inicialización de los atributos. Hace la función de "constructor", pero sin serlo. El constructor
            ApplicationAdapter es el encargado de llamar a este método. Lo veremos en profundidad cuando estudiemos herencia
         */
        batch = new SpriteBatch();
        player = new Texture[5];
        for (int i = 0;i<5;i++) {
            player[i] = new Texture("snake_body" + (i+1) + ".png");
        }
        endImage = new Texture("end.png");
        portadaImage = new Texture("portada.png");
        bGanamos = true;
        snaky = new Serpiente(player,Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),20);
        contador = 0;

        panelPuntos = new NumbersPanel(Gdx.graphics.getWidth()/10,Gdx.graphics.getHeight()/10, 30);

    }

    @Override
    public void render() {

        /*  AAR
            Método corazón de todos los videojuegos Libgdx
            Aquí solemos tener:
             - control de entrada
             - simulación del mundo
             - control de cambios
             - dibujar el mundo

            Sabemos que se va a ejecutar una y otra vez, una y otra vez, hasta que alguién cierre la app
         */

        //------------------------------
        //Control de entrada
        //------------------------------

        if (Gdx.input.justTouched()) {
            //Si entramos aquí es que se ha tocado/clicado la pantalla entre el anterior "render" y este
            iPosXClicked = Gdx.input.getX();
            iPosYClicked = Gdx.input.getY();

            if (snaky.getDireccion()== Cuadrado.Direccion.ABAJO || snaky.getDireccion() == Cuadrado.Direccion.ARRIBA) {
                if (iPosXClicked< snaky.getPosXCabeza()) {
                    snaky.setDireccion(Cuadrado.Direccion.IZQ);
                    //Ibamos arriba o abajo, ahora a la izquierda
                }
                else {
                    snaky.setDireccion(Cuadrado.Direccion.DER);; //Han tocado por la derecha...
                }
            } else {
                if (Gdx.graphics.getHeight()-iPosYClicked< snaky.getPosYCabeza()) {
                    snaky.setDireccion(Cuadrado.Direccion.ABAJO);
                    //Ibamos izq o der ahora abajo
                }
                else {
                    snaky.setDireccion(Cuadrado.Direccion.ARRIBA);
                }
            }

            //Control del fin de partida
            if (bGanamos) {
                bGanamos = false;
                //La antigua serpiente, al quedarse sin referencia, desaparece y se recicla
                snaky = new Serpiente(player,Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),20);
                contador = 0;
                panelPuntos.setData(0);
            }
        }

        //------------------------------
        //Simulación del mundo
        //------------------------------
        //Dependiendo de la dirección, tenemos que actualizar las posiciones del jugador.
        if (!bGanamos) {
            /*switch (iDireccion) {
                case 0: //arriba
                    fPosYPlayer+=fVelPlayer;
                    break;
                case 1: //abajo
                    fPosYPlayer-=fVelPlayer;
                    break;
                case 2: //izquierda
                    fPosXPlayer-=fVelPlayer;
                    break;
                case 3:
                    fPosXPlayer+=fVelPlayer;
                    break;
            }*/
            contador++; //Actualizamos el número de frames que llevamos
            //Toca moverse, quizás, o crecer
            if (contador % 20 == 0) {
                //Si hemos pasado 4 veces (240 frames) entonces crecemos y reseteamos
                if (contador == 200) {
                    contador = 0;
                    snaky.crecer();
                    panelPuntos.increment(1);
                } else
                    snaky.moverse();
            }

            //Evitamos que se salga
            /*if (fPosXPlayer>Gdx.graphics.getWidth()-image.getWidth()) fPosXPlayer = Gdx.graphics.getWidth()-image.getWidth();
            if (fPosYPlayer>Gdx.graphics.getHeight()-image.getWidth()) fPosYPlayer = Gdx.graphics.getHeight()-image.getWidth();
            if (fPosXPlayer<0) fPosXPlayer = 0;
            if (fPosYPlayer<0) fPosYPlayer = 0;*/
        }

        //También simulamos el "cambio" o "salto" de la imagen a perseguir
        /*if (!bGanamos && Math.random()>0.999) {//0,1% de posibilidades de "saltar" en cada frame
            Random dado = new Random();
            iPosXImagen = dado.nextInt(Gdx.graphics.getWidth());
            iPosYImagen = dado.nextInt(Gdx.graphics.getHeight());

        }*/

        //------------------------------
        //Control de cambios
        //------------------------------

        //Desactivamos el control de estado del juego inicial
        //Si han colisionado, hemos ganado
        if (!snaky.estaViva()) {
            //ganamos
            bGanamos = true;

        }



        //------------------------------
        // Dibujar
        //------------------------------

        //Dibujar. Es donde hacemos que el "mundo" del videojuego muestre sus datos al jugador
        //clear. Se trata de limpiar la pantalla. Siempre antes de empezar a dibujar cualquier cosa
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        //Rutina típica de dibujado. Hay que llamar obligatoriamente a begin y a end
        batch.begin();

        //Aquí los draw...
        if(bGanamos) {
            if (contador == 0) {
                batch.draw(portadaImage, 80, 0,500,500);
            } else {
                batch.draw(endImage, 80, 0);
                panelPuntos.render(batch);
            }
        } else {
            snaky.draw(batch);
            panelPuntos.render(batch);
        }


        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        //image.dispose();
        for (int i = 0;i<5;i++) {
            player[i].dispose();
        }
        endImage.dispose();
        panelPuntos.dispose();
        portadaImage.dispose();
    }


    //Función colisiona. Determina cuando dos rectángulos están solapados en un espacio 2D
    public boolean colisionan(float fPosX1, float fPosY1, float fPosX2, float fPosY2, float fLado) {
        //Lado es el ancho y alto de los cuadrados que representan al jugador y a la imagen.
        //dos cuadrados se solapan parcial o totalmente si se solapan en el eje X y en el eje Y a la vez
        //un solapamiento en X implica que x1 y x2 no estén más lejos que el tamaño del lado
        //En Y, es lo mismo.
        return (Math.abs(fPosX1-fPosX2)<fLado && Math.abs(fPosY1-fPosY2)<fLado);
    }
}
