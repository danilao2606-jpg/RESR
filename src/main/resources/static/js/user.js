document.addEventListener("DOMContentLoaded", () => {

    fetch("/api/user")
        .then(response => response.json())
        .then(user => {
            const roles = user.roles
                .map(role => role.replace("ROLE_", ""))
                .join(" ");
            document.getElementById("headerUserEmail")
                .textContent = user.email;
            document.getElementById("headerUserRoles")
                .textContent = roles;
            document.getElementById("userTableBody").innerHTML = `
                <tr>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.age}</td>
                    <td>${roles}</td>
                </tr>
            `;
        });
});