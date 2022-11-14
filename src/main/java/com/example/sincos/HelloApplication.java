package com.example.sincos;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;

public class HelloApplication extends Application {
    public static double cWidth;
    public static double cHeight;
    Canvas canvas;
    public static final int ECKEN = 6;

    public static void main(String[] args) {
        launch();

    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 450);
        canvas = (Canvas) scene.lookup("#canvas");
        cWidth = canvas.getWidth();
        cHeight = canvas.getHeight();
        stage.setTitle("Canvas");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setOnCloseRequest(
                new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent windowEvent) {
                        try {
                            stop();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        Platform.exit();
                        System.exit(0);
                    }
                }
        );
        stage.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                double time = 0;
                ArrayList<int[]> nEck = new ArrayList<>();
                int radius = 200;
                int speed = 1;

                while(true) {
                    nEck.clear();
                    for(int i = 0; i < ECKEN; i++){
                        if(i==0){
                            nEck.add(drawPixelOnCircle(time, 0, radius, speed));
                        } else {
                            nEck.add(drawPixelOnCircle(time, Math.PI*2/ECKEN*i, radius, speed));
                        }
                    }
                    for(int i = 0; i < nEck.size(); i++){
                        if(i+1 == nEck.size()){
                            drawLine(nEck.get(i)[0], nEck.get(i)[1], nEck.get(0)[0], nEck.get(0)[1], 1);
                        } else {
                            drawLine(nEck.get(i)[0], nEck.get(i)[1], nEck.get(i+1)[0], nEck.get(i+1)[1], 1);
                        }
                    }
                    delay(10);
                    clearDisplay();
                    time+=0.01;
                }
            }
        }).start();
    }

    void clearDisplay(){
        canvas.getGraphicsContext2D().clearRect(0, 0, cWidth, cHeight);
    }

    void drawPixel(int x, int y, Color color){
        canvas.getGraphicsContext2D().getPixelWriter().setColor(x, y, color);
    }

    void drawLine(double x1, double y1, double x2, double y2, double lineWidth){
        canvas.getGraphicsContext2D().setLineWidth(lineWidth);
        canvas.getGraphicsContext2D().strokeLine(x1, y1, x2, y2);
    }

    void delay(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    int[] drawPixelOnCircle(double time, double timeShift, double radius, double speed){
        int[] xy = new int[2];
        xy[0] = (int) (cWidth / 2 + radius * Math.cos((time + timeShift - Math.PI / 2) * speed));
        xy[1] = (int) (cHeight / 2 + radius * Math.sin((time + timeShift - Math.PI / 2) * speed));
        drawPixel(xy[0], xy[1], Color.BLACK);
        return xy;
    }
}