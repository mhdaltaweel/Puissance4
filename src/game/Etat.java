package game;

import game.Coup;

import java.util.ArrayList;
import java.util.List;

public class Etat {

    public static final char JETON_VIDE = '_';
    public static final char JETON_ROUGE = 'R';
    public static final char JETON_JAUNE = 'J';
    char[][] plateau = new char[6][7];
    int joueurActuel;
    Etat etat;



    public Etat(){
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                plateau[i][j] = JETON_VIDE;
               /* if(j< 3 && i == 5){
                    plateau[i][j] = JETON_JAUNE;
                }*/
            }
        }
        joueurActuel = 0 ;
    }

    // Constructeur de copie
    public Etat(Etat autre) {
        this.joueurActuel = autre.joueurActuel;
        this.plateau = new char[6][7];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                this.plateau[i][j] = autre.plateau[i][j];
            }
        }
    }


    public boolean jouerCoup(Coup coup) {
        // Trouver la première ligne libre dans la colonne choisie et placer le jeton
        for (int i = plateau.length - 1; i >= 0; i--) {
            if (plateau[i][coup.colonne] == JETON_VIDE) {
                plateau[i][coup.colonne] = joueurActuel == 0 ? JETON_ROUGE : JETON_JAUNE;
                joueurActuel = 1 - joueurActuel; // Changer de joueur
                return true; // Le coup a été joué avec succès
            }
        }
        return false; //  le coup n'est pas valide
    }




    public void printEtat(){
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                System.out.print("|"+plateau[i][j]+"|"); ;
            }
            System.out.println();

        }
        System.out.println("---------------------");
    }

    // retourne 1 si "Rouge gagne", -1 si "Jaune gagne", et 0

    public int verifierFin() {
        // Vérification horizontale
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[0].length - 3; j++) {
                if (plateau[i][j] != JETON_VIDE && plateau[i][j] == plateau[i][j+1] && plateau[i][j] == plateau[i][j+2] && plateau[i][j] == plateau[i][j+3]) {
                    return plateau[i][j] == JETON_ROUGE ? 1 : -1;
                }
            }
        }
        // Vérification verticale
        for (int j = 0; j < plateau[0].length; j++) {
            for (int i = 0; i < plateau.length - 3; i++) {
                if (plateau[i][j] != JETON_VIDE && plateau[i][j] == plateau[i+1][j] && plateau[i][j] == plateau[i+2][j] && plateau[i][j] == plateau[i+3][j]) {
                    return plateau[i][j] == JETON_ROUGE ? 1 :-1;
                }
            }
        }
        // Vérification diagonale (montante et descendante)
        for (int i = 0; i < plateau.length - 3; i++) {
            for (int j = 0; j < plateau[0].length - 3; j++) {
                if (plateau[i][j] != JETON_VIDE && plateau[i][j] == plateau[i+1][j+1] && plateau[i][j] == plateau[i+2][j+2] && plateau[i][j] == plateau[i+3][j+3]) {
                    return plateau[i][j] == JETON_ROUGE ? 1 : -1;
                }
                if (plateau[i+3][j] != JETON_VIDE && plateau[i+3][j] == plateau[i+2][j+1] && plateau[i+3][j] == plateau[i+1][j+2] && plateau[i+3][j] == plateau[i][j+3]) {
                    return plateau[i+3][j] == JETON_ROUGE ? 1 : -1;
                }
            }
        }
        // Vérification match nul
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[0].length; j++) {
                if (plateau[i][j] == JETON_VIDE) {
                    return 0; // Le jeu continue
                }
            }
        }
        return 0; // Si aucune condition de victoire n'est remplie et le plateau est plein
    }


    // Generates a list of possible moves
    public List<Coup> coupsPossibles() {
        List<Coup> coups = new ArrayList<>();
        for (int col = 0; col < plateau[0].length; col++) {
            // Check if the top row of this column is empty
            if (plateau[0][col] == JETON_VIDE) {
                coups.add(new Coup(col));
            }
        }
        return coups;
    }

    public int getJoueurActuel() {
        return joueurActuel;
    }


    // Vérification de la Victoire Immédiate pour L'IA
    public boolean estVictoireImmediat(Coup coup, char jetonJoueur) {
        int row = -1; // Trouver la ligne où le jeton va tomber
        for (int i = plateau.length - 1; i >= 0; i--) {
            if (plateau[i][coup.colonne] == JETON_VIDE) {
                row = i;
                break;
            }
        }
        if (row == -1) { // Colonne pleine, le coup n'est pas valide
            return false;
        }

        // Temporairement place le jeton dans le plateau
        plateau[row][coup.colonne] = jetonJoueur;
        boolean resultat = verifierFin() != 0; // Vérifie si ce coup mène à une victoire
        plateau[row][coup.colonne] = JETON_VIDE; // Retire le jeton pour ne pas affecter l'état du jeu

        return resultat;
    }




}
