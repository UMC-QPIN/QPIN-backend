package org.example.qpin.domain.parking.controller;

import lombok.RequiredArgsConstructor;
import org.example.qpin.domain.parking.dto.ParkingSearchReqDto;
import org.example.qpin.domain.parking.dto.ParkingSearchResDto;
import org.example.qpin.domain.parking.service.ParkingService;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;

    /**
     * 주변 주차장 정보 탐색
     */
    @GetMapping("/parking/selectList")
    public ResponseEntity<List<ParkingSearchResDto>> findParkingNearby(@RequestBody ParkingSearchReqDto parkingSearchReqDto) throws ParseException {

        double latitude=parkingSearchReqDto.getLatitude();
        double longitude=parkingSearchReqDto.getLongitude();
        double distance=parkingSearchReqDto.getDistance();
        String regionCode=parkingSearchReqDto.getRegionCode();
        return ResponseEntity.status(HttpStatus.OK).body(parkingService.findParkingNearby(latitude, longitude, distance,regionCode ));
    }
}
