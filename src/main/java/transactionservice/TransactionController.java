package transactionservice;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/transactionservice")
public class TransactionController {

    HashMap<Long, Transaction> transactions = new HashMap<>();
    private final Logger LOGGER = Logger.getLogger(TransactionController.class);

    //curl -X PUT -H "Content-Type: application/json" -d '{"amount":"5000","type":"cars"}' http://localhost:8080/transactionservice/transaction/1

    @PutMapping("/transaction/{transactionId}")
    public void putTransaction(@PathVariable(value = "transactionId") Long transaction_id, @RequestBody @Valid Transaction transaction) {
        transaction.setTransactionId(transaction_id);
        transactions.put(transaction_id, transaction);
        LOGGER.info("Print list of transactions : " + transactions.entrySet());
    }

    @GetMapping("/transaction/{transactionId}")
    public Transaction getTransactionById(@PathVariable(value = "transactionId") Long transactionId) {
        return transactions.get(transactionId);
    }

    @GetMapping("/types/{type}")
    public List<Long> getTransactionByType(@PathVariable(value = "type") String type) {
        List<Long> resultsByType = new ArrayList<>();
        for (Transaction value : transactions.values()) {
            if (type.equals(value.getType())) {
                resultsByType.add(value.getTransactionId());
            }
        }
        return resultsByType;
    }

    @GetMapping("/sum/{transactionId}")
    public String getSumByTransactionId(@PathVariable(value = "transactionId") Long transactionId) {
        Double sumResult = 0.0;
        for (Transaction value : transactions.values()) {
            if (transactionId.equals(value.getTransactionId()) || transactionId.equals(value.getParentId())) {
                sumResult += value.getAmount();
            }
        }
        return String.format("{\"sum\": %s}", sumResult);
    }

}
