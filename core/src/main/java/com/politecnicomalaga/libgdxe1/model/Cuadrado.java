package com.politecnicomalaga.libgdxe1.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import javax.swing.Spring;

public class Cuadrado {

    //Atributos
    private float fPosX;
    private float fPosY;
    private Texture imagen;
    private int iLado;

    public enum Direccion {
        ARRIBA,ABAJO,IZQ,DER
    }


    //Métodos


    public Cuadrado(float fPosX, float fPosY, Texture imagen, int iLado) {
        this.fPosX = fPosX;
        this.fPosY = fPosY;
        this.imagen = imagen;
        this.iLado = iLado;
    }

    public float getfPosX() {
        return fPosX;
    }

    public float getfPosY() {
        return fPosY;
    }

    public Texture getImagen() {
        return imagen;
    }

    public void setImagen(Texture imagen) {
        this.imagen = imagen;
    }

    public int getiLado() {
        return iLado;
    }

    public void setiLado(int iLado) {
        this.iLado = iLado;
    }

    public void draw(SpriteBatch sp) {

        sp.draw(imagen,fPosX,fPosY,iLado,iLado);
    }

    public void moverse(Direccion d) {
        switch(d) {
            case ARRIBA: fPosY+=iLado;
            break;
            case ABAJO: fPosY-=iLado;
                break;
            case IZQ: fPosX-=iLado;
                break;
            case DER: fPosX+=iLado;
                break;
        }
    }

    public boolean colisiona(Cuadrado c) {
        return (fPosX==c.getfPosX() && fPosY==c.getfPosY());
    }

    public void moverEncima(Cuadrado c) {
        fPosX = c.getfPosX();
        fPosY = c.getfPosY();
    }
}
