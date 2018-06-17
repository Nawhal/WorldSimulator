package agents.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.paint.Color;
import metier.Dieu;
import metier.Monde;
import metier.Race;

import metier.Terrain;

import sim.engine.SimState;
import sim.field.grid.ObjectGrid2D;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;
import sim.util.Int2D;


public class Map extends SimState {
    private Monde monde;

    public static int GRID_SIZE = 20;
    public SparseGrid2D yard = new SparseGrid2D(GRID_SIZE, GRID_SIZE);
    public int num_terrains = 5;
    public List<AgentPopulation> agentsDieux = new ArrayList<AgentPopulation>();
    
    
    public Monde getMonde() {
		return monde;
	}

	public void setMonde(Monde monde) {
		this.monde = monde;
	}

	public int getNum_terrains() {
		return num_terrains;
	}

	public void setNum_terrains(int num_terrains) {
		this.num_terrains = num_terrains;
	}

	public List<AgentPopulation> getAgents() {
		return agentsDieux;
	}

	public void setAgents(List<AgentPopulation> agents) {
		this.agentsDieux = agents;
	}

	public Map (long seed, Monde monde) {
        super(seed);
        this.monde = monde;
    }

    public SparseGrid2D getYard() {
		return yard;
	}

	public void setYard(SparseGrid2D yard) {
		this.yard = yard;
	}

	public void start () {
        System.out.println("Simulation started");
        super.start();
        yard.clear();
        
        initTestBattle();
    }

    private void initTestBattle () {

        for (int i = 0; i < GRID_SIZE; i++) {
            for(int j = 0; j < GRID_SIZE; j++) {
                Random r = new Random();
                String nomTerrain = "Plaine";

                Terrain a = new Terrain(nomTerrain, 1.0f, 1.1f);
                yard.setObjectLocation(a, i, j);
            }
        }
    	//int tmp = GRID_SIZE;
        Race r = new Race("Nymphe", 0.9f, 1.1f);
        Dieu d = new Dieu("Chauntéa, Déesse des Plaines", "Plaine", 0.9f, 1.5f, 0.9f, 2.5f, Color.ORANGE, "design/Chauntea.jpg");

        Dieu1 p = new Dieu1(d,r);
        yard.setObjectLocation(p, 0, 0);
        p.x = 0;
        p.y = 0;
        schedule.scheduleRepeating(p);
        Race r2 = new Race("Elfe", 0.95f, 1.05f);
        Dieu d2 = new Dieu("Heruwa, Dieu des déserts", "Désert", 0.9f, 1.5f, 0.9f, 1.5f, Color.ANTIQUEWHITE, "design/Heruwa.jpg");
        Dieu2 q = new Dieu2(d2, r2);

        yard.setObjectLocation(q,19,19);
        q.x = 19;
        q.y = 19;
        schedule.scheduleRepeating(q);

    	
    }

  private void initAgentPopulation(Dieu d, Race r, int x, int y) {
	  
		AgentPopulation p = new AgentPopulation(d, r);
        yard.setObjectLocation(p,x, y);
        p.x = x;
        p.y = y;
        schedule.scheduleRepeating(p);
  }

    public boolean free (int x, int y) {
        int xx = yard.stx(x);
        int yy = yard.sty(y);
        return yard.getObjectsAtLocation(xx, yy) == null;
    }

    private boolean inGrid (int x, int y) {
        return (x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE);
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
