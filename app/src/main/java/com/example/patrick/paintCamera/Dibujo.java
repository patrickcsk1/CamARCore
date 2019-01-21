package com.example.patrick.paintCamera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dibujo extends View {

    public LinearLayout.LayoutParams parametros;
    public LinearLayout linearLayout;
    private Path path = new Path();
    private Path pathVector = new Path();
    private Paint pincelVector = new Paint();
    private Paint pincel = new Paint();

    private TextView texto;
    private List<Sensor> listaSensores;

    private Map<Integer,Objeto> puntos;
    private static final int FACTOR_PUNTOS = 3;
    private int contadorPtosLinea;
    int flag=0;

    public Dibujo(Context context) {
        super(context);
        init();
    }

    public Dibujo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Dibujo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setTexto(TextView textOut){
        texto = textOut;
        texto.setTextSize(12f);
    }

    public void setSensores(SensorManager sensorManager){
        this.listaSensores = sensorManager.getSensorList(Sensor.TYPE_ALL);
        Log.d("AAA", "setSensores: Nro de sensores: " + this.listaSensores.size());
        for (int i = 0; i < listaSensores.size(); i++) {
            Log.d("AAA", "setSensores: Sensor Nro "+ ((i<10)?"0"+i:i) + ": " + this.listaSensores.get(i));
        }
    }

    public void init(){
        pincel.setAntiAlias(true);
        pincel.setColor(Color.BLACK);
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeJoin(Paint.Join.ROUND);
        pincel.setStrokeWidth(8f);

        pincelVector.setAntiAlias(true);
        pincelVector.setColor(Color.RED);
        pincelVector.setStyle(Paint.Style.STROKE);
        pincelVector.setStrokeJoin(Paint.Join.ROUND);
        pincelVector.setStrokeWidth(10f);

        parametros = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        puntos = new HashMap<Integer, Objeto>();
        contadorPtosLinea = 0;

        this.setBackgroundColor(Color.TRANSPARENT);
//        this.setZ(5f);
   }

    public void limpiarPantalla(){
        texto.setText("");
        contadorPtosLinea = 0;
        flag=0;
        puntos.clear();
        path.reset();
        pathVector.reset();
        postInvalidate();
    }

    private void calculos(int inicial){
        int numPtos = contadorPtosLinea /FACTOR_PUNTOS;
        if(numPtos>=8){
            for (int i = inicial; i < puntos.size(); i++) {
                pathVector.addCircle(puntos.get(i+FACTOR_PUNTOS).getPtoX(),puntos.get(i+FACTOR_PUNTOS).getPtoY(),5f,Path.Direction.CW);
            }
        }
        pathVector.addCircle(puntos.get(inicial).getPtoX(),puntos.get(inicial).getPtoY(),15f,Path.Direction.CW);
        pathVector.addCircle(puntos.get(contadorPtosLinea-1).getPtoX(),puntos.get(contadorPtosLinea-1).getPtoY(),15f,Path.Direction.CW);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX,pointY;
        pointX = event.getX();
        pointY = event.getY();

        if (pointY >= viewHeight) {
            pointY = viewHeight;
        }

        int index = 0;
        puntos.put(index,new Objeto(pointX,pointY));
        float diferencia,prevX=0f,prevY=0f;
        String nombreEvento="";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                nombreEvento = "ACTION_DOWN";
                path.moveTo(pointX, pointY);
                path.addCircle(pointX,pointY,2f,Path.Direction.CW);
                prevX = pointX;
                prevY = pointY;
                index++;
                break;
            case MotionEvent.ACTION_MOVE:
                nombreEvento = "ACTION_MOVE";
                if (flag==0) {
                    diferencia = (float) Math.sqrt(Math.pow(pointX-prevX,2) + Math.pow(pointY-prevY,2));
//                    if(diferencia<0.05) inicial = index;
//                    else inicial = index-1;
                    flag=1;
                }
                contadorPtosLinea++;
                path.lineTo(pointX,pointY);
                index++;
                break;
            case MotionEvent.ACTION_UP:
                nombreEvento = "ACTION_UP";
//                if(index>1) calculos();
                puntos.clear();
                index=0;
                contadorPtosLinea=0;
//                inicial=0;
                flag=0;
                break;
        }
        String coordenada = "Event: " + nombreEvento;
        coordenada+= "\nSensor de orientacion: " + listaSensores.get(2).getName() + " - " + listaSensores.get(2).getPower();
        coordenada+= "\nSensor de proximidad: " + listaSensores.get(4).getName() + " - " + listaSensores.get(4).getPower();
        coordenada+= "\nX: " + pointX + "\nY: " + pointY + "\n";
        coordenada+= "Orientación: " + event.getOrientation() + "\nRawX: " + event.getRawX() + "\nRawY: " + event.getRawY() + "\nPressure: " + event.getPressure() ;
        texto.setText(coordenada);
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path,pincel);
        canvas.drawPath(pathVector,pincelVector);
    }

    int viewHeight = 0;
    int viewWidth = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("onMeasure", "onMeasure: " + getHeight());
        Log.d("onMeasure", "onMeasure 2: " + getMeasuredHeight());

        viewHeight = getMeasuredHeight();
        viewWidth = getMeasuredWidth();
    }
}
