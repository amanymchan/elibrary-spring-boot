package com.elibrary.book;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    Optional<BorrowRecord> findFirstByBookIdAndStatusOrderByBorrowedOnDesc(
            Long bookId, BorrowRecord.Status status);
}
