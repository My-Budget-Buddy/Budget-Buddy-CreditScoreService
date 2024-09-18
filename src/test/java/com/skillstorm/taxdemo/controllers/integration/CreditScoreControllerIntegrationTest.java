package com.skillstorm.taxdemo.controllers.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.taxdemo.models.CreditScoreHistory;
import com.skillstorm.taxdemo.models.UserCreditData;
import com.skillstorm.taxdemo.repositories.UserCreditDataRepository;
import com.skillstorm.taxdemo.services.CreditScoreService;


@SpringBootTest
@AutoConfigureMockMvc
public class CreditScoreControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreditScoreService creditScoreService;

    @MockBean
    private UserCreditDataRepository userCreditDataRepository;

    /*
     * Expect the status below with the header User-ID provided and a valid user id
     * returns "status": 200
     */
    @Test
    public void testGetHistory() throws Exception {      

    // Arrange
    List<CreditScoreHistory> actual = new ArrayList<>();
    Long id = 1L;
    Long userId = 1L;
    int score = 700;
    LocalDateTime current = LocalDateTime.now();
    actual.add(new CreditScoreHistory(id, userId,score, current));

    when(creditScoreService.getCreditScoreHistory(anyLong())).thenReturn(actual);
    // Act and Assert
    mockMvc.perform(get("/api/credit/history")
        .header("User-ID", 1L) 
        .contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(actual.size())))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].userId", is(1)))
        .andExpect(jsonPath("$[0].score", is(score)));
    }

    /*
     * If a Header User-ID is provided will return a 200 status.
     */
    @Test
    public void testGetHistoryWithNoValues() throws Exception {      
    
    // Arrange
    List<CreditScoreHistory> history = new ArrayList<>();
    when(creditScoreService.getCreditScoreHistory(anyLong())).thenReturn(history);
    // Act and Assert
    mockMvc.perform(get("/api/credit/history")
        .header("User-ID", 1L)
        .contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)))
        .andExpect(jsonPath("$", is(history)));
    }

    /*
     * If the header User-id is not provided you can expect the status and error below
     * "status": 400, "error": "Bad Request"
     */
    @Test
    public void testGetHistoryWithNoHeaderValue() throws Exception {   

    // Arrange
    List<CreditScoreHistory> history = new ArrayList<>();
    when(creditScoreService.getCreditScoreHistory(anyLong())).thenReturn(history);
    // Act and Assert
    mockMvc.perform(get("/api/credit/history")
        .contentType("application/json"))
        .andExpect(status().isBadRequest());
    }

    /*
     * If a Header User-ID and valid userID is provided will return a 200 status.
     */
    @Test
    public void testGetCreditImprovementTips()throws Exception{

        // Arrange
        Long userId = 1L;
        String tip ="Your credit profile looks good! Keep up the good work.";
        List<String> tips = new ArrayList<>();
        tips.add(tip);
        when(creditScoreService.getCreditImprovementTips(anyLong())).thenReturn(tips);

        // Act and Assert
        mockMvc.perform(get("/api/credit/tips")
            .header("User-ID", userId)
            .contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0]", is(tip)));
    }

    /*  
     * If no User-ID header is provided, the status will be 400 Bad Request
     */
    @Test
    public void testGetCreditImprovementTipsWithNoHeaderValue() throws Exception {

        // Arrange
        List<String> tips = new ArrayList<>();
        when(creditScoreService.getCreditImprovementTips(anyLong())).thenReturn(tips);
        // Act and Assert
        mockMvc.perform(get("/api/credit/tips")
            .contentType("application/json"))
            .andExpect(status().isBadRequest());
    }

    /*
     * If a Header User-ID is provided will return a 200 status.
     */
    @Test
    public void testGetCreditReport() throws Exception {

        // Arrange
        String report = "Credit Report";
        when(creditScoreService.generateCreditReport(anyLong())).thenReturn(report);
        // Act and Assert
        mockMvc.perform(get("/api/credit/report")
            .header("User-ID", 1L)
            .contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is(report)));
    }

    /*
     * If no User-ID header is provided, the status will be 400 Bad Request
     */
    @Test
    public void testGetCreditReportNoHeaderValue() throws Exception {

        // Arrange
        String report = "Credit Report";
        when(creditScoreService.generateCreditReport(anyLong())).thenReturn(report);
        // Act and Assert
        mockMvc.perform(get("/api/credit/report")
            .contentType("application/json"))
            .andExpect(status().isBadRequest());
    }

    /*
     * If a Header User-ID is provided will return a 200 status.
     * this should be status.isCreated() instead of isOk() returning 201 status code instead of 200
     * but the controller class explicitly returns 200 status code isOk()
     */
    @Test
    public void testPostSaveCreditData() throws Exception {
        
        // Arrange
        UserCreditData userData = new UserCreditData(2L,2L,50,0,
        0,0,25.0, 
        30_000.0, 5, new ArrayList<>(),
         0, 0);
        var jsonResponseBody = objectMapper.writeValueAsString(userData);
        
        when(userCreditDataRepository.save(any(UserCreditData.class))).thenReturn(userData);
        // Act and Assert
        mockMvc.perform(post("/api/credit/data")
            .content(jsonResponseBody)
            .contentType(MediaType.APPLICATION_JSON))                          
            .andExpect(status().isOk())                                     
            .andExpect(jsonPath("$.id", is(2)))            
            .andExpect(jsonPath("$.userId", is(2)))
            .andExpect(jsonPath("$.onTimePayments", is(50)))
            .andExpect(jsonPath("$.latePayments", is(0)))
            .andExpect(jsonPath("$.missedPayments", is(0)))
            .andExpect(jsonPath("$.publicRecords", is(0)))
            .andExpect(jsonPath("$.creditUtilization", is(25.0)))
            .andExpect(jsonPath("$.totalDebt", is(30_000.0)))
            .andExpect(jsonPath("$.oldestAccountAge", is(5)))
            .andExpect(jsonPath("$.recentInquiries", is(0)))
            .andExpect(jsonPath("$.newAccounts", is(0))); 
    }

    /*
     * Put request requires a header as well as a body to return a 200 status
     */
    @Test
    public void testPutSaveCreditData() throws Exception {
        
        // Arrange
        UserCreditData userData = new UserCreditData(2L,2L,50,0,
        0,0,25.0, 
        30_000.0, 5, new ArrayList<>(),
         0, 0);

        var jsonResponseBody = objectMapper.writeValueAsString(userData);
        when(userCreditDataRepository.findByUserId(anyLong())).thenReturn(Optional.of(userData));
        when(userCreditDataRepository.save(any(UserCreditData.class))).thenReturn(userData);
        // Act and Assert
        mockMvc.perform(put("/api/credit/data")
            .header("User-ID", 2L)
            .content(jsonResponseBody)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())                           
            .andExpect(status().isOk())                                     
            .andExpect(jsonPath("$.id", is(2)))            
            .andExpect(jsonPath("$.userId", is(2)))
            .andExpect(jsonPath("$.onTimePayments", is(50)))
            .andExpect(jsonPath("$.latePayments", is(0)))
            .andExpect(jsonPath("$.missedPayments", is(0)))
            .andExpect(jsonPath("$.publicRecords", is(0)))
            .andExpect(jsonPath("$.creditUtilization", is(25.0)))
            .andExpect(jsonPath("$.totalDebt", is(30_000.0)))
            .andExpect(jsonPath("$.oldestAccountAge", is(5)))
            .andExpect(jsonPath("$.recentInquiries", is(0)))
            .andExpect(jsonPath("$.newAccounts", is(0))); 
    }

    /* 
     * If no header is provided, the status will be 400 Bad Request
     */
    @Test
    public void testPutSaveCreditDataNoHeader() throws Exception {
        
        // Arrange
        UserCreditData userData = new UserCreditData(2L,2L,50,0,
        0,0,25.0, 
        30_000.0, 5, new ArrayList<>(),
         0, 0);

        var jsonResponseBody = objectMapper.writeValueAsString(userData);
        when(userCreditDataRepository.findByUserId(anyLong())).thenReturn(Optional.of(userData));
        when(userCreditDataRepository.save(any(UserCreditData.class))).thenReturn(userData);
        // Act and Assert
        mockMvc.perform(put("/api/credit/data")
            .content(jsonResponseBody)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())                           
            .andExpect(status().isBadRequest());                                
    }

    /*
     * This method actually has a bug in the code. It is a GET request but through service call it actually creates a POST
     * request using creditScoreHistoryRepository.save(anyData) in the CreditScoreService calculateFICOScore(Long userId) method.
     */
    @Test
    public void testGetCreditScore() throws Exception {
        // todo - currently a bug makes a post-request/save to the CreditScoreHistoryRepository,
        // todo - should be fixed before testing otherwise test will create a new record in the database
    }

}
