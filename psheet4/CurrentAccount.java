/** CurrentAcount.java
 *
 * Write a subclass called CurrentAccount that does not accrue any interest.
 * It should have facilities for an overdraft and an associated PIN number (stored as an int).
 *
 * It should also contain the following additional functions:
 * authenticate
 * changePIN
 * setPIN
 * increaseOverdraft
 * decreaseOverdraft
 *
 */
public class CurrentAccount extends BankAccount{
    long overdraft = 0;
    int pin = 0;
    public long getAvailableBalance(){
            return balance + overdraft;
    }
    public void withdrawMoney(long sum){
        if(balance+overdraft >= sum){
            balance -= sum;
        }
    }
    public void transferMoney(long sum){
        if(balance+overdraft >= sum){
            balance -= sum;
        }
    }
    public void changePIN(int pin){
            this.pin = pin;
    }
    public void setPIN(int pin){
            this.pin = pin;
    }
    public void increaseOverdraft(long num){
        overdraft += num;
    }
    public void decreaseOverdraft(long num){
        overdraft -= num;
    }
    public boolean authenticate(int pin){
        if(pin == this.pin){
            return true;
        }
        return false;
    }
}