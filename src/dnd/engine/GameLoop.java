package dnd.engine;

import dnd.characters.Monster;
import dnd.characters.Player;
import dnd.combat.CombatSystem;
import dnd.items.Armor;
import dnd.items.Consumable;
import dnd.items.Inventory;
import dnd.items.Potion;
import dnd.items.Weapon;
import dnd.skills.BasicAttack;
import dnd.skills.Fireball;
import dnd.skills.Heal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class GameLoop {
    private final Scanner sc = new Scanner(System.in);
    private final Random random = new Random();
    private Player player;
    private boolean gameRunning;
    
    // Available monsters for random encounters
    private final List<Monster> monsters = new ArrayList<>();
    
    // Available items for shops/chests
    private final List<Weapon> availableWeapons = new ArrayList<>();
    private final List<Armor> availableArmor = new ArrayList<>();
    private final List<Potion> availablePotions = new ArrayList<>();
    
    public GameLoop() {
        initializeGame();
    }
    
    private void initializeGame() {
        System.out.println("=== WELCOME TO D&D ANBENNAR ===");
        System.out.print("Enter your character name: ");
        String name = sc.nextLine();
        
        // Create player
        player = new Player(name);
        
        // Add starting skills
        player.addSkill(new BasicAttack());
        player.addSkill(new Fireball());
        player.addSkill(new Heal());
        
        // Add starting items
        player.addItem(new Weapon("Rusty Dagger", 1));
        player.addItem(Potion.createHealthPotion());
        
        // Initialize monsters
        initializeMonsters();
        
        // Initialize available items
        initializeItems();
        
        gameRunning = true;
        
        System.out.println("\nWelcome, " + player.getName() + "!");
        System.out.println("You start your adventure with a Rusty Dagger and a Health Potion.");
        System.out.println("You have learned: Power Attack, Fireball, and Heal skills.");
    }
    
    private void initializeMonsters() {
        monsters.add(new Monster("Goblin", 15, 4, 2));
        monsters.add(new Monster("Orc", 25, 6, 4));
        monsters.add(new Monster("Dragon", 50, 10, 6));
        monsters.add(new Monster("Skeleton", 20, 5, 3));
        monsters.add(new Monster("Bandit", 18, 5, 2));
        monsters.add(new Monster("Wolf", 12, 6, 1));
    }
    
    private void initializeItems() {
        // Weapons
        availableWeapons.add(new Weapon("Iron Sword", 3));
        availableWeapons.add(new Weapon("Steel Axe", 4));
        availableWeapons.add(new Weapon("Magic Staff", 2));
        availableWeapons.add(new Weapon("Greatsword", 5));
        
        // Armor - Use the 2-parameter constructor
        availableArmor.add(new Armor("Leather Armor", 2));
        availableArmor.add(new Armor("Chainmail", 4));
        availableArmor.add(new Armor("Plate Armor", 6));
        
        // Potions
        availablePotions.add(Potion.createHealthPotion());
        availablePotions.add(Potion.createGreaterHealthPotion());
    }
    
    public void startGame() {
        while (gameRunning && player.isAlive()) {
            displayMainMenu();
            int choice = getMenuChoice(1, 6);
            
            switch (choice) {
                case 1 -> explore();
                case 2 -> rest();
                case 3 -> player.showStats();
                case 4 -> player.useItemMenu(sc);
                case 5 -> visitShop();
                case 6 -> exitGame();
            }
        }
        
        if (!player.isAlive()) {
            System.out.println("\n=== GAME OVER ===");
            System.out.println("Your adventure has come to an end...");
            System.out.println("You reached level " + player.getLevel() + "!");
        }
    }
    
    private void displayMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Explore");
        System.out.println("2. Rest (Heal HP/MP)");
        System.out.println("3. Check Status");
        System.out.println("4. Use Items");
        System.out.println("5. Visit Shop");
        System.out.println("6. Exit Game");
        System.out.print("Choose your action: ");
    }
    
    private void explore() {
        System.out.println("\nYou venture into the wilderness...");
        
        // Random encounter chance: 70%
        if (random.nextInt(100) < 70) {
            encounterMonster();
        } else {
            // Other exploration events
            int event = random.nextInt(100);
            if (event < 30) {
                findTreasure();
            } else if (event < 50) {
                findSafeHaven();
            } else {
                System.out.println("The path is quiet. You find nothing of interest.");
            }
        }
    }
    
    private void encounterMonster() {
    Monster monster = getRandomMonster();
    System.out.println("A wild " + monster.getName() + " appears!");
    
    // Use fully qualified name
    dnd.combat.CombatResult result = CombatSystem.start(player, monster);
    
   if (result.isVictory()) {
    System.out.println("You emerge victorious from the battle!");
    
    // Base loot chance: 80% (was 60%)
    int lootChance = 80;
    
    // Higher chance for tougher monsters
    lootChance += (monster.getMaxHp() / 10);
    
    if (random.nextInt(100) < lootChance) {
        dropLoot();
    }
    
    // Always get at least some gold
    int minGold = 5 + (player.getLevel() * 2);
    player.gainGold(minGold);
    System.out.println("You also find " + minGold + " gold on the corpse.");
    } else {
            System.out.println("You have been defeated...");
        }
    }
    
    private Monster getRandomMonster() {
        // Higher level players encounter stronger monsters
        int monsterIndex;
        if (player.getLevel() >= 5) {
            monsterIndex = random.nextInt(monsters.size());
        } else if (player.getLevel() >= 3) {
            monsterIndex = random.nextInt(Math.min(4, monsters.size()));
        } else {
            monsterIndex = random.nextInt(Math.min(2, monsters.size()));
        }
        
        return monsters.get(monsterIndex);
    }
    
    private void dropLoot() {
    System.out.println("\n=== LOOT DROPPED ===");
    
    // Multiple loot rolls based on monster difficulty
    int lootRolls = 1 + random.nextInt(3); // 1-3 loot items
    
    for (int i = 0; i < lootRolls; i++) {
        generateLootItem();
    }
}

