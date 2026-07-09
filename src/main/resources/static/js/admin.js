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