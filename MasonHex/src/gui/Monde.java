package gui;




import model.AgentPopulation;
import model.Terrain;
import model.student;
import sim.app.wcss.tutorial02.Student;
import sim.engine.*;
import sim.field.continuous.Continuous2D;
import sim.field.grid.ObjectGrid2D;
import sim.field.grid.SparseGrid2D;
import sim.portrayal.grid.HexaSparseGridPortrayal2D;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.HexagonalPortrayal2D;
import sim.util.Double2D;
import sim.util.Int2D;
public class Monde extends SimState
{
	public  SparseGrid2D yard = new SparseGrid2D(80,80);
	public int numTerrains = 5;
	public Monde(long seed)
{
super(seed);
}
/**
 * Generate la position des agent aleatoirement
 * 
 * @return location
 */
private Int2D getFreeLocation() {
	Int2D location = new Int2D(random.nextInt(yard.getWidth()), random.nextInt(yard.getHeight()));
	Object ag;
	while ((ag = yard.getObjectsAtLocation(location.x, location.y)) != null) {
		location = new Int2D(random.nextInt(yard.getWidth()), random.nextInt(yard.getHeight()));
	}
	return location;
}
public void start()
{

// clear the yard
yard.clear();
// clear the buddies

// add some Terrains to the yard
for (int i = 0; i < numTerrains; i++) {
	Terrain a = new Terrain("Terrain"+ i,1.2f, 1f);
	AgentPopulation agp = new AgentPopulation();	
	Int2D location = getFreeLocation();
	yard.setObjectLocation(a,location.x, location.y);
	yard.setObjectLocation(agp,location.x, location.y);
	schedule.scheduleRepeating(a);
	schedule.scheduleRepeating(agp);
	
}



}


}