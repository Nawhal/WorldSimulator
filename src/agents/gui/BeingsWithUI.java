package agents.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

import agents.model.Monde;
import metier.MondeInfos;
import metier.Terrain;

import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.Inspector;
import sim.portrayal.grid.HexaSparseGridPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import agents.model.AgentPopulation;

public class BeingsWithUI extends GUIState {
    private static int FRAME_SIZE = 600;
    private Display2D display;
    private JFrame displayFrame;

    private HexaSparseGridPortrayal2D yardPortrayal = new HexaSparseGridPortrayal2D();

    public BeingsWithUI (SimState state) {
        super(state);
    }

    public static String getName () {
        return "God Fight";
    }

    public void start () {
        super.start();
        setupPortrayals();
    }

    public void load (SimState state) {
        super.load(state);
        setupPortrayals();
    }

    private void setupPortrayals () {
        Monde map = (Monde) state;
        yardPortrayal.setField(map.yard);
        yardPortrayal.setPortrayalForClass(AgentPopulation.class, new PopulationPortrayal());
        yardPortrayal.setPortrayalForClass(Terrain.class, new TerrainPortrayal());
        display.reset();
        addBackgroundImage();
        display.repaint();
    }

    public void init (Controller c) {
        super.init(c);
        display = new Display2D(FRAME_SIZE, FRAME_SIZE, this);
        display.setClipping(false);
        displayFrame = display.createFrame();
        displayFrame.setTitle("God Fight");
        c.registerFrame(displayFrame); // so the frame appears in the "Display" list
        displayFrame.setVisible(true);
        display.attach(yardPortrayal, "Monde");
    }

    private void addBackgroundImage() {
        Image i = new ImageIcon(getClass().getResource("../../design/GameBackground.jpg")).getImage();
        int w = i.getWidth(null) / 5;
        int h = i.getHeight(null) / 5;
        BufferedImage b = display.getGraphicsConfiguration().createCompatibleImage(w, h);
        Graphics g = b.getGraphics();
        g.drawImage(i, 0, 0, w, h, null);
        g.dispose();
        display.setBackdrop(new TexturePaint(b, new Rectangle(0, 0, w, h)));
    }

    public Object getSimulationInspectedObject () {
        return state;
    }

    public Inspector getInspector () {
        Inspector i = super.getInspector();
        i.setVolatile(true);
        return i;
    }
}
