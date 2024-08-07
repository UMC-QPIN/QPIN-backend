package org.example.qpin.domain.safephonenumber.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.qpin.domain.member.entity.Member;
import org.example.qpin.domain.qr.entity.Qr;
import org.example.qpin.global.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SafePhoneNumber extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long safePhoneNumberId;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String phoneNum;

    @OneToMany(mappedBy = "safePhoneNumber",cascade = CascadeType.ALL)
    private List<Qr> qrList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
