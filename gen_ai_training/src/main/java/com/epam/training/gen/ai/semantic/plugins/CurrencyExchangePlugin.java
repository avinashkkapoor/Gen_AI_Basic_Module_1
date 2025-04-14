package com.epam.training.gen.ai.semantic.plugins;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
public class CurrencyExchangePlugin {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String EXCHANGE_URI_TEMPLATE =
            "https://fxds-public-exchange-rates-api.oanda.com/cc-api/currencies?base={sourceCurrency}&quote={targetCurrency}&data_type=chart&start_date={yesterday}&end_date={today}";

    private final RestTemplate exchangeClient = new RestTemplate();

    /**
     * Calculates the amount in target currency based on a source amount and currency pair.
     *
     * @param sourceAmount The amount of money in the source currency
     * @param sourceCode The source currency code (e.g., USD, GBP, EUR)
     * @param targetCode The target currency code (e.g., USD, GBP, EUR)
     * @return The converted amount in target currency as a string
     */
    @DefineKernelFunction(
            name = "exchange_currency_from",
            description = "Calculates the amount of money in the target currency based on the existing money"
    )
    public String exchangeCurrencyFrom(
            @KernelFunctionParameter(
                    description = "How much money user has in the source currency",
                    name = "source_amount"
            ) String sourceAmount,
            @KernelFunctionParameter(
                    description = "What currency user has? Source currency code, e.g. USD, GBP, RUR etc.",
                    name = "source_currency_code"
            ) String sourceCode,
            @KernelFunctionParameter(
                    description = "What currency user wants? Target currency code, e.g. USD, GBP, RUR etc.",
                    name = "target_currency_code"
            ) String targetCode) {
        log.info("Exchange currency from invoked!");
        BigDecimal source = new BigDecimal(sourceAmount);
        BigDecimal rate = callForExchangeRate(sourceCode, targetCode);
        return source.multiply(rate).toString();
    }

    /**
     * Calculates the amount in source currency needed to get a desired amount in target currency.
     *
     * @param targetAmount The desired amount in the target currency
     * @param sourceCode The source currency code (e.g., USD, GBP, EUR)
     * @param targetCode The target currency code (e.g., USD, GBP, EUR)
     * @return The required amount in source currency as a string
     */
    @DefineKernelFunction(
            name = "exchange_currency_to",
            description = "Calculates the amount of money user need based on how much user wants to get"
    )
    public String exchangeCurrencyTo(
            @KernelFunctionParameter(
                    description = "How much money user wants to get in the target currency",
                    name = "target_amount"
            ) String targetAmount,
            @KernelFunctionParameter(
                    description = "What currency user has? Source currency code, e.g. USD, GBP, RUR etc.",
                    name = "source_currency_code"
            ) String sourceCode,
            @KernelFunctionParameter(
                    description = "What currency user wants? Target currency code, e.g. USD, GBP, RUR etc.",
                    name = "target_currency_code"
            ) String targetCode) {
        log.info("Exchange currency to invoked!");
        BigDecimal target = new BigDecimal(targetAmount);
        BigDecimal rate = callForExchangeRate(sourceCode, targetCode);
        return target.divide(rate, 6, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * Retrieves the current exchange rate between two currencies from the OANDA API.
     *
     * @param sourceCurrency The source currency code
     * @param targetCurrency The target currency code
     * @return The exchange rate as a BigDecimal
     */
    @SuppressWarnings("unchecked")
    private BigDecimal callForExchangeRate(String sourceCurrency, String targetCurrency) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        Map<String, String> uriVariables = Map.of(
                "sourceCurrency", sourceCurrency,
                "targetCurrency", targetCurrency,
                "yesterday", yesterday.format(FORMATTER),
                "today", today.format(FORMATTER)
        );

        var response = exchangeClient.getForObject(
                EXCHANGE_URI_TEMPLATE,
                Map.class,
                uriVariables
        );

        String averageBid = ((List<Map<String, String>>) response.get("response")).getFirst().get("average_bid");
        log.info("Exchange rate {}/{}={}", sourceCurrency, targetCurrency, averageBid);
        return new BigDecimal(averageBid);
    }
}
