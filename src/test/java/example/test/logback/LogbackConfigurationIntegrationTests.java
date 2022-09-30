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

import org.junit.jupiter.api.Test;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

/**
 * Integration Tests for Logback Configuration.
 *
 * @author John Blum
 * @see org.junit.jupiter.api.Test
 * @see org.slf4j.ILoggerFactory
 * @see org.slf4j.Logger
 * @see ch.qos.logback.classic.Logger
 * @see ch.qos.logback.classic.LoggerContext
 * @see ch.qos.logback.core.Appender
 * @see example.test.logback.AbstractLogbackIntegrationTests
 * @since 0.1.0
 */
public class LogbackConfigurationIntegrationTests extends AbstractLogbackIntegrationTests {

	@Test
	public void logbackConfigurationIsCorrect() {

		ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();

		assertThat(loggerFactory).isInstanceOf(LoggerContext.class);

		LoggerContext loggerContext = (LoggerContext) loggerFactory;

		assertLogbackLoggerConfiguration(loggerContext, "test.logger");

		Logger testLogger = loggerContext.getLogger("test.logger");

		ch.qos.logback.classic.Logger logbackTestLogger =
			assertLogbackLogger(testLogger, "test.logger", Level.INFO);

		assertLoggerAppenderConfiguration(logbackTestLogger, "CONSOLE", "TestAppender");
	}
}
