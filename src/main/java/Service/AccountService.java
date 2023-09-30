package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {

    public static boolean authenticate(Account account){
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()
                || account.getPassword() == null) {
            return false;
        }
        if (AccountDAO.authenticateAcount(account.getUsername(), account.getPassword())){
            return true;
        }
        return false;
    }

    public static boolean registerUser(Account account) {
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()
                || account.getPassword() == null || account.getPassword().length() < 5) {
            return false;
        }
        if (doesAccountExist(account)){
            return false;
        }

        return AccountDAO.registerUser(account);
    }

    public static boolean doesAccountExist(Account account){
        Account existingAccount = AccountDAO.getAccountByUsername(account.getUsername());
        return existingAccount != null;
    }

    public static Account getAccountByUsername(Account account) {
        return AccountDAO.getAccountByUsername(account.getUsername());
    }

    public static Account getAccountById(Integer account_id) {
        return AccountDAO.getAccountById(account_id);
    }

}