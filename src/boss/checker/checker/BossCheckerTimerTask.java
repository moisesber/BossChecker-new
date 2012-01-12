package boss.checker.checker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.util.Log;
import boss.checker.checker.bossInfo.BossStatus;
import boss.checker.properties.BossCheckerProperties;

//public class BossCheckerTimerTask extends TimerTask {
public class BossCheckerTimerTask {

	private final BossChecker parent;
	private boolean aBossIsAlive;

	public BossCheckerTimerTask(BossChecker parent){
		this.parent = parent;
	}
	
	public List<BossStatus> getAlts(String string) throws IOException{
		StringTokenizer tokens = new StringTokenizer(string);
		List<BossStatus> alts = new ArrayList<BossStatus>();
//		System.out.println("String = "+string);
		
		while(tokens.hasMoreTokens()){
			String next = tokens.nextToken();
			boolean updateDate = false;
			
			if(next.contains("alt")){
				String boss = next.replaceAll("alt", "").replaceAll("=", "").replace("\"", "");
//				System.out.println("Olha o boss |"+boss+"|");
				next = tokens.nextToken();
//				System.out.println("Olha o next |"+next+"|");

				if(boss.equalsIgnoreCase("Queen")){
					next = tokens.nextToken();
				}
				boolean status = false;
				if(checkMorto(next)){
					status = false;
					
					
				} else {
					this.aBossIsAlive = true;
					status = true;
					updateDate = true;
//					BossFileWriter.writeBossStatus(boss, new Date());
				}
				
				alts.add(new BossStatus(boss,status, this.getDate(updateDate,boss)));
			}
		}
		
		return alts;
	}
	
	private Date getDate(boolean updateDate, String boss) {
		return updateDate ? new Date() : this.parent.getBossLastBossStatus(boss) != null ? this.parent.getBossLastBossStatus(boss).getLastAlive() : null;
	}

	public boolean checkMorto(String s){
		return s.contains("Morto") || s.contains("morto");
	}
	
	public List<BossStatus> checkBossIsAlive(){
		this.aBossIsAlive = false;
		// -----------------------------------------------------//
		// Step 1: Start creating a few objects we'll need.
		// -----------------------------------------------------//

		URL u;
		InputStream is = null;
		BufferedReader dis;
		String s;

		try {

			// ------------------------------------------------------------//
			// Step 2: Create the URL. //
			// ------------------------------------------------------------//
			// Note: Put your real URL here, or better yet, read it as a //
			// command-line arg, or read it from a file. //
			// ------------------------------------------------------------//
//			String link = BossCheckerProperties.getInstance().getPropertie(BossCheckerProperties.SITE_URL);
			String link = "http://anonymouse.org/cgi-bin/anon-www.cgi/http://www.l2ouro.com/pt-br/lineage2/pvp/spawnboss/";
			u = new URL(link);
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpGet httpGet = new HttpGet(link);
			HttpResponse response = httpClient.execute(httpGet, localContext);
			 
			dis = new BufferedReader(
			    new InputStreamReader(
			      response.getEntity().getContent()
			    )
			  );
			 
//			String line = null;
//			while ((line = reader.readLine()) != null){
//			  result += line + "\n";
//			}

			// ----------------------------------------------//
			// Step 3: Open an input stream from the url. //
			// ----------------------------------------------//

//			is = u.openStream(); // throws an IOException

			// -------------------------------------------------------------//
			// Step 4: //
			// -------------------------------------------------------------//
			// Convert the InputStream to a buffered DataInputStream. //
			// Buffering the stream makes the reading faster; the //
			// readLine() method of the DataInputStream makes the reading //
			// easier. //
			// -------------------------------------------------------------//

//			dis = new BufferedReader(new InputStreamReader(
//					new BufferedInputStream(is)));

			// ------------------------------------------------------------//
			// Step 5: //
			// ------------------------------------------------------------//
			// Now just read each record of the input stream, and print //
			// it out. Note that it's assumed that this problem is run //
			// from a command-line, not from an application or applet. //
			// ------------------------------------------------------------//

			StringBuffer siteData = new StringBuffer();
			String morto = "";
			while ((s = dis.readLine()) != null) {
				siteData.append(s);

				if (morto.equals("")
						&& (checkMorto(s))) {
					morto = s;
				}
			}

//			System.out.println("String capturada: "+morto);
			if(morto.equals("")){
				return null;
			}

			List<BossStatus> alts = getAlts(morto);
			StringBuffer buffer = new StringBuffer();
			for (BossStatus bossStatus : alts) {
//				System.out.println(bossStatus);
				buffer.append(bossStatus);
			}
			return alts;
		} catch (MalformedURLException mue) {

//			System.out.println("Ouch - a MalformedURLException happened.");
			mue.printStackTrace();
			System.exit(1);

		} catch (IOException ioe) {

//			System.out.println("Nao consegui conectar com site, verifique suas conexoes de rede.");
//			ioe.printStackTrace();
//			System.exit(1);

		} finally {

			// ---------------------------------//
			// Step 6: Close the InputStream //
			// ---------------------------------//

			try {
				if(is != null){
					is.close();
				}
				
			} catch (IOException ioe) {
				// just going to ignore this one
			}

		} // end of 'finally' clause
		
		return null;
	}

	public void run() {
//		System.out.println("Atualizando status dos bosses.");
		Log.d("Boss", "Timer Task should be running now...");
		this.parent.incCounter();
		List<BossStatus> result = this.checkBossIsAlive();
		Log.d("Boss", "Status got....");
//		System.out.println(+this.parent.getCounter()+" - Status atualizado com sucesso, verificando se ha alguma modificacao.");
		
		if(( this.parent.getLastResult() == null && someBossIsAlive(result))){
//			System.out.println("1");
			modifiedResultDetected(result);
			return;
		}
		
		if( this.parent.getLastResult() != null ){
			if(!newEqualsOldResult(result) || someBossIsAlive(result)){
//				System.out.println("2");
				modifiedResultDetected(result);
				return;
			}
		}
		Log.d("Boss", "No modification nor boss alive...");
//			System.out.println("Nenhuma modificacao nem boss vivo :/");
		
		this.parent.setLastResult(result);
	}

	private void modifiedResultDetected(List<BossStatus> result) {
		Log.d("Boss", "Modification detected!");
//		System.out.println("Modificacao detectada.");
		try {
			this.parent.setLastResult(result);
			this.parent.updateBossStatus(result,this.aBossIsAlive);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean someBossIsAlive(List<BossStatus> result) {
		if(result == null){
			return false;
		}
		for (BossStatus bossStatus : result) {
			if(bossStatus.isAlive()){
				return true;
			}
		}
		return false;
	}

	private boolean newEqualsOldResult(List<BossStatus> result) {
		if((result == null && this.parent.getLastResult() != null) || (result != null && this.parent.getLastResult() == null)){
			return false;
		}
		
		for (BossStatus bossStatus : result) {
			if(!this.parent.getLastResult().contains(bossStatus)){
				return false;
			}
		}
		return true;
	}

}
