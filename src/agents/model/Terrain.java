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
   public AgentPopulation Dieux_control;
   
        @Override
	public void step(SimState state) {
		// TODO Auto-generated method stub
        monde = (Map)state;  
        	Int2D pos = monde.getYard().getObjectLocation(this);
        	Dieux_control.x = pos.x;
        	Dieux_control.y = pos.y;
    		monde.getYard().setObjectLocation(Dieux_control,Dieux_control.x, Dieux_control.y);
    		monde.schedule.scheduleRepeating(Dieux_control);
        
		
		
	}
		public AgentPopulation getDieux_control() {
			return Dieux_control;
		}
		public void setDieux_control(AgentPopulation dieux_control) {
			Dieux_control = dieux_control;
		}
		/** @return Nom du terrain */
        public String getNom(){return nom;}
        public void setNom(String nom){this.nom = nom;}
    
    private float bonusAccroissment;
        /** @return Bonus d'accroissement octroyé par le terrain */
        public float getBonusAccroissment() {return bonusAccroissment;}
        public void setBonusAccroissment(float bonusAccroissment){this.bonusAccroissment = bonusAccroissment;}
    
    private float bonusPuissance;
        /** @return Bonus de puissance octroyé par le terrain */
        public float getBonusPuissance() {return bonusPuissance;}
        public void setBonusPuissance(float bonusPuissance){this.bonusPuissance = bonusPuissance;}
        
    public Terrain(String nom, float bonusAccroissment, float bonusPuissance,AgentPopulation Dieux) {
        this.nom = nom;
        this.bonusAccroissment = bonusAccroissment;
        this.bonusPuissance = bonusPuissance;
        this.Dieux_control = Dieux;
    }
}
