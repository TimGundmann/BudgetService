package dk.gundmann.budget;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BudgetRestTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Test
    public void shouldReturnABudget() throws Exception {
    	// given 
        MockMultipartFile file = new MockMultipartFile("file", "bankfile.csv", "text/plain", "some line".getBytes());

    	// when then
        this.mockMvc.perform(MockMvcRequestBuilders
        		.multipart("/budget/make")
        		.file(file))
        	.andExpect(status().isOk())
        .andExpect(jsonPath("$").value(notNullValue()));
    }
    
}