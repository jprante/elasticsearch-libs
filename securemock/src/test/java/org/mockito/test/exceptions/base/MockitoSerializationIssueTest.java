/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.test.exceptions.base;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoSerializationIssue;
import org.mockito.test.configuration.ConfigurationAccess;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MockitoSerializationIssueTest {

    @Test
    public void should_filter_out_test_class_from_stacktrace_when_clean_flag_is_true() {
        // given
        ConfigurationAccess.getConfig().overrideCleansStackTrace(true);

        // when
        MockitoSerializationIssue issue = new MockitoSerializationIssue("msg", new Exception("cause"));

        // then
        assertThat(Arrays.toString(issue.getUnfilteredStackTrace())).contains("MockitoSerializationIssueTest");
        assertThat(Arrays.toString(issue.getStackTrace())).doesNotContain("MockitoSerializationIssueTest");
    }

    @Test
    public void should_keep_executing_class_in_stacktrace_when_clean_flag_is_false() {
        // given
        ConfigurationAccess.getConfig().overrideCleansStackTrace(false);

        // when
        MockitoSerializationIssue issue = new MockitoSerializationIssue("msg", new Exception("cause"));

        // then
        assertThat(Arrays.toString(issue.getUnfilteredStackTrace())).contains("MockitoSerializationIssueTest");
        assertThat(Arrays.toString(issue.getStackTrace())).contains("MockitoSerializationIssueTest");
    }
}
