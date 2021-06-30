package com.digiwin.dap.middleware.dmc;

import com.fobgochod.auth.FobAuthenticationFilter;
import com.fobgochod.auth.handler.AuthService;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.util.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileUploadControllerTest {

    private final static ObjectMapper objectMapper = JsonUtils.createObjectMapper();

    @Autowired
    private AuthService authService;
    @Autowired
    private WebApplicationContext webContext;
    private MockMvc mockMvc;


    @Before
    public void setupMockMvc() {
        FobAuthenticationFilter authenticationFilter = new FobAuthenticationFilter(authService);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webContext)
                .addFilter(authenticationFilter, "/*")
                .build();
    }

    @Test
    public void uploadSegTest() throws Exception {
        String userToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkbWMiLCJkYXRhIjoie1wiaWRcIjpcIjVmYWJhMDU3ZDIwYzI5MDAwZmYzYjUwMlwiLFwidXNlcm5hbWVcIjpcInpob3V4eFwiLFwiaWFtXCI6ZmFsc2UsXCJidWNrZXRzXCI6W10sXCJhdXRob3JpdGllc1wiOlt7XCJyb2xlXCI6XCJST0xFX1N5c0FkbWluXCJ9XSxcImVuYWJsZWRcIjp0cnVlLFwiYWNjb3VudE5vbkV4cGlyZWRcIjp0cnVlLFwiY3JlZGVudGlhbHNOb25FeHBpcmVkXCI6dHJ1ZSxcImFjY291bnROb25Mb2NrZWRcIjp0cnVlfSIsImlzcyI6ImRpZ2l3aW4uZGFwLm1pZGRsZXdhcmUuZG1jLlVzZXJUb2tlbiIsImV4cCI6MTYxNTkwNDc1Nn0.GtP8V0XjiM1EC6zLO8zCbkVDodKzwMzct9y5DWQ6UzY";

        FileInfo fileInfo = new FileInfo();
        fileInfo.setName("test.docx");
        fileInfo.setDisplayName("测试文件描述");
        fileInfo.setTag("文件;测试");
        fileInfo.setExpireDate(LocalDateTime.now().plusDays(1));

        String json = objectMapper.writeValueAsString(fileInfo);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/dmc/v1/buckets/ddd/files/segment")
                .header("digi-middleware-auth-user", userToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        String jsonResult = mvcResult.getResponse().getContentAsString();
        Map<String, Object> result = objectMapper.readValue(jsonResult, new TypeReference<HashMap<String, Object>>() {
        });
        String fileId = result.get("id").toString();


        File file = ResourceUtils.getFile("classpath:static/test/test.docx");
        long length = file.length();
        long readLen = 0;
        int bufferSize = 1024 * 255;
        try (FileInputStream inputStream = new FileInputStream(file)) {
            while (readLen < length) {
                byte[] buffer = new byte[bufferSize];
                int currentReadLen = inputStream.read(buffer, 0, bufferSize);
                if (currentReadLen == -1) {
                    break;
                }
                if (currentReadLen + readLen > length) {
                    currentReadLen = (int) (length - readLen);
                }
                mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/dmc/v1/buckets/ddd/files/{0}/{1}/{2}/{3}", fileId, readLen, readLen + currentReadLen - 1, length)
                        .header("digi-middleware-auth-user", userToken)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .content(buffer)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(request().asyncStarted())
                        .andReturn();

                mockMvc.perform(asyncDispatch(mvcResult))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").exists())
                        .andReturn();

                readLen += currentReadLen;

                mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/dmc/v1/buckets/ddd/files/{0}/status", fileId)
                        .header("digi-middleware-auth-user", userToken)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.completedBytes").value(readLen))
                        .andExpect(jsonPath("$.totalBytes").value(length))
                        .andReturn();
            }
            java.io.File f1 = new java.io.File(System.getProperty("java.io.tmpdir") + "/test.docx");
            if (f1.exists()) {
                boolean delete = f1.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
