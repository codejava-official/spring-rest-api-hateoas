package net.codejava.account;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

	@Query("UPDATE Account a SET a.balance = a.balance + ?1 WHERE a.id = ?2")
	@Modifying
	@Transactional
	public void deposit(float amount, Integer id);
	
	@Query("UPDATE Account a SET a.balance = a.balance - ?1 WHERE a.id = ?2")
	@Modifying
	@Transactional
	public void withdraw(float amount, Integer id);	
}
