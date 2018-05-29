package agents;
import sim.display.Console;

public class InsectMain {
    public static void main(String[] args) {
        runUI();
    }
    public static void runUI() {
        Beings model = new Beings(System.currentTimeMillis());
        InsectsWithUI gui = new InsectsWithUI(model);
        Console console = new Console(gui);
        console.setVisible(true);
    }
}

