import java.util.Scanner;

public class Puissance4 {

    private Etat etatActuel ;


    public Puissance4(){
        this.etatActuel = new Etat();
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
                colonneChoisie = (int) (Math.random() * 7); // Choix aléatoire
                System.out.println("Ordinateur joue dans la colonne : " + colonneChoisie);
                etatActuel.jouerCoup(new Coup(colonneChoisie));
            }

            String resultat = etatActuel.verifierFin();
            if (!resultat.equals("Continue")) {
                jeuTerminer = true;
                etatActuel.printEtat(); // Affiche le plateau final
                if (resultat.equals("Match nul")) {
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
