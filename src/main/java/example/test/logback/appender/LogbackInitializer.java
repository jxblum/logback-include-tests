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
package example.test.logback.appender;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * Initializer to initialize Logback programmatically at runtime.
 *
 * @author John Blum
 * @see ch.qos.logback.classic.LoggerContext
 * @since 1.0.0
 */
public abstract class LogbackInitializer {

	public static LoggerContext initialize(LoggerContext loggerContext) {

		try {
			loggerContext.stop();
			new ContextInitializer(loggerContext).autoConfig();
			return loggerContext;
		}
		catch (JoranException cause) {
			throw new RuntimeException("Failed to initialize Logback", cause);
		}
	}
}
