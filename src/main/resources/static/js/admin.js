document.addEventListener("DOMContentLoaded", function () {

    console.log("admin.js подключен");

    const editModal = document.getElementById('editModal');

    editModal.addEventListener('show.bs.modal', function (event) {
        console.log("Окно редактирования открывается");
        const button = event.relatedTarget;
        const id = button.getAttribute('data-id');
        const name = button.getAttribute('data-name');
        const age = button.getAttribute('data-age');
        const email = button.getAttribute('data-email');
        const login = button.getAttribute('data-login');
        const roles = button.getAttribute('data-roles');

        document.getElementById('editName').value = name;
        document.getElementById('editAge').value = age;
        document.getElementById('editEmail').value = email;
        document.getElementById('editLogin').value = login;
        document.getElementById("editId").value = id;
        document.getElementById('editForm').action =
            "/admin/admin-update/" + id;

        const roleNames = roles.split(",");
        const roleSelect = document.getElementById('editRoles');
        for (let option of roleSelect.options) {
            option.selected = false;
        }
        for (let option of roleSelect.options) {
            if (roleNames.includes(option.text)) {
                option.selected = true;
            }
        }
        document.getElementById('editForm').action =
            "/admin/admin-update/" + id;
    });

    const deleteModal = document.getElementById('deleteModal');

    deleteModal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        const id = button.getAttribute('data-id');
        const name = button.getAttribute('data-name');
        const age = button.getAttribute('data-age');
        const email = button.getAttribute('data-email');
        const login = button.getAttribute('data-login');

        document.getElementById('deleteUserId').value = id;
        document.getElementById('deleteName').value = name;
        document.getElementById('deleteAge').value = age;
        document.getElementById('deleteEmail').value = email;
        document.getElementById('deleteLogin').value = login;

    });

        console.log(
            "saveEditBtn:",
            document.getElementById("saveEditBtn")
        );

        fetch("/admin/api/user")
            .then(response => response.json())
            .then(user => {

                console.log("Получен пользователь:", user);

                const roles = user.roles.join(" ");
                const row = `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${roles}</td>
                </tr>
            `;
                console.log("Нашёл таблицу:", document.getElementById("adminInfo"));
                document.getElementById("adminInfo").innerHTML = row;
            });


        function loadUsers() {
            fetch("/api/users")
                .then(response => response.json())
                .then(users => {
                    const tableBody = document.getElementById("usersTableBody");
                    tableBody.innerHTML = "";
                    users.forEach(user => {
                        const roles = user.roles
                            .map(role => role.replace("ROLE_", ""))
                            .join(" ");
                        tableBody.innerHTML += `
                        <tr>
                            <td>${user.name}</td>
                            <td>${user.age}</td>
                            <td>${user.email}</td>
                            <td>${roles}</td>
                            <td>
                                <button class="btn btn-info"
                                        data-bs-toggle="modal"
                                        data-bs-target="#editModal"
                                        data-id="${user.id}"
                                        data-name="${user.name}"
                                        data-age="${user.age}"
                                        data-email="${user.email}"
                                        data-login="${user.login}"
                                        data-roles="${user.roles}">
                                    Edit
                                </button>
                            </td>
                            
                            <td>
                                <button class="btn btn-danger"
                                        data-bs-toggle="modal"
                                        data-bs-target="#deleteModal"
                                        data-id="${user.id}"
                                        data-name="${user.name}"
                                        data-age="${user.age}"
                                        data-email="${user.email}"
                                        data-login="${user.login}">
                                    Delete
                                </button>
                            </td>
                        </tr>
                    `;
                    });
                });
        }

        loadUsers();

    document.getElementById("saveUserBtn")
        .addEventListener("click", function () {
            console.log("КНОПКА СОЗДАНИЯ НАЖАТА");
            const roles = Array.from(
                document.getElementById("addRoles").selectedOptions
            )
                .map(option => Number(option.value));
            const user = {
                name: document.getElementById("addName").value,
                age: document.getElementById("addAge").value,
                email: document.getElementById("addEmail").value,
                login: document.getElementById("addLogin").value,
                password: document.getElementById("addPassword").value,
                roles: roles
            };

            console.log("Создаём пользователя:", user);
            fetch("/api/users", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(user)
            })
                .then(response => {
                    console.log("Статус:", response.status);
                    if (!response.ok) {
                        throw new Error("Ошибка создания пользователя");
                    }
                    return response.json();
                })
                .then(data => {
                    console.log("Создан пользователь:", data);
                    loadUsers();
                    document.getElementById("addUserForm").reset();
                    document.querySelector('[href="#userTable"]').click();
                })
                .catch(error => {
                    console.error(error);
                });
        });

        console.log(
            "Кнопка сохранения:",
            document.getElementById("saveEditBtn")
        );

        document.getElementById("saveEditBtn")
            .addEventListener("click", function () {
                alert("Клик работает");
                console.log("КНОПКА СОХРАНЕНИЯ НАЖАТА");
                const id = document.getElementById("editId").value;
                const roles = Array.from(
                    document.getElementById("editRoles").selectedOptions
                )
                    .map(option => Number(option.value));
                const user = {
                    name: document.getElementById("editName").value,
                    age: document.getElementById("editAge").value,
                    email: document.getElementById("editEmail").value,
                    login: document.getElementById("editLogin").value,
                    password: document.getElementById("editPassword").value,
                    roles: roles
                };
                console.log("Обновляем пользователя:", user);
                fetch(`/api/users/${id}`, {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(user)
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error("Ошибка обновления");
                        }
                        return response.json();
                    })
                    .then(data => {
                        console.log("Обновлено:", data);
                        const modal = bootstrap.Modal
                            .getInstance(document.getElementById("editModal"));
                        modal.hide();
                        loadUsers();
                    });
            });

        document.getElementById("confirmDeleteBtn")
            .addEventListener("click", function () {
                const id = document.getElementById("deleteUserId").value;
                fetch(`/api/users/${id}`, {
                    method: "DELETE"
                })
                    .then(response => {
                        if (response.ok) {
                            const modal = bootstrap.Modal
                                .getInstance(document.getElementById('deleteModal'));
                            modal.hide();
                            loadUsers();
                        }
                    });
            });
});
