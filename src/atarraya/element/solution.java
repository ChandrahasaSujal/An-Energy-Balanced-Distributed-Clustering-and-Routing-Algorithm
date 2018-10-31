/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package atarraya.element;

import java.util.Vector;

/**
 *
 * @author Pedro
 */
public class solution {

    Vector<Integer> the_vector;

    public solution(int size){
        the_vector = new Vector<Integer>(size);

        for(int i=0;i<size-1;i++)
            the_vector.add(0);

        the_vector.add(1);

    }

    public solution(solution sol){

        int tam = sol.getSize();
        the_vector = new Vector<Integer>(tam);

        for(int i=0;i<tam;i++)
            the_vector.add(sol.get(i));

    }

    public int get(int i){
        return the_vector.get(i);
    }

    public int getSize(){
        return the_vector.size();
    }

    public void set(int i, int val){
        the_vector.set(i,val);
    }

    public void clear(){
        for(int i=0;i<the_vector.size()-1;i++)
            the_vector.set(i,0);
    }

    public int getSum(){
    int count=0;
        for(int i=0; i<the_vector.size();i++)
            count+=the_vector.get(i); // Values of 0 and 1 only

    return count;
    }

    public String toString(){

        String temp="";
        int i;

        for(i=0; i<the_vector.size();i++){
            temp = temp+the_vector.get(i)+",";
        }

        return temp;
    }

}
