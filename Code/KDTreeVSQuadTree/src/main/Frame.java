package main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Okan
 */
public class Frame extends JFrame{
    
    public Frame(){
        super("KD-Tree VS Quad-Tree");
        Main main= new Main();
        main.init();
        main.start();
        add(main);
        setSize(1227,550);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
         
    }
    public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            new Frame();
         }
      });
    }
}
