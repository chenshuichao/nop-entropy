package io.nop.record.reader;

import java.io.Closeable;
import java.io.IOException;

public interface IDataReaderBase extends Closeable {
    boolean isEof() throws IOException;

    long pos() throws IOException;

    IDataReaderBase subInput(long maxLength) throws IOException;
}
