package agents.model;

import sim.engine.SimState;
import sim.engine.Steppable;
import metier.Dieu;
import metier.Race;
import metier.StrategieDeJeu;
import sim.util.Int2D;

public class Population implements Steppable {
	private Dieu dieu;
	private String name;
	private StrategieDeJeu strat;
	private Race race;
	private int numberInhabitants = 0;
	public static int MAX_POPULATION = 1000;
    public int x, y;

    private void grow(){
    	this.numberInhabitants += 1;
	}

	 protected void expend(SimState state) {
		Map map = (Map) state;
		Population p = new Population();
		Int2D location = map.getFreeLocation(this.x, this.y);
		if (location != null) {
			map.yard.set(location.x, location.y, p);
			p.x = location.x;
			p.y = location.y;
			map.schedule.scheduleRepeating(p);
		}
	}

	@Override
	public void step(SimState state) {
		Map map = (Map) state;
		if(this.numberInhabitants < MAX_POPULATION) this.grow();
		else this.expend(state);
		//System.out.println(this.numberInhabitants);
		}
}
