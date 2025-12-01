package dnd.combat;

import dnd.characters.Monster;
import dnd.characters.Player;
import dnd.skills.Skill; // Keep this import
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class CombatSystem {
    private static final Scanner sc = new Scanner(System.in); // Make final
    private static final Random random = new Random(); // Make final
    private static final List<String> combatLog = new ArrayList<>(); // Make final
    
    public static CombatResult start(Player p, Monster m) {
        System.out.println("\nA wild " + m.getName() + " appears!");
        combatLog.clear();
        combatLog.add("Combat started between " + p.getName() + " and " + m.getName());
        
        // Initiative roll
        boolean playerFirst = initiativeRoll(p, m);
        
        while (p.isAlive() && m.isAlive()) {
            displayCombatStatus(p, m);
            
            if (playerFirst) {
                playerTurn(p, m);
                if (!m.isAlive()) break;
                monsterTurn(p, m);
            } else {
                monsterTurn(p, m);
                if (!p.isAlive()) break;
                playerTurn(p, m);
            }
        }
        
        return concludeCombat(p, m);
    }
    
    private static boolean initiativeRoll(Player p, Monster m) {
        int playerInitiative = p.getDexterity() + random.nextInt(20) + 1;
        int monsterInitiative = m.getDexterity() + random.nextInt(20) + 1;
        
        System.out.println("\n--- Initiative Roll ---");
        System.out.println(p.getName() + " rolls: " + playerInitiative);
        System.out.println(m.getName() + " rolls: " + monsterInitiative);
        
        if (playerInitiative >= monsterInitiative) {
            System.out.println(p.getName() + " goes first!");
            return true;
        } else {
            System.out.println(m.getName() + " goes first!");
            return false;
        }
    }
    
    private static void playerTurn(Player p, Monster m) {
        System.out.println("\n=== YOUR TURN ===");
        System.out.println("1. Attack");
        System.out.println("2. Use Skill");
        System.out.println("3. Use Item");
        System.out.println("4. Defend");
        System.out.println("5. Run");
        System.out.print("Choice: ");
        
        int choice = sc.nextInt();
        
        switch (choice) {
            case 1 -> performAttack(p, m);
            case 2 -> useSkill(p, m);
            case 3 -> useItem(p);
            case 4 -> defend(p);
            case 5 -> attemptEscape(p, m);
            default -> System.out.println("Invalid choice. You hesitate and lose your turn!");
        }
    }
    
    private static void performAttack(Player p, Monster m) {
    // D&D 5e: Roll d20, add attack bonus, compare to armor class
    int attackRoll = random.nextInt(20) + 1;
    boolean isCritical = (attackRoll == 20);
    
    // Calculate attack bonus: proficiency + ability modifier
    int proficiencyBonus = 2 + ((p.getLevel() - 1) / 4); // +2 at level 1-4, +3 at 5-8, etc.
    int abilityModifier = Math.max(0, (p.getDexterity() - 10) / 2); // Dex mod (min 0)
    int attackBonus = proficiencyBonus + abilityModifier;
    
    int totalAttack = attackRoll + attackBonus;
    
    // Monster's armor class (simplified)
    int monsterAC = 10 + (m.getDexterity() / 2); // Base 10 + half DEX
    
    System.out.println("\n=== ATTACK ===");
    System.out.println("Roll: " + attackRoll + " + Bonus: " + attackBonus + " = " + totalAttack);
    System.out.println("vs Monster AC: " + monsterAC);
    
    if (totalAttack >= monsterAC || isCritical) {
        // Calculate damage: weapon damage + ability modifier
        int weaponDamage = p.getAttack(); // This should be just weapon/base damage
        int abilityDamageMod = Math.max(0, abilityModifier);
        int baseDamage = weaponDamage + abilityDamageMod;
        
        if (isCritical) {
            baseDamage *= 2;
            System.out.println("CRITICAL HIT!");
        }
        
        // Apply armor penetration and calculate final damage
        int effectiveArmor = p.calculateEffectiveArmor(m);
        double armorReducedDamage = baseDamage * (100.0 / (100 + effectiveArmor));
        int finalDamage = (int) Math.max(1, Math.round(armorReducedDamage));
        
        m.takeDamage(baseDamage); // Monster applies resistance
        
        System.out.println(p.getName() + " hits for " + finalDamage + " damage!");
        combatLog.add(p.getName() + " hits " + m.getName() + " for " + finalDamage + " damage!");
        
    } else {
        System.out.println("You missed!");
        combatLog.add(p.getName() + " missed " + m.getName());
    }
}
    
    private static void useSkill(Player p, Monster m) {
        List<Skill> skills = p.getSkills(); // This will now work!
        if (skills.isEmpty()) {
            System.out.println("You have no skills available!");
            return;
        }
        
        System.out.println("\nAvailable Skills:");
        for (int i = 0; i < skills.size(); i++) {
            Skill skill = skills.get(i);
            System.out.println((i + 1) + ". " + skill.getName() + 
                             " (Cost: " + skill.getManaCost() + " MP) - " + skill.getDescription());
        }
        System.out.print("Choose skill: ");
        
        int choice = sc.nextInt() - 1;
        if (choice >= 0 && choice < skills.size()) {
            Skill skill = skills.get(choice);
            if (p.getMana() >= skill.getManaCost()) {
                skill.use(p, m);
                p.reduceMana(skill.getManaCost());
                combatLog.add(p.getName() + " used " + skill.getName() + " on " + m.getName());
            } else {
                System.out.println("Not enough mana!");
            }
        } else {
            System.out.println("Invalid skill choice!");
        }
    }
    
    private static void useItem(Player p) {
    // Simply call the player's item menu - it now uses the Inventory class
    p.useItemMenu(sc);
    
    // Log the action
    combatLog.add(p.getName() + " used an item");
    }
    
    private static void defend(Player p) {
        p.setDefending(true);
        System.out.println("You take a defensive stance! Damage reduced next turn.");
        combatLog.add(p.getName() + " is defending");
    }
    
    private static void attemptEscape(Player p, Monster m) {
        int escapeChance = 40 + p.getDexterity() - m.getDexterity();
        if (random.nextInt(100) < escapeChance) {
            System.out.println("You successfully escaped!");
            combatLog.add(p.getName() + " escaped from combat");
        } else {
            System.out.println("Escape failed!");
            combatLog.add(p.getName() + " failed to escape");
        }
    }
    
    private static void monsterTurn(Player p, Monster m) {
        System.out.println("\n=== " + m.getName().toUpperCase() + "'S TURN ===");
        
        // Simple AI for monster
        if (m.getHp() < m.getMaxHp() * 0.3 && random.nextInt(100) < 30) {
            // Monster might use special ability when low on health
            useMonsterAbility(m, p);
        } else {
            performMonsterAttack(p, m);
        }
        
        // Reset player defense
        p.setDefending(false);
    }
    
    private static void performMonsterAttack(Player p, Monster m) {
        int attackRoll = random.nextInt(20) + 1;
        boolean isCritical = (attackRoll == 20);
        
        if (attackRoll >= p.getArmorClass() || isCritical) {
            int baseDamage = m.getAttack();
            if (isCritical) {
                baseDamage *= 2;
                System.out.println("CRITICAL HIT from " + m.getName() + "!");
            }
            
            // Apply defense reduction if player was defending
            if (p.isDefending()) {
                baseDamage = Math.max(1, baseDamage / 2);
            }
            
            int finalDamage = Math.max(1, baseDamage - p.getDefense());
            p.takeDamage(finalDamage);
            
            String log = m.getName() + " hits " + p.getName() + " for " + finalDamage + " damage!" + 
                        (isCritical ? " (Critical!)" : "");
            System.out.println(log);
            combatLog.add(log);
            
        } else {
            System.out.println(m.getName() + " missed!");
            combatLog.add(m.getName() + " missed " + p.getName());
        }
    }
    
    private static void useMonsterAbility(Monster m, Player p) {
        System.out.println(m.getName() + " uses a special attack!");
        // Implement monster special abilities
        int specialDamage = m.getAttack() + 5;
        p.takeDamage(specialDamage);
        System.out.println("It deals " + specialDamage + " damage!");
        combatLog.add(m.getName() + " used special attack on " + p.getName());
    }
    
    private static void displayCombatStatus(Player p, Monster m) {
        System.out.println("\n=== COMBAT STATUS ===");
        System.out.println(p.getName() + ": " + p.getHp() + "/" + p.getMaxHp() + " HP | " + 
                          p.getMana() + "/" + p.getMaxMana() + " MP");
        System.out.println(m.getName() + ": " + m.getHp() + "/" + m.getMaxHp() + " HP");
        System.out.println("---------------------");
    }
    
    private static CombatResult concludeCombat(Player p, Monster m) {
        CombatResult result = new CombatResult();
        
        if (p.isAlive()) {
            int expGained = m.getExpReward();
            int goldGained = m.getGoldReward();
            
            p.gainExperience(expGained);
            p.gainGold(goldGained);
            
            System.out.println("\n=== VICTORY! ===");
            System.out.println("You gained " + expGained + " experience and " + goldGained + " gold!");
            
            result.setVictory(true);
            result.setExpGained(expGained);
            result.setGoldGained(goldGained);
        } else {
            System.out.println("\n=== DEFEAT ===");
            System.out.println("You have fallen in battle...");
            result.setVictory(false);
        }
        
        // Display combat log
        System.out.println("\n--- Combat Log ---");
        for (String log : combatLog) {
            System.out.println(log);
        }
        
        return result;
    }
}