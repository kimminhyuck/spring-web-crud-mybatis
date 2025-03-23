package com.myproject.model;

/**
 * User 모델 클래스
 * - MyBatis의 매핑 대상이 되는 POJO로, 사용자 정보를 담습니다.
 * - 시큐리티 및 JWT 관련 처리를 위해 필요한 기본 정보를 포함합니다.
 */
public class User {
    
    // 데이터베이스 기본키 (PK)
    private Long id;
    
    // 로그인 시 사용할 사용자 아이디
    private String username;
    
    // 사용자 이름
    private String name;
    
    // 사용자 이메일
    private String email;
    
    // 암호화되어 저장되어야 하는 비밀번호
    private String password;
    
    // 카카오 주소 목록 API 등을 통해 가져올 주소 정보
    private String address;
    
    // 사용자 권한 정보 (ADMIN, USER)
    private Role role;

    // 기본 생성자
    public User() {}

    // 전체 필드를 포함한 생성자
    public User(Long id, String username, String name, String email, String password, String address, Role role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
    }

    // Getter 및 Setter 메서드
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    
    // toString() 메서드 (디버깅 용도)
    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", username='" + username + '\'' +
               ", name='" + name + '\'' +
               ", email='" + email + '\'' +
               ", password='" + password + '\'' +
               ", address='" + address + '\'' +
               ", role=" + role +
               '}';
    }
}
