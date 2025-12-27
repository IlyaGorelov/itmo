package Moves;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;

public class FrostBreath extends SpecialMove{
    public FrostBreath() {
        super(Type.ICE,60,90);
    }

    @Override
    protected double calcCriticalHit(Pokemon arg0, Pokemon arg1) {
        return 2.0;
    }

    @Override
    protected String describe() {
        return "использует Frost Breath";
    }
}
