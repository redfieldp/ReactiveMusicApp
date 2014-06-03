package reactivemusicapp;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class JuneThirdElaphant extends ReactiveMusicApp
{

    PFont font;

    // Mode 3
    int xspacing = 10;
    int w;
    float theta = 0.0f;
    float amplitude = 200.0f;
    float period = 500.0f;
    float dx;
    float[] yvalues;

    boolean trigger = false;
    int triggerHold = 255;
    int triggerCount = 1;
    PImage mod;

    int[] modSizes;

    String[] letterStrings = {"E", "L", "A", "P", "H", "A", "N", "T"};

    protected void setupImpl()
    {
        // Your setup work should go here
        font = loadFont("Futura-CondensedExtraBold-200.vlw");
        textFont(font, 200);
        mode = 0;

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
        if (mode == 2) {
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
        else if (mode == 5) {
            // Mirror Ball
            background(0);
            translate(width/2, height/2, 500);
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
        else if (mode == 8) {
            // End To Begin
            background(0);

            for (int i = 0; i < scaledBandLevels.length; i++) {
                if (modSizes[i] < scaledBandLevels[i] && modSizes[i] < 60) {
                    modSizes[i]+=2;
                }
                else  if (modSizes[i] > 5) {
                    modSizes[i]--;
                }
            }
            
            theta += .2;

            if (subMode >=3) {
                theta += .6;
            }

            float tempAngle = theta;
            for (int i = 0; i < yvalues.length; i++) {
                yvalues[i] = sin(tempAngle)*amplitude;
                tempAngle+=dx;
            }

            noStroke();
            if (subMode >= 4) {
                translate(0, height/2);
            }
            int currentFrameCount = frameCount;
            for (int x = 0; x < yvalues.length; x++) {
                if (subMode >= 4) {
                    fill(0,random(128,255),255, scaledBandLevels[x % scaledBandLevels.length] * 3);
                    rotateX(currentFrameCount * (PI/200));
                }
                else {
                    fill(0,random(128,255),255, scaledBandLevels[x % scaledBandLevels.length]);
                }
                ellipse(x*xspacing - period/2, height/2+yvalues[x], modSizes[x % modSizes.length], modSizes[x % modSizes.length]);
                if (subMode >= 1) {
                    ellipse(x*xspacing, height/2+yvalues[x] - height/4, modSizes[x % modSizes.length], modSizes[x % modSizes.length]);
                    ellipse(x*xspacing, height/2+yvalues[x] + height/4, modSizes[x % modSizes.length], modSizes[x % modSizes.length]);
                }
                if (subMode >= 2) {
                    ellipse(x*xspacing, height/2+yvalues[x] - height/8, modSizes[x % modSizes.length], modSizes[x % modSizes.length]);
                    ellipse(x*xspacing, height/2+yvalues[x] + height/8, modSizes[x % modSizes.length], modSizes[x % modSizes.length]);
                }
            }
        }
        else if (mode == 1) {
            // Life Support
            background(0);
            noStroke();
            if (trigger) { 
                if (subMode == 0) {
                    fill(255, 0, 0, triggerHold - (triggerCount * 4));
                }
                else if (subMode == 1) {
                    fill(255, triggerHold - (triggerCount * 4));
                }
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
        else if (mode == 6) {
            // Stacking Cards
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
                    else if (modSizes[i] > 40) {
                        modSizes[i]--;
                    }
                    image(mod, ((width/scaledBandLevels.length) * i) + ((width/scaledBandLevels.length)/2), (height/2), modSizes[i], modSizes[i]);
                }
            }
            else if (subMode == 2) {
                for (int i = 0; i < scaledBandLevels.length; i++) {
                    if (modSizes[i] < scaledBandLevels[i]) {
                        modSizes[i]+=20;
                    }
                    else  if (modSizes[i] > 40) {
                        modSizes[i]--;
                    }
                }
                for (int j = 0; j < 4; j++) {
                    rotateY(j * (PI/4));
                    for (int i = 0; i < scaledBandLevels.length; i++) {
                        image(mod, ((width/scaledBandLevels.length) * i) + ((width/scaledBandLevels.length)/2), (height/2), modSizes[i], modSizes[i]);
                    }
                }
            }
            else if (subMode == 3) {
                for (int i = 0; i < scaledBandLevels.length; i++) {
                    if (modSizes[i] < scaledBandLevels[i]) {
                        modSizes[i]+=20;
                    }
                    else  if (modSizes[i] > 40) {
                        modSizes[i]--;
                    }
                }
                for (int j = 0; j < 8; j++) {
                    rotateY(j * (PI/8));
                    for (int i = 0; i < scaledBandLevels.length; i++) {
                        image(mod, ((width/scaledBandLevels.length) * i) + ((width/scaledBandLevels.length)/2), (height/2), modSizes[i], modSizes[i]);
                    }
                }
            }
            else if (subMode == 4) {
                for (int i = 0; i < scaledBandLevels.length; i++) {
                    if (modSizes[i] < scaledBandLevels[i]) {
                        modSizes[i]+=20;
                    }
                    else  if (modSizes[i] > 40) {
                        modSizes[i]--;
                    }
                }
                for (int j = 0; j < 8; j++) {
                    rotateY(j * (PI/8));
                    for (int k = 0; k < height; k+= 300) {
                        for (int i = 0; i < scaledBandLevels.length; i++) {
                            image(mod, ((width/scaledBandLevels.length) * i) + ((width/scaledBandLevels.length)/2), k, modSizes[i], modSizes[i]);
                        }
                    }
                }
            }
        }       
        else if (mode == 4) {
            background(0);
            noStroke();
            for (int i = 0; i < letterStrings.length; i++) {    
                if (subMode == 0) {
                    fill(234,100,255, scaledBandLevels[i % scaledBandLevels.length]);
                    if (i < letterStrings.length/2) {
                        text(letterStrings[i], (width/2) - (textWidth(letterStrings[i])/2) - scaledBandLevels[i % scaledBandLevels.length] - (400 - (i * 100)), (height/2) + 75);
                    }
                    else {
                        text(letterStrings[i], (width/2) - (textWidth(letterStrings[i])/2) + scaledBandLevels[i % scaledBandLevels.length] + (i * 100) - 400, (height/2) + 75);
                    }
                }
                else if (subMode == 1) {
                    fill(234,100,255, scaledBandLevels[i % scaledBandLevels.length]);
                    for (int j = -100; j < height + 100; j+=200) {
                        if (i < letterStrings.length/2) {
                            text(letterStrings[i], (width/2) - (textWidth(letterStrings[i])/2) - scaledBandLevels[i % scaledBandLevels.length] - (400 - (i * 100)), j);
                        }
                        else {
                            text(letterStrings[i], (width/2) - (textWidth(letterStrings[i])/2) + scaledBandLevels[i % scaledBandLevels.length] + (i * 100) - 400, j);
                        }
                    }
                }
                else if (subMode == 2) {
                    fill(234,100,255, scaledBandLevels[i % scaledBandLevels.length]);
                    for (int j = -100 - (frameCount % 400); j < height + 600; j+=200) {
                        if (i < letterStrings.length/2) {
                            text(letterStrings[i], (width/2) - (textWidth(letterStrings[i])/2) - scaledBandLevels[i % scaledBandLevels.length] - (400 - (i * 100)), j);
                        }
                        else {
                            text(letterStrings[i], (width/2) - (textWidth(letterStrings[i])/2) + scaledBandLevels[i % scaledBandLevels.length] + (i * 100) - 400, j);
                        }
                    }
                }
                else if (subMode == 3) {
                    for (int j = -100; j < height + 100; j+=200) {
                        if (random(0,1) < .5) {
                            fill(0,250,255, scaledBandLevels[i % scaledBandLevels.length]);
                        }
                        else {
                            fill(234,100,255, scaledBandLevels[i % scaledBandLevels.length]);
                        }
                        if (i < letterStrings.length/2) {
                            text(letterStrings[i], (width/2) - (textWidth(letterStrings[i])/2) - scaledBandLevels[i % scaledBandLevels.length] - (400 - (i * 100)), j);
                        }
                        else {
                            text(letterStrings[i], (width/2) - (textWidth(letterStrings[i])/2) + scaledBandLevels[i % scaledBandLevels.length] + (i * 100) - 400, j);
                        }
                    }
                }
            }
        }
        else if (mode == 9) {
            background(0);
            noStroke();
            fill(234,100,255);
            String bandName = "ELAPHANT";
            text(bandName, (width/2) - (textWidth(bandName)/2), (height/2) + 75);
        }
        else {
            background(0);
        }
    }

    protected void keyPressedImpl()
    {
        // Your key controls should go here
        if (key == 'g') {
            trigger = true;
            triggerCount = 1;
        }
        else if (key == 'h') {
            trigger = !trigger;
        }
    }

    public static void main(String [] args) 
    {
        // Full screen code from http://pehrhovey.net/blog/2009/02/fullscreen-in-processingorg-with-eclipse/
        int primary_display = 0; //index into Graphic Devices array...  

        int primary_width;

        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice devices[] = environment.getScreenDevices();
        String location;
        if(devices.length>1 ){ //we have a 2nd display/projector
            primary_width = devices[0].getDisplayMode().getWidth();
            location = "--location="+primary_width+",0";

        }else{//leave on primary display
            location = "--location=0,0";

        }

        String display = "--display="+primary_display+1;  //processing considers the first display to be # 1
        PApplet.main(new String[] { location , "--hide-stop", display, reactivemusicapp.JuneThirdElaphant.class.getName()});
        // End full screen code
    }
}
