package fr.fms;

import java.util.Date;
import java.util.Scanner;

import fr.fms.business.IBankImpl;
import fr.fms.entities.Account;
import fr.fms.entities.Current;
import fr.fms.entities.Customer;
import fr.fms.entities.Saving;
import fr.fms.entities.Transaction;

public class MyBankApp {	
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m	";

	public static void main(String[] args) throws Exception {
		//représente l'activité de notre banque
		IBankImpl bankJob = new IBankImpl();

		Customer robert = new Customer(1, "dupont", "Robert", "robert.dupont@xmail.com");
		Customer julie = new Customer(2, "jolie", "julie", "julie.jolie@xmail.com");		
		Current firstAccount = new Current(100200300, new Date(), 1500, 200 , robert);
		Saving secondAccount = new Saving(200300400, new Date(), 2000, 5.5, julie);

		bankJob.addAccount(firstAccount);
		bankJob.addAccount(secondAccount);

		Scanner scanner = new Scanner(System.in);
		boolean done;
		String CustomerName;
		long accountNumber = 0;
		int customerAmount = 0;
		while(true) {
			done = false;
			try {
				System.out.println(ANSI_BLUE+"Saisissez un numéro de compte valide :"+ANSI_RESET);
				while(!scanner.hasNextLong()) scanner.next();
				accountNumber = scanner.nextLong();
				CustomerName = bankJob.consultAccount(accountNumber).getCustomer().getFirstName();
				System.out.println(ANSI_BLUE+"----- Bonjour " + CustomerName+" -----");
				while(!done) {
					try {
						System.out.println("Menu principal : ");
						System.out.println("1. Versement			4. Information sur ce compte");
						System.out.println("2. Retrait			5. Liste des opérations");
						System.out.println("3. Virement			6. Sortir"+ANSI_RESET);
						while(!scanner.hasNextInt()) scanner.next();
						int choice = scanner.nextInt();
						scanner.nextLine();
						switch (choice) {
						case 1:
							System.out.println(ANSI_BLUE+"\nQuel est le montant de votre versement ?"+ANSI_RESET);
							customerAmount = scanner.nextInt();
							bankJob.pay(bankJob.consultAccount(accountNumber).getAccountId(),customerAmount);
							break;
						case 2:
							System.out.println(ANSI_BLUE+"\nQuel est le montant de votre retait ?"+ANSI_RESET);
							customerAmount = scanner.nextInt();
							bankJob.withdraw(bankJob.consultAccount(accountNumber).getAccountId(),customerAmount);
							break;
						case 3:
							System.out.println(ANSI_BLUE+"\nQuel est le montant de votre virement ?"+ANSI_RESET);
							customerAmount = scanner.nextInt();
							System.out.println(ANSI_BLUE+"Saisissez un numéro de compte valide à créditer :"+ANSI_RESET);
							long accountNumber2 = scanner.nextLong();
							bankJob.withdraw(bankJob.consultAccount(accountNumber).getAccountId(),customerAmount);
							bankJob.pay(bankJob.consultAccount(accountNumber2).getAccountId(),customerAmount);
							break;
						case 4:
							System.out.println(bankJob.consultAccount(accountNumber));
							break;
						case 5:
							for(Transaction trans : bankJob.listTransactions(accountNumber))
								System.out.println(trans);
							break;
						case 6:
							done = true;
							System.out.println(ANSI_RED);
							System.out.println("Fin de la transaction.");
							System.out.print(ANSI_RESET);
							break;

						default:
							System.out.println(ANSI_RED);
							System.out.println("Choix invalide.");
							System.out.print(ANSI_RESET);
							break;
						}
					} catch (Exception e) {
						System.out.println(ANSI_RED + e.getMessage() + ANSI_RESET);
					}

				}
			} catch (Exception e) {
				System.out.println(ANSI_RED + e.getMessage() + ANSI_RESET);
			}
		}
	}
}