private void generateLootItem() {
    int lootType = random.nextInt(100);
    
    if (lootType < 40) { // 40% chance: Gold
        generateGold();
    } else if (lootType < 70) { // 30% chance: Potions
        generatePotion();
    } else if (lootType < 85) { // 15% chance: Weapons
        generateWeapon();
    } else if (lootType < 95) { // 10% chance: Armor
        generateArmor();
    } else { // 5% chance: Rare/Special
        generateRareItem();
    }
}

private void generateGold() {
    int goldMin = 10;
    int goldMax = 50 + (player.getLevel() * 5);
    int gold = goldMin + random.nextInt(goldMax - goldMin + 1);
    player.gainGold(gold);
    System.out.println("  Found " + gold + " gold!");
}

private void generatePotion() {
    int potionType = random.nextInt(100);
    
    if (potionType < 60) { // 60%: Health Potion
        player.addItem(Potion.createHealthPotion());
        System.out.println("  Found a Health Potion!");
    } else if (potionType < 90) { // 30%: Greater Health Potion
        player.addItem(Potion.createGreaterHealthPotion());
        System.out.println("  Found a Greater Health Potion!");
    } else { // 10%: Special Potion
        player.addItem(new Potion("Mana Potion", "Restores 15 MP", 0) {
            @Override
            public void use(Player player) {
                player.restoreMana(15);
                System.out.println("You drink the Mana Potion and restore 15 MP!");
            }
        });
        System.out.println("  Found a rare Mana Potion!");
    }
}

