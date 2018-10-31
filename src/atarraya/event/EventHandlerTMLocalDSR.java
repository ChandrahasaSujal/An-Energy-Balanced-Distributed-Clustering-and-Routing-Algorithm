/*
 * EventHandlerA3.java
 *
 * Created on February 8, 2007, 2:48 PM
 *
This file is part of Atarraya.

Atarraya is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Atarraya is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Atarraya.  If not, see <http://www.gnu.org/licenses/>.

 Copyright 2009 Pedro M. Wightman
 */

package atarraya.event;

import atarraya.constants;
import atarraya.element.*;
import atarraya.atarraya_frame;

/**
 *
 * @author Pedro
 */


public class EventHandlerTMLocalDSR implements EventHandlerSW,constants{
    
    atarraya_frame father;
    
    int type = TM_PROTOCOL;
    boolean TM_Selected;
    boolean SD_Selected;
    boolean COMM_Selected;
    int TMType;
    int temp_address;
    
    int temp_num_structures;
    
    int active;
    int inactive;
    int initial;
    int sleeping;
    
    double candidate_grouping_step;
            
    /** Creates a new instance of EventHandlerA3 */
    public EventHandlerTMLocalDSR(atarraya_frame _father) {
        
        father = _father;    
        TM_Selected=false;
        SD_Selected=false;
        COMM_Selected=false;
        TMType = TM_ENERGY;
        
        if(father.getVariable(SELECTED_TM_PROTOCOL)!=TM_PROTOCOL_NO_SELECTED)
            TM_Selected=true;
        
        if(father.getVariable(SELECTED_SD_PROTOCOL)!=SENSOR_PROTOCOL_NO_SELECTED)
            SD_Selected=true;
        
        if(father.getVariable(SELECTED_COMM_PROTOCOL)!=COMM_PROTOCOL_NO_SELECTED)
            COMM_Selected=true;
        
        temp_num_structures = (int)father.getVariable(NUMINFRASTRUCTURES);
        
        candidate_grouping_step = father.getVariable(CANDIDATE_GROUP_STEP);
        
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
    
    public void InvalidateAllEventsFromIDFromTimeTOfTypeTyOfCodeC(int id, double t, int Ty, int c, int tree){
        father.InvalidateAllEventsFromIDFromTimeTOfTypeTyOfCodeC(id,t,Ty,c,tree);
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
        int i, tam=0,temp=0;
        tam = (int)father.getVariable(NUMPOINTS);
        double _clock = father.getVariable(CLOCK);
        
        for(i=0;i<tam;i++){
            //getNode(i).setTM_State(TM_INACTIVE);
            //getNode(i).getTM_temp_routing().clearRoutingTable();
            InvalidateAllEventsFromIDFromTimeTOfTypeTy(i, _clock+DELTA_TIME, type,_treeID);
        }
        
    }
    
    public void init_node(int _treeID, int id_node){
        double _clock = father.getVariable(CLOCK);
        //getNode(id_node).setTM_State(TM_INACTIVE);
        //getNode(id_node).getTM_temp_routing().clearRoutingTable();
        InvalidateAllEventsFromIDFromTimeTOfTypeTy(id_node, _clock+DELTA_TIME, type,_treeID);
    }
    
    public void initial_event(int _id, int _treeID){
        
        double _clock = father.getVariable(CLOCK);
        
        InvalidateAllEventsFromIDFromTimeTOfTypeTy(_id, _clock+DELTA_TIME, type,_treeID);
        
        //Program the neighborhood verification only for the sink
        pushEvent(new event_sim(_clock+NEXT_HELLO_TIMEOUT, _clock, _id, _id, SEND_HELLO_MESSAGE_TM, "",_treeID,type));
        
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
        String temp_data2 = "";
        int temp_tree = e.getTree();
        int temp_data_int=0;
        int temp_data_int2=0;
        int temp_data_int3=0;
        String[] temp_data_array = new String[50];
        String[] temp_data_array2 = new String[10];
        
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
        double temp_val_double=0.0;
        double temp_val_double1=0.0;
        double temp_val_double2=0.0;
        double temp_val_double3=0.0;
        double temp_val_double4=0.0;
        double metric=0;
        double y;
        boolean sw=true;
        boolean sw2=true;
        boolean sw3=true;
        double primary_val=0;
        double secondary_val=0;
        boolean sw4 = true;
        candidate temp_cand;

        if(receiver != -1)
            sw4 = getNode(receiver).isAlive();
                else
            sw4=false;

        if(sw4){


        switch(code){
        
            case INIT_NODE:
                init_node(temp_tree, receiver);
                break;
            
            case INIT_EVENT:
                if(getNode(sender).isSink())
                    initial_event(receiver,temp_tree);
                break;
            
            case RESET_TM_PROTOCOL:
                init_node(temp_tree, receiver);
                break;
                
                
            case SEND_HELLO_MESSAGE_TM:
                //Sends the Hello message to all its neighbors
                broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, source, final_destination, sender, -1, HELLO_MESSAGE_TM, "",temp_tree);

                getNode(sender).setNeighbCountTM(0);
                
                //Schedule the process of counting the neighbors that answered
                pushEvent(new event_sim(temp_clock+WAIT_HELLO_REPLY_TIMEOUT, temp_clock, sender, sender, HELLO_MESSAGE_WAIT_TIMEOUT_TM, "",temp_tree,type));
                break;
                
            case HELLO_MESSAGE_TM:
                if(getNode(receiver).isActive(temp_tree)){
                    //Sends the Reply to Hello message to the sender if the node is active
                    broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, source, final_destination, receiver, sender, REPLY_MESSAGE_TM, "",temp_tree);
                }
                break;
                
            case REPLY_MESSAGE_TM:
                if(receiver == destination){
                    getNode(receiver).add2NeighbCountTM();
                }
                break;
                
            case HELLO_MESSAGE_WAIT_TIMEOUT_TM:
                if(getNode(sender).getNeighbCountTM()==0)
                    father.simulation_end();
                else
                    //Schedule the process of counting the neighbors that answered
                    pushEvent(new event_sim(temp_clock+NEXT_HELLO_TIMEOUT, temp_clock, sender, sender, SEND_HELLO_MESSAGE_TM, "",temp_tree,type));
                break;
                
            default:
                break;
                
                
                
             /*
             * This event is when the node that is failing realizes that, and sends the reactivation message to its neighbors.
             */
            case SEND_REACTIVATE_MESSAGE:
                
                //if(getNode(receiver).getTM_State() == TM_INACTIVE){
                
                //If the node have not started its own process of replacing
                if(!getNode(receiver).isValidTMStructConfIndex(sender)){

                    father.addTMInvocation();
                    getNode(receiver).addTmConfStructure(sender);

                    getNode(receiver).setRoutingActive(temp_tree, false);

                    //Get the list of all children of the node
                    temp_data = getNode(sender).getLevel(temp_tree)+"@"+getNode(sender).getInfrastructure(temp_tree).getCandidateIDList();


                    //Sending Hello message to all neighbors
                    broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, sender, -1, sender, -1, REACTIVATE_MESSAGE_I, temp_data,temp_tree);

                    //Turn off the radio of the sender node, whose energy is below the threshold
                    //getNode(sender).setRadioOn(temp_tree, false);

                    //Set its TM_State as inactive
                    getNode(sender).setTM_State(sender, TM_DEAD);

                    pushEvent(new event_sim(temp_clock+TIMEOUT_BACK_TO_NORMAL, temp_clock, sender, -1, sender, sender, sender, TIMEOUT_BACK_TO_NORMAL_EVENT, "",temp_tree,type));

                    getNode(receiver).setRoutingActive(temp_tree, true);

                    //Set a Timeout for sending the Route Request Message
                    //pushEvent(new event_sim(temp_clock+TIMEOUT_GO_DEAD, temp_clock, receiver, receiver, TIMEOUT_GO_DEAD_EVENT, "",temp_tree,type));

                }else{
                /*
                 * This statement would be eliminated if a node can manage several tm replacement processes!!!!
                 * 
                 * 
                    if(getNode(receiver).getTM_State(sender) != TM_DEAD){
                        //Set a Timeout for trying again in case it is participating in a TM Local process already
                        pushEvent(new event_sim(temp_clock+10.0, temp_clock, receiver, receiver, SEND_REACTIVATE_MESSAGE, "",temp_tree,type));
                    }
                */
                }
                break;
                
                
            case TIMEOUT_GO_DEAD_EVENT:
                
                getNode(sender).setDead();
                
                break;
                
            case REACTIVATE_MESSAGE_I:
                
                //if(getNode(receiver).isGateway(temp_tree, sender)){
                
                    getNode(receiver).setRoutingActive(temp_tree, false);

                    try{
                        temp_data_array = temp_data.split("@");
                        temp_level = Integer.parseInt(temp_data_array[0]);
                        temp=0;

                        temp_data_array2 = temp_data_array[1].split("_");
                        temp = Integer.parseInt(temp_data_array2[0]);

                        i=1;
                        sw=true;
                        while(i<=temp && sw){
                            if(Integer.parseInt(temp_data_array2[i]) == receiver)
                                sw=false;

                            i++;
                        }

                    }catch(Exception ex){ex.printStackTrace();}

                    //Create the TMConfStructure for this TM Local replacement process
                    getNode(receiver).addTmConfStructure(source);
                    
                    // If the receiver node is an active child of the failling node
                    if(!sw){

                        if(getNode(receiver).isSleeping(temp_tree)){

                            //Turn the radio on
                            getNode(receiver).setRadioOn(temp_tree, true);

                        }

                        //Send Reactivation Message III to all its neighbors
                        broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock,source, -1, receiver, -1, REACTIVATE_MESSAGE_III, "",temp_tree);
                        

                        //Set the current TM_State of the node in SEMI_ACTIVE
                        getNode(receiver).setTM_State(source, TM_SEMI_ACTIVE);

                        //Set a Timeout for going back to normal
                        //pushEvent(new event_sim(temp_clock+TIMEOUT_BACK_TO_NORMAL, temp_clock, receiver, receiver, TIMEOUT_BACK_TO_NORMAL_EVENT, "",temp_tree,type));


                        //Include this node on its own TM Routing table. I do not know if it is necessary, so it is in comment right now.
                        getNode(receiver).getTM_temp_routing(source).addRoutingRegister(new routing_table_register(receiver, receiver, 0, Integer.MAX_VALUE));


                    }else 

