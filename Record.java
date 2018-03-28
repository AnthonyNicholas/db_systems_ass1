/*
 *This class provides an object to store data from a single bus name record
 *
 */


    public class Record{
    
        String name, status, reg_dt, canc_dt, renew_dt, state_num, state, abn;
 
        public Record(String[] args){
            this.name = args[1]; 
            this.status = args[2]; //String 15 char max
            this.reg_dt = args[3];
            this.canc_dt = args[4];
            this.renew_dt = args[5];
            this.state_num = args[6];
            this.state = args[7];
            this.abn = args[8];
        }   

        public void print(){
        
            String s = this.name + "\t" + this.status + "\t" + this.reg_dt + "\t" + this.canc_dt + "\t";
            s +=  this.renew_dt + "\t" + this.state_num + "\t" + this.state + "\t" + this.abn;

            System.out.println(s);
        }


    } 
