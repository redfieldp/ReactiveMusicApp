package reactivemusicapp;

import processing.core.PApplet;
import ddf.minim.AudioInput;
import ddf.minim.Minim;


public abstract class ReactiveMusicApp extends PApplet {
    Minim minim;
    AudioInput userInput;
    int bandCount;
    int[] bandRanges;
    int[] currentBandLevels;
    int[] bandMaxes;
    
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
        
    }
    
    public void keyPressed() {
        // All the controls for the audio reactivity
        
        // Call the user's key controls
        keyPressedImpl();
    }
    
    /**
     * Sets the number of bands and reinitializes all of the band variables accordingly
     * 
     * @param myBandCount the number of volume bands the user desires
     */
    public void setBandCount(int myBandCount) {
        
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
