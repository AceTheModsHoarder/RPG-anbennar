package dnd.combat;

import dnd.characters.Player;
import dnd.characters.Monster;

import java.util.Scanner;

public class CombatSystem {

    private static Scanner sc = new Scanner(System.in);

    public static void start(Player player, Monster monster) {
        System.out.println("\nA wild " + monster.getName() + " appears!");

        while (player.isAlive() && monster.isAlive()) {

            System.out.println("\n=== COMBAT ===");
            System.out.println("1. Attack");
            System.out.println("2. Run");
            System.out.print("Choose an action: ");

            int choice = sc.nextInt();

            if (choice == 1) {
                // Player attacks
                int dmg = player.attack - monster.defense;
                if (dmg < 0) dmg = 0;
                monster.takeDamage(dmg);

                System.out.println("You deal " + dmg + " damage!");

                if (!monster.isAlive()) {
                    System.out.println("You defeated the " + monster.getName() + "!");
                    return;
                }

                // Monster attacks
                int mdmg = monster.attack - player.defense;
                if (mdmg < 0) mdmg = 0;
                player.takeDamage(mdmg);

                System.out.println(monster.getName() + " hits you for " + mdmg + "!");

                if (!player.isAlive()) {
                    System.out.println("\nYou have been defeated...");
                    return;
                }
            }
            else if (choice == 2) {
                System.out.println("You escaped safely!");
                return;
            }
            else {
                System.out.println("Invalid choice.");
            }
        }
    }
}

