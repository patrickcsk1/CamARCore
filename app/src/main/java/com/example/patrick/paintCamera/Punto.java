package com.example.patrick.paintCamera;

public class Punto {
    private float ptoX;
    private float ptoY;

    public Punto(float x, float y) {
        setPtoX(x);
        setPtoY(y);
    }

    public float getPtoX() {
        return ptoX;
    }

    public void setPtoX(float ptoX) {
        this.ptoX = ptoX;
    }

    public float getPtoY() {
        return ptoY;
    }

    public void setPtoY(float ptoY) {
        this.ptoY = ptoY;
    }
}
