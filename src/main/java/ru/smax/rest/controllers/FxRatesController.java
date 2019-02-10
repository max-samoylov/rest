package ru.smax.rest.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.smax.rest.model.FxRate;
import ru.smax.rest.service.FxRatesService;

import java.util.List;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api")
public class FxRatesController {
    private static final String RUB = "RUB";

    private final FxRatesService fxRatesService;

    @GetMapping("/rates")
    public List<FxRate> getRates() {
        return fxRatesService.getRates().entrySet().stream()
                .map(codeToRate -> FxRate.builder()
                        .base(codeToRate.getKey())
                        .quote(RUB)
                        .value(codeToRate.getValue())
                        .build())
                .collect(toList());
    }

    @GetMapping("/rates/{base}")
    public FxRate getRate(@PathVariable(name = "base") String baseCurrency,
                          @RequestParam(name = "quote", defaultValue = RUB) String quoteCurrency) {
        final String baseCode = baseCurrency.toUpperCase();
        final String quoteCode = quoteCurrency.toUpperCase();

        return FxRate.builder()
                .base(baseCode)
                .quote(quoteCode)
                .value(fxRatesService.getRate(baseCode, quoteCode))
                .build();
    }
}
