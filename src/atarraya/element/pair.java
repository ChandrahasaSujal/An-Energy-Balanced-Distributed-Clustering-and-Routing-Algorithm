/*
 * pair.java
 *
 * Created on March 29, 2006, 2:39 PM
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

/**
 *
 * @author pedrow
 */
public class pair {
    
    int x;
    int y;
    
    /** Creates a new instance of pair */
    public pair(int _x, int _y) {
        x = _x;
        y = _y;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public void setX(int _x){
        x = _x;
    }
    
    public void setY(int _y){
        y = _y;
    }
    
    public void setXY(int _x, int _y){
        x = _x;
        y = _y;
    }
    
}
