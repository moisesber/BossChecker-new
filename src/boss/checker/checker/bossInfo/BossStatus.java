package boss.checker.checker.bossInfo;

import java.util.Date;

public class BossStatus {

	public BossStatus(String boss, boolean status, Date lastAlive) {
		this.boss = boss;
		this.alive = status;
		this.lastAlive = lastAlive;
	}

	private String boss;
	private boolean alive;
	private Date lastAlive;

	public String toString() {
		return "O boss " + boss + " esta " + this.statusToString();
	}

	private String statusToString() {
		return alive ? "vivo!" : "morto";
	}

	public boolean equals(Object o) {
		if (o instanceof BossStatus) {
			BossStatus other = (BossStatus) o;

			return this.boss.equals(other.boss)
					&& this.alive == other.alive
					&& ((this.lastAlive != null && other.lastAlive != null) ? this.lastAlive
							.equals(other.lastAlive)
							: true);
		} else {
			return false;
		}
	}

	public String getBoss() {
		return boss;
	}

	public boolean isAlive() {
		return alive;
	}

	public String[] toStringArray() {
		return new String[] { boss, Boolean.toString(alive),
				this.lastAlive != null ? this.lastAlive.toString() : "null" };
	}

	public Date getLastAlive() {
		return lastAlive;
	}
	
	public void setLastAlive(Date lastAlive){
		this.lastAlive = lastAlive;
	}

}
