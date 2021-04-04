package de.hauke_stieler.geonotes.data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ResultTest {
    private Result success;
    private Result error;

    @Before
    public void before() {
        success = new Result.Success<>("test");
        error = new Result.Error(new Exception("Test Exception"));
    }

    @Test
    public void should_construct_success_result() {
        assertNotNull(success);
    }

    @Test
    public void should_return_success_string() {
        assertTrue(success.toString().startsWith("Success"));
    }

    @Test
    public void should_construct_error_result() {
        assertNotNull(error);
    }

    @Test
    public void should_return_error_string() {
        assertTrue(error.toString().startsWith("Error"));
    }
}
