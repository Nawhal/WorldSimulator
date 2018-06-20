package agents.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import metier.FabriqueTerrain;
import metier.Terrain;
import metier.ValeursParDefaut;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.HexagonalPortrayal2D;

public class TerrainPortrayal extends HexagonalPortrayal2D{

	TerrainPortrayal() {
		super();
        scale = 1;
	}

	@Override
	public void draw(Object arg0, Graphics2D arg1, DrawInfo2D arg2) {
        Terrain terrain = (Terrain) arg0;
        javafx.scene.paint.Color color = ValeursParDefaut.couleurParTerrain().get(FabriqueTerrain.fabriquerTerrain(terrain.getNom()));
        if (color != null) {
            paint = new Color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) color.getOpacity());
        } else {
            paint = new Color(0.3f, 0.3f, 0.3f, 0.3f);
        }

		super.draw(arg0, arg1, arg2);
	}
}
