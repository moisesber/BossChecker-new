package boss.checker.checker;

import java.util.List;

import boss.checker.checker.bossInfo.BossStatus;
import android.content.Intent;
import android.os.Bundle;

public class BossUpdateIntent extends Intent {


	public BossUpdateIntent(Bundle lastResult) {

		
		this.putExtras(lastResult);
		this.setAction(BossChecker.BOSS_UPDATE);
	}

	
}
