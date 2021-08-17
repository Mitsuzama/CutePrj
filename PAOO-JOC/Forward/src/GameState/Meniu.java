package GameState;

import TileMap.Background; //am nevoie doar de bkgr, deci nu are rost asa importez tot
import java.awt.*;
import java.awt.event.KeyEvent;

public class Meniu extends GameState{ //sau MenuState, primul GameState

    public Background bg;

    private int optiuneCurenta = 0;
    private String[] optiuniButoane ={
            "Începe",
            "Ajutor",
            "Ieși"
    };

    private Color culoareaTitlului;
    private Font fontulTitlului;

    private Font fontGeneral;


    public Meniu(GameStateManager gsm){
        super(gsm);

        try{
            bg = new Background("/Backgrounds/menubg.gif", 1);
            bg.setVector(-0.1, 0); //imaginea de fundal merge la stanga cu cate 0.1pixeli

            //culoareaTitlului= new Color(255, 143, 58); //orange fox
            culoareaTitlului= new Color(115, 23, 2); //maroniu
            fontulTitlului = new Font("Amatic SC", Font.BOLD, 28);
            fontGeneral = new Font("Source Code Pro", Font.PLAIN, 12);

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(){
        bg.update();
    }

    @Override
    public void draw(Graphics2D g){
        bg.draw(g); //draw background

        //draw - titlu
        g.setColor(culoareaTitlului);
        g.setFont(fontulTitlului);
        g.drawString("FORWARD", 80, 70);

        //draw - optiuni meniu
        g.setFont(fontGeneral);
        for(int i=0; i<optiuniButoane.length ; i++){
            if(i == optiuneCurenta){
                g.setColor(Color.BLACK);
            }
            else{
                g.setColor(Color.RED);
            }
            g.drawString(optiuniButoane[i], 140, 140+i*15);

        }

    }

    @Override
    public void init() {

    }

    private void select(){
        if(optiuneCurenta == 0){ //adica este pe START
            gsm.setState(GameStateManager.NIVEL1_STATE);
        }
        if(optiuneCurenta == 1) {//AJUTOR
            //
        }
        if(optiuneCurenta == 2){ //IESIRE
            System.exit(0);
        }
    }

    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_ENTER){
            select();
        }
        if(k == KeyEvent.VK_UP){
            optiuneCurenta--;
            if(optiuneCurenta == -1){
                optiuneCurenta = optiuniButoane.length - 1;
            }
        }
        if(k == KeyEvent.VK_DOWN){
            optiuneCurenta++;
            if(optiuneCurenta == optiuniButoane.length){
                optiuneCurenta = 0;
            }
        }

    }

    @Override
    public void keyReleased(int k) {

    }
}
