import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class homework {

	static int[][] directions = {
			{-1, -1}, {-1, 0}, {-1, 1},
			{0, -1}, {0, 1},
			{1, -1}, {1, 0}, {1, 1}
	};

	public static void main(String[] args) {
		/*
		 * Store input.txt into String List 
		 * Grader input is always valid 
		 */
		List<String> inputs = new ArrayList<>();
		try {
			File myFile = new File("input.txt");
			Scanner scan = new Scanner(myFile);
			while (scan.hasNextLine()) {
				String data = scan.nextLine();
				System.out.println(data);
				inputs.add(data);
			}
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("error");
			e.printStackTrace();
		}

		/*
		 * Populate initial variables from inputs
		 */
		String algorithm = inputs.get(0);
		// W = col, H = row
		int col = Integer.valueOf((inputs.get(1).split(" ")[0]));
		int row = Integer.valueOf((inputs.get(1).split(" ")[1]));

		int x = Integer.valueOf((inputs.get(2).split(" ")[0]));
		int y = Integer.valueOf((inputs.get(2).split(" ")[1]));

		int maxHeight = Integer.valueOf(inputs.get(3));
		int numOfSites = Integer.valueOf(inputs.get(4));
		int[][] settlingSites = new int[numOfSites][2];
		for (int i = 0; i < numOfSites; i ++) {
			settlingSites[i][0] = Integer.valueOf((inputs.get(5+i).split(" ")[0]));
			settlingSites[i][1] = Integer.valueOf((inputs.get(5+i).split(" ")[1]));;
		}

		int map_index = 5 + numOfSites;
		int[][] map = new int[row][col];

		for (int i = 0; i < row; i++){
			for (int j = 0; j < col; j++){
				map[i][j] = Integer.valueOf((inputs.get(map_index+i).split("\\s+")[j]));;
			}
		}

		List<String> output = new ArrayList<String>();

		if (algorithm.equals("BFS")){  	
			output = bfs(row, col, x, y, maxHeight, settlingSites, map);
		}else if (algorithm.equals("UCS")){
			output = ucs(row, col, x, y, maxHeight, settlingSites, map);
		}else{
			output = aStar(row, col, x, y, maxHeight, settlingSites, map);
		}

		try {
			FileWriter writer = new FileWriter("output.txt");
			for (String s : output) {
				if (output.indexOf(s) != output.size()-1) {
					writer.write(s+"\r");
				}else {
					writer.write(s);
				}

			}
			writer.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}	
	}

	/*
	 *  Object represent each cell in the map
	 *  x: row
	 *  y: col
	 *  m: Height/Muddiness
	 */
	static class Cell {
		int x;
		int y;
		int m;

		public Cell (int x, int y, int m){
			this.x = x;
			this.y = y;
			this.m = m;
		}

		public String toString(){
			return x + "," + y;
		}
	}

	/*
	 * Print out a BFS path into output.txt
	 */
	private static List<String> bfs(int row, int col, int x, int y, int maxHeight,
			int[][] settlingSites, int[][] map) {
		// System.out.println("BFS implementation:");
		List<String> result = new ArrayList<>();
		Cell origin = new Cell(x, y, map[x][y]);

		for (int[] site : settlingSites) {
			// System.out.println(origin +" --> " + site[0]+"," + site[1]);
			// keep track of visted Cells
			int[][] visited = new int[row][col];
			visited[x][y] = 1;
			// Track prev visted Cell
			Map<Cell, Cell> parents = new HashMap<>();
			Queue<Cell> queue = new LinkedList<>();
			queue.add(origin);

			// track last visted Cell 
			Cell curr = queue.peek();

			while(!queue.isEmpty()){
				curr = queue.remove();
				if (curr.x == site[0] && curr.y == site[1]) break;
				// add adjacent vertex that are unvisited and valid into queue
				for (int[] d : directions){
					// check if the next vertex is in bound
					if (curr.x + d[0] >= 0 && curr.x + d[0] <= row-1 && curr.y + d[1] >= 0 && curr.y + d[1] <= col-1){
						// check if the next vertex is visited and can be moved to
						if (visited[curr.x+d[0]][curr.y+d[1]] == 0 && canMove(map, maxHeight, curr.x, curr.y, curr.x+d[0], curr.y+d[1])){
							// Create the adjacent Cell and enqueue
							Cell next = new Cell(curr.x+d[0], curr.y+d[1], map[curr.x+d[0]][curr.y+d[1]]);
							queue.add(next);
							// mark curr Cell as the parent of the next Cell
							parents.put(next, curr);
							// mark next as visited
							visited[curr.x+d[0]][curr.y+d[1]] = 1;
						}
					}
				}
			}

			//			for (Cell u : parents.keySet()){
			//
			//				System.out.println(parents.get(u).toString() + "->" + u.toString());
			//
			//			}

			String output = "";

			// if queue is empty and settlers didn't make it
			if (curr.x != site[0] || curr.y != site[1]){
				output = "FAIL";
			}else{
				// back track to origin
				while (curr != null) {
					output = " " + curr.toString() + output;
					curr = parents.get(curr);
				}
				output = output.substring(1);
			}

			result.add(output);
		}

		return result;
	}

	private static List<String> ucs(int row, int col, int x, int y, int maxHeight,
			int[][] settlingSites, int[][] map) {
		System.out.println("USC");
		return null;
	}

	private static List<String> aStar(int row, int col, int x, int y, int maxHeight,
			int[][] settlingSites, int[][] map) {
		System.out.println("A*");
		return null;

	}

	private static boolean canMove(int[][] map, int maxHeight, int x, int y, int r, int c) {
		int originHeight = map[x][y] < 0 ? Math.abs(map[x][y]) : 0;
		int targetHeight = map[r][c] < 0 ? Math.abs(map[r][c]) : 0;
		return Math.abs(originHeight - targetHeight) <= maxHeight;
	}

}
