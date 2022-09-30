/*
 * Copyright 2017-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package example.test.logback;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import example.test.logback.appender.TestAppender;

/**
 * Integration Tests for Logback {@link Logger} asserting the proper {@link String log message} are logged
 * based on the configured {@link Level log level}.
 *
 * @author John Blum
 * @see org.junit.jupiter.api.Test
 * @see org.slf4j.Logger
 * @see ch.qos.logback.classic.Level
 * @since 0.1.0
 */
public class LogbackLoggingIntegrationTests {

	private final Logger testLogger = LoggerFactory.getLogger("test.logger");

	private final TestAppender testAppender = TestAppender.getInstance();

	@BeforeEach
	public void logMessagesBeforeTests() {

		this.testLogger.error("ERROR TEST");
		this.testLogger.warn("WARN TEST");
		this.testLogger.info("INFO TEST");
		this.testLogger.debug("DEBUG TEST");
	}

	@Test
	public void loggerLoggingIsCorrect() {

		assertThat(this.testAppender.lastLogMessage()).isEqualTo("INFO TEST");
		assertThat(this.testAppender.lastLogMessage()).isEqualTo("WARN TEST");
		assertThat(this.testAppender.lastLogMessage()).isEqualTo("ERROR TEST");
	}
}
