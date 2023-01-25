//package com.ssafy.commonpjt.api.service;
//
//import com.ssafy.commonpjt.api.dto.ReserveDto;
//import com.ssafy.commonpjt.api.dto.ResponseDto;
//import com.ssafy.commonpjt.db.entity.Reserve;
//import com.ssafy.commonpjt.db.repository.ReserveRepository;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@Service
//public class ReserveService {
//    ReserveRepository reserveRepository;
//    ResponseDto response;
//
//    public ResponseEntity<?> reservation(ReserveDto reserveDto) {
//        Reserve reserve = Reserve.builder()
//                // 외래 키로 변경 필수
//                .chatIndex(1L)
//                .reserveDate(reserveDto.getReserveDate())
//                .reserveTime(reserveDto.getReserveTime())
//                .build();
//        reserveRepository.save(reserve);
//        return response.success();
//    }
//
//    @Transactional(readOnly = true)
//    public Optional<Reserve> getMyReserve(Long chatIndex) {
//        return reserveRepository.findByChatIndex(chatIndex);
//    }
//}
