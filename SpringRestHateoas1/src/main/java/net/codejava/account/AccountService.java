package net.codejava.account;

import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {
	private AccountRepository repo;

	public AccountService(AccountRepository repo) {
		this.repo = repo;
	}
	
	List<Account> listAll() {
		return repo.findAll();
	}
	
	Account get(Integer id) {
		return repo.findById(id).get();
	}
	
	Account save(Account account) {
		return repo.save(account);
	}
	
	Account deposit(float amount, Integer id) {
		repo.deposit(amount, id);
		return repo.findById(id).get();
	}
	
	Account withdraw(float amount, Integer id) {
		repo.withdraw(amount, id);
		return repo.findById(id).get();
	}	
	
	void delete(Integer id) {
		repo.deleteById(id);
	}
}
