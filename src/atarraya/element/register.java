/*
 * register.java
 *
 * Created on July 17, 2006, 3:41 PM
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
import java.util.Vector;
import atarraya.constants;

/**
 *
 * @author pedrow
 */   
    /** Creates a new instance of register */
    public class register implements Comparable, constants{
    
    int ID;
    int address;
    double distance;
    double energy;
    int mode;
    int state;
    int sortingmode;
    
    
    /** Creates a new instance of register */
    public register(int _ID, int _address, double _distance, double _energy) {
            ID = _ID;
            address = _address;
            distance = _distance;
            energy = _energy;
            mode = 0;
            state = BASIC;
            sortingmode=METRIC1_ONLY;
    }
    
    /** Creates a new instance of register */
    public register(int _ID, int _address, double _distance, double _energy, int _mode) {
            ID = _ID;
            address = _address;
            distance = _distance;
            energy = _energy;
            mode = _mode;
    }
    
    /** Creates a new instance of register */
    public register(int _ID, int _address, double _distance, double _energy, int _mode, int _sortingmode) {
            ID = _ID;
            address = _address;
            distance = _distance;
            energy = _energy;
            mode = _mode;
            sortingmode=_sortingmode;
    }
    
    public int getID(){
        return ID;
    }
    
    public int getAddress(){
        return address;
    }
    
    public double getDistance(){
        return distance;
    }

    public double getEnergy(){
        return energy;
    }

    public int getMode(){
        return mode;
    }
    
    public int getState(){
        return state;
    }
    
    public void setState(int s){
        state=s;
    }
    
    public boolean equals(Object obj){
        
        if(obj instanceof register){
            register temp = (register)obj;
            if(ID == temp.getID()){
                if(address == temp.getAddress()){
                    if(distance == temp.getDistance()){
                        if(energy == temp.getEnergy())
                            return true;
                    }
                }
            }
        
        }
        return false;
    }
    
    public String toString(){
        return ""+ID+"-"+address+"-"+getDistance()+"-"+getEnergy();
    }
    
     public String toString2(){
        return "ID: "+ID+"\n"
                +"Address: "+address+"\n"
                +"Distance: "+getDistance()+"\n"
                +"Energy: "+getEnergy()+"#\n";
    }
    
    public int compareTo(Object obj){
        if(obj instanceof register){
            register e = (register)obj;
            return (int)java.lang.Math.round(e.getDistance()-distance);
        }
            
        return -2000;
    }
    
}

    

