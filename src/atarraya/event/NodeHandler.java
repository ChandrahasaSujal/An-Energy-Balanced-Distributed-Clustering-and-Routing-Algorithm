

package atarraya.event;

import atarraya.*;

/**
 *
 * @author Pedro
 */
public class NodeHandler implements EventHandlerSW, constants{
    
    /*
    public static int TC_PROTOCOL = 0;
    public static int TM_PROTOCOL = 1;
    public static int COMM_PROTOCOL = 2;
    public static int SENSOR_PROTOCOL = 3;
     */
    
    atarraya_frame father;
    
    //Topology Construction protocol handler
    EventHandlerSW tc_protocol_handler;
    
    //Topology Maintanence protocol handler
    EventHandlerSW tm_protocol_handler;
    
    //Communication protocol handler (data forwarding, etc)
    EventHandlerSW comm_protocol_handler;
    
    //Sensor handler (monitoring enviromental event)
    EventHandlerSW sensor_handler;

    //Sensor handler (monitoring enviromental event)
    EventHandlerSW motion_handler;
    
    
    public NodeHandler(atarraya_frame father_) {
        father = father_;
    }
    
    /** Creates a new instance of NodeHandler */
    public NodeHandler(atarraya_frame father_,int tc_protocol_handler_, int tm_protocol_handler_, int comm_protocol_handler_, int sensor_handler_) {

        father = father_;
        
        //DEFINITION OF THE TOPOLOGY CONSTRUCTION HANDLER
        switch(tc_protocol_handler_){
        
            case PROTOCOL_NO_SELECTED:
                    tc_protocol_handler=null;
                    break;

         
            case PROTOCOL_A3:
                     System.out.println("Enter A3");
                    tc_protocol_handler = new EventHandlerTCA3(father);
                    tc_protocol_handler.setLabels(S_INITIAL_STATE, S_ACTIVE, S_SLEEP, S_SLEEP);
                    break;
                    

            case PROTOCOL_ECDS:
                     System.out.println("Enter Protocol ECDS");
                    tc_protocol_handler = new EventHandlerTCEECDS(father);
                    tc_protocol_handler.setLabels(WHITE, BLUE, GREY, GREY_FINAL);
                    break;
                    
            case PROTOCOL_CDS_RULEK:
                     System.out.println("Enter CDS RULEK");
                    tc_protocol_handler = new EventHandlerTCRuleK(father);  
                    tc_protocol_handler.setLabels(S_NOT_VISITED, C_BLACK_FINAL, C_UNMARKED,C_UNMARKED_FINAL);
                    break;
            
            case PROTOCOL_SIMPLE_TREE:
                     System.out.println("Enter Simple Tree");
                    tc_protocol_handler = new EventHandlerTCSimpleTree(father);  
                    tc_protocol_handler.setLabels(S_INITIAL_STATE, S_ACTIVE, S_SLEEP, S_SLEEP);
                    break;
                    
            case PROTOCOL_JUST_TREE:
                 System.out.println("Enter Just Tree");
                    tc_protocol_handler = new EventHandlerTCJustTree(father);  
                    tc_protocol_handler.setLabels(S_INITIAL_STATE, S_ACTIVE, S_SLEEP, S_SLEEP);
                    break;

            case PROTOCOL_KNEIGH:
                 System.out.println("Enter KNEIGH");
                    tc_protocol_handler = new EventHandlerTCKNeigh(father);
                    tc_protocol_handler.setLabels(KN_NOT_VISITED, KN_UPDATED, S_SLEEP, S_SLEEP);
                    break;

             case PROTOCOL_STATELESS_MULTICAST:
                   System.out.println("Enter Multicast");
                    tc_protocol_handler = new EventHandlerClustering(father);
                    tc_protocol_handler.setLabels(KN_NOT_VISITED, KN_UPDATED, S_SLEEP, S_SLEEP);
                    break;

                     case PROTOCOL_LEACH:
                   System.out.println("Enter Leach");
                    tc_protocol_handler = new EventHandlerLeach(father);
                    tc_protocol_handler.setLabels(KN_NOT_VISITED, KN_UPDATED, S_SLEEP, S_SLEEP);
                    break;

                    
        }
        
        //DEFINITION OF THE TOPOLOGY MAINTANANCE HANDLER
        switch(tm_protocol_handler_){
        
            case TM_PROTOCOL_NO_SELECTED:
                    tm_protocol_handler=null;
                    break;
               
            case TM_PROTOCOL_SINK_ISOLATED:
                    tm_protocol_handler = new EventHandlerTMSimple(father);    
                    break;
                    
            case TM_PROTOCOL_TIME_RESET:
                    tm_protocol_handler = new EventHandlerDGTTRec(father);    
                break;
                    
                    
            case TM_PROTOCOL_TIME_MULTIPLE_STRUCT:
                tm_protocol_handler = new EventHandlerSGTTRot(father);  
                break;
            
            case TM_PROTOCOL_TIME_MULTIPLE_STRUCT_REGEN:
                tm_protocol_handler = new EventHandlerHGTTRecRot(father);  
                break;
                
            case TM_PROTOCOL_ENERGY_LOCAL_PATCHING_DSR:
                tm_protocol_handler = new EventHandlerTMLocalDSR(father);  
                break;
                
            case TM_PROTOCOL_ENERGY_RESET:
                tm_protocol_handler = new EventHandlerDGETRec(father);  
                break;

            case TM_PROTOCOL_ENERGY_MULTIPLE_STRUCT:
                tm_protocol_handler = new EventHandlerSGETRot(father);  
                break; 
                
            case TM_PROTOCOL_ENERGY_MULTIPLE_STRUCT_REGEN:
                tm_protocol_handler = new EventHandlerHGETRecRot(father);  
                break;

        }
        
        //DEFINITION OF THE COMMUNICATION PROTOCOL HANDLER
        switch(comm_protocol_handler_){
        
            case COMM_PROTOCOL_NO_SELECTED:
                    comm_protocol_handler=null;
                    break;
            
            case COMM_PROTOCOL_SIMPLE_FORWARDING:
                    comm_protocol_handler = new EventHandlerCOMMSimpleForwarding(father);  
                    break;
            
            default:
                    comm_protocol_handler = new EventHandlerCOMMSimpleForwarding(father);  
                break;
                    
        }
        
        //DEFINITION OF THE SENSOR HANDLER
         switch(sensor_handler_){
        
            case SENSOR_PROTOCOL_NO_SELECTED:
                    tm_protocol_handler=null;
                    break;
            
            case SENSOR_PROTOCOL_BASIC:
                    tm_protocol_handler = new EventHandlerSensorSimple(father);
                    break;
            
        }



    }
    
    
    public void SetHandler(int type, int code){
    
        switch(type){
        
            case TC_PROTOCOL:
                //DEFINITION OF THE TOPOLOGY CONSTRUCTION HANDLER
                switch(code){

                    case PROTOCOL_NO_SELECTED:
                            tc_protocol_handler=null;
                            break;

                    case PROTOCOL_A3:
                            tc_protocol_handler = new EventHandlerTCA3(father);
                            tc_protocol_handler.setLabels(S_INITIAL_STATE, S_ACTIVE, S_SLEEP, S_SLEEP);
                            break;

           
                    case PROTOCOL_ECDS:
                            tc_protocol_handler = new EventHandlerTCEECDS(father);
                            tc_protocol_handler.setLabels(WHITE, BLUE, GREY, GREY_FINAL);
                            break;

                    case PROTOCOL_CDS_RULEK:
                            tc_protocol_handler = new EventHandlerTCRuleK(father);  
                            tc_protocol_handler.setLabels(S_NOT_VISITED, C_BLACK_FINAL, C_UNMARKED,C_UNMARKED_FINAL);
                            break;

                    case PROTOCOL_SIMPLE_TREE:
                            tc_protocol_handler = new EventHandlerTCSimpleTree(father);  
                            tc_protocol_handler.setLabels(S_INITIAL_STATE, S_ACTIVE, S_SLEEP, S_SLEEP);
                            break;
                    
                    case PROTOCOL_JUST_TREE:
                            tc_protocol_handler = new EventHandlerTCJustTree(father);  
                            tc_protocol_handler.setLabels(S_INITIAL_STATE, S_ACTIVE, S_SLEEP, S_SLEEP);
                            break;

                    case PROTOCOL_KNEIGH:
                            tc_protocol_handler = new EventHandlerTCKNeigh(father);
                            tc_protocol_handler.setLabels(KN_NOT_VISITED, KN_UPDATED, S_SLEEP, S_SLEEP);
                    break;

                     case PROTOCOL_STATELESS_MULTICAST:
                         
                            tc_protocol_handler = new EventHandlerClustering(father);
                            //tc_protocol_handler.setLabels(KN_NOT_VISITED, KN_UPDATED, S_SLEEP, S_SLEEP);
                    break;

                     case PROTOCOL_LEACH:

                            tc_protocol_handler = new EventHandlerLeach(father);
           
                            
                }
                break;
                
            case TM_PROTOCOL:
                //DEFINITION OF THE TOPOLOGY MAINTANANCE HANDLER
                switch(code){

                    case TM_PROTOCOL_NO_SELECTED:
                            tm_protocol_handler=null;
                            break;

                    case TM_PROTOCOL_SINK_ISOLATED:
                            tm_protocol_handler = new EventHandlerTMSimple(father);    
                            break;

                    case TM_PROTOCOL_TIME_RESET:
                            tm_protocol_handler = new EventHandlerDGTTRec(father);    
                        break;

                    case TM_PROTOCOL_TIME_MULTIPLE_STRUCT:
                            tm_protocol_handler = new EventHandlerSGTTRot(father);  
                            break;
                            
                    case TM_PROTOCOL_TIME_MULTIPLE_STRUCT_REGEN:
                            tm_protocol_handler = new EventHandlerHGTTRecRot(father);  
                            break;
                            
                    case TM_PROTOCOL_ENERGY_LOCAL_PATCHING_DSR:
                            tm_protocol_handler = new EventHandlerTMLocalDSR(father);  
                            break;
                            
                    case TM_PROTOCOL_ENERGY_RESET:
                            tm_protocol_handler = new EventHandlerDGETRec(father);  
                            break;

                    case TM_PROTOCOL_ENERGY_MULTIPLE_STRUCT:
                            tm_protocol_handler = new EventHandlerSGETRot(father);  
                            break; 
                            
                    case TM_PROTOCOL_ENERGY_MULTIPLE_STRUCT_REGEN:
                            tm_protocol_handler = new EventHandlerHGETRecRot(father);  
                        break;
                            
                    default:
                        tm_protocol_handler = new EventHandlerTMSimple(father);    
                        break;
                            
                }
                break;
                
            case COMM_PROTOCOL:
                //DEFINITION OF THE COMMUNICATION PROTOCOL HANDLER
                switch(code){

                    case COMM_PROTOCOL_NO_SELECTED:
                            comm_protocol_handler=null;
                            break;

                    case COMM_PROTOCOL_SIMPLE_FORWARDING:
                            comm_protocol_handler = new EventHandlerCOMMSimpleForwarding(father);  
                            break;
                    default:
                            comm_protocol_handler = new EventHandlerCOMMSimpleForwarding(father);  
                    break;
                            
                }
                break;
                
            case SENSOR_PROTOCOL:
                //DEFINITION OF THE SENSOR HANDLER
                 switch(code){

                    case SENSOR_PROTOCOL_NO_SELECTED:
                            sensor_handler=null;
                            break;

                    case SENSOR_PROTOCOL_BASIC:
                            sensor_handler = new EventHandlerSensorSimple(father);  
                            break;

                            
                    default:
                            sensor_handler = new EventHandlerSensorSimple(father);  
                            break;
                }
            break;


            case MOTION_PROTOCOL:
                //DEFINITION OF THE SENSOR HANDLER
                 switch(code){

                    case MOTION_PROTOCOL_NO_SELECTED:
                            motion_handler=null;
                            break;

                    case MOTION_PROTOCOL_SIMPLE_MOTION:
                            motion_handler = new EventHandlerMotionSimple(father);
                            break;


                    default:
                            motion_handler = new EventHandlerMotionSimple(father);
                            break;
                }
            break;

        }
    }
    
