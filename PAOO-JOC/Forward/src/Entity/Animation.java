package Entity;

import java.awt.image.BufferedImage;

public class Animation {
    private BufferedImage[] frames; //pentru a tine frameurile impreuna
    private int currentFrame; //variabila pentru a vedea care este frameul actual

    private long startTime; //timer intre frame-uri
    private long delay; //diferenta de timp intre fiecare frame

    private boolean playedOnce; //ne spune daca animatia a fost played deja

    public Animation(){
        playedOnce = false;
    }

    public void setFrames(BufferedImage[] frames){ //setter de frameuri
        this.frames = frames;
        currentFrame = 0; //resetez
        startTime = System.nanoTime();
        playedOnce = false;
    }

    public void setDelay(long d) {delay = d;}
    public void setFrame(int i) {currentFrame = i;}

    public void update(){ //ma va ajuta sa vad daca trebuie sa ma duc la urmatorul frame
        if(delay == -1) return;

        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if(elapsed > delay) {
            currentFrame++;
            startTime = System.nanoTime();
        }

        if(currentFrame == frames.length){ //daca frameul curent este egal cu ultimul frame din vector, inseamna ca am ajuns la capatul animatiei
            currentFrame = 0;
            playedOnce = true;
        }

    }

    //gettere
    public int getFrame() { return currentFrame; }
    public BufferedImage getImage() { return frames[currentFrame]; } //returneaza imaginea pe care trebuie sa o desenam
    public boolean hasPlayedOnce() {return playedOnce; }

}
