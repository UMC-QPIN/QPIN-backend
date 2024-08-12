package org.example.qpin.global.common.repository;

import org.example.qpin.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(Long memberId);

    Member findByName(String name);

    Boolean existsByName(String name);
}
