import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class homework {

	static int[][] directions = {
			{-1, -1}, {-1, 0}, {-1, 1},
			{0, -1},         {0, 1},
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
			System.out.println("input error");
			e.printStackTrace();
		}

		/*
		 * Populate initial variables from inputs
		 */
		String algorithm = inputs.get(0);
		// W = col, H = row
		int col = Integer.valueOf((inputs.get(1).split(" ")[0]));
		int row = Integer.valueOf((inputs.get(1).split(" ")[1]));

		// start state col and row
		int c = Integer.valueOf((inputs.get(2).split(" ")[0]));
		int r = Integer.valueOf((inputs.get(2).split(" ")[1]));

		int maxHeight = Integer.valueOf(inputs.get(3));
		int numOfSites = Integer.valueOf(inputs.get(4));
		int[][] settlingSites = new int[numOfSites][2];
		for (int i = 0; i < numOfSites; i ++) {
			// col
			settlingSites[i][1] = Integer.valueOf((inputs.get(5+i).split(" ")[0]));;
			// row
			settlingSites[i][0] = Integer.valueOf((inputs.get(5+i).split(" ")[1]));

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
			output = bfs(row, col, r, c, maxHeight, settlingSites, map);
		}else if (algorithm.equals("UCS")){
			output = ucs(row, col, r, c, maxHeight, settlingSites, map);
		}else{
			output = aStar(row, col, r, c, maxHeight, settlingSites, map);
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
			System.out.println("output error.");
			e.printStackTrace();
		}	
	}

	/*
	 *  Object represent each cell in the map
	 *  r: row
	 *  c: col
	 *  m: Height/Muddiness
	 *  cost: the cost from start state to the cell
	 */
	static class Cell {
		int r;
		int c;
		int m;
		int cost;

		public Cell (int x, int y, int m){
			this.r = x;
			this.c = y;
			this.m = m;
			this.cost = 0;
		}

		public Cell (int x, int y, int m, int cost){
			this.r = x;
			this.c = y;
			this.m = m;
			this.cost = cost;
		}

		// print out in the format of c,r
		public String toString(){
			return c + "," + r;
		}
	}

	/*
	 * Comparator for Class Cell, order Cell in increasing cost order
	 */
	static class CellComparator implements Comparator<Cell>{

		@Override
		public int compare(homework.Cell c1, homework.Cell c2) {
			return c1.cost - c2.cost;
		}

	}

	/*
	 * Print out a BFS path into output.txt
	 */
	private static List<String> bfs(int row, int col, int r, int c, int maxHeight,
			int[][] settlingSites, int[][] map) {
		// System.out.println("BFS implementation:");
		List<String> result = new ArrayList<>();
		Cell origin = new Cell(r, c, map[r][c]);

		// Generate one output for each settlement
		for (int[] site : settlingSites) {
			// System.out.println(origin +" --> " + site[0]+"," + site[1]);
			// keep track of visted Cells
			int[][] visited = new int[row][col];
			visited[r][c] = 1;
			// Track prev visted Cell
			Map<Cell, Cell> parents = new HashMap<>();
			Queue<Cell> queue = new LinkedList<>();
			queue.add(origin);

			// track last visted Cell 
			Cell curr = queue.peek();

			while(!queue.isEmpty()){
				curr = queue.remove();
				if (curr.r == site[0] && curr.c == site[1]) break;
				// add adjacent vertex that are unvisited and valid into queue
				for (int[] d : directions){
					// check if the next vertex is in bound
					if (curr.r + d[0] >= 0 && curr.r + d[0] <= row-1 && curr.c + d[1] >= 0 && curr.c + d[1] <= col-1){
						// check if the next vertex is visited and can be moved to
						if (visited[curr.r+d[0]][curr.c+d[1]] == 0 && canMove(map, maxHeight, curr.r, curr.c, curr.r+d[0], curr.c+d[1])){
							// Create the adjacent Cell and enqueue
							Cell next = new Cell(curr.r+d[0], curr.c+d[1], map[curr.r+d[0]][curr.c+d[1]]);
							queue.add(next);
							// mark curr Cell as the parent of the next Cell
							parents.put(next, curr);
							// mark next as visited
							visited[curr.r+d[0]][curr.c+d[1]] = 1;
						}
					}
				}
			}

			//			for (Cell u : parents.keySet()){
			//
			//				System.out.println(parents.get(u).toString() + "->" + u.toString());
			//
			//			}

			// Generate output
			String output = "";

			// if queue is empty and settlers didn't make it
			if (curr.r != site[0] || curr.c != site[1]){
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
		System.out.println("UCS:");

		List<String> result = new ArrayList<>();
		Cell origin = new Cell(x, y, map[x][y]);

		for (int[] site : settlingSites) {
			int[][] visited = new int[row][col];
			visited[x][y] = 1;

			Map<Cell, Cell> parents = new HashMap<>();
			Queue<Cell> queue = new PriorityQueue<>(new CellComparator());
			// memo to keep the pointers of Cell objects to update the cost
			Cell[][] memo = new Cell[row][col];
			memo[x][y] = origin;
			//			Cell a = new Cell(0,0,5,5);
			//			Cell b = new Cell(1,1,10,10);
			//			Cell c = new Cell(2,2,15,15);
			//			queue.add(a);
			//			queue.add(b);
			//			queue.add(c);
			//
			//			while (!queue.isEmpty()) {
			//				Cell curr = queue.poll();
			//				System.out.println(curr.cost);
			//			}

			queue.add(origin);

			// track last visted Cell 
			Cell curr = queue.peek();

			while(!queue.isEmpty()){
				// remove head and mark as visited
				curr = queue.poll();
				visited[curr.r][curr.c] = 1;

				// break if at goal state
				if (curr.r == site[0] && curr.c == site[1]) break;

				for (int[] d : directions){
					// check inbound
					if (curr.r + d[0] >= 0 && curr.r + d[0] <= row-1 && curr.c + d[1] >= 0 && curr.c + d[1] <= col-1){
						// calculate the weight
						int weight = 10;
						if (Math.abs(d[0])+Math.abs(d[1]) == 2) {
							// weight is 14 if path is diagonal
							weight = 14;
						}
						// if unvisited and not blocked
						if (visited[curr.r+d[0]][curr.c+d[1]] == 0 && canMove(map, maxHeight, curr.r, curr.c, curr.r+d[0], curr.c+d[1])){

							// if the adjacent cell was queued
							if (memo[curr.r+d[0]][curr.c+d[1]] != null) {
								// if the new path is cheaper
								if (memo[curr.r+d[0]][curr.c+d[1]].cost > curr.cost + weight ) {
									// update the cost and point parent to the curr cell
									memo[curr.r+d[0]][curr.c+d[1]].cost = curr.cost + weight;
									parents.put(memo[curr.r+d[0]][curr.c+d[1]], curr);
								}
								// else ignore this path
							}else { // the adjacent cell was not queued
								// Create the adjacent Cell with cost and enqueue
								Cell next = new Cell(curr.r+d[0], curr.c+d[1], map[curr.r+d[0]][curr.c+d[1]], curr.cost + weight);
								queue.add(next);
								// mark curr Cell as the parent of the next Cell
								parents.put(next, curr);
								memo[curr.r+d[0]][curr.c+d[1]] = next;
							}
						}
					}
				}
			}

			// Generate output
			String output = "";

			// if queue is empty and settlers didn't make it
			if (curr.r != site[0] || curr.c != site[1]){
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
