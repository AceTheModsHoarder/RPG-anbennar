package engine;

import Combat.CombatSystem;
import characters.Monster;

public class GameLoop {
    private void explore() {
    System.out.println("\nYou walk into the wilderness...");

    // Generate a random monster
    Monster monster = generateRandomMonster();

    // Start combat
    CombatSystem.start(player, monster);
}
private Monster generateRandomMonster() {
    int roll = (int) (Math.random() * 3);

    return switch (roll) {
        case 0 -> new Monster("Goblin", 15, 4, 1);
        case 1 -> new Monster("Wolf", 20, 5, 2);
        default -> new Monster("Bandit", 25, 6, 3);
    };
}
case 2 -> explore();

}
