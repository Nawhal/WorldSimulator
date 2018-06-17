package agents.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.OvalPortrayal2D;
import agents.model.AgentPopulation;

public class StrangeOvalPortrayal extends OvalPortrayal2D {

	public StrangeOvalPortrayal() {
		super();
		paint = Color.GRAY;
		filled = true;
	}

	@Override
	public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
		AgentPopulation agent = (AgentPopulation)object;
        int r = (int) (agent.getDieu().getCouleur().getRed() * 255);
        int g = (int) (agent.getDieu().getCouleur().getGreen() * 255);
        int b = (int) (agent.getDieu().getCouleur().getBlue() * 255);
		paint = new Color(r, g, b);
		super.draw(object, graphics, info);
	}
}
