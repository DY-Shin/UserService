//package com.ssafy.commonpjt.db.entity;
//
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.time.LocalDate;
//import java.time.LocalTime;
//
//@Data
//@Entity
//@NoArgsConstructor  // (access = AccessLevel.PROTECTED)
//public class Reserve {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "reserve_index")
//    private Long id;
//
////    @ManyToOne(fetch = FetchType.LAZY)
////    @JoinColumn(name = "chat_index")
//    // private ChatRoom chatIndex;
//    @Column
//    private Long chatIndex;
//
//    @Column(nullable = false)
//    private LocalDate reserveDate;
//
//    @Column(nullable = false)
//    private LocalTime reserveTime;
//
//    @Builder
//    // public Reserve(ChatRoom chatIndex, LocalDate reserveDate, LocalTime reserveTime) {
//    public Reserve(Long chatIndex, LocalDate reserveDate, LocalTime reserveTime) {
//        this.chatIndex = chatIndex;
//        this.reserveDate = reserveDate;
//        this.reserveTime = reserveTime;
//    }
//}
