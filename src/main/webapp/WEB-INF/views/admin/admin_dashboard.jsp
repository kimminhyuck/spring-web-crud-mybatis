<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/sidebar.jsp" %>

<body>
    <div class="main-panel">
        <div class="main-header">
        </div>

        <div class="container">
            <div class="page-inner">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h3 class="fw-bold">회원 관리</h3>
                    <div class="d-flex">
                        <input type="text" id="searchInput" class="form-control me-2" placeholder="회원 검색">
                        <button class="btn btn-primary" onclick="searchUser()">검색</button>
                    </div>
                </div>

                <!-- 회원 목록 테이블 -->
                <div class="table-responsive">
                    <table class="table table-bordered align-middle">
                        <thead class="table-dark">
                            <tr>
                                <th>회원 ID</th>
                                <th>이름</th>
                                <th>이메일</th>
                                <th>주소</th>
                                <th>역할</th>
                                <th>관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="user" items="${userList}">
                                <tr>
                                    <td>${user.id}</td>
                                    <td>${user.name}</td>
                                    <td>${user.email}</td>
                                    <td>${user.address}</td>
                                    <td>${user.role}</td>
                                    <td>
                                        <button class="btn btn-info btn-sm" onclick="editUser(${user.id})">수정</button>
                                        <button class="btn btn-danger btn-sm" onclick="deleteUser(${user.id})">삭제</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- 페이징 -->
                <nav>
                    <ul class="pagination justify-content-center">
                        <c:if test="${pagination.currentPage > 1}">
                            <li class="page-item">
                                <a class="page-link" href="?page=${pagination.currentPage - 1}">이전</a>
                            </li>
                        </c:if>
                        <c:forEach begin="1" end="${pagination.totalPages}" var="i">
                            <li class="page-item ${pagination.currentPage == i ? 'active' : ''}">
                                <a class="page-link" href="?page=${i}">${i}</a>
                            </li>
                        </c:forEach>
                        <c:if test="${pagination.currentPage < pagination.totalPages}">
                            <li class="page-item">
                                <a class="page-link" href="?page=${pagination.currentPage + 1}">다음</a>
                            </li>
                        </c:if>
                    </ul>
                </nav>
            </div>
        </div>

        <%@ include file="/WEB-INF/views/common/footer.jsp" %>
    </div>

    <!-- 회원 수정 모달 -->
    <div class="modal fade" id="editUserModal" tabindex="-1" aria-labelledby="editUserModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editUserModalLabel">회원 정보 수정</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="editUserForm">
                        <input type="hidden" id="editUserId">
                        <div class="mb-3">
                            <label for="editUserName" class="form-label">이름</label>
                            <input type="text" class="form-control" id="editUserName" required>
                        </div>
                        <div class="mb-3">
                            <label for="editUserEmail" class="form-label">이메일</label>
                            <input type="email" class="form-control" id="editUserEmail" required>
                        </div>
                        <div class="mb-3">
                            <label for="editUserRole" class="form-label">역할</label>
                            <select class="form-control" id="editUserRole">
                                <option value="USER">USER</option>
                                <option value="ADMIN">ADMIN</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary">수정 완료</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- 자바스크립트 -->
    <script>
        function searchUser() {
            const keyword = document.getElementById('searchInput').value;
            window.location.href = '/admin/users?keyword=' + keyword;
        }

        function editUser(id) {
            // AJAX 요청을 통해 회원 정보를 가져와서 모달에 채움
            fetch(`/admin/users/${id}`)
                .then(response => response.json())
                .then(data => {
                    document.getElementById('editUserId').value = data.id;
                    document.getElementById('editUserName').value = data.name;
                    document.getElementById('editUserEmail').value = data.email;
                    document.getElementById('editUserRole').value = data.role;
                    new bootstrap.Modal(document.getElementById('editUserModal')).show();
                });
        }

        document.getElementById('editUserForm').addEventListener('submit', function(event) {
            event.preventDefault();
            const id = document.getElementById('editUserId').value;
            const name = document.getElementById('editUserName').value;
            const email = document.getElementById('editUserEmail').value;
            const role = document.getElementById('editUserRole').value;

            fetch(`/admin/users/update`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id, name, email, role })
            }).then(() => location.reload());
        });

        function deleteUser(id) {
            if (confirm('정말 삭제하시겠습니까?')) {
                fetch(`/admin/users/delete/${id}`, { method: 'POST' })
                    .then(() => location.reload());
            }
        }
    </script>
</body>
</html>
