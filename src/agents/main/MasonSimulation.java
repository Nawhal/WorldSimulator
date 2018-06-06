package agents.main;

import agents.model.Map;
import metier.Monde;
import sim.display.Console;
import agents.gui.BeingsWithUI;

public class MasonSimulation {
	public static void runUI(Monde monde) {
		Map model = new Map(System.currentTimeMillis(), monde);
		BeingsWithUI gui = new BeingsWithUI(model, monde);
		Console console = new Console(gui);
		console.setVisible(true);
	}
}
