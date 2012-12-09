package project_7;

import java.util.Comparator;

public class schedule {
	public int finish=0;
	public int remaining_time=0;
	public float ratio=0;
	public int ID = 0;//the id of the process
	public int arrive_Time = 0;//when the process is ready to run
	public int time_To_Completion = 0; //how long it take for the thread to complete
	public int wait;
	public int turnAround;
	/**
	 * @param args
	 */
	schedule(int I, int AT, int TTC) {
		// TODO Auto-generated method stub
		ID = I;
		arrive_Time = AT;
		time_To_Completion = TTC;
		finish = 0;
		remaining_time = 0;
		ratio = 0;
	}
	public int compareTo(schedule compare){
		int compareQ = ((schedule) compare).ID;
		return this.ID-compareQ;
	}
	public static Comparator<schedule> shceduleComparator = new Comparator<schedule>(){
		public int compare(schedule a, schedule b){
			Integer Id1 = a.ID;
			Integer Id2 = b.ID;
			
			return Id1.compareTo(Id2);
		}
	};
}
