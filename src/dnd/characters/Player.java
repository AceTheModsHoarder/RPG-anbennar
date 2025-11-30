package dnd.characters;

import java.util.ArrayList;
import java.util.Scanner;
import dnd.items.Item;

public class Player extends Character {
    private ArrayList<Item> inventory = new ArrayList<>();
    
    public Player(String name) {
        super(name, 30, 6, 3);
    }
    
    public void showStats() {
        System.out.println("\n=== PLAYER STATS ===");
        System.out.println("Name: " + name);
        System.out.println("HP: " + hp + "/" + maxHp);
        System.out.println("ATK: " + attack);
        System.out.println("DEF: " + defense);
    }
    
    public void addItem(Item item) {
        inventory.add(item);
        System.out.println("Added to inventory: " + item.getName());
    }
    
    public void increaseAttack(int amount) {
        attack += amount;
        System.out.println("Attack increased by " + amount + "!");
    }
    
    public void heal(int amount) {
        hp += amount;
        if (hp > maxHp) hp = maxHp;
    }
    
    public void useItemMenu(Scanner sc) {
        if (inventory.isEmpty()) {
            System.out.println("\nInventory is empty.");
            return;
        }
        
        System.out.println("\n=== INVENTORY ===");
        for (int i = 0; i < inventory.size(); i++) {
            System.out.println((i+1) + ". " + inventory.get(i).getName());
        }
        
        System.out.print("Choose item to use (0 to cancel): ");
        int choice = sc.nextInt();
        
        if (choice < 1 || choice > inventory.size()) {
            System.out.println("Cancelled.");
            return;
        }
        
        Item item = inventory.get(choice-1);
        item.use();
        inventory.remove(item);
    }
}