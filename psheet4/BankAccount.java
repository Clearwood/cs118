/** BankAccount.java
 *
 * Write an abstract class called BankAccount that contains the following methods:
 * withdrawMoney
 * getAccountBalance        (Get the overall balance of the account (may be negative))
 * getAvailableBalance      (Get the amount of money available to use (may include an overdraft))
 * transferMoney
 * depositMoney
 *
 * Some of the methods may be abstract, some should be implemented.
 */
public abstract class BankAccount{
        protected long balance;
        public abstract void withdrawMoney(long sum);
        public abstract void transferMoney(long sum);
        public void depositMoney(long addition){
        balance = balance + addition;
        };
        public long getAccountBalance(){
            System.out.println("The balance is: " + balance);
            return balance;
        }
        public abstract long getAvailableBalance();
}