private void generateWeapon() {
    // Better weapons at higher levels
    int weaponTier = Math.min(3, player.getLevel() / 3);
    
    List<Weapon> tierWeapons = new ArrayList<>();
    
    // Tier 0 weapons (starting level)
    tierWeapons.add(new Weapon("Rusty Sword", 2, 20, 0, 1));
    tierWeapons.add(new Weapon("Wooden Club", 1, 15, 0, 0));
    
    // Tier 1 weapons (level 3+)
    if (weaponTier >= 1) {
        tierWeapons.add(new Weapon("Iron Sword", 3, 50, 0, 3));
        tierWeapons.add(new Weapon("Steel Dagger", 2, 40, 10, 2));
    }
    
    // Tier 2 weapons (level 6+)
    if (weaponTier >= 2) {
        tierWeapons.add(new Weapon("Longsword", 4, 100, 0, 5));
        tierWeapons.add(new Weapon("Battle Axe", 5, 120, 20, 0));
    }
    
    // Tier 3 weapons (level 9+)
    if (weaponTier >= 3) {
        tierWeapons.add(new Weapon("Magic Sword", 6, 250, 30, 8));
        tierWeapons.add(new Weapon("Dragonbone Axe", 7, 300, 25, 10));
    }
    
    Weapon weapon = tierWeapons.get(random.nextInt(tierWeapons.size()));
    player.addItem(weapon);
    System.out.println("  Found a " + weapon.getName() + " (+" + weapon.getAttackBonus() + " ATK)!");
}

private void generateArmor() {
    int armorTier = Math.min(2, player.getLevel() / 4);
    
    List<Armor> tierArmor = new ArrayList<>();
    
    // Tier 0 armor
    tierArmor.add(new Armor("Leather Vest", 1, 25));
    tierArmor.add(new Armor("Padded Jacket", 2, 40));
    
    // Tier 1 armor (level 4+)
    if (armorTier >= 1) {
        tierArmor.add(new Armor("Chain Shirt", 3, 80));
        tierArmor.add(new Armor("Scale Mail", 4, 120));
    }
    
    // Tier 2 armor (level 8+)
    if (armorTier >= 2) {
        tierArmor.add(new Armor("Plate Armor", 5, 200));
        tierArmor.add(new Armor("Dragon Scale Armor", 6, 300));
    }
    
    Armor armor = tierArmor.get(random.nextInt(tierArmor.size()));
    player.addItem(armor);
    System.out.println("  Found " + armor.getName() + " (+" + armor.getDefenseBonus() + " DEF)!");
}

private void generateRareItem() {
    int rareType = random.nextInt(100);
    
    if (rareType < 50) { // 50%: Gemstones (sellable for high gold)
        String[] gems = {"Ruby", "Sapphire", "Emerald", "Diamond", "Topaz"};
        String gem = gems[random.nextInt(gems.length)];
        int gemValue = 50 + random.nextInt(100);
        
        // Create a sellable gem item
        player.addItem(new Object() {
            @Override
            public String toString() {
                return gem + " (Worth " + gemValue + " gold)";
            }
        });
        
        // Store gem value somehow (you might want to create a proper Gem class)
        System.out.println("  Found a rare " + gem + "! (Can be sold at shop)");
        
    } else if (rareType < 80) { // 30%: Special Weapon
        Weapon specialWeapon = new Weapon("Enchanted Blade", 5, 200, 25, 8);
        player.addItem(specialWeapon);
        System.out.println("  Found an Enchanted Blade! (+5 ATK, 25% armor pen)");
        
    } else { // 20%: Unique Item
        System.out.println("  Found a mysterious artifact!");
        // This could grant a permanent buff or unlock something
        player.gainExperience(50); // Bonus XP for rare find
    }
}
    
    private void findTreasure() {
    System.out.println("\nYou discover a hidden treasure chest!");
    
    // Chest quality based on player level
    int chestQuality = 1 + (player.getLevel() / 3);
    int itemsInChest = 2 + random.nextInt(chestQuality);
    
    System.out.println("The chest contains " + itemsInChest + " items:");
    
    for (int i = 0; i < itemsInChest; i++) {
        generateTreasureItem(chestQuality);
    }
}

