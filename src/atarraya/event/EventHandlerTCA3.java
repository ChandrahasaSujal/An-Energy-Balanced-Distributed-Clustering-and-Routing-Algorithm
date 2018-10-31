

package atarraya.event;

import atarraya.constants;
import atarraya.element.candidate;
import atarraya.element.node;
import atarraya.element.register;
import atarraya.atarraya_frame;

/**
 *
 * @author Pedro
 */


public class EventHandlerTCA3 implements EventHandlerSW,constants{
    
    atarraya_frame father;
    
    int type = TC_PROTOCOL;
    boolean TM_Selected;
    boolean SD_Selected;
    int TMType;
    boolean COMM_Selected;
    int temp_address;
    int temp_selectedTMprotocol;
    
    int temp_num_structures;
    
    int active;
    int inactive;
    int initial;
    int sleeping;
    
    double candidate_grouping_step;
            
    /** Creates a new instance of EventHandlerA3 */
    public EventHandlerTCA3(atarraya_frame _father) {
        
        father = _father;    
        TM_Selected=false;
        SD_Selected=false;
        COMM_Selected=false;
        TMType = NO_TM;
        
        if(father.getVariable(SELECTED_TM_PROTOCOL)!=TM_PROTOCOL_NO_SELECTED)
            TM_Selected=true;
        
        if(father.getVariable(SELECTED_SD_PROTOCOL)!=SENSOR_PROTOCOL_NO_SELECTED)
            SD_Selected=true;
        
        if(father.getVariable(SELECTED_COMM_PROTOCOL)!=COMM_PROTOCOL_NO_SELECTED)
            COMM_Selected=true;
        
        temp_num_structures = (int)father.getVariable(NUMINFRASTRUCTURES);
        
        candidate_grouping_step = father.getVariable(CANDIDATE_GROUP_STEP);
        
        temp_selectedTMprotocol = (int)father.getVariable(SELECTED_TM_PROTOCOL);
        
        temp_address = 0;
    
        active = 0;
        inactive = 0;
        initial = 0;
        sleeping = 0;
        
    }
    
    public int getTMType(){
        return TMType;
    }
    
    public void setLabels(int _initial,int _active, int _inactive, int _sleeping){
        active=_active;
        inactive=_inactive;
        initial=_initial;
        sleeping = _sleeping;
    }
       
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
        father.InvalidateAllEventsFromIDFromTimeTOfCodeC(id,t,c,tree);
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

  
    public void init_nodes(int _treeID){
        int i, tam=0;
        tam = (int)father.getVariable(NUMPOINTS);
        double _clock = father.getVariable(CLOCK);
        
        for(i=0;i<tam;i++){
            getNode(i).setState(initial,_treeID);
            getNode(i).SetInfrastructureStarted(_treeID,true);
            getNode(i).defineLabels(initial,active, inactive, sleeping);
            
            if(TM_Selected){
                //stop all future event from the TM protocol
                pushEvent(new event_sim(_clock+getRandom(PROCESSING_DELAY), _clock, i, i, RESET_TM_PROTOCOL, "",_treeID,TM_PROTOCOL));
            }

            if(SD_Selected){
                //stop all future event from the sensor-data protocol
                pushEvent(new event_sim(_clock+getRandom(PROCESSING_DELAY), _clock, i, i, RESET_QUERY_SENSOR, "",_treeID,SENSOR_PROTOCOL));
            }
        
            
        }
        
    }
    
    public void init_node(int _treeID, int id_node){
        getNode(id_node).setState(initial,_treeID);
    }
    
