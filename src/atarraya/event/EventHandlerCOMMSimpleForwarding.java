

package atarraya.event;

import atarraya.constants;
import atarraya.element.*;
import atarraya.atarraya_frame;

/**
 *
 * @author Pedro
 */


public class EventHandlerCOMMSimpleForwarding implements EventHandlerSW,constants{
    
    atarraya_frame father;
    
    int type = COMM_PROTOCOL;
    int TMType;
    int temp_address=0;
            
    /** Creates a new instance of EventHandlerA3 */
    public EventHandlerCOMMSimpleForwarding(atarraya_frame _father) {
        
        father = _father;    
        TMType = NO_TM;
    }
    
    public int getTMType(){
        return TMType;
    }
    
    public void setLabels(int _initial,int _active, int _inactive, int _sleeping){ }   
    
    public int getTreeID(){
        return father.getTreeID();
    }
    
    public int getSortingMode(){
        return (int)father.getVariable(SORTINGMODE);
    }
    
    public int getSimMode(){
        return (int)father.getVariable(SIMMODE);
    }    
    
    
    public int getbatchMode(){
        return (int)father.getVariable(BATCH_SIMULATION);
    }
    
 public node getNode(int id){
        return father.getNode(id);
    }
    
    public void pushEvent(event_sim e){
        father.pushEvent(e);
    }
    
    public void AddTreeLine(int s, int d, int t){
        if(getbatchMode()==1){}
            //father.AddTreeLine(s,d,t);
    }
    
    public void RemoveTreeLine(int s, int d, int t){
        if(getbatchMode()==1){}
            //father.RemoveTreeLine(s,d,t);
    }
    
    public void frame_repaint(){
        if(getbatchMode()==1)
            father.frame_repaint();
        
    }
 
    
    public void InvalidateAllEventsFromIDFromTimeTOfCodeC(int id, double t, int c, int tree){
        father.InvalidateAllEventsFromIDFromTimeTOfCodeC(id,t,c, tree);
    }
    
    public void InvalidateAllEventsFromIDFromTimeT(int id, double t, int tree){
        father.InvalidateAllEventsFromIDFromTimeT(id,t,tree);
    }
    
    public void InvalidateAllEventsFromIDFromTimeTOfTypeTy(int id, double t, int Ty, int tree){
        father.InvalidateAllEventsFromIDFromTimeTOfTypeTy(id,t,Ty,tree);
    }
 
    
 public void broadcast(double final_time, double current_time, int _sender, int _destination, int _code){
        father.broadcast(final_time, current_time, _sender, _destination, _code,"",-1,type);
    }

 public void broadcast(double final_time, double current_time, int _sender, int _destination, int _code, int _tree){
        father.broadcast(final_time, current_time, _sender, _destination, _code,"",_tree,type);
    }
 
 public void broadcast(double final_time, double current_time, int _sender, int _destination, int _code, String msg){
        father.broadcast(final_time, current_time, _sender, _destination, _code,msg,type);
    }
    
 public void broadcast(double final_time, double current_time, int _sender, int _destination, int _code, String msg, int _tree){
        father.broadcast(final_time, current_time,-1,-1, _sender, _destination, _code,msg,_tree,type);
    }
 
 public void broadcast(double final_time, double current_time, int _source, int _final_destination,int _sender, int _destination, int _code, String msg, int _tree){
        father.broadcast(final_time, current_time,_source,_final_destination, _sender, _destination, _code,msg,_tree,type);
    }
 
 public double getRandom(double max_val){
        return java.lang.Math.random()*max_val;
    }
 
 public void showMessage(String m){
        father.showMessage(m);
    }

 public void showEventList(String m){
    father.showEventList(m);
 } 
 
  public void init_node(int _treeID, int _id){
    getNode(_id).setRoutingActive(_treeID, false);
  }
  
    public void init_nodes(int _treeID){
        int i;
        
        for(i=0;i<father.getVariable(NUMPOINTS);i++){
            getNode(i).setRoutingActive(_treeID, false);
            getNode(i).setAutoupdate(_treeID, false);
        }
        
    }
    
    public void initial_event(int _id, int _treeID){

        System.out.println("Entered Event Handling");
        //Obtain the time from the simulator
        double _clock = father.getVariable(CLOCK);
        
        //stop all future event from the sensor-data protocol
        InvalidateAllEventsFromIDFromTimeTOfTypeTy(_id, _clock+DELTA_TIME, type,_treeID);
        
        //Program a new Query Sensor on the tree_ID received
        pushEvent(new event_sim(_clock+PROCESSING_DELAY, _clock, _id, _id, UPDATE_ROUTING_TABLE,"",_treeID,type));
        
    }
    
