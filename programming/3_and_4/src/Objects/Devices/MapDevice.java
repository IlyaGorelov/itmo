package Objects.Devices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import Interfaces.Crashable;
import Objects.FlightProgram;
import Objects.Planets.Earth;
import Objects.Planets.Moon;

public class MapDevice extends Device implements Crashable {

    ArrayList<int[]> path = new ArrayList<>();

    public MapDevice(String name, double condition) {
        super(name, condition);
    }

    public void printMap(FlightProgram fp) {
        int size = 31;

        String[][] map = new String[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                map[i][j] = "..";

        map[15][15] = "EE"; // Earth

        map[15][16] = "##"; // North base
        map[16][15] = "##"; // East Base
        map[15][14] = "##"; // South base
        map[14][15] = "##"; // West base

        map[14][14] = "T"; // corner btw west and south
        map[14][16] = "T"; // corner btw west and north
        map[16][16] = "T"; // corner btw north and east
        map[16][14] = "T"; // corner btw east and south

        int[] moonCoords = Moon.getPosition(fp.time);
        map[moonCoords[0]][moonCoords[1]] = "MM"; // Moon

        int[] baseCoords = Earth.getPosition(fp.base);
        map[baseCoords[0]][baseCoords[1]] = "BB"; // Base

        path = findPath(map, baseCoords, moonCoords);

        for (int[] p : path) {
            if (map[p[0]][p[1]].equals(".."))
                map[p[0]][p[1]] = "**";
        }

        for (int i = map.length - 1; i >= 0; i--) {
            for (int j = 0; j < map[i].length; j++)
                System.out.print(map[i][j].replace("T", ".."));
            System.out.println();
        }

    }

    private ArrayList<int[]> findPath(String[][] map, int[] start, int[] target) {
        int n = map.length;
        boolean[][] visited = new boolean[n][n];
        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        Map<String, String> parent = new HashMap<>();

        Queue<int[]> q = new LinkedList<>();
        q.add(start);
        visited[start[0]][start[1]] = true;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            if (cur[0] == target[0] && cur[1] == target[1]) {
                ArrayList<int[]> path = new ArrayList<>();
                String key = cur[0] + "," + cur[1];
                while (parent.containsKey(key)) {
                    String[] parts = key.split(",");
                    path.add(new int[] { Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) });
                    key = parent.get(key);
                }
                Collections.reverse(path);
                return path;
            }

            for (int[] d : dirs) {
                int nx = cur[0] + d[0];
                int ny = cur[1] + d[1];
                if (nx < 0 || ny < 0 || nx >= n || ny >= n)
                    continue;
                if (visited[nx][ny])
                    continue;
                if (map[nx][ny].equals("EE") || map[nx][ny].equals("##") || map[nx][ny].equals("T"))
                    continue;

                visited[nx][ny] = true;
                q.add(new int[] { nx, ny });
                parent.put(nx + "," + ny, cur[0] + "," + cur[1]);
            }
        }
        return null;
    }

    public ArrayList<Integer> getFlightCommands(FlightProgram fp) {
        ArrayList<Integer> commands = new ArrayList<Integer>();

        switch (fp.base) {
            case North:
                commands.add(0);
                break;
            case East:
                commands.add(1);
                break;
            case South:
                commands.add(2);
                break;
            case West:
                commands.add(3);
                break;

        }

        for (int i = 0; i < path.size(); i++) {
            int[] currentPoint = path.get(i);
            if (i != 0) {
                int[] previousPoint = path.get(i - 1);

                if (previousPoint[0] == currentPoint[0]) {
                    if (previousPoint[1] - currentPoint[1] == -1)
                        commands.add(3);
                    else
                        commands.add(1);
                } else if (previousPoint[1] == currentPoint[1]) {
                    if (previousPoint[0] - currentPoint[0] == -1)
                        commands.add(0);
                    else
                        commands.add(2);
                }
            }
        }

        return commands;
    }

    @Override
    public void crash() {
        double breakChance = Math.pow(1.0 - (getCondition() / 100.0), 2);
        if (Math.random() < breakChance) {
            setCondition(0);
            System.out.printf("Оборудование %s сломалось\n", getName());
        }
    }

    @Override
    public void repair() {
        setCondition(100);
        System.out.printf("Оборудование %s восстановлено\n", getName());
    }

    @Override
    public void scan() {
        if (Math.random() < 0.3) {
            {
                setCondition(getCondition() - 40);
                System.out.printf("В ходе сканирования %s было выявлено ухудшение состояния\n", getName());
                crash();
            }
        }
    }

    @Override
    public boolean isNeedRepairing() {
        return getCondition() <= 0;
    }

}
