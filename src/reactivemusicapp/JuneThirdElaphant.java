package reactivemusicapp;

import java.util.ArrayList;

public class JuneThirdElaphant extends ReactiveMusicApp
{
    protected void setupImpl()
    {
        // Your setup work should go here
        size(1024,768, P3D);
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
                    float value = scaledBandLevels[i % scaledBandLevels.length];
                    float shapeSize = map(value, 0, 255, 0, 150);
                    int currentX = (width/triangleXDensity) * i;
                    int currentY = (int)(((height/triangleYDensity) * j));
                    
                    if (subMode == 1) {
                        currentY = (int)(((height/triangleYDensity) * j) - (frameCount % height * 3));
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

        }
        else if (mode == 4) {

        }
        else if (mode == 5) {

        }
        else if (mode == 6) {

        }
        else if (mode == 7) {

        }
        else if (mode == 8) {

        }
        else if (mode == 9) {

        }
    }

    protected void keyPressedImpl()
    {
        // Your key controls should go here
    }
}
