package reactivemusicapp;

import processing.core.PApplet;
import ddf.minim.AudioInput;
import ddf.minim.Minim;


public abstract class ReactiveMusicApp extends PApplet {
    Minim minim;
    AudioInput userInput;
    int bandCount;
    int[] bandRanges;
    float[] actualBandLevels;
    float[] scaledBandLevels;
    float[] bandMaxes;
    
    boolean drawInterface = false;

    public void setup() {
        // Setup variables for the reactive audio hooks
        minim = new Minim(this);
        userInput = minim.getLineIn(Minim.STEREO);
        bandCount = 5;
        setBandCount(bandCount);
        
        // After we have setup our variables, call the user's setup
        setupImpl();
    }
    
    public void draw() {
        updateAudioData();
        
        drawImpl();
        
        // Once the user's drawing is done, we draw over the visuals if they are active
        if(drawInterface) {
            int bandRangeCounter = 0;
            for(int i = 0; i < userInput.bufferSize() - 1; i++)
            {
              stroke(0, 128);
              
              // Scale some data for drawing purposes
              int xCoord = (int)map(i, 0, userInput.bufferSize() - 1, 0, width);
              int scaledHeightLeft = (int)map(userInput.left.get(i), -1, 1, 0, height/2);
              int scaledHeightRight = (int)map(userInput.right.get(i), -1, 1, 0, height/2);
              
              // For each channel draw a line of current levels so we can see where the 
              // waveforms fall in the selected bands
              line( xCoord, height/2, xCoord, (height/2) - scaledHeightLeft);
              line( xCoord, height, xCoord, height - scaledHeightRight);
              
              // If one of the band ranges stops here, draw an indicator line
              if (bandRanges[bandRangeCounter] == i) {
                  if (bandRangeCounter < bandRanges.length - 1) {
                      bandRangeCounter++;
                  }
                  stroke(255,0,0);
                  line(xCoord, 0, xCoord, height);
              }
            }
        }
    }
    
    public void keyPressed() {
        // All the controls for the audio reactivity
        if (key == ' ') {
            drawInterface = !drawInterface;
        }
        
        // Call the user's key controls
        keyPressedImpl();
    }
    
    /**
     * Sets the number of bands and reinitializes all of the band variables accordingly
     * 
     * @param myBandCount the number of volume bands the user desires
     */
    public void setBandCount(int myBandCount) {
        bandRanges = new int[myBandCount];
        actualBandLevels = new float[myBandCount];
        scaledBandLevels = new float[myBandCount];
        bandMaxes = new float[myBandCount];
        for (int i = 0; i < myBandCount; i++) {
            bandRanges[i] = (i + 1) * (userInput.bufferSize()/myBandCount);
            actualBandLevels[i] = 0;
            scaledBandLevels[i] = 0;
            bandMaxes[i] = 0;
        }
    }
    
    /**
     * Update the audio data based on current number of bands and the active input
     */
    public void updateAudioData() {
        
    }
    
    /**
     * User's setup actions
     */
    protected abstract void setupImpl();
    
    /**
     * User's draw actions
     */
    protected abstract void drawImpl();
    
    /**
     * User's key controls
     */
    protected abstract void keyPressedImpl();
}
