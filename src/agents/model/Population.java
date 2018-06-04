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
		boolean fight = false;

		Population adv = map.getAdversaryLocation(this.x, this.y, this);
		if(adv != null) {

        }

		if(!fight) { // On imagine que si un Dieu a combattu, il ne peux pas grossir le même tour
            if(this.numberInhabitants < MAX_POPULATION) this.grow();
            else this.expend(state);
        }
		//System.out.println(this.numberInhabitants);
		}


    public void checkTerrain() {
        // Le plus simple serait de faire une grille contenant les types de terrains qui serait généré au début et stocker dans la map.
        // Sinon il faut refaire la plupart des fonctions de récupération d'objet
    }

    public Int2D getPosition() {
        return new Int2D(this.x, this.y);
    }

    public void tuer()
    {
        this.numberInhabitants = 0;
    }


    public boolean attaquer(Population ennemi)
    {
        if (ennemi.defendre(this))
            return true;

        tuer(); // On a perdu ou égalité donc notre population meurt
        return false;
    }

    public void setNombreHabitants(int nbrH) {
        this.numberInhabitants = nbrH;
    }
    public int getNombreHabitants() {
        return this.numberInhabitants;
    }

    private boolean defendre(Population attaquant)
    {

        checkTerrain(); // Il faudra checker si le terrain nous donne un aventage
        //float ratioDefenseur = getRatioPuissanceAttaque(getCasePop().getTerrain());//le terrain de l'attaque est le terrain du defenseur
        //ratioDefenseur += getCasePop().getTerrain().getBonusPuissance();//le defenseur a droit a un bonus de defence car il connait le terrain ou il se trouve
        //float ratioEnnemi = ennemi.getRatioPuissanceAttaque(getCasePop().getTerrain());

        /*if( (ratioDefenseur*(float)getNombreHabitants()) >= (ratioEnnemi*(float)ennemi.getNombreHabitants()) )//defenseur gagne
        {
            //setNombreHabitants(getNombreHabitants() - calculNbMortPourLeGagnant(this, ratioDefenseur, attaquant, ratioEnnemi));//Calcul des "morts"
            return false;
        }*/
        tuer();
        //attaquant.setNombreHabitants(attaquant.getNombreHabitants() - calculNbMortPourLeGagnant(ennemi, ratioEnnemi, this, ratioDefenseur));//Calcul des "morts"
        attaquant.setNombreHabitants(attaquant.getNombreHabitants() - (101 % attaquant.getNombreHabitants()) + 1);
        return true;
    }



}
