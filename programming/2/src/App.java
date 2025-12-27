
import Pokemons.Leavanny;
import Pokemons.Ninetales;
import Pokemons.Regice;
import Pokemons.Sewaddle;
import Pokemons.Swadloon;
import Tests.DayOfWeek;
import ru.ifmo.se.pokemon.Battle;

public class App {
    public static void main(String[] args) throws Exception {
        Battle b = new Battle();

        Ninetales nt = new Ninetales("234", 23);

        Regice r = new Regice("efe", 1);
        System.out.println(nt.sink("null"));
        b.addAlly(new Regice("fht", 4));
        b.addAlly(r);
        b.addAlly(r);

        b.addFoe(new Sewaddle("Севаддл", 50));
        b.addFoe(new Swadloon("Свадлун", 1));
        b.addFoe(new Leavanny("Ливанни", 100));
        b.go();
    }
}
