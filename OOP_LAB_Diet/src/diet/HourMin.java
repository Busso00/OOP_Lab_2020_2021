package diet;


public class HourMin implements Comparable<HourMin>{
	private Integer hour;
	private Integer min;	
	public HourMin(int hour,int min) {
		this.hour=hour;
		this.min=min;
	}
	public HourMin(String s) {
		Integer hM[]=new Integer[2];
		String hMS[]=new String[2];
		hMS=s.split(":");
		hM[0]=Integer.parseInt(hMS[0]);
		hM[1]=Integer.parseInt(hMS[1]);
		this.hour=hM[0];
		this.min=hM[1];
	}
	@Override
	public int compareTo(HourMin hour) {
		int cfrHour = this.hour.compareTo(hour.hour);
		return cfrHour != 0 ? cfrHour : this.min.compareTo( hour.min);
	}
	@Override
	public String toString() {
		int h=this.hour;
		int m=this.min;
		String hS;
		String mS;
		if(h<10) {
			hS="0"+h;
		}else {
			hS=Integer.toString(h);
		}
		if(m<10) {
			mS="0"+m;
		}else {
			mS=Integer.toString(m);
		}
		return "("+hS+":"+mS+")";
	}
	
}