    public void HandleEvent(event_sim e){
    
        int type = e.getType();
        switch(type){
            case TC_PROTOCOL:
                tc_protocol_handler.HandleEvent(e);                
                break;
                
            case TM_PROTOCOL:
                tm_protocol_handler.HandleEvent(e);
                break;
                
            case COMM_PROTOCOL:
                comm_protocol_handler.HandleEvent(e);
                break;
                
            case SENSOR_PROTOCOL:
                sensor_handler.HandleEvent(e);
                break;

            case MOTION_PROTOCOL:
                motion_handler.HandleEvent(e);
                break;

                //Default case. Type -1 has no type defined, but it will be handledTC.
            case -1:
            default:
                tc_protocol_handler.HandleEvent(e);
                break;
                
        }
        
    }
    
    public int getTMType(){
        
        if(tm_protocol_handler!=null)
            return tm_protocol_handler.getTMType();
        
        return NO_TM;
    }
    
    public void setLabels(int initial_, int active_, int inactive_, int sleeping_){}
    
    public void initial_event(int _id, int _treeID){}
    
    public void initial_event(int _id, int _treeID, int type){
        
        switch(type){
            case TC_PROTOCOL:
                tc_protocol_handler.initial_event(_id,_treeID);
                break;
                
            case TM_PROTOCOL:
                tm_protocol_handler.initial_event(_id,_treeID);
                break;
                
            case COMM_PROTOCOL:
                comm_protocol_handler.initial_event(_id,_treeID);
                break;
                
            case SENSOR_PROTOCOL:
                sensor_handler.initial_event(_id,_treeID);
                break;
            
            case MOTION_PROTOCOL:
                motion_handler.initial_event(_id,_treeID);
                break;

             //Default case. Type -1 has no type defined, but it will be handledTC.
            case -1:
            default:
                tc_protocol_handler.initial_event(_id,_treeID);
                break;
                
        }
        
    }
    
