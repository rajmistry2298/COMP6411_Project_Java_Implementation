import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Main Entry Point For Message Passing Project.
 * Reads Calls.txt To Get Sender & Their Receivers Info.
 * Creates Master Process And Starts Its Life Cycle.
 * @author rajmistry
 */
public class Exchange {
	
	public static void main(String[] args) {
		//Reading calls.txt File And Creating List For Sender & Its Contact List
		List<CallsToBeMade> callsToBeMadeList = new ArrayList<>();
		BufferedReader fileReader;
		try {
			fileReader = new BufferedReader(new FileReader("calls.txt"));
			String line = null;
			while((line = fileReader.readLine()) != null){
			    if(line.trim().length() == 0)
			            continue;
			    String[] senderreceivers = line.split(",", 2);
			    String sender = senderreceivers[0].replace("{", "");
			    String[] receivers = senderreceivers[1].replace("[", "").trim().replace("]", "").trim().replace("}", "").trim().replace(".", "").trim().split(",");
			    CallsToBeMade callsData = new CallsToBeMade(sender, receivers);
			    callsToBeMadeList.add(callsData);
			    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Creating Starting Master Process
		MasterThread masterThread = new MasterThread(callsToBeMadeList);
        Thread mainThread = new Thread(masterThread, "Master Process");
        mainThread.start();
	}
}