    public void initial_event(int _id, int _treeID){
        
        getNode(_id).setLevel(0,_treeID);
        getNode(_id).addGateway(_treeID,_id,_id);
        getNode(_id).setSinkAddress(_treeID, _id);
        getNode(_id).setState(S_WAITING_ACTIVE,_treeID);
        
        double _clock = father.getVariable(CLOCK);
        pushEvent(new event_sim(_clock+getRandom(MAX_TX_DELAY_RANDOM), _clock, _id, _id, SEND_HELLO,"0@"+_id+"@"+_id,_treeID,type));
        
        if(TM_Selected){
            //stop all future event from the TM protocol
            pushEvent(new event_sim(_clock+getRandom(PROCESSING_DELAY), _clock, _id, _id, RESET_TM_PROTOCOL, "",_treeID,TM_PROTOCOL));
        }

        if(SD_Selected){
            //stop all future event from the sensor-data protocol
            pushEvent(new event_sim(_clock+getRandom(PROCESSING_DELAY), _clock, _id, _id, RESET_QUERY_SENSOR, "",_treeID,SENSOR_PROTOCOL));
        }
        
    }
    
    public boolean CheckIfDesiredFinalState(int s){
        if(s==active || s==inactive)
            return true;
        
        return false;
    }

    
    public boolean CheckIfDesiredSleepingState(int s){
        if(s==sleeping)
            return true;
        
        return false;
    }
    
    public boolean verifIfNodeInCDS(int _id, int _treeID){
    
        if(getNode(_id).isActive(_treeID) || getNode(_id).isSink(_treeID) || _treeID==-1)
            return true;
        
        return false;
    }
    
    public int GetMessageSize(int code){
        
        if(code==SON_RECOGNITION || code==DATA)
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

        double temp_val_double=0.0;
        double temp_time;
        double temp_val_double1=0.0;
        double temp_val_double2=0.0;
        double temp_val_double3=0.0;
        double metric=0;
        double y;
        boolean sw=true;
        boolean sw2;
        candidate temp_cand;
        double primary_val=0;
        double secondary_val=0;
        
        switch(code){
        
            case INIT_NODE:
                init_node(temp_tree, receiver);
                break;
            
            case INIT_EVENT:
                initial_event(receiver,temp_tree);
                break;
            
            /*
             * This event is the when a node will start sending Hello message to its neighbors
             */
            case SEND_HELLO:
                
                

                getNode(sender).setEnergyState(STATE_READY);

                
                //The node will clean the candidates from the neighbor's hellos messages
                //getNode(sender).cleanCandidates();
                getNode(sender).cleanCandidates(temp_tree);

                getNode(sender).setState(S_WAITING_ACTIVE,temp_tree);

                //Sending Hello message to all neighbors
                broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, sender, -1, HELLO, temp_data,temp_tree);

                //Final timeout for evaluating candidates and adopt children nodes
                pushEvent(new event_sim(temp_clock+TIMEOUT_DELAY, temp_clock, sender, sender, PARENT_RECOG_TIME_OUT, "",temp_tree,type));

                
                break;
            
            /*
             * This event is the when a node received a Hello message from its parent
             */
            case HELLO:
                
                if(getNode(receiver).getState(temp_tree) == initial){
                
                    //Decompress the data in the message
                    temp_data_array = temp_data.split("@");

                    if(temp_data_array.length<3)
                        temp_data_int3=0;

                    try{                                                    
                            temp_data_int = Integer.parseInt(temp_data_array[0]);  //The level of the parent is comming in the data
                            temp_data_int2 = Integer.parseInt(temp_data_array[1]); //The sink address of this tree
                            temp_data_int3 = Integer.parseInt(temp_data_array[2]); //The address of this father
                        }catch(Exception ex){ex.printStackTrace();}


                    if(getNode(receiver).getGatewayID(temp_tree,0) == -1){
                        
                        getNode(receiver).setState(S_CHILD,temp_tree);
                        getNode(receiver).addGateway(temp_tree, sender, temp_data_int3); 
                        getNode(receiver).setDefaultGateway(temp_tree, sender);
                        getNode(receiver).setSinkAddress(temp_tree, temp_data_int2);
                        getNode(receiver).setLevel(temp_data_int+1,temp_tree);        // Define the level

                        // Schedules the Broadcast of the Parent Recognition message
                        pushEvent(new event_sim(temp_clock+getRandom(PROCESSING_DELAY), temp_clock, sender, receiver, PARENT_RECOGNITION_EVENT, "",temp_tree,type));

                       
                    }//If node has no parent in this tree

                }
                break;
            
                
             /*
             * This event is the when the timeout for listening to other neighbors finishes, 
             * and the node can send its own reply to the parent.
             */
            case PARENT_RECOGNITION_EVENT:

                if(getNode(receiver).isNeighbor(sender)){
                    //Get the Radius
                    temp_val_double = getNode(receiver).getInfrastructure(temp_tree).getComm_radius();

                    temp_val_double1 = getNode(receiver).getNeighborDistance(getNode(receiver).getNeighborIndex(sender));

                    temp_val_double2 = temp_val_double1/temp_val_double;

                    temp_val_double3 = java.lang.Math.max(getNode(receiver).getEnergyRatio(),0);

                    temp_data_int2 = getNode(receiver).getNumTrees();

                    if(temp_data_int2>0)
                        temp_val_double3 = temp_val_double3*java.lang.Math.pow(0.5, temp_data_int2);

                    //Metric 1 = Distance, Metric2 = Energy
                    temp_data = ""+temp_val_double2+"@"+temp_val_double3;


                    //Broadcast the message
                    broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver, sender, PARENT_RECOGNITION, temp_data,temp_tree);
                    }
                break;
                
