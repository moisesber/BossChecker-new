package boss.checker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import boss.checker.checker.BossChecker;
import boss.checker.checker.bossInfo.BossStatus;

public class BossCheckerActivity extends Activity {
	
	private BossStatusReceiver dataUpdateReceiver;
	
	private List<BossStatus> defaultLastResult() {
		List<BossStatus> lastResult = new ArrayList<BossStatus>();
		
		lastResult.add(new BossStatus("Valakas", false, null));
		lastResult.add(new BossStatus("Antharas", false, null));
		lastResult.add(new BossStatus("Baium", false, null));
		lastResult.add(new BossStatus("Zaken", false, null));
		lastResult.add(new BossStatus("Ant Queen", false, null));
		lastResult.add(new BossStatus("Core", false, null));
		
		return lastResult;
	}
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataUpdateReceiver = new BossStatusReceiver(this);
        
        int SECS = 1000;
        int MINS = 60 * SECS;
        Calendar cal = Calendar.getInstance();
        Intent in = new Intent(this, BossChecker.class);
        PendingIntent pi = PendingIntent.getService(this, 0, in, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarms = (AlarmManager)this.getSystemService(
                Context.ALARM_SERVICE);
        alarms.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 
                10 * SECS, pi);

        
        
//        setContentView(R.layout.main);
        
//        this.startActivity(new Intent());
        
//        mService = BossChecker.getInstance();
//        while(mService == null){
//            mService = BossChecker.getInstance();
//        }
      
//        ScrollView sv = new ScrollView(this);
//        
//            LinearLayout ll = new LinearLayout(this);
//            sv.addView(ll);
//
//            EditText tv = new EditText(this);
//            tv.setText("Queen Ant");
//            tv.setKeyListener(null);
//            ll.addView(tv);
//            
//            tv = new EditText(this);
//            tv.setText("false");
//            tv.setClickable(false);
//            ll.addView(tv);
//
//            HorizontalScrollView hsv = new HorizontalScrollView(this);
//            tv = new EditText(this);
//            tv.setText(new GregorianCalendar().getTime().toString());
//            tv.setClickable(false);
//            hsv.addView(tv);
//            ll.addView(hsv);
//        
//        this.setContentView(sv);
        
        drawSpreadSheet(this.defaultLastResult());
        Log.d("Boss", "Starting service...");
//        Intent svc = new Intent(this, BossChecker.class);
//        Intent svc = new Intent("boss.checker.checker.BossChecker");
//        this.startService(svc);
        Log.d("Boss", "Service should be running now...");

        
    }
    




	public void drawSpreadSheet(List<BossStatus> status) {
		final BossCheckerActivity activity = this;
		final List<BossStatus> bossStatuses = status;
		runOnUiThread(new Runnable() {
		     public void run() {
		         Log.d("Boss", "Drawing status...");

//		         List<BossStatus> status = mService.getLastResult();
//		       List<BossStatus> status = defaultLastResult();

		         ScrollView sv = new ScrollView(activity);
		         LinearLayout vll = new LinearLayout(activity);
		         vll.setOrientation(LinearLayout.VERTICAL);
		         sv.addView(vll);
		         
		         int index = 0;
		         for (BossStatus bossStatus : bossStatuses) {
		             Log.d("Boss", "Status line...");

		             LinearLayout ll = new LinearLayout(activity);
		             vll.addView(ll);

		             EditText tv = new EditText(activity);
		             tv.setText(bossStatus.getBoss());
		             tv.setKeyListener(null);
		             ll.addView(tv);
		             
		             tv = new EditText(activity);
		             tv.setText(bossStatus.isAlive()+ "");
		             tv.setClickable(false);
		             ll.addView(tv);

		             HorizontalScrollView hsv = new HorizontalScrollView(activity);
		             tv = new EditText(activity);
		             Date lastAlive = bossStatus.getLastAlive();
		             tv.setText(lastAlive != null? lastAlive.toString() : "null");

//		             tv.setText(lastAlive.toString());
		             tv.setClickable(false);
		             hsv.addView(tv);
		             ll.addView(hsv);
		 		}
		         
		         setContentView(sv);

		    }
		});
		
        Log.d("Boss", "Status drawn...");

	}
    
    @Override
    public void onResume(){
    	super.onResume();
    	
    	if (dataUpdateReceiver == null) dataUpdateReceiver = new BossStatusReceiver(this);
    	IntentFilter intentFilter = new IntentFilter(BossChecker.BOSS_ALIVE);
    	registerReceiver(dataUpdateReceiver, intentFilter);
    	intentFilter = new IntentFilter(BossChecker.BOSS_UPDATE);
    	registerReceiver(dataUpdateReceiver, intentFilter);
    }
    @Override
    public void onPause(){
    	if (dataUpdateReceiver != null) unregisterReceiver(dataUpdateReceiver);
    }
    
//    
//    @Override
//    public void onStop(){
//    	super.onStop();
//    	
//    	mService.stopSelf();
//    }
//    
//    /** Defines callbacks for service binding, passed to bindService() */
//    private ServiceConnection mConnection = new ServiceConnection() {
//
//        public void onServiceConnected(ComponentName className,
//                IBinder service) {
//            // We've bound to LocalService, cast the IBinder and get LocalService instance
//            LocalBinder binder = (LocalBinder) service;
//            mService = binder.getService();
//            mBound = true;
//        }
//
//        public void onServiceDisconnected(ComponentName arg0) {
//            mBound = false;
//        }
//    };
}