/*
 *This class provides an object to store data from a single bus name record
 *
 */

import java.io.*;

    public class Record{
    
        String name, status, reg_dt, canc_dt, renew_dt, state_num, state, abn;
        byte [] b_name, b_status, b_reg_dt, b_canc_dt, b_renew_dt, b_state_num, b_state, b_abn; 
        int byteLength;

        public Record(String[] args){
            //Store properties
            this.name = args[1]; 
            this.status = args[2]; //String 15 char max
            this.reg_dt = args[3];
            this.canc_dt = args[4];
            this.renew_dt = args[5];
            this.state_num = args[6];
            this.state = args[7];
            this.abn = args[8];

            //Store binary versions of each property
            this.b_name = name.getBytes(); 
            this.b_status = status.getBytes();
            this.b_reg_dt = reg_dt.getBytes();
            this.b_canc_dt = canc_dt.getBytes();
            this.b_renew_dt = renew_dt.getBytes();
            this.b_state_num = state_num.getBytes();
            this.b_state = state.getBytes();
            this.b_abn = abn.getBytes();
            
            //calculate bytelength
            this.byteLength = this.getByteArray().length;
       }   

        public void print(){
        
            String s = this.name + "\t" + this.status + "\t" + this.reg_dt + "\t" + this.canc_dt + "\t";
            s +=  this.renew_dt + "\t" + this.state_num + "\t" + this.state + "\t" + this.abn;

            System.out.println(s);
        }

        public byte[] getByteArray() {
           
            ByteArrayOutputStream o = new ByteArrayOutputStream();
 
            try{        
                    
                //o = new ByteArrayOutputStream();
                o.write(this.b_name);
                o.write(this.b_status);
                o.write(this.b_reg_dt);
                o.write(this.b_canc_dt);
                o.write(this.b_renew_dt);
                o.write(this.b_state_num);
                o.write(this.b_state);
                o.write(this.b_abn);
            } catch(Exception e){
                System.out.println(e.getMessage());
            }
            return o.toByteArray();
            
        }

    } 
