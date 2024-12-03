package com.konkuk.chapterkeep.bookRecommend.repository;

import com.konkuk.chapterkeep.domain.KonkukBookList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KonkukBookListRepository extends JpaRepository<KonkukBookList, Long> {

    List<KonkukBookList> findTop3ByOrderByRentalCountDesc();
}
