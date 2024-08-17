package org.example.qpin.global.common.repository;

import org.example.qpin.domain.parking.entity.Parking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingRepository extends JpaRepository<Parking, Long> {
}
