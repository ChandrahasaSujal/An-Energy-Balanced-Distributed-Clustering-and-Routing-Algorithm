/*
 * BatchExecutor.java
 *
 * Created on March 4, 2007, 4:52 PM
 *
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
 *
 */

package atarraya;

import java.lang.Thread;
import java.util.concurrent.Semaphore;

/**
 *
 * @author HP_Owner
 */
public class BatchExecutor extends Thread implements constants{
    
    atarraya_frame papa;
    int work;
    boolean sw;
    Semaphore sem;
    
    String path_for_batch;
    int num_topos_for_batch;
    
    /** Creates a new instance of BatchExecutor */
    public BatchExecutor(atarraya_frame pa) {
        papa = pa;
        work = 0;
        sw=true;
        path_for_batch = "";
        sem = new Semaphore(0);
    }
    
    public void setWork(int _work){
        work = _work;
        sem.release();
    }
    
    public void setEnd(){
        sw=false;
    }
    
    public void setParamsForBatchCreation(int n,String path){
        path_for_batch = path;
        num_topos_for_batch = n;
    }
    
    public void run(){
    
        while(sw){
            
            try{
                    sem.acquire();
                }catch(Exception ex){ex.printStackTrace();}
            
            switch(work){
                case NO_WORK:
                        break;
                        
                case GENERATE_TOPOS:
                        papa.NodeGeneratorForbatch(num_topos_for_batch,path_for_batch);
                        work = NO_WORK;
                        break;
                        
                case SIMULATE:
                        papa.batchSimulation();
                        work = NO_WORK;
                        break;
            
                case GIANTCOMPONENT:
                        papa.GiantComponentTest();
                        work = NO_WORK;
                        break;

                case POSITIONFILES:
                        papa.CreatePositionFiles();
                        work = NO_WORK;
                    break;

                case OPTIMALSOLUTION:
                    papa.SolveCDS2();
                    work = NO_WORK;
                    break;

                case OPTIMALSOLUTIONBATCH:
                    papa.SolveBatchCDS();
                    work = NO_WORK;
                    break;
                        
            }
        
        }
        
    }
    
    
}
