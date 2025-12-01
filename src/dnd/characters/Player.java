package dnd.characters;

import dnd.characters.Monster;
import dnd.items.Weapon;
import dnd.items.Armor;
import dnd.items.Potion;
import dnd.items.Consumable;
import dnd.items.Inventory;
import dnd.skills.Skill;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Player {
    private String name;
    private int hp;
    private int maxHp;
    private int mana;
    private int maxMana;
    private int attack;
    private int defense;
    private int dexterity;
    private int armorClass;
    private int level;
    private int experience;
    private boolean isAlive;
    private boolean isDefending;
    private Weapon equippedWeapon;
    private Armor equippedArmor;
    private Inventory inventory; // Changed from List<Object>
    private List<Skill> skills;
    private int percentArmorPenetration;
    private int flatArmorPenetration;
    
    public Player(String name) {
        this.name = name;
        this.level = 1;
        this.experience = 0;
        this.maxHp = 20;
        this.hp = maxHp;
        this.maxMana = 10;
        this.mana = maxMana;
        this.attack = 8; // Increased for better combat
        this.defense = 3;
        this.dexterity = 14; // Increased for better hit chance
        this.armorClass = 12;
        this.isAlive = true;
        this.isDefending = false;
        this.inventory = new Inventory(); // Now using Inventory class
        this.skills = new ArrayList<>();
        this.percentArmorPenetration = 0;
        this.flatArmorPenetration = 0;
    }
    
    // Getters
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getMana() { return mana; }
    public int getMaxMana() { return maxMana; }
    public int getAttack() { 
        int baseAttack = attack;
        if (equippedWeapon != null) {
            baseAttack += equippedWeapon.getAttackBonus();
        }
        return baseAttack;
    }
    public int getDefense() { 
        int baseDefense = defense;
        if (equippedArmor != null) {
            baseDefense += equippedArmor.getDefenseBonus();
        }
        return baseDefense;
    }
    public int getDexterity() { return dexterity; }
    public int getArmorClass() { return armorClass; }
    public int getLevel() { return level; }
    public int getExperience() { return experience; }
    public int getGold() { return inventory.getGold(); } // Get gold from inventory
    public boolean isAlive() { return isAlive; }
    public boolean isDefending() { return isDefending; }
    public Weapon getEquippedWeapon() { return equippedWeapon; }
    public Armor getEquippedArmor() { return equippedArmor; }
    public Inventory getInventory() { return inventory; } // Returns Inventory object
    public List<Skill> getSkills() { return skills; }
    public int getPercentArmorPenetration() { return percentArmorPenetration; }
    public int getFlatArmorPenetration() { return flatArmorPenetration; }
    
    // Setters
    public void setDefending(boolean defending) { this.isDefending = defending; }
    public void setEquippedWeapon(Weapon weapon) { this.equippedWeapon = weapon; }
    public void setEquippedArmor(Armor armor) { this.equippedArmor = armor; }
    public void setPercentArmorPenetration(int value) { this.percentArmorPenetration = Math.min(100, Math.max(0, value)); }
    public void setFlatArmorPenetration(int value) { this.flatArmorPenetration = Math.max(0, value); }
    
    // Combat methods
    public void takeDamage(int damage) {
        hp = Math.max(0, hp - damage);
        if (hp == 0) {
            isAlive = false;
        }
    }
    
    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }
    
    public void reduceMana(int amount) {
        mana = Math.max(0, mana - amount);
    }
    
    public void restoreMana(int amount) {
        mana = Math.min(maxMana, mana + amount);
    }
    
    // Add this method to your Player class:
    public void applyResistanceReduction(Monster target, int reductionAmount) {
    target.reduceDamageResistance(reductionAmount);
    System.out.println(target.getName() + "'s damage resistance reduced by " + reductionAmount + "!");
    }
    
    // Inventory methods - updated for Inventory class
    public void addItem(Object item) {
        if (item instanceof Weapon) {
            inventory.addWeapon((Weapon) item);
        } else if (item instanceof Armor) {
            inventory.addArmor((Armor) item);
        } else if (item instanceof Consumable) {
            inventory.addConsumable((Consumable) item);
        } else if (item instanceof Potion) {
            // Convert Potion to Consumable
            Potion potion = (Potion) item;
            inventory.addConsumable(new Consumable(
                potion.getName(), 
                potion.getDescription(), 
                potion.getHealAmount()
            ));
        }
    }
    
    public void useItemMenu(Scanner scanner) {
        inventory.displayInventory();
        
        if (inventory.getConsumables().isEmpty() && 
            inventory.getWeapons().isEmpty() && 
            inventory.getArmors().isEmpty()) {
            System.out.println("No items to use!");
            return;
        }
        
        System.out.println("\nWhat type of item would you like to use?");
        System.out.println("1. Use Consumable");
        System.out.println("2. Equip Weapon");
        System.out.println("3. Equip Armor");
        System.out.println("4. Back");
        System.out.print("Choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Clear buffer
        
        switch (choice) {
            case 1 -> useConsumable(scanner);
            case 2 -> equipWeapon(scanner);
            case 3 -> equipArmor(scanner);
            case 4 -> System.out.println("Returning...");
            default -> System.out.println("Invalid choice!");
        }
    }
    
    private void useConsumable(Scanner scanner) {
        List<Consumable> consumables = inventory.getConsumables();
        if (consumables.isEmpty()) {
            System.out.println("No consumables available!");
            return;
        }
        
        System.out.println("\nSelect consumable to use:");
        for (int i = 0; i < consumables.size(); i++) {
            Consumable c = consumables.get(i);
            System.out.println((i + 1) + ". " + c.getName() + 
                             " - " + c.getDescription() + 
                             " (x" + c.getQuantity() + ")");
        }
        System.out.println((consumables.size() + 1) + ". Back");
        
        System.out.print("Choice: ");
        int choice = scanner.nextInt() - 1;
        
        if (choice >= 0 && choice < consumables.size()) {
            Consumable consumable = consumables.get(choice);
            consumable.use(this);
            
            if (consumable.isEmpty()) {
                inventory.getConsumables().remove(consumable);
            }
        }
    }
    
    private void equipWeapon(Scanner scanner) {
        List<Weapon> weapons = inventory.getWeapons();
        if (weapons.isEmpty()) {
            System.out.println("No weapons available!");
            return;
        }
        
        System.out.println("\nSelect weapon to equip:");
        for (int i = 0; i < weapons.size(); i++) {
            Weapon w = weapons.get(i);
            System.out.println((i + 1) + ". " + w.getName() + 
                             " (+" + w.getAttackBonus() + " ATK)");
        }
        System.out.println((weapons.size() + 1) + ". Back");
        
        System.out.print("Choice: ");
        int choice = scanner.nextInt() - 1;
        
        if (choice >= 0 && choice < weapons.size()) {
            Weapon weapon = weapons.get(choice);
            
            // Unequip current weapon if any
            if (equippedWeapon != null) {
                inventory.addWeapon(equippedWeapon);
            }
            
            // Equip new weapon and remove from inventory
            equippedWeapon = weapon;
            inventory.getWeapons().remove(weapon);
            System.out.println("Equipped " + weapon.getName() + "!");
        }
    }
    
    private void equipArmor(Scanner scanner) {
        List<Armor> armors = inventory.getArmors();
        if (armors.isEmpty()) {
            System.out.println("No armor available!");
            return;
        }
        
        System.out.println("\nSelect armor to equip:");
        for (int i = 0; i < armors.size(); i++) {
            Armor a = armors.get(i);
            System.out.println((i + 1) + ". " + a.getName() + 
                             " (+" + a.getDefenseBonus() + " DEF)");
        }
        System.out.println((armors.size() + 1) + ". Back");
        
        System.out.print("Choice: ");
        int choice = scanner.nextInt() - 1;
        
        if (choice >= 0 && choice < armors.size()) {
            Armor armor = armors.get(choice);
            
            // Unequip current armor if any
            if (equippedArmor != null) {
                inventory.addArmor(equippedArmor);
            }
            
            // Equip new armor and remove from inventory
            equippedArmor = armor;
            inventory.getArmors().remove(armor);
            System.out.println("Equipped " + armor.getName() + "!");
        }
    }
    
    public void gainGold(int amount) {
        if (amount > 0) {
            inventory.addGold(amount);
        } else if (amount < 0) {
            // Spending gold
            int goldToRemove = -amount;
            if (inventory.getGold() >= goldToRemove) {
                inventory.addGold(amount); // Negative amount reduces gold
            }
        }
    }
    
    // Calculate effective armor after penetration
    public int calculateEffectiveArmor(dnd.characters.Monster monster) {
        int monsterArmor = monster.getArmor();
        double armorAfterPercentPen = monsterArmor * (1.0 - (percentArmorPenetration / 100.0));
        return (int) Math.max(0, armorAfterPercentPen - flatArmorPenetration);
    }
    
    // Display methods
    public void showStats() {
        System.out.println("\n=== CHARACTER STATUS ===");
        System.out.println("Name: " + name);
        System.out.println("Level: " + level);
        System.out.println("HP: " + hp + "/" + maxHp);
        System.out.println("MP: " + mana + "/" + maxMana);
        System.out.println("Exp: " + experience + "/" + (level * 100));
        System.out.println("Gold: " + getGold());
        System.out.println("Weapon: " + (equippedWeapon != null ? equippedWeapon.getName() : "None"));
        System.out.println("Armor: " + (equippedArmor != null ? equippedArmor.getName() : "None"));
        System.out.println("Attack: " + getAttack());
        System.out.println("Defense: " + getDefense());
        System.out.println("Dexterity: " + dexterity);
        System.out.println("Armor Class: " + armorClass);
        System.out.println("Armor Penetration: " + percentArmorPenetration + "% + " + flatArmorPenetration + " flat");
        
        if (!skills.isEmpty()) {
            System.out.println("\nSkills:");
            for (Skill skill : skills) {
                System.out.println("  - " + skill.getName() + " (" + skill.getManaCost() + " MP)");
            }
        }
    }
    
    // Skill methods
    public void addSkill(Skill skill) {
        skills.add(skill);
    }
    
    // Progression methods
    public void gainExperience(int exp) {
        experience += exp;
        checkLevelUp();
    }
    
    private void checkLevelUp() {
        int expNeeded = level * 100;
        if (experience >= expNeeded) {
            levelUp();
        }
    }
    
    private void levelUp() {
        level++;
        maxHp += 5;
        hp = maxHp;
        maxMana += 3;
        mana = maxMana;
        attack += 2;
        defense += 1;
        dexterity += 1;
        
        System.out.println("Level up! You are now level " + level + "!");
        System.out.println("HP: " + maxHp + ", MP: " + maxMana + ", Attack: " + attack);
    }
}