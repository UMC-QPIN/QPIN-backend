package org.example.qpin.domain.qr.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.qpin.domain.member.entity.Member;
import org.example.qpin.domain.qr.dto.request.CreateQrRequestDto;
import org.example.qpin.domain.qr.dto.request.ModifyQrRequestDto;
import org.example.qpin.domain.qr.dto.response.CheckQrDto;
import org.example.qpin.domain.qr.entity.Qr;
import org.example.qpin.domain.safephonenumber.entity.SafePhoneNumber;
import org.example.qpin.global.common.repository.MemberRepository;
import org.example.qpin.global.common.repository.QrRepository;
import org.example.qpin.global.common.repository.SafePhoneNumberRepository;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QrService {

    private final QrRepository qrRepository;
    private final MemberRepository memberRepository;
    private final SafePhoneNumberRepository safePhoneNumberRepository;

    @Transactional
    public Qr createQr(SafePhoneNumber safePhoneNumber, CreateQrRequestDto request) throws WriterException, IOException{
        Member member = memberRepository.findById(request.getMemberId()).orElseThrow();
        Qr qr = new Qr(member, request.getMemo(), request.getMyColor(), request.getSticker(),
                request.getGradation(), safePhoneNumber);
        qrRepository.save(qr);

        String qrUrl = "http://localhost:8080/qr/"+ qr.getQrId();
        qr.setQrUrl(qrUrl);

        byte[] qrImage = generateQRCodeImage(qrUrl);
        qr.setQrImage(qrImage);

        qrRepository.save(qr);
        return qr;
    }

    @Transactional
    public Qr findById(Long qrId){
        return qrRepository.findById(qrId).orElseThrow();
    }


    public byte[] generateQRCodeImage(String qrUrl) throws WriterException, IOException {

            int width = 200, height = 200;

            // QR코드 생성 옵션 설정
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.MARGIN, 0);
            hintMap.put(EncodeHintType.CHARACTER_SET,"UTF-8");

            // QR 코드 생성
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrUrl, BarcodeFormat.QR_CODE, width, height, hintMap);

            // QR 코드 이미지 생성
            BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // QR 코드 이미지를 바이트 배열로 변환, byteArrayOutputStream 에 저장
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(qrCodeImage,"png", byteArrayOutputStream);
            byteArrayOutputStream.flush();

            byte[] qrCodeBytes = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();

            return qrCodeBytes;
    }

    @Transactional
    public CheckQrDto toCheckQrDto(Long qrId) {
        Qr qr = qrRepository.findById(qrId).orElseThrow();
        CheckQrDto checkQrDto = new CheckQrDto(qr.getSafePhoneNumber().getSafePhoneNumber(),qr.getMemo(),
                qr.getMyColor(),qr.getSticker(),qr.getGradation(),qr.getQrImage());
        return  checkQrDto;
    }

    @Transactional
    public List<Qr> findAllByMember(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow();
        return qrRepository.findAllByMember(member);
    }

    @Transactional
    public List<CheckQrDto> toCheckQrDtoList(Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow();

        List<Qr> qrList = qrRepository.findAllByMember(member);
        List<CheckQrDto> checkQrDtoList = qrList.stream()
                .map(qr -> new CheckQrDto(
                        qr.getSafePhoneNumber().getSafePhoneNumber(),
                        qr.getMemo(),
                        qr.getMyColor(),
                        qr.getSticker(),
                        qr.getGradation(),
                        qr.getQrImage()
                ))
                .collect(Collectors.toList());

        return checkQrDtoList;
    }

    public void removeQr(Long qrId) {
        qrRepository.deleteById(qrId);
    }

    public void modifyQr(Long qrId, ModifyQrRequestDto request) {
        SafePhoneNumber safePhoneNumber = safePhoneNumberRepository.findByPhoneNum(request.getPhoneNum());

        Qr qr = qrRepository.findById(qrId).orElseThrow();
        qr.modifyQr(request.getMemo(), request.getMyColor(), request.getSticker(),
                    request.getGradation(), safePhoneNumber);

        qrRepository.save(qr);
    }
}
