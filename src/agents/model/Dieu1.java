package agents.model;

import metier.Dieu;
import metier.Race;
import sim.engine.SimState;
import sim.util.Int2D;

public class Dieu1 extends AgentPopulation {

    public Dieu1(Dieu dieu, Race race) {
        super(dieu, race);
    }

    public void expend (SimState state) {
        Map map = (Map) state;
        Dieu1 p = new Dieu1(this.dieu, this.race);
        Int2D location = map.getFreeLocation(this.x, this.y);
        if (location != null) {
            map.yard.setObjectLocation(p,location.x, location.y);
            p.x = location.x;
            p.y = location.y;
            map.schedule.scheduleRepeating(p);
        }
    }

    public void step (SimState state) {
        super.step(state);
    }

}