private void generateTreasureItem(int chestQuality) {
    int itemType = random.nextInt(100);
    
    // Better chances for good items in higher quality chests
    if (itemType < 30) { // Gold
        int goldMultiplier = 5 * chestQuality;
        int gold = (10 + random.nextInt(40)) * goldMultiplier;
        player.gainGold(gold);
        System.out.println("  " + gold + " gold coins");
        
    } else if (itemType < 60) { // Potions (better in better chests)
        if (chestQuality >= 3 && random.nextBoolean()) {
            player.addItem(Potion.createGreaterHealthPotion());
            System.out.println("  Greater Health Potion");
        } else {
            player.addItem(Potion.createHealthPotion());
            System.out.println("  Health Potion");
        }
        
    } else if (itemType < 80) { // Equipment
        if (random.nextBoolean()) {
            generateWeapon(); // Use the same method from loot
        } else {
            generateArmor();  // Use the same method from loot
        }
        
    } else { // Special/Artifact (rare)
        if (chestQuality >= 2) {
            generateRareItem();
        } else {
            // Fallback to gold for low-quality chests
            int gold = 100 * chestQuality;
            player.gainGold(gold);
            System.out.println("  " + gold + " gold coins");
        }
    }
}
    
    private void findSafeHaven() {
        System.out.println("You find a peaceful clearing. It's safe to rest here.");
        System.out.println("Your HP and MP are fully restored!");
        
        player.heal(player.getMaxHp() - player.getHp());
        player.restoreMana(player.getMaxMana() - player.getMana());
    }
    
    private void rest() {
        System.out.println("\nYou take time to rest and recover...");
        
        int hpHealed = player.getMaxHp() / 2;
        int mpHealed = player.getMaxMana() / 2;
        
        player.heal(hpHealed);
        player.restoreMana(mpHealed);
        
        System.out.println("Restored " + hpHealed + " HP and " + mpHealed + " MP.");
        System.out.println("Current HP: " + player.getHp() + "/" + player.getMaxHp());
        System.out.println("Current MP: " + player.getMana() + "/" + player.getMaxMana());
    }
    
    private void visitShop() {
        System.out.println("\n=== WELCOME TO THE SHOP ===");
        System.out.println("Your gold: " + player.getGold());
        
        boolean shopping = true;
        while (shopping) {
            System.out.println("\n1. Buy Weapons");
            System.out.println("2. Buy Armor");
            System.out.println("3. Buy Potions");
            System.out.println("4. Sell Items");
            System.out.println("5. Leave Shop");
            System.out.print("What would you like to do? ");
            
            int choice = getMenuChoice(1, 5);
            
            switch (choice) {
                case 1 -> buyWeapons();
                case 2 -> buyArmor();
                case 3 -> buyPotions();
                case 4 -> sellItems();
                case 5 -> shopping = false;
            }
        }
        System.out.println("Thank you for your business!");
    }
    
    private void buyWeapons() {
        System.out.println("\n=== WEAPONS ===");
        for (int i = 0; i < availableWeapons.size(); i++) {
            Weapon weapon = availableWeapons.get(i);
            int price = calculatePrice(weapon.getValue());
            System.out.println((i + 1) + ". " + weapon.getName() + " (+" + 
                             weapon.getAttackBonus() + " ATK) - " + 
                             price + " gold");
        }
        System.out.println((availableWeapons.size() + 1) + ". Back");
        
        int choice = getMenuChoice(1, availableWeapons.size() + 1);
        if (choice <= availableWeapons.size()) {
            Weapon weapon = availableWeapons.get(choice - 1);
            int price = calculatePrice(weapon.getValue());
            
            if (player.getGold() >= price) {
                player.addItem(weapon);
                player.gainGold(-price);
                System.out.println("Purchased " + weapon.getName() + " for " + price + " gold!");
            } else {
                System.out.println("Not enough gold!");
            }
        }
    }
    
    private void buyArmor() {
        System.out.println("\n=== ARMOR ===");
        for (int i = 0; i < availableArmor.size(); i++) {
            Armor armor = availableArmor.get(i);
            int price = calculatePrice(armor.getValue());
            System.out.println((i + 1) + ". " + armor.getName() + " (+" + 
                             armor.getDefenseBonus() + " DEF) - " + 
                             price + " gold");
        }
        System.out.println((availableArmor.size() + 1) + ". Back");
        
        int choice = getMenuChoice(1, availableArmor.size() + 1);
        if (choice <= availableArmor.size()) {
            Armor armor = availableArmor.get(choice - 1);
            int price = calculatePrice(armor.getValue());
            
            if (player.getGold() >= price) {
                player.addItem(armor);
                player.gainGold(-price);
                System.out.println("Purchased " + armor.getName() + " for " + price + " gold!");
            } else {
                System.out.println("Not enough gold!");
            }
        }
    }
    
    private void buyPotions() {
        System.out.println("\n=== POTIONS ===");
        System.out.println("1. Health Potion (20 HP) - 30 gold");
        System.out.println("2. Greater Health Potion (50 HP) - 70 gold");
        System.out.println("3. Back");
        
        int choice = getMenuChoice(1, 3);
        if (choice == 1) {
            if (player.getGold() >= 30) {
                player.addItem(Potion.createHealthPotion());
                player.gainGold(-30);
                System.out.println("Purchased Health Potion for 30 gold!");
            } else {
                System.out.println("Not enough gold!");
            }
        } else if (choice == 2) {
            if (player.getGold() >= 70) {
                player.addItem(Potion.createGreaterHealthPotion());
                player.gainGold(-70);
                System.out.println("Purchased Greater Health Potion for 70 gold!");
            } else {
                System.out.println("Not enough gold!");
            }
        }
    }

    private void sellItems() {
    System.out.println("\n=== SELL ITEMS ===");
    
    Inventory inventory = player.getInventory();
    if (inventory.isEmpty()) {
        System.out.println("Your inventory is empty!");
        return;
    }
    
    System.out.println("Your gold: " + player.getGold());
    
    boolean selling = true;
    while (selling) {
        System.out.println("\nWhat would you like to sell?");
        int optionNumber = 1;
        
        if (!inventory.getWeapons().isEmpty()) {
            System.out.println(optionNumber + ". Weapons (" + inventory.getWeapons().size() + ")");
            optionNumber++;
        }
        
        if (!inventory.getArmors().isEmpty()) {
            System.out.println(optionNumber + ". Armor (" + inventory.getArmors().size() + ")");
            optionNumber++;
        }
        
        if (!inventory.getConsumables().isEmpty()) {
            System.out.println(optionNumber + ". Consumables (" + inventory.getConsumables().size() + ")");
            optionNumber++;
        }
        
        System.out.println(optionNumber + ". Sell All");
        int sellAllOption = optionNumber;
        optionNumber++;
        
        System.out.println(optionNumber + ". Back to Shop");
        int backOption = optionNumber;
        
        System.out.print("Choose: ");
        int choice = getMenuChoice(1, optionNumber);
        
        if (choice == backOption) {
            selling = false;
        } else if (choice == sellAllOption) {
            sellAllItems();
        } else {
            // Determine which category was selected
            int category = 1;
            if (!inventory.getWeapons().isEmpty()) {
                if (choice == category) {
                    sellWeapons();
                    break;
                }
                category++;
            }
            if (!inventory.getArmors().isEmpty()) {
                if (choice == category) {
                    sellArmor();
                    break;
                }
                category++;
            }
            if (!inventory.getConsumables().isEmpty()) {
                if (choice == category) {
                    sellConsumables();
                    break;
                }
            }
        }
    }
}

