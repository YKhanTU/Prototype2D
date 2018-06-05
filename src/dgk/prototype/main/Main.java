package dgk.prototype.main;

import dgk.prototype.game.GameWindow;
import dgk.prototype.input.InputManager;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World.");
        GameWindow gw = new GameWindow(new InputManager());
        gw.setup();
        gw.loop();
    }
}
