package Moves;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;

public class ConfuseRay extends StatusMove{
    public ConfuseRay() {
        super(Type.GHOST,0,100);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        p.confuse();
    }

    @Override
    protected String describe() {
        return "использует Confuse Ray";
    }
}
