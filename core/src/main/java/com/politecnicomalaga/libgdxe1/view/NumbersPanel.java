package com.politecnicomalaga.libgdxe1.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Clase PanelNumeros. Se trata de crear una estructura de datos de texture con los números del 0 al 9
 * precargados. Tendrá un método que dado un integer (ahora mismo sólo positivos) prepare un arraylist
 * de imágenes que puedan después pintarse en el SpriteBatch para formar ese número en pantalla
 * Se usará posteriormente para pintar la puntuación, vidas, etc en pantalla
 */

public class NumbersPanel {

    /////////////////////////////////////////////////////////////////////////////////////
    //
    //ESTADO
    //
    /////////////////////////////////////////////////////////////////////////////////////



    //Almacenaremos números de base en un array list "digitos"
    protected Array<Texture> numberList;


    //Almacenaremos los números necesarios para pintar el número asignado en otro arraylist
    protected Array<Texture> showedList;


    protected float fPosX;
    protected float fPosY;
    protected float fWidth;

    protected int iStoredValue;
    /////////////////////////////////////////////////////////////////////////////////////
    //
    //COMPORTAMIENTO
    //
    /////////////////////////////////////////////////////////////////////////////////////


    //CONSTRUCTORES
    public NumbersPanel(float pX, float pY, float nWidth) {
        fPosX = pX;
        fPosY = pY;
        fWidth = nWidth;

        //creamos el array de base y el array a pintar (con el 0)
        numberList = new Array<Texture>();
        for (int i = 0;i<10;i++) {
            Texture newDigit = new Texture("numbers/digito"   + String.valueOf(i) + ".png");
            numberList.add(newDigit);
        }

        showedList = new Array<Texture>();
        showedList.add(numberList.get(0));
        iStoredValue = 0;

    }

    //Resto de comportamientos

    //Método pintarse
    public void render(SpriteBatch miSB) {
        float pX,pY;
        pX = fPosX;
        pY = fPosY;
        for (Texture digit : showedList) {
            miSB.draw(digit,pX,pY, fWidth, fWidth);
            pX += fWidth;
        }

    }

    //Asignar el número:
    public void setData(int iValue) {

        if (iValue >= 0) {
            iStoredValue = iValue;
            showedList.clear();
            String sNumber = String.valueOf(iValue);
            for (int i=0; i<sNumber.length();i++) {
                String sDigit = String.valueOf(sNumber.charAt(i));

                showedList.add(numberList.get(Integer.valueOf(sDigit)));
            }
        }
    }

    //Método incremento
    public void increment(int iValue) {
        if (iValue > 0) {
            iStoredValue += iValue;
            showedList.clear();
            String sNumber = String.valueOf(iStoredValue);
            for (int i=0; i<sNumber.length();i++) {
                String sDigit = String.valueOf(sNumber.charAt(i));

                showedList.add(numberList.get(Integer.valueOf(sDigit)));
            }
        }
    }

    //Método dispose. Para eliminar los recursos
    public void dispose() {
        for ( Texture t : numberList) {
            t.dispose();
        }
    }
}


