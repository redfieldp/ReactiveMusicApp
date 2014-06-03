package reactivemusicapp;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;

import processing.core.PApplet;
import ddf.minim.AudioInput;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;


public abstract class ReactiveMusicApp extends PApplet {
    Minim minim;
    AudioInput userInput;
    FFT leftFFT, rightFFT;
    int bandCount;
    int[] bandRanges;
    float[] actualBandLevels;
    float[] scaledBandLevels;
    float[] smoothedBandLevels;
    float[] bandMaxes;
    int fftCap;
    int subMode = 0;
    int mode = 0;

    boolean active = false;
    
    // Full screen vars
    int WIDTH, HEIGHT;
    
    public void init(){

        // Full screen code from http://pehrhovey.net/blog/2009/02/fullscreen-in-processingorg-with-eclipse/
        if(frame!=null){
            frame.removeNotify();//make the frame not displayable
            frame.setResizable(false);
            frame.setUndecorated(true);
            println("frame is at "+frame.getLocation());
            frame.addNotify();
        }
        super.init();
        // End full screen code
    }

    public void setup() {
        // Start Full screen code from http://pehrhovey.net/blog/2009/02/fullscreen-in-processingorg-with-eclipse/
        //***** figure out the display environment ****/
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice devices[] = environment.getScreenDevices();
        System.out.println(Arrays.toString(devices));

        if(devices.length>1 ){ //we have a 2nd display/projector
            //learn the true dimensions of the secondary display
            WIDTH =devices[1].getDisplayMode().getWidth();
            HEIGHT= devices[1].getDisplayMode().getHeight();
            println("Adjusting animation size to "+WIDTH+"x"+HEIGHT+" b/c of 2ndary display");
        }else{ //no 2nd screen but make it fullscreen anyway
            WIDTH =devices[0].getDisplayMode().getWidth();
            HEIGHT= devices[0].getDisplayMode().getHeight();
            println("Adjusting animation size to "+WIDTH+"x"+HEIGHT+" to fit primary display");
        }
        size(WIDTH, HEIGHT, OPENGL);
        // End full screen code
        
        // Setup variables for the reactive audio hooks
        minim = new Minim(this);
        userInput = minim.getLineIn(Minim.STEREO, 4096, 44100);

        leftFFT = new FFT(userInput.left.size(), 44100);
        rightFFT = new FFT(userInput.right.size(), 44100);

        // Starting with the lower 1/4 of the FFT usually covers most music output
        fftCap = leftFFT.specSize()/10;

        setBandCount(5);

        // After we have setup our variables, call the user's setup
        setupImpl();
    }

    public void draw() {
        updateAudioData();

        drawImpl();

        // Once the user's drawing is done, we draw over the visuals if they are active
        if(active) {
            background(255);
            int bandRangeCounter = 0;
            for(int i = 0; i < fftCap - 1; i++)
            {
                stroke(0, 128);

                // Scale some data for drawing purposes
                int xCoord = (int)map(i, 0, fftCap - 1, 0, width);
                int scaledHeightLeft = (int)leftFFT.getBand(i);
                int scaledHeightRight = (int)rightFFT.getBand(i);

                // For each channel draw a line of current levels so we can see where the 
                // waveforms fall in the selected bands
                line( xCoord, height/2, xCoord, (height/2) - scaledHeightLeft);
                line( xCoord, height, xCoord, height - scaledHeightRight);

                // If one of the band ranges stops here, draw a volume indicator line
                if (bandRanges[bandRangeCounter] == i) {
                    if (bandRangeCounter < bandRanges.length - 1) {
                        bandRangeCounter++;
                    }
                    stroke(255,0,0);
                    line(xCoord, 0, xCoord, (int)(map(actualBandLevels[bandRangeCounter], 0, bandMaxes[bandRangeCounter], 0, height)));
                }
            }
        }
    }

