package dgk.prototype.main;

import dgk.prototype.game.GameWindow;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World.");
        GameWindow gw = new GameWindow();
        gw.setup();
        gw.loop();
    }
}
