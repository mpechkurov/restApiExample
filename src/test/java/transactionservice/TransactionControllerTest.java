package transactionservice;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class TransactionControllerTest {

    @Autowired
    private TransactionController transactionController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private String transactionBody;
    private Transaction transact;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        transact = new Transaction();
        transact.setTransactionId(1L);
        transact.setAmount(5000.0);
        transact.setType("cars");
    }

    @Test
    public void testPutTransaction_shouldCreateTransactionWithoutParentId() throws Exception {
        transactionBody = new JSONObject().put("amount", "5000.0").put("type", "cars").toString();
        mockMvc.perform(put("/transactionservice/transaction/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(transactionBody)).andExpect(status().isOk());
        assertEquals(transactionController.transactions.size(), 1);
        assertEquals(transactionController.transactions.values().iterator().next(), transact);
    }

    @Test
    public void testPutTransaction_shouldCreateTransactionWithParentId() throws Exception {
        transactionBody = new JSONObject().put("amount", "5000.0").put("type", "cars").put("parentId", 2).toString();
        transact.setParentId(2L);
        mockMvc.perform(put("/transactionservice/transaction/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(transactionBody)).andExpect(status().isOk());
        assertEquals(transactionController.transactions.size(), 1);
        assertEquals(transactionController.transactions.values().iterator().next(), transact);
    }


    @Test
    public void testPutTransaction_throwBadRequest() throws Exception {
        mockMvc.perform(put("/transactionservice/transaction/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content("")).andExpect(status().isBadRequest());
    }

    @Test
    public void testGetTransactionByType_shouldReturnEmptyArray() throws Exception {
        mockMvc.perform(get("/transactionservice/types/notExist"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void testGetTransactionByType_shouldReturnSingleValue() throws Exception {
        transact.setType("exist");
        transactionController.transactions.put(1L, transact);
        mockMvc.perform(get("/transactionservice/types/exist"))
                .andExpect(status().isOk())
                .andExpect(content().string("[1]"));
    }

    @Test
    public void testGetTransactionByType_shouldReturnMultipleValues() throws Exception {
        transact.setType("exist");
        transact.setAmount(200.0);
        transactionController.transactions.put(1L, transact);
        Transaction transact2 = new Transaction();
        transact2.setTransactionId(2L);
        transact2.setType("exist");
        transact2.setAmount(400.0);
        transactionController.transactions.put(2L, transact2);
        Transaction transact3 = new Transaction();
        transact3.setTransactionId(3L);
        transact3.setType("noexist");
        transact3.setAmount(400.0);
        transactionController.transactions.put(3L, transact3);
        mockMvc.perform(get("/transactionservice/types/exist"))
                .andExpect(status().isOk())
                .andExpect(content().string("[1,2]"));
    }

    @Test
    public void testGetSumByTransaction_shouldReturnEmptyValue() throws Exception {
        transact.setType("exist");
        transact.setAmount(200.0);
        transactionController.transactions.put(1L, transact);
        mockMvc.perform(get("/transactionservice/sum/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"sum\": 0.0}"));
    }

    @Test
    public void testGetSumByTransaction_shouldReturnCorrectValue() throws Exception {
        transact.setTransactionId(1L);
        transact.setType("exist");
        transact.setAmount(200.0);
        transactionController.transactions.put(1L, transact);
        mockMvc.perform(get("/transactionservice/sum/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"sum\": 200.0}"));
    }

    @Test
    public void testGetSumByTransaction_shouldReturnCorrectSum() throws Exception {
        transact.setTransactionId(1L);
        transact.setType("exist");
        transact.setAmount(200.0);
        transactionController.transactions.put(1L, transact);
        Transaction tr2 = new Transaction();
        tr2.setTransactionId(2L);
        tr2.setType("exist");
        tr2.setAmount(400.0);
        tr2.setParentId(1L);
        transactionController.transactions.put(2L, tr2);
        mockMvc.perform(get("/transactionservice/sum/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"sum\": 600.0}"));
    }

    @Configuration
    @SpringBootApplication
    public static class Application {
    }

}