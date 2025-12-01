package dnd.skills;

import dnd.characters.Player;
import dnd.characters.Monster;

public class ShatterArmor extends Skill {
    private final int armorReduction; // Amount of armor to reduce
    private final int resistanceReduction; // Amount of resistance to reduce
    
    public ShatterArmor() {
        super("Shatter Armor", "Reduces enemy armor and damage resistance", 4);
        this.armorReduction = 10;
        this.resistanceReduction = 5;
    }
    
    @Override
    public void use(Player caster, Monster target) {
        // Store original values for display
        int originalArmor = target.getArmor();
        int originalResistance = target.getDamageResistance();
        
        // Apply armor reduction
        target.setArmor(Math.max(0, target.getArmor() - armorReduction));
        
        // Apply resistance reduction
        target.reduceDamageResistance(resistanceReduction);
        
        System.out.println("You shatter " + target.getName() + "'s defenses!");
        System.out.println("Armor reduced from " + originalArmor + " to " + target.getArmor());
        System.out.println("Resistance reduced from " + originalResistance + " to " + target.getDamageResistance());
    }
}