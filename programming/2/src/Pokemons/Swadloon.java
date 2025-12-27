package Pokemons;

import Moves.CalmMind;
import Moves.DreamEater;
import Moves.GrassWhistle;

public class Swadloon extends Sewaddle{
    public Swadloon(String var1, int var2) {
        super(var1,var2);
        setStats(55, 63, 90, 50, 80, 42);
        
        setMove(new DreamEater(),new CalmMind(),new GrassWhistle());
    }

}
