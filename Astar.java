import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Astar {
    String str = "";
    String goal = "123804765";
        
    PriorityQueue <StateOrder> queue;
    Map<String,Integer> levelDepth;
    Map<String,String> stateHistory;

    int nodes = 0;
	int limit = 50;
	int unique = -1;
    int newValue;
    int a;
    int h;

    String currState;
    char heuristic;
    boolean solution = false;

    final int puzzleSize;
    final int printSize;

    Astar (String str, char heuristic, int puzzleSize, int printSize) {
        queue = new PriorityQueue <StateOrder> ();
        levelDepth = new HashMap<String, Integer>();
        stateHistory = new HashMap<String,String>();
        this.str = str;
        this.heuristic = heuristic;
        this.puzzleSize = puzzleSize;
        this.printSize = printSize;
        addToQueue(str,null);
    }

    void doSearch () {
        while (!queue.isEmpty()) {
            currState = queue.poll().toString();
            if (currState.equals(goal)) { 
                solution = true;
                System.out.println("Solution Exists \n");
                System.out.println("Run puzzle (y/n)?");
                Scanner scanner = new Scanner(System.in);
                String confirm = scanner.nextLine();
                System.out.println();
                if (confirm.equals("y") || confirm.equals("yes")) {
                    printSolution(currState);
                }
                else if (confirm.equalsIgnoreCase("n") || confirm.equalsIgnoreCase("no")) {
                    System.out.println("Closing");
                    System.exit(0);
                }
                break;
            }
            if (levelDepth.get(currState) == limit) {
                solution = false;
                printSolution(currState);
                break;
            }
            else {
            
                a = currState.indexOf("0");
                //left
                while (a != 0 && a != 3 && a != 6) {
                    String nextState = currState.substring(0, a - 1) + "0" + currState.charAt(a - 1) + currState.substring(a + 1);
                    addToQueue(nextState, currState);
                    nodes++;
                    break;
                }

                //up
                while (a != 0 && a != 1 && a != 2){
                    String nextState = currState.substring(0, a - 3) + "0" + currState.substring(a - 2, a) + currState.charAt(a - 3) + currState.substring(a + 1);
                    addToQueue(nextState, currState);
                    nodes++;
                    break;
                }

                //right
                while(a != 2 && a != 5 && a != 8){
                    String nextState = currState.substring(0, a) + currState.charAt(a + 1) + "0" + currState.substring(a + 2);
                    addToQueue(nextState, currState);
                    nodes++;
                    break;
                }

                //down
                while (a != 6 && a != 7 && a != 8) {
                    String nextState = currState.substring(0, a)+currState.substring(a + 3, a + 4) + currState.substring(a + 1, a + 3) + "0" + currState.substring(a + 4);
                    addToQueue(nextState, currState);
                    nodes++;
                    break;
                }
            }
        }

        if (!solution) {
            System.out.println("Solution not yet found! My suggestion are:");
            System.out.println("1. Try to increase level depth limit. ");
            System.out.println("2. Use other heuristic. ");
            System.out.println("3. Maybe solution does not exist. ");
        }
    }

    private void addToQueue (String newState, String oldState) {
        if(!levelDepth.containsKey(newState)) {
            if (oldState == null) {
                newValue = 0;
            }
            else {
                newValue = levelDepth.get(oldState) + 1;
            }
            unique ++;
            levelDepth.put(newState, newValue);
            if (heuristic == 'm')
            {
                h =  calcManhattan(newState,goal) + newValue;
            }
            else if (heuristic == 't')
            {
                h = calcMisplacedTiles(newState,goal) + newValue;
            }
            else if (heuristic == 'o')
            {
                h = calcTilesOutOfRowAndCol(newState,goal) + newValue;
            }
            queue.add(new StateOrder(h,newState));
            stateHistory.put(newState, oldState);
        }
    }

    int calcTilesOutOfRowAndCol(String currState, String goalState) {
        int read = 0;
        int out = 0;
        int[][] currPuzzle = new int[printSize][printSize];
        int[][] goalPuzzle = new int[printSize][printSize];
        for (int i = 0; i < printSize; i++) {
            for (int j = 0; j < printSize; j++) {
                currPuzzle[i][j] = currState.charAt(read);
                goalPuzzle[i][j] = goalState.charAt(read);
                read++;
            }
        }
        for (int k = 0; k < printSize; k++) {
            for (int l = 0; l < printSize; l++) {
                if (currPuzzle[k][l] != goalPuzzle[k][l]){
                    out++;
                }
            }
        }
        return out;
    }

    int calcMisplacedTiles(String currState, String goalState) {
        int mis = 0;
        for (int i = 1; i < puzzleSize; i++) {
            if (currState.indexOf(String.valueOf(i)) != goalState.indexOf(String.valueOf(i))){
                mis++;
            }
        }
        return mis;
    }

    int calcManhattan(String currState, String goalState) {
        int [][] manValue = {
            {0,1,2,1,2,3,2,3,4},
            {1,0,1,2,1,2,3,2,3},
            {2,1,0,3,2,1,4,3,2},
            {1,2,3,0,1,2,1,2,3},
            {2,1,2,1,0,1,2,1,2},
            {3,2,1,2,1,0,3,2,1},
            {2,3,4,1,2,3,0,1,2},
            {3,2,3,2,1,2,1,0,1},
            {4,3,2,3,2,1,2,1,0},
        };

        int heu = 0 ;
        int result = 0;

        for (int i = 1; i < puzzleSize; i++) {
            heu = manValue[currState.indexOf(String.valueOf(i))][goalState.indexOf(String.valueOf(i))];
            result = result + heu;
        }
        return result;
    }

    void printSolution (String currState){
        if (solution) {
            System.out.println("Number of steps: " +levelDepth.get(currState));
            //System.out.println("Node generated: "+ nodes);
            System.out.println("Opened: "+ unique);
            System.out.println("Max in memory " + limit);
            System.out.println();
        }
        else {
            System.out.println("Solution not found!");
            System.out.println("Max memory reached!");
            System.out.println("Opened: "+ unique);
            //System.out.println("Node generated: "+ nodes);
        }

        String traceState = currState;
        String[] printArray = new String[levelDepth.get(currState) + 1];
        int i = 0;
        while (traceState != null) {
           
            printArray[i] = traceState;
            traceState = stateHistory.get(traceState);
            
            //System.out.println("Array at "+ i + ", value "+printArray[i]);
            i++;
        }
        int s = printArray.length - 1;
        int r = 0;
        String line;
        while (s > -1) {
            System.out.println("Step: " + r++);
            line = printArray[s];
            try {
		        for(int z = 0; z < puzzleSize; z++) {
                    System.out.print(" " + String.valueOf(line.charAt(z)) + " ");
                    if ((z + 1) % 3 == 0) {
                        System.out.println();
                    }
                }
                System.out.println();
            }
            catch (NullPointerException e) {}
            //printArray[i] = traceState;
            //traceState = stateHistory.get(traceState);
            
            //System.out.println("Array at "+ i + ", value "+printArray[i]);i++;
            s--;
        }
	}
}
