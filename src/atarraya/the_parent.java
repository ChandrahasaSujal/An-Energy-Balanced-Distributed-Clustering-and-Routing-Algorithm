/*
 * the_parent.java
 *
 * Created on September 22, 2006, 11:34 AM
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

package atarraya;

import atarraya.element.node;
import atarraya.event.event_sim;


/**
 *
 * @author pedrow
 */
public interface the_parent {
    
    public void setNodePos(int x, int y, int n);
    public boolean verifpa(int i, int tree);
    public boolean verifTree(int i,int tree);
    public void showMessage(String t);
    public void showNodeInfo(int id);
    
    public node getNode(int n);
    
    public void pushEvent(event_sim e);
    
    
    public void frame_repaint();
    
    public int getTreeID();
    
    public void UpdateStats(String stats);
    
    public boolean checkSimFinish();
    
    public void broadcast(double final_time, double current_time, int _source, int _final_destination, int _sender, int _destination, int _code, String _data, int _tree, int type);

    
    public int getRandomActiveNodeID(int tree);
}
