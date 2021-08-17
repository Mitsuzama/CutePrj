package GameState;

import Audio.JukeBox;
import Main.GamePanel;

import java.util.*;
import java.awt.Graphics2D;

public class GameStateManager {

    private GameState[] gameStates;
    private int currentState; //folosit ca index la gameState
    private boolean paused;
    private PauseState pauseState;

    public static final int NUMARUL_GAMESTATURILOR = 4;
    public static final int MENU_STATE = 0;
    public static final int NIVEL1_STATE = 1;
    public static final int NIVEL2_STATE = 2;
    public static final int NIVEL3_STATE = 3;

    public GameStateManager(){
        JukeBox.init();
        gameStates = new GameState[NUMARUL_GAMESTATURILOR];
        pauseState = new PauseState(this);
        paused = false;
        currentState = MENU_STATE;
        load(currentState);
    }

    private void load(int stare){
        if(stare == MENU_STATE){  //adaug un state cu Meniul jocului
            gameStates[stare] = new Meniu(this);
        }
        if(stare == NIVEL1_STATE){ //adaug un state pentru Nivelul 1 al jocului
            gameStates[stare] = new Nivel1(this);
        }
        if(stare == NIVEL2_STATE){ //adaug un state pentru Nivelul 2 al jocului
            gameStates[stare] = new Nivel2(this);
        }
        if(stare == NIVEL3_STATE){//adaug un state pentru Nivelul 3 al jocului
            gameStates[stare] = new Nivel3(this);
        }
    }

    private void unload(int stare){
        gameStates[stare] = null;
    }

    public void setState(int state) {//pentru schimbarea starii curente
        unload(currentState);
        currentState = state;
        load(currentState);
        //gameStates[currentState].init();
    }

    public void update(){ //functie de update a starii curente

        try {
            if(gameStates[currentState] != null) gameStates[currentState].update();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setPaused(boolean b) { paused = b; }

    public void draw(Graphics2D g){
        try {
            if(gameStates[currentState] != null) gameStates[currentState].draw(g);
            else {
                g.setColor(java.awt.Color.BLACK);
                g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void keyPressed(int k){
        gameStates[currentState].keyPressed(k);
    }

    public void keyReleased(int k){
        gameStates[currentState].keyReleased(k);
    }

}
