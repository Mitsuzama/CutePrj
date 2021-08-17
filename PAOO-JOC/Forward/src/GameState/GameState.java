package GameState;

import java.awt.*;

public abstract class GameState {//nu vom folosi clasa in sine , asa ca o facem abstracta

    protected GameStateManager gsm;

    public GameState(GameStateManager gsm) {

        this.gsm = gsm;
    }


    public abstract void init();
    public abstract void draw(Graphics2D g);
    public abstract void update();
    public abstract void keyPressed(int k);
    public abstract void keyReleased(int k);


}
