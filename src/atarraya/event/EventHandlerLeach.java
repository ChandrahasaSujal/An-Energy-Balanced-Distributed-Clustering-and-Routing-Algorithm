/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package atarraya.event;

import atarraya.atarraya_frame;
import atarraya.constants;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kitty
 */
public class EventHandlerLeach implements EventHandlerSW,constants {

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

    public static double BS_X,BS_Y;

    public static ArrayList<ClusterDistance> cluster_list=new ArrayList<ClusterDistance>();

    double THRESHOLD_DISTANCE=120;




    public EventHandlerLeach(atarraya_frame _father)
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
           clusterGenerationMessage(e.getSender(),e.getReceiver(),e.getWeight());
       }
       else if(packet_type==atarraya_frame.MESSAGE_TYPE_CLUSTER_FORMATION_REPLY)
       {

           System.out.println("Entered Cluster Formation Reply");

               father.getNode(e.getReceiver()).CLUSTER_SENDER_LIST.add(new NodeEnergyStats(e.getSender(),e.getWeight()));
               if(father.getNode(e.getReceiver()).first_cluster_formation_reply)
               {
               father.getNode(e.getReceiver()).scheduleClusterReplyTimer(father);
                father.getNode(e.getReceiver()).first_cluster_formation_reply=false;
               }


       }
      else if(packet_type==atarraya_frame.MESSAGE_TYPE_JOIN_REQUEST)
      {
          double ry=father.getNode(e.getReceiver()).getPosition().getY();
          double sy=father.getNode(e.getSender()).getPosition().getY();
          double distance=Math.abs(sy-ry);
          father.getNode(e.getReceiver()).JOIN_LIST.add(new NodeDistanceStats(e.getSender(),distance));
          if(father.getNode(e.getReceiver()).first_join_request)
          {
          father.getNode(e.getReceiver()).first_join_request=false;
          father.getNode(e.getReceiver()).scheduleJoinTimer(father);
          }

      }
        else if(packet_type==atarraya_frame.MESSAGE_TYPE_JOIN_REPLY)
       {
            double y=father.getNode(e.getReceiver()).getPosition().getY();
            double x=father.getNode(e.getReceiver()).getPosition().getX();
            double diffy=Math.abs(BS_Y-y);
            double diffx=Math.abs(BS_Y-x);
            boolean below=false;
            if(BS_Y<y)
            {
                below=true;
            }
            if(diffy<THRESHOLD_DISTANCE)
            {
            ClusterDistance cdis=new ClusterDistance(e.getReceiver(), diffy,x,y,below);
            cluster_list.add(cdis);
            father.getNode(e.getReceiver()).DATA_NODE=atarraya_frame.BASE_STATION_ID;
            father.getNode(e.getReceiver()).connected_to_base_station=true;
            father.drawRedLines(e.getReceiver(),atarraya_frame.BASE_STATION_ID);
            father.getNode(e.getReceiver()).reduceEnergyforClustering();
            father.repaint();
            }
            else
            {
                int pos_y=father.getNode(e.getReceiver()).getPosition().y;
                int pos_x=father.getNode(e.getReceiver()).getPosition().x;
                 Iterator<ClusterDistance> itr=EventHandlerClustering.cluster_list.iterator();
          int p=0;
          int small_node=0;
          double small_diffx=0,small_diffy=0,small_diff=0;
          double diff_base_y=0;
          boolean below_y=false;

          while(itr.hasNext())
          {

              ClusterDistance cdis=itr.next();

              if(below_y==cdis.below_base_station)
              {
              if(p==0)
              {
                  small_node=cdis.id;
                  small_diffy=Math.abs(pos_y-cdis.y);
                 // small_diffx=Math.abs(pos_x-cdis.x);
                  small_diff=small_diffy;
                  //diff_base_y=Math.abs(BS_Y-cdis.y);
                  p=1;
              }
              else
              {

                 double dify=Math.abs(pos_y-cdis.y);
                  //double difx=Math.abs(pos_x-cdis.x);
                  //double diff_base_cy=Math.abs(BS_Y-cdis.y);
                  //double s_diff=(difx+dify)/2;
                 if(dify<small_diff)
                 {
                   //  small_diffx=difx;
                     small_diffy=dify;
                    // small_diff=dify;
                  //   diff_base_y=diff_base_cy;
                     small_node=cdis.id;
                 }

              }

                }
          }
          System.out.println("smaLL id:"+small_node);
          double _clock = father.getVariable(CLOCK);
          father.getNode(e.getReceiver()).DATA_NODE=small_node;
          father.pushEvent(new event_sim(atarraya_frame.BASE_STATION_ID,e.getReceiver(),small_node,200,_clock));
          father.drawRedLines(e.getReceiver(), small_node);
          father.repaint();



                //father.getNode(e.getReceiver()).scheduleConnectionTimer(father);
            }

       }


        else if(packet_type==atarraya_frame.MESSAGE_TYPE_DATA_TRANSFER)
       {
            if(e.getReceiver()==atarraya_frame.BASE_STATION_ID)
            {
                System.out.println("Data Received");
               // father.clearGreenLines();
                //father.repaint();
            }

            else
            {
                father.getNode(e.getReceiver()).reduceEnergyforDataTransfer();
                int dnode=father.getNode(e.getReceiver()).DATA_NODE;
                double _clock = father.getVariable(CLOCK);
                father.pushEvent(new event_sim(atarraya_frame.BASE_STATION_ID,e.getReceiver(),dnode,atarraya_frame.MESSAGE_TYPE_DATA_TRANSFER,_clock));
                father.drawGreenLines(e.getReceiver(),dnode);
                father.repaint();

            }

       }
    }



      public void sendJoinRequest(int id,int cluster_head)
    {
        double _clock = father.getVariable(CLOCK);
        int[] neibours=father.getNode(id).getNeighborsIds();
        for(int i=0;i<neibours.length;i++)
        {
            if(neibours[i]!=cluster_head&&neibours[i]!=atarraya_frame.BASE_STATION_ID)
            {
             father.getNode(id).reduceEnergyforClustering();
            father.pushEvent(new event_sim(cluster_head, id,neibours[i],atarraya_frame.MESSAGE_TYPE_JOIN_REQUEST,_clock));
            father.drawBlueLines(id, neibours[i]);
            father.repaint();
            }
        }
    }


    public void clusterGenerationMessage(int sender,int receiver,double weight)
    {

        double _clock = father.getVariable(CLOCK);
      //  int[] neibours=father.getNode(receiver).getNeighborsIds();
       // double wght=father.getNode(receiver).getEnergy()*(father.getNode(receiver).getEnergy()/neibours.length);
       // father.getNode(receiver).my_weight=wght;
         father.getNode(receiver).reduceEnergyforClustering();
        System.out.println("Weigth of Node "+receiver+"is :"+father.getNode(receiver).my_weight);
        father.pushEvent(new event_sim(_clock,receiver,sender,atarraya_frame.MESSAGE_TYPE_CLUSTER_FORMATION_REPLY,father.getNode(receiver).my_weight));
        father.drawGreenLines(receiver, sender);
        father.repaint();
      /*  for(int k=0;k<neibours.length;k++)
        {
            father.pushEvent(new event_sim(wght,_clock,receiver,neibours[k],atarraya_frame.MESSAGE_TYPE_CLUSTER_FORMATION));
            father.drawBlackLines(receiver, neibours[k]);
            father.repaint();
        }*/

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
                        father.getNode(receiver).reduceEnergyforClustering();
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
           for(int i=0;i<neibours.length;i++)
           {
        //   if(!father.getNode(i).CLUSTER_REQUEST)
        //   {
                father.getNode(receiver).reduceEnergyforClustering();
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
                    System.out.println("Entered LEach");
                   double _clock = father.getVariable(CLOCK);

                   for(int j=0;j<father.getVariable(NUMPOINTS);j++)
                   {
                       if(j%2==0)
                       {
                           father.getNode(j).setEnergy(80);
                            father.getNode(j).startDeadTimer();
                       }
                       else if(j % 3 == 0)
                       {
                           father.getNode(j).setEnergy(150);
                           father.getNode(j).startDeadTimer();
                       }
                        else
                       {
                           father.getNode(j).setEnergy(140);
                           father.getNode(j).startDeadTimer();
                       }


                   }

                   BS_X=father.getNode(atarraya_frame.BASE_STATION_ID).getPosition().getX();
                   BS_Y=father.getNode(atarraya_frame.BASE_STATION_ID).getPosition().getY();

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

                 if(j!=atarraya_frame.BASE_STATION_ID)
                 {
            int[] neibours=father.getNode(j).getNeighborsIds();
            double weight=father.getNode(j).getEnergy();
            father.getNode(j).my_weight=weight;
            System.out.println("Weight of Node:"+j+"is "+father.getNode(j).my_weight);
             for(int k=0;k<neibours.length;k++)
            {
              father.getNode(j).reduceEnergyforClustering();
            father.pushEvent(new event_sim(_clock,j,neibours[k],atarraya_frame.MESSAGE_TYPE_CLUSTER_FORMATION,weight));
            father.drawGreenLines(j,neibours[k]);
            father.repaint();

            }
                }

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
