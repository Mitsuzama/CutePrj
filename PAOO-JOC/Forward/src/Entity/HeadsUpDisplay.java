package Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HeadsUpDisplay {

    private Player player;
    private BufferedImage imagine;
    private Font font;


    public HeadsUpDisplay(Player player){
        this.player = player;

        try{
            imagine = ImageIO.read(getClass().getResourceAsStream("/HUD/hud.gif"));
            font = new Font("Arial", Font.PLAIN, 15);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g){
        g.drawImage(imagine, 0, 5, null);
        g.setFont(font);
        g.setColor(Color.white);
        g.drawString(player.getHealth()+"/"+ player.getMaxHealth(), 30, 20);
        g.drawString(player.getAttack() /100 + "/"+player.getMaxAttack()/100, 30, 41);
        g.setColor(java.awt.Color.WHITE);
        g.drawString(player.getTimeToString(), 290, 15);

    }
}
