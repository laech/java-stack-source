package stack.source.junit4;

import org.junit.ComparisonFailure;
import stack.source.internal.Decorator;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UncheckedIOException;

final class DecoratedComparisonFailure extends ComparisonFailure {

    /*
     * Decorates an original ComparisonFailure, by being a ComparisonFailure
     * as well this just works with existing IDEs features such as those in
     * IntelliJ. IntelliJ provides diff like viewer when test are failed
     * due to comparison failures, it's quite useful.
     */

    DecoratedComparisonFailure(ComparisonFailure src) {
        super(src.toString(), src.getExpected(), src.getActual());
        initCause(src.getCause());
        setStackTrace(src.getStackTrace());
        for (Throwable sp : src.getSuppressed()) {
            addSuppressed(sp);
        }
    }

    @Override
    public void printStackTrace(PrintStream s) {
        try {
            new Decorator(this).print(s);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        try {
            new Decorator(this).print(s);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
