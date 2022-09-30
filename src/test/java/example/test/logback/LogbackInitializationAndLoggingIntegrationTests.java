/*
 * Copyright 2022-present the original author or authors.
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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import example.test.logback.appender.LogbackInitializer;
import example.test.logback.appender.TestAppender;

/**
 * Integration Tests for Logback testing the configuration and {@link Logger} logging behavior
 * when Logback is programmatically re-initialized.
 *
 * @author John Blum
 * @see org.junit.jupiter.api.Test
 * @see org.slf4j.Logger
 * @see ch.qos.logback.classic.LoggerContext
 * @since 1.0.0
 */
public class LogbackInitializationAndLoggingIntegrationTests extends AbstractLogbackIntegrationTests {

	private static TestAppender testAppender;

	@BeforeAll
	public static void initializeLogback() {

		ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();

		assertThat(loggerFactory).isInstanceOf(LoggerContext.class);

		LoggerContext loggerContext = (LoggerContext) loggerFactory;

		LogbackInitializer.initialize(loggerContext);

		testAppender = TestAppender.getInstance();
	}

	private final Logger testLogger = LoggerFactory.getLogger("test.logger");

	@Test
	public void loggerConfigurationIsCorrect() {
		assertLoggerAppenderConfiguration(assertLogbackLogger(this.testLogger, "test.logger", Level.INFO),
			"TestAppender");
	}

	@Test
	public void loggerLoggingBehaviorIsCorrect() {

		this.testLogger.error("ERROR TEST");
		this.testLogger.warn("WARN TEST");
		this.testLogger.info("INFO TEST");
		this.testLogger.debug("DEBUG TEST");

		assertThat(testAppender.lastLogMessage()).isEqualTo("INFO TEST");
		assertThat(testAppender.lastLogMessage()).isEqualTo("WARN TEST");
		assertThat(testAppender.lastLogMessage()).isEqualTo("ERROR TEST");
		assertThat(testAppender.lastLogMessage()).isNull();
	}
}
