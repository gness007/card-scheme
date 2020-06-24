package ng.com.gness.cardscheme.services;

import ng.com.gness.cardscheme.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CardService {

    private CardRepository cardRepository;
    @Autowired
    CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional
    public Map<String,Object> findDistinctIin(Pageable pageable) {
        Map<String, Object> cardSchemeStats = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        cardSchemeStats.put("success",false);
        Page<String> pageResult = cardRepository.findDistinctIin(pageable);
        pageable = pageResult.getPageable();
        int start = pageable.getPageNumber();
        int limit = pageable.getPageSize();
        List<String> contents = pageResult.getContent();

        pageResult.getSize();
        cardSchemeStats.put("size", pageResult.getTotalElements());
        cardSchemeStats.put("totalPages", (long)pageResult.getTotalPages());
        cardSchemeStats.put("start", (long)start);
        cardSchemeStats.put("limit", (long)limit);
        if (pageResult.getTotalElements() > 0) {
            cardSchemeStats.put("success",true);
        }

        contents.forEach(schemeIIn -> {
            long count = cardRepository.countDistinctByIin(schemeIIn);
            payload.put(schemeIIn, count);
        });
        cardSchemeStats.put("payload", payload);
        return cardSchemeStats;
    }
}
