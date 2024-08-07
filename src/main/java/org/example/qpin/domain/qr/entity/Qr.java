package org.example.qpin.domain.qr.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.qpin.domain.safephonenumber.entity.SafePhoneNumber;
import org.example.qpin.global.common.BaseEntity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Qr extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qrId;

    @Column
    private String memo;

    @Enumerated(value = EnumType.STRING)
    private MyColor myColor;

    @Enumerated(value = EnumType.STRING)
    private Sticker sticker;

    @Column
    private int gradation; //추후 검토 필요

    @Column
    private String background_picture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="safe_phone_number_id")
    private SafePhoneNumber safePhoneNumber;

}
