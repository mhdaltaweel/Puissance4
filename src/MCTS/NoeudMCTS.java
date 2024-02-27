package MCTS;

import game.Coup;
import game.Etat;

import java.util.ArrayList;
import java.util.List;


public class NoeudMCTS {
    private Etat etat;
    private Coup coup;
    private NoeudMCTS parent;
    private List<NoeudMCTS> enfants;
    private int nbVictoires;
    private int nbVisites;

    public NoeudMCTS(Etat etat, Coup coup, NoeudMCTS parent) {
        this.etat = new Etat(etat);
        this.coup = coup;
        this.parent = parent;
        this.enfants = new ArrayList<>();
        this.nbVictoires = 0 ;
        this.nbVisites = 0;

    }

    // parcours de l'arbre depuis la racine pour choisir les noeuds
    public NoeudMCTS selection() {
        NoeudMCTS noeud = this;
        while (!noeud.getEnfants().isEmpty()) {
            NoeudMCTS meilleurNoeud = null;
            double maxUCB1 = Double.MIN_VALUE;
            for (NoeudMCTS enfant : noeud.getEnfants()) {
                if (enfant.getNbVisites() == 0) continue; // Pour éviter la division par zéro.
                                // socre
                double ucb1 = (enfant.getNbVictoires() / (double) enfant.getNbVisites() ) +
                        Math.sqrt(2) * Math.sqrt(Math.log(noeud.getNbVisites()) / (double) enfant.getNbVisites());
                if (ucb1 > maxUCB1) {
                    meilleurNoeud = enfant;
                    maxUCB1 = ucb1;
                }
            }
            if (meilleurNoeud == null) break; // Cela peut arriver si tous les enfants ont 0 visites.
            noeud = meilleurNoeud;
        }
        return noeud;
    }

    // ajoute un nouvel enfant au noeud sélectionné pour un coup non encore exploré
    public void expansion() {
        List<Coup> coupsPossibles = this.etat.coupsPossibles();
        for (Coup coup : coupsPossibles) {
            Etat nouvelEtat = new Etat(this.etat); // Using copy constructor
            if (nouvelEtat.jouerCoup(coup)) { // Make sure jouerCoup updates the state correctly
                NoeudMCTS nouvelEnfant = new NoeudMCTS(nouvelEtat, coup, this);
                this.enfants.add(nouvelEnfant);
            }
        }
    }









    public void ajouterEnfant(NoeudMCTS enfant) {
        enfants.add(enfant);
    }

    public Etat getEtat() {
        return etat;
    }

    public NoeudMCTS getParent() {
        return parent;
    }

    public List<NoeudMCTS> getEnfants() {
        return enfants;
    }

    public int getNbVictoires() {
        return nbVictoires;
    }

    public void incrementerVictoires() {
        this.nbVictoires++;
    }

    public int getNbVisites() {
        return nbVisites;
    }

    public void incrementerVisites() {
        this.nbVisites++;
    }

    public Coup getCoup() {
        return coup;
    }



}
