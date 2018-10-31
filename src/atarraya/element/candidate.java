/*
 * candidate.java
 *
 * Created on July 25, 2006, 11:31 AM
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

package atarraya.element;

import java.lang.Comparable;
import atarraya.constants;

/**
 *
 * @author pedrow
 */
public class candidate implements Comparable, constants{
    
    
    int ID;
    int address;
    int sortingmode;
    int state;
    int level;
    
    int index_metric;
    double metric_primary;
    double metric_secondary;
    double w1;
    double w2;
    
    /** Creates a new instance of candidate */
    public candidate(int _ID,  int _address, double _metric) {
        
        ID = _ID;
        address = _address;
        sortingmode=METRIC1_ONLY;
        state=0;
        level = 0;
        
        index_metric=0;
        metric_primary=_metric;
        metric_secondary=0.0;
        
        w1=0.5;
        w2=0.5;
    }
    
    public candidate(int _ID,  int _address, double _metric, int _sortingmode) {  
        this(_ID,_address, _metric);
        sortingmode=_sortingmode;
    }
    
    
    //If two metrics are going to be used, this is the constructor to be used.
    public candidate(int _ID,  int _address, int _sortingmode, int _indexmetric, double _metricprimary, double _metricsecondary, double _w1, double _w2) {  
        this(_ID,_address, 0,_sortingmode);
        index_metric=_indexmetric;
        metric_primary=_metricprimary;
        metric_secondary=_metricsecondary;
        w1 = _w1;
        w2 = _w2;
    }
    
    
    
    public candidate(int _ID,  int _address, double _metric, int _sortingmode, int _state) {
        
        this(_ID,_address, _metric,_sortingmode);
        state=_state;
    }
    
    public candidate(int _ID,  int _address, double _metric, int _sortingmode, int _state, int _level) {
        
        this(_ID,_address, _metric,_sortingmode,_state);
        level = _level;
    }
    
    
    
    public int getIndexMetric(){
        return index_metric;
    }
    
    public void setIndexMetric(int _index_metric){
        index_metric = _index_metric;
    }
    
    public double getPrimaryMetric(){
        return metric_primary;
    }
    
    public void setPrimaryMetric(double _primary_metric){
        metric_primary = _primary_metric;
    }
    
    public double getSecondaryMetric(){
        return metric_secondary;
    }
    
    public void setSecondaryMetric(double _secondary_metric){
        metric_secondary = _secondary_metric;
    }
    
    public double getLinearCombMetric(){
        return (w1*metric_primary+w2*metric_secondary)/(w1+w2);
    }
    
    
    public void setLevel(int l){
        level = l;
    }
    
    public int getLevel(){
        return level;
    }
    
    public void setState(int s){
        state = s;
    }
    
    public int getState(){
        return state;
    }
    
    public int getID(){
        return ID;
    }

    public int getAddress(){
        return address;
    }
    
    public double getMetric(){
        
        switch(getSortingMode()){

                case METRIC1_ONLY_INV:
                case METRIC1_ONLY:
                    return getPrimaryMetric();
                    
                case METRIC2_ONLY:
                    return getSecondaryMetric();
                    
                case METRIC1_PRIMARY:
                     return getPrimaryMetric();
                    
                case METRIC2_PRIMARY:
                     return getSecondaryMetric();
                    
                case LINEAR_COMB_1_2:
                    return getLinearCombMetric();
            
        }
        
        return -2000;
    
    }
    
    public int getSortingMode(){
        return sortingmode;
    }
    
    public void setMetric(double m){
        metric_primary = m;
    }
    
    public void setAddress(int a){
        address = a;
    }
    
    public void setSortingMode(int m){
        sortingmode = m;
    }
    
    public boolean equals(Object obj){
        
        if(obj instanceof candidate){
            candidate e = (candidate) obj;
            if(ID==e.getID() && address==e.getAddress() && metric_primary==e.getMetric() && metric_secondary==e.getSecondaryMetric() )
                return true;
        }
        
        return false;
    }
    
    
    public String toString(){
        return ""+ID+"-"+address+"-"+metric_primary+"-"+metric_secondary;
    }
    
    public String toString2(){
        return "ID: "+ID+"\n"
                +"Address: "+address+"\n"
                +"Metric Index: "+getIndexMetric()+"\n"
                +"Metric Primary: "+getPrimaryMetric()+"\n"
                +"Metric Secondary: "+getSecondaryMetric()+"\n"
                +"Metric LinearComb 1 and 2: "+getLinearCombMetric()+"\n"
                +"#\n";
    }
    
    public int compareTo(Object obj){
        
        //If the metrics are the same, then the IDs will solve be the differentiation factor
        //All sorting modes are design to work in Decreasing order
        double diff = 0;
        
        if(obj instanceof candidate){
            candidate e = (candidate) obj;
            
            switch(getSortingMode()){

                case METRIC1_ONLY_INV:
                    if(e.getPrimaryMetric() < getPrimaryMetric())
                        return 1;
                    else
                        if(e.getPrimaryMetric() > getPrimaryMetric())
                            return -1;
                        else
                            if(e.getID() < getID())
                                return 1;
                            else
                                if(e.getID() > getID())
                                    return -1;
                                else
                                    return 0;

                case METRIC1_ONLY:
                    if(e.getPrimaryMetric() > getPrimaryMetric())
                        return 1;
                    else
                        if(e.getPrimaryMetric() < getPrimaryMetric())
                            return -1;
                        else
                            if(e.getID() > getID())
                                return 1;
                            else
                                if(e.getID() < getID())
                                    return -1;
                                else
                                    return 0;
                    
                case METRIC2_ONLY:
                    if(e.getSecondaryMetric() > getSecondaryMetric())
                        return 1;
                    else
                        if(e.getSecondaryMetric() < getSecondaryMetric())
                            return -1;
                        else
                            if(e.getID() > getID())
                                return 1;
                            else
                                if(e.getID() < getID())
                                    return -1;
                                else
                                    return 0;
                    
                case METRIC1_PRIMARY:
                                    
                    if(e.getIndexMetric() > getIndexMetric())
                        return 1;
                    else
                        if(e.getIndexMetric() < getIndexMetric())
                            return -1;
                        else{
                             if(e.getSecondaryMetric() > getSecondaryMetric())
                                return 1;
                            else
                                if(e.getSecondaryMetric() < getSecondaryMetric())
                                    return -1;
                                else
                                    if(e.getID() > getID())
                                        return 1;
                                    else
                                        if(e.getID() < getID())
                                            return -1;
                                        else
                                            return 0;
                        }
                    
                    case METRIC2_PRIMARY:
                                    
                    if(e.getIndexMetric() > getIndexMetric())
                        return 1;
                    else
                        if(e.getIndexMetric() < getIndexMetric())
                            return -1;
                        else{
                             if(e.getPrimaryMetric() > getPrimaryMetric())
                                return 1;
                            else
                                if(e.getPrimaryMetric() < getPrimaryMetric())
                                    return -1;
                                else
                                    if(e.getID() > getID())
                                        return 1;
                                    else
                                        if(e.getID() < getID())
                                            return -1;
                                        else
                                            return 0;
                        }
                    
                    
                case LINEAR_COMB_1_2:
                    if(e.getLinearCombMetric() > getLinearCombMetric())
                        return 1;
                    else
                        if(e.getLinearCombMetric() < getLinearCombMetric())
                            return -1;
                        else
                            if(e.getID() > getID())
                                return 1;
                            else
                                if(e.getID() < getID())
                                    return -1;
                                else
                                    return 0;
                    
            }
            
        }
        
        return -2000;
    }
    
}
