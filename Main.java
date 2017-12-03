import javax.net.ssl.ExtendedSSLSession;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int size;
        int puzzleSize;
        int finalSize;
        String str = "";
        String search;
        char heuristic;
        
        if (args.length < 1)
		{
			printUsage();
        }
        else if (args.length > 1)
		{
			search = args[0].toLowerCase();
			Process p;
			String cmd1 = "g++ cleaner.cpp -std=c++14 -o clean";
			String cmd2 = "./clean " + args[1];
			try {
				p = Runtime.getRuntime().exec(cmd1);
				p = Runtime.getRuntime().exec(cmd2);
			}
			catch (Exception e) {

			}
            
            In in = new In(args[1]);
			size = in.readInt();
			puzzleSize = (size * size) + 1;
            finalSize = size * size;
			int[] startingStateBoard = new int[puzzleSize];
			int i = 1;
			while (i < puzzleSize)
			{
				startingStateBoard[i - 1] = in.readInt();
				i++;
            }
            StringBuilder builder = new StringBuilder();

			
            for(int j = 0; j < (startingStateBoard.length - 1); j++) {
               	builder.append(startingStateBoard[j]);
            }
			str = builder.toString();
			int s = 0;
			System.out.println("\nRead puzzle of size "+size+"x"+size+"\n");
			for(int z = 0; z < size; z++) {
				for(int y = 0; y < size; y++) {
					System.out.print(" " + str.charAt(s) + " ");
					s++;
				}
				System.out.println();
			}
			
			if (size == 3) {
            	if (search.equals("asm"))
				{
                	System.out.println("\nSelected: Manhattan Distance");
					heuristic = 'm';
            	}
            	else if (search.equals("aso"))
				{
                	System.out.println("\nSelected: Mismatched");
					heuristic = 'o';
            	}
            	else
            	{
                	System.out.println("\nDefault Selected: Manhattan Distance");
                	heuristic = 'm';
            	}
            
            	if (str.length() == finalSize) {
            	    Astar ast = new Astar(str, heuristic, finalSize, size);
            	    ast.doSearch();
            	}
           		else {
            	    System.out.println("Error: Puzzle Size inaccuracy!\n");
					System.out.println("Program closing");
					System.exit(0);
				}
			}
			else {
				System.out.println("Program currently only handles 3x3 puzzle sizes");
				System.out.println("Program closing");
				System.exit(0);
			}
        }
        else {
			printUsage();
        }      
    }

    private static void printUsage()
	{
		System.out.println("Usage: java Main <asm/aso> [text file]");
		System.exit(-1);
	}
}
