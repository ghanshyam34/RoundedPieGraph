package com.ghanshyam.graphlibs;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
/**
 * Created by Ghanshyam on 02/10/2018.
 */
public class GraphData {
    private float value;
    private final int color;
    private Paint paint;
    private float startAngle;
    private float sweepAngle;
    float getValue() {
        return this.value;
    }

    public void setValue(float value){
        this.value = value;
    }

    int getColor() {
        return this.color;
    }

    void setPaint(Paint paint) {
        this.paint = paint;
        this.paint.setColor(color);
    }

    void setStartAngle(float angle) {
        this.startAngle = angle;
    }

    void setSweepAngle(float sweep) {
        this.sweepAngle = sweep;
    }

    float getStartAngle() {
        return this.startAngle;
    }

    float getSweepAngle() {
        return this.sweepAngle;
    }

    Paint getPaint() {
        return this.paint;
    }

    public GraphData(float value, int color) {
        this.value = value;
        this.color = color;
    }

    public Path generatePath(float animationProgress,RectF drawingArea) {
        float startAngle = -90;
        float dataAngle = (getStartAngle() + getSweepAngle());
        dataAngle -= startAngle;
        float sweepAngle = dataAngle * animationProgress;
        Path path = new Path();
        path.addArc(drawingArea,startAngle, sweepAngle);
        return path;
    }
}
