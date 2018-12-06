### AI Project 7

## Q-Learing
Implement Q-learning. Here is some pseudocode for one iteration of Q-learning. Your program will call this in a loop until it converges:

```
// Pick an action
Let Q be a table of q-values;
Let i be the current state;
Let ε = 0.05;
if(rand.nextDouble() < ε)
{
	// Explore (pick a random action)
	action = rand.nextInt(4);
}
else
{
	// Exploit (pick the best action)
	action = 0;
	for(int candidate = 0; candidate < 4; candidate++)
		if(Q(i, candidate) > Q(i, action))
			action = candidate;
	if(Q(i, action) == 0.0)
		action = rand.nextInt(4);
}

// Do the action
do_action(action);
Let j be the new state

// Learn from that experience
Apply the equation below to update the Q-table.
	a = action.
	Q(i,a) refers to the Q-table entry for doing
		action "a" in state "i".
	Q(j,b) refers to the Q-table entry for doing
		action "b" in state "j".
	Use 0.1 for αk. (Don't get "αk" mixed up with "a".)
	use 0.97 for γ (gamma).sebastian_edwards@nextstepinnovation.com
	A(j) is the set of four possible actions, {<,>,^,v}.
	r(i,a,j) is the reward you obtained when you landed in state j.

// Reset
If j is the goal state, teleport to the start state.
```