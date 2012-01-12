package boss.checker.checker;

import android.content.Intent;

public class BossAlive extends Intent {
	
	public BossAlive(){
		this.setAction(BossChecker.BOSS_ALIVE);
	}
	

}