                    // If the receiver node is active and the default gateway or other active node in range of the sender node.
                    if(getNode(receiver).isActive(temp_tree) && sw && getNode(receiver).getCandidateIndexFromID(temp_tree, sender)!=-1){

                        //Set the state of the node in TM_GRANDPA, so it is at least 2 hops away for the orphan nodes
                        getNode(receiver).setTM_State(source, TM_GRANDPA);

                        //Send Reactivation Message II to all its neighbors
                        broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock,source,-1, receiver, -1, REACTIVATE_MESSAGE_II, "",temp_tree);

                        //Include all the nodes from the list on its own TM Routing table.
                        try{
                            for(i=1;i<=temp;i++){
                                //Getting the ID of the node
                                temp_data_int = Integer.parseInt(temp_data_array2[i]);
                                //Adding the node on the Routing Table, with the worst route: metric = 0 and infinite nu ber of hops.
                                getNode(receiver).getTM_temp_routing(source).addRoutingRegister(new routing_table_register(temp_data_int, temp_data_int, 0, Integer.MAX_VALUE));
                            }
                        }catch(Exception ex){ex.printStackTrace();}

                        //Set a Timeout for sending the Route Request Message
                        pushEvent(new event_sim(temp_clock+TIMEOUT_SENDING_RRQ, temp_clock, source, -1, receiver, receiver, -1, TIMEOUT_SEND_RRQ_MESSAGE, "",temp_tree,type));

