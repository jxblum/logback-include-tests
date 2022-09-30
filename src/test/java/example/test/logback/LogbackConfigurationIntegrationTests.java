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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.Appender;

/**
 * Integration Tests for Logback Logging.
 *
 * @author John Blum
 * @see org.junit.jupiter.api.Test
 * @see org.slf4j.ILoggerFactory
 * @see org.slf4j.Logger
 * @see ch.qos.logback.classic.Logger
 * @see ch.qos.logback.classic.LoggerContext
 * @see ch.qos.logback.core.Appender
 * @since 0.1.0
 */
public class LogbackConfigurationIntegrationTests {

	private static final Class<ch.qos.logback.classic.Logger> LOGBACK_LOGGER_TYPE =
		ch.qos.logback.classic.Logger.class;

	private static final Function<? super ch.qos.logback.classic.Logger, Level> LOGBACK_LOGGER_LEVEL =
		ch.qos.logback.classic.Logger::getLevel;

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

	private ch.qos.logback.classic.Logger assertLogbackLogger(Logger slf4jLogger, String loggerName, Level logLevel) {

		assertThat(slf4jLogger).isInstanceOf(LOGBACK_LOGGER_TYPE);
		assertThat(slf4jLogger.getName()).isEqualTo(loggerName);
		assertThat(slf4jLogger)
			.asInstanceOf(InstanceOfAssertFactories.type(LOGBACK_LOGGER_TYPE))
			.extracting(LOGBACK_LOGGER_LEVEL)
			.isEqualTo(logLevel);

		return LOGBACK_LOGGER_TYPE.cast(slf4jLogger);
	}

	private void assertLogbackLoggerConfiguration(LoggerContext loggerContext, String... loggerNames) {

		List<String> configuredLoggerNames = nullSafeList(loggerContext.getLoggerList()).stream()
			.filter(Objects::nonNull)
			.map(Logger::getName)
			.filter(LogbackConfigurationIntegrationTests::hasText)
			.toList();

		assertThat(configuredLoggerNames).contains(loggerNames);
	}

	private void assertLoggerAppenderConfiguration(ch.qos.logback.classic.Logger logbackLogger,
			String... appenderNames) {

		Spliterator<Appender<?>> configuredTestLoggerAppenders =
			Spliterators.spliteratorUnknownSize(logbackLogger.iteratorForAppenders(), Spliterator.NONNULL);

		List<String> configuredTestLoggerAppenderNames =
			StreamSupport.stream(configuredTestLoggerAppenders, false)
				.filter(Objects::nonNull)
				.map(Appender::getName)
				.filter(LogbackConfigurationIntegrationTests::hasText)
				.toList();

		assertThat(configuredTestLoggerAppenderNames).contains(appenderNames);
	}

	private static boolean hasText(String value) {
		return value != null && !value.isBlank();
	}

	private static <T> List<T> nullSafeList(List<T> list) {
		return list != null ? list : Collections.emptyList();
	}
}
