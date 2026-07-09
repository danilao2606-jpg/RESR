document.addEventListener("DOMContentLoaded", function () {
    getCurrentAdmin();
    getAllUsers();
});


// CREATE USER
document.getElementById('formCreateNewUser')
    .addEventListener('submit', function (event) {
        event.preventDefault();
        const formData = new FormData(this);
        const rolesSelected = Array.from(
            document.getElementById('new_role').selectedOptions
        ).map(option => ({
            id: option.value,
            name: option.text
        }));
        let newUser = {
            name: formData.get('new_name'),
            age: formData.get('new_age'),
            email: formData.get('new_email'),
            login: formData.get('new_login'),
            password: formData.get('new_password'),
            roles: rolesSelected
        };
        fetch('/api/admin', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newUser)
        })
            .then(() => {
                getAllUsers();
                this.reset();
                document.getElementById('adminTable').click();
            });
    });

// CURRENT ADMIN
function getCurrentAdmin() {
    fetch("/api/currentAdmin")
        .then(response => response.json())
        .then(user => {
            const roles = user.roles
                .map(role => role.name.replace('ROLE_', ''))
                .join(', ');
            document.getElementById("adminEmail").textContent = user.email;
            document.getElementById("adminRole").textContent = roles;
            let tableCurrentAdmin = `
                <tr>
                    <td>${user.name}</td>
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${roles}</td>
                </tr>
            `;
            document.getElementById("adminInfo").innerHTML =
                tableCurrentAdmin;
        });
}

// ALL USERS
function getAllUsers() {
    fetch("/api/admin")
        .then(response => response.json())
        .then(users => {
            let tableUser = "";
            users.forEach(user => {
                const roles = user.roles
                    .map(role => role.name.replace('ROLE_', ''))
                    .join(', ');
                tableUser += `
                <tr id="${user.id}">
                    <td>${user.name}</td>
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${roles}</td>
                    <td>
                        <button 
                        type="button"
                        class="btn btn-info"
                        data-bs-toggle="modal"
                        data-bs-target="#modalEdit"
                        onclick="getModalEdit(${user.id})">
                            Edit
                        </button>
                    </td>
                    <td>
                        <button
                        type="button"
                        class="btn btn-danger"
                        data-bs-toggle="modal"
                        data-bs-target="#modalDelete"
                        onclick="getModalDelete(${user.id})">
                            Delete
                        </button>
                    </td>
                </tr>
                `;
            });
            document.getElementById("allUsers").innerHTML =
                tableUser;
        });
}

// DELETE MODAL
function getModalDelete(userId) {
    fetch(`/api/admin/${userId}`)
        .then(response => response.json())
        .then(user => {
            document.getElementById('deleteUserId').value =
                user.id;
            document.getElementById('deleteName').value =
                user.name;
            document.getElementById('deleteAge').value =
                user.age;
            document.getElementById('deleteEmail').value =
                user.email;
            document.getElementById('deleteLogin').value =
                user.login;
        });
}

// DELETE USER
document.getElementById('formDeleteUser')
    .addEventListener('submit', function(event){
        event.preventDefault();
        let idDeleteUser =
            document.getElementById('deleteUserId').value;
        fetch('/api/admin/' + idDeleteUser, {
            method: 'DELETE'
        })
            .then(() => {
                getAllUsers();
                document
                    .getElementById('closeDeleteUser')
                    .click();
            });
    });

// EDIT MODAL
function getModalEdit(userId) {
    fetch(`/api/admin/${userId}`)
        .then(response => response.json())
        .then(user => {
            document.getElementById('editUserId').value =
                user.id;
            document.getElementById('editName').value =
                user.name;
            document.getElementById('editAge').value =
                user.age;
            document.getElementById('editEmail').value =
                user.email;
            document.getElementById('editLogin').value =
                user.login;
        });
}

// UPDATE USER
document.getElementById('formEditUser')
    .addEventListener('submit', function(event){
        event.preventDefault();
        const formData = new FormData(this);
        const rolesSelected = Array.from(
            document.getElementById('editRoles').selectedOptions
        ).map(option => ({
            id: option.value,
            name: option.text
        }));
        let editUser = {
            id: formData.get('editUserId'),
            name: formData.get('editName'),
            age: formData.get('editAge'),
            email: formData.get('editEmail'),
            login: formData.get('editLogin'),
            password: formData.get('editPassword'),
            roles: rolesSelected
        };
        fetch('/api/admin', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(editUser)
        })
            .then(() => {
                getAllUsers();
                this.reset();
                document
                    .getElementById('closeEditUser')
                    .click();
            });
    });