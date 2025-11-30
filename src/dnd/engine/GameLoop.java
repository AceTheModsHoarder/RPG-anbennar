package dnd.engine;

import dnd.characters.Monster;
import dnd.characters.Player;
import dnd.combat.CombatSystem;
import dnd.items.*;
import java.util.Scanner;

public class GameLoop {
    private Scanner sc = new Scanner(System.in);
    private Player player;
    
    public void start() {
        System.out.println("=== DND TEXT RPG ===");
        createPlayer();
        
        while (player.isAlive()) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. View Stats");
            System.out.println("2. Explore");
            System.out.println("3. Inventory");
            System.out.println("4. Exit");
            System.out.print("Choice: ");
            
            int choice = sc.nextInt();
            sc.nextLine();
            
            switch (choice) {
                case 1 -> player.showStats();
                case 2 -> explore();
                case 3 -> player.useItemMenu(sc);
                case 4 -> {
                    System.out.println("Thanks for playing!");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
        
        System.out.println("\n=== GAME OVER ===");
        System.out.println("You have been defeated!");
    }
    
    private void createPlayer() {
        System.out.print("Enter your name: ");
        sc.nextLine(); // Clear buffer
        String name = sc.nextLine();
        
        if (name.isEmpty()) {
            name = "Hero";
        }
        
        player = new Player(name);
        System.out.println("Character created!");
    }
    
    private void explore() {
        System.out.println("\nYou venture into the wilds...");
        Monster m = generateRandomMonster();
        CombatSystem.start(player, m);
        
        if (!player.isAlive()) {
            return;
        }
        
        if (!m.isAlive()) {
            dropRandomItem();
            System.out.println("Press Enter to continue...");
            sc.nextLine();

        }
    }
    
    private Monster generateRandomMonster() {
        int roll = (int) (Math.random() * 3);
        return switch (roll) {
            case 0 -> new Monster("Goblin", 15, 4, 1);
            case 1 -> new Monster("Wolf", 20, 5, 2);
            default -> new Monster("Bandit", 25, 6, 3);
        };
    }
    
    private void dropRandomItem() {
        int roll = (int)(Math.random() * 2);
        
        if (roll == 0) {
            System.out.println("\nThe enemy dropped an Iron Sword!");
            Weapon w = new Weapon("Iron Sword", 3);
            player.addItem(w);
            player.addItem(w);
            System.out.println("You can equip this weapon in the Inventory menu.");

        } else {
            System.out.println("\nYou found a Healing Potion!");
            Potion p = new Potion("Healing Potion", 15);
            player.addItem(p);
        }
    }
}