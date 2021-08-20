package net.entity;


public class DimensionsModel {


    int X;
    int Y;
    int Width;

    public DimensionsModel(int width, int height, int x, int y) {
        Width = width;
        Height = height;
        X = x;
        Y = y;
    }

    int Height;

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public int getWidth() {
        return Width;
    }

    public void setWidth(int width) {
        Width = width;
    }

    public int getHeight() {
        return Height;
    }

    public void setHeight(int height) {
        Height = height;
    }


}

