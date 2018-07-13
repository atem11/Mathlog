package kripke;

import operations.Expression;

import java.util.*;

public class World {
    public Set<Expression> real;
    private List<World> sons;
    public World parent;
    public int num;

    public World() {
        real = new HashSet<>();
        sons = new ArrayList<>();
    }

    public World(World p) {
        real = new HashSet<>();
        sons = new ArrayList<>();
        parent = p;
    }

    public void addSon(World son) {
        sons.add(son);
    }

    public List<World> getSons() {
        return sons;
    }

    public boolean check(Expression v) {
        return real.contains(v);
    }

    public void addVar(Expression v) {
        real.add(v);
        for (World w : sons) {
            w.addVar(v);
        }
    }

    public void remVar(Expression v) {
        real.remove(v);
        for (World w : sons) {
            w.remVar(v);
        }
    }
}
