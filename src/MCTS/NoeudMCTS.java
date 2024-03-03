package MCTS;

import game.Coup;
import game.Etat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


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
       // System.out.println("Début selection");
        NoeudMCTS noeud = this;
        Random rand = new Random();

        while (!noeud.getEnfants().isEmpty()) {
            NoeudMCTS meilleurNoeud = null;
            double maxUCB1 = Double.MIN_VALUE;

            // Vérifier d'abord si un enfant n'a jamais été visité
            List<NoeudMCTS> enfantsNonVisites = noeud.getEnfants().stream()
                    .filter(e -> e.getNbVisites() == 0)
                    .collect(Collectors.toList());
            if (!enfantsNonVisites.isEmpty()) {
                return enfantsNonVisites.get(rand.nextInt(enfantsNonVisites.size()));
            }

            // Si tous les enfants ont été visités, utiliser UCB1 pour choisir
            for (NoeudMCTS enfant : noeud.getEnfants()) {
                double ucb1 = enfant.getUCB1Score(noeud.getNbVisites());
                if (ucb1 > maxUCB1) {
                    meilleurNoeud = enfant;
                    maxUCB1 = ucb1;
                }
            }

            noeud = meilleurNoeud; // Cela ne devrait jamais être null ici car tous les enfants ont été visités
        }
        //System.out.println("Fin selection, retourne: " + noeud);

        return noeud; // Retourne le noeud sélectionné pour expansion
    }

    public double getUCB1Score(int nbVisitesParent) {
        if (this.nbVisites == 0) {
            return Double.MAX_VALUE; // Encourage l'exploration des noeuds non visités
        }
        double exploitation = (double) this.nbVictoires / this.nbVisites;
        double exploration = Math.sqrt(2) * Math.sqrt(Math.log(nbVisitesParent) / this.nbVisites);
        return exploitation + exploration;
    }



    // ajoute un nouvel enfant au noeud sélectionné pour un coup non encore exploré
    public void expansion() {
        //System.out.println("Début expansion");
        List<Coup> coupsPossibles = this.etat.coupsPossibles();
        for (Coup coup : coupsPossibles) {
            Etat nouvelEtat = new Etat(this.etat); // Using copy constructor
            if (nouvelEtat.jouerCoup(coup)) { // Make sure jouerCoup updates the state correctly
                NoeudMCTS nouvelEnfant = new NoeudMCTS(nouvelEtat, coup, this);
                this.enfants.add(nouvelEnfant);
            }
        }
        //System.out.println("Fin expansion, nombre d'enfants ajoutés: " + this.enfants.size());
    }




    // joue des coups aléatoires depuis l'état du noeud
    public double simulation() {
        //System.out.println("Début simulation");
        Etat etatTemp = new Etat(this.etat); // Utilise le constructeur de copie
        Random rand = new Random();

        while (true) {
            List<Coup> coupsPossibles = etatTemp.coupsPossibles();
            if (coupsPossibles.isEmpty()) break; // Si aucun coup n'est possible, termine la simulation

            Coup coup = coupsPossibles.get(rand.nextInt(coupsPossibles.size()));
            etatTemp.jouerCoup(coup); // Joue le coup choisi aléatoirement

            int resultat = etatTemp.verifierFin();
            if (resultat != 0) { // Si le jeu est fini (victoire, défaite ou match nul)
                //System.out.println("simultation ......................... " + resultat);
                return resultat; // Retournez le résultat de la simulation
            }
            //System.out.println("Fin simulation, résultat: " + resultat);
        }

        return 0; // Retourne 0 si le jeu n'est pas fini après la simulation
    }



    public void propagationEnArriere(double resultat) {
        //System.out.println("Début propagation en arrière, resultat: " + resultat);
        NoeudMCTS noeud = this;
        while (noeud != null) {
            noeud.incrementerVisites(); // Incrémente le nombre de visites pour ce noeud
            if (resultat == 1.0) {
                noeud.incrementerVictoires(); // Incrémente le nombre de victoires si le résultat est une victoire
            }
           // System.out.println("Noeud mis à jour: " + noeud.toString());
            noeud = noeud.getParent(); // Remonte vers le parent jusqu'à atteindre la racine
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
