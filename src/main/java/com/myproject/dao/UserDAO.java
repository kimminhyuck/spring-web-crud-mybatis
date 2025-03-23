package com.myproject.dao;

import com.myproject.model.User;

/**
 * UserDAO 인터페이스
 * ────────────────────────────────
 * - MyBatis를 활용하여 사용자(User) 관련 CRUD 작업을 수행하는 DAO 인터페이스입니다.
 * - SQL은 XML 매퍼(UserDAO.xml)에서 관리됩니다.
 */
public interface UserDAO {

    /**
     * 사용자 이름(username)을 기준으로 사용자 정보를 조회합니다.
     *
     * @param username 조회할 사용자 이름
     * @return 해당 사용자의 정보를 담은 User 객체 (존재하지 않을 경우 null)
     */
    User findByUsername(String username);

    /**
     * 새로운 사용자를 등록합니다.
     *
     * @param user 등록할 사용자 정보를 담은 User 객체
     * @return 삽입된 레코드 수 (정상적으로 삽입되면 1)
     */
    int insertUser(User user);

    /**
     * 기존 사용자 정보를 수정합니다.
     *
     * @param user 수정할 사용자 정보가 담긴 User 객체 (id 필드는 필수)
     * @return 수정된 레코드 수
     */
    int updateUser(User user);

    /**
     * 사용자 ID를 기준으로 사용자를 삭제합니다.
     *
     * @param id 삭제할 사용자의 ID
     * @return 삭제된 레코드 수
     */
    int deleteUser(Long id);

    /**
     * 이메일을 기준으로 사용자 정보를 조회합니다.
     *
     * @param email 조회할 이메일
     * @return 해당 사용자의 정보를 담은 User 객체 (존재하지 않을 경우 null)
     */
    User findByEmail(String email);

    /**
     * 사용자 ID를 기준으로 사용자 정보를 조회합니다.
     *
     * @param id 조회할 사용자의 ID
     * @return 해당 사용자의 정보를 담은 User 객체 (존재하지 않을 경우 null)
     */
    User findById(Long id);
}
