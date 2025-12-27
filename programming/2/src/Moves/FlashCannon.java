package Moves;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;

public class FlashCannon extends SpecialMove{
    public FlashCannon() {
        super(Type.STEEL,80,100);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        if (new Effect().chance(0.1).success())
            p.setMod(Stat.SPECIAL_DEFENSE, -1);
    }

    @Override
    protected String describe() {   
        return "использует Flash Cannon";
    }
}