    public void init_nodes(int _treeID){}
    
    public void init_nodes(int _treeID, int type){
    
        switch(type){
            case TC_PROTOCOL:
                tc_protocol_handler.init_nodes(_treeID);
                break;
                
            case TM_PROTOCOL:
                tm_protocol_handler.init_nodes(_treeID);
                break;
                
            case COMM_PROTOCOL:
                comm_protocol_handler.init_nodes(_treeID);
                break;
                
            case SENSOR_PROTOCOL:
                sensor_handler.init_nodes(_treeID);
                break;

            case MOTION_PROTOCOL:
                motion_handler.init_nodes(_treeID);
                break;

             //Default case. Type -1 has no type defined, but it will be handledTC.
            case -1:
            default:
                tc_protocol_handler.init_nodes(_treeID);
                break;
                
        }
    
    }
    
    public boolean CheckIfDesiredFinalState(int s){
        return false;
    }
    
    public boolean CheckIfDesiredFinalState(int s, int type){
        
        boolean sw=false;
        
        switch(type){
            case TC_PROTOCOL:
                sw=tc_protocol_handler.CheckIfDesiredFinalState(s);
                break;
                
            case TM_PROTOCOL:
                sw=tm_protocol_handler.CheckIfDesiredFinalState(s);
                break;
                
            case COMM_PROTOCOL:
                sw=comm_protocol_handler.CheckIfDesiredFinalState(s);
                break;
                
            case SENSOR_PROTOCOL:
                sw=sensor_handler.CheckIfDesiredFinalState(s);
                break;
                
             //Default case. Type -1 has no type defined, but it will be handledTC.
            case -1:
            default:
                sw=tc_protocol_handler.CheckIfDesiredFinalState(s);
                break;
                
        }
        
        return sw;
    }
    
    
    public boolean verifIfNodeInCDS(int _id, int _treeID){
        return tc_protocol_handler.verifIfNodeInCDS(_id, _treeID);
    }
    
    
    public boolean verifIfNodeInCDS(int _id, int _treeID, int type){
        
        boolean sw=false;
        
        switch(type){
            case TC_PROTOCOL:
                sw=tc_protocol_handler.verifIfNodeInCDS(_id, _treeID);
                break;
                
            case TM_PROTOCOL:
                sw=tm_protocol_handler.verifIfNodeInCDS(_id, _treeID);
                break;
                
            case COMM_PROTOCOL:
                sw=comm_protocol_handler.verifIfNodeInCDS(_id, _treeID);
                break;
                
            case SENSOR_PROTOCOL:
                sw=sensor_handler.verifIfNodeInCDS(_id, _treeID);
                break;
                
             //Default case. Type -1 has no type defined, but it will be handledTC.
            case -1:
            default:
                sw=tc_protocol_handler.verifIfNodeInCDS(_id, _treeID);
                break;
                
        }
        
        return sw;
    }
    
