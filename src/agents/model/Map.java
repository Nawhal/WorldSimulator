package agents.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import metier.Monde;

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
    	//int tmp = GRID_SIZE;
        Dieu1 p = new Dieu1();
        yard.setObjectLocation(p, 0, 0);
        p.x = 0;
        p.y = 0;
        schedule.scheduleRepeating(p);
        Dieu2 q = new Dieu2();

        yard.setObjectLocation(q,19,19);
        q.x = 19;
        q.y = 19;
        schedule.scheduleRepeating(q);
        agentsDieux.add(p);
        agentsDieux.add(q);
        for (int i = 0; i < GRID_SIZE; i++) {
            for(int j = 0; j < GRID_SIZE; j++) {
                Random r = new Random();
                int rdmterr = r.nextInt(10); //On génère un int entre 0 et 10 (pour le moment, à voir plus tard pour créer les terrains selon les valeurs de l'UI
                Terrain a = new Terrain("Terrain"+ i, rdmterr);
                yard.setObjectLocation(a, i, j);
                schedule.scheduleRepeating(a);
            }
        }
    	
    }

  private void initAgentPopulation() {
	  
		AgentPopulation p = new AgentPopulation();
        yard.setObjectLocation(p,5, 5);
        p.x = 5;
        p.y = 5;
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
        if(inGrid(x - 1,y)) {
            bb = yard.getObjectsAtLocation(x - 1,y);
            if(bb == null || bb.isEmpty()) {
                return new Int2D(x - 1,y);
            }

        }
        if(inGrid(x + 1,y)) {
            bb = yard.getObjectsAtLocation(x + 1,y);
            if(bb == null || bb.isEmpty()) {
                return new Int2D(x + 1,y);
            }
        }
        if(inGrid(x,y - 1)) {
            bb = yard.getObjectsAtLocation(x,y - 1);
            if(bb == null || bb.isEmpty()) {
                return new Int2D(x,y - 1);
            }
        }
        if(inGrid(x,y + 1)) {
            bb = yard.getObjectsAtLocation(x,y + 1);
            if(bb == null || bb.isEmpty()) {
                return new Int2D(x,y + 1);
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
}
