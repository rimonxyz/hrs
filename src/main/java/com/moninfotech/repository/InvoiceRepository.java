package com.moninfotech.repository;

import com.moninfotech.domain.Booking;
import com.moninfotech.domain.Invoice;
import com.moninfotech.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sayemkcn on 6/7/17.
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long>{
    List<Invoice> findByUserAndPaid(User user,boolean paid);
    Invoice findByBooking(Booking booking);
}
