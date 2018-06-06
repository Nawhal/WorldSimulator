package agents.main;

import agents.model.Map;
import sim.display.Console;
import agents.gui.BeingsWithUI;

public class WorldMain {
	public static void main (String[] args) {
        runUI();
	}

	public static void runUI() {
		Map model = new Map(System.currentTimeMillis());
		BeingsWithUI gui = new BeingsWithUI(model);
		Console console = new Console(gui);
		console.setVisible(true);
	}
}
