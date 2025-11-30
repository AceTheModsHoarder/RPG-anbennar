package dnd.items;

import dnd.characters.Player;

public class Potion extends Item {
    private int healAmount;
    private Player player;
    
    public Potion(String name, int healAmount, Player player) {
        super(name);
        this.healAmount = healAmount;
        this.player = player;
    }
    
    @Override
    public void use() {
        player.heal(healAmount);
        System.out.println("You drink a " + name + 
            " and restore " + healAmount + " HP!");
    }
}