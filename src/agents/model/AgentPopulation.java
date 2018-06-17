package agents.model;

import metier.Terrain;
import metier.*;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;

public class AgentPopulation implements Steppable {
    protected Dieu dieu;
    private String name;
    private StrategieDeJeu strat;
    public int NUMBER_TERRAIN = 5;
    public int getNUMBER_TERRAIN() {
		return NUMBER_TERRAIN;
	}

	public void setNUMBER_TERRAIN(int nUMBER_TERRAIN) {
		NUMBER_TERRAIN = nUMBER_TERRAIN;
	}

	protected Race race;
    private int numberInhabitants = 1;
    public static int MAX_POPULATION = 100;
    public int x, y;

    public AgentPopulation(Dieu dieu, Race race) {
        this.dieu = dieu;
        this.race = race;
    }

    private void grow () {
        this.numberInhabitants += 1;
    }

    protected void expend (SimState state) {
        Map map = (Map) state;
        AgentPopulation p = new AgentPopulation(dieu, race);
        Int2D location = map.getFreeLocation(this.x, this.y);
        if (location != null) {
            map.yard.setObjectLocation(p,location.x, location.y);
            p.x = location.x;
            p.y = location.y;
            map.schedule.scheduleRepeating(p);
        }
    }

    @Override
    public void step (SimState state) {
        if(this.numberInhabitants == 0) {
            state.schedule.scheduleRepeating(this).stop();
            return;
        }
        Map map = (Map) state;
        boolean fight = false;

        AgentPopulation adv = map.getAdversaryLocation(this.x, this.y, this);
        if (adv != null) {
            attaquer(adv, map);
        }

        if (!fight) { // On imagine que si un Dieu a combattu, il ne peux pas grossir le même tour
            if (this.numberInhabitants < MAX_POPULATION) this.grandir(map);
            else this.expend(state);
        }
        //System.out.println(this.numberInhabitants);
    }

    public void checkTerrain () {
        // Le plus simple serait de faire une grille contenant les types de terrains qui serait généré au début et stocker dans la map.
        // Sinon il faut refaire la plupart des fonctions de récupération d'objet
    }

    public Int2D getPosition () {
        return new Int2D(this.x, this.y);
    }

    public void tuer (Map map) {
        this.numberInhabitants = 0;
        map.yard.remove(this);
    }
    public boolean attaquer (AgentPopulation ennemi, Map map) {
        //System.out.println("test2");
        if (ennemi.defendre(this, map))
            return true;

        //tuer(map); // On a perdu ou égalité donc notre population meurt
        return false;
    }

    public void setNombreHabitants (int nbrH) {
        if(nbrH>MAX_POPULATION)
            nbrH = MAX_POPULATION;
        if(nbrH<0)
            nbrH = 0;
        this.numberInhabitants = nbrH;
    }

    public int getNombreHabitants () {
        return this.numberInhabitants;
    }

    private boolean defendre (AgentPopulation attaquant, Map map) {
        Terrain terrain = map.getTerrain(this.x, this.y);
        if(terrain == null) {
            tuer(map);
            return true; //Cas qui ne devrait pas arriver, mais autant le gérer (si le terrain est nul, on tue la population présente sur la case)
        }
        float ratioDefenseur = getRatioPuissanceAttaque(terrain); // le terrain de l'attaque est le terrain du defenseur
        ratioDefenseur += map.getTerrain(this.x, this.y).getBonusPuissance(); // le defenseur a droit a un bonus de defence car il connait le terrain ou il se trouve
        float ratioEnnemi = attaquant.getRatioPuissanceAttaque(terrain);

        if( (ratioDefenseur*(float)getNombreHabitants()) >= (ratioEnnemi*(float)attaquant.getNombreHabitants()) ) { // defenseur gagne
            setNombreHabitants(getNombreHabitants() - calculNbMortPourLeGagnant(this, ratioDefenseur, attaquant, ratioEnnemi)); // Calcul des "morts"
            return false;
        }
        tuer(map);
        attaquant.setNombreHabitants(attaquant.getNombreHabitants() - calculNbMortPourLeGagnant(attaquant, ratioEnnemi, this, ratioDefenseur)); // Calcul des "morts"
        return true;




        /*
        //System.out.println("test");
        checkTerrain(); // Il faudra checker si le terrain nous donne un aventage
        //float ratioDefenseur = getRatioPuissanceAttaque(getCasePop().getTerrain());//le terrain de l'attaque est le terrain du defenseur
        //ratioDefenseur += getCasePop().getTerrain().getBonusPuissance();//le defenseur a droit a un bonus de defence car il connait le terrain ou il se trouve
        //float ratioEnnemi = ennemi.getRatioPuissanceAttaque(getCasePop().getTerrain());

        /*if( (ratioDefenseur*(float)getNombreHabitants()) >= (ratioEnnemi*(float)ennemi.getNombreHabitants()) )//defenseur gagne
        {
            //setNombreHabitants(getNombreHabitants() - calculNbMortPourLeGagnant(this, ratioDefenseur, attaquant, ratioEnnemi));//Calcul des "morts"
            return false;
        }*
        tuer(map);
        //attaquant.setNombreHabitants(attaquant.getNombreHabitants() - calculNbMortPourLeGagnant(ennemi, ratioEnnemi, this, ratioDefenseur));//Calcul des "morts"
        //attaquant.setNombreHabitants(attaquant.getNombreHabitants() - (101 % attaquant.getNombreHabitants()) + 1);
        return true;*/
    }
    private int calculNbMortPourLeGagnant (AgentPopulation gagnant, float ratioGagnant, AgentPopulation perdant, float ratioPerdant) {
        return (int)( ( (float)gagnant.getNombreHabitants() * (ratioPerdant*(float)perdant.getNombreHabitants()) ) / (ratioGagnant * (float)gagnant.getNombreHabitants()) );
    }

    private float getRatioPuissanceAttaque (Terrain terrainAttaque) {
        float ratio = this.race.getBonusAttaque() + this.dieu.getBonusBasePuissance();
        if(terrainAttaque.getNom().equals(this.dieu.getTerrainPredilection())) {
            ratio += this.dieu.getBonusTerrainPuissance();
        }
        return ratio;
    }

    public void grandir (Map map) {
        int bebes = (int)(calculBonusAccroissement(map) * getNombreHabitants()/2);
        if (bebes == 0)
            bebes = 1;
        setNombreHabitants(getNombreHabitants() + bebes);
    }

    private float calculBonusAccroissement(Map map) {
        float result = this.dieu.getBonusBaseAccroissement() * this.race.getBonusAttaque() ;
        result *= map.getTerrain(this.x, this.y).getBonusAccroissment();
        if (map.getTerrain(this.x, this.y).getNom().equals(this.dieu.getTerrainPredilection()))
            result *= this.dieu.getBonusTerrainAccroissement();
        return result;
    }


    public int getIdDieu() {
        return 0;
    }
}
