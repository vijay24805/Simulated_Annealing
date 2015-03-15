package Sudoku;

import java.util.Random;

public class SimulatedAnnealing {

	int column;
	int rows;
	int prevscore;
	int globalcurrScore;
	double prob;
	double exp;
	int bestscore=-162;
	int maxcount,checkscore,first;
	Random rand, rand1, rand2, rand3;
	int[][] SudBrdArray;
	SudokuBoard oldBoard, newBoard;

	public SimulatedAnnealing(SudokuBoard sudbrd) {
		
		
		oldBoard = sudbrd;
		newBoard=new SudokuBoard(sudbrd);   
		
		SudBrdArray=newBoard.getBoard();
		
		//newBoard.toString();
		System.out.println("Initial Board :");
		System.out.println(" ");
		System.out.println(oldBoard.toString());
		
		
		
		rand1 = new Random();
		rand2 = new Random();
		rand3 = new Random();
		globalcurrScore = prevscore = 0;
		maxcount = 3000;
		first=maxcount;
		run();

		
		
		
				

	}

	private void run() {
		// TODO Auto-generated method stub
		int count = 0;
		boolean validSwap;
		int colmn,t;
		int[] columvalues={0,1,2,3,4,5,6,7,8};
		int temp,selectedcol;
		
		calculateScore(SudBrdArray);
		//System.out.println("Initial score is " + globalcurrScore);
		
		long lStartTime = System.currentTimeMillis();
		
		while (maxcount > 0 && prevscore!=bestscore) {
			// generate random column to start
			//System.out.println(newBoard.getBoard().length);
			//System.out.println("count -->" + count + "-->" +  prevscore);
			
				
		// permuting the columns	
		for(t=columvalues.length-1;t<0;t--)
		{
			selectedcol=rand3.nextInt(t);
			temp=columvalues[t];
			columvalues[t]=columvalues[selectedcol];
			columvalues[selectedcol]=temp;
			
		}
			
			
		// For each column number from the permuted list 	
		 for(int colIndex=0;colIndex<columvalues.length ;colIndex++)
		 {
			 colmn=columvalues[colIndex];
		
			// number of swaps to be done in a particular column
			for(int numOfSwaps=0;numOfSwaps<21;numOfSwaps++)
			{	
				//copy oldboard to  new board
				newBoard=new SudokuBoard(oldBoard);
				
				
				//If best score found break out of the loop.
				if(prevscore==bestscore)
				{
				System.out.println("REACHED BEST SCORE");
				break;
				}
				
				//Generate random row values for swap
				int r1 = rand1.nextInt(9);
				int r2 = rand1.nextInt(9);
				
				if(r1==r2)
				{
					//If rows selected are same do nothing
					//System.out.println("Random numbers generated should not be equal");
				}
				else
				{
					
					validSwap=newBoard.swap(colmn, r1, r2);
					if(validSwap)
					{
						SudBrdArray=newBoard.getBoard();
						// the score is reset as 0 before calculation
						globalcurrScore=0;
						globalcurrScore=calculateScore(SudBrdArray);
						
						
						/*
						probility of acceptance= e^(currscore-prevscor)/maxcount
						*/
						
						
						exp=(-(double)(globalcurrScore-prevscore)/(double)maxcount);
						prob=Math.pow(2.718, exp);
						
						if(prob>0.8)
						{
						  
							prevscore=globalcurrScore;
							//Copy the new board to old board and move on to next column.
							oldBoard=new SudokuBoard(newBoard);
							SudBrdArray=oldBoard.getBoard();
							
						}
						else
						{
							//Don't copy the newboard to old board
							
						}
						
					}
					else
					{
						//System.out.println("go for next iteration");
					}
					
				}
				
			}
			
		 
		 
			}
		 if(prevscore>globalcurrScore)
		 maxcount-=80;
		 
		 else
		 maxcount--;
		 
		}
		//At last the final score and the resultant board is displayed.
		
		
		SudBrdArray=oldBoard.getBoard();
		
		System.out.println("Final Board :");
		System.out.println(" ");
		System.out.println(oldBoard.toString());
		System.out.println(" ");
		System.out.println("Final score:" + prevscore);
		System.out.print("Best Score:" + -168);
		System.out.println(" ");
		
		long lEndTime = System.currentTimeMillis();
		long difference = lEndTime - lStartTime;
		 
		System.out.print("Elapsed milliseconds: " + difference);
 
		
	}

	private int calculateScore(int[][] board){
		//boolean[] duplicates=new boolean[10];
		int scorerow ,scorecol;
		
		//scoring rows
		scorerow=scoreRows(board);
			
		//scoring squares
		scorecol=scoreCols(board);
		//System.out.println("Current score is------------>" + (scorerow+scorecol));
		return scorerow+scorecol;
	}

	private int scoreRows(int [][]board){
        int []duplicates;
        int currScore = 0;
        //int nonDup;
    
        for(int row = 0; row < board.length; row++){
		    //nonDup = 0;
	    	//index zero will be use to state a duplicate was found
	    	duplicates = new int[10]; //reset
		
	    	for(int col = 0; col < board[row].length; col++){
	    	    duplicates[board[row][col]]++;
	    	}
		
	    	//minus one for each non-duplicated number
	    	for(int x = 1; x < duplicates.length; x++){
	    	    if(duplicates[x] == 1){
	    	        currScore--;
	    	        //nonDup++;
	    	    }
	        }
	       
	    }
  //      System.out.println("First Currrrrnet" + currScore);
	    return currScore;
    }

     private int scoreCols(int [][]board){
        int []duplicates;
        //int nonDup;
        int currScore = 0;
        int row = 0;
	    int col = 0;
	    int lim1 = row + 3;
	    int lim2 = col + 3;
	
    	while(row < board.length){
    	    //nonDup = 0;
    		duplicates = new int[10];
		
    		while(row < lim1){
    			col = lim2 - 3;
    			while(col < lim2){
    			   // System.out.print(board[row][col]);
    				duplicates[board[row][col]]++;
    				++col;
    			}
    			//System.out.println();
    			++row;
    		}
    		
    		//minus one for each non-duplicated number
    		for(int x = 1; x < duplicates.length; x++){
    		    if(duplicates[x] == 1){
    		        currScore--;
    		        //nonDup++;
    		    }
    	    }
    		
    		//System.out.println("Num of Non Dup in Square: " + nonDup);
    		
    		if(lim2 < board.length){
    			row = lim1 - 3;
    			col = lim2;
    			lim2 += 3;
    		}
    		else{
    			col = 0;
    			lim2 = 3;
    			row = lim1;
    			lim1 = row + 3;
    		}
    	}
    //	System.out.println("First Currrrrnet" + currScore);
    	return currScore;
    }
}
