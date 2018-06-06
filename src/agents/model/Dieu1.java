package agents.model;

import sim.engine.SimState;
import sim.util.Int2D;

public class Dieu1 extends Population{

    public void expend (SimState state) {
        Map map = (Map) state;
        Dieu1 p = new Dieu1();
        Int2D location = map.getFreeLocation(this.x, this.y);
        if (location != null) {
            map.yard.set(location.x, location.y, p);
            p.x = location.x;
            p.y = location.y;
            map.schedule.scheduleRepeating(p);
        }
    }

    public void step (SimState state) {
        super.step(state);
    }
}
