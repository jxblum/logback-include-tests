package example.test.logback.appender;

import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AppenderBase;

/**
 * SLF4J / Logback {@link Appender} used for testing purposes capture {@link ILoggingEvent logging events},
 * such as {@link String log messages}.
 *
 * @author John Blum
 * @see ch.qos.logback.classic.spi.ILoggingEvent
 * @see ch.qos.logback.core.Appender
 * @see ch.qos.logback.core.AppenderBase
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class TestAppender extends AppenderBase<ILoggingEvent> {

	private static final AtomicReference<TestAppender> INSTANCE = new AtomicReference<>(null);

	private static final Stack<String> logMessages = new Stack<>();

	public static TestAppender getInstance() {
		return assertNotNull(INSTANCE.get());
	}

	private static TestAppender assertNotNull(TestAppender appender) {

		if (appender == null) {
			throw new IllegalStateException(String.format("[%s] was not properly configured",
				TestAppender.class.getName()));
		}

		return appender;
	}

	public TestAppender() {
		INSTANCE.compareAndSet(null, this);
	}

	@Override
	protected void append(ILoggingEvent event) {

		Optional.ofNullable(event)
			.map(ILoggingEvent::getFormattedMessage)
			.filter(this::hasText)
			.ifPresent(logMessages::push);
	}

	private boolean hasText(String text) {
		return text != null && !text.isBlank();
	}

	public String lastLogMessage() {

		synchronized (logMessages) {
			return logMessages.empty() ? null : logMessages.pop();
		}
	}

	public void clear() {
		logMessages.clear();
	}
}