    public boolean CheckIfDesiredFinalState(int s){
        return false;
    }
    
    public boolean CheckIfDesiredSleepingState(int s){
        return false;
    }
    
    public boolean verifIfNodeInCDS(int _id, int _treeID){
        return false;
    }
    
    public int GetMessageSize(int code){
        
        if(code==DATA)
            return SIZE_LONG_PACKETS;
            
        return SIZE_SHORT_PACKETS;
    }
    
    public void HandleEvent(event_sim e){
    
        int code = e.getCode();
        int sender = e.getSender();
        int source = e.getSource();
        int final_destination = e.getFinalDestination();
        int receiver = e.getReceiver();
        int destination = e.getDestination();
        double temp_clock = e.getTime();
        String temp_data = e.getData();
        int temp_tree = e.getTree();
        int temp_data_int=0;
        int temp_data_int2=0;
        int temp_data_int3=0;
        String[] temp_data_array = new String[50];
        String[] temp_data_array2;
        
        int numneighb,i,j,temp_ID;
        int index,tam;
        int min_child;
        register temp_reg;
        int tempdist;
        int temp_level=0;
        int temptam=0;
        int tempmax,nmax;
        int temp=0;
        int temp2=0;
        double temp_time;
        double temp_val_double1=0.0;
        double metric=0;
        double y;
        boolean sw=true;
        boolean sw2;
        candidate temp_cand;
        
        if(getNode(receiver).isAlive()){
            
        switch(code){
            
            case INIT_NODE:
                init_node(temp_tree, receiver);
                break;
            
            case INIT_EVENT:
                initial_event(sender,temp_tree);
                break;
            
            case RESET_COMM_PROTOCOL:
                //stop all future event from the sensor-data protocol
                InvalidateAllEventsFromIDFromTimeTOfTypeTy(receiver, temp_clock+DELTA_TIME, type,temp_tree);
                break;
                
            case UPDATE_ROUTING_TABLE:
                
                temp = getNode(receiver).getActiveTree();
                
                //If the node is active on its active tree
                if(getNode(receiver).isActive(temp)){
                    
                    // Get the number of gateways to be included on the Routing Table
                    temp2 = getNode(receiver).getNumGateways(temp);
                    
                    //Set as default gateway of the Routing Table the sink address in memory
                    //getNode(receiver).setDefaultGateway(temp, getNode(receiver).getSinkAddress(temp_tree));
                    
                    for(i=0;i<temp2;i++){
                    
                        //Get ID of  Gateway i
                        temp_data_int=getNode(receiver).getGatewayID(temp, i);
                        //Get Address of  Gateway i
                        temp_data_int2=getNode(receiver).getGatewayAddress(temp, i);
                        //Get the candidate index number if this node is a candidate. If not we will get a -1 in return.
                        temp_data_int3 = getNode(receiver).getCandidateIndexFromID(temp, temp_data_int);
                        
                        //If the node is indeed a candidate(child), add its metric
                        if(temp_data_int3 != -1){
                            //Get the gatweay  metric from the candidate list
                            temp_val_double1 = getNode(receiver).getCandidate(temp, temp_data_int3).getMetric();
                            //Add the gateway to the routing table, including the metric used to reach them
                            getNode(receiver).getRoutingTable(temp).addRoutingRegister(new  routing_table_register(temp_data_int, temp_data_int2,temp_val_double1));
                        }else{
                            //Add the gateway to the routing table... we have no information about the metric to these other nodes.
                            getNode(receiver).getRoutingTable(temp).addRoutingRegister(new  routing_table_register(temp_data_int, temp_data_int2));
                        }
                            
                        
                    }
                    
                    //Set that the node now uses the routing table to determine the next step on the packets.
                    getNode(receiver).setRoutingActive(temp,true);
                    
                    //By default all nodes have Autoupdate in false in this protocol. The table changes only when the topology changes.
                    if(getNode(receiver).isAutoupdate(temp)){
                        //Program the next query of the sensor
                        pushEvent(new event_sim(temp_clock+NEXT_UPDATE_ROUTING_TABLE_TIMEOUT, temp_clock, sender, sender, UPDATE_ROUTING_TABLE, "",temp,type));
                    }
                }
                
                break;
            
                
            default:
                break;
        }
        }
        
    }
}
