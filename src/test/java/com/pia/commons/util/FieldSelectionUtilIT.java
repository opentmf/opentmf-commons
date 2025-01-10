package com.pia.commons.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Abdullah Beker
 */
@Sql(scripts = "/db/insert-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class FieldSelectionUtilIT {

  private static final String API_URL = "/professor";

  private MockMvc mockMvc;

  @BeforeEach
  void setUp(WebApplicationContext webApplicationContext) {
    mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilters(new CharacterEncodingFilter("UTF-8"))
            .build();
  }

  @Test
  void testFieldsSelector_withNullFieldsParam_returnRootLevelFieldsWithDepthLevelOne()
      throws Exception {
    mockMvc
        .perform(get(API_URL).param("size", "1"))
        .andDo(print())
        .andExpectAll(
            status().isOk(),
            jsonPath("$.content.[*].id", everyItem(notNullValue())),
            jsonPath("$.content.[*].name", everyItem(notNullValue())),
            jsonPath("$.content.[*].surname", everyItem(notNullValue())),
            jsonPath("$.content.[*].createdOn", everyItem(notNullValue())),
            jsonPath("$.content.[*].student.[*].id", everyItem(notNullValue())),
            jsonPath("$.content.[*].student.[*].name", everyItem(notNullValue())),
            jsonPath("$.content.[*].student.[*].surname", everyItem(notNullValue())),
            jsonPath("$.content.[*].classroom.id", everyItem(notNullValue())),
            jsonPath("$.content.[*].classroom.buildingName", everyItem(notNullValue())),
            jsonPath("$.content.[*].classroom.buildingCode", everyItem(notNullValue())),
            jsonPath("$.content.[*].classroom.floor", everyItem(notNullValue())),
            jsonPath("$.content.[*].classroom.doorCode", everyItem(notNullValue())),
            jsonPath("$.content.[*].cars..id", everyItem(notNullValue())),
            jsonPath("$.content.[*].cars..brand", everyItem(notNullValue())),
            jsonPath("$.content.[*].cars..licensePlate", everyItem(notNullValue())),
            jsonPath("$.content.[*].courses.id", everyItem(notNullValue())),
            jsonPath("$.content.[*].courses.credits", everyItem(notNullValue())),
            jsonPath("$.content.[*].courses.name", everyItem(notNullValue())));
  }

  @Test
  void testFieldsSelector_withRootLevelFields_returnRootLevelFieldsOnly() throws Exception {
    mockMvc
        .perform(get(API_URL).param("size", "1").param("fields", "id,name,student.name"))
        .andDo(print())
        .andExpectAll(
            status().isOk(),
            jsonPath("$.content.[*].id", everyItem(notNullValue())),
            jsonPath("$.content.[*].name", everyItem(notNullValue())),
            jsonPath("$.content.[*].student.[*].id", hasSize(0)),
            jsonPath("$.content.[*].student.[*].name", everyItem(notNullValue())),
            jsonPath("$.content.[*].student.[*].surname", hasSize(0)),
            jsonPath("$.content.[*].classroom.id", hasSize(0)),
            jsonPath("$.content.[*].classroom.buildingName", hasSize(0)),
            jsonPath("$.content.[*].classroom.buildingCode", hasSize(0)),
            jsonPath("$.content.[*].classroom.floor", hasSize(0)),
            jsonPath("$.content.[*].classroom.doorCode", hasSize(0)),
            jsonPath("$.content.[*].cars..id", hasSize(0)),
            jsonPath("$.content.[*].cars..brand", hasSize(0)),
            jsonPath("$.content.[*].cars..licensePlate", hasSize(0)),
            jsonPath("$.content.[*].courses.id", hasSize(0)),
            jsonPath("$.content.[*].courses.credits", hasSize(0)),
            jsonPath("$.content.[*].courses.name", hasSize(0)));
  }
}
