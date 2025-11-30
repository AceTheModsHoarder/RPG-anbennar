package dnd.combat;

import dnd.characters.Monster;
import dnd.characters.Player;
import java.util.Scanner;

public class CombatSystem {
    private static Scanner sc = new Scanner(System.in);
    
    public static void start(Player p, Monster m) {
        System.out.println("\nA wild " + m.getName() + " appears!");
        
        while (p.isAlive() && m.isAlive()) {
            System.out.println("\n=== COMBAT ===");
            System.out.println("Your HP: " + p.getHp());
            System.out.println(m.getName() + " HP: " + m.getHp());
            System.out.println("1. Attack");
            System.out.println("2. Run");
            System.out.print("Choice: ");
            
            int c = sc.nextInt();
            
            if (c == 1) {
                int dmg = Math.max(0, p.getAttack() - m.getDefense());
                m.takeDamage(dmg);
                System.out.println("You dealt " + dmg + " damage!");
                
                if (!m.isAlive()) {
                    System.out.println("You defeated the " + m.getName() + "!");
                    return;
                }
                
                int mdmg = Math.max(0, m.getAttack() - p.getDefense());
                p.takeDamage(mdmg);
                System.out.println(m.getName() + " hits you for " + mdmg + "!");
                
                if (!p.isAlive()) {
                    System.out.println("You have fallen...");
                    return;
                }
            } else if (c == 2) {
                System.out.println("You ran away!");
                return;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}