private void sellWeapons() {
    List<Weapon> weapons = player.getInventory().getWeapons();
    if (weapons.isEmpty()) {
        System.out.println("No weapons to sell!");
        return;
    }
    
    System.out.println("\n=== SELL WEAPONS ===");
    for (int i = 0; i < weapons.size(); i++) {
        Weapon weapon = weapons.get(i);
        int sellPrice = calculateSellPrice(weapon.getValue());
        System.out.println((i + 1) + ". " + weapon.getName() + 
                         " (+" + weapon.getAttackBonus() + " ATK)" +
                         " - " + sellPrice + " gold");
    }
    System.out.println((weapons.size() + 1) + ". Sell All Weapons");
    System.out.println((weapons.size() + 2) + ". Back");
    
    int choice = getMenuChoice(1, weapons.size() + 2);
    
    if (choice == weapons.size() + 2) {
        return; // Back
    } else if (choice == weapons.size() + 1) {
        // Sell all weapons
        int totalGold = 0;
        for (Weapon weapon : weapons) {
            totalGold += calculateSellPrice(weapon.getValue());
        }
        player.getInventory().removeAllWeapons();
        player.gainGold(totalGold);
        System.out.println("Sold all weapons for " + totalGold + " gold!");
    } else {
        // Sell single weapon
        Weapon weapon = weapons.get(choice - 1);
        int sellPrice = calculateSellPrice(weapon.getValue());
        player.gainGold(sellPrice);
        player.getInventory().removeWeapon(weapon);
        System.out.println("Sold " + weapon.getName() + " for " + sellPrice + " gold!");
    }
}