    public void keyPressed() {
        if (key == ' ') {
            active = !active;
        }

        // All the controls for the audio reactivity, only valid if we are active
        if (active) {
            if (key == CODED) {
                if (keyCode == UP && fftCap < leftFFT.specSize()) {
                    setFFTCap(fftCap + 1);
                }
                else if (keyCode == DOWN && fftCap > bandCount){
                    setFFTCap(fftCap - 1);
                }
                else if (keyCode == LEFT && bandCount > 1) {
                    setBandCount(bandCount-1);
                }
                else if (keyCode == RIGHT && bandCount < 10) {
                    setBandCount(bandCount+1);
                }
            }
        }
        else {
            // Else call the user's key controls
            if (key == '1' ){
                subMode = 0;
                mode = 1;
            }
            else if (key == '2') {
                subMode = 0;
                mode = 2;
            }
            else if (key == '3') {
                subMode = 0;
                mode = 3;
            }
            else if (key == '4') {
                subMode = 0;
                mode = 4;
            }
            else if (key == '5') {
                subMode = 0;
                mode = 5;
            }
            else if (key == '6') {
                subMode = 0;
                mode = 6;
            }
            else if (key == '7') {
                subMode = 0;
                mode = 7;
            }
            else if (key == '8') {
                subMode = 0;
                mode = 8;
            }
            else if (key == '9') {
                subMode = 0;
                mode = 9;
            }
            else if (key == '0') {
                subMode = 0;
                // mode 0 is blackout
                mode = 0;
            }
            else if (key == 'q') {
                subMode = 0;
            }
            else if (key == 'w') {
                subMode = 1;
            }
            else if (key == 'e') {
                subMode = 2;
            }
            else if (key == 'r') {
                subMode = 3;
            }
            else if (key == 't') {
                subMode = 4;
            }
            else if (key == 'y') {
                subMode = 5;
            }
            keyPressedImpl();
        }
    }

    /**
     * Sets the number of bands and reinitializes all of the band variables accordingly
     * 
     * @param myBandCount the number of volume bands the user desires
     */
    public void setBandCount(int myBandCount) {
        bandCount = myBandCount;
        bandRanges = new int[myBandCount];
        actualBandLevels = new float[myBandCount];
        scaledBandLevels = new float[myBandCount];
        bandMaxes = new float[myBandCount];
        for (int i = 0; i < myBandCount; i++) {
            bandRanges[i] = (i + 1) * (fftCap/myBandCount);
            actualBandLevels[i] = 0;
            scaledBandLevels[i] = 0;
            bandMaxes[i] = 0;
        }
    }

    public void setFFTCap(int newCap) {
        fftCap = newCap;
        setBandCount(bandCount);
    }

    /**
     * Update the audio data based on current number of bands and the active input
     */
    public void updateAudioData() {
        // Push FFT
        leftFFT.forward(userInput.left);
        rightFFT.forward(userInput.right);

        // Add the bands in each range to the level counters
        int bandRangeCounter = 0;

        for (int i = 0; i < actualBandLevels.length; i++) {
            actualBandLevels[i] = 0;
        }

        for (int i = 0; i < leftFFT.specSize(); i++) {
            if (bandRangeCounter < bandRanges.length) {
                if (actualBandLevels[bandRangeCounter] < leftFFT.getBand(i)) {
                    actualBandLevels[bandRangeCounter] = leftFFT.getBand(i);
                }
                // When reaching in the end of the 
                if (bandRanges[bandRangeCounter] == i) {
                    // If we surpassed the previous max for this range, store the max
                    if (actualBandLevels[bandRangeCounter] > bandMaxes[bandRangeCounter]) {
                        bandMaxes[bandRangeCounter] = actualBandLevels[bandRangeCounter];
                    }
                    
                    // Move to the next range segment if there is one
                    scaledBandLevels[bandRangeCounter] = map(actualBandLevels[bandRangeCounter], 0, (float)(bandMaxes[bandRangeCounter] * 1.10), 0, 255);
                    bandRangeCounter++;
                }

            }
            else {
                // We don't need to store data from any of the FFT range above our max
                break;
            }
        }
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
