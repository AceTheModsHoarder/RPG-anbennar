package dnd.items;

import dnd.characters.Player;

public class Potion extends Item {
    private final int healAmount;
    
    
    public Potion(String name, int healAmount) {
        super(name);
        this.healAmount = healAmount;
    }
    
    public int getHealAmount() {
        return healAmount;
    }

    @Override
    public void use(Player player) {
        player.heal(healAmount);
        System.out.println("You used a " + getName() + " and healed " + healAmount + " HP!");
    }
}