private void sellArmor() {
    List<Armor> armors = player.getInventory().getArmors();
    if (armors.isEmpty()) {
        System.out.println("No armor to sell!");
        return;
    }
    
    System.out.println("\n=== SELL ARMOR ===");
    for (int i = 0; i < armors.size(); i++) {
        Armor armor = armors.get(i);
        int sellPrice = calculateSellPrice(armor.getValue());
        System.out.println((i + 1) + ". " + armor.getName() + 
                         " (+" + armor.getDefenseBonus() + " DEF)" +
                         " - " + sellPrice + " gold");
    }
    System.out.println((armors.size() + 1) + ". Sell All Armor");
    System.out.println((armors.size() + 2) + ". Back");
    
    int choice = getMenuChoice(1, armors.size() + 2);
    
    if (choice == armors.size() + 2) {
        return; // Back
    } else if (choice == armors.size() + 1) {
        // Sell all armor
        int totalGold = 0;
        for (Armor armor : armors) {
            totalGold += calculateSellPrice(armor.getValue());
        }
        player.getInventory().removeAllArmor();
        player.gainGold(totalGold);
        System.out.println("Sold all armor for " + totalGold + " gold!");
    } else {
        // Sell single armor
        Armor armor = armors.get(choice - 1);
        int sellPrice = calculateSellPrice(armor.getValue());
        player.gainGold(sellPrice);
        player.getInventory().removeArmor(armor);
        System.out.println("Sold " + armor.getName() + " for " + sellPrice + " gold!");
    }
}

