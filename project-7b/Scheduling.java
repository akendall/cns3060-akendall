package project_7;

import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;

public class Scheduling {
	public static void FCFS(schedule[] s){
		int turnAround = 0;
		int length = 0;
		int averageWait = 0;
		int averageTurnAround = 0;
		int count = 0;
		int wait = 0;
		System.out.println("First Come First Serve\nPid\tWait\tTurnaround\n---\t-----\t----------");
		for(int i = 0; i<s.length-1; i++)
		{
			length+=s[i].time_To_Completion;
			wait = length - (s[i].arrive_Time+s[i].time_To_Completion);
			turnAround = wait + s[i].time_To_Completion;
			averageWait +=wait;
			averageTurnAround += turnAround;
			count++;
			System.out.println(s[i].ID + "\t" + wait + "\t" + turnAround);
		}
		System.out.println("Average wait: " + (double)averageWait/count + " Average turnaround: " + (double)averageTurnAround/count+ "\n");
	}
	public static void SJF(schedule[] s){
		int turnAround = 0;
		int length = 0;
		int averageWait = 0;
		int averageTurnAround = 0;
		int count = 0;
		int wait = 0;
		schedule shortest = null;
		int index = 0;
		int done = 0;
		int iter = 0;
		int l = s.length;
		List<schedule> list = new ArrayList<schedule>();
		for(int m = 0; m<s.length-1; m++){
			list.add(m, s[m]);
		}
		List<schedule> ready = new ArrayList<schedule>();
		schedule[] q = new schedule[s.length];
		System.out.println("Shortest Job First\nPid\tWait\tTurnaround\n---\t-----\t----------");
		for(int i = 0; i<=s.length-2; i++){
			if(i==0){
				shortest = list.get(i);
				length+=shortest.time_To_Completion;
				turnAround = wait + shortest.time_To_Completion;
				averageWait +=wait;
				averageTurnAround += turnAround;
				count++;
			}else{
				//check to see which ones came into the queue
				if((shortest = list.get(0))!=null){
					for(int j = l-2; j>=0; j--){
						if(list.get(j).arrive_Time<=length){
							ready.add(index, list.get(j));
							index++;
						}
					}
					for(int k = index-1; k>=0; k--){
						//shortest = ready.get(k);
						if (ready.get(k).time_To_Completion<shortest.time_To_Completion){
							shortest = ready.get(k);
							done++;
						}	
					}
					length+=shortest.time_To_Completion;
					wait = length - (shortest.arrive_Time+shortest.time_To_Completion);
					turnAround = wait + shortest.time_To_Completion;
					averageWait +=wait;
					averageTurnAround += turnAround;
					count++;
					ready.removeAll(ready);
					index = 0;
				}
			}
			System.out.println(shortest.ID + "\t" + wait + "\t" + turnAround);
			for(int o=0; o<l-1;o++){
				if(list.get(o).equals(shortest)&&list.get(o)!=null){
					list.remove(o);
					l--;
				}
			}
		}
		System.out.println("Average wait: " + (double)averageWait/count + " Average turnaround: " + (double)averageTurnAround/count+ "\n");
	}
	static int no;
	static public schedule[] p;
	public static void SRTN(schedule[] s){
		p = new schedule[s.length];
		int i, j, k = 0, time = 0, average_turn = 0, n = s.length-1;
		float average_wait = 0;
		for(i = 0; i<n; i++){
			p[i]=s[i];
			p[i].remaining_time = s[i].time_To_Completion;
		}
		no = 0;
		j = 1;
		while(chkprocess(n)==1){
			if(p[no+1] != null && p[no+1].arrive_Time==time){
				no++;
				if(p[j].remaining_time==0)
					p[j].finish=time;
				j = nextprocess();
			}
			if(p[j].remaining_time !=0){
				p[j].remaining_time--;
				for(i=1;i <= no; i++){
					if(i !=j && p[i].remaining_time != 0)
						p[i].wait++;
				}
			}else{
				p[j].finish = time;
				j=nextprocess();
				time--;
				k=j;
			}
			time++;
		}
		p[k].finish = time;
		System.out.println("Shortest Remaining Time Next\nPid\tWait\tTurnaround\n---\t-----\t----------");
		for(i=0;i<n; i++){
			average_wait += p[i].wait;
			p[i].turnAround = p[i].wait+p[i].time_To_Completion;
			average_turn += p[i].turnAround;
			p[i].ratio = (float)p[i].turnAround/(float)p[i].time_To_Completion;
			
			System.out.println(p[i].ID + "\t"+p[i].wait+"\t"+p[i].turnAround);
		}
		System.out.println("Average wait: " + (double)average_wait/n + " Average turnaround: " + (double)average_turn/n + "\n");
	}
	public static int chkprocess(int s){
		int i;
		for(i=0; i<s; i++){
			if(p[i].remaining_time != 0)
				return 1;
		}
		return 0;
	}
	public static int nextprocess(){
		int min, l = 0, i;
		min = 32000;
		for(i=1;i<=no;i++){
			if(p[i].remaining_time != 0 && p[i].remaining_time < min){
				min = p[i].remaining_time;
				l=i;
			}
		}
		return l;
	}
	public static void RR(schedule[] s){
		//easy to implement with a list 
		ArrayList<schedule> ready = new ArrayList<schedule>();
		int index = 0;

		int time = 4;
		int length = 0;
		int current_wait = 0;
		//some code here populates the list the first time
		List<schedule> list = new ArrayList<schedule>();
		for(int m = 0; m<s.length-1; m++){
			list.add(m, s[m]);
		}
		boolean needToWork = true;
		for(int i= 0; i< 50; i++){
			for(int j = (s.length-2)-index; j>=0; j--){
				if(list.get(j).arrive_Time<=length){
					list.get(j).remaining_time = list.get(j).time_To_Completion;
					list.get(j).wait = length - list.get(j).arrive_Time;
					ready.add(0, list.get(j));
					list.remove(j);
					index++;
				}
			}
			needToWork = true;
			while(needToWork){
				int count = index;
				while(count!=0){
					schedule nextTask = ready.remove(0);//get the first object 
					if(nextTask.remaining_time > 0){
						if((nextTask.remaining_time-time)>0){
							length +=4;
							nextTask.remaining_time = nextTask.time_To_Completion-time;
							nextTask.turnAround += nextTask.time_To_Completion;
						}else{
							current_wait += nextTask.remaining_time;
							nextTask.turnAround += nextTask.remaining_time;
							length += nextTask.remaining_time;
							nextTask.remaining_time = 0;
						}
					}
					ready.add(nextTask);
					needToWork = false;
					count--;
					}
			}
		}
		int j;
		schedule tmp = ready.get(0);
		for(int i=0;i<s.length-1;i++){
			s[i] = ready.get(i);
		}
		for(int i=0;i<s.length-1;i++){
			j = i-1;
			tmp = s[i];
			while((j>=0)&&(tmp.ID<s[j].ID)){
				s[j+1]= s[j];
				j--;
			}
			s[j+1]=tmp;
			//insertionSort(s);
		}
		
		System.out.println("Round Robin\nPid\tWait\tTurnaround\n---\t-----\t----------");
		for(int o=0; o<index; o++)
			System.out.println(s[o].ID + "\t"+s[o].wait+"\t"+s[o].turnAround);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// set up variables 
		String [] input = new String[100];
		String [] sp = new String[5];
		String in = null;
		Integer pId = 0;
		Integer wait = 0;
		Integer turnaround = 0;//time from when the thread first enters the state to when it exits
		//response time; the time it takes for the system to respond to some user aciton
		Integer count = 0;
		boolean done = false;
		System.out.println("Enter numbers to represent the schedule:");
		System.out.println("ID\tArrival Time\tTime to completion\n");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			while(!done){
				in = br.readLine();
				if(in.equals("") || in.equals(null))
					done = true;
				input[count] = in;
				count++;
			}
			
		}catch (IOException e ){
			System.err.println("Error: " + e.getMessage());
		}

		schedule[] s = new schedule[count];
		for (int i=0; i<count-1; i++){
			sp = input[i].split(" ");
			pId = Integer.parseInt(sp[0]);
			wait = Integer.parseInt(sp[1]);
			turnaround = Integer.parseInt(sp[2]);
			s[i] = new schedule(pId, wait, turnaround);
		}
		FCFS(s);
		SJF(s);
		SRTN(s);
		RR(s);
	}

}
