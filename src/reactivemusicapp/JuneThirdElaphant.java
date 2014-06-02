package reactivemusicapp;

import processing.core.PFont;
import processing.core.PImage;

public class JuneThirdElaphant extends ReactiveMusicApp
{

    PFont font;

    // Mode 3
    int xspacing = 10;
    int w;
    float theta = 0.0f;
    float amplitude = 75.0f;
    float period = 500.0f;
    float dx;
    float[] yvalues;

    boolean trigger = false;
    int triggerHold = 255;
    int triggerCount = 1;
    PImage mod;

    int[] modSizes;

    protected void setupImpl()
    {
        // Your setup work should go here
        size(1024,768, P3D);
        font = loadFont("Futura-Heavy-200.vlw");
        textFont(font, 200);
        mode = 5;

        // Mode 3
        w = width * 2;
        dx = (TWO_PI / period) * xspacing;
        yvalues = new float[w/xspacing];

        // Mode 5
        mod = loadImage("mod.png");
        modSizes = new int[scaledBandLevels.length];
    }

    protected void drawImpl()
    {
        // Your draw work should go here
        if (mode == 0) {
            background(0);
        }
        else if (mode == 1) {
            // Invader
            background(0);
            noStroke();
            int triangleXDensity = 32;
            int triangleYDensity = 18;
            for (int i = 0; i < triangleXDensity + 100; i++) {
                for (int j = 0; j < triangleYDensity + 100; j++) {
                    float value = scaledBandLevels[(i % scaledBandLevels.length)];
                    float shapeSize = map(value, 0, 255, 0, 150);
                    int currentX = (width/triangleXDensity) * i;
                    int currentY = (int)(((height/triangleYDensity) * j));

                    if (subMode == 1) {
                        currentY = (int)(((height/triangleYDensity) * j) - (frameCount % height * 3));
                    }
                    else if (subMode == 3) {
                        currentY = (int)(((height/triangleYDensity) * j) - (frameCount % height * 6));
                    }

                    if (subMode == 4) {
                        shapeSize = map(value, 0, 255, 0, 500);
                    }

                    if ((i + j) % (int)random(1,5) == 0) {
                        fill(0, 255, 255);
                    }
                    else {
                        fill(234,100,255);
                    }
                    pushMatrix();
                    if (subMode == 2) {
                        translate(width/2, height/2);
                        rotate(radians(frameCount % value * 2));
                        translate(-width/2, -height/2);
                    }
                    triangle(currentX, currentY - shapeSize/2, currentX - shapeSize/2, currentY + shapeSize/2, currentX + shapeSize/2, currentY + shapeSize/2);
                    popMatrix();
                }
            }

        }
        else if (mode == 2) {
            // Mirror Ball
            background(0);
            translate(width/2, height/2, 200);
            noStroke();
            if (subMode == 0) {
                rotateY(frameCount * PI/800);
            }
            else if (subMode == 1 || subMode == 2) {
                rotateY(frameCount * PI/800);
                rotateX(frameCount * PI/100);
            }
            else if (subMode == 3) {
                rotateY(frameCount * PI/400);
            }

            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    rotateY((2*PI/200) * j);
                    pushMatrix();
                    rotateX((2*PI/200) * i);
                    translate(0,200);
                    if (j > 50 && j < 150) {
                        rotateX(PI/2);
                    }
                    else {
                        rotateX(-PI/2);
                    }
                    if (subMode == 2 || subMode == 3) {
                        fill(i * 2, 0, j, scaledBandLevels[i % scaledBandLevels.length] * 2);
                    }
                    else {
                        fill(255, scaledBandLevels[i % scaledBandLevels.length]);
                    }
                    rect(0,200,20,20);
                    popMatrix();
                }
            }
        }
        else if (mode == 3) {
            // End To Begin
            background(0);

            theta += userInput.left.level();

            if (scaledBandLevels[0] > amplitude) {
                amplitude += 10;
            }
            else if (amplitude > 1) {
                amplitude--;
            }

            float tempAngle = theta;
            for (int i = 0; i < yvalues.length; i++) {
                yvalues[i] = sin(tempAngle)*amplitude;
                tempAngle+=dx;
            }

            noStroke();

            for (int x = 0; x < yvalues.length; x++) {
                fill(255, leftFFT.getBand(x) * 4);
                if (subMode >= 3) {
                    translate(width/2, height/2, 200);
                    rotateX(frameCount * PI/100);
                }
                ellipse(x*xspacing - period/2, height/2+yvalues[x], scaledBandLevels[x % scaledBandLevels.length]/2, scaledBandLevels[x % scaledBandLevels.length]/2);
                if (subMode >= 1) {
                    ellipse(x*xspacing, height/2+yvalues[x] - height/4, scaledBandLevels[x % scaledBandLevels.length]/2, scaledBandLevels[x % scaledBandLevels.length]/2);
                    ellipse(x*xspacing, height/2+yvalues[x] + height/4, scaledBandLevels[x % scaledBandLevels.length]/2, scaledBandLevels[x % scaledBandLevels.length]/2);
                }
                if (subMode >= 2) {
                    ellipse(x*xspacing, height/2+yvalues[x] - height/8, scaledBandLevels[x % scaledBandLevels.length]/2, scaledBandLevels[x % scaledBandLevels.length]/2);
                    ellipse(x*xspacing, height/2+yvalues[x] + height/8, scaledBandLevels[x % scaledBandLevels.length]/2, scaledBandLevels[x % scaledBandLevels.length]/2);
                }
            }
        }
        else if (mode == 4) {
            // Life Support
            background(0);
            if (trigger) { 
                noStroke();
                fill(255, triggerHold - (triggerCount * 4));

                rect(0,0,width,height);
                if (triggerCount > triggerHold) {
                    trigger = false;
                    triggerCount = 1;
                }
                else {
                    triggerCount++;
                }
            }
        }
        else if (mode == 5) {
            background(255);
            if (subMode >= 1) {
                translate(width/2, 0);
                rotateY(frameCount * PI/200);
            }
            imageMode(CENTER);
            
            if (subMode <= 1) {
                for (int i = 0; i < scaledBandLevels.length; i++) {
                    if (modSizes[i] < scaledBandLevels[i]) {
                        modSizes[i]+=40;
                    }
                    else {
                        if (modSizes[i] > 0) {
                            modSizes[i]--;
                        }
                    }
                    image(mod, ((width/scaledBandLevels.length) * i) + ((width/scaledBandLevels.length)/2), (height/2), modSizes[i], modSizes[i]);
                }
            }

            if (subMode == 2) {
                for (int j = 0; j < 4; j++) {
                    rotateY(j * (PI/4));
                    for (int i = 0; i < scaledBandLevels.length; i++) {
                        if (modSizes[i] < scaledBandLevels[i]) {
                            modSizes[i]+=20;
                        }
                        else {
                            if (modSizes[i] > 0) {
                                modSizes[i]--;
                            }
                        }
                        image(mod, ((width/scaledBandLevels.length) * i) + ((width/scaledBandLevels.length)/2), (height/2), modSizes[i], modSizes[i]);
                    }
                }
            }
        }
        else if (mode == 6) {

        }
        else if (mode == 7) {

        }
        else if (mode == 8) {

        }
        else if (mode == 9) {
            background(0);
            noStroke();
            fill(234,100,255);
            String bandName = "elaphant";
            text(bandName, (width/2) - (textWidth(bandName)/2), (height/2));
        }
    }

    protected void keyPressedImpl()
    {
        // Your key controls should go here
        if (key == 'g') {
            trigger = true;
            triggerCount = 1;
        }
    }
}
