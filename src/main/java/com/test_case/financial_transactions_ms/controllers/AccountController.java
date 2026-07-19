package com.test_case.financial_transactions_ms.controllers;

import com.test_case.financial_transactions_ms.dtos.AccountDTO;
import com.test_case.financial_transactions_ms.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("{externalId}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable("externalId") String externalId) {
        return ResponseEntity.ok(accountService.findByExternalId(externalId).toDTO());
    }

    @PostMapping
    public ResponseEntity<Void> createAccount(@Valid @RequestBody AccountDTO accountDTO) {
        var createdAccount = accountService.create(accountDTO.toEntity());
        return  ResponseEntity
                .created(URI.create("/v1/accounts/" + createdAccount.getExternalId()))
                .build();
    }

}
