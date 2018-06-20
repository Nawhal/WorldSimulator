package agents.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.HexagonalPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import agents.model.AgentPopulation;

public class PopulationPortrayal extends HexagonalPortrayal2D {
	PopulationPortrayal () {
		super();
		filled = true;
		scale = 1;
	}

	@Override
	public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
        AgentPopulation agent = (AgentPopulation)object;
        javafx.scene.paint.Color color = agent.getDieu().getCouleur();
        paint = new Color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) color.getOpacity());
        //scale = agent.getNombreHabitants() / 1000;
        super.draw(object, graphics, info);
	}
}
