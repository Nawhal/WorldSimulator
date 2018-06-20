package agents.model;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.paint.Color;
import metier.*;

import metier.Terrain;
import sim.engine.SimState;
import sim.field.grid.ObjectGrid2D;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;
import sim.util.Int2D;


public class Monde extends SimState {
    private MondeInfos mondeInfos;

    private int grid_size;
    public SparseGrid2D yard;

	public Monde (long seed, MondeInfos mondeInfos) {
        super(seed);
        this.mondeInfos = mondeInfos;

        int nbCase = calculerNbCase(mondeInfos);
        double squareRoot = Math.sqrt(nbCase);
        this.grid_size = squareRoot % 1 == 0 ? (int)squareRoot : (int)squareRoot + 1;
        this.yard = new SparseGrid2D(grid_size, grid_size);
    }

    /**
     * Calcul le nombre de case necessaire a partir des informations du monde a générer
     * @param mondeInfos Information sur le monde à générer
     * @return Nombre de case necessaire pour créer le monde
     */
    private int calculerNbCase (MondeInfos mondeInfos) {
        int nbCase = 0;
        for(Terrain t : mondeInfos.getTerrains().keySet()) {
            nbCase += mondeInfos.getTerrains().get(t);
        }
        return nbCase;
    }

	public void start () {
        System.out.println("Simulation started");
        super.start();
        yard.clear();
        genererTerrainCase();
        mettrePopulationsDansCases();
    }

    /**
     * Renseigne le Terrain de chaque case de manière aléatoire
     */
    private void genererTerrainCase () {
        int index;
        List<Int2D> coordonnees = new ArrayList<>();
        for (int x = 0; x < grid_size; x++) {
            for (int y = 0; y < grid_size; y++) {
                coordonnees.add(new Int2D(x, y));
            }
        }

        HashMap<Terrain, Integer> terrains = this.mondeInfos.getTerrains();
        int nbTerrains = calculerNbCase(this.mondeInfos);
        int nbCoordonnees = coordonnees.size();
        if (nbTerrains < nbCoordonnees) {
            terrains.put(new Terrain(FabriqueTerrain.fabriquerTerrain("Terrain Neutre")), nbCoordonnees - nbTerrains);
        }

        for(Terrain t : terrains.keySet()) {
            for(int i = 0; i < terrains.get(t); i++) {
                index = ThreadLocalRandom.current().nextInt(0, coordonnees.size());
                yard.setObjectLocation(new Terrain(FabriqueTerrain.fabriquerTerrain(t.getNom())), coordonnees.get(index));
                coordonnees.remove(index);
            }
        }
    }

    /**
     * Positionne aléatoirement les populations dans le yard
     */
    private void mettrePopulationsDansCases () {
        int index;
        List<Int2D> coordonnees = new ArrayList<>();
        for (int x = 0; x < grid_size; x++) {
            for (int y = 0; y < grid_size; y++) {
                coordonnees.add(new Int2D(x, y));
            }
        }

        for(Population p : mondeInfos.getPopulations().values()) {
            index = ThreadLocalRandom.current().nextInt(0, coordonnees.size());
            initAgentPopulation(p, coordonnees.get(index));
            coordonnees.remove(index);
        }
    }

    private void initAgentPopulation(Population pop, Int2D coord) {
        AgentPopulation p = new AgentPopulation(pop);
        yard.setObjectLocation(p, coord);
        p.x = coord.x;
        p.y = coord.y;
        schedule.scheduleRepeating(p);
    }

    public boolean free (int x, int y) {
        int xx = yard.stx(x);
        int yy = yard.sty(y);
        return yard.getObjectsAtLocation(xx, yy) == null;
    }

    private boolean inGrid (int x, int y) {
        return (x >= 0 && x < grid_size && y >= 0 && y < grid_size);
    }

    Int2D getFreeLocation (int x, int y) {
        Bag bb = null;
        boolean hasOnlyTerrain = false;
        if(inGrid(x - 1,y)) {
            bb = yard.getObjectsAtLocation(x - 1,y);
            if(bb == null || bb.isEmpty()) {
                return new Int2D(x - 1,y);
            } else {
                hasOnlyTerrain = true;
                for(Object obj : bb) {
                    if(! (obj instanceof Terrain)) {
                        hasOnlyTerrain = false;
                    }
                }
                if(hasOnlyTerrain) {
                    return new Int2D(x - 1,y);
                }
            }

        }
        if(inGrid(x + 1,y)) {
            bb = yard.getObjectsAtLocation(x + 1,y);
            if(bb == null || bb.isEmpty()) {
                return new Int2D(x + 1,y);
            }else {
                hasOnlyTerrain = true;
                for(Object obj : bb) {
                    if(! (obj instanceof Terrain)) {
                        hasOnlyTerrain = false;
                    }
                }
                if(hasOnlyTerrain) {
                    return new Int2D(x + 1,y);
                }
            }
        }
        if(inGrid(x,y - 1)) {
            bb = yard.getObjectsAtLocation(x,y - 1);
            if(bb == null || bb.isEmpty()) {
                return new Int2D(x,y - 1);
            } else {
                hasOnlyTerrain = true;
                for(Object obj : bb) {
                    if(! (obj instanceof Terrain)) {
                        hasOnlyTerrain = false;
                    }
                }
                if(hasOnlyTerrain) {
                    return new Int2D(x,y - 1);
                }
            }
        }
        if(inGrid(x,y + 1)) {
            bb = yard.getObjectsAtLocation(x,y + 1);
            if(bb == null || bb.isEmpty()) {
                return new Int2D(x,y + 1);
            } else {
                hasOnlyTerrain = true;
                for(Object obj : bb) {
                    if(! (obj instanceof Terrain)) {
                        hasOnlyTerrain = false;
                    }
                }
                if(hasOnlyTerrain) {
                    return new Int2D(x,y + 1);
                }
            }
        }
        return null;
    }

    private AgentPopulation getAgentPopulation(int x, int y) {
		Bag bag = yard.getObjectsAtLocation(x, y);
		if(bag != null) {
			for(Object o : bag) {
				if(o instanceof AgentPopulation) {
					return (AgentPopulation)o;
				}
			}
		}
		return null;
	}
	AgentPopulation getAdversaryLocation (int x, int y, AgentPopulation p) {
        AgentPopulation adversary;
		if(inGrid(x - 1,y)) {
            adversary = getAgentPopulation(x - 1,y);
            if(adversary != null && adversary.getIdDieu() != p.getIdDieu()) {
                return adversary;
            }

        }
        if(inGrid(x + 1,y)) {
            adversary = getAgentPopulation(x + 1,y);
            if(adversary != null && adversary.getIdDieu() != p.getIdDieu()) {
                return adversary;
            }

        }
        if(inGrid(x,y - 1)) {
            adversary = getAgentPopulation(x,y - 1);
            if(adversary != null && adversary.getIdDieu() != p.getIdDieu()) {
                return adversary;
            }

        }
        if(inGrid(x,y + 1)) {
            adversary = getAgentPopulation(x,y + 1);
            if(adversary != null && adversary.getIdDieu() != p.getIdDieu()) {
                return adversary;
            }

        }
        return null;
    }

    Terrain getTerrain(int x, int y) {
        if(inGrid(x,y)) {
            Bag bag = yard.getObjectsAtLocation(x, y);
            if(bag != null) {
                for(Object o : bag) {
                    if(o instanceof Terrain) {
                        return (Terrain) o;
                    }
                }
            }
        }
        return null;
    }
}
