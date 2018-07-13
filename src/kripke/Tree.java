package kripke;

import operations.*;

import java.util.*;

public class Tree {
    private World root;
    private Expression expression;

    public Tree(Expression e) {
        expression = e;
    }

    public Tree(World rt, Expression e) {
        root = rt;
        expression = e;
    }

    private boolean checkNeg(World w, Expression e) {
        Deque<World> q = new ArrayDeque<>();
        q.addLast(w);
        while (!q.isEmpty()) {
            World world = q.pollFirst();
            if (checkOneWorld(world, e)) {
                return false;
            }
            for (World w1 : world.getSons()) {
                q.addLast(w1);
            }

        }
        return true;
    }

    private boolean checkOneWorld(World w, Expression e) {
        if (e instanceof Conjunction) {
            return checkOneWorld(w, ((Conjunction) e).getLeft()) && checkOneWorld(w, ((Conjunction) e).getRight());
        } else if (e instanceof Disjunction) {
            return checkOneWorld(w, ((Disjunction) e).getLeft()) || checkOneWorld(w, ((Disjunction) e).getRight());
        } else if (e instanceof Implication) {
            Expression a = ((Implication) e).getLeft();
            Expression b = ((Implication) e).getRight();
            Deque<World> q = new ArrayDeque<>();
            q.addLast(w);
            while (!q.isEmpty()) {
                World world = q.pollFirst();
                if (checkOneWorld(world, a) && !checkOneWorld(world, b)) {
                    return false;
                }
                for (World w1 : world.getSons()) {
                    q.addLast(w1);
                }

            }
            return true;
        } else if (e instanceof Negation) {
            return checkNeg(w, ((Negation) e).get());
        } else {
            return w.check(e);
        }
    }

    public boolean check() {
        Deque<World> q = new ArrayDeque<>();
        q.addLast(root);
        while (!q.isEmpty()) {
            World world = q.pollFirst();
            if (!checkOneWorld(world, expression)) {
                return false;
            }
            for (World w1 : world.getSons()) {
                q.addLast(w1);
            }

        }
        return true;
    }


    private World makeTree(List<Integer> psp, int ind, World w) {
        if (ind >= psp.size()) {
            return w;
        }
        if (psp.get(ind) == -1) {
            return makeTree(psp, ++ind, w.parent);
        } else {
            World s = new World(w);
            w.addSon(s);
            return makeTree(psp, ++ind, s);
        }
    }

    private int NUM;
    private Set<Expression> vars = new HashSet<>();

    public Set<Expression> retVar() {
        if (vars.size() == 0) {
            findVar(expression);
        }
        return vars;
    }

    private void findVar(Expression e) {
        if (e instanceof Variable) {
            vars.add(e);
        } else if (e instanceof Negation) {
            findVar(((Negation) e).get());
        } else {
            for (Expression ex : e.getChildren()) {
                findVar(ex);
            }
        }
    }

    private void findWorld(Expression v, int num, World w) {
        if (w.num == num) {
            w.addVar(v);
        } else {
            for (World w1 : w.getSons()) {
                findWorld(v, num, w1);
            }
        }
    }

    private void doGen(List<List<Integer>> pos) {
        int n = 0;
        for (Expression v : vars) {
            List<Integer> p = pos.get(n);
            //System.out.print(v + "::");
            if (p.size() > 0) {
                for (int pp : p) {
                    //System.out.print(pp + " ");
                    findWorld(v, pp, root);
                }
            }
            //System.out.println();
            n++;
        }
        //System.out.println();
    }

    private void remVars() {
        for (Expression v : vars) {
            root.remVar(v);
        }
    }

    private World genVar(int cnt, int ind, List<List<Integer>> pos) {
        World tmp;
        if (ind == NUM) {
            if (cnt == vars.size() - 1) {
                doGen(pos);
                if (!check()) {
                    return root;
                } else {
                    remVars();
                    return null;
                }
            } else {
                tmp = genVar(cnt + 1, 0, pos);
                if (tmp != null) {
                    return tmp;
                }
            }
        } else {
            tmp = genVar(cnt, ind + 1, pos);
            if (tmp != null) {
                return tmp;
            }
            pos.get(cnt).add(ind);
            tmp = genVar(cnt, ind + 1, pos);
            pos.get(cnt).remove(pos.get(cnt).size() - 1);
            if (tmp != null) {
                return tmp;
            }
        }
        return null;
    }

    public void doNum() {
        NUM = 0;
        numirate(root);
    }

    private void numirate(World w) {
        w.num = NUM++;
        for (World ww : w.getSons()) {
            numirate(ww);
        }
    }

    private boolean checkPSP(List<Integer> psp) {
        int balance = 0;
        for (Integer b : psp) {
            balance += b;
            if (balance < 0) {
                return false;
            }
        }
        return balance == 0;
    }

    private World genPSP(int len, List<Integer> psp) {
        if (psp.size() == len) {
            if (checkPSP(psp)) {
                root = makeTree(psp, 0, root);
                NUM = 0;
                numirate(root);
                List<List<Integer>> x = new ArrayList<>();
                for (int i = 0; i < vars.size(); ++i) {
                    x.add(new ArrayList<>());
                }
                World ans = genVar(0, 0, x);
                root = new World();
                if (ans != null) {
                    return ans;
                }
            }
            return null;
        } else {
            psp.add(-1);
            World w = genPSP(len, psp);
            psp.remove(psp.size() - 1);
            if (w != null) {
                return w;
            }
            psp.add(1);
            w = genPSP(len, psp);
            psp.remove(psp.size() - 1);
            if (w != null) {
                return w;
            }
            return null;
        }
    }

    public World genAll() {
        findVar(expression);
        for (int i = 0; i < 5; ++i) {
            root = new World();
            World ans = genPSP(i * 2, new ArrayList<>());
            if (ans != null) {
                return ans;
            }
        }
        return null;
    }
}
