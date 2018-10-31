/*
 * edge.java
 *
 * Created on March 19, 2007, 12:19 PM
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

/**
 *
 * @author Pedro
 */

    public class edge implements Comparable{
    
    int x;
    int y;
    double z;
    
    /** Creates a new instance of pair */
    public edge(int _x, int _y, double _z) {
        x = _x;
        y = _y;
        z = _z;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public double getZ(){
        return z;
    }
    
    public void setX(int _x){
        x = _x;
    }
    
    public void setY(int _y){
        y = _y;
    }
    
    public void setZ(int _z){
        z = _z;
    }
    
    public void setXYZ(int _x, int _y, double _z){
        x = _x;
        y = _y;
        z = _z;
    }
    
    public boolean equals(Object obj){
    
        if(obj instanceof edge){
            edge temp = (edge)obj;
            if(x==temp.getX() && y==temp.getY() && z==temp.getZ())
                return true;
        }
        
        return false;
    }
    
    public int compareTo(Object obj){
        
        if(obj instanceof edge){
            edge e = (edge) obj;
            return (int)java.lang.Math.round(getZ()-e.getZ());
        }
        
        return -2000;
    }
    
}
