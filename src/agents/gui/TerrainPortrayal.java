package agents.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;

import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.HexagonalPortrayal2D;

public class TerrainPortrayal extends HexagonalPortrayal2D{

	public TerrainPortrayal() {
		super();
		float[] hsb = new float[3];
		paint = Color.decode("#417378");
		filled = true;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Object arg0, Graphics2D arg1, DrawInfo2D arg2) {
		// TODO Auto-generated method stub
		//AgentType agent = (AgentType)arg0;
		scale = 1;
	 
		super.draw(arg0, arg1, arg2);
	}

	

}
