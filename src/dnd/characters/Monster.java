package dnd.characters;

public class Monster {
    private String name;
    private int hp;
    private int maxHp;
    private int attack;
    private int armor; // League-style armor (percentage reduction)
    private int damageResistance; // Flat damage reduction (can be reduced by spells/effects)
    private int dexterity;
    private int expReward;
    private int goldReward;
    private boolean isAlive;
    
    public Monster(String name, int hp, int attack, int armor, int damageResistance) {
        this.name = name;
        this.maxHp = hp;
        this.hp = maxHp;
        this.attack = attack;
        this.armor = armor;
        this.damageResistance = damageResistance;
        this.dexterity = 10;
        this.expReward = (hp + attack + armor) * 2;
        this.goldReward = (hp + attack) / 2;
        this.isAlive = true;
    }
    
    // For backward compatibility
    public Monster(String name, int hp, int attack, int defense) {
        this(name, hp, attack, defense, 0);
    }
    
    // Getters
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAttack() { return attack; }
    public int getArmor() { return armor; }
    public int getDamageResistance() { return damageResistance; }
    public int getDexterity() { return dexterity; }
    public int getExpReward() { return expReward; }
    public int getGoldReward() { return goldReward; }
    public boolean isAlive() { return isAlive; }
    
    // Setters for resistance (for spells/effects to modify)
    public void setDamageResistance(int resistance) {
        this.damageResistance = Math.max(0, resistance); // Can't go below 0
    }
    
    public void reduceDamageResistance(int amount) {
    this.damageResistance = Math.max(0, this.damageResistance - amount);
    }
    
    public void increaseDamageResistance(int amount) {
        this.damageResistance += amount;
    }
    
    public void setArmor(int armor) {
    this.armor = Math.max(0, armor); // Armor can't be negative
    }

    // Calculate post-mitigation damage (League of Legends formula + flat resistance)
    public int calculatePostMitigationDamage(int rawDamage) {
        // Apply armor reduction first (League formula)
        double armorReducedDamage = rawDamage * (100.0 / (100 + armor));
        
        // Apply flat damage resistance (subtract from damage)
        double finalDamage = Math.max(1, armorReducedDamage - damageResistance);
        
        return (int) Math.round(finalDamage);
    }
    
    public void takeDamage(int rawDamage) {
        int actualDamage = calculatePostMitigationDamage(rawDamage);
        hp = Math.max(0, hp - actualDamage);
        if (hp == 0) {
            isAlive = false;
        }
    }
    
    // Factory methods
    public static Monster createGoblin() {
        return new Monster("Goblin", 15, 4, 0, 0);
    }
    
    public static Monster createOrc() {
        return new Monster("Orc", 25, 6, 10, 2);
    }
    
    public static Monster createDragon() {
        return new Monster("Dragon", 50, 10, 40, 10);
    }

    // Tier 1 Monsters (Easy - Level 1-3)
    public static Monster createRat() {
        return new Monster("Giant Rat", 8, 3, 5, 0);
    }

    public static Monster createSpider() {
        return new Monster("Giant Spider", 10, 4, 6, 1);
    }

    public static Monster createKobold() {
        return new Monster("Kobold", 12, 3, 8, 2);
    }

    public static Monster createBandit() {
        return new Monster("Bandit", 18, 5, 15, 3);
    }

    public static Monster createWolf() {
        return new Monster("Wolf", 12, 6, 5, 8);
    }

    // Tier 2 Monsters (Medium - Level 4-6)
    public static Monster createSkeleton() {
        return new Monster("Skeleton", 20, 5, 30, 0);
    }

    public static Monster createZombie() {
        return new Monster("Zombie", 30, 5, 10, 8);
    }

    public static Monster createOgre() {
        return new Monster("Ogre", 35, 7, 12, 8);
    }

    public static Monster createTroll() {
        return new Monster("Cave Troll", 40, 8, 10, 8);
    }

    public static Monster createHarpy() {
        return new Monster("Harpy", 22, 6, 8, 3);
    }

    // Tier 3 Monsters (Hard - Level 7-9)
    public static Monster createMinotaur() {
        return new Monster("Minotaur", 45, 9, 25, 12);
    }

    public static Monster createElemental() {
        return new Monster("Fire Elemental", 28, 9, 10, 0);
    }

    public static Monster createWraith() {
        return new Monster("Wraith", 25, 6, 8, 15);
    }

    public static Monster createGiant() {
        return new Monster("Hill Giant", 70, 10, 20, 12);
    }

    public static Monster createBasilisk() {
        return new Monster("Basilisk", 35, 7, 18, 10);
    }

    // Tier 4 Monsters (Boss - Level 10+)
    public static Monster createAncientDragon() {
        return new Monster("Ancient Dragon", 100, 15, 50, 30);
    }

    public static Monster createLich() {
        return new Monster("Lich King", 60, 12, 35, 25);
    }

    public static Monster createDemonLord() {
        return new Monster("Demon Lord", 80, 12, 40, 25);
    }

    public static Monster createBeholder() {
        return new Monster("Beholder", 65, 10, 35, 20);
    }

    public static Monster createHydra() {
        return new Monster("Hydra", 90, 11, 30, 15);
    }

    // Special/Unique Monsters
    public static Monster createVampire() {
        return new Monster("Vampire", 40, 8, 20, 15);
    }

    public static Monster createMummy() {
        return new Monster("Ancient Mummy", 32, 6, 25, 20);
}

    public static Monster createDoppelganger() {
    return new Monster("Doppelganger", 25, 7, 15, 5);
}

    public static Monster createTreant() {
    return new Monster("Treant", 55, 8, 30, 18);
}

    public static Monster createGorgon() {
    return new Monster("Gorgon", 38, 9, 28, 12);
}
}