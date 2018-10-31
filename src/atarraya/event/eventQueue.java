/*
 * eventQueue.java
 *
 * Created on July 17, 2006, 10:20 PM
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

import java.util.Vector;
import java.util.concurrent.Semaphore;

/**
 *
 * @author HP_Owner
 */
public class eventQueue {
    
    Vector<event_sim> queue;
    Semaphore sem;
    
    
    /** Creates a new instance of eventQueue */
    public eventQueue() {
        queue = new Vector<event_sim>();
        sem = new Semaphore(1);
    }
    
    public int getSize(){
        return queue.size();
    }
    
    public event_sim Pop(){
        event_sim e = null;
        try{
            sem.acquire();
            if(queue.size()>0){
                e = queue.firstElement();
                queue.remove(0);
            }
            sem.release();
        }catch(Exception ex){ex.printStackTrace();}
        return e;
    }

     public void Push(event_sim e){
         try{
            sem.acquire();
            //queue.add(e);
            add_sorted(e);
            sem.release();
         }catch(Exception ex){ex.printStackTrace();}
        //sort();
    }
    
    public void sort(){
        
        try{
            sem.acquire();
            java.util.Collections.sort(queue);
            sem.release();
         }catch(Exception ex){ex.printStackTrace();}
        
        
    }
    
    public void add_sorted(event_sim e){
        
        int n = queue.size();
        double temp_time = e.getTime();
        double temp_time_ini = e.getTimeIni();
        int low, up, mid=0;
        
        if(n==0){
            queue.add(e);
        }else{
        
            low = 0;
            up = n-1;
            
            
            while(low < up){
                mid = (int)java.lang.Math.floor((low+up)/2);
                if(temp_time > queue.get(mid).getTime())
                    low = mid+1;
                else if(temp_time < queue.get(mid).getTime())
                    up =  mid-1;
                else{
                    while(temp_time == queue.get(mid).getTime() && mid<(n-1)){
                        mid++;
                    }
                    low = mid;
                    up = mid;
                }
                
            }
            
            mid = (int)java.lang.Math.floor((low+up)/2);
            
            if(temp_time > queue.get(mid).getTime())
                    queue.insertElementAt(e,mid+1);
            else if(temp_time < queue.get(mid).getTime())
                    queue.insertElementAt(e,mid);
            else{
                
                if(temp_time_ini >= queue.get(mid).getTimeIni())
                    queue.insertElementAt(e,mid+1);
                else
                    queue.insertElementAt(e,mid);
                
            }
        }
    
        
    }
    
     public void InvalidateAllEventsFromID(int id, int tree){
    
        int i=0;
        /*int tam = queue.size();
        for(i=0;i<tam;i++){
            if(queue.get(i).getSender() == id && queue.get(i).getTree() == tree)
                queue.get(i).setInvalid();
        }*/
        
        
        for(i=0;i<queue.size();i++){
            if(queue.get(i).getSender() == id && queue.get(i).getTree() == tree){
                queue.remove(i);
                i--;
            }
        }
        
    }
    
     public void InvalidateAllEventsFromIDFromTimeT(int id, double t, int tree){
    
        int i=0;
        int tam = queue.size();
        for(i=0;i<queue.size();i++){
            if(queue.get(i).getSender() == id && queue.get(i).getTime() >= t  && queue.get(i).getTree() == tree){
                //queue.get(i).setInvalid();
                queue.remove(i);
                i--;
            }
        }
        
    }
     
     public void InvalidateAllEventsFromIDFromTimeTOfCodeC(int id, double t, int c, int tree){
    
        int i=0;
        int tam = queue.size();
        try{
            sem.acquire();
            for(i=0;i<queue.size();i++){
                if(queue.get(i).getSender() == id && queue.get(i).getTime() >= t && queue.get(i).getCode() == c  && queue.get(i).getTree() == tree){
                    //queue.get(i).setInvalid();
                    queue.remove(i);
                    i--;
                }
            }
             sem.release();
         }catch(Exception ex){ex.printStackTrace();}
        
    }
     
     public void InvalidateAllEventsFromIDFromTimeTOfTypeTy(int id, double t, int Ty, int tree){
      
        int i=0;
        int tam = queue.size();
        event_sim ev;
        try{
            sem.acquire();
            for(i=0;i<queue.size();i++){
                ev = queue.get(i);
                if(ev.getSender() == id && ev.getTime() >= t && ev.getType() == Ty  && queue.get(i).getTree() == tree){
                    //queue.get(i).setInvalid();
                    queue.remove(i);
                    i--;
                }
            }
             sem.release();
         }catch(Exception ex){ex.printStackTrace();}
         
    }
     
     public void InvalidateAllEventsFromIDFromTimeTOfTypeTyOfCodeC(int id, double t, int Ty, int c, int tree){
        
         int i=0;
        int tam = queue.size();
        event_sim ev;
        try{
            sem.acquire();
            for(i=0;i<queue.size();i++){
                ev = queue.get(i);
                if(ev.getSender() == id && ev.getTime() >= t && ev.getType() == Ty && ev.getCode() == c  && queue.get(i).getTree() == tree){
                    //queue.get(i).setInvalid();
                    queue.remove(i);
                    i--;
                }
            }
             sem.release();
         }catch(Exception ex){ex.printStackTrace();}
        
         
    }

     public void InvalidateRxAllEventsFromIDFromTimeTOfTypeTyOfCodeC(int id, double t, int Ty, int c, int tree){

         int i=0;
        int tam = queue.size();
        event_sim ev;
        try{
            sem.acquire();
            for(i=0;i<queue.size();i++){
                ev = queue.get(i);
                if(ev.getReceiver() == id && ev.getTime() >= t && ev.getType() == Ty && ev.getCode() == c  && queue.get(i).getTree() == tree){
                    //queue.get(i).setInvalid();
                    queue.remove(i);
                    i--;
                }
            }
             sem.release();
         }catch(Exception ex){ex.printStackTrace();}


    }

    public void clear(){
        queue.clear();
    }
   
}
