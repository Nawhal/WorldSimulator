package gui;

import java.awt.Color;

import javax.swing.JFrame;

import model.AgentPopulation;
import model.Terrain;
import model.student;
import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.field.continuous.Continuous2D;
import sim.portrayal.continuous.ContinuousPortrayal2D;
import sim.portrayal.grid.HexaObjectGridPortrayal2D;
import sim.portrayal.grid.HexaSparseGridPortrayal2D;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.HexagonalPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.util.Double2D;
import sim.util.*;
public class MondeGUI extends GUIState {

	public static int FRAME_SIZE = 600;
	public Display2D display;
	public JFrame displayFrame;
	
	HexaSparseGridPortrayal2D yardPortrayal = new 	HexaSparseGridPortrayal2D();
	
	public MondeGUI(SimState state) {
		super(state);
	
	}
	public void start() {
		  super.start();
		  setupPortrayals();
		}

		public void load(SimState state) {
		  super.load(state);
		  setupPortrayals();
		}
		public HexagonalPortrayal2D getTerrainPortRayal() {
			TerrainPortrayal hexPort2D = new TerrainPortrayal();
			return hexPort2D;
		}
		
		public OvalPortrayal2D getOvalPortrayal2D() {
			OvalPortrayal2D hexport = new OvalPortrayal2D();
			
			hexport.filled = true;
			hexport.paint= Color.darkGray;
					
			hexport.scale = 2;
			return hexport;
		}
		
		public void setupPortrayals() {
			Monde monde = (Monde) state;
			// tell the portrayals what to portray and how to portray them
			yardPortrayal.setField(monde.yard);
			System.out.println("Tamano"+ monde.yard.getHeight() + "Width" + monde.yard.getWidth() );
			yardPortrayal.setPortrayalForClass(Terrain.class, getTerrainPortRayal());
			yardPortrayal.setPortrayalForClass(AgentPopulation.class, getOvalPortrayal2D());
			//yardPortrayal.setPortrayalForAll(new OvalPortrayal2D(1,true));
			// reschedule the displayer
			display.reset();
			display.setBackdrop(Color.decode("#F4F7D9"));
			// redraw the display
			display.repaint();

		}
	public Display2D getDisplay() {
		return display;
	}
	public void setDisplay(Display2D display) {
		this.display = display;
	}
	public JFrame getDisplayFrame() {
		return displayFrame;
	}
	public void setDisplayFrame(JFrame displayFrame) {
		this.displayFrame = displayFrame;
	}

	
	public void init(Controller c) {
		  super.init(c);
		  display = new Display2D(FRAME_SIZE,FRAME_SIZE,this);
		  display.setClipping(false);
		  displayFrame = display.createFrame();
		  displayFrame.setTitle("Champ");
		  c.registerFrame(displayFrame); // so the frame appears in the "Display" list
		  displayFrame.setVisible(true);
		  display.attach( yardPortrayal, "Yard" );
		}
	
	

public static void main(String[] args)
{
	Monde model = new Monde(System.currentTimeMillis());
	MondeGUI gui = new MondeGUI(model);
	Console c = new Console(gui);
	c.setVisible(true);
	
	
	}

}
