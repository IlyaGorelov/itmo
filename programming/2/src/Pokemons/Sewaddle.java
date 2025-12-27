package Pokemons;

import Moves.CalmMind;
import Moves.DreamEater;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Sewaddle extends Pokemon{
    public Sewaddle(String var1, int var2) {
        super(var1,var2);
        setStats(45, 53, 70, 40, 60, 42);
        setType(Type.BUG);
        setType(Type.GRASS);
        setMove(new DreamEater(),new CalmMind());
    }
}
