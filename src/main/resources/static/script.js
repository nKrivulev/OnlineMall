const API = "http://localhost:9000/api";

async function getCurrentUser() {
    try {
        const res = await fetch(`${API}/users/me`);
        if (res.ok) return await res.json();
    } catch (e) {
        console.error("Auth error", e);
    }
    return null;
}

async function ensureAuthenticated() {
    const user = await getCurrentUser();
    return !!user;
}

function logout() {
    fetch(`${API}/auth/logout`, { method: "POST" });
    localStorage.clear();
    window.location.href = "index.html";
}

// Зареждане на navbar при нужда
document.addEventListener("DOMContentLoaded", () => {
    const navbarContainer = document.getElementById("navbar-container") || document.getElementById("navbar-placeholder");
    if (navbarContainer) {
        fetch("navbar.html")
            .then(res => res.text())
            .then(html => {
                navbarContainer.innerHTML = html;
            });
    }
});
