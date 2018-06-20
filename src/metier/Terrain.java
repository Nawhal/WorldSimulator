package metier;

/**
 *
 * @author Cedric Paris
 */
public class Terrain {
    
    private String nom;
        /** @return Nom du terrain */
        public String getNom () {return nom;}

    private float bonusAccroissment;
        /** @return Bonus d'accroissement octroyé par le terrain */
        public float getBonusAccroissment () {return bonusAccroissment;}

    private float bonusPuissance;
        /** @return Bonus de puissance octroyé par le terrain */
        public float getBonusPuissance () {return bonusPuissance;}

    public Terrain (String nom, float bonusAccroissment, float bonusPuissance) {
        this.nom = nom;
        this.bonusAccroissment = bonusAccroissment;
        this.bonusPuissance = bonusPuissance;
    }

    public Terrain (Terrain terrain) {
        this.nom = terrain.nom;
        this.bonusAccroissment = terrain.bonusAccroissment;
        this.bonusPuissance = terrain.bonusPuissance;
    }
}
