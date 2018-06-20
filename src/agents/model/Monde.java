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
    private List<AgentPopulation> agentsDieux = new ArrayList<>();
        public List<AgentPopulation> getAgents() { return agentsDieux; } // FIXME useful ?
        public void setAgents(List<AgentPopulation> agents) { this.agentsDieux = agents; }

	public Monde (long seed, MondeInfos mondeInfos) {
        super(seed);
        this.mondeInfos = mondeInfos;

        int nbCase = calculerNbCase(mondeInfos);
        double squareRoot = Math.sqrt(nbCase);
        this.grid_size = squareRoot % 1 == 0 ? (int)squareRoot : (int)squareRoot + 1;
        this.yard = new SparseGrid2D(grid_size, grid_size);
        genererTerrainCase();
        mettrePopulationsDansCases();
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
        //initTestBattle();
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



    private void initTestBattle () {

        for (int i = 0; i < grid_size; i++) {
            for(int j = 0; j < grid_size; j++) {
                Random r = new Random();
                String nomTerrain = "Plaine";

                Terrain a = new Terrain(nomTerrain, 1.0f, 1.1f);
                yard.setObjectLocation(a, i, j);
            }
        }
    	//int tmp = GRID_SIZE;
        Race r = new Race("Nymphe", 0.9f, 1.1f);
        Dieu d = new Dieu("Chauntéa, Déesse des Plaines", "Plaine", 0.9f, 1.5f, 0.9f, 2.5f, Color.ORANGE, "design/Chauntea.jpg");

//        Dieu1 p = new Dieu1(d,r);
//        yard.setObjectLocation(p, 0, 0);
//        p.x = 0;
//        p.y = 0;
//        schedule.scheduleRepeating(p);
//        Race r2 = new Race("Elfe", 0.95f, 1.05f);
//        Dieu d2 = new Dieu("Heruwa, Dieu des déserts", "Désert", 0.9f, 1.5f, 0.9f, 1.5f, Color.ANTIQUEWHITE, "design/Heruwa.jpg");
//        Dieu2 q = new Dieu2(d2, r2);
//        yard.setObjectLocation(q,19,19);
//        q.x = 19;
//        q.y = 19;
//        schedule.scheduleRepeating(q);
}

    public boolean free (int x, int y) {
        int xx = yard.stx(x);
        int yy = yard.sty(y);
        return yard.getObjectsAtLocation(xx, yy) == null;
    }

    private boolean inGrid (int x, int y) {
        return (x >= 0 && x < grid_size && y >= 0 && y < grid_size);
    }

    public Int2D getFreeLocation (int x, int y) {
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
	public AgentPopulation getAdversaryLocation (int x, int y, AgentPopulation p) {
		boolean t = false;
        AgentPopulation adversary = null;
		if(inGrid(x - 1,y) && !t) {
            adversary = getAgentPopulation(x - 1,y);
            if(adversary != null && adversary.getIdDieu() != p.getIdDieu()) {
                return adversary;
            }

        }
        if(inGrid(x + 1,y) && !t) {
            adversary = getAgentPopulation(x + 1,y);
            if(adversary != null && adversary.getIdDieu() != p.getIdDieu()) {
                return adversary;
            }

        }
        if(inGrid(x,y - 1) && !t) {
            adversary = getAgentPopulation(x,y - 1);
            if(adversary != null && adversary.getIdDieu() != p.getIdDieu()) {
                return adversary;
            }

        }
        if(inGrid(x,y + 1) && !t) {
            adversary = getAgentPopulation(x,y + 1);
            if(adversary != null && adversary.getIdDieu() != p.getIdDieu()) {
                return adversary;
            }

        }
        return null;
    }

    public Terrain getTerrain(int x, int y) {
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
