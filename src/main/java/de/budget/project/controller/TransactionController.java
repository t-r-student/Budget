package de.budget.project.controller;

import de.budget.project.exception.exceptions.CategoryTypeNotFoundException;
import de.budget.project.exception.exceptions.InputValidationException;
import de.budget.project.exception.exceptions.TransactionNotFoundException;
import de.budget.project.model.dao.TransactionDAO;
import de.budget.project.model.entites.Transaction;
import de.budget.project.model.web.TransactionWebRequest;
import de.budget.project.model.web.TransactionWebResponse;
import de.budget.project.services.TransactionService;
import org.hibernate.result.spi.ResultContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/transactions")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Long createTransaction(@RequestBody @Valid TransactionWebRequest transactionWebRequest, BindingResult result) {
        if(result.hasErrors()){
            throw new InputValidationException(result);
        }
        return transactionService.createTransaction(transactionWebRequest.getCustomDate(),
                transactionWebRequest.getAmount(),
                transactionWebRequest.getWalletId(),
                transactionWebRequest.getCategoryId(),
                transactionWebRequest.getDescription());
    }

    @GetMapping("/transactions/{id}")
    @ResponseBody
    public TransactionWebResponse getTransactionById(@PathVariable("id") Long id) {
        Transaction transaction = transactionService.getTransactionById(id);
        if (transaction == null){
            throw new TransactionNotFoundException("Transaction by id " + id + " is not found");
        } else
        return convertToWebResponse(transaction);
    }

    @GetMapping("/transactions/wallet/{id}")
    @ResponseBody
    public List<TransactionWebResponse> getTransactionsByWalletId(@PathVariable("id") Long walletId) {
        List<Transaction> transactions = transactionService.getTransactionsByWalletId(walletId);
        if(transactions.isEmpty()){
         throw new TransactionNotFoundException("There are no transactions made within wallet by id " + walletId);
        }
        return convertToListWebResponse(transactions);
    }

    @GetMapping("/transactions/user/{id}")
    @ResponseBody
    public List<TransactionDAO> getTransactionsByUserId(@PathVariable("id") Long id) {
        List<TransactionDAO> transactionDAOS = transactionService.getTransactionsByUserId(id);
        if(transactionDAOS.isEmpty()){
            throw new TransactionNotFoundException("There are no transactions belongs to user by id " + id);
        } else
        return transactionDAOS;
    }

    private TransactionWebResponse convertToWebResponse(Transaction transaction) {
        TransactionWebResponse transactionWeBResponse = new TransactionWebResponse();
        transactionWeBResponse.setAmount(transaction.getAmount());
        transactionWeBResponse.setCategoryName(transaction.getCategory().getName());
        transactionWeBResponse.setDescription(transaction.getDescription());
        transactionWeBResponse.setBalance(transactionService.recalculateBalance(transaction.getWallet().getId()));
        return transactionWeBResponse;
    }

    private List<TransactionWebResponse> convertToListWebResponse(List<Transaction> transactions) {
        return transactions
                .stream()
                .map(this::convertToWebResponse)
                .collect(Collectors.toList());
    }
}