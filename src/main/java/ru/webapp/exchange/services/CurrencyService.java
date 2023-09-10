package ru.webapp.exchange.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ru.webapp.exchange.models.Currency;
import ru.webapp.exchange.models.Expense;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CurrencyService {
    private Currency[] currencies;

    {
        this.currencies = this.updateExchange();
    }

    public Currency[] updateExchange() {
        try {
            URL url = new URL("https://api.currencyapi.com/v3/latest?" +
                    "apikey=cur_live_IGeMF0TL91kvPORCWzvk6Vi8kbsYHvDC7VRdcQdm&" +
                    "currencies=EUR%2CUSD%2CCNY%2CTRY%2CRUB&" +
                    "base_currency=RUB");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                Scanner sc = new Scanner(url.openStream());
                StringBuilder inline = new StringBuilder();
                while (sc.hasNext()) {
                    inline.append(sc.nextLine());
                }
                sc.close();
                conn.disconnect();

                this.currencies = parseJson(inline.toString());
                System.out.println(currencies[0]);
                return this.currencies;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (this.currencies == null) {
                return null;
            }
            return this.currencies;
        }
    }

    private Currency[] parseJson(String json) throws JsonProcessingException {
        String CNY = json.substring(json.indexOf("CNY") + 5, json.indexOf("EUR") - 2);
        String EUR = json.substring(json.indexOf("EUR") + 5, json.indexOf("RUB") - 2);
        String RUB = json.substring(json.indexOf("RUB") + 5, json.indexOf("TRY") - 2);
        String TRY = json.substring(json.indexOf("TRY") + 5, json.indexOf("USD") - 2);
        String USD = json.substring(json.indexOf("USD") + 5, json.length() - 2);

        ObjectMapper om = new ObjectMapper();
        Currency CnyCur = om.readValue(CNY, Currency.class);
        Currency EurCur = om.readValue(EUR, Currency.class);
        Currency RubCur = om.readValue(RUB, Currency.class);
        Currency TryCur = om.readValue(TRY, Currency.class);
        Currency UsdCur = om.readValue(USD, Currency.class);
        return new Currency[] { CnyCur, EurCur, RubCur, TryCur, UsdCur };
    }

    public List<Expense> switchToCurrency(List<Expense> initialList, String currency,
                                          Currency[] currencies) {
        if (Objects.equals(currency, "RUB")) return initialList;

        Optional<Currency> cur = Arrays.stream(currencies).
                filter(entry -> Objects.equals(entry.getCode(), currency))
                .findFirst();
        for (Expense entry : initialList) {
            entry.setTotal(entry.getTotal() * cur.get().getValue());
        }
        return initialList;
    }

    public Currency[] getCurrencies() {
        if (currencies == null) {
            currencies = updateExchange();
        }
        return currencies;
    }
}
