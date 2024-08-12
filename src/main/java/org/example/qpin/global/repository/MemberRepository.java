package org.example.qpin.global.repository;

import org.example.qpin.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
