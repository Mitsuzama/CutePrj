package Main;

import javax.swing.*;
import java.awt.*;

public class Game {
    public static void main(String[] args){

        JFrame fereastra1 = new JFrame("Forward - Meniul Principal"); //construiesc un frame initial invizibil cu titlul specificat
        ImageIcon iconita = new ImageIcon("Backgrounds/eevee.png");
        fereastra1.setContentPane(new GamePanel());
        fereastra1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //pentru a nu avea defaultul HIDE_ON_CLOSE la inchiderea ferestrei
        fereastra1.setResizable(false);

        fereastra1.setIconImage(iconita.getImage()); //am setat iconita
        //fereastra1.setIconImage(new ImageIcon(getClass().getResource(("/Backgrounds/eevee.png")))).getImage();
        //
        fereastra1.pack(); //folosit pentru a face resize in functie de elementele continute
        fereastra1.setVisible(true);
    }
}
