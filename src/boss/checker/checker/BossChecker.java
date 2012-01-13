package boss.checker.checker;

//------------------------------------------------------------//
//JavaGetUrl.java:                                          //
//------------------------------------------------------------//
//A Java program that demonstrates a procedure that can be  //
//used to download the contents of a specified URL.         //
//------------------------------------------------------------//
//Code created by Developer's Daily                         //
//http://www.DevDaily.com                                   //
//------------------------------------------------------------//

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Timer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import boss.checker.checker.bossInfo.BossStatus;

public class BossChecker extends Service {
	
	public static String BOSS_ALIVE = "BOSS_ALIVE!";
	public static String BOSS_UPDATE = "BOSS_UPDATE";
	
	public static String LAST_ALIVE_SUFIX = "|lastAlive";


	private List<BossStatus> bossStatus;
	private List<BossStatus> lastResult;

	private int counter;
	private Timer timer;
	private boolean firstTime;
    private Handler mHandler;
	private BossCheckerTimerTask bossCheckerTimerTask;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("Boss", "BossChecker Service created...");
		this.firstTime = true;

//		this.startTimer();
		
		
        mHandler = new Handler();
        bossCheckerTimerTask = new BossCheckerTimerTask(this,mHandler);
        
        mHandler.removeCallbacks(bossCheckerTimerTask);
        mHandler.postDelayed(bossCheckerTimerTask, 0);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		
		this.timer.cancel();
	}

	public void updateBossStatus(List<BossStatus> result, boolean aBossIsAlive) throws Exception {
		this.bossStatus = result;
		Log.d("Boss", "Updating boss status...");

		//update table here

//		bossCheckerActivity.drawSpreadSheet();
		sendBroadcast(new Intent(new BossUpdateIntent(BossChecker.getBundleFromStatus(this.getLastResult()))));

		
		if(aBossIsAlive){
			Log.d("Boss", "A boss is alive, should notify...");
			sendBroadcast(new Intent(new BossAlive()));
			//send notification alert!
		}
		
	}
	


	
	public void initLastResult(){
//		BossCheckerTimerTask initCheck = new BossCheckerTimerTask(this);
//		this.lastResult = initCheck.checkBossIsAlive();
	}

	private List<BossStatus> defaultLastResult() {
		this.lastResult = new ArrayList<BossStatus>();
		
		lastResult.add(new BossStatus("Valakas", false, null));
		lastResult.add(new BossStatus("Antharas", false, null));
		lastResult.add(new BossStatus("Baium", false, null));
		lastResult.add(new BossStatus("Zaken", false, null));
		lastResult.add(new BossStatus("Ant Queen", false, null));
		lastResult.add(new BossStatus("Core", false, null));
		
		return lastResult;
	}

	private void showNewBossStatus() {
		if(this.bossStatus == null){
			return ;
		}
		
		for (BossStatus bossStat : this.bossStatus) {
			System.out.println(bossStat);
		}
	}

	public void setLastResult(List<BossStatus> lastResult) {
		if(lastResult == null){
			return;
		}
		for (BossStatus bossStatus : lastResult) {
			if(bossStatus.getLastAlive() == null && this.getBossLastBossStatus(bossStatus.getBoss()) != null){
				bossStatus.setLastAlive(this.getBossLastBossStatus(bossStatus.getBoss()).getLastAlive());
			}
		}
		this.lastResult = lastResult;
		
	}

	public List<BossStatus> getLastResult() {
//		return this.lastResult == null ? this.defaultLastResult() : this.lastResult;
		if(this.firstTime){
			this.firstTime = false;
			return this.lastResult == null ? this.defaultLastResult() : this.lastResult;
		}
		return this.lastResult;
	}

//	public void startTimer() {
//		new BossCheckerTimerTask(this).run();
//
//		timer = new Timer();
//		int delay = Integer.parseInt(BossCheckerProperties.getInstance()
////				.getPropertie(BossCheckerProperties.CHECKER_DELAY_STRING)) * 1000 * 60;
//				.getPropertie(BossCheckerProperties.CHECKER_DELAY_STRING)) * 1000 * 2;
//		timer.scheduleAtFixedRate(new BossCheckerTimerTask(this),0, delay);
//	}
	
	
	public void incCounter(){
		counter++;
	}
	
	public int getCounter(){
		return this.counter;
	}

	public BossStatus getBossLastBossStatus(String boss) {
		if(this.lastResult == null){
			return null;
		}
		
		for (BossStatus bossStatus : this.lastResult) {
			if(bossStatus.getBoss().equals(boss)){
				return bossStatus;
			}
		}
		return null;
	}

	public void windowClosed() {
		timer.cancel();
		mHandler.removeCallbacks(bossCheckerTimerTask);
	}

	// URL url = new
	// URL("http://anonymouse.org/cgi-bin/anon-www.cgi/http://www.l2ouro.com/lineage2/grandboss.php");
	// HTML
	// System.out.println((String) url.getContent());

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public BossChecker getService() {
            return BossChecker.this;
        }
    }

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	
	public static Bundle getBundleFromStatus(List<BossStatus> lastResult){
		Bundle bd = new Bundle();

		for (BossStatus bossStatus : lastResult) {
			bd.putString(bossStatus.getBoss(), bossStatus.isAlive()+"");
			
			if(bossStatus.getLastAlive() != null){
				bd.putString(bossStatus.getBoss()+LAST_ALIVE_SUFIX, bossStatus.getLastAlive().toString() );
			}
			
		}
		
		return bd;
	}
	
	public static List<BossStatus> getStatusFromBundle(Bundle lastResult){
		List<BossStatus> list = new ArrayList<BossStatus>();

		Set<String> keys = lastResult.keySet();
		for (String bossName : keys) {
			boolean isAlive = Boolean.parseBoolean(lastResult.getString(bossName));
			
			String lastAlive = null;
			if(lastResult.containsKey(bossName+LAST_ALIVE_SUFIX)){
				String lastResultAlive = lastResult.getString(bossName+LAST_ALIVE_SUFIX);
				Log.d("Boss", "Last alive received for "+bossName+" was "+lastResultAlive);

//				if(lastResultAlive != null && !lastResultAlive.equals("null")){
//					lastAlive = new Date(lastResultAlive);	
//				} else { 
//					lastAlive = null;
//				}
			}

			if(!bossName.contains(LAST_ALIVE_SUFIX)) {
				list.add(new BossStatus(bossName, isAlive, lastAlive));
			}
		}
		
		return list;
	}
	
	
}
