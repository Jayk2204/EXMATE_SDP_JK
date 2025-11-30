package com.example.exmate_sdp.views.animations;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

public class ParticleView extends View {

    private static final int NUM_PARTICLES = 40;
    private float[] x = new float[NUM_PARTICLES];
    private float[] y = new float[NUM_PARTICLES];
    private float[] speedX = new float[NUM_PARTICLES];
    private float[] speedY = new float[NUM_PARTICLES];
    private float[] size = new float[NUM_PARTICLES];
    private boolean isAnimating = false;


    private Paint paint = new Paint();
    private Random random = new Random();

    public ParticleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParticles();
    }

    private void initParticles() {
        for (int i = 0; i < NUM_PARTICLES; i++) {
            x[i] = random.nextInt(1000);
            y[i] = random.nextInt(2000);

            speedX[i] = -1 + random.nextFloat() * 2;
            speedY[i] = -1 + random.nextFloat() * 2;

            size[i] = 4 + random.nextFloat() * 8;
        }

        paint.setColor(Color.parseColor("#44FFA040")); // Neon Golden
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < NUM_PARTICLES; i++) {
            canvas.drawCircle(x[i], y[i], size[i], paint);

            x[i] += speedX[i];
            y[i] += speedY[i];

            if (x[i] < 0 || x[i] > getWidth())
                speedX[i] *= -1;

            if (y[i] < 0 || y[i] > getHeight())
                speedY[i] *= -1;
        }

        invalidate(); // Keep animating
    }
    public void startAnimation() {
        isAnimating = true;
        invalidate();
    }

}
