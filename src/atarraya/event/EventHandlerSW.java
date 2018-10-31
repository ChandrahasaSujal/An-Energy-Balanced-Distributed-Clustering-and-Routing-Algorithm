

package atarraya.event;

import atarraya.constants;

/**
 *
 * @author Pedro
 */
public interface EventHandlerSW {
  
    public void HandleEvent(event_sim e);
    public void initial_event(int _id, int _treeID);
    public void init_nodes(int _treeID);
    public boolean CheckIfDesiredFinalState(int s);
    public boolean verifIfNodeInCDS(int _id, int _treeID);
    public boolean CheckIfDesiredSleepingState(int s);
    public int GetMessageSize(int code);
    public void setLabels(int initial_, int active_, int inactive_, int sleeping_);
    
    public int getTMType();
    
}
