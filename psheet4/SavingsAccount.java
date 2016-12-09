/** SavingsAccount.java
 *
 * Finally, create a SavingsAccount subclass that can accumulate interest into
 * a separate variable. Each savings account will have an individual interest rate
 * and a function that will accrue one days worth of interest into the counter.
 * This function will be executed by the bank on every account daily.
 * Additionally there should be a function to add the interest onto the account balance
 * and zero the interest counter.
 * This function will be executed by the bank on a monthly basis.
 *
 * Specifically you should have the following additional functions:
 *
 * setInterestRate
 * getInterestRate
 * accrueDailyInterest
 * addInterest
 *
 * For example, with an annual interest rate of 2%, and a starting balance of £100,
 * each day, ((100 * 0.02) / 365) will accumulate into a counter. Every month, this
 * amount will be added to the account balance and the counter will be zeroed. For the
 * next month the amount added to the counter will be ((0.2 * 100.16) / 365) [since the
 * new account balance will include the 16p accumulated in the previous month].
 */
public class SavingsAccount extends BankAccount{
    int dailyInterest = 0;
    double Interest = 0.0;
    public void setInterestRate(double interest){
        Interest = interest;
    }
    public double getInterestRate(){
        return Interest;
    }
    public void accrueDailyInterest(){
        dailyInterest += (int) ((balance*Interest)/365);
    }
    public void addInterest(){
        balance += dailyInterest;
        dailyInterest = 0;
    }
    public void withdrawMoney(long sum){
        if(balance >= sum){
            balance -= sum;
        }
    }
    public void transferMoney(long sum){
        if(balance >= sum){
            balance -= sum;
        }
    }
    public long getAvailableBalance(){
        return getAccountBalance();
    }
}