package GameState;

import Helper.Keys;
import Main.GamePanel;

import java.awt.*;

public class PauseState extends GameState{
    private Font font;
    public PauseState(GameStateManager gsm) {

        super(gsm);

        // fonts
        font = new Font("Century Gothic", Font.PLAIN, 14);

    }

    @Override
    public void init() {

    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString("PAUZA", 90, 90);
    }

    @Override
    public void update() {
        handleInput();
    }

    public void handleInput() {
        if(Keys.isPressed(Keys.ESCAPE)) gsm.setPaused(false);
        if(Keys.isPressed(Keys.BUTTON1)) {
            gsm.setPaused(false);
            gsm.setState(GameStateManager.MENU_STATE);
        }
    }

    @Override
    public void keyPressed(int k) {

    }

    @Override
    public void keyReleased(int k) {

    }
}
