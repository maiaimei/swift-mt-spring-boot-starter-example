package cn.maiaimei.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Currency;
import java.util.Locale;

@Slf4j
public class CurrencyTest {
    @Test
    void test() {
        Currency currency = Currency.getInstance(Locale.CANADA);
        log.info("{} {}", currency.getCurrencyCode(), currency.getDefaultFractionDigits());
    }
}
