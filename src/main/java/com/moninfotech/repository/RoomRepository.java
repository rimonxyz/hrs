package com.moninfotech.repository;

import com.moninfotech.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by sayemkcn on 4/4/17.
 */
@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {
}
