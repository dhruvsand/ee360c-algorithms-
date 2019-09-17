import java.util.ArrayList;
/*
 * Name: Dhruv Sandesara
 * EID: djs3967
 */

public class Graph implements Program2{
	// n is the number of ports
	private int n;

	// Edge is the class to represent an edge between two nodes
	// node is the destination node this edge connected to
	// time is the travel time of this edge
	// capacity is the capacity of this edge
	// Use of this class is optional. You may make your own, and comment
	// this one out.
	private class Edge{
		public int node;
		public int time;
		public int capacity;
		public Edge(int n, int t, int c){
			node = n;
			time = t;
			capacity = c;
		}

		// prints out an Edge.
		public String toString() {
			return "" + node;
		}
	}

	// Here you have to define your own data structure that you want to use
	// to represent the graph
	// Hint: This include an ArrayList or many ArrayLists?
	// ....

    //ArrayList<ArrayList<Edge>> Adjacency_Matrix_2 = new ArrayList<ArrayList<Edge>>();

    //Edge[][] Adjacency_Matrix= new Edge[n][n];
    Edge[][] Adjacency_Matrix;


    // This function is the constructor of the Graph. Do not change the parameters
	// of this function.
	//Hint: Do you need other functions here?
	public Graph(int x) {
		n = x;
        Adjacency_Matrix= new Edge[n][n];
	}
    
	// This function is called by Driver. The input is an edge in the graph.
	// Your job is to fix this function to generate your graph.
	// Do not change its parameters or return type.
	// Hint: Here is the place to build the graph with the data structure you defined.
	public void inputEdge(int port1, int port2, int time, int capacity) {

	    Edge e1= new Edge(port2,time,capacity);
	    Adjacency_Matrix[port1][port2]=e1;
        Edge e2= new Edge(port1,time,capacity);
        Adjacency_Matrix[port2][port1]=e2;

		return;
	}

	// This function is the solution for the Shortest Path problem.
	// The output of this function is an int which is the shortest travel time from source port to destination port
	// Do not change its parameters or return type.
	public int findTimeOptimalPath(int sourcePort, int destPort) {
        int dist []= new int[n];

        for (int i=0;i<n;i++){
            dist[i]= Integer.MAX_VALUE;
        }

        dist[sourcePort]=0;
        ArrayList<Integer> queue= new ArrayList<Integer>();
        for (int i=0;i<n;i++)
            queue.add(i);

        while (!queue.isEmpty()){
            int temp_dist = Integer.MAX_VALUE;//to get the min distace first intialize to max value
            int temp_dist_index=0,temp_queue_index=0;

            for (int i=0;i<queue.size();i++) {
                if (dist[queue.get(i)] < temp_dist) {//this basically gets the node with the last time
                    temp_dist = dist[queue.get(i)];//to easily get the distance
                    temp_dist_index = queue.get(i);
                    temp_queue_index = i;//to remember which index to remove from queue
                }
            }


            if(temp_dist==Integer.MAX_VALUE)
                    break;//this means that both the start and the end are disconnected

                queue.remove(temp_queue_index);//beacue we are abt to compare with this node we need to take it out

            if(temp_dist_index==destPort)
                    break;//we found our port


        ArrayList<Integer> neighbours =getNeighbors(temp_dist_index);

        for (int neighbour_index=0;neighbour_index<neighbours.size();neighbour_index++){
            int alt = dist[temp_dist_index] + Adjacency_Matrix[temp_dist_index][neighbours.get(neighbour_index)].time;

            if(alt<dist[neighbours.get(neighbour_index)])
                dist[neighbours.get(neighbour_index)]=alt;
        }
        }
		return dist[destPort];
	}

	// This function is the solution for the Widest Path problem.
	// The output of this function is an int which is the maximum capacity from source port to destination port 
	// Do not change its parameters or return type.
	public int findCapOptimalPath(int sourcePort, int destPort) {
        int capacity []= new int[n];

        for (int i=0;i<n;i++){
            capacity[i]= 0;
        }

        capacity[sourcePort]=Integer.MAX_VALUE;
        ArrayList<Integer> queue= new ArrayList<Integer>();
        for (int i=0;i<n;i++)
            queue.add(i);

        while (!queue.isEmpty()){
            int temp_capacity = 0;//to get the min capacity first intialize to min value
            int temp_capacity_index=0,temp_queue_index=0;

            for (int i=0;i<queue.size();i++) {
                if (capacity[queue.get(i)] > temp_capacity) {//this basically gets the node with the most throughtput
                    temp_capacity = capacity[queue.get(i)];//to easily get the capacity
                    temp_capacity_index = queue.get(i);
                    temp_queue_index = i;//to remember which index to remove from queue
                }
            }


            if(temp_capacity==0)
                break;//this means that both the start and the end are disconnected

            queue.remove(temp_queue_index);//beacue we are abt to compare with this node we need to take it out

            if(temp_capacity_index==destPort)
                break;//we found our port


            ArrayList<Integer> neighbours =getNeighbors(temp_capacity_index);
            int previous_node_capacity=capacity[temp_capacity_index];
            for (int neighbour_index=0;neighbour_index<neighbours.size();neighbour_index++){
                int edge_capacity =  Adjacency_Matrix[temp_capacity_index][neighbours.get(neighbour_index)].capacity;
                int min_capacity=0;
                if(edge_capacity<previous_node_capacity)
                    min_capacity=edge_capacity;
                else
                    min_capacity=previous_node_capacity;


                if(min_capacity>capacity[neighbours.get(neighbour_index)])
                    capacity[neighbours.get(neighbour_index)]=min_capacity;
            }
        }
        return capacity[destPort];
	}

	// This function returns the neighboring ports of node.
	// This function is used to test if you have contructed the graph correct.
	public ArrayList<Integer> getNeighbors(int node) {
		ArrayList<Integer> edges = new ArrayList<Integer>();
		// ...
        for (int i=0; i<n;i++){
            if(Adjacency_Matrix[node][i]!=null)
                edges.add(i);
        }


		return edges;
	}

	public int getNumPorts() {
		return n;
	}
}
