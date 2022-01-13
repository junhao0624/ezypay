package com.test.ezypay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.ezypay.constant.SubscriptionConstants;
import com.test.ezypay.model.SubscriptionRequest;
import com.test.ezypay.repository.SubscriptionRepo;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;


import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class RestApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	SubscriptionRepo subscriptionRepo;

	private static ObjectMapper objectMapper = new ObjectMapper();

	@Before()
	public void setup()
	{
		mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void testDailySubscription () throws Exception {

		SubscriptionRequest request = new SubscriptionRequest();
		request.setIdNumber(Long.valueOf(1));
		request.setSubType(SubscriptionConstants.SubscriptionType.DAILY.name());
		request.setSubInput("20");
		request.setSubPrice(BigDecimal.valueOf(10.50));
		request.setStartDate("11/01/2022");
		request.setEndDate("15/01/2022");

		Mockito.when(subscriptionRepo.save(request)).thenReturn(request);

		mockMvc.perform(post("/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.invoiceDate", hasSize(5)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.invoiceDate",
				containsInAnyOrder("11/01/2022", "12/01/2022","13/01/2022","14/01/2022","15/01/2022")));
	}

	@Test
	void testWeeklySubscription () throws Exception {
		SubscriptionRequest request = new SubscriptionRequest();
		request.setSubType(SubscriptionConstants.SubscriptionType.WEEKLY.name());
		request.setSubInput("TUESDAY");
		request.setSubPrice(BigDecimal.valueOf(15.50));
		request.setStartDate("06/01/2022");
		request.setEndDate("30/01/2022");

		Mockito.when(subscriptionRepo.save(request)).thenReturn(request);

		mockMvc.perform(post("/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.jsonPath("$.invoiceDate", hasSize(3)))
						.andExpect(MockMvcResultMatchers.jsonPath("$.invoiceDate",
						containsInAnyOrder("11/01/2022", "18/01/2022","25/01/2022")));

	}

	@Test
	void testMonthlySubscription () throws Exception {
		SubscriptionRequest request = new SubscriptionRequest();
		request.setSubType(SubscriptionConstants.SubscriptionType.MONTHLY.name());
		request.setSubInput("20");
		request.setSubPrice(BigDecimal.valueOf(10.50));
		request.setStartDate("11/01/2022");
		request.setEndDate("30/03/2022");

		Mockito.when(subscriptionRepo.save(request)).thenReturn(request);

		mockMvc.perform(post("/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.invoiceDate", hasSize(3)))
				.andExpect(jsonPath("$.invoiceDate",
						containsInAnyOrder("20/01/2022", "20/02/2022","20/03/2022")));
	}

	@Test
	void testInvalidSubscriptionType () throws Exception {
		SubscriptionRequest request = new SubscriptionRequest();
		request.setSubType("UNKNOWN");
		request.setSubInput("20");
		request.setSubPrice(BigDecimal.valueOf(10.50));
		request.setStartDate("11/01/2022");
		request.setEndDate("30/03/2022");

		Mockito.when(subscriptionRepo.save(request)).thenReturn(request);

		MockHttpServletRequestBuilder mockRequest = post("/create")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(request));

		mockMvc.perform(mockRequest)
				.andExpect(status().isBadRequest());
	}

	@Test
	void testStartDateGreaterThanEndDate () throws Exception {
		SubscriptionRequest request = new SubscriptionRequest();
		request.setSubType(SubscriptionConstants.SubscriptionType.DAILY.name());
		request.setSubInput("20");
		request.setSubPrice(BigDecimal.valueOf(10.50));
		request.setStartDate("30/01/2022");
		request.setEndDate("14/01/2022");

		Mockito.when(subscriptionRepo.save(request)).thenReturn(request);

		MockHttpServletRequestBuilder mockRequest = post("/create")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(request));

		mockMvc.perform(mockRequest)
				.andExpect(status().isBadRequest());

	}

	@Test
	void testDateGreaterThanMaxDayRange () throws Exception {
		SubscriptionRequest request = new SubscriptionRequest();
		request.setSubType(SubscriptionConstants.SubscriptionType.DAILY.name());
		request.setSubInput("20");
		request.setSubPrice(BigDecimal.valueOf(10.50));
		request.setStartDate("11/01/2022");
		request.setEndDate("30/06/2022");

		Mockito.when(subscriptionRepo.save(request)).thenReturn(request);

		MockHttpServletRequestBuilder mockRequest = post("/create")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(request));

		mockMvc.perform(mockRequest)
				.andExpect(status().isBadRequest());

	}
}
