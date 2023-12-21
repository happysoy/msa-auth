package auth.jwt.repository;

import auth.jwt.domain.user.User;
import auth.jwt.domain.user.Role;
import auth.jwt.dto.response.UserInfoResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
//    Page<Member> findAllBy(final Pageable pageable); // List가 아닌 Page 사용

    Long countByRole(Role role);

    List<UserInfoResponse> findAllByRole(Role role); //TODO Page 로 바꾸기

//    Page<MemberDetailResponse> getMembersByPage(Pageable pageable);
//
//    Page<Member> findAll(Pageable pageable);
}
