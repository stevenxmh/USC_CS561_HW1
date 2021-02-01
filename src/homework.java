import java.io.File;
import java.io.FileNotFoundException;
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

		if (algorithm.equals("BFS")){  	
			bfs(row, col, x, y, maxHeight, settlingSites, map);
		}else if (algorithm.equals("UCS")){
			ucs(row, col, x, y, maxHeight, settlingSites, map);
		}else{
			aStar(row, col, x, y, maxHeight, settlingSites, map);
		}

	}

	/*
	 *  Object represent each cell in the map
	 *  x: row
	 *  y: col
	 *  m: Height/Muddiness
	 */
	static class Unit {
		int x;
		int y;
		int m;

		public Unit (int x, int y, int m){
			this.x = x;
			this.y = y;
			this.m = m;
		}

		public String toString(){
			return "("+x+","+y+")"+" M="+this.m;
		}
	}

	/*
	 * Print out a BFS path into output.txt
	 */
	private static void bfs(int row, int col, int x, int y, int maxHeight,
			int[][] settlingSites, int[][] map) {
		System.out.println("BFS implementation:");

		Unit origin = new Unit(x, y, map[x][y]);

		for (int[] site : settlingSites) {
			System.out.println("goal: (" + site[0]+"," + site[1]+")");
			// keep track of visted Units
			int[][] visited = new int[row][col];
			visited[x][y] = 1;
			// Track prev visted Unit
			Map<Unit, Unit> parents = new HashMap<>();
			Queue<Unit> queue = new LinkedList<>();
			queue.add(origin);

			// track last visted Unit 
			Unit curr = queue.peek();

			while(!queue.isEmpty()){
				curr = queue.remove();
				if (curr.x == site[0] && curr.y == site[1]) break;
				// add adjacent vertex that are unvisited and valid into queue
				for (int[] d : directions){
					// check if the next vertex is in bound
					if (curr.x + d[0] >= 0 && curr.x + d[0] <= row-1 && curr.y + d[1] >= 0 && curr.y + d[1] <= col-1){
						// check if the next vertex is visited and can be moved to
						if (visited[curr.x+d[0]][curr.y+d[1]] == 0 && canMove(map, maxHeight, curr.x, curr.y, curr.x+d[0], curr.y+d[1])){
							// Create the adjacent Unit and enqueue
							Unit next = new Unit(curr.x+d[0], curr.y+d[1], map[curr.x+d[0]][curr.y+d[1]]);
							queue.add(next);
							// mark curr Unit as the parent of the next Unit
							parents.put(next, curr);
							// mark next as visited
							visited[curr.x+d[0]][curr.y+d[1]] = 1;
						}
					}
				}
			}


			for (Unit u : parents.keySet()){

				System.out.println(parents.get(u).toString() + "->" + u.toString());

			}
		}

	}

	private static void ucs(int row, int col, int x, int y, int maxHeight,
			int[][] settlingSites, int[][] map) {
		System.out.println("USC");

	}

	private static void aStar(int row, int col, int x, int y, int maxHeight,
			int[][] settlingSites, int[][] map) {
		System.out.println("A*");

	}

	private static boolean canMove(int[][] map, int maxHeight, int x, int y, int r, int c) {
		int originHeight = map[x][y] < 0 ? Math.abs(map[x][y]) : 0;
		int targetHeight = map[r][c] < 0 ? Math.abs(map[r][c]) : 0;
		return Math.abs(originHeight - targetHeight) <= maxHeight;
	}

}
