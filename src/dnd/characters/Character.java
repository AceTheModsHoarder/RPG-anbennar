package dnd.characters;

public abstract class Character {
    protected String name;
    protected int hp;
    protected int maxHp;
    protected int attack;
    protected int defense;
    
    public Character(String name, int hp, int attack, int defense) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attack = attack;
        this.defense = defense;
    }
    
    public boolean isAlive() { 
        return hp > 0; 
    }
    
    public void takeDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;
    }
    
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
}