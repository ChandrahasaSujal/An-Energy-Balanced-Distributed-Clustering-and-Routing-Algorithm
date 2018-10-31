/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package atarraya.event;

import atarraya.atarraya_frame;
import atarraya.constants;
import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kitty
 */
public class EventHandlerBalancedClustering implements EventHandlerSW,constants {

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
     int k=0;








    public EventHandlerBalancedClustering(atarraya_frame _father)
    {
        //allocation of memory for total number of nodes list in each quadrant

        father=_father;
        TM_Selected=false;
        SD_Selected=false;
        COMM_Selected=false;
        TMType = NO_TM;


        temp_num_structures = (int)father.getVariable(NUMINFRASTRUCTURES);

        candidate_grouping_step = father.getVariable(CANDIDATE_GROUP_STEP);

        temp_selectedTMprotocol = (int)father.getVariable(SELECTED_TM_PROTOCOL);

        temp_address = 0;

        active = 0;
        inactive = 0;
        initial = 0;
        sleeping = 0;
    }


    public void HandleEvent(event_sim e) {

      
       int packet_type=e.getCode();
       
       if(packet_type==atarraya_frame.MESSAGE_TYPE_BSHELLO)
       {
           baseStationBroadcast(e);
       }//end of Base_Station_Hello
       else if(packet_type==atarraya_frame.MESSAGE_TYPE_CLUSTER_FORMATION)
       {
           clusterGenerationMessage(e.getSender(),e.getReceiver(),e.getIds());
       }
       else if(packet_type==atarraya_frame.MESSAGE_TYPE_JOIN_REQUEST)
       {

          int receiver=e.getReceiver();
          int sender=e.getSender();
          sendJoinReply(receiver, sender);
           
       }
    }

   

      public void sendJoinReply(int receiver,int sender)
    {
        double _clock = father.getVariable(CLOCK);
         father.getNode(receiver).color=Color.RED;

         father.getNode(receiver).NODE_TYPE="sensor";
         father.repaint();

        father.pushEvent(new event_sim(20.0,_clock,receiver,sender,atarraya_frame.MESSAGE_TYPE_JOIN_REPLY));
        father.drawGreenLines(receiver, sender);
        father.repaint();
        
    }


    public void clusterGenerationMessage(int sender,int receiver,int[] ids)
    {
        double _clock = father.getVariable(CLOCK);
       
        double wght=father.getNode(receiver).weight;
        father.pushEvent(new event_sim(wght,_clock,receiver,sender,atarraya_frame.MESSAGE_TYPE_JOIN_REQUEST));
        father.drawBlueLines(receiver, sender);
        father.repaint();
        for(int i=0;i<ids.length;i++)
        {
        String nod_stat=father.getNode(ids[i]).NODE_TYPE;
        if(nod_stat.equals("intermediate")&&ids[i]!=receiver&&ids[i]!=sender)
        {
        father.pushEvent(new event_sim(wght,_clock,receiver,ids[i],atarraya_frame.MESSAGE_TYPE_JOIN_REQUEST));
        father.drawBlueLines(receiver,ids[i]);
        father.repaint();
        }

        }
    

    }

    public void baseStationBroadcast(event_sim e)
    {
         double _clock = father.getVariable(CLOCK);
       int receiver=e.getReceiver();
        int destination=e.getDestination();
           if( father.getNode(receiver).HOP_COUNT_RECEIVED)
           {
               if(father.getNode(receiver).DISTANCE_FROM_BASE_STATION>e.getHopCount())
               {
                    father.getNode(receiver).DISTANCE_FROM_BASE_STATION=e.getHopCount();
                    int hop_cnt=e.getHopCount()+1;
                    int[] neibours=father.getNode(receiver).getNeighborsIds();
                    for(int i=0;i<neibours.length;i++)
                    {
                        father.pushEvent(new event_sim(hop_cnt, _clock,receiver,neibours[i],destination,atarraya_frame.MESSAGE_TYPE_BSHELLO));
                        father.drawRedLines(receiver, neibours[i]);
                        father.repaint();
                    }
               }


           }
           else
           {

           




           father.getNode(receiver).HOP_COUNT_RECEIVED=true;
           father.getNode(receiver).DISTANCE_FROM_BASE_STATION=e.getHopCount();
           int hop_cnt=e.getHopCount()+1;
           int[] neibours=father.getNode(receiver).getNeighborsIds();
            double weight=father.getNode(receiver).getEnergy()*(father.getNode(receiver).getEnergy()/neibours.length);
            father.getNode(receiver).weight=weight;
           for(int i=0;i<neibours.length;i++)
           {
        //   if(!father.getNode(i).CLUSTER_REQUEST)
        //   {
           father.pushEvent(new event_sim(hop_cnt, _clock,receiver,neibours[i],destination,atarraya_frame.MESSAGE_TYPE_BSHELLO));
           father.drawRedLines(receiver, neibours[i]);
           father.repaint();
          // }
           }

           }

    }





