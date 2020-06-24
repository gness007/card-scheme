package ng.com.gness.cardscheme.repository;

import ng.com.gness.cardscheme.models.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends PagingAndSortingRepository<Card,Long> {
    @Query("SELECT DISTINCT a.iin FROM Card a")
    Page<String> findDistinctIin(Pageable pageable);

    long countDistinctByIin(String iin);
}
