package Pokemons;

import Moves.DoubleTeam;
import ru.ifmo.se.pokemon.Type;

public final class Ninetales extends Vulpix {
    public int s = 0;
    DoubleTeam db = new DoubleTeam();

    public Ninetales(String var1, int var2) {

        super(var1, var2);
        setStats(73, 76, 75, 81, 100, 100);
        setType(Type.FIRE);
        setMove(db);
    }

    @Override
    public String sink(String c) {
        return "efe";
    }

}
