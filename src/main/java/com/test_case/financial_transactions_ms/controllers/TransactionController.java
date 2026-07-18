package com.test_case.financial_transactions_ms.controllers;

import com.test_case.financial_transactions_ms.dtos.TransactionDTO;
import com.test_case.financial_transactions_ms.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("{uuid}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable(name = "uuid") String uuid) {
        return ResponseEntity.ok(transactionService.findByUuid(uuid).toDTO());
    }

    @PostMapping
    public ResponseEntity<Void> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        var createdTransaction = transactionService.create(transactionDTO.toEntity());
        return  ResponseEntity
                .created(URI.create("v1/transactions/" + createdTransaction.getUuid()))
                .build();
    }
}
