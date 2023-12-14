package com.ecommerce.myapp;

import com.ecommerce.myapp.services.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class MyappApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    // Test the createUser API
    @Test
    @Transactional
    void createUser() throws Exception {
        mockMvc.perform(post("/api/v1/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"testuser\", \"password\":\"password123\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("User created successfully"));
    }

    // Test the getUserById API
    @Test
    @Transactional
    void getUserById() throws Exception {
        int userId = 1;
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User found")))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println(responseContent);
    }

    // Test the getAllUser API
    @Test
    @Transactional
    void getAllUser() throws Exception {
        mockMvc.perform(get("/api/v1/user/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    // Test the updateUser API
    @Test
    @Transactional
    void updateUser() throws Exception {
        int userId = 1;
        mockMvc.perform(put("/api/v1/user/update/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"updatedUser\", \"password\":\"newPassword123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User updated successfully"));
    }

    // Test the deleteUser API
    @Test
    @Transactional
    void deleteUser() throws Exception {
        int userId = 1;
        mockMvc.perform(delete("/api/v1/user/delete/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }
}
