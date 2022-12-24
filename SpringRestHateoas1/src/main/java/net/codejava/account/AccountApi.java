package net.codejava.account;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountApi {
	
	private AccountService service;
	
	public AccountApi(AccountService service) {
		this.service = service;
	}	
	
	@GetMapping
	public CollectionModel<Account> listAll() {
		List<Account> listAccounts = service.listAll();
		
		for (Account account : listAccounts) {
			account.add(linkTo(methodOn(AccountApi.class).getOne(account.getId())).withSelfRel());
		}
		
		CollectionModel<Account> collectionModel = CollectionModel.of(listAccounts);
		collectionModel.add(linkTo(methodOn(AccountApi.class).listAll()).withSelfRel());
		
		return collectionModel;
	}
	
	@GetMapping("/{id}")
	public HttpEntity<Account> getOne(@PathVariable("id") Integer id) {
		try {
			Account account = service.get(id);
			
			account.add(linkTo(methodOn(AccountApi.class).getOne(id)).withSelfRel());
			account.add(linkTo(methodOn(AccountApi.class).listAll()).withRel(IanaLinkRelations.COLLECTION));
			
			return new ResponseEntity<>(account, HttpStatus.OK);
		} catch (NoSuchElementException ex) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping
	public HttpEntity<Account> add(@RequestBody Account account) {
		Account savedAccount = service.save(account);
		
		account.add(linkTo(methodOn(AccountApi.class)
					.getOne(savedAccount.getId())).withSelfRel());
		
		account.add(linkTo(methodOn(AccountApi.class)
					.listAll()).withRel(IanaLinkRelations.COLLECTION));
		
		return ResponseEntity.created(linkTo(methodOn(AccountApi.class)
					.getOne(savedAccount.getId())).toUri()).body(savedAccount);		
	}
	
	@PutMapping
	public HttpEntity<Account> replace(@RequestBody Account account) {
		Account updatedAccount = service.save(account);
		
		updatedAccount.add(linkTo(methodOn(AccountApi.class)
					.getOne(updatedAccount.getId())).withSelfRel());
		
		updatedAccount.add(linkTo(methodOn(AccountApi.class)
					.listAll()).withRel(IanaLinkRelations.COLLECTION));
		
		return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
	}
	
	@PatchMapping("/{id}/deposits")
	public HttpEntity<Account> deposit(@PathVariable("id") Integer id, @RequestBody Amount amount) {
		Account updatedAccount = service.deposit(amount.getAmount(), id);
		updatedAccount.add(linkTo(methodOn(AccountApi.class)
				.getOne(updatedAccount.getId())).withSelfRel());
	
		updatedAccount.add(linkTo(methodOn(AccountApi.class)
					.listAll()).withRel(IanaLinkRelations.COLLECTION));
		
		return new ResponseEntity<>(updatedAccount, HttpStatus.OK);		
	}
	
	@PatchMapping("/{id}/withdrawal")
	public HttpEntity<Account> withdraw(@PathVariable("id") Integer id, @RequestBody Amount amount) {
		Account updatedAccount = service.withdraw(amount.getAmount(), id);
		updatedAccount.add(linkTo(methodOn(AccountApi.class)
				.getOne(updatedAccount.getId())).withSelfRel());
	
		updatedAccount.add(linkTo(methodOn(AccountApi.class)
					.listAll()).withRel(IanaLinkRelations.COLLECTION));
		
		return new ResponseEntity<>(updatedAccount, HttpStatus.OK);		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}	
}
