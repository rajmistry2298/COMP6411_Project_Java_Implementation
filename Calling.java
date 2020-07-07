import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
/**
 * Abstract Class Providing Life Cycle Of Master & Friends Processes
 * @author rajmistry
 *
 */
abstract public class Calling implements Runnable{

    private  long lastUpdateTime = System.currentTimeMillis();
    
    private boolean controlWaitingTime(){
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastUpdateTime >= getAllowedWaitTime()){
            return  false;
        }
        return true;
    }

    @Override
    public void run() {
        startProcess();
        try {
            do{
                Thread.sleep(new Random().nextInt(100) + 1);
                messagePassing();
            }while(controlWaitingTime());
        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception: "+e);
        }
        displayEndingMessage();
    }

    protected void changeLastUpdateTime(){
        lastUpdateTime = System.currentTimeMillis();
    }
    
    protected abstract void startProcess();
    protected abstract void messagePassing();
    protected abstract int getAllowedWaitTime();
    protected abstract void displayEndingMessage();
}

/**
 * Master Process That Follows Life Cycle From Calling Class.
 * Creates Friends Processes & Starts Their Life Cycle.
 * Provides Methods For Message Passing And Display Messages To Console To Friends Processes.
 * @author rajmistry
 */
class MasterThread extends Calling{
	private List<CallsToBeMade> callsToBeMadeList;
	private Map<String,Queue<Messages>> messageQueue = new HashMap<>();
    private Map<String,Queue<Messages>> replyQueue = new HashMap<>();
	
    public MasterThread(List<CallsToBeMade> callsToBeMadeList) {
		this.callsToBeMadeList = callsToBeMadeList;
	}

	@Override
	protected void startProcess() {
		System.out.println("** Calls to be made **");
        for(CallsToBeMade callsData : callsToBeMadeList){
            System.out.println(callsData.toString());
        }
        System.out.println("\n");
		createFriendsThread();
	}

	private void createFriendsThread() {
		List<Thread> friendsThreads = new ArrayList<>();
        for(CallsToBeMade callsData : callsToBeMadeList){
            Queue<Messages> queue = new LinkedBlockingDeque<Messages>();
            Queue<Messages> replyQueing = new LinkedBlockingDeque<Messages>();
            messageQueue.put(callsData.getSender(), queue);
            replyQueue.put(callsData.getSender(), replyQueing);
            FriendThread friendThread = new FriendThread(callsData, this);
            Thread thread = new Thread(friendThread, callsData.getSender() + "-Thread");
            friendsThreads.add(thread);
        }
        for(Thread thread : friendsThreads){
            thread.start();
        }
	}

	@Override
	protected void messagePassing() {
		// Nothing Will be performed in Master Process
		
	}

	@Override
	protected int getAllowedWaitTime() {
		//10 Seconds
		return 10000;
	}

	@Override
	protected void displayEndingMessage() {
		System.out.println("\nMaster has received no replies for 10 seconds, ending...");
	}

	
	//Methods For Message Handling
    public void sendMessage(Messages message) {
        messageQueue.get(message.getReceiver()).add(message);
        changeLastUpdateTime();
    }

    public void sendReply(Messages message) {
        replyQueue.get(message.getSender()).add(message);
        changeLastUpdateTime();
    }

    public Messages getFirstMessageFromQueue(String receiverName){
        return  messageQueue.get(receiverName).poll();
    }

    public Messages getFirstReplyFromQueue(String senderName){
        return replyQueue.get(senderName).poll();
    }

    public void notifyAction(String notification){
        System.out.println(notification);
        changeLastUpdateTime();
    }
}

/**
 * Friends Processes That Follows Life Cycle From Calling Class.
 * Performs Message Passing Using Methods Provided By Master.
 * @author rajmistry
 *
 */
class FriendThread extends Calling{
	private CallsToBeMade callsData;
    private MasterThread messagePassingActivity;
	
    public FriendThread(CallsToBeMade callsData, MasterThread masterThread) {
    	this.callsData = callsData;
		this.messagePassingActivity = masterThread;
	}

	@Override
	protected int getAllowedWaitTime() {
		//5 Seconds
		return 5000;
	}

	@Override
	protected void startProcess() {
		for(String receiver : callsData.getReceivers()){
            Messages message = new Messages(callsData.getSender(), receiver," ");
            messagePassingActivity.sendMessage(message);
        }	
	}

	@Override
	protected void messagePassing() {
		boolean notWorking = true;
        // Get First Message From Queue
        Messages message = messagePassingActivity.getFirstMessageFromQueue(callsData.getSender());
        if(message != null){
            notWorking = false;
			String time = ""+System.currentTimeMillis();
			message.setMessage(time);
            messagePassingActivity.sendReply(message);
            messagePassingActivity.notifyAction(message.getReceiver() +" received intro message from "+ message.getSender()+" ["+ message.getMessage() +"]");
        }

        //Get First Reply From Queue
        Messages reply = messagePassingActivity.getFirstReplyFromQueue(callsData.getSender());
        if(reply != null){
        	notWorking = false;
            messagePassingActivity.notifyAction(reply.getSender() +" received reply message from "+ reply.getReceiver()+" ["+ reply.getMessage() +"]");
        }
        if(!notWorking)
            changeLastUpdateTime();
	}

	@Override
	protected void displayEndingMessage() {
		System.out.println("\nProcess "+callsData.getSender()+" has received no calls for 5 seconds, ending...");
		
	}
}