            /*
             * This event is the when a node received a reply from a Hello message from its children or neighbors.
             */    
            case PARENT_RECOGNITION:
               
                //If the parent is the one that receives this message
                if(receiver == destination && getNode(receiver).getState(temp_tree) == S_WAITING_ACTIVE){
                    
                    //Decompress the data in the message
                    temp_data_array = temp_data.split("@");

                    try{                                                    
                            temp_val_double1 = Double.parseDouble(temp_data_array[0]); //Energy metric
                            temp_val_double2 = Double.parseDouble(temp_data_array[1]); //Distance metric
                    }catch(Exception ex){ex.printStackTrace();}
                    

                    primary_val = temp_val_double1;
                    secondary_val = temp_val_double2;
                        
                    temp_data_int = (int)java.lang.Math.round(primary_val/candidate_grouping_step);
                    if(primary_val==1)
                        temp_data_int--;
                    
                    temp_val_double1 = father.getVariable(WEIGHT_METRIC1);
                    temp_val_double2 = father.getVariable(WEIGHT_METRIC2);
                    
                    //Include a new candidate from which the node heard a hello message                    
                    getNode(receiver).addCandidate(temp_tree, new candidate(sender,0,getSortingMode(),temp_data_int,primary_val,secondary_val,temp_val_double1,temp_val_double2));
                    
                }
                
                break;
            
            
            /*
             * This event is the when a parent node's timeout to select children finished.
             */    
            case PARENT_RECOG_TIME_OUT:    
                i=0;
                sw=true;
                temp=0;
                temp_data="";
                        
                //Number of candidates
                temp2=getNode(sender).getNumCandidates(temp_tree);

