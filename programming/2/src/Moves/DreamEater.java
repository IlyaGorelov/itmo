package Moves;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Status;
import ru.ifmo.se.pokemon.Type;

public class DreamEater extends SpecialMove{
    public DreamEater() {
        super(Type.PSYCHIC,100,100);
    }

    @Override
    protected void applyOppDamage(Pokemon def, double arg1) {
        if(def.getCondition() == Status.SLEEP){
            super.applyOppDamage(def, arg1);
        }
    }

    @Override
    protected String describe() {
        return "использует Dream Eater";
    }
}
