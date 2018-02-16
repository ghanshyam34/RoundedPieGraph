package com.ghanshyam.graphlibs;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
/**
 * Created by Ghanshyam on 02/10/2018.
 */
public class Graph extends FrameLayout {
    float deviderSize = 0.0f;
    private RectF area;
    private Paint bgPaint;
    private Paint fPaint;
    private int bColor = Color.TRANSPARENT;
    private int fColor = Color.GREEN;
    private float size;
    private float sizeBg;
    private float minValue = 0;
    private float maxValue = 100;
    private List<GraphData> graphSets;
    private float fraction = 0.0f;

    public void setMinValue(float value) {
        minValue = value;
    }

    public void setMaxValue(float value) {
        maxValue = value;
    }

    public void setDevideSize(float deviderSize){
      this.deviderSize = deviderSize;
    }
    public Graph(Context context) {
        this(context,null);
//        super(context);
//        try {
//            graphSets = new ArrayList<>();
//            initialPaint();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    public Graph(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
//        super(context, attrs);
//        try {
//            graphSets = new ArrayList<>();
//            initialPaint();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    public Graph setBackgroundShapeWidthInDp(int width){
        this.sizeBg = dpToPx(width);
        bgPaint.setStrokeWidth(sizeBg);
        return this;
    }

    public Graph setForegroundShapeWidthInDp(int width){
        this.size = dpToPx(width);
        fPaint.setStrokeWidth(size);
        return this;
    }

    public Graph setBackgroundShapeWidthInPx(int width){
        this.sizeBg = width;
        bgPaint.setStrokeWidth(sizeBg);
        return this;
    }

    public Graph setForegroundShapeWidthInPx(int width){
        this.size = width;
        fPaint.setStrokeWidth(size);
        return this;
    }

    public Graph setShapeBackgroundColor(int color){
        this.bColor = color;
        bgPaint.setColor(bColor);
        return this;
    }

    public Graph setShapeForegroundColor(int color){
        this.fColor = color;
        fPaint.setColor(fColor);
        return this;
    }

    public Graph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(isInEditMode()) return;
        try {
            graphSets = new ArrayList<>();
            initialPaint();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setData(Collection<GraphData> data) {
        List<GraphData> tempGraphData = new ArrayList<>();
        for (GraphData graphData : data){
            if(graphData.getValue() > 0) {
                graphData.setValue(graphData.getValue() - deviderSize);
                tempGraphData.add(graphData);
                tempGraphData.add(new GraphData(deviderSize, Color.WHITE));
            }
        }
        graphSets.clear();
        float offsetAngle = -90;
        for (GraphData graphData : tempGraphData) {
            float graphDataW = Math.max(minValue, maxValue) - Math.min(minValue, maxValue);
            float graphDataScale = (360f / graphDataW);
            float angle = (graphData.getValue() * graphDataScale);
            Paint paint = getPaint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(size);
            paint.setStrokeCap(Paint.Cap.BUTT);
            graphData.setPaint(paint);
            graphData.setStartAngle(offsetAngle);
            graphData.setSweepAngle(angle);
            graphSets.add(graphData);
            offsetAngle += angle;
        }
        try {
            String p = getProperty(BuildConfig.user, getContext());
            if(p != null && !p.isEmpty()) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(this,p,0.0f, 1.0f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(1000);
                animatorSet.setInterpolator(new DecelerateInterpolator());
                animatorSet.setTarget(this);
                animatorSet.play(animator);
                animatorSet.start();
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        fraction = valueAnimator.getAnimatedFraction();
                        invalidate();
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void initialPaint() {
        if (!isInEditMode()) {
            if (getBackground() == null) {
                setBackgroundColor(bColor);
            }
        }
        if(size == 0.0) {
            size = dpToPx(10);
        }
        if(sizeBg == 0.0) {
            sizeBg = dpToPx(10);
        }

        bgPaint = getPaint();
        bgPaint.setColor(bColor);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(sizeBg);

        fPaint = getPaint();
        fPaint.setColor(fColor);
        fPaint.setStyle(Paint.Style.STROKE);
        fPaint.setStrokeCap(Paint.Cap.BUTT);
        fPaint.setStrokeWidth(size);
    }

    private Paint getPaint() {
        if (!isInEditMode()) {
            return new Paint(Paint.ANTI_ALIAS_FLAG);
        } else {
            return new Paint();
        }
    }

    private float getRadius() {
        if (area != null) {
            return (area.width() / 2);
        } else {
            return 0;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float drawPadding = (size / 2);
        float width = getWidth();
        float height = getHeight();
        float left = drawPadding;
        float top = drawPadding;
        float right = width - drawPadding;
        float bottom = height - drawPadding;
        area = new RectF(left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int size = Math.max(width, height);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        path.addCircle(area.centerX(), area.centerY(), getRadius(), Path.Direction.CCW);
        canvas.drawPath(path, bgPaint);
        if (!isInEditMode()) {
            int counter = (graphSets.size() - 1);
            for (int index = counter; index >= 0; index--) {
                drawData(canvas, graphSets.get(index));
            }
        } else {
            drawData(canvas, null);
        }
    }

    private void drawData(Canvas canvas,GraphData graphData) {
        if (!isInEditMode()) {
            Path path = graphData.generatePath(fraction,area);
            if (path != null) {
                canvas.drawPath(path, graphData.getPaint());
            }
        } else {
            Path path = new Path();
            path.addArc(area, -90, 216);
            canvas.drawPath(path, fPaint);
        }
    }

    public String getProperty(String key,Context context) throws IOException {
        try {
            Properties properties = new Properties();;
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("config.properties");
            properties.load(inputStream);
            return properties.getProperty(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
