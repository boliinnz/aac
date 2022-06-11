package net.contal.demo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import net.contal.demo.commands.CustomerAccountCommand;
import net.contal.demo.exceptions.BadRequestException;
import net.contal.demo.modal.BankTransaction;
import net.contal.demo.modal.CustomerAccount;
import net.contal.demo.services.NewBankService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author Bo Li
 */
@RestController
@RequestMapping("/api/v2/banks")
public class NewBankController {
    final Logger logger = LoggerFactory.getLogger(BankController.class);
    final NewBankService bankService;

    public NewBankController(NewBankService bankService) {
        this.bankService = bankService;
    }

    /**
     *  TODO call properiate method in dataService to create an bank account , return generated bank account number
     * @param command {firstName:"" , lastName:"" }
     * @return bank account number
     */
    @Operation(summary = "Create a new Account.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Create a new account successfully", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = CustomerAccount.class)))}),
            @ApiResponse(responseCode = "404", description = "Service not found", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid params", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal error", content = {@Content(mediaType = "application/json"
            )})})
    @RequestMapping(method = RequestMethod.POST,value = "/create")
    public long createBankAccount(@RequestBody CustomerAccountCommand command){
        logger.info("{}" ,command.toString());
        String message = null;
        // validate parameters
        if (StringUtils.isBlank(command.getFirstName()) || StringUtils.isBlank(command.getLastName())) {
            message = "Invalid params: firstName and lastName both cannot be blank";
        }
        if (command.getAccountBalance() < 0) {
            message = "Invalid params: account balance is invalid";
        }
        if (message != null) {
            logger.error(message);
            throw new BadRequestException(message);
        }

        return bankService.createAnAccount(
                CustomerAccount.builder()
                        .firstName(command.getFirstName())
                        .lastName(command.getLastName())
                        .accountBalance(command.getAccountBalance())
                        .build());
    }

    /**
     *TODO call related Method from Service class to do the process
     * @param command account search command
     */
    @Operation(summary = "Create a new Transaction.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Change a Transaction successfully", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = Boolean.class)))}),
            @ApiResponse(responseCode = "404", description = "Service not found", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid params", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal error", content = {@Content(mediaType = "application/json"
            )})})
    @RequestMapping(method = RequestMethod.POST,value = "/transaction")
    public boolean addTransaction(@RequestBody CustomerAccountCommand command){
        int accountNumber = command.getAccountNumber();
        double amount = command.getAmount();
        logger.info("Bank Account number is :{} , Transaction Amount {}",accountNumber,amount);
        try {
            return bankService.addTransactions(accountNumber, amount);
        } catch (NumberFormatException e){
            //TODO need to add an controller adviser
            throw new BadRequestException(String.format("Provided account number %s is invalid", accountNumber));
        }
    }

    /**
     * TODO call related Method from Service class to do the process
     * @param command account search command
     * @return balance
     */
    @Operation(summary = "Get balance by account number .")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get balance by account number", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = Double.class)))}),
            @ApiResponse(responseCode = "404", description = "Service not found", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid params", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal error", content = {@Content(mediaType = "application/json"
            )})})
    @RequestMapping(method = RequestMethod.POST, value = "/balance")
    public Double getBalance(@RequestBody CustomerAccountCommand command){
        int accountNumber = command.getAccountNumber();
        logger.info("Bank Account number is :{}",accountNumber);
        //TODO implement the rest
        try {
            return bankService.getBalance(accountNumber);
        } catch (NumberFormatException e){
            //TODO need to add an controller adviser
            throw new BadRequestException(String.format("Provided account number %s is invalid", accountNumber));
        }
    }

    /**
     * TODO call related Method from Service class to do the process
     * @param command account search command
     * @return balance
     */
    @Operation(summary = "Get balance by account number group by transaction date .")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get balance by account number.", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = Map.class)))}),
            @ApiResponse(responseCode = "404", description = "Service not found", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid params", content = {@Content(mediaType = "application/json"
            )})})
    @RequestMapping(method = RequestMethod.POST,value = "/date-balance")
    public Map<LocalDate,Double> getDateBalance(@RequestBody CustomerAccountCommand command) {
        int accountNumber = command.getAccountNumber();
        logger.info("Bank Account number is :{}",accountNumber);
        //TODO implement the rest
        try {
            return bankService.getDateBalance(accountNumber);
        } catch (NumberFormatException e){
            //TODO need to add an controller adviser
            throw new BadRequestException(String.format("Provided account number %s is invalid", accountNumber));
        }
    }

    /**
     * Retrieve account details by account number.
     *
     * @param command customer account command {"accountNumber": 123456]
     * @return account details
     */
    @Operation(summary = "Get account details by account number group by transaction date .")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get balance by account number.", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = CustomerAccount.class)))}),
            @ApiResponse(responseCode = "404", description = "Service not found", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid params", content = {@Content(mediaType = "application/json"
            )})})
    @RequestMapping(method = RequestMethod.POST,value = "/account")
    public CustomerAccount getCustomerAccount(@RequestBody CustomerAccountCommand command){
        Integer accountNumber = command.getAccountNumber();
        logger.info("Bank Account number is :{}", accountNumber);
        try {
            return bankService.findByAccountNumber(accountNumber);
        } catch (NumberFormatException e){
            throw new BadRequestException(String.format("Provided account number %s is invalid", accountNumber));
        }
    }

    /**
     * Retrieve last 10 transactions by account number
     *
     * @param command customer account command {"accountNumber": 123456]
     * @return account details
     */
    @Operation(summary = "Get last 10 transactions by account number.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get balance by account number.", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = BankTransaction.class)))}),
            @ApiResponse(responseCode = "404", description = "Service not found", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid params", content = {@Content(mediaType = "application/json"
            )})})
    @RequestMapping(method = RequestMethod.POST,value = "/account/last-ten-transaction")
    public List<BankTransaction> getLastTenTransactions(@RequestBody CustomerAccountCommand command){
        Integer accountNumber = command.getAccountNumber();
        logger.info("Bank Account number is :{}", accountNumber);
        try {
            return bankService.getLastTenTransactions(accountNumber);
        } catch (NumberFormatException e){
            throw new BadRequestException(String.format("Provided account number %s is invalid", accountNumber));
        }
    }
}
