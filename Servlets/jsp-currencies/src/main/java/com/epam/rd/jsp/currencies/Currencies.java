package com.epam.rd.jsp.currencies;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Currencies {
    private Map<String, BigDecimal> curs = new TreeMap<>();

    public void addCurrency(String currency, BigDecimal weight) {
        curs.put(currency, weight);
    }

    public Collection<String> getCurrencies() {
        return curs.keySet();
    }

    public Map<String, BigDecimal> getExchangeRates(String referenceCurrency) {
        return new TreeMap<>(curs.entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> curs.get(referenceCurrency).divide(entry.getValue(), 5, RoundingMode.HALF_UP))));
    }

    public BigDecimal convert(BigDecimal amount, String sourceCurrency, String targetCurrency) {
        return amount.multiply(curs.get(sourceCurrency)).divide(curs.get(targetCurrency), 5, RoundingMode.HALF_UP);
    }
}
