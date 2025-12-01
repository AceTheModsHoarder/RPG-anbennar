package dnd.items;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Weapon> weapons;
    private List<Armor> armors;
    private List<Consumable> consumables;
    private int gold;
    
    public Inventory() {
        this.weapons = new ArrayList<>();
        this.armors = new ArrayList<>();
        this.consumables = new ArrayList<>();
        this.gold = 0;
    }
    
    public void addWeapon(Weapon weapon) {
        weapons.add(weapon);
    }
    
    public void addArmor(Armor armor) {
        armors.add(armor);
    }
    
    public void addConsumable(Consumable consumable) {
        // Check if consumable already exists to stack quantities
        for (Consumable existing : consumables) {
            if (existing.getName().equals(consumable.getName())) {
                existing.addQuantity(consumable.getQuantity());
                return;
            }
        }
        consumables.add(consumable);
    }
    
    public void addGold(int amount) {
        gold += amount;
    }

    public void clear() {
        weapons.clear();
        armors.clear();
        consumables.clear();
    }
    
    // Getters
    public List<Weapon> getWeapons() { return weapons; }
    public List<Armor> getArmors() { return armors; }
    public List<Consumable> getConsumables() { return consumables; }
    public int getGold() { return gold; }
    
    public boolean removeGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            return true;
        }
        return false;
    }
    
     // Remove a specific weapon
    public boolean removeWeapon(Weapon weapon) {
        return weapons.remove(weapon);
    }
    
    // Remove a specific armor
    public boolean removeArmor(Armor armor) {
        return armors.remove(armor);
    }
    
    // Get total number of items
    public int getTotalItems() {
        return weapons.size() + armors.size() + consumables.size();
    }
    
    // Check if inventory is empty
    public boolean isEmpty() {
        return weapons.isEmpty() && armors.isEmpty() && consumables.isEmpty();
    }

    // Get iterator for all items (for sellAllItems method)
    public Iterator<Object> getAllItemsIterator() {
        List<Object> allItems = new ArrayList<>();
        allItems.addAll(weapons);
        allItems.addAll(armors);
        allItems.addAll(consumables);
        return allItems.iterator();
    }
    
    // Remove all items of a specific list (for sellAll)
    public void removeAllWeapons() {
        weapons.clear();
    }
    
    public void removeAllArmor() {
        armors.clear();
    }
    
    public void removeAllConsumables() {
        consumables.clear();
    }


    // Remove a specific consumable
    public boolean removeConsumable(Consumable consumable) {
        return consumables.remove(consumable);
    }
    
    // Display methods
    public void displayInventory() {
        System.out.println("\n=== INVENTORY ===");
        System.out.println("Gold: " + gold);
        
        System.out.println("\nWeapons:");
        if (weapons.isEmpty()) {
            System.out.println("  None");
        } else {
            for (int i = 0; i < weapons.size(); i++) {
                Weapon weapon = weapons.get(i);
                System.out.println("  " + (i + 1) + ". " + weapon.getName() + " (+" + weapon.getAttackBonus() + " ATK)");
            }
        }
        
        System.out.println("\nArmor:");
        if (armors.isEmpty()) {
            System.out.println("  None");
        } else {
            for (int i = 0; i < armors.size(); i++) {
                Armor armor = armors.get(i);
                System.out.println("  " + (i + 1) + ". " + armor.getName() + " (+" + armor.getDefenseBonus() + " DEF)");
            }
        }
        
        System.out.println("\nConsumables:");
        if (consumables.isEmpty()) {
            System.out.println("  None");
        } else {
            for (int i = 0; i < consumables.size(); i++) {
                Consumable consumable = consumables.get(i);
                System.out.println("  " + (i + 1) + ". " + consumable.getName() + 
                                 " - " + consumable.getDescription() + 
                                 " (x" + consumable.getQuantity() + ")");
            }
        }
    }
}