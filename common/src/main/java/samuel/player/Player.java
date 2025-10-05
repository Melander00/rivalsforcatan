package samuel.player;

import samuel.effect.Effect;

public interface Player {

    void giveEffect(Effect effect);

    int requestInt();
    int requestInt(int min, int max);
}
