package agents.model;

import metier.Monde;
import sim.engine.SimState;
import sim.field.grid.ObjectGrid2D;
import sim.util.Int2D;


public class Map extends SimState {
    private Monde monde;

    public static int GRID_SIZE = 20;
    public ObjectGrid2D yard = new ObjectGrid2D(GRID_SIZE, GRID_SIZE);

    public Map (long seed, Monde monde) {
        super(seed);
        this.monde = monde;
    }

    public void start () {
        System.out.println("Simulation started");
        super.start();
        yard.clear();
        initTestBattle();
    }

    private void initTestBattle () {
        Dieu1 p = new Dieu1();
        yard.set(0, 0, p);
        p.x = 0;
        p.y = 0;
        schedule.scheduleRepeating(p);

        Dieu2 q = new Dieu2();
        yard.set(19, 19, q);
        q.x = 19;
        q.y = 19;
        schedule.scheduleRepeating(q);
    }

  private void initAgentPopulation () {
		AgentPopulation p = new AgentPopulation();
        yard.set(5,5, p);
        p.x = 5;
        p.y = 5;
  }

    public boolean free (int x, int y) {
        int xx = yard.stx(x);
        int yy = yard.sty(y);
        return yard.get(xx, yy) == null;
    }

    private boolean inGrid (int x, int y) {
        return (x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE);
    }

    public Int2D getFreeLocation (int x, int y) {
        boolean t = false;
        Int2D location = null;
        if (inGrid(x - 1, y) && yard.get(x - 1, y) == null && t == false) {
            location = new Int2D(x - 1, y);
            t = true;
        }

        if (inGrid(x + 1, y) && yard.get(x + 1, y) == null && t == false) {
            location = new Int2D(x + 1, y);
            t = true;
        }

        if (inGrid(x, y - 1) && yard.get(x, y - 1) == null && t == false) {
            location = new Int2D(x, y - 1);
            t = true;
        }

        if (inGrid(x, y + 1) && yard.get(x, y + 1) == null && t == false) {
            location = new Int2D(x, y + 1);
            t = true;
        }

        return location;
    }

	public AgentPopulation getAdversaryLocation (int x, int y, AgentPopulation p) {
		boolean t = false;
        AgentPopulation adversary = null;
		if(inGrid(x - 1,y) && yard.get(x - 1,y) != null && (p.getIdDieu() != ((AgentPopulation) yard.get(x - 1,y)).getIdDieu()) && t == false) {
			adversary = (AgentPopulation) yard.get(x - 1,y);
			t = true;
		}

		if(inGrid(x + 1,y) && yard.get(x + 1,y) != null && (p.getIdDieu() != ((AgentPopulation) yard.get(x + 1,y)).getIdDieu()) && t == false) {
			adversary = (AgentPopulation) yard.get(x + 1,y);
			t = true;
		}

		if(inGrid(x,y - 1) && yard.get(x,y - 1) != null && (p.getIdDieu() != ((AgentPopulation) yard.get(x,y - 1)).getIdDieu()) && t == false) {
			adversary = (AgentPopulation) yard.get(x,y - 1);
			t = true;
		}

		if(inGrid(x,y + 1) && yard.get(x,y + 1) != null && (p.getIdDieu() != ((AgentPopulation) yard.get(x,y + 1)).getIdDieu()) && t == false) {
			adversary = (AgentPopulation) yard.get(x,y + 1);
		}

        return adversary;
    }
}
