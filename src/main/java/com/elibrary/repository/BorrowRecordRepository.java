package com.elibrary.repository;

import com.elibrary.model.BorrowRecord;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByUserIdAndStatusOrderByBorrowedOnDesc(Long userId, BorrowRecord.Status status);
    Optional<BorrowRecord> findFirstByBookIdAndStatusOrderByBorrowedOnDesc(Long bookId, BorrowRecord.Status status);
}
