package Pokemons;

import Moves.CalmMind;
import Moves.DreamEater;
import Moves.GrassWhistle;
import Moves.StringShot;

public final class Leavanny extends Swadloon{
    public Leavanny(String var1, int var2) {
        super(var1, var2);
        setStats(75, 103, 80, 70, 80, 92);
        setMove(new DreamEater(),new CalmMind(),new GrassWhistle(),new StringShot());
    }
}
