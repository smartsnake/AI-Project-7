import java.util.Random;
import java.util.Arrays;
import java.util.Collections;

public class QLearner {
	
	State s = new State();
	State p = new State();
	State goal = new State();
	double [][][] qTable;
	int x, y, z;
	Actions action;
	double [][] reward;

	enum Actions
	{
		North, East, South, West;
	}
	
	
	//Default Constructor
	QLearner(double[][] reward, State goal)
	{
		qTable = new double[20][10][4];
		x = 20;
		y = 10;
		z = 4;
		this.reward = reward;
		this.goal = goal;
	}
	
	//Specify Size of Q-table
	QLearner(int x, int y, int possibleMoves, double[][] reward, State goal)
	{
		qTable = new double[x][y][possibleMoves];
		this.x = x;
		this.y = y;
		z = possibleMoves;
		this.reward = reward;
		this.goal = goal;
	}
	
	void do_action(Actions a, Random r)
	{
		p.x = s.x;
		p.y = s.y;
		if(r.nextDouble() < 0.01)
			do{
				int temp = r.nextInt(4);
				if(temp == 0){
					a = Actions.North;
				}
				else if(temp == 1){
					a = Actions.East;
				}
				else if(temp == 2){
					a = Actions.South;
				}
				else{
					a = Actions.West;
				}
			}
			while(!actionIsValid(a));
		switch(a){
		case North:
			s.y--;
			break;
		case East:
			s.x++;
			break;
		case South:
			s.y++;
			break;
		case West:
			s.x--;
			break;
		}
			
	}
	boolean actionIsValid(Actions a)
	{
		if(a == Actions.North && s.y == 0)
			return false;
		else if(a == Actions.East && s.x == x-1)
			return false;
		else if(a == Actions.South && s.y == y-1)
			return false;
		else if(a == Actions.West && s.x == 0)
			return false;
		else
			return true;
	}
	
	void printQ()
	{
		for(int y = 0; y < 10; y++)
		{
			for(int x = 0; x < 20; x++)
			{
				double bestValue = qTable[x][y][0];
				for(int z = 1; z < 4; z++)
					if(qTable[x][y][z] > bestValue)
						bestValue = qTable[x][y][z];
				System.out.print(bestValue + " ");
			}
			System.out.println("\n");
		}
	}
	
	void printAllQ()
	{
		for(int z = 0; z < 4; z++)
		{
			for(int y = 0; y < 10; y++)
			{
				for(int x = 0; x < 20; x++)
				{
					System.out.print(qTable[x][y][z] + " ");
				}
				System.out.println("\n");
			}
			System.out.println("\n\n\n");
		}
	}
	
	void print()
	{
		int [][] bestAction = new int[20][10];
		for(int x = 0; x < 20; x++)
		{
			for(int y = 0; y < 10; y++)
			{
				bestAction[x][y] = 0;
				for(int i = bestAction[x][y]; i < 4; i++)
					if(qTable[x][y][i] > qTable[x][y][bestAction[x][y]])
						bestAction[x][y] = i;
			}
		}
		
		for(int y = 0; y < 10; y++)
		{
			for(int x = 0; x < 20; x++)
			{
				if((x == 10 && y > 5) || (x == 10 && y < 4))
					System.out.print("# ");
				else if(x == 0 && y == 9)
					System.out.print("S ");
				else if(x == 19 && y == 0)
					System.out.print("G ");
				else if(bestAction[x][y] == 0)
					System.out.print("^ ");
				else if(bestAction[x][y] == 1)
					System.out.print("> ");
				else if(bestAction[x][y] == 2)
					System.out.print("V ");
				else if(bestAction[x][y] == 3)
					System.out.print("< ");
			}
			System.out.println("\n");
		}
		
	}
	
	void learn(Random r)
	{
		double e = 0.05;
		
		//Check if all options are equal
		boolean different = false;
		for(int i = 0; i < z-1; i++)
			if(qTable[s.x][s.y][i] != qTable[s.x][s.y][i+1])
				different = true;
		
		if(r.nextDouble() < e || !different)
			do
			{
				int temp = r.nextInt(z);
				if(temp == 0){
					action = Actions.North;
				}
				else if(temp == 1){
					action = Actions.East;
				}
				else if(temp == 2){
					action = Actions.South;
				}
				else{
					action = Actions.West;
				}
			}
			while (!actionIsValid(action));
		else
		{
			//Exploit (choose the best valid action)
			Actions[] act = Actions.values();
			int index = 0;
			action = Actions.North;
			while(!actionIsValid(action))
				action = act[index++];
			for(Actions a : act)
				if(qTable[s.x][s.y][a.ordinal()] > qTable[s.x][s.y][action.ordinal()])
					if(actionIsValid(a))
						action = a;
		}
		do_action(action, r);
		Actions a = action;
		int b = 0;
		double alphaK = 0.2;
		double gamma = 0.99; 
		Actions[] act = Actions.values();
		while(!actionIsValid(act[b]))
			b++;
		for(Actions A : act)
			if(qTable[s.x][s.y][A.ordinal()] > qTable[s.x][s.y][act[b].ordinal()])
				if(actionIsValid(A))
					b = A.ordinal();
		qTable[p.x][p.y][a.ordinal()] = (1 - alphaK) * qTable[p.x][p.y][a.ordinal()] + alphaK * (reward[s.x][s.y] + gamma * qTable[s.x][s.y][b]);
		if(s.equals(goal))
			s = new State();
		
	}

}
