package agents.model;

import java.util.List;

import sim.engine.SimState;
import sim.util.Int2D;

/**
 *
 * @author Manuel F
 */
public class Terrain extends AgentType {
	public Map monde;
    private String nom;
    private int identifiant;
   
        @Override
	public void step(SimState state) {
		
	}
		/** @return Nom du terrain */
        public String getNom(){return nom;}
        public void setNom(String nom){this.nom = nom;}

    //A priori, c'est le dieu qui aura les valeurs des bonus d'accroissement.
    /*private float bonusAccroissment;
        /** @return Bonus d'accroissement octroyé par le terrain
        public float getBonusAccroissment() {return bonusAccroissment;}
        public void setBonusAccroissment(float bonusAccroissment){this.bonusAccroissment = bonusAccroissment;}
    
    private float bonusPuissance;
        /** @return Bonus de puissance octroyé par le terrain
        public float getBonusPuissance() {return bonusPuissance;}
        public void setBonusPuissance(float bonusPuissance){this.bonusPuissance = bonusPuissance;}*/
        
    public Terrain(String nom, int identifiant) {
        this.nom = nom;
        this.identifiant = identifiant;
    }
}
