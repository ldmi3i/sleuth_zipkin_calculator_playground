package cloud_calculator.calculator.utils;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class DefaultIdProvider implements IdProvider {
    private final AtomicLong idGen = new AtomicLong(0);

    @Override
    public Long getNext() {
        return idGen.getAndIncrement();
    }
}
