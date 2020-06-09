package game.main;

import javax.swing.JFrame;

public class Window extends JFrame {

    JFrame frame = new JFrame("Snake");
    Game game = new Game();

    Window() {
        frame.add(game);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
