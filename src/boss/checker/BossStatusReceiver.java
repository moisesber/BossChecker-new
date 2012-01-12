package boss.checker;

import java.util.List;

import boss.checker.checker.BossChecker;
import boss.checker.checker.BossUpdateIntent;
import boss.checker.checker.bossInfo.BossStatus;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BossStatusReceiver extends BroadcastReceiver {

	private final BossCheckerActivity bossCheckerActivity;

	public BossStatusReceiver(BossCheckerActivity bossCheckerActivity) {
		this.bossCheckerActivity = bossCheckerActivity;
	}

	@Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BossChecker.BOSS_ALIVE)) {
        	
        } else if (intent.getAction().equals(BossChecker.BOSS_UPDATE)) {
    		Log.d("Boss", "Class is = |"+intent.getClass());

//        	BossUpdateIntent bossIntent = (BossUpdateIntent)intent;
    		List<BossStatus> lastResult = BossChecker.getStatusFromBundle(intent.getExtras());
    		this.bossCheckerActivity.drawSpreadSheet(lastResult);
        }
        //Do stuff - maybe update my view based on the changed DB contents
    }

}
