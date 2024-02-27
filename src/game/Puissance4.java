package game;

import MCTS.NoeudMCTS;

import java.util.List;
import java.util.Scanner;

import static game.Etat.JETON_JAUNE;
import static game.Etat.JETON_ROUGE;

public class Puissance4 {

    private Etat etatActuel ;


    public Puissance4(){
        this.etatActuel = new Etat();
    }

    private Coup choisirMeilleurCoupMCTS() {
        // Vérifie d'abord si une victoire immédiate est possible
        List<Coup> coupsPossibles = etatActuel.coupsPossibles();
        for (Coup coup : coupsPossibles) {
            if (etatActuel.estVictoireImmediat(coup, etatActuel.getJoueurActuel() == 0 ? JETON_ROUGE : JETON_JAUNE)) {
                return coup; // Retourne ce coup pour gagner le jeu
            }
        }

        // Vérifie ensuite si une victoire immédiate est possible pour l'adversaire et tente de bloquer
        char jetonAdversaire = etatActuel.getJoueurActuel() == 0 ? JETON_JAUNE : JETON_ROUGE;
        for (Coup coup : coupsPossibles) {
            if (etatActuel.estVictoireImmediat(coup, jetonAdversaire)) {
                return coup; // Retourne ce coup pour bloquer l'adversaire
            }
        }

        // Initialisation de la racine de l'arbre MCTS avec l'état actuel
        NoeudMCTS racine = new NoeudMCTS(etatActuel, null, null);

        int nombreDeSimulations = 1000; // Par exemple, faire 1000 simulations

        for (int i = 0; i < nombreDeSimulations; i++) {
            NoeudMCTS noeudSelectionne = racine.selection(); // Sélection
            noeudSelectionne.expansion(); // Expansion
            double resultatSimulation = noeudSelectionne.simulation(); // Simulation
            noeudSelectionne.propagationEnArriere(resultatSimulation); // Rétropropagation
        }

        // Choisir le meilleur coup parmi les enfants de la racine
        NoeudMCTS meilleurNoeud = null;
        double meilleurScore = Double.MIN_VALUE;

        for (NoeudMCTS enfant : racine.getEnfants()) {
            double score = (double) enfant.getNbVictoires() / enfant.getNbVisites();
            if (score > meilleurScore) {
                meilleurScore = score;
                meilleurNoeud = enfant;
            }
        }

        return meilleurNoeud != null ? meilleurNoeud.getCoup() : null;
    }



    public void jouer(){
        Scanner scanner = new Scanner(System.in);
        boolean jeuTerminer = false ;
        int colonneChoisie ;

        while (!jeuTerminer){
            this.etatActuel.printEtat();

            // c'est l'humain
            if(etatActuel.getJoueurActuel() == 0){
                System.out.println("Joueur , choisi un colonne (1-7) : ");
                colonneChoisie = scanner.nextInt() - 1;
                while (colonneChoisie < 0 || colonneChoisie >= 7 || !etatActuel.jouerCoup(new Coup(colonneChoisie))){
                    System.out.println("colonne invalide ou pleine   ...");
                    colonneChoisie = scanner.nextInt() - 1;
                }
            }else{
                // ici L'ia
                Coup meilleurCoup = choisirMeilleurCoupMCTS();
                System.out.println("Ordinateur joue dans la colonne : " + meilleurCoup.colonne);
                etatActuel.jouerCoup(meilleurCoup);
               // colonneChoisie = (int) (Math.random() * 7); // Choix aléatoire
               // System.out.println("Ordinateur joue dans la colonne : " + colonneChoisie);
               // etatActuel.jouerCoup(new Coup(colonneChoisie));
            }

            int resultat = etatActuel.verifierFin();
            if (resultat != 0) {
                jeuTerminer = true;
                etatActuel.printEtat(); // Affiche le plateau final
                if (resultat == 0) {
                    System.out.println("La partie se termine par un match nul !");
                } else {
                    System.out.println(resultat); // Affiche le gagnant
                }
            }

        }
        scanner.close();
    }

    public static void main(String[] args) {
        System.out.println("Jeu Puissance 4 commence !");
        Puissance4 jeu = new Puissance4();

        jeu.jouer();

    }


}