                        //Set a Timeout for going back to normal
                        //pushEvent(new event_sim(temp_clock+TIMEOUT_BACK_TO_NORMAL, temp_clock, receiver, receiver, TIMEOUT_BACK_TO_NORMAL_EVENT, "",temp_tree,type));

                    }else
                        //Set the state of the node to TM_INITIAL
                        getNode(receiver).setTM_State(source, TM_INITIAL);

                //}
                
                break;
                
                
            case REACTIVATE_MESSAGE_II:

                if(getNode(receiver).isGateway(temp_tree, sender)){
                
                    getNode(receiver).setRoutingActive(temp_tree, false);
                    
                    if(!getNode(receiver).isValidTMStructConfIndex(source))
                        //Create the TMConfStructure according to the index received from the sender
                        getNode(receiver).addTmConfStructure(source);

                    if(getNode(receiver).getTM_State(source) == TM_INACTIVE && getNode(receiver).getTM_State(source)!=TM_GRANDPA){

                        if(getNode(receiver).isSleeping(temp_tree)){

                            //Turn the radio on
                            getNode(receiver).setRadioOn(temp_tree, true);

                        } else{

                            //Send Reactivation Message II to all the neighbors of the active nodes of the grandpa node
                            broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, source, -1, receiver, -1, REACTIVATE_MESSAGE_III, "",temp_tree);

                        }

                        //Set the state of the node to TM_INITIAL
                        getNode(receiver).setTM_State(source, TM_INITIAL);

                        //Set a Timeout for going back to normal
                        //pushEvent(new event_sim(temp_clock+TIMEOUT_BACK_TO_NORMAL, temp_clock, receiver, receiver, TIMEOUT_BACK_TO_NORMAL_EVENT, "",temp_tree,type));

                    }
                }
                break;
                
                
                
            case REACTIVATE_MESSAGE_III:
                
                if(getNode(receiver).isGateway(temp_tree, sender)){
                
                    getNode(receiver).setRoutingActive(temp_tree, false);

                    if(!getNode(receiver).isValidTMStructConfIndex(source))
                        //Create the TMConfStructure according to the index received from the sender
                        getNode(receiver).addTmConfStructure(source);
                    
                    //If the receiver node is active but have not been initiated, it will keep being inactive in the TM protocol.
                    if(getNode(receiver).getTM_State(source)==TM_INACTIVE){

                        if(getNode(receiver).isSleeping(temp_tree)){

                            //Set the state of the node to TM_INITIAL
                            getNode(receiver).setTM_State(source, TM_INITIAL);

                            //Turn the radio off
                            getNode(receiver).setRadioOn(temp_tree, true);

                            //Set a Timeout for going back to normal
                           //pushEvent(new event_sim(temp_clock+TIMEOUT_BACK_TO_NORMAL, temp_clock, receiver, receiver, TIMEOUT_BACK_TO_NORMAL_EVENT, "",temp_tree,type));

                        }

                    }
                    
                }
                break;
                
                
            case TIMEOUT_SEND_RRQ_MESSAGE:

                //father.simulation_pause();
                
                //Obtain the list of all the nodes that this one needs to find a route to. This is a special list in which the values 
                // for metric and number of hops were initiaited in 0.
                // structure of the list:  Number of elements$ID of destination@address of gateway@metric# of hops$ID of destination@address of gateway@metric# of hops$...
                temp_data = getNode(sender).getTM_temp_routing(source).toString4TMLocal();
                
                //Send Route Request Message to all its neighbors
                broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, source, -1, sender, -1, ROUTE_REQUEST_MESSAGE, temp_data,temp_tree);
                
                break;
                
                
            case ROUTE_REQUEST_MESSAGE:
                
                if(getNode(receiver).isValidTMStructConfIndex(source)){
                    if(getNode(receiver).getTM_State(source) == TM_INACTIVE){
                        // Do nothing
                    } else

                    if(getNode(receiver).getTM_State(source) != TM_GRANDPA){

                        try{

                            temp_data_array = temp_data.split("#");

                            if(temp_data_array.length == 1)
                                i=0;

                            //Number of routing table registers in message
                            temp = Integer.parseInt(temp_data_array[0]);

                            if(temp > 0){

                                // Getting the radius in the current tree 
                                temp_val_double = getNode(receiver).getInfrastructure(temp_tree).getComm_radius();

                                // Getting the distance between the sender and receiver
                                temp_val_double1 = getNode(receiver).calcDistance(getNode(sender));

                                //Getting the ratio of distance between the sender and receiver
                                temp_val_double2 = temp_val_double1/temp_val_double;

                                //Obtaining the energy reserve of the receiver node
                                temp_val_double3 = java.lang.Math.max(getNode(receiver).getEnergyRatio(),0);

                                //Metric of this node respect the sender node.
                                temp_val_double4 = father.getVariable(WEIGHT_METRIC1)*temp_val_double2 + father.getVariable(WEIGHT_METRIC2)*temp_val_double3;

                                for(i=1;i<=temp;i++){

                                    temp_data_array2 = temp_data_array[i].split("@");

                                    //Get the destination ID
                                    temp_data_int = Integer.parseInt(temp_data_array2[0]); 

                                    //Updating the metric of the path 
                                    temp_val_double2 = Double.parseDouble(temp_data_array2[2]) + temp_val_double4;

                                    //Updating the number of hops of the path
                                    temp_data_int2 = Integer.parseInt(temp_data_array2[3]) + 1; 

                                    //Creating or updating the register
                                    sw=getNode(receiver).getTM_temp_routing(source).addRoutingRegister(new routing_table_register(sender, temp_data_int, temp_val_double2,temp_data_int2));

                                    sw2 = sw2|| sw;

                                    //If there was a change exactly on the active child entry, it needs to delay
                                    if(sw == false && temp_data_int == receiver)
                                        sw3 = false;
                                }

                            }



                        }catch(Exception ex){ex.printStackTrace();}


                        //If the register did generated a change on the Routing Table, the the timeout will be generated.
                        if(getNode(receiver).getTM_State(source) != TM_VISITED){
                            if(getNode(receiver).getTM_State(source) != TM_SEMI_ACTIVE){
                                //Set a Timeout for stop listening for RRQ messages, for every node that is not active
                                pushEvent(new event_sim(temp_clock+TIMEOUT_LISTENING_RRQ, temp_clock, source, -1, receiver, receiver, receiver, TIMEOUT_LISTENING_RRQ_MESSAGE, "",temp_tree,type));
                            }else{
                                //Set a Timeout for stop listening for RRQ messages, but in the case of active nodes, we are giving them longer time to get better decission
                                pushEvent(new event_sim(temp_clock+TIMEOUT_LISTENING_RRQ_ON_ACTIVE+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, source, -1, receiver, receiver, -1, SEND_ROUTE_REPLY_MESSAGE, "",temp_tree,type));
                            }
                            //If the node was visited, it will change state.
                            getNode(receiver).setTM_State(source, TM_VISITED);
                            
                        }




                    } 
                    
                }
                break;
                
                
            case TIMEOUT_LISTENING_RRQ_MESSAGE:
                
                if(getNode(receiver).isValidTMStructConfIndex(source)){
                    //If the node was visited, and finished it mission Now is ready to forward RRP messages.
                    getNode(sender).setTM_State(source, TM_READY);

                    //Obtain the list of all the nodes that this node is checking for routes. 
                    temp_data = getNode(sender).getTM_temp_routing(source).toString4TMLocal2();

                    //Send Route Request Message to all its neighbors
                    broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, source, -1, sender, -1, ROUTE_REQUEST_MESSAGE, temp_data,temp_tree);
                    
                    pushEvent(new event_sim(temp_clock+TIMEOUT_BACK_TO_NORMAL, temp_clock, source, -1, sender, sender,-1, TIMEOUT_BACK_TO_NORMAL_EVENT, "",temp_tree,type));
                }
                break;
                
                
            case SEND_ROUTE_REPLY_MESSAGE:

                //father.simulation_pause();
                father.frame_repaint();
                
                pushEvent(new event_sim(temp_clock+TIMEOUT_BACK_TO_NORMAL, temp_clock, source, -1, sender, sender,-1, TIMEOUT_BACK_TO_NORMAL_EVENT, "",temp_tree,type));
                
                //Obtain the list of all the nodes that this node is checking for routes. 
                temp_data = getNode(sender).getTM_temp_routing(source).toString4TMLocal2();
                
                //Send Route Request Message to all its neighbors
                broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, source, -1, sender, -1, ROUTE_REQUEST_MESSAGE, temp_data,temp_tree);
                
                
                //If the node was visited, and finished it mission Now is ready to forward RRP messages.
                getNode(sender).setTM_State(source, TM_READY);
                
                //Obtain the ID of the best previous hop to reach the active node
                temp = getNode(sender).getTM_temp_routing(source).getGateway(sender);
                
                
                /*//Set node in Active state, according to its label set
                getNode(sender).setState(getNode(sender).getLabel(temp_tree, TYPE_LABEL_ACTIVE), temp_tree);
                
                //Setting the radio on
                getNode(sender).setRadioOn(temp_tree, true);
                */
                
                
                //Update the information on the TC and COMM protocol data structures
                getNode(sender).setDefaultGateway(temp_tree, temp);
                
                if(!getNode(sender).isGateway(temp_tree, temp)){
                    //Update the information on the TC and COMM protocol data structures -  adding the node as a new gateway
                    getNode(sender).addGateway(temp_tree, temp, temp);
                }
                
                //CHECK THIS LINE... HOW TO GET THE ADDRESS OF THE NEW GATEWAY??? MAYBE ANOTHER MESSAGE IS NEEDED FOR READDRESSING
                getNode(sender).getRoutingTable(temp_tree).addRoutingRegister(new routing_table_register(temp, temp));

                temp_data=""+sender;
                
                //Send Route Request Message to all its neighbors
                broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, source, -1, sender, temp, ROUTE_REPLY_MESSAGE, temp_data,temp_tree);
                
                break;
                
           
            case ROUTE_REPLY_MESSAGE:
                
                if(receiver == destination && getNode(receiver).isValidTMStructConfIndex(source)){
                    if(getNode(receiver).getTM_State(source) == TM_INACTIVE){
                        // Do nothing
                    } else{

                        
                        if(getNode(receiver).getTM_State(source) != TM_GRANDPA){
                            
                            //Eliminate all future actions about TM
                            //InvalidateAllEventsFromIDFromTimeTOfTypeTy(receiver, temp_clock+DELTA_TIME, type);
                            //pushEvent(new event_sim(temp_clock+(TIMEOUT_BACK_TO_NORMAL/2), temp_clock, source, -1, receiver, receiver, -1, TIMEOUT_BACK_TO_NORMAL_EVENT, "",temp_tree,type));
                            
                            if(getNode(receiver).isSleeping(temp_tree)){
                            
                               
                                
                                //Initiate the sensor querying protocol only on the active nodes of the topology!!
                                if(SD_Selected){
                                    pushEvent(new event_sim(temp_clock+DELTA_TIME, temp_clock, receiver, receiver, INIT_EVENT, "",temp_tree,SENSOR_PROTOCOL));
                                }

                                //Initiate the COMM  protocol only on the active nodes of the topology!!
                                if(COMM_Selected){
                                    pushEvent(new event_sim(temp_clock+DELTA_TIME, temp_clock, receiver, receiver, INIT_EVENT, "",temp_tree,COMM_PROTOCOL));
                                }
                                
                            }
                            
                             //Set node in Active state, according to its label set
                                getNode(receiver).setState(getNode(receiver).getLabel(temp_tree, TYPE_LABEL_ACTIVE), temp_tree);

                                //Setting the radio on
                                getNode(receiver).setRadioOn(temp_tree, true);
                            
                            try{
                            
                                temp_data_int = Integer.parseInt(temp_data);
                                
                            }catch(Exception ex){ex.printStackTrace();}
                            
                            //Obtain the ID of the best previous hop to reach the active node
                            temp = getNode(receiver).getTM_temp_routing(source).getGateway(temp_data_int);

                            //Update the information on the TC and COMM protocol data structures
                            getNode(receiver).setDefaultGateway(temp_tree, temp);
                            
                            if(!getNode(receiver).isGateway(temp_tree, sender)){
                                getNode(receiver).addGateway(temp_tree, sender, sender);
                                getNode(receiver).getRoutingTable(temp_tree).addRoutingRegister(new routing_table_register(sender, sender));
                            }
                            
                            if(!getNode(receiver).isCandidate(temp_tree, sender))
                                getNode(receiver).addCandidate(temp_tree, new candidate(sender, sender, 0));
                            
                            if(!getNode(receiver).isGateway(temp_tree, temp)){
                                //CHECK THIS LINES... HOW TO GET THE ADDRESS OF THE NEW GATEWAY??? MAYBE ANOTHER MESSAGE IS NEEDED FOR READDRESSING
                                getNode(receiver).addGateway(temp_tree, temp, temp);
                                getNode(receiver).getRoutingTable(temp_tree).addRoutingRegister(new routing_table_register(temp, temp));
                            }
                            //Send Route Request Message to all its neighbors
                            broadcast(temp_clock+getRandom(MAX_TX_DELAY_RANDOM), temp_clock, receiver, temp, ROUTE_REPLY_MESSAGE, temp_data,temp_tree);

                        }else{
                        
                            if(!getNode(receiver).isGateway(temp_tree, sender)){
                                //CHECK THIS LINE... HOW TO GET THE ADDRESS OF THE NEW GATEWAY??? MAYBE ANOTHER MESSAGE IS NEEDED FOR READDRESSING
                                getNode(receiver).addGateway(temp_tree, sender, sender);
                                getNode(receiver).getRoutingTable(temp_tree).addRoutingRegister(new routing_table_register(sender, sender));
                            }
                            
                        }
                        
                        
                    }

                }
                break;
                
                
            case TIMEOUT_BACK_TO_NORMAL_EVENT:
                
                if(getNode(sender).getTM_State(source) == TM_DEAD){
                
                    getNode(sender).setRadioOn(temp_tree, false);
                    getNode(sender).setState(getNode(sender).getLabel(temp_tree, TYPE_LABEL_SLEEPING), temp_tree);
                    
                    
                }else if(getNode(sender).getState(temp_tree) == getNode(sender).getLabel(temp_tree, TYPE_LABEL_SLEEPING)){
                    getNode(sender).setRadioOn(temp_tree, false);
                }
                
                getNode(sender).removeTmConfStructure(source);
                //getNode(sender).setRoutingActive(temp_tree, true);
                break;
                
                
        }
        
    }
    }
}
