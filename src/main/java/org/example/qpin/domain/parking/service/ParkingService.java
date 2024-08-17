package org.example.qpin.domain.parking.service;

import lombok.RequiredArgsConstructor;
import org.example.qpin.domain.parking.dto.ParkingSearchResDto;
import org.example.qpin.global.common.repository.ParkingRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingService {

    private final ParkingRepository parkingRepository;

    //latitude: 위도, longitude: 경도

    /**
     * 위도와 경도, 거리, 지역 코드를 입력하면
     * 해당 위치로부터 입력한 거리 이내의 주차장의 정보를 반환함.
     */
    public List<ParkingSearchResDto> findParkingNearby(double mylatitude, double mylongitude, double distance, String regionCode) throws ParseException {
        final int page=1;
        final int perPage=150;
        final String DECODING_KEY="yncOh3M5FtqbW1UwmQmkBKpkkyYqZMj1FddwHcalnFzVCFtnlwkDOhRPFHkhnJPRKYy4scMVfbJMxn954Ym/Eg=="; //키 암호화 필요
        final String API_URL="https://api.odcloud.kr/api/15050093/v1/uddi:d19c8e21-4445-43fe-b2a6-865dff832e08?"
                +"page="        + page
                +"&perPage="    + perPage
                +"&cond%5B%EC%A7%80%EC%97%AD%EC%BD%94%EB%93%9C%3A%3AEQ%5D=" + regionCode
                +"&serviceKey=" + DECODING_KEY;

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(API_URL);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        WebClient webClient=WebClient
                .builder()
                .uriBuilderFactory(factory)
                .baseUrl(API_URL)
                .build();

        String response=webClient.get()
                .retrieve()
                .bodyToMono(String.class)
                .block();


        JSONParser jsonParser=new JSONParser();
        JSONObject jsonObject=(JSONObject) jsonParser.parse(response); //예외처리?
        JSONArray dataList=(JSONArray) jsonObject.get("data");

        List<ParkingSearchResDto> parkingSearchResDtoList =new ArrayList<>();
        for(int i=0; i<dataList.size(); i++){
            JSONObject data=(JSONObject) dataList.get(i); //해당되는 주차장이 없으면 null예외처리 해야함. 500에러 발생.
            double latitude=Double.parseDouble((String) data.get("위도"));
            double longitude=Double.parseDouble((String) data.get("경도"));
            /**
             * 설정한 거리보다 가까운 주차장만 리스트에 추가
             */
            if(distance>=distance(mylatitude, mylongitude, latitude, longitude)){
                ParkingSearchResDto parkingSearchResDto =new ParkingSearchResDto().builder()
                        .latitude(latitude)
                        .longitude(longitude)
                        .parkId(Long.parseLong((String) data.get("주차장관리번호")))
                        .name((String) data.get("주차장명"))
                        .address((String) data.get("주차장도로명주소"))
                        .price((String) data.get("요금정보"))
                        .build();
                parkingSearchResDtoList.add(parkingSearchResDto);
            }
        }

        return parkingSearchResDtoList;

    }

    // 두 좌표 사이의 거리를 구하는 함수
    // dsitance(첫번쨰 좌표의 위도, 첫번째 좌표의 경도, 두번째 좌표의 위도, 두번째 좌표의 경도)
    private static double distance(double lat1, double lon1, double lat2, double lon2){
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))* Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))*Math.cos(deg2rad(lat2))*Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60*1.1515*1609.344;

        return dist; //단위 meter
    }

    //10진수를 radian(라디안)으로 변환
    private static double deg2rad(double deg){
        return (deg * Math.PI/180.0);
    }
    //radian(라디안)을 10진수로 변환
    private static double rad2deg(double rad){
        return (rad * 180 / Math.PI);
    }

}
