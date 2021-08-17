package TileMap;

import Main.GamePanel;

import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Background {

    private BufferedImage imagine;

    private double x;
    private double y;
    private double dx;
    private double dy;

    private double scaleBackgroundMove; //scara la care se misca imaginea de fundal/ move scale

    public Background(String s, double sbm){

        //importarea resurselor in joc
        try{

            imagine = ImageIO.read(
                    getClass().getResourceAsStream(s)
            );
            scaleBackgroundMove = sbm;
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public void setPosition(double x, double y){
        this.x = (x * scaleBackgroundMove) % GamePanel.WIDTH; //ca sa nu iesim de pe ecran
        this.y = (y * scaleBackgroundMove) %GamePanel.HEIGHT;
    }

    public void setVector(double dx, double dy){//daca vrem ca imaginea de fundal sa dea scroll automat
        this.dx = dx;
        this.dy = dy;
    }

    public void update(){ //daca voi folosi setVector, va trebui sa updatez si x, y
        x +=dx;
        y +=dy;
    }

    public void draw(Graphics2D g){

        /*if(imagine == null){
            System.out.println("IMAGINE BACKGROUND NULA");
            System.exit(0);
        }*/
        /*if(g == null){
            System.out.println("GRAPHICS2D NUL");
            System.exit(0);
        }*/
        g.drawImage(imagine, (int)x, (int)y, null);
        //acum trebuie sa ma asigur ca am ecranul mplin tot timpul
        //spre exemplu, daca merge spre stanga imaginea de fundal, sa se completeze si in dreapta in continuare
        if(x<0){
            g.drawImage(imagine,
                    (int)x + GamePanel.WIDTH,
                    (int)y ,
                    null);
        }

        if(x>0){
            g.drawImage(imagine,
                    (int)x -GamePanel.WIDTH,
                    (int)y,
                    null);
        }
    }

}
