package dnd.items;

import dnd.characters.Player;

public class Weapon extends Item {
    private final int attackBoost;
    
    public Weapon(String name, int attackBoost) {
        super(name);
        this.attackBoost = attackBoost;
    }
    
    public int getAttackBoost() { 
        return attackBoost; 
    }
    
    @Override
    public void use(Player player) {
        System.out.println("Equipped " + name + " (+" + attackBoost + " ATK)");
    }
}