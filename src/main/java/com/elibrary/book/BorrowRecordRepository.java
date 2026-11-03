package com.elibrary.book;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByUserIdAndStatusOrderByBorrowedOnDesc(
            Long userId, BorrowRecord.Status status);

    java.util.Optional<BorrowRecord> findFirstByBookIdAndStatusOrderByBorrowedOnDesc(
            Long bookId, BorrowRecord.Status status);
}
