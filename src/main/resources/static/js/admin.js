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

    const roleIds = roles
        .replace('[', '')
        .replace(']', '')
        .split(',')
        .map(id => id.trim());
    const roleSelect = document.getElementById('editRoles');
    for (let option of roleSelect.options) {
        option.selected = false;
    }
    for (let option of roleSelect.options) {
        if (roleIds.includes(option.value)) {
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


    document.getElementById('deleteName').value = name;
    document.getElementById('deleteAge').value = age;
    document.getElementById('deleteEmail').value = email;
    document.getElementById('deleteLogin').value = login;
    document.getElementById('deleteUserForm').action =
        "/admin/delete/" + id;
});

document.addEventListener("DOMContentLoaded", function () {

    console.log("Загрузка USER PANEL");

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
});

