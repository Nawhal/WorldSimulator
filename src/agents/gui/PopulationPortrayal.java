package agents.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.OvalPortrayal2D;
import agents.model.AgentPopulation;

public class PopulationPortrayal extends OvalPortrayal2D {
	PopulationPortrayal () {
		super();
		filled = true;
	}

	@Override
	public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
        AgentPopulation agent = (AgentPopulation)object;
        javafx.scene.paint.Color color = agent.getDieu().getCouleur();
        paint = new Color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) color.getOpacity());
        super.draw(object, graphics, info);
	}
}
