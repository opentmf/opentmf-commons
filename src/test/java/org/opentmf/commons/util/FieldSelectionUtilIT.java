package org.opentmf.commons.util;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

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
            jsonPath("$.content.[0].name", notNullValue()),
            jsonPath("$.content.[0].surname", notNullValue()),
            jsonPath("$.content.[0].createdOn", notNullValue()),
            jsonPath("$.content.[0].student.[*]", hasSize(is(3))),
            jsonPath("$.content.[0].student.[*].id", everyItem(notNullValue())),
            jsonPath("$.content.[0].student.[*].name", everyItem(notNullValue())),
            jsonPath("$.content.[0].student.[*].surname", everyItem(notNullValue())),
            jsonPath("$.content.[0].classroom.id", notNullValue()),
            jsonPath("$.content.[0].classroom.buildingName", notNullValue()),
            jsonPath("$.content.[0].classroom.buildingCode", notNullValue()),
            jsonPath("$.content.[0].classroom.floor", notNullValue()),
            jsonPath("$.content.[0].classroom.doorCode", notNullValue()),
            jsonPath("$.content.[0].cars..id", everyItem(notNullValue())),
            jsonPath("$.content.[0].cars..brand", everyItem(notNullValue())),
            jsonPath("$.content.[0].cars..licensePlate", everyItem(notNullValue())),
            jsonPath("$.content.[0].courses.[*].id", everyItem(notNullValue())),
            jsonPath("$.content.[0].courses.[*].credits", everyItem(notNullValue())),
            jsonPath("$.content.[0].courses.[*].name", everyItem(notNullValue())));
  }

  @Test
  void testFieldsSelector_withRootLevelFields_returnRootLevelFieldsOnly() throws Exception {
    mockMvc
        .perform(get(API_URL).param("size", "1").param("fields", "name,student.name"))
        .andDo(print())
        .andExpectAll(
            status().isOk(),
            jsonPath("$.content.[0].name", notNullValue()),
            jsonPath("$.content.[0].student.[*]", hasSize(is(3))),
            jsonPath("$.content.[0].student.[*].id", hasSize(0)),
            jsonPath("$.content.[0].student.[*].name", hasSize(is(3))),
            jsonPath("$.content.[0].student.[*].name", everyItem(notNullValue())),
            jsonPath("$.content.[0].student.[*].surname", hasSize(0)),
            jsonPath("$.content.[0].classroom.id").doesNotExist(),
            jsonPath("$.content.[0].classroom.buildingName").doesNotExist(),
            jsonPath("$.content.[0].classroom.buildingCode").doesNotExist(),
            jsonPath("$.content.[0].classroom.floor").doesNotExist(),
            jsonPath("$.content.[0].classroom.doorCode").doesNotExist(),
            jsonPath("$.content.[0].cars..id").doesNotExist(),
            jsonPath("$.content.[0].cars..brand").doesNotExist(),
            jsonPath("$.content.[0].cars..licensePlate").doesNotExist(),
            jsonPath("$.content.[0].courses.[*].id").doesNotExist(),
            jsonPath("$.content.[0].courses.[*].credits").doesNotExist(),
            jsonPath("$.content.[0].courses.[*].name").doesNotExist());
  }
}