    public void initial_event(int _id, int _treeID) {

                try {
                    father.sat=true;
                   double _clock = father.getVariable(CLOCK);
                   int[] neibours=father.getNode(atarraya_frame.BASE_STATION_ID).getNeighborsIds();
                   for(int i=0;i<neibours.length;i++)
                   {                   
                   father.pushEvent(new event_sim(1, _clock,atarraya_frame.BASE_STATION_ID,neibours[i], atarraya_frame.BASE_STATION_ID,atarraya_frame.MESSAGE_TYPE_BSHELLO));
                   father.drawRedLines(atarraya_frame.BASE_STATION_ID, neibours[i]);
                   father.repaint();
                   }
                   new Timer().schedule(new ClusterProcessTimer(),8000);
                

                } catch (Exception ex) {
                    Logger.getLogger(EventHandlerBalancedClustering.class.getName()).log(Level.SEVERE, null, ex);
                }
    }

     public void sendClusterFormationRequest(int j,int greater_id,int[] ids)
     {
        double _clock = father.getVariable(CLOCK);
        father.pushEvent(new event_sim(ids,_clock,j,greater_id,atarraya_frame.MESSAGE_TYPE_CLUSTER_FORMATION));
        father.drawBlueLines(j,greater_id);
        father.repaint();
     }


    class ClusterProcessTimer extends TimerTask
    {

        @Override
        public void run()
        {
            double _clock = father.getVariable(CLOCK);
            father.clearRedLines();
            father.repaint();
           
       

             for(int j=0;j<father.getVariable(NUMPOINTS);j++)
            {


               double my_weight=father.getNode(j).weight;
               double greater_weight=my_weight;
               int greater_id=j;
               int neibours[]=father.getNode(j).getNeighborsIds();
               for(int i=0;i<neibours.length;i++)
               {


                   double weight=father.getNode(neibours[i]).weight;
                   if(greater_weight<weight)
                   {
                       greater_weight=weight;
                       greater_id=neibours[i];
                   }
               }

                 father.getNode(greater_id).color=Color.BLUE;
                 father.getNode(greater_id).NODE_TYPE="cluster";
                 father.repaint();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(EventHandlerBalancedClustering.class.getName()).log(Level.SEVERE, null, ex);
                }
                 sendClusterFormationRequest(j,greater_id,neibours);

            }
            

        }

    }




     public double getRandom(double max_val){
        return java.lang.Math.random()*max_val;
    }

    public void init_nodes(int _treeID) {
      //  System.out.println("Init Nodes");
    }

    public boolean CheckIfDesiredFinalState(int s) {
        return true;
    }

    public boolean verifIfNodeInCDS(int _id, int _treeID) {
        return true;
    }

    public boolean CheckIfDesiredSleepingState(int s) {
         if(s==active || s==inactive)
            return true;

        return false;
    }

    public int GetMessageSize(int code) {
             if(code==SON_RECOGNITION || code==DATA)
            return SIZE_LONG_PACKETS;

        return SIZE_SHORT_PACKETS;
    }

    public void setLabels(int initial_, int active_, int inactive_, int sleeping_) {
      initial_=active_;
      inactive_=inactive_;
      sleeping_=sleeping_;
    }

    public int getTMType() {
        return TMType;
    }

}
