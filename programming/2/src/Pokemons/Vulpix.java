package Pokemons;

import Moves.ConfuseRay;
import Moves.DoubleTeam;
import Moves.FireBlast;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Vulpix extends Pokemon {
    public Vulpix(String var1, int var2) {
        super(var1, var2);
        setStats(38, 41, 40, 50, 65, 65);
        setType(Type.FIRE);
        setMove(new DoubleTeam(), new FireBlast(), new ConfuseRay());
    }

    public String sink(String s) {
        return s;
    }
}
