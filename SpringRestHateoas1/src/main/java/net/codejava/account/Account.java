package net.codejava.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name = "accounts")
public class Account extends RepresentationModel<Account> {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, unique = true, length = 20)
	private String accountNumber;
	
	private float balance;
	
	public Account() { }
	
	public Account(String accountNumber, float balance) {
		this.accountNumber = accountNumber;
		this.balance = balance;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}
	
	
}
