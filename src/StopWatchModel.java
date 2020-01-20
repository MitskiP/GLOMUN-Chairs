import java.util.ArrayList;


public class StopWatchModel implements Runnable {

	private ArrayList<StopWatch> parents;

	private Thread t;
	private boolean running;
	private long time;
	private long resetTo;
	private int sign;

	public StopWatchModel() {
		parents = new ArrayList<StopWatch>();
		setSign(1);
		setResetTo(0);
		reset();
	}
	public boolean containsParent(StopWatch p) {
		return parents.contains(p);
	}
	public void addParent(StopWatch p) {
		parents.add(p);
		//update();
		updateUi();
	}
	public void removeParent(StopWatch p) {
		parents.remove(p);
	}
	public void setSign(int i) { sign = i; updateUi(); }
	public int getSign() { return sign; }
	public void setResetTo(long t) { resetTo = t; }
	public void reset() {
		//time = 100*60*59;
		time = resetTo;
		update();
	}
	private void update() {
		for(int i=0;i<parents.size();i++) {
			//if(parents.)
			parents.get(i).update();
		}
	}
	private void updateUi() {
		for(int i=0;i<parents.size();i++) {
			parents.get(i).updateUi();
		}
	}
	private void timeIsZero() {
		for(int i=0;i<parents.size();i++) {
			parents.get(i).timeIsZero();
		}
	}
	public void start() {
		if(!isRunning()) {
			setRunning(true);
			t = new Thread(this);
			t.start();
		}
	}
	public void pause() {
		if(isRunning()) {
			t.interrupt();
			setRunning(false);
		}
	}
	public void toggle() { if(isRunning()) pause(); else start(); }
	private void setRunning(boolean b) { running = b; updateUi(); }
	public boolean isRunning() { return running; }
	public void addTime(int t) { // in seconds
		time += t*100;
		update();
	}
	@Override
	public void run() {
		try {
			update();
			boolean positive = false;
			while(true) {
				//time++;
				//Thread.sleep(10);
				//if(time % 3 == 0) // update is not constantly necessary; %3 is a good number as all digits are repeated
				//	update();
				time += 3*sign;
				if(time >= 0) positive = true;
				else {
					if(positive && sign < 0) {
						time = 0;
						update();
						timeIsZero();
					}
					positive = false;
				}
				Thread.sleep(30);
				update();
			}
		} catch(Exception e) {
			//reset();
		}
	}
	public int getHour() {
		return (int)time/100/60/60%24;
	}
	public int getMinute() {
		return (int)time/100/60%60;
	}
	public int getSecond() {
		return (int)time/100%60;
	}
	public int getMillSecond() {
		return (int)time%100;
	}
	public long getTime() { return time; }
}