private void sellConsumables() {
    List<Consumable> consumables = player.getInventory().getConsumables();
    if (consumables.isEmpty()) {
        System.out.println("No consumables to sell!");
        return;
    }
    
    // Group consumables by name
    Map<String, Integer> consumableCounts = new HashMap<>();
    Map<String, Integer> consumablePrices = new HashMap<>();
    
    for (Consumable consumable : consumables) {
        String name = consumable.getName();
        consumableCounts.put(name, consumableCounts.getOrDefault(name, 0) + 1);
        
        // Set prices based on name
        if (name.contains("Health Potion")) {
            consumablePrices.put(name, name.contains("Greater") ? 25 : 10);
        } else {
            consumablePrices.put(name, 15); // Default price
        }
    }
    
    System.out.println("\n=== SELL CONSUMABLES ===");
    List<String> consumableNames = new ArrayList<>(consumableCounts.keySet());
    for (int i = 0; i < consumableNames.size(); i++) {
        String name = consumableNames.get(i);
        int count = consumableCounts.get(name);
        int price = consumablePrices.get(name);
        System.out.println((i + 1) + ". " + name + " (x" + count + ") - " + 
                         price + " gold each");
    }
    System.out.println((consumableNames.size() + 1) + ". Sell All Consumables");
    System.out.println((consumableNames.size() + 2) + ". Back");
    
    int choice = getMenuChoice(1, consumableNames.size() + 2);
    
    if (choice == consumableNames.size() + 2) {
        return; // Back
    } else if (choice == consumableNames.size() + 1) {
        // Sell all consumables
        int totalGold = 0;
        for (Consumable consumable : consumables) {
            String name = consumable.getName();
            totalGold += consumablePrices.get(name);
        }
        player.getInventory().removeAllConsumables();
        player.gainGold(totalGold);
        System.out.println("Sold all consumables for " + totalGold + " gold!");
    } else {
        // Sell one type of consumable
        String selectedName = consumableNames.get(choice - 1);
        int availableCount = consumableCounts.get(selectedName);
        
        System.out.print("How many " + selectedName + "s to sell? (You have " + 
                        availableCount + "): ");
        int quantity = getMenuChoice(1, availableCount);
        int sellPrice = consumablePrices.get(selectedName) * quantity;
        
        // Remove the specified quantity
        int removed = 0;
        List<Consumable> toRemove = new ArrayList<>();
        for (Consumable consumable : consumables) {
            if (consumable.getName().equals(selectedName)) {
                toRemove.add(consumable);
                removed++;
                if (removed >= quantity) break;
            }
        }
        
        for (Consumable consumable : toRemove) {
            player.getInventory().removeConsumable(consumable);
        }
        
        player.gainGold(sellPrice);
        System.out.println("Sold " + quantity + " " + selectedName + 
                         "(s) for " + sellPrice + " gold!");
    }
}

private void sellAllItems() {
    Inventory inventory = player.getInventory();
    if (inventory.isEmpty()) {
        System.out.println("No items to sell!");
        return;
    }
    
    int totalGold = 0;
    
    // Calculate weapon values
    for (Weapon weapon : inventory.getWeapons()) {
        totalGold += calculateSellPrice(weapon.getValue());
    }
    
    // Calculate armor values
    for (Armor armor : inventory.getArmors()) {
        totalGold += calculateSellPrice(armor.getValue());
    }
    
    // Calculate consumable values
    for (Consumable consumable : inventory.getConsumables()) {
        String name = consumable.getName();
        if (name.contains("Health Potion")) {
            totalGold += name.contains("Greater") ? 25 : 10;
        } else {
            totalGold += 15;
        }
    }
    
    System.out.print("Sell all " + inventory.getTotalItems() + " items for " + 
                    totalGold + " gold? (y/n): ");
    String confirm = sc.nextLine().toLowerCase();
    
    if (confirm.equals("y") || confirm.equals("yes")) {
        inventory.clear();
        player.gainGold(totalGold);
        System.out.println("Sold all items for " + totalGold + " gold!");
    } else {
        System.out.println("Sale cancelled.");
    }
}

private int calculateSellPrice(int baseValue) {
    // Sell price is 50% of base value
    return Math.max(5, baseValue / 2);
}

private int calculatePrice(int baseValue) {
    // Buy price is 150% of base value
    return Math.max(10, (int) (baseValue * 1.5));
}

    private void exitGame() {
        System.out.println("\nExiting the game...");
        gameRunning = false;
    }

    private int getMenuChoice(int min, int max) {
        while (true) {
            try {
                int choice = sc.nextInt();
                sc.nextLine(); // Clear the buffer
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.print("Please enter a number between " + min + " and " + max + ": ");
                }
            } catch (Exception e) {
                System.out.print("Please enter a valid number: ");
                sc.next(); // Clear invalid input
            }
        }
    }
}