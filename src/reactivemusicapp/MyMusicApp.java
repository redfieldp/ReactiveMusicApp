package reactivemusicapp;

public class MyMusicApp extends ReactiveMusicApp
{
    int mode = 0;
    
    protected void setupImpl()
    {
        // Your setup work should go here
        size(1024,768);
    }

    protected void drawImpl()
    {
        // Your draw work should go here
        background(255);
        if (mode == 0) {
            fill(0);
        }
        else if (mode == 1) {
            
        }
        else if (mode == 2) {
            
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
        if (key == '1' ){
            mode = 1;
        }
        else if (key == '2') {
            mode = 2;
        }
        else if (key == '3') {
            mode = 3;
        }
        else if (key == '4') {
            mode = 4;
        }
        else if (key == '5') {
            mode = 5;
        }
        else if (key == '6') {
            mode = 6;
        }
        else if (key == '7') {
            mode = 7;
        }
        else if (key == '8') {
            mode = 8;
        }
        else if (key == '9') {
            mode = 9;
        }
        else if (key == '0') {
            // mode 0 is blackout
            mode = 0;
        }
    }

}
