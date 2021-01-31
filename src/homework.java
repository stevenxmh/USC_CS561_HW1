import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class homework {

	public static void main(String[] args) {
		/*
		 * Store input.txt into String List
		 */
		List<String> inputs = new ArrayList<>();
	    try {
	        File myFile = new File("input.txt");
	        Scanner scan = new Scanner(myFile);
	        while (scan.hasNextLine()) {
	          String data = scan.nextLine();
	          inputs.add(data);
	        }
	        scan.close();
	      } catch (FileNotFoundException e) {
	        System.out.println("error");
	        e.printStackTrace();
	      }
	    
	    for (String s : inputs){
//	    	System.out.println(s);
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
	    
	}

}
