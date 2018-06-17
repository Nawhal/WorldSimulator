package agents.model;

import java.util.*;

import javafx.scene.paint.Color;
import metier.*;

import metier.Terrain;
import sim.engine.SimState;
import sim.field.grid.ObjectGrid2D;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;
import sim.util.Int2D;


public class Map extends SimState {
    private Monde monde;

    //public static int GRID_SIZE = 20;
    //public SparseGrid2D yard = new SparseGrid2D(GRID_SIZE, GRID_SIZE);
    public static int grid_size;
    public SparseGrid2D yard;
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

        this.grid_size = monde.getLongueurMax();
        this.yard = new SparseGrid2D(grid_size, grid_size);

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
        int nbCasesTotal = grid_size * grid_size;
        genererTerrainCase(this.monde, nbCasesTotal);
        //initTestBattle();
        genererDieux(monde);
    }



    private void genererTerrainCase (Monde monde, int nbCase) {

        ArrayList<Case> cases = monde.getListeCase();
        int index = cases.size();
        Random r = new Random();
        while (index < nbCase) {
            Case c = new Case(monde, 0);
            c.setTerrain(new Terrain("Terrain neutre", 0f, 0f));

            cases.add(r.nextInt(index), c);
            index = cases.size();
        }
        for(int i = 0; i < grid_size; i++) {
            for(int j = 0; j < grid_size; j++) {
                index--;
                if(index < 0) {
                    Terrain a = new Terrain("Terrain neutre", 0f, 0f);
                    yard.setObjectLocation(a, i, j);
                } else {
                    Case c = cases.get(index);
                    Terrain t = new Terrain(c.getTerrain().getNom(), c.getTerrain().getBonusAccroissment(), c.getTerrain().getBonusPuissance());
                    yard.setObjectLocation(t, i, j);
                }
            }
        }

        for(int i = 0; i < grid_size; i++) {
            for(int j = 0; j < grid_size; j++) {
                Bag b = yard.getObjectsAtLocation(i, j);
                if(b == null)
                    System.out.println(0);
                else
                    System.out.println(1);

            }
        }
    }


    private void genererDieux (Monde monde) {
        HashMap<Dieu, Population> dieux = monde.getPopulation();

        for (java.util.Map.Entry<Dieu, Population> entry : dieux.entrySet()) {
            Random r = new Random();
            Int2D free = null;
            int x = -1;
            int y = -1;
            do {
                x = r.nextInt(grid_size);
                y = r.nextInt(grid_size);
                free = getFreeLocation(x, y);
            } while(free == null);
            Dieu dieu = entry.getKey();
            Race race = entry.getValue().getRacePop();
            initAgentPopulation(dieu, race, x, y);
        }

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
