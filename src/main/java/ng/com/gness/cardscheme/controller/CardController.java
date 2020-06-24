package ng.com.gness.cardscheme.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ng.com.gness.cardscheme.pojo.CardPojo;
import ng.com.gness.cardscheme.pojo.CardScheme;
import ng.com.gness.cardscheme.services.CardService;
import ng.com.gness.cardscheme.services.KafkaMessageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "CardController")
@RestController
@RequestMapping("/api")
public class CardController {

    Logger logger = LoggerFactory.getLogger(CardController.class);

    @Value(value = "${message.topic.name}")
    private String topicName;

    @Autowired
    public CardController(RestTemplate restTemplate, CardService cardService, KafkaMessageService messageService) {
        this.restTemplate = restTemplate;
        this.cardService = cardService;
        this.messageService = messageService;
    }

    private RestTemplate restTemplate;
    private CardService cardService;
    private KafkaMessageService messageService;

    static final String baseApiUrl
            = "https://lookup.binlist.net/";

    @ApiOperation(value = "returns payload of card scheme")
    @GetMapping(value = "/card-scheme/verify/{cardNumber}",
            produces = {"application/json"})
    public Map<String, Object> verifyCard(@PathVariable String cardNumber) {
        String url
                = baseApiUrl + cardNumber;

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        map.put("success", false);
        map.put("payload", payload);

        try {
            ResponseEntity<CardScheme> response = restTemplate.getForEntity(url, CardScheme.class);
            CardScheme cardScheme = response.getBody();
            String type = cardScheme.getType();
            String brand = cardScheme.getBrand();
            String bank = cardScheme.getBank() != null ? cardScheme.getBank().getName() : null;
            String scheme = cardScheme.getScheme();
            String iin = cardNumber.substring(0, 6);

            //if card scheme and type are detected, cards is valid.
            boolean verified = (!StringUtils.isEmpty(scheme) && !StringUtils.isEmpty(type));
            if (verified) {
                CardPojo card = new CardPojo(scheme, type, brand, iin, bank, true);
                logger.info("send message to kafka topic\n");
                messageService.sendMessage(topicName, card);
                logger.info("card: " + scheme + " sent!");
            }
            payload.put("type", type);
            payload.put("bank", bank);
            payload.put("scheme", scheme);
            map.put("success", verified);
            map.put("payload", payload);
            return map;
        } catch (Exception e) {
            //log exception
            logger.error("Exception was caught \n" + e.getMessage());
        }
        return map;
    }

    @ApiOperation(value = "returns card scheme statistics")
    @GetMapping(value = "/card-scheme/stats",
            produces = {"application/json"})
    public Map<String, Object> cardSchemeStats(@RequestParam("start") int start,
                                               @RequestParam("limit") int limit) {
        Pageable pageable = PageRequest.of(start, limit);
        Map<String, Object> cardSchemeStats = cardService.findDistinctIin(pageable);
        return cardSchemeStats;
    }
}