    public boolean CheckIfDesiredSleepingState(int s){
        return false;
    }
    
    public boolean CheckIfDesiredSleepingState(int s, int type){
        
        boolean sw=false;
        
        switch(type){
            case TC_PROTOCOL:
                sw=tc_protocol_handler.CheckIfDesiredSleepingState(s);
                break;
                
            case TM_PROTOCOL:
                sw=tm_protocol_handler.CheckIfDesiredSleepingState(s);
                break;
                
            case COMM_PROTOCOL:
                sw=comm_protocol_handler.CheckIfDesiredSleepingState(s);
                break;
                
            case SENSOR_PROTOCOL:
                sw=sensor_handler.CheckIfDesiredSleepingState(s);
                break;
                
             //Default case. Type -1 has no type defined, but it will be handledTC.
            case -1:
            default:
                sw=tc_protocol_handler.CheckIfDesiredSleepingState(s);
                break;
                
        }
        
        return sw;
    }
    
    public int GetMessageSize(int code){
        return SIZE_SHORT_PACKETS;
    }
    
    public int GetMessageSize(int code, int type){
        
        int size=SIZE_SHORT_PACKETS;
        
        switch(type){
            case TC_PROTOCOL:
                size=tc_protocol_handler.GetMessageSize(code);
                break;
                
            case TM_PROTOCOL:
                size=tm_protocol_handler.GetMessageSize(code);
                break;
                
            case COMM_PROTOCOL:
                size=comm_protocol_handler.GetMessageSize(code);
                break;
                
            case SENSOR_PROTOCOL:
                size=sensor_handler.GetMessageSize(code);
                break;
                
             //Default case. Type -1 has no type defined, but it will be handledTC.
            case -1:
            default:
                size=tc_protocol_handler.GetMessageSize(code);
                break;
                
        }
        
        
        return size;
    }
    
    
}
