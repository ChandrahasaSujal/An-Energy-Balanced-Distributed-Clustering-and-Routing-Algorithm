/*
 * TMConfStructure class
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

import atarraya.constants;

/**
 *
 * @author pedro
 */
public class TMConfStructure implements constants{

    routing_table the_table;
    int index;
    int TMState;
    
    public TMConfStructure(int _index){
    
        the_table = new routing_table();
        index = _index;
        TMState = TM_INACTIVE;
        the_table.setRoutingActive(true);
        
    }

    public routing_table getTMTable() {
        return the_table;
    }

    public int getTMState() {
        return TMState;
    }

    public void setTMState(int TMState) {
        this.TMState = TMState;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    
    
    
    
    
}
