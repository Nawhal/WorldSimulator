package agents.main;

import agents.model.Monde;
import metier.MondeInfos;
import sim.display.Console;
import agents.gui.BeingsWithUI;

public class MasonSimulation {
	public static void runUI(MondeInfos mondeInfos) {
		Monde model = new Monde(System.currentTimeMillis(), mondeInfos);
		BeingsWithUI gui = new BeingsWithUI(model);
		Console console = new Console(gui);
		console.setVisible(true);
	}
}
