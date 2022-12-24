package net.codejava.account;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
	private AccountModelAssembler assembler;

	public AccountApi(AccountService service, AccountModelAssembler assembler) {
		this.service = service;
		this.assembler = assembler;
	}
	
	@PostMapping
	public HttpEntity<EntityModel<Account>> add(@RequestBody Account account) {
		Account savedAccount = service.save(account);
		EntityModel<Account> model = assembler.toModel(savedAccount);
		
		return ResponseEntity.created(linkTo(methodOn(AccountApi.class).getOne(savedAccount.getId())).toUri()).body(model);		
	}
	
	@PutMapping
	public HttpEntity<EntityModel<Account>> replace(@RequestBody Account account) {
		Account savedAccount = service.save(account);	
		return new ResponseEntity<>(assembler.toModel(savedAccount), HttpStatus.OK);		
	} 
	
	@PatchMapping("/{id}/deposit")
	public HttpEntity<EntityModel<Account>> deposit(@PathVariable("id") Integer id, @RequestBody Amount amount) {
		Account updatedAccount = service.deposit(amount.getAmount(), id);
		return new ResponseEntity<>(assembler.toModel(updatedAccount), HttpStatus.OK);	
	}

	@PatchMapping("/{id}/withdraw")
	public HttpEntity<EntityModel<Account>> withdraw(@PathVariable("id") Integer id, @RequestBody Amount amount) {
		Account updatedAccount = service.withdraw(amount.getAmount(), id);		
		return new ResponseEntity<>(assembler.toModel(updatedAccount), HttpStatus.OK);	
	}	
	
	@GetMapping
	public CollectionModel<EntityModel<Account>> getAll() {
		List<EntityModel<Account>> accounts = service.listAll().stream().map(assembler::toModel).collect(Collectors.toList());
		
		return CollectionModel.of(accounts,
				linkTo(methodOn(AccountApi.class).getAll()).withRel("accounts")
				);
	}
	
	@GetMapping("/{id}")
	public EntityModel<Account> getOne(@PathVariable("id") Integer id) {
		Account account = service.get(id);
		return assembler.toModel(account);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