                if(temp2>0 || getNode(sender).isSink(temp_tree)){

                    getNode(sender).setState(S_ACTIVE,temp_tree);
                    
                    //Changing the state of the node to be parent
                    //getNode(sender).setState(S_PARENT,temp_tree);
                    getNode(sender).setParentState(temp_tree, true);
                    getNode(sender).setEnergyState(temp_tree, STATE_READY);
                    
                    //Initiate the TM protocol only on the active nodes of the topology, except on the local
                    if(TM_Selected && (father.getTMType() != TM_ENERGY || getNode(sender).isSink(temp_tree))){
                        pushEvent(new event_sim(temp_clock+DELTA_TIME, temp_clock, receiver, receiver, INIT_EVENT, "",temp_tree,TM_PROTOCOL));
                    }
                            
                    //Initiate the sensor querying protocol only on the active nodes of the topology!!
                    if(SD_Selected  && getNode(receiver).getActiveTree() == temp_tree){
                        pushEvent(new event_sim(temp_clock+DELTA_TIME, temp_clock, receiver, receiver, INIT_EVENT, "",temp_tree,SENSOR_PROTOCOL));
                    }

                    //Initiate the COMM  protocol only on the active nodes of the topology!!
                    if(COMM_Selected){
                        pushEvent(new event_sim(temp_clock+DELTA_TIME, temp_clock, receiver, receiver, INIT_EVENT, "",temp_tree,COMM_PROTOCOL));
                    }
                    
                    
                    //Sorting the cadidates, based on their metrics
                    getNode(sender).SortCandidates(temp_tree);

                    for(i=0;i<temp2;i++){
                        
                        //Get the i th candidate
                        temp_cand = getNode(sender).getCandidate(temp_tree, i);
                        
                        
                        getNode(sender).getCandidate(temp_tree,i).setAddress(temp_cand.getID());
                        
                        
                        //Adding the tree link with the new child
                        getNode(sender).addChild(temp_tree, temp_cand);
                        AddTreeLine(sender,temp_cand.getID(),temp_tree);

                        temp_data = temp_data + temp_cand.getID()+"-";
                    }

                    //Restrict the number of candidates that will start the Hello messages broadcasting
                    //temp = temp2*max_children_I;
                    temp = temp2;

                    //Building the packet to sent to the rest of candidates
                    temp_data = ""+temp+"@"+temp_data;

                    //Sending the packet
                    broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, sender, -1, SON_RECOGNITION, temp_data,temp_tree);                    
                 }else{
                    
                    getNode(sender).setState(S_SLEEP,temp_tree);

                    //Set the radio on, set the sensor off and processor on sleeping mode
                    //getNode(sender).setEnergyState(STATE_JUST_RADIO);

                    if(getNode(sender).getActiveTree() == temp_tree && temp_num_structures>1)
                        pushEvent(new event_sim(temp_clock+RADIO_OFF_DELAY_PER_TREE+getRandom(PROCESSING_DELAY), temp_clock, sender, sender, DELAYED_RADIO_OFF, "",temp_tree,type));
                    else
                        getNode(sender).setEnergyState(temp_tree, STATE_SLEEP);

                    temp = getNode(sender).getGatewayID(temp_tree, 0);
                    broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, sender, temp, GOING_SLEEP, temp_data,temp_tree);                    
                    //getNode(sender).setRadioOn(temp_tree, false);
                    //pushEvent(new event_sim(temp_clock+(RADIO_OFF_DELAY_PER_TREE*(temp_num_structures-1))+getRandom(PROCESSING_DELAY), temp_clock, sender, sender, DELAYED_RADIO_OFF, "",temp_tree,type));
                 }
               
                break;
        
                
                
           case SON_RECOGNITION:
                       
                       if(sender==getNode(receiver).getDefaultGateway(temp_tree) && getNode(receiver).getState(temp_tree) == S_CHILD){
                           
                           getNode(receiver).setState(S_CANDIDATE,temp_tree);
                           
                           sw = false;
                                sw2 = false;
                                i=0;
                                temp_data_array = temp_data.split("@");
                                try{
                                    temp = Integer.parseInt(temp_data_array[0]);        //temp = Number of brothers that this node has
                                }catch(Exception ex){ex.printStackTrace();}

                                if(temp>0){
                                    temp_data = temp_data_array[1];
                                    temp_data_array = temp_data.split("-");                     // temp_data_array = IDs of the brothers

                                    i=0;
                                    temp2 = -1;

                                    while(i<temp && temp2!=receiver){
                                        try{
                                            temp2 = Integer.parseInt(temp_data_array[i]);   //temp2 = ID from the list
                                        }catch(Exception ex){ex.printStackTrace();}
                                        i++;
                                    }

                                    getNode(receiver).setNumBrothers(i);

                                    getNode(sender).setAddress(sender);
                                }
                                pushEvent(new event_sim(temp_clock+(MAX_TX_DELAY_RANDOM*i), temp_clock, receiver, receiver, CHECK_4_BROTHER, "",temp_tree,type));
                                if(getSimMode()==MAX_COVERAGE){
                                    //If it is not a selected, it will try to see if there are any nodes not covered in its area
                                    pushEvent(new event_sim(temp_clock+SECOND_PARENTHOOD_DELAY+(MAX_TX_DELAY_RANDOM*i), temp_clock, receiver, receiver, CHECK_4_CHILDREN, "",temp_tree,type));
                                }

                            }
                       
               break;
                
                
            case CHECK_4_BROTHER:
               
                if(getNode(sender).getState(temp_tree)==S_CANDIDATE && getNode(sender).getState(temp_tree)!=S_SLEEP){
                    temp_time = temp_clock+getRandom(MAX_TX_DELAY_RANDOM);
                    broadcast(temp_time, temp_clock, sender, -1, BROTHER_BLOCKAGE_MESSAGE, "" + getNode(sender).getLevel(temp_tree),temp_tree);                    
                    temp_time+=getRandom(PROCESSING_DELAY);
                    getNode(sender).setState(S_ACTIVE_CANDIDATE,temp_tree);
                    pushEvent(new event_sim(temp_time, temp_clock, sender, sender, SEND_HELLO, ""+getNode(receiver).getLevel(temp_tree)+"@"+getNode(receiver).getSinkAddress(temp_tree)+"@"+getNode(receiver).getAddress(),temp_tree,type));
                    
                }
                
                break;
                
            case GOING_SLEEP:
                if(receiver == destination && getNode(receiver).getState(temp_tree)==S_ACTIVE ){
                    if(getNode(receiver).isChildren(temp_tree, sender)){
                       getNode(receiver).RemoveGatewayFromID(temp_tree,sender);
                    }
                }
                break;
                
           
            case BROTHER_BLOCKAGE_MESSAGE:
                try{
                    temp = Integer.parseInt(temp_data);
                }catch(Exception ex){ex.printStackTrace();}
                
              if(!getNode(receiver).isActive(temp_tree) && getNode(receiver).getLevel(temp_tree) == temp && getNode(receiver).getState(temp_tree)==S_CANDIDATE){
                    getNode(receiver).setState(S_SLEEP_CANDIDATE, temp_tree);
                    getNode(receiver).addTempNumTree(-1);

                    //Set the radio on, set the sensor off and processor on sleeping mode
                    getNode(receiver).setEnergyState(temp_tree, STATE_JUST_RADIO);

                    //getNode(receiver).setRadioOn(temp_tree, false);
                    //pushEvent(new event_sim(temp_clock+(RADIO_OFF_DELAY_PER_TREE*(temp_num_structures-1))+getRandom(PROCESSING_DELAY), temp_clock, receiver, receiver, DELAYED_RADIO_OFF, "",temp_tree,type));
              }
                
                if(getNode(receiver).isChildren(temp_tree, sender) && getNode(receiver).getState(temp_tree)==S_ACTIVE){
                   getNode(receiver).addGateway(temp_tree, sender, sender);
                }
               
                break;
                
            case CHECK_4_CHILDREN:
                
                if(getNode(sender).getState(temp_tree) == S_SLEEP_CANDIDATE){
                    pushEvent(new event_sim(temp_clock+getRandom(PROCESSING_DELAY), temp_clock, sender, sender, SEND_HELLO, ""+getNode(sender).getLevel(temp_tree)+"@"+getNode(sender).getSinkAddress(temp_tree)+"@"+getNode(sender).getAddress(),temp_tree,type));
                }
                
               break;
               
            case DELAYED_RADIO_OFF:
                
                //getNode(sender).setRadioOn(temp_tree, false);

                temp_data_int=0;
                temp_data_int2=0;

                for(i=0;i<MAX_TREES;i++){
                    if(getNode(sender).IsInfrastructureStarted(i)){
                        temp_data_int++;
                        if(CheckIfDesiredFinalState(getNode(sender).getState(i)))
                            temp_data_int2++;
                    }
                }

                if(temp_data_int  == temp_data_int2)
                    getNode(sender).setEnergyState(temp_tree, STATE_SLEEP);
                else
                    pushEvent(new event_sim(temp_clock+RADIO_OFF_DELAY_PER_TREE+getRandom(PROCESSING_DELAY), temp_clock, sender, sender, DELAYED_RADIO_OFF, "",temp_tree,type));

                break;
                
                
            default:
                break;
        }
        
    }
}
