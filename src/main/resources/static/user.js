document.addEventListener("DOMContentLoaded", () => {
    getCurrentUser();
});

function getCurrentUser() {

    fetch("/api/user")
        .then(response => response.json())
        .then(user => {
            const roles = user.roles
                .map(role => role.name.replace("ROLE_", ""))
                .join(" ");
            document.getElementById("userEmail").textContent = user.username;
            document.getElementById("userRole").textContent = roles;
            document.getElementById("userInfo").innerHTML = `
                <tr>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.age}</td>
                    <td>${roles}</td>
                </tr>
            `;
        });
}