package samuel.die;

import java.util.Random;

public class ProductionDie implements Die<Integer> {

    private final Random random = new Random();

    @Override
    public Integer rollDie() {
        return random.nextInt(1, 7);
    }
}
