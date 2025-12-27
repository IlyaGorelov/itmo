package Pokemons;

import Moves.Stomp;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public final class Regice extends Pokemon {

    Stomp s = new Stomp();
    Ninetales d = new Ninetales("efe", 5);

    public Regice(String var1, int var2) {

        super(var1, var2);
        setStats(80, 50, 100, 100, 200, 50);
        setType(Type.ICE);
        setMove(s);
    }
}
